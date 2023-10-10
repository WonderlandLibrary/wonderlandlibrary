package my.NewSnake.Tank.cosmetics.impl;

import my.NewSnake.Tank.cosmetics.CosmeticBase;
import my.NewSnake.Tank.cosmetics.CosmeticController;
import my.NewSnake.Tank.cosmetics.CosmeticModelBase;
import my.NewSnake.Tank.module.modules.PLAYER.Cosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CosmeticTopHat extends CosmeticBase {
   private final CosmeticTopHat.ModelTopHat modelTopHat;
   private static final ResourceLocation TEXTURE = new ResourceLocation("client/hat.png");

   public CosmeticTopHat(RenderPlayer var1) {
      super(var1);
      this.modelTopHat = new CosmeticTopHat.ModelTopHat(this, var1);
   }

   public void render(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (CosmeticController.shouldRenderTopHat(var1)) {
         GlStateManager.pushMatrix();
         this.playerRenderer.bindTexture(TEXTURE);
         if (var1.isSneaking()) {
            GL11.glTranslated(0.0D, 0.225D, 0.0D);
         }

         float[] var9 = CosmeticController.getTopHatColor(var1);
         GL11.glColor3d((double)var9[0], (double)var9[1], (double)var9[2]);
         this.modelTopHat.render(var1, var2, var3, var5, var6, var7, var8);
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

   }

   private class ModelTopHat extends CosmeticModelBase {
      private ModelRenderer rim;
      private ModelRenderer pointy;
      final CosmeticTopHat this$0;

      public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
         if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Chapeu) {
            this.rim.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.rim.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.rim.rotationPointX = 0.0F;
            this.rim.rotationPointY = 0.0F;
            this.rim.render(var7);
            this.pointy.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.pointy.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.pointy.rotationPointX = 0.0F;
            this.pointy.rotationPointY = 0.0F;
            this.pointy.render(var7);
         }

      }

      public ModelTopHat(CosmeticTopHat var1, RenderPlayer var2) {
         super(var2);
         this.this$0 = var1;
         this.rim = new ModelRenderer(this.playerModel, 0, 0);
         this.rim.addBox(-5.5F, -9.0F, -5.5F, 11, 2, 11);
         this.pointy = new ModelRenderer(this.playerModel, 0, 13);
         this.pointy.addBox(-3.5F, -17.0F, -3.5F, 7, 8, 7);
      }
   }
}
