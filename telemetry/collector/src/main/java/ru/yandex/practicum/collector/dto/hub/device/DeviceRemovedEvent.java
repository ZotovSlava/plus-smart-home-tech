package ru.yandex.practicum.collector.dto.hub.device;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.HubEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {
    @NotBlank(message = "Device id can not be blank")
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
