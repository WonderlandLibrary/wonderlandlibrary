package dev.monsoon.util.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class RenderUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
	public static void drawBoundingBox(AxisAlignedBB aa, float red, float green, float blue, float alpha)  {
        int drawMode = 3;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(3);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.startDrawing(drawMode);
        wr.addVertex(aa.minX, aa.minY, aa.minZ);
        wr.addVertex(aa.minX, aa.minY, aa.maxZ);
        wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
        wr.addVertex(aa.maxX, aa.minY, aa.minZ);
        wr.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.draw();
        wr.startDrawing(drawMode);
        wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
        wr.addVertex(aa.minX, aa.maxY, aa.minZ);
        wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
        wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.draw();
        wr.startDrawing(drawMode);
        wr.addVertex(aa.minX, aa.minY, aa.minZ);
        wr.addVertex(aa.minX, aa.maxY, aa.minZ);
        wr.addVertex(aa.maxX, aa.maxY, aa.minZ);
        wr.addVertex(aa.maxX, aa.minY, aa.minZ);
        wr.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.draw();
        wr.startDrawing(drawMode);
        wr.addVertex(aa.minX, aa.minY, aa.maxZ);
        wr.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        wr.startDrawing(drawMode);
        wr.addVertex(aa.maxX, aa.minY, aa.maxZ);
        wr.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.draw();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawAxisAlignedBBFilled(AxisAlignedBB axisAlignedBB, float r, float g, float b, boolean depth) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        if (depth) glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor4f(r,g,b,0.3f);
        drawBoxFilled(axisAlignedBB);
        glEnable(GL_TEXTURE_2D);
        if (depth) glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static boolean isHovered(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX > x && mouseY > y && mouseX < width && mouseY < height;
    }

    public static void drawBox(BlockPos pos, int r, int g, int b, boolean depth) {
        final RenderManager renderManager = mc.getRenderManager();
        final net.minecraft.util.Timer timer = mc.timer;

        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = mc.theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, pos).expand(.002, .002, .002).offset(-posX, -posY, -posZ);

            drawAxisAlignedBBFilled(axisAlignedBB, r,g,b, depth);
        }
    }

    public static void startSmooth() {
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
    }

    public static void endSmooth() {
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
    }

    public static void drawBoxFilled(AxisAlignedBB axisAlignedBB) {
        glBegin(GL_QUADS);
        {
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
            glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        }
        glEnd();
    }
    public static void drawFilledBox(AxisAlignedBB box, int r, int g, int b, int a)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.5f);

        RenderGlobal.renderFilledBox(box, r, g, b, a);
        //RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5F);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawFilledBoxFromBlockpos(BlockPos blockPos, int r, int g, int b, int a)
    {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ, blockPos.getX() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
        drawFilledBox(axisAlignedBB, r, g, b, a);
    }

    public static void drawBoxFromBlockpos(BlockPos blockPos, int r, int g, int b, int a)
    {
        drawBox(blockPos, r, g, b, true);
    }
	
	public static void tracerLine(double x, double y, double z, Color color)
	{
		x += 0.5 - RenderManager.renderPosX;
		y += 0.5 - RenderManager.renderPosY;
		z += 0.5 - RenderManager.renderPosZ;
        glBlendFunc(770, 771);
        glEnable(GL_BLEND);
        glLineWidth(2.0F);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
       	setColor(color.getRGB());
		glBegin(GL_LINES);
		{
			glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
			glVertex3d(x, y, z);
		}
		glEnd();
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
	}
	
	public static void setColor(int color) {
	    float a = (color >> 24 & 0xFF) / 255.0F;
	    float r = (color >> 16 & 0xFF) / 255.0F;
	    float g = (color >> 8 & 0xFF) / 255.0F;
	    float b = (color & 0xFF) / 255.0F;
	    GL11.glColor4f(r, g, b, a);
	  }
	
	public static int getColorFromPercentage(float current, float max) {
        float percentage = (current / max) / 3;
        return Color.HSBtoRGB(percentage, 1.0F, 1.0F);
    }
	
public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        minX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        minY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        minZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
        maxX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        maxY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        maxZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(3.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
        
        worldrenderer.startDrawing(1);
        worldrenderer.addVertex(minX, minY, minZ);
        worldrenderer.addVertex(maxX, maxY, maxZ);
        tessellator.draw();
        
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
        
	}

	public static boolean SetCustomYaw = false;
	public static float CustomYaw = 0;
	
	public static void setCustomYaw(float customYaw) {
		CustomYaw = customYaw;
		SetCustomYaw = true;
		Minecraft.getMinecraft().thePlayer.rotationYawHead = customYaw;
	}
	
	public static void resetPlayerYaw() {
		SetCustomYaw = false;
	}
	
	public static float getCustomYaw() {
		
		return CustomYaw;
		
	}
	public static boolean SetCustomPitch = false;
	public static float CustomPitch = 0;
	
	public static void setCustomPitch(float customPitch) {
		CustomPitch = customPitch;
		SetCustomPitch = true;
	}
	
	public static void resetPlayerPitch() {
		SetCustomPitch = false;
	}
	
	public static float getCustomPitch() {
		
		return CustomPitch;
		
	}
}
