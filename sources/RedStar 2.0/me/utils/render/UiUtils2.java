package me.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UiUtils2 {
    public static int width() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public static int anima(int target, int speed) {
        int a = 0;
        if (a < target) {
            a += speed;
        }
        if (a > target) {
            a -= speed;
        }
        return a;
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f), (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawRoundRect(double d, double e, double g, double h, int color) {
        UiUtils2.drawRect(d + 1.0, e, g - 1.0, h, color);
        UiUtils2.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        UiUtils2.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        UiUtils2.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        UiUtils2.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        UiUtils2.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        UiUtils2.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        UiUtils2.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }

    public static void drawRoundRectWithRect(double d, double e, double g, double h, int color) {
        UiUtils2.drawRect(d + 1.0, e, g - 1.0, h, color);
        UiUtils2.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        UiUtils2.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        UiUtils2.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        UiUtils2.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        UiUtils2.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        UiUtils2.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        UiUtils2.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * scaleFactor), (int)(mc.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    public static void stopGlScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void outlineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        UiUtils2.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        UiUtils2.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        UiUtils2.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        UiUtils2.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        UiUtils2.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(new Color(color));
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }
}
