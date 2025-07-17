package ru.yandex.practicum.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.model.scenario.Scenario;
import ru.yandex.practicum.model.scenario.ScenarioAction;
import ru.yandex.practicum.model.scenario.ScenarioCondition;
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

        log.info("Добавление записи \"{}\" в таблицу. Класс объекта: \"{}\"", record, payload.getClass().getSimpleName());

        switch (payload) {
            case DeviceAddedEventAvro event -> {
                Sensor sensor = new Sensor();
                sensor.setId(event.getId());
                sensor.setHubId(hubId);
                sensorRepository.save(sensor);
            }

            case DeviceRemovedEventAvro event -> {
                sensorRepository.findByIdAndHubId(event.getId(), hubId)
                        .ifPresent(sensorRepository::delete);
            }

            case ScenarioAddedEventAvro event -> {

                Scenario scenario = new Scenario();
                scenario.setHubId(hubId);
                scenario.setName(event.getName());

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
                            .orElseThrow(() -> new RuntimeException("Sensor not found: " + condAvro.getSensorId()));

                    ScenarioCondition sc = new ScenarioCondition();

                    sc.setScenario(scenario);
                    sc.setSensor(sensor);
                    sc.setCondition(newCondition);

                    scenarioConditions.add(sc);
                }

                for (DeviceActionAvro actionAvro : event.getActions()) {
                    Action newAction = new Action();
                    newAction.setType(actionAvro.getType().name());
                    newAction.setValue(actionAvro.getValue().longValue());

                    actionRepository.save(newAction);

                    Sensor sensor = sensorRepository.findById(actionAvro.getSensorId())
                            .orElseThrow(() -> new RuntimeException("Sensor not found: " + actionAvro.getSensorId()));

                    ScenarioAction sa = new ScenarioAction();

                    sa.setScenario(scenario);
                    sa.setSensor(sensor);
                    sa.setAction(newAction);

                    scenarioActions.add(sa);
                }

                scenario.setConditions(scenarioConditions);
                scenario.setActions(scenarioActions);

                scenarioRepository.save(scenario);
            }

            case ScenarioRemovedEventAvro event -> {
                scenarioRepository.findByHubIdAndName(hubId, event.getName())
                        .ifPresent(scenarioRepository::delete);
            }

            default -> log.info("Неизвестный класс объекта: {}", payload.getClass().getSimpleName());
        }
    }
}
