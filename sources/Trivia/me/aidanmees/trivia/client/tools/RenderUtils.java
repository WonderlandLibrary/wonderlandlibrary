package me.aidanmees.trivia.client.tools;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class RenderUtils {
	
	public static void blockESPBox(BlockPos blockPos) {

		double x = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		// drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(0, 1, 5, 1F);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void setColor(Color c) 
	{
		GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
	}
	
	public static void drawGradientRect(final float x, final float y, final float x1, final float y1,
			final int topColor, final int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}
	
	public static int transparency(final int color, final double alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }

	public static void drawHLine(float x, float y, final float x1, final int y1) {
		if (y < x) {
			final float var5 = x;
			x = y;
			y = var5;
		}
		drawRect(x, x1, y + 2.0f, x1 + 2.0f, y1);
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawRect(final float x, final float y, final float x1, final float y1) {
		
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}

	public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
		enableGL2D();
		
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawVLine(final float x, float y, float x1, final int y1) {
		if (x1 < y) {
			final float var5 = y;
			y = x1;
			x1 = var5;
		}
		drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
	}

	public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
		enableGL2D();
		x *= 2.0f;
		x1 *= 2.0f;
		y *= 2.0f;
		y1 *= 2.0f;
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		drawVLine(x, y, y1, borderC);
		drawVLine(x1 - 1.0f, y, y1, borderC);
		drawHLine(x, x1 - 1.0f, y, borderC);
		drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
		drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		disableGL2D();
	}
	
	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
	
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
	
	public static void drawGradientBorderedRectReliant(final float x, final float y, final float x1, final float y1, final float lineWidth, final int border, final int bottom, final int top) {
        enableGL2D();
        drawGradientRect(x, y, x1, y1, top, bottom);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

	public static void aaa(float x, float y, float x2, float y2, float l1, int col1, int col2)
	  {
	    kek(x, y, x2, y2, col2);
	    float f = (col1 >> 24 & 0xFF) / 255.0F;
	    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
	    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
	    float f4 = (col1 & 0xFF) / 255.0F;
	    GL11.glEnable(3042);
	    GL11.glDisable(3553);
	    GL11.glBlendFunc(770, 771);
	    GL11.glEnable(2848);
	    GL11.glPushMatrix();
	    GL11.glColor4f(f2, f3, f4, f);
	    GL11.glLineWidth(l1);
	    GL11.glBegin(1);
	    GL11.glVertex2d(x, y);
	    GL11.glVertex2d(x, y2);
	    GL11.glVertex2d(x2, y2);
	    GL11.glVertex2d(x2, y);
	    GL11.glVertex2d(x, y);
	    GL11.glVertex2d(x2, y);
	    GL11.glVertex2d(x, y2);
	    GL11.glVertex2d(x2, y2);
	    GL11.glEnd();
	    GL11.glPopMatrix();
	    GL11.glEnable(3553);
	    GL11.glDisable(3042);
	    GL11.glDisable(2848);
	  }
	  
	  public static void kek(float g, float h, float i, float j, int col1)
	  {
	    float f = (col1 >> 24 & 0xFF) / 255.0F;
	    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
	    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
	    float f4 = (col1 & 0xFF) / 255.0F;
	    GL11.glEnable(3042);
	    GL11.glDisable(3553);
	    GL11.glBlendFunc(770, 771);
	    GL11.glEnable(2848);
	    GL11.glPushMatrix();
	    GL11.glColor4f(f2, f3, f4, f);
	    GL11.glBegin(7);
	    GL11.glVertex2d(i, h);
	    GL11.glVertex2d(g, h);
	    GL11.glVertex2d(g, j);
	    GL11.glVertex2d(i, j);
	    GL11.glEnd();
	    GL11.glPopMatrix();
	    GL11.glEnable(3553);
	    GL11.glDisable(3042);
	    GL11.glDisable(2848);
	  }
	  public static void drawVerticalLine(int x, int startY, int endY, int color) {
	        if (endY < startY) {
	            int i = startY;
	            startY = endY;
	            endY = i;
	        }
	        drawRect(x, startY + 2, x + 2, endY, color);
	    }

	    public static void drawHorizontalLine(int startX, int endX, int y, int color) {
	        if (endX < startX) {
	            int i = startX;
	            startX = endX;
	            endX = i;
	        }
	        drawRect(startX, y, endX + 1, y + 1, color);
	    }

	    public static void drawVerticalLine(int x, int startY, int endY) {
	        drawVerticalLine(x, startY, endY, 0x70000000);
	    }

	    public static void drawHorizontalLine(int startX, int endX, int y) {
	        drawHorizontalLine(startX, endX, y, 0x70000000);
	    }
	}

	

