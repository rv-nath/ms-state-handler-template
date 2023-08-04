package com.example;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;

import com.example.exceptions.DeserializationException;
import com.example.utils.HttpUtils;

public class ConnectedState implements State {
  private static final Logger LOGGER = LogManager.getLogger(ConnectedState.class.getName());

  private boolean isRunning;
  private ConsumerRecords<String, CustomerRecord> records;
  private int recordCounter;
  TopicPartition tp;

  public ConnectedState() {
    this.isRunning = true;
    this.recordCounter = 0;
    this.tp = new TopicPartition("sometopic", 0);
  }

  @Override
  public void execute(AppContext context) {
    KafkaConsumer<String, CustomerRecord> consumer = context.getKafkaConsumer();
    String postUrl = context.getPostUrl();

    while (isRunning) {
      try {
        // if (recordCounter == 0) {
        records = consumer.poll(Duration.ofMillis(100));
        // }
        if (records == null || records.isEmpty()) {
          continue;
        }
        for (; recordCounter < records.count(); recordCounter++) {
          try {
            ConsumerRecord<String, CustomerRecord> record = records.iterator().next();
            String userId = record.value().getUserId();
            LOGGER.info("Received message for user ID: {0}. Posting to service: {1}",
                new Object[] { userId, postUrl });

            if (!HttpUtils.isEndpointReachable(postUrl)) {
              LOGGER.warn("service not reachable. Transitioning to Not Connected state.");
              context.switchState("notConnected");
              break;
            }

            // Perform HTTP operation for the user ID and post to the endpoint
            HttpUtils.sendHttpPostRequest(postUrl, record.value().toJSOString());

            // Reset the counter if we have processed all the records
            if (recordCounter >= records.count()) {
              recordCounter = 0;
            }
          } catch (Exception e) {
            LOGGER.warn("Error during Kafka consumption: {0}", e.getMessage());
          }
        }
      } catch (DeserializationException dse) {
        LOGGER.warn("Received malformed JSON. Ignoring...");
      } catch (SerializationException se) {
        long pos = consumer.position(tp);
        // LOGGER.log(Level.INFO, "offset position:{0}", pos);
        LOGGER.warn("Received malformed JSON. Ignoring...");
        consumer.seek(tp, pos + 1);

      } catch (Exception e) {
        LOGGER.warn("Error during Kafka consumption: {0}", e.getMessage());
      }
    }
  }

  public void stop() {
    isRunning = false;
  }
}
