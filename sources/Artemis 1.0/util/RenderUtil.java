package artemis.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {
	  public static Minecraft mc = Minecraft.getMinecraft();
	
	
	public static void drawImage(GuiIngame gui, ResourceLocation fileLocation, int x, int y, int w, int h, float fw, float fh, float u, float v) {
	     GL11.glEnable(3042);
	     GL11.glBlendFunc(770, 771);
	     GL11.glEnable(3553);
	     GL11.glEnable(2884);
	     GL11.glDisable(2929);
	     GL11.glDepthMask(false);
	     OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	     GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	     mc.getTextureManager().bindTexture(fileLocation);
	     gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);
	     GL11.glDepthMask(true);
	     GL11.glEnable(2929);
	     GL11.glEnable(3008);
	     GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	     GL11.glPushMatrix();
	     GL11.glPopMatrix();
	    }


	public static void drawBorderedRect(float f, int i, int j, int k, float g, int l, int m) {
		
	}


	public static void drawBorderedRect(float f, float g, int j, int k, float g2, int l, int minValue) {
		
	}
}
