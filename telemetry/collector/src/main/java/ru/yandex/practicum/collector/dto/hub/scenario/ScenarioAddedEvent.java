package ru.yandex.practicum.collector.dto.hub.scenario;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.HubEventType;

import java.util.List;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    private String name;

    private List<ScenarioCondition> conditions;

    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
