package ru.yandex.practicum.collector.mapper.protoMapper;


import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.collector.dto.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.collector.dto.hub.device.DeviceType;
import ru.yandex.practicum.collector.dto.hub.scenario.*;
import ru.yandex.practicum.grpc.telemetry.event.*;

import java.time.Instant;
import java.util.List;

public class ProtoHubMapper {
    public static HubEvent toDto(HubEventProto proto) {
        HubEvent hubEvent;

        switch (proto.getPayloadCase()) {
            case DEVICE_ADDED -> hubEvent = deviceAddEventProtoToDto(proto);
            case DEVICE_REMOVED -> hubEvent = deviceRemoveEventProtoToDto(proto);
            case SCENARIO_ADDED -> hubEvent = scenarioAddedEventProtoToDto(proto);
            case SCENARIO_REMOVED -> hubEvent = scenarioRemovedEventProtoToDto(proto);
            default -> throw new IllegalArgumentException("Unknown event type: " + proto.getPayloadCase());
        }

        return hubEvent;
    }

    private static HubEvent deviceAddEventProtoToDto(HubEventProto proto) {
        DeviceAddedEventProto payload = proto.getDeviceAdded();

        return DeviceAddedEvent.builder()
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond( proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()))
                .id(payload.getId())
                .deviceType(DeviceType.valueOf(payload.getType().name()))
                .build();
    }


    private static HubEvent deviceRemoveEventProtoToDto(HubEventProto proto) {
        DeviceRemovedEventProto payload = proto.getDeviceRemoved();

        return DeviceRemovedEvent.builder()
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond( proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()))
                .id(payload.getId())
                .build();
    }

    private static HubEvent scenarioAddedEventProtoToDto(HubEventProto proto) {
        ScenarioAddedEventProto payload = proto.getScenarioAdded();

        return ScenarioAddedEvent.builder()
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond( proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()))
                .name(payload.getName())
                .conditions(getScenarioConditionList(payload.getConditionList()))
                .actions(getDeviceActionList(payload.getActionList()))
                .build();
    }

    private static HubEvent scenarioRemovedEventProtoToDto(HubEventProto proto) {
        ScenarioRemovedEventProto payload = proto.getScenarioRemoved();

        return ScenarioRemovedEvent.builder()
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond( proto.getTimestamp().getSeconds(),
                        proto.getTimestamp().getNanos()))
                .name(payload.getName())
                .build();
    }

    private static List<ScenarioCondition> getScenarioConditionList(List<ScenarioConditionProto> conditions) {
        return conditions.stream()
                .map(ProtoHubMapper::toScenarioCondition)
                .toList();
    }

    private static List<DeviceAction> getDeviceActionList(List<DeviceActionProto> actions) {
        return actions.stream()
                .map(ProtoHubMapper::toDeviceAction)
                .toList();
    }

    private static DeviceAction toDeviceAction(DeviceActionProto deviceActionProto) {
        Integer value = null;
        switch (deviceActionProto.getValueCase()) {
            case INT_VALUE -> value = deviceActionProto.getIntValue();
            case BOOL_VALUE -> value = deviceActionProto.getBoolValue() ? 1 : 0;
            case VALUE_NOT_SET -> value = null;
        }

        return DeviceAction.builder()
                .sensorId(deviceActionProto.getSensorId())
                .type(DeviceActionType.valueOf(deviceActionProto.getType().name()))
                .value(value)
                .build();
    }

    private static ScenarioCondition toScenarioCondition(ScenarioConditionProto scenarioConditionProto) {
        Integer value = null;
        switch (scenarioConditionProto.getValueCase()) {
            case INT_VALUE -> value = scenarioConditionProto.getIntValue();
            case BOOL_VALUE -> value = scenarioConditionProto.getBoolValue() ? 1 : 0;
            case VALUE_NOT_SET -> value = null;
        }

        return ScenarioCondition.builder()
                .sensorId(scenarioConditionProto.getSensorId())
                .type(ScenarioConditionType.valueOf(scenarioConditionProto.getType().name()))
                .operation(ScenarioConditionOperation.valueOf(scenarioConditionProto.getOperation().name()))
                .value(value)
                .build();
    }
}

