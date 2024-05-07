package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.eventmanager.impl.EventTick;
import com.masterof13fps.features.modules.Category;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "MotionGraph", category = Category.RENDER, description = "Visualises your movement motion")
public class MotionGraph extends Module {

    private final List<Double> motionSpeed = new ArrayList<>();

    public Setting outline = new Setting("Outline", this, true);
    public Setting rainbow = new Setting("Rainbow", this, true);

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
        if (event instanceof EventTick && mc.currentScreen == null) {
            motionSpeed.add(Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ) * 100);

            if (motionSpeed.size() > 70) {
                motionSpeed.remove((motionSpeed.size() - 71));
            }
        }
        if (event instanceof EventRender) {
            if (((EventRender) event).getType() == EventRender.Type.twoD) {
                ScaledResolution sr = new ScaledResolution(mc);

                if (outline.isToggled()) {
                    if (rainbow.isToggled()) {
                        GL11.glPopMatrix();
                        GL11.glPushMatrix();
                        GL11.glColor3f(0, 0, 0);
                        GL11.glLineWidth(4);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add2 = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            GL11.glVertex2d(sr.width() / 2 - 60 + add2, sr.height() - 75 - motionSpeed.get(i));
                            add2 += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glPopMatrix();

                        GL11.glPushMatrix();
                        GL11.glColor3f(1, 1, 1);
                        GL11.glLineWidth(2);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add = 0;
                        int offset = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            Color color = Rainbow.rainbow(offset, 1);
                            GL11.glColor3f(((float) color.getRed() / 255), ((float) color.getGreen() / 255), ((float) color.getBlue() / 255));
                            offset += 30;
                            GL11.glVertex2d(sr.width() / 2 - 60 + add, sr.height() - 75 - motionSpeed.get(i));
                            add += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                    } else {
                        GL11.glPopMatrix();
                        GL11.glPushMatrix();
                        GL11.glColor3f(0, 0, 0);
                        GL11.glLineWidth(4);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add2 = 0;
                        int offset = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            GL11.glVertex2d(sr.width() / 2 - 60 + add2, sr.height() - 75 - motionSpeed.get(i));
                            add2 += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glPopMatrix();

                        GL11.glPushMatrix();
                        GL11.glColor3f(1, 1, 1);
                        GL11.glLineWidth(2);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            GL11.glVertex2d(sr.width() / 2 - 60 + add, sr.height() - 75 - motionSpeed.get(i));
                            add += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                    }
                } else {
                    if (rainbow.isToggled()) {
                        GL11.glPushMatrix();
                        GL11.glColor3f(1, 1, 1);
                        GL11.glLineWidth(2);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add = 0;
                        int offset = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            Color color = Rainbow.rainbow(offset, 1);
                            GL11.glColor3f(((float) color.getRed() / 255), ((float) color.getGreen() / 255), ((float) color.getBlue() / 255));
                            offset += 30;
                            GL11.glVertex2d(sr.width() / 2 - 60 + add, sr.height() - 75 - motionSpeed.get(i));
                            add += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                    } else {
                        GL11.glPushMatrix();
                        GL11.glColor3f(1, 1, 1);
                        GL11.glLineWidth(2);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        double add = 0;
                        for (int i = 0; i < motionSpeed.size(); i++) {
                            GL11.glVertex2d(sr.width() / 2 - 60 + add, sr.height() - 75 - motionSpeed.get(i));
                            add += 2;
                        }
                        GL11.glEnd();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                    }
                }
            }
        }
    }
}
