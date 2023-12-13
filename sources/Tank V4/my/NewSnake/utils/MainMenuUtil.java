package my.NewSnake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MainMenuUtil {
   public static void drawImg(ResourceLocation var0, int var1, int var2, int var3, int var4) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      Gui.drawModalRectWithCustomSizedTexture((double)var1, (double)var2, 0.0F, 0.0F, (double)var3, (double)var4, (double)var3, (double)var4);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawString(double var0, String var2, float var3, float var4, int var5) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(var0, var0, var0);
      Minecraft.getMinecraft();
      Minecraft.fontRendererObj.drawStringWithShadow(var2, var3, var4, var5);
      GlStateManager.popMatrix();
   }
}
