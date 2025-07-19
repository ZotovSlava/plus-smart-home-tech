package ru.yandex.practicum.collector.dto.hub.scenario;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
