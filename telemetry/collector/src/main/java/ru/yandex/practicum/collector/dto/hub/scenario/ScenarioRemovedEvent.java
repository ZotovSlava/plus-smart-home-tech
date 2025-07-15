package ru.yandex.practicum.collector.dto.hub.scenario;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.HubEventType;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
