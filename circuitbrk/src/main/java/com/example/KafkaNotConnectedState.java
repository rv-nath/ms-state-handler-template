package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafkaNotConnectedState implements State {
  private static final Logger LOGGER = LogManager.getLogger(App.class);
  private boolean warned;

  private Properties props = null;
  private String kafkaHost;
  private int kafkaPort;

  public KafkaNotConnectedState() throws Exception {
    props = AppConfig.getKafkaConsumerProperties();
    String bs = props.getProperty("bootstrap.servers");
    kafkaHost = bs.split(":")[0];
    kafkaPort = Integer.parseInt(bs.split(":")[1]);
    warned = false;
  }

  @Override
  public void execute(AppContext context) {
    LOGGER.info("Attempting to connect to kafka cluster at {}:{}", kafkaHost, kafkaPort);
    while (true) {
      boolean result = isBrokerAvailable(kafkaHost, kafkaPort);
      if (result == true) {
        context.switchState("httpNotConnected");
        break;
      } else {
        if (!warned) {
          LOGGER.warn("Cannot connect to kafka.  Will keep trying untile connected.");
          warned = true;
        }
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          // e.printStackTrace();
        }
      }
    }
  }

  private static boolean isBrokerAvailable(String host, int port) {
    try (Socket socket = new Socket()) {
      // Set a timeout for the socket connection (in milliseconds)
      int timeout = 5000; // 5 seconds
      socket.connect(new InetSocketAddress(host, port), timeout);
      return true; // Connection successful, broker is available
    } catch (IOException e) {
      return false; // Connection failed, broker is unavailable
    }
  }
}
