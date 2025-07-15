package ru.yandex.practicum.collector.mapper.avroMapper;


import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.HubEventType;
import ru.yandex.practicum.collector.dto.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.collector.dto.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.collector.dto.hub.scenario.DeviceAction;
import ru.yandex.practicum.collector.dto.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.collector.dto.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.collector.dto.hub.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

public class AvroHubMapper {
    public static SpecificRecordBase toAvro(HubEvent hubEvent) {
        HubEventType type = hubEvent.getType();

        return switch (type) {
            case DEVICE_ADDED -> deviceAddEventToAvro((DeviceAddedEvent) hubEvent);
            case DEVICE_REMOVED -> deviceRemoveEventToAvro((DeviceRemovedEvent) hubEvent);
            case SCENARIO_ADDED -> scenarioAddedEventToAvro((ScenarioAddedEvent) hubEvent);
            case SCENARIO_REMOVED -> scenarioRemovedEventToAvro((ScenarioRemovedEvent) hubEvent);
            default -> throw new IllegalArgumentException("Unknown event type: " + type);
        };
    }

    private static SpecificRecordBase deviceAddEventToAvro(DeviceAddedEvent event) {
        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase deviceRemoveEventToAvro(DeviceRemovedEvent event) {
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase scenarioAddedEventToAvro(ScenarioAddedEvent event) {
        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(toListScenarioConditionAvro(event.getConditions()))
                .setActions(toListDeviceActionAvro(event.getActions()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase scenarioRemovedEventToAvro(ScenarioRemovedEvent event) {
        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static List<ScenarioConditionAvro> toListScenarioConditionAvro(List<ScenarioCondition> conditions) {
        return conditions.stream()
                .map(AvroHubMapper::toScenarioConditionAvro)
                .collect(Collectors.toList());
    }

    private static ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .setValue(condition.getValue())
                .build();
    }

    private static List<DeviceActionAvro> toListDeviceActionAvro(List<DeviceAction> actions) {
        return actions.stream()
                .map(AvroHubMapper::toDeviceActionAvro)
                .collect(Collectors.toList());
    }

    private static DeviceActionAvro toDeviceActionAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();
    }
}
