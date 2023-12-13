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
   public Subscription.SubscriptionType type;

   public Subscription() {
      this.type = Subscription.SubscriptionType.NORMAL;
   }

   public static Subscription parse(String var0) {
      Subscription var1 = new Subscription();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         var1.startDate = JsonUtils.getLongOr("startDate", var3, 0L);
         var1.daysLeft = JsonUtils.getIntOr("daysLeft", var3, 0);
         var1.type = typeFrom(JsonUtils.getStringOr("subscriptionType", var3, Subscription.SubscriptionType.NORMAL.name()));
      } catch (Exception var4) {
         LOGGER.error("Could not parse Subscription: " + var4.getMessage());
      }

      return var1;
   }

   private static Subscription.SubscriptionType typeFrom(String var0) {
      try {
         return Subscription.SubscriptionType.valueOf(var0);
      } catch (Exception var2) {
         return Subscription.SubscriptionType.NORMAL;
      }
   }

   public static enum SubscriptionType {
      NORMAL,
      RECURRING;

      private static final Subscription.SubscriptionType[] $VALUES = new Subscription.SubscriptionType[]{NORMAL, RECURRING};
   }
}
