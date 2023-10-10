package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

public class BlockPartFace {
   public final BlockFaceUV blockFaceUV;
   public static final EnumFacing FACING_DEFAULT = null;
   public final String texture;
   public final EnumFacing cullFace;
   public final int tintIndex;

   public BlockPartFace(EnumFacing var1, int var2, String var3, BlockFaceUV var4) {
      this.cullFace = var1;
      this.tintIndex = var2;
      this.texture = var3;
      this.blockFaceUV = var4;
   }

   static class Deserializer implements JsonDeserializer {
      public BlockPartFace deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = var1.getAsJsonObject();
         EnumFacing var5 = this.parseCullFace(var4);
         int var6 = this.parseTintIndex(var4);
         String var7 = this.parseTexture(var4);
         BlockFaceUV var8 = (BlockFaceUV)var3.deserialize(var4, BlockFaceUV.class);
         return new BlockPartFace(var5, var6, var7, var8);
      }

      protected int parseTintIndex(JsonObject var1) {
         return JsonUtils.getInt(var1, "tintindex", -1);
      }

      private String parseTexture(JsonObject var1) {
         return JsonUtils.getString(var1, "texture");
      }

      private EnumFacing parseCullFace(JsonObject var1) {
         String var2 = JsonUtils.getString(var1, "cullface", "");
         return EnumFacing.byName(var2);
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }
   }
}
