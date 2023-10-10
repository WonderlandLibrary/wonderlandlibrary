package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Ops {
   public Set ops = new HashSet();

   public static Ops parse(String var0) {
      Ops var1 = new Ops();
      JsonParser var2 = new JsonParser();

      try {
         JsonElement var3 = var2.parse(var0);
         JsonObject var4 = var3.getAsJsonObject();
         JsonElement var5 = var4.get("ops");
         if (var5.isJsonArray()) {
            Iterator var6 = var5.getAsJsonArray().iterator();

            while(var6.hasNext()) {
               JsonElement var7 = (JsonElement)var6.next();
               var1.ops.add(var7.getAsString());
            }
         }
      } catch (Exception var8) {
      }

      return var1;
   }
}
