package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;

import java.awt.*;

public class EnderPearlESP extends Feature {

    public BooleanSetting tracers = new BooleanSetting("Tracers", true, () -> true);

    public EnderPearlESP() {
        super("EnderPearlESP", "Показывает есп перла", Type.Visuals);
        addSettings(tracers);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        GlStateManager.pushMatrix();
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderPearl) {
                boolean viewBobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                mc.gameSettings.viewBobbing = viewBobbing;
                if (tracers.getBoolValue()) {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDepthMask(false);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glLineWidth(1);
                    double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                    double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                    double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                    RenderHelper.setColor(-1);
                    Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                    GL11.glBegin(2);
                    GL11.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                    GL11.glVertex3d(x, y + 1.10, z);
                    GL11.glEnd();
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GL11.glPopMatrix();
                }
                RenderHelper.drawEntityBox(entity, Color.WHITE, true, 0.20F);
            }
        }
        GlStateManager.popMatrix();
    }
}
