package com.example;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.exceptions.DeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerRecordJsonSerde implements Serializer<CustomerRecord>, Deserializer<CustomerRecord> {
    private final ObjectMapper objectMapper;

    public CustomerRecordJsonSerde() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration required
    }

    @Override
    public byte[] serialize(String topic, CustomerRecord data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    @Override
    public CustomerRecord deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), CustomerRecord.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException("Error deserializing JSON to object", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during deserialization", e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}
