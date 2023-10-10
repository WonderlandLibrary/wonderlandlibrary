package my.NewSnake.Tank.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public abstract class Cosmetic implements LayerRenderer {
   protected final RenderPlayer playerRenderer;
   protected final ModelBiped playerModel;

   public abstract void render(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.hasPlayerInfo() && !var1.isInvisible()) {
         String var10000 = var1.getName();
         Minecraft.getMinecraft();
         if (var10000.equals(Minecraft.thePlayer.getName())) {
            this.render(var1, var2, var3, var4, var5, var6, var7, var8);
         }
      }

   }

   public Cosmetic(RenderPlayer var1) {
      this.playerRenderer = var1;
      this.playerModel = var1.getMainModel();
   }
}
