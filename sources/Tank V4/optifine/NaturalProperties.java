package optifine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class NaturalProperties {
   public int rotation = 1;
   public boolean flip = false;
   private Map[] quadMaps = new Map[8];

   public NaturalProperties(String var1) {
      if (var1.equals("4")) {
         this.rotation = 4;
      } else if (var1.equals("2")) {
         this.rotation = 2;
      } else if (var1.equals("F")) {
         this.flip = true;
      } else if (var1.equals("4F")) {
         this.rotation = 4;
         this.flip = true;
      } else if (var1.equals("2F")) {
         this.rotation = 2;
         this.flip = true;
      } else {
         Config.warn("NaturalTextures: Unknown type: " + var1);
      }

   }

   public boolean isValid() {
      return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
   }

   public synchronized BakedQuad getQuad(BakedQuad var1, int var2, boolean var3) {
      int var4 = var2;
      if (var3) {
         var4 = var2 | 4;
      }

      if (var4 > 0 && var4 < this.quadMaps.length) {
         Object var5 = this.quadMaps[var4];
         if (var5 == null) {
            var5 = new IdentityHashMap(1);
            this.quadMaps[var4] = (Map)var5;
         }

         BakedQuad var6 = (BakedQuad)((Map)var5).get(var1);
         if (var6 == null) {
            var6 = this.makeQuad(var1, var2, var3);
            ((Map)var5).put(var1, var6);
         }

         return var6;
      } else {
         return var1;
      }
   }

   private BakedQuad makeQuad(BakedQuad var1, int var2, boolean var3) {
      int[] var4 = var1.getVertexData();
      int var5 = var1.getTintIndex();
      EnumFacing var6 = var1.getFace();
      TextureAtlasSprite var7 = var1.getSprite();
      if (var1 == false) {
         return var1;
      } else {
         var4 = this.transformVertexData(var4, var2, var3);
         BakedQuad var8 = new BakedQuad(var4, var5, var6, var7);
         return var8;
      }
   }

   private int[] transformVertexData(int[] var1, int var2, boolean var3) {
      int[] var4 = (int[])var1.clone();
      int var5 = 4 - var2;
      if (var3) {
         var5 += 3;
      }

      var5 %= 4;
      int var6 = var4.length / 4;

      for(int var7 = 0; var7 < 4; ++var7) {
         int var8 = var7 * var6;
         int var9 = var5 * var6;
         var4[var9 + 4] = var1[var8 + 4];
         var4[var9 + 4 + 1] = var1[var8 + 4 + 1];
         if (var3) {
            --var5;
            if (var5 < 0) {
               var5 = 3;
            }
         } else {
            ++var5;
            if (var5 > 3) {
               var5 = 0;
            }
         }
      }

      return var4;
   }
}
