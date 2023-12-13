package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class NaturalTextures {
   private static NaturalProperties[] propertiesByIndex = new NaturalProperties[0];

   public static void update() {
      propertiesByIndex = new NaturalProperties[0];
      if (Config.isNaturalTextures()) {
         String var0 = "optifine/natural.properties";

         try {
            ResourceLocation var1 = new ResourceLocation(var0);
            if (!Config.hasResource(var1)) {
               Config.dbg("NaturalTextures: configuration \"" + var0 + "\" not found");
               return;
            }

            boolean var2 = Config.isFromDefaultResourcePack(var1);
            InputStream var3 = Config.getResourceStream(var1);
            ArrayList var4 = new ArrayList(256);
            String var5 = Config.readInputStream(var3);
            var3.close();
            String[] var6 = Config.tokenize(var5, "\n\r");
            if (var2) {
               Config.dbg("Natural Textures: Parsing default configuration \"" + var0 + "\"");
               Config.dbg("Natural Textures: Valid only for textures from default resource pack");
            } else {
               Config.dbg("Natural Textures: Parsing configuration \"" + var0 + "\"");
            }

            TextureMap var7 = TextureUtils.getTextureMapBlocks();

            for(int var8 = 0; var8 < var6.length; ++var8) {
               String var9 = var6[var8].trim();
               if (!var9.startsWith("#")) {
                  String[] var10 = Config.tokenize(var9, "=");
                  if (var10.length != 2) {
                     Config.warn("Natural Textures: Invalid \"" + var0 + "\" line: " + var9);
                  } else {
                     String var11 = var10[0].trim();
                     String var12 = var10[1].trim();
                     TextureAtlasSprite var13 = var7.getSpriteSafe("minecraft:blocks/" + var11);
                     if (var13 == null) {
                        Config.warn("Natural Textures: Texture not found: \"" + var0 + "\" line: " + var9);
                     } else {
                        int var14 = var13.getIndexInMap();
                        if (var14 < 0) {
                           Config.warn("Natural Textures: Invalid \"" + var0 + "\" line: " + var9);
                        } else {
                           if (var2 && !Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + var11 + ".png"))) {
                              return;
                           }

                           NaturalProperties var15 = new NaturalProperties(var12);
                           if (var15.isValid()) {
                              while(var4.size() <= var14) {
                                 var4.add((Object)null);
                              }

                              var4.set(var14, var15);
                              Config.dbg("NaturalTextures: " + var11 + " = " + var12);
                           }
                        }
                     }
                  }
               }
            }

            propertiesByIndex = (NaturalProperties[])var4.toArray(new NaturalProperties[var4.size()]);
         } catch (FileNotFoundException var16) {
            Config.warn("NaturalTextures: configuration \"" + var0 + "\" not found");
            return;
         } catch (Exception var17) {
            var17.printStackTrace();
         }
      }

   }

   public static BakedQuad getNaturalTexture(BlockPos var0, BakedQuad var1) {
      TextureAtlasSprite var2 = var1.getSprite();
      if (var2 == null) {
         return var1;
      } else {
         NaturalProperties var3 = getNaturalProperties(var2);
         if (var3 == null) {
            return var1;
         } else {
            int var4 = ConnectedTextures.getSide(var1.getFace());
            int var5 = Config.getRandom(var0, var4);
            int var6 = 0;
            boolean var7 = false;
            if (var3.rotation > 1) {
               var6 = var5 & 3;
            }

            if (var3.rotation == 2) {
               var6 = var6 / 2 * 2;
            }

            if (var3.flip) {
               var7 = (var5 & 4) != 0;
            }

            return var3.getQuad(var1, var6, var7);
         }
      }
   }

   public static NaturalProperties getNaturalProperties(TextureAtlasSprite var0) {
      if (!(var0 instanceof TextureAtlasSprite)) {
         return null;
      } else {
         int var1 = var0.getIndexInMap();
         if (var1 >= 0 && var1 < propertiesByIndex.length) {
            NaturalProperties var2 = propertiesByIndex[var1];
            return var2;
         } else {
            return null;
         }
      }
   }
}
