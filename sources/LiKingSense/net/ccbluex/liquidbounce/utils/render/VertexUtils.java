/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class VertexUtils {
    public static void start(int mode) {
        GL11.glBegin((int)mode);
    }

    public static void add(double x, double y, Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glVertex2d((double)x, (double)y);
    }

    public static void end() {
        GL11.glEnd();
    }
}

