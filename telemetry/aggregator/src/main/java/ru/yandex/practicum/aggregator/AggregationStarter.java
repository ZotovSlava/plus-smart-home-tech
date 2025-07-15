package ru.yandex.practicum.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.EventClient;
import ru.yandex.practicum.EventTopics;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.InMemorySensorEvents;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final EventClient client;
    private final InMemorySensorEvents service;

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        Consumer<String, SpecificRecordBase> consumer = client.getSensorConsumer();
        try {
            consumer.subscribe(List.of(EventTopics.TELEMETRY_SENSOR_TOPIC));
            int count = 0;

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    SensorEventAvro event = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> snapshotOptional = service.updateState(event);

                    snapshotOptional.ifPresent(snapshot -> {
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(EventTopics.TELEMETRY_SNAPSHOT_TOPIC, snapshot);
                        client.getProducer().send(producerRecord);
                        log.info("Снапшот отправлен в топик {}", EventTopics.TELEMETRY_SNAPSHOT_TOPIC);
                    });

                    manageOffsets(record, count, consumer);
                    count++;
                }

                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
            // ожидаемое завершение
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                log.info("Завершаем работу: сбрасываем буферы и фиксируем смещения");
                client.getProducer().flush();
                consumer.commitSync(currentOffsets);
            } catch (Exception e) {
                log.warn("Ошибка при финализации producer/consumer", e);
            } finally {
                log.info("Закрываем consumer");
                consumer.close();
                log.info("Закрываем producer");
                client.getProducer().close();
            }
        }
    }

    private static void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count, Consumer<String, SpecificRecordBase> consumer) {
        TopicPartition partition = new TopicPartition(record.topic(), record.partition());
        OffsetAndMetadata offsetMeta = new OffsetAndMetadata(record.offset() + 1);
        currentOffsets.put(partition, offsetMeta);

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка при асинхронной фиксации смещений: {}", offsets, exception);
                }
            });
        }
    }
}
