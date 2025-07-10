package ru.yandex.practicum.collector.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sensorsTopic() {
        return new NewTopic("telemetry.sensors.v1", 10, (short) 1);
    }

    @Bean
    public NewTopic hubsTopic() {
        return new NewTopic("telemetry.hubs.v1", 2, (short) 3);
    }
}
