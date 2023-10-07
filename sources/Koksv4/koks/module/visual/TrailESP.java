package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.Render3DEvent;
import koks.event.UpdateEvent;
import koks.event.UpdateMotionEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "TrailESP", description = "Its render your positions in to a line", category = Module.Category.VISUAL)
public class TrailESP extends Module {

    @Value(name = "Length", minimum = 5, maximum = 1000)
    int length = 50;

    @Value(name = "Show In FirstPerson")
    boolean showInFirstPerson = true;

    @Value(name = "Flash Effect")
    boolean flashEffect = true;

    @Value(name = "Color", colorPicker = true)
    int color = Color.green.getRGB();

    @Value(name = "Rainbow")
    boolean rainbow = false;

    private final ArrayList<double[]> positions = new ArrayList<>();

    int rainbowOffset;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Color":
                return !rainbow;
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof Render3DEvent) {
            Color customColor = new Color(color);
            if (getGameSettings().thirdPersonView != 0 || showInFirstPerson) {
                GL11.glPushMatrix();

                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glLineWidth(2F);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                int offset = 0;
                for (double[] pos : positions) {
                    if (rainbow)
                        customColor = getRainbow((offset + rainbowOffset) * 100, 6000, 0.8F, 1F);
                    GL11.glColor4f(customColor.getRed() / 255F, customColor.getGreen() / 255F, customColor.getBlue() / 255F, customColor.getAlpha() / 255F);
                    GL11.glVertex3d(pos[0] - mc.getRenderManager().renderPosX, pos[1] - mc.getRenderManager().renderPosY, pos[2] - mc.getRenderManager().renderPosZ);
                    offset++;
                }

                GL11.glVertex3d(0, 0.01, 0);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1, 1, 1, 1);

                GL11.glPopMatrix();
            }
        }

        if (event instanceof UpdateEvent) {
            for (int i = 0; i < positions.size(); i++) {
                final double[] position = positions.get(i);
                if (System.currentTimeMillis() - position[3] > length) {
                    rainbowOffset++;
                    positions.remove(position);
                }
            }
        }

        if (event instanceof UpdateMotionEvent) {
            positions.add(new double[]{getX(), getY() + 0.01, getZ(), System.currentTimeMillis()});
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
