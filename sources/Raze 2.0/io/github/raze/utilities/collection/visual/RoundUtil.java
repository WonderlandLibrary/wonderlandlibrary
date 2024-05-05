package io.github.raze.utilities.collection.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RoundUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }

    public static void drawSmoothRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x = (float)(x * 2.0D);
        y = (float)(y * 2.0D);
        x1 = (float)(x1 * 2.0D);
        y1 = (float)(y1 * 2.0D);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        setColor(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        int i;
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d((x + radius) + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, (y + radius) + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d((x + radius) + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, (y1 - radius) + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d((x1 - radius) + Math.sin(i * Math.PI / 180.0D) * radius, (y1 - radius) + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d((x1 - radius) + Math.sin(i * Math.PI / 180.0D) * radius, (y + radius) + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glBegin(2);
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d((x + radius) + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, (y + radius) + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d((x + radius) + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, (y1 - radius) + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d((x1 - radius) + Math.sin(i * Math.PI / 180.0D) * radius, (y1 - radius) + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d((x1 - radius) + Math.sin(i * Math.PI / 180.0D) * radius, (y + radius) + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GL11.glLineWidth(1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
