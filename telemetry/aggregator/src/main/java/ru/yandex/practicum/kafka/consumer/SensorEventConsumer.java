package ru.yandex.practicum.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.service.AggregatorService;

@Component
public class SensorEventConsumer {

    private final AggregatorService aggregatorService;

    public SensorEventConsumer(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @KafkaListener(topics = "telemetry.sensors.v1", groupId = "aggregator-group")
    public void listen(SensorEventAvro event) {
        aggregatorService.processEvent(event);
    }
}