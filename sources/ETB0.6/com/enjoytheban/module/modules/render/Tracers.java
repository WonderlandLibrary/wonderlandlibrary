package com.enjoytheban.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.render.RenderUtil;

import java.awt.*;

/**
 * wicked tracers hacke
 * @author Purity
 */

public class Tracers extends Module {

    public Tracers() {
        super("Tracers", new String[]{"lines", "tracer"}, ModuleType.Render);
        setColor(new Color(60,136,166).getRGB());
    }

    @EventHandler
    private void on3DRender(EventRender3D e) {
        //loopdi doop the entity list
        for (Object o : mc.theWorld.loadedEntityList) {
            //vroom
            Entity entity = (Entity) o;
            //validity check
            if ((entity.isEntityAlive() && entity instanceof EntityPlayer && entity != mc.thePlayer)) {
				final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks()
						- RenderManager.renderPosX;
				final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks()
						- RenderManager.renderPosY;
				final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks()
						- RenderManager.renderPosZ;
				boolean old = mc.gameSettings.viewBobbing;
				RenderUtil.startDrawing();
				mc.gameSettings.viewBobbing = false;
				mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
				mc.gameSettings.viewBobbing = old;
		         float color = Math.round(255D - mc.thePlayer.getDistanceSqToEntity(entity) * 255D
	                        / MathUtil.square(mc.gameSettings.renderDistanceChunks * 2.5)) / 255F;
				drawLine(entity, FriendManager.isFriend(entity.getName()) ? new double[]{0, 1, 1} : new double[]{color, 1 - color, 0},posX, posY, posZ);
				RenderUtil.stopDrawing();
            }
        }
    }

    //the method to draw the LINE
    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
		final float distance = mc.thePlayer.getDistanceToEntity(entity);	
		float xD = distance / 48;
		if(xD >= 1) xD = 1;
		boolean entityesp = false;
		
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (color.length >= 4) {
            if (color[3] <= 0.1) {
                return;
            }
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
		GL11.glLineWidth(1);
		GL11.glBegin(1);
		GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}