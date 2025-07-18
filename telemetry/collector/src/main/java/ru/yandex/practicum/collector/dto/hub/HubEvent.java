package ru.yandex.practicum.collector.dto.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.collector.dto.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.collector.dto.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.collector.dto.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.collector.dto.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
@Setter
@Getter
@ToString
public abstract class HubEvent {
    @NotBlank(message = "Hub ID can not be blank")
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
