package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Application starting...");

        LOGGER.info("Creating kafka consumer instance...");
        KafkaConsumer<String, CustomerRecord> kc = KafkaConsumerFactory.createConsumer();
        AppContext ctx = new AppContext(kc, "http://localhost:3000/messages");
        LOGGER.info("Executing application context..");
        ctx.execute();
    }
}
