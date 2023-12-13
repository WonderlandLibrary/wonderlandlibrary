package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Json {
   public static float getFloat(JsonObject var0, String var1, float var2) {
      JsonElement var3 = var0.get(var1);
      return var3 == null ? var2 : var3.getAsFloat();
   }

   public static boolean getBoolean(JsonObject var0, String var1, boolean var2) {
      JsonElement var3 = var0.get(var1);
      return var3 == null ? var2 : var3.getAsBoolean();
   }

   public static String getString(JsonObject var0, String var1) {
      return getString(var0, var1, (String)null);
   }

   public static String getString(JsonObject var0, String var1, String var2) {
      JsonElement var3 = var0.get(var1);
      return var3 == null ? var2 : var3.getAsString();
   }

   public static float[] parseFloatArray(JsonElement var0, int var1) {
      return parseFloatArray(var0, var1, (float[])null);
   }

   public static float[] parseFloatArray(JsonElement var0, int var1, float[] var2) {
      if (var0 == null) {
         return var2;
      } else {
         JsonArray var3 = var0.getAsJsonArray();
         if (var3.size() != var1) {
            throw new JsonParseException("Wrong array length: " + var3.size() + ", should be: " + var1 + ", array: " + var3);
         } else {
            float[] var4 = new float[var3.size()];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               var4[var5] = var3.get(var5).getAsFloat();
            }

            return var4;
         }
      }
   }

   public static int[] parseIntArray(JsonElement var0, int var1) {
      return parseIntArray(var0, var1, (int[])null);
   }

   public static int[] parseIntArray(JsonElement var0, int var1, int[] var2) {
      if (var0 == null) {
         return var2;
      } else {
         JsonArray var3 = var0.getAsJsonArray();
         if (var3.size() != var1) {
            throw new JsonParseException("Wrong array length: " + var3.size() + ", should be: " + var1 + ", array: " + var3);
         } else {
            int[] var4 = new int[var3.size()];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               var4[var5] = var3.get(var5).getAsInt();
            }

            return var4;
         }
      }
   }
}
