package ru.yandex.practicum.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class AvroSensorMapper {

    public static SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        return switch (sensorEvent.getType()) {
            case LIGHT_SENSOR_EVENT -> lightSensorEventToAvro((LightSensorEvent) sensorEvent);
            case MOTION_SENSOR_EVENT -> motionSensorEventToAvro((MotionSensorEvent) sensorEvent);
            case SWITCH_SENSOR_EVENT -> switchSensorEventToAvro((SwitchSensorEvent) sensorEvent);
            case CLIMATE_SENSOR_EVENT -> climateSensorEventToAvro((ClimateSensorEvent) sensorEvent);
            case TEMPERATURE_SENSOR_EVENT -> temperatureSensorEventToAvro((TemperatureSensorEvent) sensorEvent);
            default -> throw new IllegalArgumentException("Unknown sensor event type: " + sensorEvent.getType());
        };
    }

    private static SpecificRecordBase lightSensorEventToAvro(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    private static SpecificRecordBase motionSensorEventToAvro(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .setMotion(event.getMotion())
                .build();
    }

    private static SpecificRecordBase switchSensorEventToAvro(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    private static SpecificRecordBase climateSensorEventToAvro(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .build();
    }

    private static SpecificRecordBase temperatureSensorEventToAvro(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}
