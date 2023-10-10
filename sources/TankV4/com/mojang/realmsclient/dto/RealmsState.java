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

   public static RealmsState parse(String var0) {
      RealmsState var1 = new RealmsState();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         var1.statusMessage = JsonUtils.getStringOr("statusMessage", var3, (String)null);
         var1.buyLink = JsonUtils.getStringOr("buyLink", var3, (String)null);
      } catch (Exception var4) {
         LOGGER.error("Could not parse RealmsState: " + var4.getMessage());
      }

      return var1;
   }

   public String getStatusMessage() {
      return this.statusMessage;
   }

   public String getBuyLink() {
      return this.buyLink;
   }
}
