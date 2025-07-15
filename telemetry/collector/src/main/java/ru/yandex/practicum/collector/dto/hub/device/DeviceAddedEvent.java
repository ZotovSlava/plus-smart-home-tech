package ru.yandex.practicum.collector.dto.hub.device;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.HubEventType;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
