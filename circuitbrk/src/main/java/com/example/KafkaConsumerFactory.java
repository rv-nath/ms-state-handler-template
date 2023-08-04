package com.example;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerFactory {
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String GROUP_ID = "third_app";
  private static final String TOPIC = "sometopic";

  public static KafkaConsumer<String, CustomerRecord> createConsumer() {
    // Creating consumer properties
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerRecordJsonSerde.class.getName());
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // Disable auto-commit
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    // Creating the Kafka consumer
    KafkaConsumer<String, CustomerRecord> consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Arrays.asList(TOPIC));
    return consumer;
  }
}
