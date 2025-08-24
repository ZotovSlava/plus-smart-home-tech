package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryState;
import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoOrder.OrderState;
import ru.yandex.practicum.dto.dtoOrder.ProductReturnRequest;
import ru.yandex.practicum.dto.dtoPayment.PaymentDto;
import ru.yandex.practicum.dto.dtoWarehouse.AddressDto;
import ru.yandex.practicum.dto.dtoWarehouse.BookedProductsDto;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderAddress;
import ru.yandex.practicum.model.OrderItem;
import ru.yandex.practicum.storage.OrderAddressRepository;
import ru.yandex.practicum.storage.OrderItemRepository;
import ru.yandex.practicum.storage.OrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;
    private final OrderAddressRepository orderAddressRepository;

    @Override
    public List<OrderDto> getAllUserOrder(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Not Authorized User");
        }
        List<Order> orders = orderRepository.findAllByUsername(username);

        return orders.stream().map(OrderMapper::toDto2).collect(Collectors.toList());
    }

    @Override
    public OrderDto create(CreateNewOrderRequest createNewOrderRequest) {
        BookedProductsDto bookedProductsDto =
                warehouseClient.checkAvailabilityProduct(createNewOrderRequest.getShoppingCartDto());

        Order order = orderRepository
                .save(OrderMapper.toEntity(createNewOrderRequest, bookedProductsDto));

        Map<UUID, Integer> products = createNewOrderRequest.getShoppingCartDto().getProducts();

        products.forEach((productId, quantity) -> {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(productId)
                    .quantity(quantity)
                    .build();

            orderItemRepository.save(orderItem);
        });

        AddressDto warehouseAddress = warehouseClient.getAddress();
        AddressDto userAddress = createNewOrderRequest.getAddressDto();
        OrderAddress orderAddress = orderAddressRepository.save(AddressMapper.toEntity(userAddress));
        order.setOrderAddress(orderAddress);


        DeliveryDto deliveryDto = DeliveryDto.builder()
                .to(userAddress)
                .from(warehouseAddress)
                .OrderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build();

        DeliveryDto delivery = deliveryClient.create(deliveryDto);
        order.setDeliveryId(delivery.getDeliveryId());


        BigDecimal productCost = paymentClient.calculateProductCost(OrderMapper.toDto(order, createNewOrderRequest));
        BigDecimal deliveryCost = deliveryClient.calculateDeliveryCost(OrderMapper.toDto(order, createNewOrderRequest));

        order.setDeliveryPrice(deliveryCost);
        order.setProductPrice(productCost);

        BigDecimal totalCost = paymentClient.calculateTotalCost(OrderMapper.toDto(order, createNewOrderRequest));

        order.setTotalPrice(totalCost);

        PaymentDto paymentDto = paymentClient.processPayment(OrderMapper.toDto(order, createNewOrderRequest));

        order.setPaymentId(paymentDto.getPaymentId());

        return OrderMapper.toDto(orderRepository.save(order), createNewOrderRequest);
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest productReturnRequest) {
        Order order = orderRepository.findById(productReturnRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        List<OrderItem> products = new ArrayList<>(order.getProducts());

        Iterator<OrderItem> iterator = products.iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            Integer quantityReturn = productReturnRequest.getProducts().get(orderItem.getProductId());
            if (quantityReturn == null) continue;

            int oldQuantity = orderItem.getQuantity();
            int newQuantity = oldQuantity - quantityReturn;

            if (newQuantity <= 0) {
                iterator.remove();
            } else {
                orderItem.setQuantity(newQuantity);
            }
        }

        order.setProducts(products);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto processPayment(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.PAID);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentFailed(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.PAYMENT_FAILED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto processDelivery(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.DELIVERED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto deliveryFailed(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.DELIVERY_FAILED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto processCompleted(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.COMPLETED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateTotal(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        BigDecimal totalCost = paymentClient.calculateTotalCost(OrderMapper.toDto2(order));

        order.setTotalPrice(totalCost);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDelivery(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        BigDecimal totalCost = deliveryClient.calculateDeliveryCost(OrderMapper.toDto2(order));

        order.setDeliveryPrice(totalCost);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto collectOrder(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.ASSEMBLED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }

    @Override
    public OrderDto collectOrderFailed(UUID orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setState(OrderState.ASSEMBLY_FAILED);

        return OrderMapper.toDto2(orderRepository.save(order));
    }
}
