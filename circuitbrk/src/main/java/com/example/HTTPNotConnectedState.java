package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.utils.HttpUtils;

public class HTTPNotConnectedState implements State {
  private static final Logger LOGGER = LogManager.getLogger(App.class);

  private boolean isRunning;
  private boolean warned;

  public HTTPNotConnectedState() {
    this.isRunning = true;
    warned = false;
  }

  @Override
  public void execute(AppContext context) {
    // LOGGER.log(Level.INFO, "Not Connected State: Waiting for connection...");
    LOGGER.info("Checking if HTTP endpont can be reached...");

    while (isRunning) {
      // Check the reachability of the HTTP service
      //
      if (HttpUtils.isEndpointReachable(context.getPostUrl())) {
        LOGGER.info("HTTP service is now reachable. Transitioning to Connected state.");
        context.switchState("connected");
        break;
      }

      // Sleep for a certain interval before checking again
      try {
        if (!warned) {
          LOGGER.warn("Cannot connect to http service.  Will retry after 5 seconds.");
          warned = true;
        }
        Thread.sleep(5000); // Wait for 5 seconds before checking again
      } catch (InterruptedException e) {
        LOGGER.info("Thread interrupted.", e);
      }
    }
  }

  public void stop() {
    isRunning = false;
  }
}
