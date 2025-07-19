package ru.yandex.practicum.collector.dto.sensor;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer voltage;
    private Boolean motion;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
