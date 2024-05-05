package me.aidanmees.trivia.client.csgogui.csgogui.util;

import java.awt.Color;
import java.nio.FloatBuffer;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public final class RenderHelper {


	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
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

	public static void drawFullCircle(double d, double e, double r, int c) {
		r *= 2.0D;
		d *= 2.0D;
		e *= 2.0D;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f2 = (c >> 16 & 0xFF) / 255.0F;
		float f3 = (c >> 8 & 0xFF) / 255.0F;
		float f4 = (c & 0xFF) / 255.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(6);
		int i = 0;
		while (i <= 360) {
			double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
			double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
			GL11.glVertex2d(d + x, e + y);
			i++;
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}

}
