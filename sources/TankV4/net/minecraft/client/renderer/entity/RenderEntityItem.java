package net.minecraft.client.renderer.entity;

import java.util.Random;
import my.NewSnake.Tank.module.modules.PLAYER.ItemPhysics;
import my.NewSnake.Tank.module.modules.RENDER.NoRender;
import my.NewSnake.utils.ItemUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEntityItem extends Render {
   private static final String __OBFID = "CL_00002442";
   private Random field_177079_e = new Random();
   private final RenderItem field_177080_a;

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.func_177076_a((EntityItem)var1);
   }

   private int func_177078_a(ItemStack var1) {
      byte var2 = 1;
      if (var1.stackSize > 48) {
         var2 = 5;
      } else if (var1.stackSize > 32) {
         var2 = 4;
      } else if (var1.stackSize > 16) {
         var2 = 3;
      } else if (var1.stackSize > 1) {
         var2 = 2;
      }

      return var2;
   }

   private int func_177077_a(EntityItem var1, double var2, double var4, double var6, float var8, IBakedModel var9) {
      ItemStack var10 = var1.getEntityItem();
      Item var11 = var10.getItem();
      if (var11 == null) {
         return 0;
      } else {
         boolean var12 = var9.isAmbientOcclusion();
         int var13 = this.func_177078_a(var10);
         float var14 = 0.25F;
         float var15 = MathHelper.sin(((float)var1.getAge() + var8) / 10.0F + var1.hoverStart) * 0.1F + 0.1F;
         GlStateManager.translate((float)var2, (float)var4 + var15 + 0.25F, (float)var6);
         float var16;
         if (var12 || this.renderManager.options != null && this.renderManager.options.fancyGraphics) {
            var16 = (((float)var1.getAge() + var8) / 20.0F + var1.hoverStart) * 57.295776F;
            GlStateManager.rotate(var16, 0.0F, 1.0F, 0.0F);
         }

         if (!var12) {
            var16 = -0.0F * (float)(var13 - 1) * 0.5F;
            float var17 = -0.0F * (float)(var13 - 1) * 0.5F;
            float var18 = -0.046875F * (float)(var13 - 1) * 0.5F;
            GlStateManager.translate(var16, var17, var18);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         return var13;
      }
   }

   protected ResourceLocation func_177076_a(EntityItem var1) {
      return TextureMap.locationBlocksTexture;
   }

   public RenderEntityItem(RenderManager var1, RenderItem var2) {
      super(var1);
      this.field_177080_a = var2;
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void func_177075_a(EntityItem var1, double var2, double var4, double var6, float var8, float var9) {
      ItemStack var10 = var1.getEntityItem();
      this.field_177079_e.setSeed(187L);
      boolean var11 = false;
      if (this.bindEntityTexture(var1)) {
         this.renderManager.renderEngine.getTexture(this.func_177076_a(var1)).setBlurMipmap(false, false);
         var11 = true;
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();
      IBakedModel var12 = this.field_177080_a.getItemModelMesher().getItemModel(var10);
      int var13 = this.func_177077_a(var1, var2, var4, var6, var9, var12);

      for(int var14 = 0; var14 < var13; ++var14) {
         if (var12.isAmbientOcclusion()) {
            GlStateManager.pushMatrix();
            if (var14 > 0) {
               float var15 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float var16 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float var17 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               GlStateManager.translate(var15, var16, var17);
            }

            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            this.field_177080_a.renderItem(var10, var12);
            GlStateManager.popMatrix();
         } else {
            this.field_177080_a.renderItem(var10, var12);
            GlStateManager.translate(0.0F, 0.0F, 0.046875F);
         }
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.bindEntityTexture(var1);
      if (var11) {
         this.renderManager.renderEngine.getTexture(this.func_177076_a(var1)).restoreLastBlurMipmap();
      }

      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      if (!(new NoRender()).getInstance().isEnabled()) {
         if ((new ItemPhysics()).getInstance().isEnabled()) {
            ItemUtil.doRenderItemPhysic((EntityItem)var1, var2, var4, var6, var8, var9);
         } else {
            this.func_177075_a((EntityItem)var1, var2, var4, var6, var8, var9);
         }
      }

   }
}
