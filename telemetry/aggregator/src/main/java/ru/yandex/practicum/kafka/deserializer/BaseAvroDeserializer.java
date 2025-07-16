package ru.yandex.practicum.kafka.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class BaseAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
    private DecoderFactory decoderFactory;
    private Schema schema;
    private DatumReader<T> reader;

    public BaseAvroDeserializer() {
    }

    public BaseAvroDeserializer(Schema schema) {
        this(DecoderFactory.get(), schema);
    }

    public BaseAvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        this.decoderFactory = decoderFactory;
        this.schema = schema;
        this.reader = new SpecificDatumReader<>(schema);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.decoderFactory = DecoderFactory.get();

        String schemaString = (String) configs.get("schema");
        if (schemaString == null) {
            throw new IllegalArgumentException("Avro schema not provided in config under key 'schema'");
        }

        this.schema = new Schema.Parser().parse(schemaString);
        this.reader = new SpecificDatumReader<>(schema);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) return null;

        if (decoderFactory == null || reader == null) {
            throw new IllegalStateException("Deserializer not properly configured");
        }

        try {
            BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
            return this.reader.read(null, decoder);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка десериализации Avro-сообщения с топика: " + topic, e);
        }
    }

    @Override
    public void close() {

    }
}
