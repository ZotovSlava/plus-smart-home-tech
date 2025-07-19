package ru.yandex.practicum.collector.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.collector.mapper.avroMapper.AvroHubMapper;
import ru.yandex.practicum.collector.mapper.avroMapper.AvroSensorMapper;

@Service
@AllArgsConstructor
@Slf4j
public class CollectorServiceImpl implements CollectorService {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Override
    public void createEventFromSensor(SensorEvent sensorEvent) {
        SpecificRecordBase avroRecord = AvroSensorMapper.toAvro(sensorEvent);
        log.info("Sending sensor event to Kafka: key={}, value={}", sensorEvent.getHubId(), avroRecord);
        kafkaTemplate.send("telemetry.sensors.v1", sensorEvent.getHubId(), avroRecord);
    }

    @Override
    public void createEventFromHub(HubEvent hubEvent) {
        SpecificRecordBase avroRecord = AvroHubMapper.toAvro(hubEvent);
        log.info("Sending hub event to Kafka: key={}, value={}", hubEvent.getHubId(), avroRecord);
        kafkaTemplate.send("telemetry.hubs.v1", hubEvent.getHubId(), avroRecord);
    }
}
