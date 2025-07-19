package ru.yandex.practicum.collector.dto.hub;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
@ToString
public abstract class HubEvent {
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
