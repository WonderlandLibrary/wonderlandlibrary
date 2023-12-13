package com.mojang.realmsclient.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsError {
   private static final Logger LOGGER = LogManager.getLogger();
   private String errorMessage;
   private int errorCode;

   public RealmsError(String var1) {
      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var1).getAsJsonObject();
         this.errorMessage = JsonUtils.getStringOr("errorMsg", var3, "");
         this.errorCode = JsonUtils.getIntOr("errorCode", var3, -1);
      } catch (Exception var4) {
         LOGGER.error("Could not parse RealmsError: " + var4.getMessage());
      }

   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public int getErrorCode() {
      return this.errorCode;
   }
}
