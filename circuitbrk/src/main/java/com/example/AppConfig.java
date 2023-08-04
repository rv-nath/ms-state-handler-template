package com.example;

import java.util.Properties;

public class AppConfig {
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String GROUP_ID = "third_app";
  private static final String TOPIC = "sometopic";
  private static final String POST_URL = "http://example.com/api";

  private static Properties properties;

  // Private constructor to prevent instantiation from outside the class
  private AppConfig() {
    // Initialize properties here or load them from a configuration file
    properties = new Properties();
    properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.put("value.deserializer", "com.example.CustomerRecordJsonSerde");
    properties.put("group.id", GROUP_ID);
    properties.put("auto.offset.reset", "earliest");
    properties.put("request.timeout.ms", 5000);
  }

  // Static method to get the Kafka consumer properties
  public static Properties getKafkaConsumerProperties() {
    if (properties == null) {
      new AppConfig();
    }
    return properties;
  }

  // Static method to get the HTTP endpoint URL
  public static String getPostUrl() {
    return POST_URL;
  }
}
