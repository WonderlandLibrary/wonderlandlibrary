package optifine;

import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.BiomeGenBase;

public class Matches {
   public static boolean block(BlockStateBase var0, MatchBlock[] var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            MatchBlock var3 = var1[var2];
            if (var3.matches(var0)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean block(int var0, int var1, MatchBlock[] var2) {
      if (var2 == null) {
         return true;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            MatchBlock var4 = var2[var3];
            if (var4.matches(var0, var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean blockId(int var0, MatchBlock[] var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            MatchBlock var3 = var1[var2];
            if (var3.getBlockId() == var0) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean metadata(int var0, int[] var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] == var0) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean sprite(TextureAtlasSprite var0, TextureAtlasSprite[] var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] == var0) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean biome(BiomeGenBase var0, BiomeGenBase[] var1) {
      if (var1 == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] == var0) {
               return true;
            }
         }

         return false;
      }
   }
}
