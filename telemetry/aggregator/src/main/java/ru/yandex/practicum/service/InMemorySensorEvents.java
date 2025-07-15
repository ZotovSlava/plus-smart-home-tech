package ru.yandex.practicum.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class InMemorySensorEvents {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();
        Instant eventTimestamp = event.getTimestamp();
        Object eventPayload = event.getPayload();

        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, id -> {
            SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setSensorsState(new HashMap<>())
                    .setTimestamp(eventTimestamp)
                    .build();
            return newSnapshot;
        });

        Map<String, SensorStateAvro> sensorStates = snapshot.getSensorsState();
        SensorStateAvro oldState = sensorStates.get(sensorId);

        if (oldState != null) {
            Instant oldTimestamp = oldState.getTimestamp();

            if (!eventTimestamp.isAfter(oldTimestamp)) {
                return Optional.empty();
            }

            String oldDataStr = oldState.getData() == null ? null : oldState.getData().toString();
            String newDataStr = eventPayload == null ? null : eventPayload.toString();
            if (Objects.equals(oldDataStr, newDataStr)) {
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(eventTimestamp)
                .setData(eventPayload)
                .build();

        sensorStates.put(sensorId, newState);

        SensorsSnapshotAvro updatedSnapshot = SensorsSnapshotAvro.newBuilder(snapshot)
                .setTimestamp(eventTimestamp)
                .setSensorsState(sensorStates)
                .build();

        snapshots.put(hubId, updatedSnapshot);

        return Optional.of(updatedSnapshot);
    }
}
