package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomSky {
   private static CustomSkyLayer[][] worldSkyLayers = null;

   public static void reset() {
      worldSkyLayers = null;
   }

   public static void update() {
      reset();
      if (Config.isCustomSky()) {
         worldSkyLayers = readCustomSkies();
      }

   }

   private static CustomSkyLayer[][] readCustomSkies() {
      CustomSkyLayer[][] var0 = new CustomSkyLayer[10][0];
      String var1 = "mcpatcher/sky/world";
      int var2 = -1;

      int var3;
      for(var3 = 0; var3 < var0.length; ++var3) {
         String var4 = var1 + var3 + "/sky";
         ArrayList var5 = new ArrayList();

         for(int var6 = 1; var6 < 1000; ++var6) {
            String var7 = var4 + var6 + ".properties";

            try {
               ResourceLocation var8 = new ResourceLocation(var7);
               InputStream var9 = Config.getResourceStream(var8);
               if (var9 == null) {
                  break;
               }

               Properties var10 = new Properties();
               var10.load(var9);
               var9.close();
               Config.dbg("CustomSky properties: " + var7);
               String var11 = var4 + var6 + ".png";
               CustomSkyLayer var12 = new CustomSkyLayer(var10, var11);
               if (var12.isValid(var7)) {
                  ResourceLocation var13 = new ResourceLocation(var12.source);
                  ITextureObject var14 = TextureUtils.getTexture(var13);
                  if (var14 == null) {
                     Config.log("CustomSky: Texture not found: " + var13);
                  } else {
                     var12.textureId = var14.getGlTextureId();
                     var5.add(var12);
                     var9.close();
                  }
               }
            } catch (FileNotFoundException var15) {
               break;
            } catch (IOException var16) {
               var16.printStackTrace();
            }
         }

         if (var5.size() > 0) {
            CustomSkyLayer[] var19 = (CustomSkyLayer[])var5.toArray(new CustomSkyLayer[var5.size()]);
            var0[var3] = var19;
            var2 = var3;
         }
      }

      if (var2 < 0) {
         return null;
      } else {
         var3 = var2 + 1;
         CustomSkyLayer[][] var17 = new CustomSkyLayer[var3][0];

         for(int var18 = 0; var18 < var17.length; ++var18) {
            var17[var18] = var0[var18];
         }

         return var17;
      }
   }

   public static void renderSky(World var0, TextureManager var1, float var2, float var3) {
      if (worldSkyLayers != null && Config.getGameSettings().renderDistanceChunks >= 8) {
         int var4 = var0.provider.getDimensionId();
         if (var4 >= 0 && var4 < worldSkyLayers.length) {
            CustomSkyLayer[] var5 = worldSkyLayers[var4];
            if (var5 != null) {
               long var6 = var0.getWorldTime();
               int var8 = (int)(var6 % 24000L);

               for(int var9 = 0; var9 < var5.length; ++var9) {
                  CustomSkyLayer var10 = var5[var9];
                  if (var10.isActive(var0, var8)) {
                     var10.render(var8, var2, var3);
                  }
               }

               Blender.clearBlend(var3);
            }
         }
      }

   }

   public static boolean hasSkyLayers(World var0) {
      if (worldSkyLayers == null) {
         return false;
      } else if (Config.getGameSettings().renderDistanceChunks < 8) {
         return false;
      } else {
         int var1 = var0.provider.getDimensionId();
         if (var1 >= 0 && var1 < worldSkyLayers.length) {
            CustomSkyLayer[] var2 = worldSkyLayers[var1];
            return var2 == null ? false : var2.length > 0;
         } else {
            return false;
         }
      }
   }
}
