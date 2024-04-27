package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "Tracers", description = "Draws a line to targets around you.", category = ModuleCategory.RENDER)
public class Tracers extends Module {

    private final FloatValue thicknessValue = new FloatValue("Thickness", 2.0f, 1.0f, 5.0f);
    private final BoolValue distanceColorValue = new BoolValue("DistanceColor", false);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != null && entity != mc.thePlayer && EntityUtils.isSelected(entity, false, false)) {
                int dist = (int) (mc.thePlayer.getDistanceToEntity(entity) * 2);
                if (dist > 255) dist = 255;

                Color color = EntityUtils.isFriend(entity) ? new Color(0, 0, 255, 150) : (distanceColorValue.get() ? new Color(255 - dist, dist, 0, 150) : new Color(255, 255, 255, 150));
                drawTracer(entity, color);
            }
        }
    }

    private void drawTracer(Entity entity, Color color) {
        double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosX);
        double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosY);
        double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosZ);

        Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0)
                .rotatePitch((float) -Math.toRadians(mc.thePlayer.rotationPitch))
                .rotateYaw((float) -Math.toRadians(mc.thePlayer.rotationYaw));

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(thicknessValue.get());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        RenderUtils.glColor(color);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(eyeVector.xCoord, mc.thePlayer.getEyeHeight() + eyeVector.yCoord, eyeVector.zCoord);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + entity.height, z);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();
    }
}
