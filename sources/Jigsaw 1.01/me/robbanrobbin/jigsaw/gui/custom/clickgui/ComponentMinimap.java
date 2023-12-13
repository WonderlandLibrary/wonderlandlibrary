package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.Minimap;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.client.waypoints.Waypoint;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.minimap.MinimapEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ComponentMinimap extends Component {

	private WaitTimer timer = new WaitTimer();
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw() {

		drawMinimap();
		
		GlStateManager.resetColor();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GuiUtils.enableDefaults();
		
	}

	private void drawToFrameBuffer() {
		
		if(mc.player == null) {
			return;
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		GlStateManager.pushMatrix();

		Minimap.frameBuffer.framebufferClear();
		Minimap.frameBuffer.bindFramebuffer(true);
		
		GuiUtils.enableDefaults();

		int translateX = Minimap.renderWidth / 2;
		int translateY = Minimap.renderHeight / 2;
		
		GL11.glTranslated(translateX, translateY, 0);
		
		if(Minimap.mapInfo == null) {
			return;
		}
		
		for (int index = 0; index < Minimap.mapInfo.getBlockEntriesSize(); index++) {

			MinimapEntry.Block entry = Minimap.mapInfo.getBlockEntries().get(index);

			int blockXInWorld = entry.getX();
			int blockZInWorld = entry.getZ();

			if (blockXInWorld > mc.player.posX + Minimap.renderWidth / 2
					|| blockXInWorld < mc.player.posX - Minimap.renderWidth / 2
					|| blockZInWorld > mc.player.posZ + Minimap.renderHeight / 2
					|| blockZInWorld < mc.player.posZ - Minimap.renderHeight / 2) {
				continue;
			}

			// int rgb = entry.getColor();
			//
			// float light = -(entry.getLight() / 100f);
			//
			// float max = 0.5f;
			//
			// if(entry.getColor() != 0x85A6FF) {
			// if(light > max) {
			// light = max;
			// }
			//
			// if(light < -max) {
			// light = -max;
			// }
			//
			// light += 0.6f;
			// }
			// else {
			// light = 1f;
			// }

			int posX = blockXInWorld - (int) mc.player.posX;
			int posY = blockZInWorld - (int) mc.player.posZ;

			GL11.glColor4f(entry.getRed(), entry.getGreen(), entry.getBlue(), 1f);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				glVertex2d(posX, posY);
				glVertex2d(posX + 1, posY);
				glVertex2d(posX + 1, posY + 1);
				glVertex2d(posX, posY + 1);
			}
			GL11.glEnd();

		}
		
		for(int index = 0; index < Minimap.mapInfo.getEntityEntriesSize(); index++) {
			
			MinimapEntry.Entity entry = Minimap.mapInfo.getEntityEntries().get(index);
			
			int posXInWorld = entry.getX();
			int posZInWorld = entry.getZ();
			
			if (posXInWorld > mc.player.posX + Minimap.renderWidth / 2
					|| posXInWorld < mc.player.posX - Minimap.renderWidth / 2
					|| posZInWorld > mc.player.posZ + Minimap.renderHeight / 2
					|| posZInWorld < mc.player.posZ - Minimap.renderHeight / 2) {
				continue;
			}
			
			Entity entity = entry.getEntity();
			
			int posX = posXInWorld - (int) mc.player.posX - 1;
			int posY = posZInWorld - (int) mc.player.posZ - 1;
			
			GuiUtils.setColor(ClientSettings.getForeGroundGuiColor().brighter(), 1f);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				glVertex2d(posX, posY);
				glVertex2d(posX + 2, posY);
				glVertex2d(posX + 2, posY + 2);
				glVertex2d(posX, posY + 2);
			}
			GL11.glEnd();
			
		}
		
		for(int index = 0; index < Minimap.mapInfo.getArrowEntriesSize(); index++) {
			
			MinimapEntry.Arrow entry = Minimap.mapInfo.getArrowEntries().get(index);
			
			int posXInWorld = entry.getX();
			int posZInWorld = entry.getZ();
			
			if (posXInWorld > mc.player.posX + Minimap.renderWidth / 2
					|| posXInWorld < mc.player.posX - Minimap.renderWidth / 2
					|| posZInWorld > mc.player.posZ + Minimap.renderHeight / 2
					|| posZInWorld < mc.player.posZ - Minimap.renderHeight / 2) {
				continue;
			}
			
			EntityArrow arrow = entry.getArrow();
			
			int posX = posXInWorld - (int) mc.player.posX;
			int posY = posZInWorld - (int) mc.player.posZ;
			
			RenderTools.color4f(0.3f, 1f, 1f, 1f);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2i(posX, posY);
				GL11.glVertex2i(posX + 1, posY);
				GL11.glVertex2i(posX + 1, posY + 1);
				GL11.glVertex2i(posX, posY + 1);
			}
			GL11.glEnd();
			
		}
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, -1);
			glVertex2d(0 + 1, -1);
			glVertex2d(0 + 1, -1 + 1);
			glVertex2d(0, -1 + 1);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0 + 1, 0);
			glVertex2d(0 + 1, 0 + 1);
			glVertex2d(0, 0 + 1);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, -2);
			glVertex2d(0 + 1, -2);
			glVertex2d(0 + 1, -2 + 1);
			glVertex2d(0, -2 + 1);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			glVertex2d(1, -1);
			glVertex2d(1 + 1, -1);
			glVertex2d(1 + 1, -1 + 1);
			glVertex2d(1, -1 + 1);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			glVertex2d(-1, -1);
			glVertex2d(-1 + 1, -1);
			glVertex2d(-1 + 1, -1 + 1);
			glVertex2d(-1, -1 + 1);
		}
		GL11.glEnd();
		
		GL11.glTranslated(-translateX, -translateY, 0);

		GuiUtils.disableDefaults();
		
		Minimap.frameBuffer.unbindFramebuffer();
		GlStateManager.popMatrix();
		
		mc.getFramebuffer().bindFramebuffer(true);

		// long timeNow = System.nanoTime();

		// long totalTime = timeNow - timeThen;

		// Jigsaw.chatMessage(totalTime);
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

	}
	
	

	private void drawMinimap() {
		
		if(timer.hasTimeElapsed(50, true)) {
			drawToFrameBuffer();
		}
		
		if (Minimap.mapInfo == null) {
			return;
		}
		
		GuiUtils.translate(this, false);
		
		GlStateManager.pushMatrix();
		Minimap.frameBuffer.framebufferRenderExtNoSetupMatrix(Minimap.frameBuffer.framebufferWidth / 2, Minimap.frameBuffer.framebufferHeight / 2, true, false);
		GlStateManager.popMatrix();

		if(hovered) {
			GuiUtils.enableDefaults();

			double posX = mouseX + 0.5;
			double posY = mouseY - 0.5;
			
			RenderTools.color4f(1f, 1f, 1f, 0.4f);
			GL11.glLineWidth(1.5f);
			
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2d(0, posY);
				GL11.glVertex2d(getWidth(), posY);
			}
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2d(posX, 0);
				GL11.glVertex2d(posX, getHeight());
			}
			GL11.glEnd();
			
			Fonts.font18.drawString(Minimap.coordStringToDraw, posX + 2, posY - 10, 0xffffffff);

			GuiUtils.disableDefaults();
			
		}
		else {
			Minimap.mouseX = -1;
			Minimap.mouseY = -1;
		}
		
		GuiUtils.translate(this, true);
		
		mc.getFramebuffer().bindFramebuffer(true);
		
	}
	
	private double mouseX;
	private double mouseY;
	
	@Override
	public void onHover(double x, double y) {
		mouseX = x;
		mouseY = y;
		Minimap.mouseX = x;
		Minimap.mouseY = y;
		super.onHover(x, y);
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		OverlayWindow wind = Jigsaw.getClickGuiManager().createOverlayWindow();
		
		wind.addChild(new ComponentButton("Add Waypoint", "Adds a waypoint at this location."){
			@Override
			public void onClicked(double x, double y, int button) {
				super.onClicked(x, y, button);
				Jigsaw.getWaypointManager().addWaypoint(new Waypoint(new BlockPos(Minimap.inWorldMouseX, Minimap.inWorldMouseY, Minimap.inWorldMouseZ), "New Waypoint"));
			}
		});
		
		wind.addChild(new ComponentButton("Teleport", "Teleports you using the TpAura pathfinder."){
			@Override
			public void onClicked(double x, double y, int button) {
				super.onClicked(x, y, button);
				Utils.pathFinderTeleportTo(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(Minimap.inWorldMouseX, Minimap.inWorldMouseY, Minimap.inWorldMouseZ));
				mc.player.setPosition(Minimap.inWorldMouseX, Minimap.inWorldMouseY, Minimap.inWorldMouseZ);
			}
		});
		
	}

	@Override
	public double getPreferedWidth() {
		return Minimap.renderWidth;
	}

	@Override
	public double getPreferedHeight() {
		return Minimap.renderHeight;
	}

}
