package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.utils.render.Vec4;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.eventmanager.impl.EventTick;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "TrailESP", category = Category.RENDER, description = "Renders a thin line as your movement path")
public class TrailESP extends Module {

    private final List<Vec4> points = new ArrayList<>();

    Setting pointCounter = new Setting("Point Counter", this, 100, 10, 1000, true);
    Setting trailLength = new Setting("Trail Length", this, 1000, 100, 10000, true);
    Setting trailWidth = new Setting("Trail Width", this, 2, 1, 50, true);
    Setting rainbow = new Setting("Rainbow", this, true);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventTick) {
            try {
                if (timeHelper.hasReached((long) pointCounter.getCurrentValue())) {
                    points.add(new Vec4(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, System.currentTimeMillis()));
                }
            } catch (NullPointerException ignored) {
            }
            for (int i = 0; i < points.size(); i++) {
                if (System.currentTimeMillis() - points.get(i).getW() > trailLength.getCurrentValue()) {
                    points.remove(i);
                }
            }
        }

        if (event instanceof EventRender) {
            if (((EventRender) event).getType() == EventRender.Type.threeD) {
                if (rainbow.isToggled()) {
                    GlStateManager.pushMatrix();
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glLineWidth((float) trailWidth.getCurrentValue());
                    GL11.glBegin(GL11.GL_LINE_STRIP);
                    int offset = 0;
                    for (int i = 0; i < points.size(); i++) {
                        Color color = Rainbow.getRainbow(offset, 10000, 1, 1);
                        GL11.glColor3f(((float) color.getRed() / 255), ((float) color.getGreen() / 255), ((float) color.getBlue() / 255));
                        offset += 200;
                        GL11.glVertex3d(points.get(i).getX() - RenderManager.renderPosX, points.get(i).getY() - RenderManager.renderPosY, points.get(i).getZ() - RenderManager.renderPosZ);
                    }
                    GL11.glColor3d(1, 1, 1);
                    GL11.glEnd();
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glLineWidth((float) trailWidth.getCurrentValue());
                    GL11.glColor3d(1, 1, 1);
                    GL11.glBegin(GL11.GL_LINE_STRIP);
                    for (int i = 0; i < points.size(); i++) {
                        GL11.glVertex3d(points.get(i).getX() - RenderManager.renderPosX, points.get(i).getY() - RenderManager.renderPosY, points.get(i).getZ() - RenderManager.renderPosZ);
                    }
                    GL11.glColor3d(1, 1, 1);
                    GL11.glEnd();
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
