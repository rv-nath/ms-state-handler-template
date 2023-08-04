package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class AppContext {
    private static final Logger LOGGER = Logger.getLogger(AppContext.class.getName());

    private State currentState;
    private Map<String, State> states;
    private KafkaConsumer<String, CustomerRecord> kafkaConsumer;
    private String postUrl;
    // other variables and methods

    public AppContext(KafkaConsumer<String, CustomerRecord> kafkaConsumer, String postUrl) throws Exception {
        this.kafkaConsumer = kafkaConsumer;
        this.postUrl = postUrl;
        states = new HashMap<>();
        states.put("httpNotConnected", new HTTPNotConnectedState());
        states.put("kafkaNotConnected", new KafkaNotConnectedState());
        states.put("connected", new ConnectedState());
        currentState = states.get("kafkaNotConnected"); // Set the initial state to NotConnectedState
        // initialize other variables
    }

    public void switchState(String stateName) {
        State nextState = states.get(stateName);
        if (nextState != null) {
            currentState = nextState;
        } else {
            LOGGER.log(Level.SEVERE, "Invalid state name: {0}", stateName);
            throw new IllegalArgumentException("Invalid state name: " + stateName);
        }
    }

    public void execute() {
        while (true) {
            // LOGGER.log(Level.INFO, "context execute..");
            currentState.execute(this);
        }
    }

    public KafkaConsumer<String, CustomerRecord> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public String getPostUrl() {
        return postUrl;
    }

    // other methods and variables
}