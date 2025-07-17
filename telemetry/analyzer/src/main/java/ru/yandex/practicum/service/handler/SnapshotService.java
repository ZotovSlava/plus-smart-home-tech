package ru.yandex.practicum.service.handler;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.scenario.Scenario;
import ru.yandex.practicum.model.scenario.ScenarioAction;
import ru.yandex.practicum.model.scenario.ScenarioCondition;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SnapshotService {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotService(@GrpcClient("hub-router")
                           HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                           SensorRepository sensorRepository,
                           ScenarioRepository scenarioRepository) {
        this.hubRouterClient = hubRouterClient;
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public void handleRecord(SensorsSnapshotAvro record) {
        try {
            String hubId = record.getHubId();
            Map<String, SensorStateAvro> sensorsState = record.getSensorsState();
            List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

            for (Scenario scenario : scenarios) {
                String scenarioName = scenario.getName();
                List<Integer> switchExecution = new ArrayList<>();

                if (!sensorRepository.existsByIdInAndHubId(sensorsState.keySet(), hubId)) {
                    log.info("В базе нет соответствующего сенсора для данного хаба: {}", hubId);
                    continue;
                }

                for (ScenarioCondition scCondition : scenario.getConditions()) {
                    Condition condition = scCondition.getCondition();
                    String sensorId = scCondition.getSensor().getId();

                    SensorStateAvro state = sensorsState.get(sensorId);
                    if (state == null) {
                        switchExecution.add(0);
                        continue;
                    }

                    boolean matched = checkCondition(condition, state);
                    switchExecution.add(matched ? 1 : 0);
                }

                if (!switchExecution.isEmpty() && !switchExecution.contains(0)) {
                    for (ScenarioAction scAction : scenario.getActions()) {
                        Action action = scAction.getAction();
                        String sensorId = scAction.getSensor().getId();
                        sendAction(hubId, scenarioName, sensorId, action);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке записи snapshot", e);
        }
    }

    private boolean checkCondition(Condition condition, SensorStateAvro state) {
        String type = condition.getType();
        String operation = condition.getOperation();
        Long expectedValue = condition.getValue();

        switch (type) {
            case "SWITCH" -> {
                SwitchSensorEventAvro data = (SwitchSensorEventAvro) state.getData();
                return compareBooleanCondition(operation, expectedValue, data.getState());
            }
            case "MOTION" -> {
                MotionSensorEventAvro data = (MotionSensorEventAvro) state.getData();
                return compareBooleanCondition(operation, expectedValue, data.getMotion());
            }
            case "LUMINOSITY" -> {
                LightSensorEventAvro data = (LightSensorEventAvro) state.getData();
                return compareNumericCondition(operation, expectedValue, data.getLuminosity());
            }
            case "CO2LEVEL" -> {
                ClimateSensorEventAvro data = (ClimateSensorEventAvro) state.getData();
                return compareNumericCondition(operation, expectedValue, data.getCo2Level());
            }
            case "HUMIDITY" -> {
                ClimateSensorEventAvro data = (ClimateSensorEventAvro) state.getData();
                return compareNumericCondition(operation, expectedValue, data.getHumidity());
            }
            case "TEMPERATURE" -> {
                Object data = state.getData();
                if (data instanceof TemperatureSensorEventAvro tempData) {
                    return compareNumericCondition(operation, expectedValue, tempData.getTemperatureC());
                } else if (data instanceof ClimateSensorEventAvro climateData) {
                    return compareNumericCondition(operation, expectedValue, climateData.getTemperatureC());
                }
                return false;
            }
            default -> {
                log.warn("Неизвестный тип условия: {}", type);
                return false;
            }
        }
    }

    private boolean compareBooleanCondition(String operation, Long expectedValue, boolean actual) {
        if (!"EQUALS".equals(operation)) return false;
        return (expectedValue == 1 && actual) || (expectedValue == 0 && !actual);
    }

    private boolean compareNumericCondition(String operation, Long expectedValue, int actualValue) {
        return switch (operation) {
            case "EQUALS" -> expectedValue == actualValue;
            case "GREATER_THAN" -> expectedValue < actualValue;
            case "LOWER_THAN" -> expectedValue > actualValue;
            default -> false;
        };
    }

    private void sendAction(String hubId, String scenarioName, String sensorId, Action action) {
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(time.getEpochSecond())
                .setNanos(time.getNano())
                .build();

        DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                .setSensorId(sensorId)
                .setType(ActionTypeProto.valueOf(action.getType()));

        if ("SET_VALUE".equals(action.getType())) {
            actionBuilder.setIntValue(action.getValue().intValue());
        }

        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(actionBuilder.build())
                .setTimestamp(timestamp)
                .build();

        try {
            hubRouterClient.handleDeviceAction(request);
        } catch (Exception e) {
            log.error("Ошибка отправки действия для сенсора {}: {}", sensorId, e.getMessage());
        }
    }
}

