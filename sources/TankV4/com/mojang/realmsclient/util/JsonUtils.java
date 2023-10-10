package com.mojang.realmsclient.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

public class JsonUtils {
   public static String getStringOr(String var0, JsonObject var1, String var2) {
      JsonElement var3 = var1.get(var0);
      if (var3 != null) {
         return var3.isJsonNull() ? var2 : var3.getAsString();
      } else {
         return var2;
      }
   }

   public static int getIntOr(String var0, JsonObject var1, int var2) {
      JsonElement var3 = var1.get(var0);
      if (var3 != null) {
         return var3.isJsonNull() ? var2 : var3.getAsInt();
      } else {
         return var2;
      }
   }

   public static long getLongOr(String var0, JsonObject var1, long var2) {
      JsonElement var4 = var1.get(var0);
      if (var4 != null) {
         return var4.isJsonNull() ? var2 : var4.getAsLong();
      } else {
         return var2;
      }
   }

   public static boolean getBooleanOr(String var0, JsonObject var1, boolean var2) {
      JsonElement var3 = var1.get(var0);
      if (var3 != null) {
         return var3.isJsonNull() ? var2 : var3.getAsBoolean();
      } else {
         return var2;
      }
   }

   public static Date getDateOr(String var0, JsonObject var1) {
      JsonElement var2 = var1.get(var0);
      return var2 != null ? new Date(Long.parseLong(var2.getAsString())) : new Date();
   }
}
