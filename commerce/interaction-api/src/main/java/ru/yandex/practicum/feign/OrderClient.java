package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoOrder.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order")
@RequestMapping("/api/v1/order")
public interface OrderClient {
    @GetMapping
    List<OrderDto> getAllUserOrder(@RequestParam String username);

    @PutMapping
    OrderDto create(@RequestBody CreateNewOrderRequest createNewOrderRequest);

    @PostMapping("/return")
    OrderDto returnProduct(@RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping("/payment")
    OrderDto processPayment(@RequestBody UUID orderID);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderID);

    @PostMapping("/delivery")
    OrderDto processDelivery(@RequestBody UUID orderID);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderID);

    @PostMapping("/completed")
    OrderDto processCompleted(@RequestBody UUID orderID);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody UUID orderID);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody UUID orderID);

    @PostMapping("/assembly")
    OrderDto collectOrder(@RequestBody UUID orderID);

    @PostMapping("/assembly/failed")
    OrderDto collectOrderFailed(@RequestBody UUID orderID);
}
