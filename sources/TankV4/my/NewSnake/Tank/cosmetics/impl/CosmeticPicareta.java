package my.NewSnake.Tank.cosmetics.impl;

import my.NewSnake.Tank.cosmetics.CosmeticBase;
import my.NewSnake.Tank.cosmetics.CosmeticModelBase;
import my.NewSnake.Tank.module.modules.PLAYER.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class CosmeticPicareta extends CosmeticBase {
   private final CosmeticPicareta.CosmeticVilligerNose2 EggsModel;

   public CosmeticPicareta(RenderPlayer var1) {
      super(var1);
      this.EggsModel = new CosmeticPicareta.CosmeticVilligerNose2(this, var1);
   }

   public void render(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Picareta) {
         Minecraft.getMinecraft();
         if (var1 == Minecraft.thePlayer) {
            GL11.glPushMatrix();
            if (var1.isSneaking()) {
               GlStateManager.translate(0.0D, 0.262D, 0.0D);
            }

            GlStateManager.rotate(var6, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var7, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var5 * 17.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            String var9 = var1.getUniqueID().toString();
            Minecraft.getMinecraft();
            if (var1 == Minecraft.thePlayer) {
               this.EggsModel.render(var1, var2, var3, var5, var6, var7, var8);
            }

            GL11.glPopMatrix();
         }
      }

   }

   public class CosmeticVilligerNose2 extends CosmeticModelBase {
      final CosmeticPicareta this$0;

      public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.25D, 0.25D, 0.25D);
         GlStateManager.translate(2.0D, 1.5D, 0.0D);
         ItemStack var8 = new ItemStack(Items.diamond_pickaxe);
         Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)var1, var8, ItemCameraTransforms.TransformType.NONE);
         GlStateManager.translate(-4.0F, 0.0F, 0.0F);
         Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)var1, var8, ItemCameraTransforms.TransformType.NONE);
         GlStateManager.translate(2.0F, 0.0F, 2.0F);
         Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)var1, var8, ItemCameraTransforms.TransformType.NONE);
         GlStateManager.translate(0.0F, 0.0F, -4.0F);
         Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)var1, var8, ItemCameraTransforms.TransformType.NONE);
         GlStateManager.popMatrix();
      }

      public CosmeticVilligerNose2(CosmeticPicareta var1, RenderPlayer var2) {
         super(var2);
         this.this$0 = var1;
      }
   }
}
