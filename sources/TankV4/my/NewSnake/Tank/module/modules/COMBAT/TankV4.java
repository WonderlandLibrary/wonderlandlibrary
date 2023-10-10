package my.NewSnake.Tank.module.modules.COMBAT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@Module.Mod
public class TankV4 extends Module {
   public boolean Image = true;

   public static void drawImg(ResourceLocation var0, double var1, double var3, double var5, double var7) {
      GlStateManager.pushMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      float var9 = 1.0F / (float)var5;
      float var10 = 1.0F / (float)var7;
      Tessellator var11 = Tessellator.getInstance();
      WorldRenderer var12 = var11.getWorldRenderer();
      var12.begin(7, DefaultVertexFormats.POSITION_TEX);
      var12.pos(var1, var3 + var7, 0.0D).tex((double)(0.0F * var9), (double)((0.0F + (float)var7) * var10)).endVertex();
      var12.pos(var1 + var5, var3 + var7, 0.0D).tex((double)((0.0F + (float)var5) * var9), (double)((0.0F + (float)var7) * var10)).endVertex();
      var12.pos(var1 + var5, var3, 0.0D).tex((double)((0.0F + (float)var5) * var9), (double)(0.0F * var10)).endVertex();
      var12.pos(var1, var3, 0.0D).tex((double)(0.0F * var9), (double)(0.0F * var10)).endVertex();
      var11.draw();
      GlStateManager.popMatrix();
   }

   @EventTarget
   private void Render2D(Render2DEvent var1) {
      if (!mc.gameSettings.showDebugInfo && this.Image) {
         drawImg(new ResourceLocation("client/icons/icon.png"), 10.0D, 10.0D, 120.0D, 120.0D);
      }

   }
}
