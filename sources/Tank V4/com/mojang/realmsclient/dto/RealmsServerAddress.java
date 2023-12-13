package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerAddress extends ValueObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public String address;

   public static RealmsServerAddress parse(String var0) {
      JsonParser var1 = new JsonParser();
      RealmsServerAddress var2 = new RealmsServerAddress();

      try {
         JsonObject var3 = var1.parse(var0).getAsJsonObject();
         var2.address = JsonUtils.getStringOr("address", var3, (String)null);
      } catch (Exception var4) {
         LOGGER.error("Could not parse McoServerAddress: " + var4.getMessage());
      }

      return var2;
   }
}
