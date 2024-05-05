package epsilon.util.ESP;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class RenderUtils {

	public static void glPre3d() {
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
	}
	
	public static void glPost3d() {
		GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255F;
        float red = (float) (hex >> 16 & 255) / 255F;
        float green = (float) (hex >> 8 & 255) / 255F;
        float blue = (float) (hex & 255) / 255F;
        GL11.glColor4f(red, green, blue, alpha);
    }
	
	public static void drawCircle(double x, double y, double radius, int from, int too, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_POLYGON);
        glColor(color);
        for (int i = from; i <= too; i++) {
            double x2 = Math.sin(((i * Math.PI) / 180)) * radius;
            double y2 = Math.cos(((i * Math.PI) / 180)) * radius;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
	
	public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), height, height);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
	
	public static void drawRoundedRect(float x, float y, float x2, float y2, int roundFactor, int color) {
		float width = x2 - x;
		float height = y2 - y;
        Gui.drawRect(x, y + height / roundFactor,x + height / roundFactor,y + height - height / roundFactor, color);
        Gui.drawRect(x + width - (height / roundFactor), y + height / roundFactor,x + width,y + height - height / roundFactor, color);
        Gui.drawRect(x + height / roundFactor, y, x + width - height / roundFactor, y + height, color);
        drawCircle(x + height / roundFactor, y + height / roundFactor, height / roundFactor, 180,270, color);
        drawCircle(x + height / roundFactor, y + height - height / roundFactor, height / roundFactor, 270, 360, color);
        drawCircle(x + width - height / roundFactor, y + height / roundFactor, height / roundFactor, 90, 180, color);
        drawCircle(x + width - height / roundFactor, y + height - height / roundFactor, height / roundFactor, 0, 90, color);
    }
	
	public static void drawTracerLine(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
	
	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
	}
	
	public static void drawBoundingBox(AxisAlignedBB aa)  {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		tessellator.draw();
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
		worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
		worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
		worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
		tessellator.draw();
	}

	public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		// GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		// GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width , y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width , y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width , y + height, z + width));
		GL11.glLineWidth(lineWdith);
		GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width , y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	
}