package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerList extends ValueObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public List servers;

   public static RealmsServerList parse(String var0) {
      RealmsServerList var1 = new RealmsServerList();
      var1.servers = new ArrayList();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         if (var3.get("servers").isJsonArray()) {
            JsonArray var4 = var3.get("servers").getAsJsonArray();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               var1.servers.add(RealmsServer.parse(((JsonElement)var5.next()).getAsJsonObject()));
            }
         }
      } catch (Exception var6) {
         LOGGER.error("Could not parse McoServerList: " + var6.getMessage());
      }

      return var1;
   }
}
