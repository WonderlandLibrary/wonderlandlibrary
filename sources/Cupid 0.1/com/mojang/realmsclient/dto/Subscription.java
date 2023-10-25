package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Subscription extends ValueObject {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public long startDate;
  
  public int daysLeft;
  
  public SubscriptionType type = SubscriptionType.NORMAL;
  
  public static Subscription parse(String json) {
    Subscription sub = new Subscription();
    try {
      JsonParser parser = new JsonParser();
      JsonObject jsonObject = parser.parse(json).getAsJsonObject();
      sub.startDate = JsonUtils.getLongOr("startDate", jsonObject, 0L);
      sub.daysLeft = JsonUtils.getIntOr("daysLeft", jsonObject, 0);
      sub.type = typeFrom(JsonUtils.getStringOr("subscriptionType", jsonObject, SubscriptionType.NORMAL.name()));
    } catch (Exception e) {
      LOGGER.error("Could not parse Subscription: " + e.getMessage());
    } 
    return sub;
  }
  
  private static SubscriptionType typeFrom(String subscriptionType) {
    try {
      return SubscriptionType.valueOf(subscriptionType);
    } catch (Exception e) {
      return SubscriptionType.NORMAL;
    } 
  }
  
  public enum SubscriptionType {
    NORMAL, RECURRING;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */