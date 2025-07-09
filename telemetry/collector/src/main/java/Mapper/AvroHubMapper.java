package Mapper;

import dto.hub.HubEvent;
import dto.hub.HubEventType;
import dto.hub.device.DeviceAddedEvent;
import dto.hub.device.DeviceRemovedEvent;
import dto.hub.scenario.*;
import org.apache.avro.specific.SpecificRecordBase;
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
            default -> throw new IllegalArgumentException("Unknown action type: " + type);
        };
    }

    private static SpecificRecordBase deviceAddEventToAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    private static SpecificRecordBase deviceRemoveEventToAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .build();
    }

    private static SpecificRecordBase scenarioAddedEventToAvro(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setName(event.getName())
                .setConditions(toListScenarioConditionAvro(event.getConditions()))
                .setActions(toListDeviceActionAvro(event.getActions()))
                .build();
    }

    private static SpecificRecordBase scenarioRemovedEventToAvro(ScenarioRemovedEvent event) {
        return ScenarioRemoveEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setName(event.getName())
                .build();
    }

    private static List<DeviceActionAvro> toListDeviceActionAvro(List<DeviceAction> actions){
        return actions.stream()
                .map(AvroHubMapper :: toDeviceActionAvro)
                .collect(Collectors.toList());
    }

    private static DeviceActionAvro toDeviceActionAvro(DeviceAction action){
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .setType(DeviceActionTypeAvro.valueOf(action.getType().name()))
                .build();
    }

    private static List<ScenarioConditionAvro> toListScenarioConditionAvro(List<ScenarioCondition> conditions){
        return conditions.stream()
                .map(AvroHubMapper :: toScenarioConditionAvro)
                .collect(Collectors.toList());
    }

    private static ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition condition){
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setValue(condition.getValue())
                .setType(ScenarioConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ScenarioConditionOperationAvro.valueOf(condition.getOperation().name()))
                .build();
    }
}
