package ru.yandex.practicum.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> hubEventConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "HubEventConsumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-client-hub");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.kafka.deserializer.HubEventDeserializer");
        return new KafkaConsumer<>(props);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> snapshotConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "SnapshotConsumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-client-snapshot");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.kafka.deserializer.SnapshotEventDeserializer");
        return new KafkaConsumer<>(props);
    }
}
