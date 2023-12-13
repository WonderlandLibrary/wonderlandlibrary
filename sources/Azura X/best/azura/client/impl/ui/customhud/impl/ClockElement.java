package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.util.render.RenderUtil;

import java.awt.*;
import java.util.Calendar;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDisable;

public class ClockElement extends Element {

    public ClockElement() {
        super("Clock", 3, 3, 128, 128);
    }

    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        final double radius = (getWidth() + getHeight()) / 4.0;
        final int hour = 6 - Calendar.getInstance().get(Calendar.HOUR),
                minute = 30 - Calendar.getInstance().get(Calendar.MINUTE),
                second = 30 - Calendar.getInstance().get(Calendar.SECOND);
        glLineWidth(1.0f);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.BLACK, 90));
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        glBegin(GL_POLYGON);
        for (int i = 0; i <= 45; i++) glVertex2d(getX() + radius + Math.sin(i * Math.PI / (45.0 / 2.0)) * radius, getY() + radius + Math.cos(i * Math.PI / (45.0 / 2.0)) * radius);
        glEnd();

        RenderUtil.INSTANCE.color(Color.WHITE);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i <= 45; i++) glVertex2d(getX() + radius + Math.sin(i * Math.PI / (45.0 / 2.0)) * radius, getY() + radius + Math.cos(i * Math.PI / (45.0 / 2.0)) * radius);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2d(getX() + radius, getY() + radius);
        glVertex2d(getX() + radius + Math.sin(hour * Math.PI / 6) * radius * 0.5, getY() + radius + Math.cos(hour * Math.PI / 6) * radius * 0.5);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2d(getX() + radius, getY() + radius);
        glVertex2d(getX() + radius + Math.sin(minute * Math.PI / 30) * radius * 0.75, getY() + radius + Math.cos(minute * Math.PI / 30) * radius * 0.75);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2d(getX() + radius, getY() + radius);
        glVertex2d(getX() + radius + Math.sin(second * Math.PI / 30) * radius * 0.85, getY() + radius + Math.cos(second * Math.PI / 30) * radius * 0.85);
        glEnd();

        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }


    @Override
    public Element copy() {
        return new ClockElement();
    }
}
