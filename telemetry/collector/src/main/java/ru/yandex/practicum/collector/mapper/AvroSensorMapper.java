package ru.yandex.practicum.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class AvroSensorMapper {
    public static SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        SensorEventType type = sensorEvent.getType();

        return switch (type) {
            case LIGHT_SENSOR_EVENT -> lightSensorEventToAvro((LightSensorEvent) sensorEvent);
            case MOTION_SENSOR_EVENT -> motionSensorEventToAvro((MotionSensorEvent) sensorEvent);
            case SWITCH_SENSOR_EVENT -> switchSensorEventToAvro((SwitchSensorEvent) sensorEvent);
            case CLIMATE_SENSOR_EVENT -> climateSensorEventToAvro((ClimateSensorEvent) sensorEvent);
            case TEMPERATURE_SENSOR_EVENT -> temperatureSensorEventToAvro((TemperatureSensorEvent) sensorEvent);
            default -> throw new IllegalArgumentException("Unknown sensor type: " + type);
        };
    }

    private static SpecificRecordBase climateSensorEventToAvro(ClimateSensorEvent event) {
        ClimateSensorEventAvro payload = ClimateSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setTemperatureC(event.getTemperatureC())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase lightSensorEventToAvro(LightSensorEvent event) {
        LightSensorEventAvro payload = LightSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase motionSensorEventToAvro(MotionSensorEvent event) {
        MotionSensorEventAvro payload = MotionSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase switchSensorEventToAvro(SwitchSensorEvent event) {
        SwitchSensorEventAvro payload = SwitchSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setState(event.getState())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private static SpecificRecordBase temperatureSensorEventToAvro(TemperatureSensorEvent event) {
        TemperatureSensorEventAvro payload = TemperatureSensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}