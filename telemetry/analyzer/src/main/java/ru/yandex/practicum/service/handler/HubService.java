package ru.yandex.practicum.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.model.scenario.*;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {

    private final SensorRepository sensorRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioRepository scenarioRepository;

    public void handleRecord(HubEventAvro record) {
        Object payload = record.getPayload();
        String hubId = record.getHubId();

        log.info("Обработка записи типа \"{}\" для хаба {}", payload.getClass().getSimpleName(), hubId);

        switch (payload) {
            case DeviceAddedEventAvro event -> {
                Sensor sensor = new Sensor();
                sensor.setId(event.getId());
                sensor.setHubId(hubId);
                sensorRepository.save(sensor);
                log.info("Добавлен сенсор: id={}, hubId={}", sensor.getId(), hubId);
            }

            case DeviceRemovedEventAvro event -> {
                sensorRepository.findByIdAndHubId(event.getId(), hubId)
                        .ifPresent(sensor -> {
                            sensorRepository.delete(sensor);
                            log.info("Удалён сенсор: id={}, hubId={}", sensor.getId(), hubId);
                        });
            }

            case ScenarioAddedEventAvro event -> {
                log.info("Добавление сценария '{}' для хаба {}", event.getName(), hubId);

                Scenario scenario = new Scenario();
                scenario.setHubId(hubId);
                scenario.setName(event.getName());
                scenarioRepository.save(scenario);

                List<ScenarioCondition> scenarioConditions = new ArrayList<>();
                List<ScenarioAction> scenarioActions = new ArrayList<>();

                for (ScenarioConditionAvro condAvro : event.getConditions()) {
                    Condition newCondition = new Condition();
                    newCondition.setType(condAvro.getType().name());
                    newCondition.setOperation(condAvro.getOperation().name());

                    Object val = condAvro.getValue();
                    if (val instanceof Integer) {
                        newCondition.setValue(((Number) val).longValue());
                    } else if (val instanceof Boolean) {
                        newCondition.setValue((Boolean) val ? 1L : 0L);
                    }

                    conditionRepository.save(newCondition);

                    Sensor sensor = sensorRepository.findById(condAvro.getSensorId())
                            .orElseThrow(() -> new RuntimeException("Сенсор не найден: " + condAvro.getSensorId()));

                    ScenarioCondition sc = new ScenarioCondition();
                    ScenarioConditionKey scKey = new ScenarioConditionKey();
                    scKey.setScenarioId(scenario.getId());
                    scKey.setSensorId(sensor.getId());
                    scKey.setConditionId(newCondition.getId());
                    sc.setId(scKey);

                    sc.setScenario(scenario);
                    sc.setSensor(sensor);
                    sc.setCondition(newCondition);

                    scenarioConditions.add(sc);

                    log.info("Добавлено условие: sensorId={}, type={}, operation={}, value={}",
                            sensor.getId(), newCondition.getType(), newCondition.getOperation(), newCondition.getValue());
                }

                for (DeviceActionAvro actionAvro : event.getActions()) {
                    Action newAction = new Action();
                    newAction.setType(actionAvro.getType().name());

                    Integer value = actionAvro.getValue();

                    if (actionAvro.getType() == ActionTypeAvro.SET_VALUE && value == null) {
                        log.warn("Для действия SET_VALUE поле value обязательно, sensorId: {}", actionAvro.getSensorId());
                        continue;
                    }

                    if (value != null) {
                        newAction.setValue(value.longValue());
                    }

                    actionRepository.save(newAction);

                    Sensor sensor = sensorRepository.findById(actionAvro.getSensorId())
                            .orElseThrow(() -> new RuntimeException("Сенсор не найден: " + actionAvro.getSensorId()));

                    ScenarioAction sa = new ScenarioAction();
                    ScenarioActionKey saKey = new ScenarioActionKey();
                    saKey.setScenarioId(scenario.getId());
                    saKey.setSensorId(sensor.getId());
                    saKey.setActionId(newAction.getId());
                    sa.setId(saKey);

                    sa.setScenario(scenario);
                    sa.setSensor(sensor);
                    sa.setAction(newAction);

                    scenarioActions.add(sa);

                    log.info("Добавлено действие: sensorId={}, type={}, value={}",
                            sensor.getId(), newAction.getType(), newAction.getValue());
                }

                scenario.setConditions(scenarioConditions);
                scenario.setActions(scenarioActions);
                scenarioRepository.save(scenario);

                log.info("Сценарий '{}' сохранён для хаба {}", scenario.getName(), hubId);
            }

            case ScenarioRemovedEventAvro event -> {
                scenarioRepository.findByHubIdAndName(hubId, event.getName())
                        .ifPresent(scenario -> {
                            scenarioRepository.delete(scenario);
                            log.info("Удалён сценарий '{}' для хаба {}", event.getName(), hubId);
                        });
            }

            default -> log.info("Неизвестный тип записи: {}", payload.getClass().getSimpleName());
        }
    }
}
