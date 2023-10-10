package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldTemplateList extends ValueObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public List templates;

   public static WorldTemplateList parse(String var0) {
      WorldTemplateList var1 = new WorldTemplateList();
      var1.templates = new ArrayList();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         if (var3.get("templates").isJsonArray()) {
            Iterator var4 = var3.get("templates").getAsJsonArray().iterator();

            while(var4.hasNext()) {
               var1.templates.add(WorldTemplate.parse(((JsonElement)var4.next()).getAsJsonObject()));
            }
         }
      } catch (Exception var5) {
         LOGGER.error("Could not parse WorldTemplateList: " + var5.getMessage());
      }

      return var1;
   }
}
