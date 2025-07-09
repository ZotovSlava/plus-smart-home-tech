package service;

import Mapper.AvroHubMapper;
import Mapper.AvroSensorMapper;
import dto.hub.HubEvent;
import dto.sensor.SensorEvent;
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
