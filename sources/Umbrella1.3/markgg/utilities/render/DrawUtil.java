/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package markgg.utilities.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class DrawUtil
extends GuiScreen {
    public static DrawUtil instance = new DrawUtil();

    public static void setColor(int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
    }

    public static void drawHollowRect(int x, int y, int w, int h, int color) {
        Gui.drawHorizontalLine(x, x + w, y, color);
        Gui.drawHorizontalLine(x, x + w, y + h, color);
        Gui.drawVerticalLine(x, y + h, y, color);
        Gui.drawVerticalLine(x + w, y + h, y, color);
    }

    public static void drawHollowRect(float x, float y, float w, float h, int color) {
        Gui.drawHorizontalLine(x, w, y, color);
        Gui.drawHorizontalLine(x, w, h, color);
        Gui.drawVerticalLine(x, h, y, color);
        Gui.drawVerticalLine(w, h, y, color);
    }
}

