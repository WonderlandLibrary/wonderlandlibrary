package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsState {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private String statusMessage;
  
  private String buyLink;
  
  public static RealmsState parse(String json) {
    RealmsState realmsState = new RealmsState();
    try {
      JsonParser parser = new JsonParser();
      JsonObject jsonObject = parser.parse(json).getAsJsonObject();
      realmsState.statusMessage = JsonUtils.getStringOr("statusMessage", jsonObject, null);
      realmsState.buyLink = JsonUtils.getStringOr("buyLink", jsonObject, null);
    } catch (Exception e) {
      LOGGER.error("Could not parse RealmsState: " + e.getMessage());
    } 
    return realmsState;
  }
  
  public String getStatusMessage() {
    return this.statusMessage;
  }
  
  public String getBuyLink() {
    return this.buyLink;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\RealmsState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */