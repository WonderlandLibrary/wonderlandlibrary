package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerActivityList {
   public long periodInMillis;
   public List serverActivities = new ArrayList();

   public static ServerActivityList parse(String var0) {
      ServerActivityList var1 = new ServerActivityList();
      JsonParser var2 = new JsonParser();

      try {
         JsonElement var3 = var2.parse(var0);
         JsonObject var4 = var3.getAsJsonObject();
         var1.periodInMillis = JsonUtils.getLongOr("periodInMillis", var4, -1L);
         JsonElement var5 = var4.get("playerActivityDto");
         if (var5 != null && var5.isJsonArray()) {
            JsonArray var6 = var5.getAsJsonArray();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               JsonElement var8 = (JsonElement)var7.next();
               ServerActivity var9 = ServerActivity.parse(var8.getAsJsonObject());
               var1.serverActivities.add(var9);
            }
         }
      } catch (Exception var10) {
      }

      return var1;
   }
}
