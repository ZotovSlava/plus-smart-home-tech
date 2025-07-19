package ru.yandex.practicum.collector.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Getter
@ToString
public abstract class SensorEvent {
    private String id;
    private String hubId;
    private Instant timestamp = Instant.now();

    @NotNull
    public abstract SensorEventType getType();
}
