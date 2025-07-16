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

    private final Map<String, SensorsSnapshotAvro> snapshots;

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();

        log.info("▶ Обработка события от сенсора {} хаба {}", sensorId, hubId);

        SensorsSnapshotAvro oldSnapshot = snapshots.get(hubId);

        if (oldSnapshot == null) {
            log.info("Снапшот для хаба {} не найден. Создаём новый.", hubId);
            SensorsSnapshotAvro newSnapshot = createNewSnapshot(event);
            snapshots.put(hubId, newSnapshot);
            return Optional.of(newSnapshot);
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

        oldSnapshot.setTimestamp(event.getTimestamp());
        oldSnapshot.getSensorsState().put(sensorId, newState);

        log.info("Новый снапшот: {}", oldSnapshot);

        return Optional.of(oldSnapshot);
    }

    private SensorsSnapshotAvro createNewSnapshot(SensorEventAvro event) {
        Map<String, SensorStateAvro> sensorMap = new HashMap<>();
        sensorMap.put(event.getId(), SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build());

        SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(sensorMap)
                .build();

        log.info("Новый снапшот создан: {}", snapshot);
        return snapshot;
    }
}
