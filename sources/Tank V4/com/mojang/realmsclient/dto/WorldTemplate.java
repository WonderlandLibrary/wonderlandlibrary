package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldTemplate extends ValueObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public String id;
   public String name;
   public String version;
   public String author;
   public String link;

   public static WorldTemplate parse(JsonObject var0) {
      WorldTemplate var1 = new WorldTemplate();

      try {
         var1.id = JsonUtils.getStringOr("id", var0, "");
         var1.name = JsonUtils.getStringOr("name", var0, "");
         var1.version = JsonUtils.getStringOr("version", var0, "");
         var1.author = JsonUtils.getStringOr("author", var0, "");
         var1.link = JsonUtils.getStringOr("link", var0, "");
      } catch (Exception var3) {
         LOGGER.error("Could not parse WorldTemplate: " + var3.getMessage());
      }

      return var1;
   }
}
