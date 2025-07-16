package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.producer.SensorEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AggregatorService {

    private final InMemorySensorEvents inMemorySensorEvents;
    private final SensorEventProducer sensorEventProducer;

    public void processEvent(SensorEventAvro event) {
        log.info("Получено новое событие: {}", event);

        Optional<SensorsSnapshotAvro> snapshot = inMemorySensorEvents.updateState(event);

        if (snapshot.isPresent()) {
            SensorsSnapshotAvro snapshotAvro = snapshot.get();
            log.info("Новый снапшот сформирован: {}", snapshotAvro);

            sensorEventProducer.send(event.getHubId(), snapshotAvro);
        } else {
            log.info("Снапшот не обновился.");
        }
    }
}

