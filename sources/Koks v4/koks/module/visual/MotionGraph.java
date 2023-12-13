package koks.module.visual;

import koks.api.utils.Resolution;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.Render2DEvent;
import koks.event.TickEvent;
import koks.event.ValueChangeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "MotionGraph", description = "Its shows your movement", category = Module.Category.VISUAL)
public class MotionGraph extends Module {

    private final List<Double> motionSpeed = new ArrayList<>();

    @Value(name = "RGB")
    boolean rgb = false;

    @Value(name = "Width", minimum = 1, maximum = 1000)
    int width = 140;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final ValueChangeEvent valueChangeEvent) {
            if (valueChangeEvent.getValue().getName().equalsIgnoreCase("Width") && valueChangeEvent.getValue().getObject().equals(this)) {
                motionSpeed.clear();
            }
        }

        if (event instanceof TickEvent) {
            motionSpeed.add(Math.hypot(getPlayer().motionX, getPlayer().motionZ) * 100);

            if (motionSpeed.size() > width / 2) {
                motionSpeed.remove(((motionSpeed.size() - ((int) width / 2 + 1))));
            }
        }

        if (event instanceof Render2DEvent) {
            final Resolution resolution = Resolution.getResolution();

            GL11.glPushMatrix();
            GL11.glColor3f(0, 0, 0);
            GL11.glLineWidth(4);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            double add2 = 0;
            for (int i = 0; i < motionSpeed.size(); i++) {
                GL11.glVertex2d(resolution.getWidth() / 2F - width / 2F + add2, resolution.getHeight() - 75 - motionSpeed.get(i));
                add2 += 2;
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            if (!rgb) {
                GL11.glColor3f(1, 1, 1);
            }
            GL11.glLineWidth(2);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            double add = 0;
            int offset = 0;
            for (Double aDouble : motionSpeed) {
                if (rgb) {
                    Color color = getRainbow(offset, 2000, 1, 1);
                    GL11.glColor3f(((float) color.getRed() / 255), ((float) color.getGreen() / 255), ((float) color.getBlue() / 255));
                    offset += 10;
                }
                GL11.glVertex2d(resolution.getWidth() / 2F - width / 2F + add, resolution.getHeight() - 75 - aDouble);
                add += 2;
            }
            GL11.glColor3f(1F,1F,1F);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
