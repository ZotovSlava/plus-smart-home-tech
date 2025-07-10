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
        return LightSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    private static SpecificRecordBase motionSensorEventToAvro(MotionSensorEvent event) {
        return MotionSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .setMotion(event.getMotion())
                .build();
    }

    private static SpecificRecordBase switchSensorEventToAvro(SwitchSensorEvent event) {
        return SwitchSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setState(event.getState())
                .build();
    }

    private static SpecificRecordBase climateSensorEventToAvro(ClimateSensorEvent event) {
        return ClimateSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setTemperatureC(event.getTemperatureC())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .build();
    }

    private static SpecificRecordBase temperatureSensorEventToAvro(TemperatureSensorEvent event) {
        return TemperatureSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}
