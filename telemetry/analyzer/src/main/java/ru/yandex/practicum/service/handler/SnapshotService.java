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
        log.info("➡️ Обработка snapshot начата для хаба: {}", record.getHubId());

        try {
            String hubId = record.getHubId();

            Map<String, SensorStateAvro> sensorsState = record.getSensorsState();
            log.info("🔍 Количество состояний датчиков в snapshot: {}", sensorsState.size());

            List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
            log.info("📊 Найдено сценариев для хаба {}: {}", hubId, scenarios.size());

            if (scenarios.isEmpty()) {
                log.warn("⚠️ Нет сценариев для хаба {}", hubId);
            }

            for (Scenario scenario : scenarios) {
                String scenarioName = scenario.getName();
                log.info("🧠 Обработка сценария: {}", scenarioName);

                List<Integer> switchExecution = new ArrayList<>();

                // Проверка наличия сенсоров в БД
                boolean sensorsExist = sensorRepository.existsByIdInAndHubId(sensorsState.keySet(), hubId);
                log.info("🔎 Сенсоры из snapshot существуют в БД для хаба {}: {}", hubId, sensorsExist);
                if (!sensorsExist) {
                    log.warn("⚠️ Нет подходящих сенсоров в БД для хаба {}", hubId);
                    continue;
                }

                for (ScenarioCondition scCondition : scenario.getConditions()) {
                    Condition condition = scCondition.getCondition();
                    String sensorId = scCondition.getSensor().getId();

                    log.debug("🔍 Проверка условия для сенсора {}", sensorId);
                    SensorStateAvro state = sensorsState.get(sensorId);
                    if (state == null) {
                        log.warn("❌ Нет состояния для сенсора {} в snapshot", sensorId);
                        switchExecution.add(0);
                        continue;
                    }

                    boolean matched = checkCondition(condition, state);
                    log.debug("✅ Условие для сенсора {} выполнено: {}", sensorId, matched);
                    switchExecution.add(matched ? 1 : 0);
                }

                if (!switchExecution.isEmpty() && !switchExecution.contains(0)) {
                    log.info("✅ Все условия выполнены для сценария {}", scenarioName);
                    for (ScenarioAction scAction : scenario.getActions()) {
                        Action action = scAction.getAction();
                        String sensorId = scAction.getSensor().getId();
                        sendAction(hubId, scenarioName, sensorId, action);
                    }
                } else {
                    log.info("🚫 Условия не выполнены для сценария {}", scenarioName);
                }
            }

            log.info("➡️ Обработка snapshot завершена для хаба: {}", hubId);

        } catch (Exception e) {
            log.error("🛑 Ошибка при обработке snapshot для хаба {}", record.getHubId(), e);
        }
    }

    private boolean checkCondition(Condition condition, SensorStateAvro state) {
        String type = condition.getType();
        String operation = condition.getOperation();
        Long expectedValue = condition.getValue();

        log.trace("🔎 Проверка условия: тип={}, операция={}, значение={}", type, operation, expectedValue);

        try {
            return switch (type) {
                case "SWITCH" -> {
                    SwitchSensorEventAvro data = (SwitchSensorEventAvro) state.getData();
                    yield compareBooleanCondition(operation, expectedValue, data.getState());
                }
                case "MOTION" -> {
                    MotionSensorEventAvro data = (MotionSensorEventAvro) state.getData();
                    yield compareBooleanCondition(operation, expectedValue, data.getMotion());
                }
                case "LUMINOSITY" -> {
                    LightSensorEventAvro data = (LightSensorEventAvro) state.getData();
                    yield compareNumericCondition(operation, expectedValue, data.getLuminosity());
                }
                case "CO2LEVEL" -> {
                    ClimateSensorEventAvro data = (ClimateSensorEventAvro) state.getData();
                    yield compareNumericCondition(operation, expectedValue, data.getCo2Level());
                }
                case "HUMIDITY" -> {
                    ClimateSensorEventAvro data = (ClimateSensorEventAvro) state.getData();
                    yield compareNumericCondition(operation, expectedValue, data.getHumidity());
                }
                case "TEMPERATURE" -> {
                    Object data = state.getData();
                    if (data instanceof TemperatureSensorEventAvro tempData) {
                        yield compareNumericCondition(operation, expectedValue, tempData.getTemperatureC());
                    } else if (data instanceof ClimateSensorEventAvro climateData) {
                        yield compareNumericCondition(operation, expectedValue, climateData.getTemperatureC());
                    }
                    yield false;
                }
                default -> {
                    log.warn("❓ Неизвестный тип условия: {}", type);
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("🛑 Ошибка при проверке условия: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean compareBooleanCondition(String operation, Long expectedValue, boolean actual) {
        log.trace("⚖️ Сравнение булево: expected={}, actual={}, operation={}", expectedValue, actual, operation);
        return "EQUALS".equals(operation) && ((expectedValue == 1 && actual) || (expectedValue == 0 && !actual));
    }

    private boolean compareNumericCondition(String operation, Long expectedValue, int actualValue) {
        log.trace("⚖️ Сравнение чисел: expected={}, actual={}, operation={}", expectedValue, actualValue, operation);
        return switch (operation) {
            case "EQUALS" -> expectedValue == actualValue;
            case "GREATER_THAN" -> expectedValue < actualValue;
            case "LOWER_THAN" -> expectedValue > actualValue;
            default -> false;
        };
    }

    private void sendAction(String hubId, String scenarioName, String sensorId, Action action) {
        log.info("📤 Отправка действия: хаб={}, сценарий={}, сенсор={}, тип={}",
                hubId, scenarioName, sensorId, action.getType());

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
            log.trace("🛠 Установка значения: {}", action.getValue());
        }

        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(actionBuilder.build())
                .setTimestamp(timestamp)
                .build();

        try {
            hubRouterClient.handleDeviceAction(request);
            log.info("✅ Действие успешно отправлено!");
        } catch (Exception e) {
            log.error("🚨 Ошибка при отправке действия для сенсора {}: {}", sensorId, e.getMessage(), e);
        }
    }
}


