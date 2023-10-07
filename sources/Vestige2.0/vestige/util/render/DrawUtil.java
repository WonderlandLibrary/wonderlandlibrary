package vestige.util.render;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import vestige.util.base.IMinecraft;

public class DrawUtil implements IMinecraft {
	
	public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
		GL11.glPushAttrib(0);
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		x *= 2.0D;
		y *= 2.0D;
		x1 *= 2.0D;
    	y1 *= 2.0D;
    	GL11.glEnable(3042);
    	GL11.glDisable(3553);
    	
    	float a = (color >> 24 & 0xFF) / 255.0F;
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		GL11.glColor4f(r, g, b, a);
		
    	GL11.glEnable(2848);
    	GL11.glBegin(9);
    	int i;
    	for (i = 0; i <= 90; i += 3)
    		GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    	for (i = 90; i <= 180; i += 3)
    		GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    	for (i = 0; i <= 90; i += 3)
    		GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
    	for (i = 90; i <= 180; i += 3)
    		GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
    	GL11.glEnd();
    	GL11.glEnable(3553);
    	GL11.glDisable(3042);
    	GL11.glDisable(2848);
    	GL11.glDisable(3042);
    	GL11.glEnable(3553);
    	GL11.glScaled(2.0D, 2.0D, 2.0D);
    	GL11.glPopAttrib();
    	GlStateManager.enableTexture2D();
    	GlStateManager.disableBlend();
	}
	
	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
	
}
