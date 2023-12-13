package summer.base.utilities;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Render2DUtil {
	public static void drawRect(double left, double top, double right, double bottom, int color) {
	    if (left < right) {
	      double i = left;
	      left = right;
	      right = i;
	    }
	    if (top < bottom) {
	      double j = top;
	      top = bottom;
	      bottom = j;
	    }
	    float f3 = (color >> 24 & 0xFF) / 255.0F;
	    float f = (color >> 16 & 0xFF) / 255.0F;
	    float f1 = (color >> 8 & 0xFF) / 255.0F;
	    float f2 = (color & 0xFF) / 255.0F;
	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	    GlStateManager.enableBlend();
	    GlStateManager.disableTexture2D();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    GlStateManager.color(f, f1, f2, f3);
	    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
	    worldrenderer.pos(left, bottom, 0.0D).endVertex();
	    worldrenderer.pos(right, bottom, 0.0D).endVertex();
	    worldrenderer.pos(right, top, 0.0D).endVertex();
	    worldrenderer.pos(left, top, 0.0D).endVertex();
	    tessellator.draw();
	    GlStateManager.enableTexture2D();
	    GlStateManager.disableBlend();
	  }

	  public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
	    drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0D), top - (!borderIncludedInBounds ? borderWidth : 0.0D), right + (!borderIncludedInBounds ? borderWidth : 0.0D), bottom + (!borderIncludedInBounds ? borderWidth : 0.0D), borderColor);
	    drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0D), top + (borderIncludedInBounds ? borderWidth : 0.0D), right - (borderIncludedInBounds ? borderWidth : 0.0D), bottom - (borderIncludedInBounds ? borderWidth : 0.0D), insideColor);
	  }
	  public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
		    float f = (col1 >> 24 & 0xFF) / 255.0F;
		    float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		    float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		    float f3 = (col1 & 0xFF) / 255.0F;
		    float f4 = (col2 >> 24 & 0xFF) / 255.0F;
		    float f5 = (col2 >> 16 & 0xFF) / 255.0F;
		    float f6 = (col2 >> 8 & 0xFF) / 255.0F;
		    float f7 = (col2 & 0xFF) / 255.0F;
		    GL11.glEnable(3042);
		    GL11.glDisable(3553);
		    GL11.glBlendFunc(770, 771);
		    GL11.glEnable(2848);
		    GL11.glShadeModel(7425);
		    GL11.glPushMatrix();
		    GL11.glBegin(7);
		    GL11.glColor4f(f1, f2, f3, f);
		    GL11.glVertex2d(left, top);
		    GL11.glVertex2d(left, bottom);
		    GL11.glColor4f(f5, f6, f7, f4);
		    GL11.glVertex2d(right, bottom);
		    GL11.glVertex2d(right, top);
		    GL11.glEnd();
		    GL11.glPopMatrix();
		    GL11.glEnable(3553);
			GL11.glDisable(3042);
		}

	public static void drawOutline(double x, double y, double width, double height, double lineWidth, int color) {
		Render2DUtil.drawRect(x, y, x + width, y + lineWidth, color);
		Render2DUtil.drawRect(x, y, x + lineWidth, y + height, color);
		Render2DUtil.drawRect(x, y + height - lineWidth, x + width, y + height, color);
		Render2DUtil.drawRect(x + width - lineWidth, y, x + width, y + height, color);
	}
}
