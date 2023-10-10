package my.NewSnake.utils;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ItemUtil {
   public static RenderItem renderItem;
   public static long tick;
   public static Minecraft mc = Minecraft.getMinecraft();
   public static Random random = new Random();
   public static final ResourceLocation RES_ITEM_GLINT;
   public static double rotation;

   public static void doRenderItemPhysic(Entity var0, double var1, double var3, double var5, float var7, float var8) {
      rotation = (double)(System.nanoTime() - tick) / 3000000.0D * 1.0D;
      if (!mc.inGameHasFocus) {
         rotation = 0.0D;
      }

      EntityItem var9 = (EntityItem)var0;
      ItemStack var10 = var9.getEntityItem();
      if (var10.getItem() != null) {
         random.setSeed(187L);
         boolean var11 = false;
         if (TextureMap.locationBlocksTexture != null) {
            mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            var11 = true;
         }

         GlStateManager.enableRescaleNormal();
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.pushMatrix();
         IBakedModel var12 = renderItem.getItemModelMesher().getItemModel(var10);
         int var13 = func_177077_a(var9, var1, var3, var5, var8, var12);
         BlockPos var14 = new BlockPos(var9);
         if (var9.rotationPitch > 360.0F) {
            var9.rotationPitch = 0.0F;
         }

         if (var9 != null && !Double.isNaN((double)var9.getAir()) && !Double.isNaN((double)var9.getEntityId()) && var9.getPosition() != null) {
            if (var9.onGround) {
               if (var9.rotationPitch != 0.0F && var9.rotationPitch != 90.0F && var9.rotationPitch != 180.0F && var9.rotationPitch != 270.0F) {
                  double var15 = formPositiv(var9.rotationPitch);
                  double var17 = formPositiv(var9.rotationPitch - 90.0F);
                  double var19 = formPositiv(var9.rotationPitch - 180.0F);
                  double var21 = formPositiv(var9.rotationPitch - 270.0F);
                  if (var15 <= var17 && var15 <= var19 && var15 <= var21) {
                     if (var9.rotationPitch < 0.0F) {
                        var9.rotationPitch = (float)((double)var9.rotationPitch + rotation);
                     } else {
                        var9.rotationPitch = (float)((double)var9.rotationPitch - rotation);
                     }
                  }

                  if (var17 < var15 && var17 <= var19 && var17 <= var21) {
                     if (var9.rotationPitch - 90.0F < 0.0F) {
                        var9.rotationPitch = (float)((double)var9.rotationPitch + rotation);
                     } else {
                        var9.rotationPitch = (float)((double)var9.rotationPitch - rotation);
                     }
                  }

                  if (var19 < var17 && var19 < var15 && var19 <= var21) {
                     if (var9.rotationPitch - 180.0F < 0.0F) {
                        var9.rotationPitch = (float)((double)var9.rotationPitch + rotation);
                     } else {
                        var9.rotationPitch = (float)((double)var9.rotationPitch - rotation);
                     }
                  }

                  if (var21 < var17 && var21 < var19 && var21 < var15) {
                     if (var9.rotationPitch - 270.0F < 0.0F) {
                        var9.rotationPitch = (float)((double)var9.rotationPitch + rotation);
                     } else {
                        var9.rotationPitch = (float)((double)var9.rotationPitch - rotation);
                     }
                  }
               }
            } else {
               BlockPos var23 = new BlockPos(var9);
               var23.add(0, 1, 0);
               Material var16 = var9.worldObj.getBlockState(var23).getBlock().getMaterial();
               Material var25 = var9.worldObj.getBlockState(var14).getBlock().getMaterial();
               boolean var18 = var9.isInsideOfMaterial(Material.water);
               boolean var26 = var9.inWater;
               if (var18 | var16 == Material.water | var25 == Material.water | var26) {
                  var9.rotationPitch = (float)((double)var9.rotationPitch + rotation / 4.0D);
               } else {
                  var9.rotationPitch = (float)((double)var9.rotationPitch + rotation * 2.0D);
               }
            }
         }

         GL11.glRotatef(var9.rotationYaw, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(var9.rotationPitch + 90.0F, 1.0F, 0.0F, 0.0F);

         for(int var24 = 0; var24 < var13; ++var24) {
            if (var12.isAmbientOcclusion()) {
               GlStateManager.pushMatrix();
               GlStateManager.scale(0.5F, 0.5F, 0.5F);
               renderItem.renderItem(var10, var12, true);
               GlStateManager.popMatrix();
            } else {
               GlStateManager.pushMatrix();
               if (var24 > 0 && shouldSpreadItems()) {
                  GlStateManager.translate(0.0F, 0.0F, 0.046875F * (float)var24);
               }

               renderItem.renderItem(var10, var12, true);
               if (!shouldSpreadItems()) {
                  GlStateManager.translate(0.0F, 0.0F, 0.046875F);
               }

               GlStateManager.popMatrix();
            }
         }

         GlStateManager.popMatrix();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableBlend();
         mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      }

   }

   public static byte getMiniItemCount(ItemStack var0, byte var1) {
      return var1;
   }

   public static byte getMiniBlockCount(ItemStack var0, byte var1) {
      return var1;
   }

   public static double formPositiv(float var0) {
      return var0 > 0.0F ? (double)var0 : (double)(-var0);
   }

   public static int func_177078_a(ItemStack var0) {
      byte var1 = 1;
      if (var0.animationsToGo > 48) {
         var1 = 5;
      } else if (var0.animationsToGo > 32) {
         var1 = 4;
      } else if (var0.animationsToGo > 16) {
         var1 = 3;
      } else if (var0.animationsToGo > 1) {
         var1 = 2;
      }

      return var1;
   }

   public static int func_177077_a(EntityItem var0, double var1, double var3, double var5, float var7, IBakedModel var8) {
      ItemStack var9 = var0.getEntityItem();
      Item var10 = var9.getItem();
      if (var10 == null) {
         return 0;
      } else {
         boolean var11 = var8.isAmbientOcclusion();
         int var12 = func_177078_a(var9);
         float var13 = 0.25F;
         float var14 = 0.0F;
         GlStateManager.translate((float)var1, (float)var3 + var14 + 0.25F, (float)var5);
         float var15 = 0.0F;
         if (var11 || mc.getRenderManager().renderEngine != null && mc.gameSettings.fancyGraphics) {
            GlStateManager.rotate(var15, 0.0F, 1.0F, 0.0F);
         }

         if (!var11) {
            var15 = -0.0F * (float)(var12 - 1) * 0.5F;
            float var16 = -0.0F * (float)(var12 - 1) * 0.5F;
            float var17 = -0.046875F * (float)(var12 - 1) * 0.5F;
            GlStateManager.translate(var15, var16, var17);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         return var12;
      }
   }

   public static boolean shouldSpreadItems() {
      return true;
   }

   static {
      renderItem = mc.getRenderItem();
      RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   }
}
