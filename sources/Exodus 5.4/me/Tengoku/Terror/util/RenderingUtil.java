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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class RenderingUtil {
    static Minecraft mc;
    private static final Frustum frustrum;
    private static final int[] DISPLAY_LISTS_2D;

    public static void drawBorderRect(double d, double d2, double d3, double d4, int n, double d5) {
        RenderingUtil.drawHLine(d, d2, d3, d2, (float)d5, n);
        RenderingUtil.drawHLine(d3, d2, d3, d4, (float)d5, n);
        RenderingUtil.drawHLine(d, d4, d3, d4, (float)d5, n);
        RenderingUtil.drawHLine(d, d4, d, d2, (float)d5, n);
    }

    public static void drawBorderedCircle(float f, float f2, double d, double d2, int n, int n2) {
        RenderingUtil.enableGL2D();
        GlStateManager.enableBlend();
        GL11.glEnable((int)2881);
        RenderingUtil.drawCircle(f, f2, (float)(d - 0.5 + d2), 72, n);
        RenderingUtil.drawFullCircle(f, f2, (float)d, n2);
        GlStateManager.disableBlend();
        GL11.glDisable((int)2881);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderingUtil.disableGL2D();
    }

    public static void drawRect(float f, float f2, float f3, float f4) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)f, (float)f4);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glVertex2f((float)f3, (float)f2);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glEnd();
    }

    public static boolean isInViewFrustrum(AxisAlignedBB axisAlignedBB) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(entity.posX, entity.posY, entity.posZ);
        return frustrum.isBoundingBoxInFrustum(axisAlignedBB);
    }

    public static boolean isMouseOnKeystroke(int n, int n2, int n3, int n4) {
        int n5 = n - 20;
        int n6 = n + 20;
        int n7 = n2;
        int n8 = n2 - 40;
        return n3 >= n5 && n3 <= n6 && n4 >= n7 && n4 <= n8;
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderingUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    static {
        frustrum = new Frustum();
        mc = Minecraft.getMinecraft();
        DISPLAY_LISTS_2D = new int[4];
        int n = 0;
        while (n < DISPLAY_LISTS_2D.length) {
            RenderingUtil.DISPLAY_LISTS_2D[n] = GL11.glGenLists((int)1);
            ++n;
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderingUtil.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderingUtil.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderingUtil.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderingUtil.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderingUtil.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderingUtil.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderingUtil.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderingUtil.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderingUtil.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderingUtil.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderingUtil.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderingUtil.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderingUtil.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderingUtil.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderingUtil.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderingUtil.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
    }

    public static void drawHLine(double d, double d2, double d3, double d4, float f, int n) {
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f3, f4, f5, f2);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)f);
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glLineWidth((float)1.0f);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawPlatform(double d, Color color, double d2) {
        RenderManager renderManager = mc.getRenderManager();
        double d3 = d - RenderManager.renderPosY;
    }

    public static void drawVLine(float f, float f2, float f3, float f4, float f5, int n) {
        if (f5 <= 0.0f) {
            return;
        }
        GL11.glPushMatrix();
        RenderingUtil.pre3D();
        float f6 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        int n2 = GL11.glGetInteger((int)2900);
        GlStateManager.shadeModel(7425);
        GL11.glColor4f((float)f7, (float)f8, (float)f9, (float)f6);
        float f10 = GL11.glGetFloat((int)2849);
        GL11.glLineWidth((float)f5);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)f, (double)f2, (double)0.0);
        GL11.glVertex3d((double)f3, (double)f4, (double)0.0);
        GL11.glEnd();
        GlStateManager.shadeModel(n2);
        GL11.glLineWidth((float)f10);
        RenderingUtil.post3D();
        GL11.glPopMatrix();
    }

    public static void draw3DLine(float f, float f2, float f3, int n) {
        RenderingUtil.pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)3);
        Minecraft.getMinecraft();
        GL11.glVertex3d((double)0.0, (double)Minecraft.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glVertex3d((double)f, (double)f2, (double)f3);
        GL11.glEnd();
        RenderingUtil.post3D();
    }

    public static void drawLoadingCircle(float f, float f2) {
        int n = (int)(System.nanoTime() / 5000000L % 360L);
        RenderingUtil.drawCircle(f, f2, 10.0f, n - 180, n);
    }

    public static void drawBorderRoundedRect(float f, float f2, float f3, float f4, int n, Color color, Color color2) {
        RenderingUtil.drawRoundedRect(f - 1.0f, f2 - 1.0f, f3 + 1.0f, f4 + 1.0f, n + 1, color2);
        RenderingUtil.drawRoundedRect(f, f2, f3, f4, n, color);
    }

    public static void drawLine3D(float f, float f2, float f3, float f4, float f5, float f6, int n) {
        RenderingUtil.pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        float f7 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f8 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f9 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f10 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f8, (float)f9, (float)f10, (float)f7);
        GL11.glLineWidth((float)0.5f);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)f, (double)f2, (double)f3);
        GL11.glVertex3d((double)f4, (double)f5, (double)f6);
        GL11.glEnd();
        RenderingUtil.post3D();
    }

    public static void drawEllipse(float f, float f2, float f3, float f4, int n, int n2) {
        GL11.glPushMatrix();
        f *= 2.0f;
        f2 *= 2.0f;
        float f5 = (float)(Math.PI * 2 / (double)n);
        float f6 = (float)Math.cos(f5);
        float f7 = (float)Math.sin(f5);
        float f8 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f10 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f11 = (float)(n2 & 0xFF) / 255.0f;
        float f12 = 1.0f;
        float f13 = 0.0f;
        RenderingUtil.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f9, (float)f10, (float)f11, (float)f8);
        GL11.glBegin((int)2);
        int n3 = 0;
        while (n3 < n) {
            GL11.glVertex2f((float)(f12 * f3 + f), (float)(f13 * f4 + f2));
            float f14 = f12;
            f12 = f6 * f12 - f7 * f13;
            f13 = f7 * f14 + f6 * f13;
            ++n3;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtil.disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static void drawRect(float f, float f2, float f3, float f4, int n) {
        RenderingUtil.enableGL2D();
        RenderingUtil.glColor(n);
        RenderingUtil.drawRect(f, f2, f3, f4);
        RenderingUtil.disableGL2D();
    }

    public static void rectangleBordered(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        RenderingUtil.rectangle(d + d5, d2 + d5, d3 - d5, d4 - d5, n);
        RenderingUtil.rectangle(d + d5, d2, d3 - d5, d2 + d5, n2);
        RenderingUtil.rectangle(d, d2, d + d5, d4, n2);
        RenderingUtil.rectangle(d3 - d5, d2, d3, d4, n2);
        RenderingUtil.rectangle(d + d5, d4 - d5, d3 - d5, d4, n2);
    }

    public static void drawFullCircle(float f, float f2, float f3, int n) {
        f3 *= 2.0f;
        f *= 2.0f;
        f2 *= 2.0f;
        float f4 = 0.19634953f;
        float f5 = (float)Math.cos(f4);
        float f6 = (float)Math.sin(f4);
        float f7 = f3;
        float f8 = 0.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glBegin((int)2);
        RenderingUtil.glColor(n);
        int n2 = 0;
        while (n2 < 32) {
            GL11.glVertex2f((float)(f7 + f), (float)(f8 + f2));
            GL11.glVertex2f((float)f, (float)f2);
            float f9 = f7;
            f7 = f5 * f7 - f6 * f8;
            f8 = f6 * f9 + f5 * f8;
            ++n2;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void glColor(float f, int n, int n2, int n3) {
        float f2 = 0.003921569f * (float)n;
        float f3 = 0.003921569f * (float)n2;
        float f4 = 0.003921569f * (float)n3;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawLines(AxisAlignedBB axisAlignedBB) {
        GL11.glPushMatrix();
        GL11.glBegin((int)2);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void rectangle(double d, double d2, double d3, double d4, int n) {
        Gui.drawRect(d, d2, d3, d4, n);
    }

    public static void drawPlatform(Entity entity, Color color) {
        RenderManager renderManager = mc.getRenderManager();
        Timer timer = RenderingUtil.mc.timer;
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)timer.renderPartialTicks - RenderManager.renderPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)timer.renderPartialTicks - RenderManager.renderPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(d, d2, d3);
    }

    public static void drawRoundedRect(float f, float f2, float f3, float f4, int n, Color color) {
        Gui.drawRect(f + (float)n, f2, f3 - (float)n, f4, color.getRGB());
        Gui.drawRect(f, f2 + (float)n, f3, f4 - (float)n, color.getRGB());
        RenderingUtil.drawFilledCircle((int)f + n, (int)f2 + n, n, color);
        RenderingUtil.drawFilledCircle((int)f3 - n, (int)f2 + n, n, color);
        RenderingUtil.drawFilledCircle((int)f3 - n, (int)f4 - n, n, color);
        RenderingUtil.drawFilledCircle((int)f + n, (int)f4 - n, n, color);
    }

    public static void drawGradient(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawFilledTriangle(float f, float f2, float f3, float f4, int n, int n2) {
        RenderingUtil.enableGL2D();
        GL11.glTranslated((double)f2, (double)f3, (double)1.0);
        GlStateManager.rotate(f, f2, f3, 1.0f);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.0);
        RenderingUtil.glColor(n);
        GL11.glEnable((int)2881);
        GL11.glBegin((int)4);
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 + f4 / 2.0f));
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 - f4 / 2.0f));
        GL11.glVertex2f((float)(f2 - f4 / 2.0f), (float)f3);
        GL11.glEnd();
        GL11.glLineWidth((float)1.3f);
        RenderingUtil.glColor(n2);
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 + f4 / 2.0f));
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 - f4 / 2.0f));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)(f2 - f4 / 2.0f), (float)f3);
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 - f4 / 2.0f));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2f((float)(f2 + f4 / 2.0f), (float)(f3 + f4 / 2.0f));
        GL11.glVertex2f((float)(f2 - f4 / 2.0f), (float)f3);
        GL11.glEnd();
        GL11.glDisable((int)2881);
        RenderingUtil.disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawRoundedRect(int n, int n2, int n3, int n4, int n5) {
        int n6 = n + n3;
        int n7 = n2 + n4;
        RenderingUtil.drawRect(n + 1, n2, n6 - 1, n7, n5);
        RenderingUtil.drawRect(n, n2 + 1, n6, n7 - 1, n5);
    }

    public static void draw2D(EntityLivingBase entityLivingBase, double d, double d2, double d3, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(d, d2, d3);
        mc.getRenderManager();
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.depthMask(true);
        RenderingUtil.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderingUtil.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.translate(0.0, 21.0 + -(entityLivingBase.getEntityBoundingBox().maxY - entityLivingBase.getEntityBoundingBox().minY) * 12.0, 0.0);
        RenderingUtil.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderingUtil.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.popMatrix();
    }

    public static void drawGradientSideways(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glColor4d((double)255.0, (double)255.0, (double)255.0, (double)255.0);
    }

    public static void drawCircle1(float f, float f2, float f3, int n, int n2) {
        f *= 2.0f;
        f2 *= 2.0f;
        float f4 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n2 & 0xFF) / 255.0f;
        float f8 = (float)(6.2831852 / (double)n);
        float f9 = (float)Math.cos(f8);
        float f10 = (float)Math.sin(f8);
        float f11 = f3 *= 2.0f;
        float f12 = 0.0f;
        RenderingUtil.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glBegin((int)2);
        int n3 = 0;
        while (n3 < n) {
            GL11.glVertex2f((float)(f11 + f), (float)(f12 + f2));
            float f13 = f11;
            f11 = f9 * f11 - f10 * f12;
            f12 = f10 * f13 + f9 * f12;
            ++n3;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtil.disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawFilledCircle(int n, int n2, float f, int n3, Color color) {
        double d = Math.PI * 2 / (double)n3;
        GL11.glPushAttrib((int)8192);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        int n4 = 0;
        while (n4 < n3) {
            float f2 = (float)((double)f * Math.sin((double)n4 * d));
            float f3 = (float)((double)f * Math.cos((double)n4 * d));
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)n + f2), (float)((float)n2 + f3));
            ++n4;
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawCircle(float f, float f2, float f3, int n, int n2) {
        GL11.glPushMatrix();
        f *= 2.0f;
        f2 *= 2.0f;
        float f4 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n2 & 0xFF) / 255.0f;
        float f8 = (float)(6.2831852 / (double)n);
        float f9 = (float)Math.cos(f8);
        float f10 = (float)Math.sin(f8);
        float f11 = f3 *= 2.0f;
        float f12 = 0.0f;
        RenderingUtil.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glBegin((int)2);
        int n3 = 0;
        while (n3 < n) {
            GL11.glVertex2f((float)(f11 + f), (float)(f12 + f2));
            float f13 = f11;
            f11 = f9 * f11 - f10 * f12;
            f12 = f10 * f13 + f9 * f12;
            ++n3;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderingUtil.disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
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

    public static void quickDrawRect(float f, float f2, float f3, float f4) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
    }

    public static void drawFilledCircle(int n, int n2, float f, Color color) {
        int n3 = 50;
        double d = Math.PI * 2 / (double)n3;
        GL11.glPushAttrib((int)8192);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        int n4 = 0;
        while (n4 < n3) {
            float f2 = (float)((double)f * Math.sin((double)n4 * d));
            float f3 = (float)((double)f * Math.cos((double)n4 * d));
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)n + f2), (float)((float)n2 + f3));
            ++n4;
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
}

