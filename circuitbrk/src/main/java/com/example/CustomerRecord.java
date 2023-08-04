package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRecord {
  @JsonProperty("userId")
  private String userId;

  // Getters and setters
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String toJSOString() {
    return String.format("{ \"userId\" : \"%s\"}", getUserId());
  }
}
