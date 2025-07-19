package ru.yandex.practicum.collector.dto.sensor;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
