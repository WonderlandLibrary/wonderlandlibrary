package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;

public class RenderLightningBolt extends Render {
   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityLightningBolt)var1);
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityLightningBolt)var1, var2, var4, var6, var8, var9);
   }

   public RenderLightningBolt(RenderManager var1) {
      super(var1);
   }

   protected ResourceLocation getEntityTexture(EntityLightningBolt var1) {
      return null;
   }

   public void doRender(EntityLightningBolt var1, double var2, double var4, double var6, float var8, float var9) {
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 1);
      double[] var12 = new double[8];
      double[] var13 = new double[8];
      double var14 = 0.0D;
      double var16 = 0.0D;
      Random var18 = new Random(var1.boltVertex);

      int var19;
      for(var19 = 7; var19 >= 0; --var19) {
         var12[var19] = var14;
         var13[var19] = var16;
         var14 += (double)(var18.nextInt(11) - 5);
         var16 += (double)(var18.nextInt(11) - 5);
      }

      for(var19 = 0; var19 < 4; ++var19) {
         Random var20 = new Random(var1.boltVertex);

         for(int var21 = 0; var21 < 3; ++var21) {
            int var22 = 7;
            int var23 = 0;
            if (var21 > 0) {
               var22 = 7 - var21;
            }

            if (var21 > 0) {
               var23 = var22 - 2;
            }

            double var24 = var12[var22] - var14;
            double var26 = var13[var22] - var16;

            for(int var28 = var22; var28 >= var23; --var28) {
               double var29 = var24;
               double var31 = var26;
               if (var21 == 0) {
                  var24 += (double)(var20.nextInt(11) - 5);
                  var26 += (double)(var20.nextInt(11) - 5);
               } else {
                  var24 += (double)(var20.nextInt(31) - 15);
                  var26 += (double)(var20.nextInt(31) - 15);
               }

               var11.begin(5, DefaultVertexFormats.POSITION_COLOR);
               float var33 = 0.5F;
               float var34 = 0.45F;
               float var35 = 0.45F;
               float var36 = 0.5F;
               double var37 = 0.1D + (double)var19 * 0.2D;
               if (var21 == 0) {
                  var37 *= (double)var28 * 0.1D + 1.0D;
               }

               double var39 = 0.1D + (double)var19 * 0.2D;
               if (var21 == 0) {
                  var39 *= (double)(var28 - 1) * 0.1D + 1.0D;
               }

               for(int var41 = 0; var41 < 5; ++var41) {
                  double var42 = var2 + 0.5D - var37;
                  double var44 = var6 + 0.5D - var37;
                  if (var41 == 1 || var41 == 2) {
                     var42 += var37 * 2.0D;
                  }

                  if (var41 == 2 || var41 == 3) {
                     var44 += var37 * 2.0D;
                  }

                  double var46 = var2 + 0.5D - var39;
                  double var48 = var6 + 0.5D - var39;
                  if (var41 == 1 || var41 == 2) {
                     var46 += var39 * 2.0D;
                  }

                  if (var41 == 2 || var41 == 3) {
                     var48 += var39 * 2.0D;
                  }

                  var11.pos(var46 + var24, var4 + (double)(var28 * 16), var48 + var26).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                  var11.pos(var42 + var29, var4 + (double)((var28 + 1) * 16), var44 + var31).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
               }

               var10.draw();
            }
         }
      }

      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
   }
}
