// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import vip.Resolute.modules.impl.combat.KillAura;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class MotionPredict extends Module
{
    public MotionPredict() {
        super("MotionPredict", 0, "Predicts target motion", Category.RENDER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            if (KillAura.target == null) {
                return;
            }
            final EventRender3D er = (EventRender3D)e;
            final EntityLivingBase player = KillAura.target;
            GL11.glPushMatrix();
            RenderUtils.pre3D();
            MotionPredict.mc.entityRenderer.setupCameraTransform(MotionPredict.mc.timer.renderPartialTicks, 2);
            final double x = player.prevPosX + (player.posX - player.prevPosX) * er.getPartialTicks() - RenderManager.renderPosX;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * er.getPartialTicks() - RenderManager.renderPosY;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * er.getPartialTicks() - RenderManager.renderPosZ;
            final double xDelta = player.posX - player.prevPosX;
            final double yDelta = player.posY - player.prevPosY;
            final double zDelta = player.posZ - player.prevPosZ;
            double yMotion = 0.0;
            final double initVel = MotionPredict.mc.thePlayer.motionY;
            for (int i = 5; i < 6; ++i) {
                yMotion += initVel - 0.002 * i;
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + xDelta * i, y + (yDelta + yMotion) * i, z + zDelta * i);
                RenderUtils.drawPlatform(player, new Color(0, 255, 88, 75));
                GlStateManager.popMatrix();
            }
            RenderUtils.post3D();
            GL11.glPopMatrix();
        }
    }
}
