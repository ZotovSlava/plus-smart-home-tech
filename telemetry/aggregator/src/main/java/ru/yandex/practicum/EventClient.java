package ru.yandex.practicum;


import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.consumer.deserializer.BaseAvroDeserializer;
import ru.yandex.practicum.kafka.producer.serializer.EventAvroSerializer;

import java.util.Properties;

@Getter
@Component
public class EventClient {

    private final Consumer<String, SpecificRecordBase> sensorConsumer;
    private final Producer<String, SpecificRecordBase> producer;

    public EventClient(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.consumer.group-id}") String groupId
    ) {
        this.sensorConsumer = createConsumer(bootstrapServers, groupId);
        this.producer = createProducer(bootstrapServers);
    }

    private Consumer<String, SpecificRecordBase> createConsumer(String bootstrapServers, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", BaseAvroDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "false");
        return new KafkaConsumer<>(props);
    }

    private Producer<String, SpecificRecordBase> createProducer(String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", EventAvroSerializer.class.getName());
        props.put("acks", "all");
        return new KafkaProducer<>(props);
    }

    public void stop() {
        sensorConsumer.wakeup();
    }
}
