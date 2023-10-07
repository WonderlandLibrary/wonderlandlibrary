package best.azura.client.util.render;

import best.azura.client.impl.ui.Texture;
import best.azura.client.util.textures.JordanTextureUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class DrawUtil {
	
	
	public static void color(Color color) {
		glColor4f(color.getRed() / 255F,
				color.getGreen() / 255F,
				color.getBlue() / 255F,
				color.getAlpha() / 255F);
	}

	public static void color(int color) {
		color(new Color(color, true));
	}

	public static void glDrawRect(int mode, double left, double top, double right, double bottom, Color color) {
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

		color(color);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		glBegin(mode);
		{
			glVertex2d(left, top);
			glVertex2d(left, bottom);
			glVertex2d(right, bottom);
			glVertex2d(right, top);
		}
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glColor4f(0, 0, 0, 1);
	}

	public static void glDrawRect(double left,
	                              double top,
	                              double right,
	                              double bottom,
	                              Color color) {
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

		color(color);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		glBegin(GL_POLYGON);
		{
			glVertex2d(left, top);
			glVertex2d(left, bottom);
			glVertex2d(right, bottom);
			glVertex2d(right, top);
		}
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glColor4f(0, 0, 0, 1);
	}
	
	public static void glDrawLine(double x,
	                              double y,
	                              double x2,
	                              double y2,
	                              float width,
	                              boolean smooth,
	                              Color color) {
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		if (smooth) {
			glEnable(GL_LINE_SMOOTH);
			glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		}
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);
		glBegin(GL_LINES);
		color(color);
		{
			glVertex2d(x, y);
			glVertex2d(x2, y2);
		}
		glEnd();
		if (smooth) {
			glDisable(GL_LINE_SMOOTH);
			glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
		}
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
	}
}
