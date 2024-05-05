package me.aidanmees.trivia.client.tools;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenderUtils2D {
	private static RenderItem	itemRenderer	= null;

	public static ScaledResolution newScaledResolution() {
		return new ScaledResolution(Minecraft.getMinecraft(), mc.displayWidth, mc.displayHeight);
	}

	public static int getMouseX() {
		return Mouse.getX() * newScaledResolution().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
	}

	public static int getMouseY() {
		return newScaledResolution().getScaledHeight() - Mouse.getY() * newScaledResolution().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
	}
	public static void drawTriangle(float x, float y, float width, float height, int color){
		GL11.glPushMatrix();
		GL11.glLineWidth(1F);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, x);
		GL11.glVertex2d(x + width, x + width);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	public static void drawFullCircle(int cx, int cy, double r, int c)
	  {
	    r *= 2.0D;
	    cx *= 2;
	    cy *= 2;
	    float f = (c >> 24 & 0xFF) / 255.0F;
	    float f1 = (c >> 16 & 0xFF) / 255.0F;
	    float f2 = (c >> 8 & 0xFF) / 255.0F;
	    float f3 = (c & 0xFF) / 255.0F;
	    enableGL2D();
	    GL11.glScalef(0.5F, 0.5F, 0.5F);
	    GL11.glColor4f(f1, f2, f3, f);
	    GL11.glBegin(6);
	    for (int i = 0; i <= 360; i++)
	    {
	      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
	      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
	      GL11.glVertex2d(cx + x, cy + y);
	    }
	    GL11.glEnd();
	    GL11.glScalef(2.0F, 2.0F, 2.0F);
	    disableGL2D();
	  }
	public static void drawFillCircle(int cx, int cy, double r, int c, int startpoint, int arc)
	  {
	    r *= 2.0D;
	    cx *= 2;
	    cy *= 2;
	    float f = (c >> 24 & 0xFF) / 255.0F;
	    float f1 = (c >> 16 & 0xFF) / 255.0F;
	    float f2 = (c >> 8 & 0xFF) / 255.0F;
	    float f3 = (c & 0xFF) / 255.0F;
	    enableGL2D();
	    GL11.glScalef(0.5F, 0.5F, 0.5F);
	    GL11.glColor4f(f1, f2, f3, f);
	    GL11.glEnable(GL11.GL_LINE_SMOOTH);
	    GL11.glBegin(6);
	    for (int i = startpoint; i <= arc; i++)
	    {
	      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
	      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
	      GL11.glVertex2d(cx + x, cy + y);
	    }
	    GL11.glEnd();
	    GL11.glDisable(GL11.GL_LINE_SMOOTH);
	    GL11.glScalef(2.0F, 2.0F, 2.0F);
	    disableGL2D();
	  }
	public static void drawArc(float cx, float cy, double r, int c, int startpoint, double arc, int linewidth)
	  {
	    r *= 2.0D;
	    cx *= 2;
	    cy *= 2;
	    float f = (c >> 24 & 0xFF) / 255.0F;
	    float f1 = (c >> 16 & 0xFF) / 255.0F;
	    float f2 = (c >> 8 & 0xFF) / 255.0F;
	    float f3 = (c & 0xFF) / 255.0F;
	    enableGL2D();
	    GL11.glScalef(0.5F, 0.5F, 0.5F);
	    GL11.glLineWidth(linewidth);
	    GL11.glEnable(GL11.GL_LINE_SMOOTH);
	    GL11.glColor4f(f1, f2, f3, f);
	    GL11.glBegin(GL11.GL_LINE_STRIP);
	    for (int i = (int) startpoint; i <= arc; i+=1)
	    {
	      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
	      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
	      GL11.glVertex2d(cx + x, cy + y);
	    }
	    GL11.glEnd();
	    GL11.glDisable(GL11.GL_LINE_SMOOTH);
	    GL11.glScalef(2.0F, 2.0F, 2.0F);
	    disableGL2D();
	  }
	  public static void glColor(int hex)
	  {
	    float alpha = (hex >> 24 & 0xFF) / 255.0F;
	    float red = (hex >> 16 & 0xFF) / 255.0F;
	    float green = (hex >> 8 & 0xFF) / 255.0F;
	    float blue = (hex & 0xFF) / 255.0F;
	    GL11.glColor4f(red, green, blue, alpha);
	  }
	  
	public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVLine(x, y + 1, y1 - 2, borderC);
		drawVLine(x1 - 1, y + 1, y1 - 2, borderC);
		drawHLine(x + 2, x1 - 3, y, borderC);
		drawHLine(x + 2, x1 - 3, y1 - 1, borderC);
		drawHLine(x + 1, x + 1, y + 1, borderC);
		drawHLine(x1 - 2, x1 - 2, y + 1, borderC);
		drawHLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		drawHLine(x + 1, x + 1, y1 - 2, borderC);
		drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}

	public static void drawBorderedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
		x *= 2;
		x1 *= 2;
		y *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVLine(x, y, y1 - 1, borderC);
		drawVLine(x1 - 1, y, y1, borderC);
		drawHLine(x, x1 - 1, y, borderC);
		drawHLine(x, x1 - 2, y1 - 1, borderC);
		drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}
	  public static void enableGL2D()
	  {
	    GL11.glDisable(2929);
	    GL11.glEnable(3042);
	    GL11.glDisable(3553);
	    GL11.glBlendFunc(770, 771);
	    GL11.glDepthMask(true);
	    GL11.glEnable(2848);
	    GL11.glHint(3154, 4354);
	    GL11.glHint(3155, 4354);
	  }
	  public static void disableGL2D()
	  {
	    GL11.glEnable(3553);
	    GL11.glDisable(3042);
	    GL11.glEnable(2929);
	    GL11.glDisable(2848);
	    GL11.glHint(3154, 4352);
	    GL11.glHint(3155, 4352);
	  }
	  
	public static boolean stringListContains(List<String> list, String needle) {
		for (String s : list) {
			if (s.trim().equalsIgnoreCase(needle.trim())) {
				return true;
			}
		}
		return false;
	}

	public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
		drawRect((float) x, (float) y, (float) x2, (float) y2, col2);

		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
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

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawHLine(float par1, float par2, float par3, int par4) {
		if (par2 < par1) {
			float var5 = par1;
			par1 = par2;
			par2 = var5;
		}

		drawRect(par1, par3, par2 + 1, par3 + 1, par4);
	}

	public static void drawVLine(float par1, float par2, float par3, int par4) {
		if (par3 < par2) {
			float var5 = par2;
			par2 = par3;
			par3 = var5;
		}

		drawRect(par1, par2 + 1, par1 + 1, par3, par4);
	}

	public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int paramColor) {
		float alpha = (float) (paramColor >> 24 & 0xFF) / 255F;
		float red = (float) (paramColor >> 16 & 0xFF) / 255F;
		float green = (float) (paramColor >> 8 & 0xFF) / 255F;
		float blue = (float) (paramColor & 0xFF) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(1F);
		GL11.glBegin(GL11.GL_LINES);
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

		drawGradientRect(x, y, x2, y2, col2, col3);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	private static Minecraft	mc	= Minecraft.getMinecraft();

	public static void drawCheck(int x, int y) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0F, 1F, 0F, 1F);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(3);
		GlStateManager.scale(2f, 2f, 1.0f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(x + 1, y + 4);
		GL11.glVertex2f(x + 3, (float) y + 6.5f);
		GL11.glVertex2f(x + 7, y + 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
		GL11.glPushMatrix();
		float f1 = (float) (color >> 24 & 255) / 255.0F;
		float f2 = (float) (color >> 16 & 255) / 255.0F;
		float f3 = (float) (color >> 8 & 255) / 255.0F;
		float f4 = (float) (color & 255) / 255.0F;
		GL11.glTranslatef(x, y, 0);
		GL11.glColor4f(f2, f3, f4, f1);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		if (angle > 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);
			for (int i = 0; i < angle; i++) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * radius);
				float yc = (float) (Math.sin(a) * radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		if (angle < 0) {
			GL11.glBegin(GL11.GL_LINE_STRIP);

			for (int i = 0; i > angle; i--) {
				float a = (float) (i * (angle * Math.PI / points));
				float xc = (float) (Math.cos(a) * -radius);
				float yc = (float) (Math.sin(a) * -radius);
				GL11.glVertex2f(xc, yc);
			}

			GL11.glEnd();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL13.GL_MULTISAMPLE);
		GL11.glDisable(GL11.GL_MAP1_VERTEX_3);
		GL11.glPopMatrix();
	}


}
