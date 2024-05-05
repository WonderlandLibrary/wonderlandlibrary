// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.render;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Render
{
    public static void drawRoundedRect(final double x, final double y, final double width, final double height, final float cornerRadius) {
        final int slices = 10;
        drawFillRectangle(x + cornerRadius, y, width - 2.0f * cornerRadius, height);
        drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawCirclePart(x + cornerRadius, y + cornerRadius, -3.1415927f, -1.5707964f, cornerRadius, 10);
        drawCirclePart(x + cornerRadius, y + height - cornerRadius, -1.5707964f, 0.0f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + cornerRadius, 1.5707964f, 3.1415927f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0.0f, 1.5707964f, cornerRadius, 10);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }
    
    public static void drawRoundedLine(final double x, final double y, final double width, final double height, final float cornerRadius) {
        final int slices = 10;
        drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawCirclePart(x + cornerRadius, y + cornerRadius, -3.1415927f, -1.5707964f, cornerRadius, 10);
        drawCirclePart(x + cornerRadius, y + height - cornerRadius, -1.5707964f, 0.0f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + cornerRadius, 1.5707964f, 3.1415927f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0.0f, 1.5707964f, cornerRadius, 10);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }
    
    public static void drawFillRectangle(final double x, final double y, final double width, final double height) {
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glBegin(7);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }
    
    public static void drawCirclePart(final double x, final double y, final float fromAngle, final float toAngle, final float radius, final int slices) {
        GlStateManager.enableBlend();
        GL11.glBegin(6);
        GL11.glVertex2d(x, y);
        final float increment = (toAngle - fromAngle) / slices;
        for (int i = 0; i <= slices; ++i) {
            final float angle = fromAngle + i * increment;
            final float dX = MathHelper.sin(angle);
            final float dY = MathHelper.cos(angle);
            GL11.glVertex2d(x + dX * radius, y + dY * radius);
        }
        GL11.glEnd();
    }
    
    public static void color(final int color) {
        final float red = (color & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color >> 16 & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void colorRGBA(final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, a);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
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
    
    public static void drawFullCircle(double d, double e, double r, final int c) {
        r *= 2.0;
        d *= 2.0;
        e *= 2.0;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(d + x, e + y);
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
}
