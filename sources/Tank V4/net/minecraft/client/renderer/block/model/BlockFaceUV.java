package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;

public class BlockFaceUV {
   public final int rotation;
   public float[] uvs;

   public int func_178345_c(int var1) {
      return (var1 + (4 - this.rotation / 90)) % 4;
   }

   public void setUvs(float[] var1) {
      if (this.uvs == null) {
         this.uvs = var1;
      }

   }

   public float func_178346_b(int var1) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int var2 = this.func_178347_d(var1);
         return var2 != 0 && var2 != 3 ? this.uvs[3] : this.uvs[1];
      }
   }

   private int func_178347_d(int var1) {
      return (var1 + this.rotation / 90) % 4;
   }

   public float func_178348_a(int var1) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int var2 = this.func_178347_d(var1);
         return var2 != 0 && var2 != 1 ? this.uvs[2] : this.uvs[0];
      }
   }

   public BlockFaceUV(float[] var1, int var2) {
      this.uvs = var1;
      this.rotation = var2;
   }

   static class Deserializer implements JsonDeserializer {
      private float[] parseUV(JsonObject var1) {
         if (!var1.has("uv")) {
            return null;
         } else {
            JsonArray var2 = JsonUtils.getJsonArray(var1, "uv");
            if (var2.size() != 4) {
               throw new JsonParseException("Expected 4 uv values, found: " + var2.size());
            } else {
               float[] var3 = new float[4];

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  var3[var4] = JsonUtils.getFloat(var2.get(var4), "uv[" + var4 + "]");
               }

               return var3;
            }
         }
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      public BlockFaceUV deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = var1.getAsJsonObject();
         float[] var5 = this.parseUV(var4);
         int var6 = this.parseRotation(var4);
         return new BlockFaceUV(var5, var6);
      }

      protected int parseRotation(JsonObject var1) {
         int var2 = JsonUtils.getInt(var1, "rotation", 0);
         if (var2 >= 0 && var2 % 90 == 0 && var2 / 90 <= 3) {
            return var2;
         } else {
            throw new JsonParseException("Invalid rotation " + var2 + " found, only 0/90/180/270 allowed");
         }
      }
   }
}
