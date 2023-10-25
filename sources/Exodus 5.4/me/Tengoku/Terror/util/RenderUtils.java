/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    static EntityLivingBase ent;
    public static float delta;
    public static boolean tracerM;
    public static boolean tracerA;
    static Minecraft mc;
    public static boolean tracerP;
    private static Frustum frustrum;

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        RenderUtils.enableGL2D();
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f8);
        RenderUtils.drawRect(f, f2, f3, f4);
        RenderUtils.disableGL2D();
    }

    public static int getColorFromPercentage(float f, float f2) {
        float f3 = f / f2 / 3.0f;
        return Color.HSBtoRGB(f3, 1.0f, 1.0f);
    }

    public static void drawRect(float f, float f2, float f3, float f4) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)f, (float)f4);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glVertex2f((float)f3, (float)f2);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glEnd();
    }

    public static void drawBorderedRectReliant(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        RenderUtils.enableGL2D();
        RenderUtils.drawRect(f, f2, f3, f4, n);
        RenderUtils.glColor(n2);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)f5);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glVertex2f((float)f, (float)f4);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glVertex2f((float)f3, (float)f2);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderUtils.disableGL2D();
    }

    static {
        mc = Minecraft.getMinecraft();
        tracerP = true;
        tracerM = false;
        tracerA = false;
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void disableGL3D() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static void drawRects(double d, double d2, double d3, double d4, int n) {
        RenderUtils.enableGL2D();
        RenderUtils.glColor(n);
        RenderUtils.drawRectZZ(d, d2, d3, d4);
        RenderUtils.disableGL2D();
    }

    public static void enableGL3D(float f) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)f);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void drawRect(float f, float f2, float f3, float f4, int n) {
        RenderUtils.enableGL2D();
        RenderUtils.glColor(n);
        RenderUtils.drawRect(f, f2, f3, f4);
        RenderUtils.disableGL2D();
    }

    public static void drawBorderedRect(int n, int n2, int n3, int n4, double d, Color color, Color color2) {
        Gui.drawRect((double)n + d, (double)n2 + d, (double)n3 - d, (double)n4 - d, color.getRGB());
        Gui.drawRect((double)n + d, n2, (float)n3 - (float)d, (double)n2 + d, color2.getRGB());
        Gui.drawRect(n, n2, (double)n + d, n4, color2.getRGB());
        Gui.drawRect((double)n3 - d, n2, n3, n4, color2.getRGB());
        Gui.drawRect((double)n + d, (double)n4 - d, (double)n3 - d, n4, color2.getRGB());
    }

    public static boolean isValidForTracers(EntityLivingBase entityLivingBase) {
        if (entityLivingBase.isInvisible()) {
            return false;
        }
        if (entityLivingBase instanceof EntityPlayer && !entityLivingBase.isPlayerSleeping()) {
            return tracerP;
        }
        if (!(entityLivingBase instanceof EntityMob) && !(entityLivingBase instanceof EntitySlime)) {
            return (entityLivingBase instanceof EntityCreature || entityLivingBase instanceof EntitySquid || entityLivingBase instanceof EntityBat || entityLivingBase instanceof EntityVillager) && tracerA;
        }
        return tracerM;
    }

    public static void glColor(float f, int n, int n2, int n3) {
        float f2 = 0.003921569f * (float)n;
        float f3 = 0.003921569f * (float)n2;
        float f4 = 0.003921569f * (float)n3;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawRectZZ(double d, double d2, double d3, double d4) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)((float)d), (float)((float)d4));
        GL11.glVertex2f((float)((float)d3), (float)((float)d4));
        GL11.glVertex2f((float)((float)d3), (float)((float)d2));
        GL11.glVertex2f((float)((float)d), (float)((float)d2));
        GL11.glEnd();
    }
}

