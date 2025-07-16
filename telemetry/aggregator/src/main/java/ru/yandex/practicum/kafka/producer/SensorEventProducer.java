package ru.yandex.practicum.kafka.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SensorEventProducer {

    private static final String SNAPSHOT_TOPIC = "telemetry.snapshots.v1";

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    public SensorEventProducer(KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String key, SpecificRecordBase snapshot) {
        kafkaTemplate.send(SNAPSHOT_TOPIC, key, snapshot)
                .thenAccept(result -> System.out.println("Snapshot sent: " + result))
                .exceptionally(ex -> {
                    System.err.println("Snapshot send failed: " + ex.getMessage());
                    return null;
                });
    }
}
