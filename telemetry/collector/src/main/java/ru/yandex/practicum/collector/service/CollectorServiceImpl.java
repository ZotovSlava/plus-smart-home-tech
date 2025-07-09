package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.collector.Mapper.AvroHubMapper;
import ru.yandex.practicum.collector.Mapper.AvroSensorMapper;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import lombok.AllArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Override
    public void createEventFromSensor(SensorEvent sensorEvent) {
        SpecificRecordBase avroRecord = AvroSensorMapper.toAvro(sensorEvent);
        kafkaTemplate.send("telemetry.sensors.v1", sensorEvent.getHubId(), avroRecord);
    }

    @Override
    public void createEventFromHub(HubEvent hubEvent) {
        SpecificRecordBase avroRecord = AvroHubMapper.toAvro(hubEvent);
        kafkaTemplate.send("telemetry.hubs.v1", hubEvent.getHubId(), avroRecord);
    }
}
