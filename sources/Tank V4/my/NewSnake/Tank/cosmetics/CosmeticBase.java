package my.NewSnake.Tank.cosmetics;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public abstract class CosmeticBase implements LayerRenderer {
   private float partialTicks;
   protected final RenderPlayer playerRenderer;

   public void setPartialTicks(float var1) {
      this.partialTicks = var1;
   }

   private float getAnimationTime(int var1, int var2) {
      float var3 = (float)((System.currentTimeMillis() + (long)var2) % (long)var1);
      return var3 / (float)var1;
   }

   private static float Sigmoid(double var0) {
      return 1.0F / (1.0F + (float)Math.exp(-var0));
   }

   protected ModelRenderer bindTextureAndColor(Color var1, ResourceLocation var2, ModelRenderer var3, ModelRenderer var4) {
      boolean var5 = false;
      if (!var5) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(var2);
      }

      return var3;
   }

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.hasPlayerInfo() && !var1.isInvisible() && var1.getName().equalsIgnoreCase(Minecraft.getMinecraft().getSession().getUsername())) {
         this.render(var1, var2, var3, var4, var5, var6, var7, var8);
      }

   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public abstract void render(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

   protected float getWingAngle(boolean var1, float var2, int var3, int var4, int var5) {
      float var6 = 0.0F;
      int var7 = var3;
      if (var1) {
         var7 = var4;
      }

      float var8 = this.getAnimationTime(var7, var5);
      if (var8 <= 0.5F) {
         var6 = Sigmoid((double)(-4.0F + var8 * 2.0F * 8.0F));
      } else {
         var6 = 1.0F - Sigmoid((double)(-4.0F + (var8 * 2.0F - 1.0F) * 8.0F));
      }

      var6 *= var2;
      return var6;
   }

   protected void setRotation(ModelRenderer var1, float var2, float var3, float var4) {
      var1.rotateAngleX = var2;
      var1.rotateAngleY = var3;
      var1.rotateAngleZ = var4;
   }

   public CosmeticBase(RenderPlayer var1) {
      this.playerRenderer = var1;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
