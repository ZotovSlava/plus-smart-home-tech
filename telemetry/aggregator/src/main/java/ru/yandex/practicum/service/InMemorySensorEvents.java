package ru.yandex.practicum.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class InMemorySensorEvents {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();

        log.info("▶Обработка события от сенсора {} хаба {}", sensorId, hubId);

        SensorsSnapshotAvro oldSnapshot = snapshots.get(hubId);

        if (oldSnapshot == null) {
            log.info("Снапшот для хаба {} не найден. Создаём новый.", hubId);
            return Optional.of(createNewSnapshot(event));
        }

        SensorStateAvro existingState = oldSnapshot.getSensorsState().get(sensorId);

        if (existingState != null) {
            log.info("Найдено предыдущее состояние сенсора {}: {}", sensorId, existingState);

            if (existingState.getTimestamp().compareTo(event.getTimestamp()) >= 0 ||
                    existingState.getData().equals(event.getPayload())) {
                log.info("⏸ Состояние не изменилось. Пропускаем обновление.");
                return Optional.empty();
            }
        }

        log.info("Обновляем состояние сенсора {}", sensorId);

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        Map<String, SensorStateAvro> newSensorsState = new HashMap<>(oldSnapshot.getSensorsState());
        newSensorsState.put(sensorId, newState);

        SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(event.getTimestamp())
                .setSensorsState(newSensorsState)
                .build();

        snapshots.put(hubId, newSnapshot);

        log.info("Новый снапшот: {}", newSnapshot);

        return Optional.of(newSnapshot);
    }

    private SensorsSnapshotAvro createNewSnapshot(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        Map<String, SensorStateAvro> sensorMap = new HashMap<>();
        sensorMap.put(sensorId, newState);

        SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(event.getTimestamp())
                .setSensorsState(sensorMap)
                .build();

        snapshots.put(hubId, snapshot);

        log.info("Новый снапшот создан: {}", snapshot);
        return snapshot;
    }
}
