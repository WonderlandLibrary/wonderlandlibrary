package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadInfo {
   private static final Logger LOGGER = LogManager.getLogger();
   private boolean worldClosed;
   private String token = "";
   private String uploadEndpoint = "";

   public static UploadInfo parse(String var0) {
      UploadInfo var1 = new UploadInfo();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         var1.worldClosed = JsonUtils.getBooleanOr("worldClosed", var3, false);
         var1.token = JsonUtils.getStringOr("token", var3, (String)null);
         var1.uploadEndpoint = JsonUtils.getStringOr("uploadEndpoint", var3, (String)null);
      } catch (Exception var4) {
         LOGGER.error("Could not parse UploadInfo: " + var4.getMessage());
      }

      return var1;
   }

   public String getToken() {
      return this.token;
   }

   public String getUploadEndpoint() {
      return this.uploadEndpoint;
   }

   public boolean isWorldClosed() {
      return this.worldClosed;
   }

   public void setToken(String var1) {
      this.token = var1;
   }
}
