package ru.yandex.practicum.collector.mapper.protoMapper;

import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.collector.dto.sensor.*;
import ru.yandex.practicum.grpc.telemetry.event.*;

import java.time.Instant;

public class ProtoSensorMapper {
    public static SensorEvent toDto(SensorEventProto proto) {
        SensorEvent sensorEvent;

        switch (proto.getPayloadCase()) {
            case LIGHT_SENSOR -> sensorEvent = lightSensorProtoToDto(proto);
            case MOTION_SENSOR -> sensorEvent = motionSensorProtoToDto(proto);
            case SWITCH_SENSOR -> sensorEvent = switchSensorProtoToDto(proto);
            case CLIMATE_SENSOR -> sensorEvent = climateSensorProtoToDto(proto);
            case TEMPERATURE_SENSOR -> sensorEvent = temperatureSensorProtoToDto(proto);
            default -> throw new IllegalArgumentException("Unknown sensor type: " + proto.getPayloadCase());
        }

        return sensorEvent;
    }

    private static SensorEvent climateSensorProtoToDto(SensorEventProto proto) {
        ClimateSensorProto payload = proto.getClimateSensor();
        return ClimateSensorEvent.builder()
                .id(proto.getId())
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond(proto.getTimestamp().getSeconds()))
                .temperatureC(payload.getTemperatureC())
                .humidity(payload.getHumidity())
                .co2Level(payload.getCo2Level())
                .build();
    }

    private static SensorEvent lightSensorProtoToDto(SensorEventProto proto) {
        LightSensorProto payload = proto.getLightSensor();
        return LightSensorEvent.builder()
                .id(proto.getId())
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond(proto.getTimestamp().getSeconds()))
                .linkQuality(payload.getLinkQuality())
                .luminosity(payload.getLuminosity())
                .build();
    }

    private static SensorEvent motionSensorProtoToDto(SensorEventProto proto) {
        MotionSensorProto payload = proto.getMotionSensor();
        return MotionSensorEvent.builder()
                .id(proto.getId())
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond(proto.getTimestamp().getSeconds()))
                .linkQuality(payload.getLinkQuality())
                .voltage(payload.getVoltage())
                .motion(payload.getMotion())
                .build();
    }

    private static SensorEvent switchSensorProtoToDto(SensorEventProto proto) {
        SwitchSensorProto payload = proto.getSwitchSensor();
        return SwitchSensorEvent.builder()
                .id(proto.getId())
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond(proto.getTimestamp().getSeconds()))
                .state(payload.getState())
                .build();
    }

    private static SensorEvent temperatureSensorProtoToDto(SensorEventProto proto) {
        TemperatureSensorProto payload = proto.getTemperatureSensor();
        return TemperatureSensorEvent.builder()
                .id(proto.getId())
                .hubId(proto.getHubId())
                .timestamp(Instant.ofEpochSecond(proto.getTimestamp().getSeconds()))
                .temperatureC(payload.getTemperatureC())
                .temperatureF(payload.getTemperatureF())
                .build();
    }
}


