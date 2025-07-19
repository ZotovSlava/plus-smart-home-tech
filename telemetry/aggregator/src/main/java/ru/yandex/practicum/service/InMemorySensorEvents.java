package ru.yandex.practicum.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemorySensorEvents {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.info("Список снапшотов: {}", snapshots.keySet());

        String hubId = event.getHubId();
        String sensorId = event.getId();

        SensorsSnapshotAvro snapshot = snapshots.get(hubId);

        if (snapshot == null) {
            log.info("Cнапшот не найден, создаем новый");
            return Optional.of(addSnapshot(event));
        }

        log.info("Cнапшот найден: {}", hubId);

        SensorStateAvro existingState = snapshot.getSensorsState().get(sensorId);

        if (existingState != null) {
            log.info("предыдущее состояние найдено {}", existingState);
            log.info("сравнение времени: старого {} и нового {}", existingState.getTimestamp(), event.getTimestamp());

            String oldData = existingState.getData().toString();
            String newData = event.getPayload().toString();

            if (existingState.getTimestamp().isAfter(event.getTimestamp()) || oldData.equals(newData)) {
                log.info("предыдущее состояние не изменилось");
                return Optional.empty();
            }

            log.info("обновление состояния");
        } else {
            log.info("новое состояние для сенсора, добавляем");
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.getSensorsState().put(sensorId, newState);
        snapshot.setTimestamp(event.getTimestamp());

        log.info("обновленный снапшот: {}", snapshot);

        return Optional.of(snapshot);
    }

    private SensorsSnapshotAvro addSnapshot(SensorEventAvro event) {
        SensorStateAvro state = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        Map<String, SensorStateAvro> stateMap = new HashMap<>();
        stateMap.put(event.getId(), state);

        SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(stateMap)
                .build();

        snapshots.put(event.getHubId(), snapshot);

        log.info("создан новый снапшот: {}", snapshot);

        return snapshot;
    }
}
