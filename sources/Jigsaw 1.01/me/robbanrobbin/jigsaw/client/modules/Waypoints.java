package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.waypoints.Waypoint;
import me.robbanrobbin.jigsaw.client.waypoints.WaypointManager;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Waypoints extends Module {

	public Waypoints() {
		super("Waypoints", Keyboard.KEY_NONE, Category.HIDDEN);
	}

	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}
	
	@Override
	public boolean getEnableAtStartup() {
		return true;
	}
	
	@Override
	public void onRender() {
		WaypointManager manager = Jigsaw.getWaypointManager();
		
		for(Waypoint waypoint : manager.getWaypoints()) {
			
			BlockPos blockPos = waypoint.getBlockPos();
			
			Vec3d renderPos = RenderTools.getRenderPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			
			float red = 1f;
			float green = 0.3f;
			float blue = 0.3f;
			float alpha = 1f;
			
			
			
			float lineWidth = 2f;
			
			double x = renderPos.x;
			double y = renderPos.y;
			double z = renderPos.z;
			
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glColorMask(true, true, true, true);
			
			renderSims(red, green, blue, alpha, x, y, z);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glColorMask(false, true, true, true);
			
			renderSims(red, green, blue, alpha, x, y, z);
			
			
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColorMask(true, true, true, true);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			
		}
		
		for(Waypoint waypoint : manager.getWaypoints()) {
			
			BlockPos blockPos = waypoint.getBlockPos();
			
			Vec3d renderPos = RenderTools.getRenderPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			
			double x = renderPos.x;
			double y = renderPos.y;
			double z = renderPos.z;
			
			double height = 1.5;
			
			String name = waypoint.getName();
			
			FontRenderer fontrenderer = mc.getRenderManager().getFontRenderer();
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.0F, (float) y + height + 0.5F, (float) z);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(-f1, -f1, f1);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			
			GlStateManager.disableDepth();
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			byte b0 = 0;

			int i = fontrenderer.getStringWidth(name) / 2;
			GlStateManager.disableTexture2D();
			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
			bufferBuilder.pos((double) (-i - 1), (double) (-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			bufferBuilder.pos((double) (-i - 1), (double) (8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			bufferBuilder.pos((double) (i + 1), (double) (8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			bufferBuilder.pos((double) (i + 1), (double) (-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			
			fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, b0, 553648127);
			GlStateManager.enableDepth();
			
			GlStateManager.depthMask(true);
			fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, b0, -1);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();



			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
			
		}
		
		super.onRender();
	}
	
	private void renderSims(float red, float green, float blue, float alpha, double x, double y, double z) {
		
		float red2 = red - 0.1f;
		float green2 = green - 0.1f;
		float blue2 = blue - 0.1f;
		float alpha2 = 1f;
		
		float red3 = red - 0.05f;
		float green3 = green - 0.05f;
		float blue3 = blue - 0.05f;
		float alpha3 = 1f;
		
		float red4 = red - 0.075f;
		float green4 = green - 0.075f;
		float blue4 = blue - 0.075f;
		float alpha4 = 1f;
		
		float lineRed = 1f;
		float lineGreen = 1f;
		float lineBlue = 1f;
		float lineAlpha = 1f;
		
		GL11.glColor4f(red3, green3, blue3, alpha3);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex3d(x, y + 0.5, z);
			GL11.glVertex3d(x + 0.4, y + 1, z + 0.4);
			GL11.glVertex3d(x - 0.4, y + 1, z + 0.4);
			GL11.glVertex3d(x - 0.4, y + 1, z - 0.4);
		}
		GL11.glEnd();
		
		GL11.glColor4f(red2, green2, blue2, alpha2);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex3d(x, y + 0.5, z);
			GL11.glVertex3d(x - 0.4, y + 1, z - 0.4);
			GL11.glVertex3d(x + 0.4, y + 1, z - 0.4);
			GL11.glVertex3d(x + 0.4, y + 1, z + 0.4);
		}
		GL11.glEnd();
		
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex3d(x, y + 1.5, z);
			GL11.glVertex3d(x - 0.4, y + 1, z - 0.4);
			GL11.glVertex3d(x - 0.4, y + 1, z + 0.4);
			GL11.glVertex3d(x + 0.4, y + 1, z + 0.4);
		}
		GL11.glEnd();
		
		GL11.glColor4f(red4, green4, blue4, alpha4);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex3d(x, y + 1.5, z);
			GL11.glVertex3d(x + 0.4, y + 1, z + 0.4);
			GL11.glVertex3d(x + 0.4, y + 1, z - 0.4);
			GL11.glVertex3d(x - 0.4, y + 1, z - 0.4);
		}
		GL11.glEnd();
	}

}
