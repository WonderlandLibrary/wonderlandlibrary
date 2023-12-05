package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderingMethods {

    private static final float FADING_HUE = 1F;

    public static float getFadingHue() {
        return FADING_HUE;
    }

    public static void drawBorderedRect(final int x, final int y, final int x1, final int y1, final float width, final int internalColor, final int borderColor) {
        enableGL2D();
        Gui.drawRect(x + (int) width, y + (int) width, x1 - (int) width, y1 - (int) width, internalColor);
        Gui.drawRect(x + (int) width, y, x1 - (int) width, y + (int) width, borderColor);
        Gui.drawRect(x, y, x + (int) width, y1, borderColor);
        Gui.drawRect(x1 - (int) width, y, x1, y1, borderColor);
        Gui.drawRect(x + (int) width, y1 - (int) width, x1 - (int) width, y1, borderColor);
        disableGL2D();
    }


    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    public static void drawBorderedRectReliant2(double x, int y, double x1, int y1, float lineWidth, int inside, int border) {
        enableGL2D();
        drawRectNigger(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f((float) x, y);
        GL11.glVertex2f((float) x, y1);
        GL11.glVertex2f((float) x1, y1);
        GL11.glVertex2f((float) x1, y);
        GL11.glVertex2f((float) x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }


    public static void drawRect(Rectangle rectangle, int color) {
        drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }

    public static void drawRectNigger(double x, int y, double x1, int y1, int color) {
        enableGL2D();
        glColor(color);
        drawRectNigger(x, y, x1, y1, color);
        disableGL2D();
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void enableGL3D() {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
    }

    public static void disableGL3D() {
        GL11.glEnable(2896);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawBorderedRect(int x, int y, int x1, int y1, final int insideC, final int borderC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1, borderC);
        drawVLine(x1 - 1, y, y1, borderC);
        drawHLine(x, x1 - 1, y, borderC);
        drawHLine(x, x1 - 2, y1 - 1, borderC);
        Gui.drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void drawHLine(int x, int y, final int x1, final int y1) {
        if (y < x) {
            final int var5 = x;
            x = y;
            y = var5;
        }
        Gui.drawRect(x, x1, y + 1, x1 + 1, y1);
    }

    public static void drawVLine(final int x, int y, int x1, final int y1) {
        if (x1 < y) {
            final int var5 = y;
            y = x1;
            x1 = var5;
        }
        Gui.drawRect(x, y + 1, x + 1, x1, y1);
    }


    public static void drawTracerLine(double[] pos, float[] c, float width) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(width);
        GL11.glColor4f(c[0], c[1], c[2], c[3]);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
            GL11.glVertex3d(pos[0], pos[1], pos[2]);
        }
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }


}