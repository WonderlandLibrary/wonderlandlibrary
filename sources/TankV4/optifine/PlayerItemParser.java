package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class PlayerItemParser {
   private static JsonParser jsonParser = new JsonParser();
   public static final String ITEM_TYPE = "type";
   public static final String ITEM_TEXTURE_SIZE = "textureSize";
   public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
   public static final String ITEM_MODELS = "models";
   public static final String MODEL_ID = "id";
   public static final String MODEL_BASE_ID = "baseId";
   public static final String MODEL_TYPE = "type";
   public static final String MODEL_ATTACH_TO = "attachTo";
   public static final String MODEL_INVERT_AXIS = "invertAxis";
   public static final String MODEL_MIRROR_TEXTURE = "mirrorTexture";
   public static final String MODEL_TRANSLATE = "translate";
   public static final String MODEL_ROTATE = "rotate";
   public static final String MODEL_SCALE = "scale";
   public static final String MODEL_BOXES = "boxes";
   public static final String MODEL_SPRITES = "sprites";
   public static final String MODEL_SUBMODEL = "submodel";
   public static final String MODEL_SUBMODELS = "submodels";
   public static final String BOX_TEXTURE_OFFSET = "textureOffset";
   public static final String BOX_COORDINATES = "coordinates";
   public static final String BOX_SIZE_ADD = "sizeAdd";
   public static final String ITEM_TYPE_MODEL = "PlayerItem";
   public static final String MODEL_TYPE_BOX = "ModelBox";

   public static PlayerItemModel parseItemModel(JsonObject var0) {
      String var1 = Json.getString(var0, "type");
      if (!Config.equals(var1, "PlayerItem")) {
         throw new JsonParseException("Unknown model type: " + var1);
      } else {
         int[] var2 = Json.parseIntArray(var0.get("textureSize"), 2);
         checkNull(var2, "Missing texture size");
         Dimension var3 = new Dimension(var2[0], var2[1]);
         boolean var4 = Json.getBoolean(var0, "usePlayerTexture", false);
         JsonArray var5 = (JsonArray)var0.get("models");
         checkNull(var5, "Missing elements");
         HashMap var6 = new HashMap();
         ArrayList var7 = new ArrayList();
         new ArrayList();

         for(int var8 = 0; var8 < var5.size(); ++var8) {
            JsonObject var9 = (JsonObject)var5.get(var8);
            String var10 = Json.getString(var9, "baseId");
            if (var10 != null) {
               JsonObject var11 = (JsonObject)var6.get(var10);
               if (var11 == null) {
                  Config.warn("BaseID not found: " + var10);
                  continue;
               }

               Iterator var13 = var11.entrySet().iterator();

               while(var13.hasNext()) {
                  Entry var12 = (Entry)var13.next();
                  if (!var9.has((String)var12.getKey())) {
                     var9.add((String)var12.getKey(), (JsonElement)var12.getValue());
                  }
               }
            }

            String var15 = Json.getString(var9, "id");
            if (var15 != null) {
               if (!var6.containsKey(var15)) {
                  var6.put(var15, var9);
               } else {
                  Config.warn("Duplicate model ID: " + var15);
               }
            }

            PlayerItemRenderer var16 = parseItemRenderer(var9, var3);
            if (var16 != null) {
               var7.add(var16);
            }
         }

         PlayerItemRenderer[] var14 = (PlayerItemRenderer[])var7.toArray(new PlayerItemRenderer[var7.size()]);
         return new PlayerItemModel(var3, var4, var14);
      }
   }

   private static void checkNull(Object var0, String var1) {
      if (var0 == null) {
         throw new JsonParseException(var1);
      }
   }

   private static ResourceLocation makeResourceLocation(String var0) {
      int var1 = var0.indexOf(58);
      if (var1 < 0) {
         return new ResourceLocation(var0);
      } else {
         String var2 = var0.substring(0, var1);
         String var3 = var0.substring(var1 + 1);
         return new ResourceLocation(var2, var3);
      }
   }

   private static int parseAttachModel(String var0) {
      if (var0 == null) {
         return 0;
      } else if (var0.equals("body")) {
         return 0;
      } else if (var0.equals("head")) {
         return 1;
      } else if (var0.equals("leftArm")) {
         return 2;
      } else if (var0.equals("rightArm")) {
         return 3;
      } else if (var0.equals("leftLeg")) {
         return 4;
      } else if (var0.equals("rightLeg")) {
         return 5;
      } else if (var0.equals("cape")) {
         return 6;
      } else {
         Config.warn("Unknown attachModel: " + var0);
         return 0;
      }
   }

   private static PlayerItemRenderer parseItemRenderer(JsonObject var0, Dimension var1) {
      String var2 = Json.getString(var0, "type");
      if (!Config.equals(var2, "ModelBox")) {
         Config.warn("Unknown model type: " + var2);
         return null;
      } else {
         String var3 = Json.getString(var0, "attachTo");
         int var4 = parseAttachModel(var3);
         float var5 = Json.getFloat(var0, "scale", 1.0F);
         ModelPlayerItem var6 = new ModelPlayerItem();
         var6.textureWidth = var1.width;
         var6.textureHeight = var1.height;
         ModelRenderer var7 = parseModelRenderer(var0, var6);
         PlayerItemRenderer var8 = new PlayerItemRenderer(var4, var5, var7);
         return var8;
      }
   }

   private static ModelRenderer parseModelRenderer(JsonObject var0, ModelBase var1) {
      ModelRenderer var2 = new ModelRenderer(var1);
      String var3 = Json.getString(var0, "invertAxis", "").toLowerCase();
      boolean var4 = var3.contains("x");
      boolean var5 = var3.contains("y");
      boolean var6 = var3.contains("z");
      float[] var7 = Json.parseFloatArray(var0.get("translate"), 3, new float[3]);
      if (var4) {
         var7[0] = -var7[0];
      }

      if (var5) {
         var7[1] = -var7[1];
      }

      if (var6) {
         var7[2] = -var7[2];
      }

      float[] var8 = Json.parseFloatArray(var0.get("rotate"), 3, new float[3]);

      for(int var9 = 0; var9 < var8.length; ++var9) {
         var8[var9] = var8[var9] / 180.0F * 3.1415927F;
      }

      if (var4) {
         var8[0] = -var8[0];
      }

      if (var5) {
         var8[1] = -var8[1];
      }

      if (var6) {
         var8[2] = -var8[2];
      }

      var2.setRotationPoint(var7[0], var7[1], var7[2]);
      var2.rotateAngleX = var8[0];
      var2.rotateAngleY = var8[1];
      var2.rotateAngleZ = var8[2];
      String var19 = Json.getString(var0, "mirrorTexture", "").toLowerCase();
      boolean var10 = var19.contains("u");
      boolean var11 = var19.contains("v");
      if (var10) {
         var2.mirror = true;
      }

      if (var11) {
         var2.mirrorV = true;
      }

      JsonArray var12 = var0.getAsJsonArray("boxes");
      JsonObject var14;
      if (var12 != null) {
         for(int var13 = 0; var13 < var12.size(); ++var13) {
            var14 = var12.get(var13).getAsJsonObject();
            int[] var15 = Json.parseIntArray(var14.get("textureOffset"), 2);
            if (var15 == null) {
               throw new JsonParseException("Texture offset not specified");
            }

            float[] var16 = Json.parseFloatArray(var14.get("coordinates"), 6);
            if (var16 == null) {
               throw new JsonParseException("Coordinates not specified");
            }

            if (var4) {
               var16[0] = -var16[0] - var16[3];
            }

            if (var5) {
               var16[1] = -var16[1] - var16[4];
            }

            if (var6) {
               var16[2] = -var16[2] - var16[5];
            }

            float var17 = Json.getFloat(var14, "sizeAdd", 0.0F);
            var2.setTextureOffset(var15[0], var15[1]);
            var2.addBox(var16[0], var16[1], var16[2], (int)var16[3], (int)var16[4], (int)var16[5], var17);
         }
      }

      JsonArray var20 = var0.getAsJsonArray("sprites");
      if (var20 != null) {
         for(int var21 = 0; var21 < var20.size(); ++var21) {
            JsonObject var22 = var20.get(var21).getAsJsonObject();
            int[] var25 = Json.parseIntArray(var22.get("textureOffset"), 2);
            if (var25 == null) {
               throw new JsonParseException("Texture offset not specified");
            }

            float[] var27 = Json.parseFloatArray(var22.get("coordinates"), 6);
            if (var27 == null) {
               throw new JsonParseException("Coordinates not specified");
            }

            if (var4) {
               var27[0] = -var27[0] - var27[3];
            }

            if (var5) {
               var27[1] = -var27[1] - var27[4];
            }

            if (var6) {
               var27[2] = -var27[2] - var27[5];
            }

            float var18 = Json.getFloat(var22, "sizeAdd", 0.0F);
            var2.setTextureOffset(var25[0], var25[1]);
            var2.addSprite(var27[0], var27[1], var27[2], (int)var27[3], (int)var27[4], (int)var27[5], var18);
         }
      }

      var14 = (JsonObject)var0.get("submodel");
      if (var14 != null) {
         ModelRenderer var23 = parseModelRenderer(var14, var1);
         var2.addChild(var23);
      }

      JsonArray var24 = (JsonArray)var0.get("submodels");
      if (var24 != null) {
         for(int var26 = 0; var26 < var24.size(); ++var26) {
            JsonObject var28 = (JsonObject)var24.get(var26);
            ModelRenderer var29 = parseModelRenderer(var28, var1);
            var2.addChild(var29);
         }
      }

      return var2;
   }
}
