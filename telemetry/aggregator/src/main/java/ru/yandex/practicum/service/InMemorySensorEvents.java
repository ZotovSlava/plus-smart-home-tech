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

        SensorsSnapshotAvro snapshot = snapshots.get(hubId);

        if (snapshot == null) {
            log.info("Снапшот для хаба {} не найден. Создаём новый.", hubId);
            return Optional.of(createNewSnapshot(event));
        }

        SensorStateAvro existingState = snapshot.getSensorsState().get(sensorId);

        if (existingState != null) {
            log.info("Найдено предыдущее состояние сенсора {}: {}", sensorId, existingState);

            // Сравнение по времени и данным
            if (existingState.getTimestamp().compareTo(event.getTimestamp()) >= 0 ||
                    existingState.getData().toString().equals(event.getPayload().toString())) {
                log.info("⏸ Состояние не изменилось. Пропускаем обновление.");
                return Optional.empty();
            }
        }

        log.info("Обновляем состояние сенсора {}", sensorId);
        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.setTimestamp(event.getTimestamp());
        snapshot.getSensorsState().put(sensorId, newState);
        log.info("Новый снапшот: {}", snapshot);

        return Optional.of(snapshot);
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
