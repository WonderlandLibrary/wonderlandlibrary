package me.dev.legacy.util;

import java.awt.Color;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderMethods {
    public static void drawGradientRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        glColor(paramInt1);
        GL11.glVertex2f(paramFloat1, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        glColor(paramInt2);
        GL11.glVertex2f(paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        disableGL2D();
    }

    public static void drawBorderedRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        enableGL2D();
        paramFloat1 *= 2.0F;
        paramFloat3 *= 2.0F;
        paramFloat2 *= 2.0F;
        paramFloat4 *= 2.0F;
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(paramFloat1, paramFloat2, paramFloat4 - 1.0F, paramInt2);
        drawVLine(paramFloat3 - 1.0F, paramFloat2, paramFloat4, paramInt2);
        drawHLine(paramFloat1, paramFloat3 - 1.0F, paramFloat2, paramInt2);
        drawHLine(paramFloat1, paramFloat3 - 2.0F, paramFloat4 - 1.0F, paramInt2);
        drawRect(paramFloat1 + 1.0F, paramFloat2 + 1.0F, paramFloat3 - 1.0F, paramFloat4 - 1.0F, paramInt1);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static void drawOutlinedBox(AxisAlignedBB paramAxisAlignedBB) {
        if (paramAxisAlignedBB == null)
            return;
        GL11.glBegin(3);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
    }

    public static void drawBorderedRectReliant(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, int paramInt1, int paramInt2) {
        enableGL2D();
        drawRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt1);
        glColor(paramInt2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(paramFloat5);
        GL11.glBegin(3);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    public static void drawStrip(int paramInt1, int paramInt2, float paramFloat1, double paramDouble, float paramFloat2, float paramFloat3, int paramInt3) {
        float f1 = (paramInt3 >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt3 >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt3 >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt3 & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glTranslated(paramInt1, paramInt2, 0.0D);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glLineWidth(paramFloat1);
        if (paramDouble > 0.0D) {
            GL11.glBegin(3);
            for (byte b = 0; b < paramDouble; b++) {
                float f5 = (float)(b * paramDouble * Math.PI / paramFloat2);
                float f6 = (float)(Math.cos(f5) * paramFloat3);
                float f7 = (float)(Math.sin(f5) * paramFloat3);
                GL11.glVertex2f(f6, f7);
            }
            GL11.glEnd();
        }
        if (paramDouble < 0.0D) {
            GL11.glBegin(3);
            for (byte b = 0; b > paramDouble; b--) {
                float f5 = (float)(b * paramDouble * Math.PI / paramFloat2);
                float f6 = (float)(Math.cos(f5) * -paramFloat3);
                float f7 = (float)(Math.sin(f5) * -paramFloat3);
                GL11.glVertex2f(f6, f7);
            }
            GL11.glEnd();
        }
        disableGL2D();
        GL11.glDisable(3479);
        GL11.glPopMatrix();
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

    public static void drawBorderedRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, int paramInt1, int paramInt2) {
        enableGL2D();
        glColor(paramInt1);
        drawRect(paramFloat1 + paramFloat5, paramFloat2 + paramFloat5, paramFloat3 - paramFloat5, paramFloat4 - paramFloat5);
        glColor(paramInt2);
        drawRect(paramFloat1 + paramFloat5, paramFloat2, paramFloat3 - paramFloat5, paramFloat2 + paramFloat5);
        drawRect(paramFloat1, paramFloat2, paramFloat1 + paramFloat5, paramFloat4);
        drawRect(paramFloat3 - paramFloat5, paramFloat2, paramFloat3, paramFloat4);
        drawRect(paramFloat1 + paramFloat5, paramFloat4 - paramFloat5, paramFloat3 - paramFloat5, paramFloat4);
        disableGL2D();
    }

    public static void glColor(Color paramColor) {
        GL11.glColor4f(paramColor.getRed() / 255.0F, paramColor.getGreen() / 255.0F, paramColor.getBlue() / 255.0F, paramColor.getAlpha() / 255.0F);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawBox(AxisAlignedBB paramAxisAlignedBB) {
        if (paramAxisAlignedBB == null)
            return;
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
    }

    public static void drawGradientBorderedRectReliant(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, int paramInt1, int paramInt2, int paramInt3) {
        enableGL2D();
        drawGradientRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt3, paramInt2);
        glColor(paramInt1);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(paramFloat5);
        GL11.glBegin(3);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    public static int applyTexture(int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer, boolean paramBoolean1, boolean paramBoolean2) {
        GL11.glBindTexture(3553, paramInt1);
        GL11.glTexParameteri(3553, 10241, paramBoolean1 ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10240, paramBoolean1 ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10242, paramBoolean2 ? 10497 : 10496);
        GL11.glTexParameteri(3553, 10243, paramBoolean2 ? 10497 : 10496);
        GL11.glPixelStorei(3317, 1);
        GL11.glTexImage2D(3553, 0, 32856, paramInt2, paramInt3, 0, 6408, 5121, paramByteBuffer);
        return paramInt1;
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

    public static void enableGL3D(float paramFloat) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        (Minecraft.getMinecraft()).entityRenderer.enableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(paramFloat);
    }

    public static void drawRoundedRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        enableGL2D();
        paramFloat1 *= 2.0F;
        paramFloat2 *= 2.0F;
        paramFloat3 *= 2.0F;
        paramFloat4 *= 2.0F;
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(paramFloat1, paramFloat2 + 1.0F, paramFloat4 - 2.0F, paramInt1);
        drawVLine(paramFloat3 - 1.0F, paramFloat2 + 1.0F, paramFloat4 - 2.0F, paramInt1);
        drawHLine(paramFloat1 + 2.0F, paramFloat3 - 3.0F, paramFloat2, paramInt1);
        drawHLine(paramFloat1 + 2.0F, paramFloat3 - 3.0F, paramFloat4 - 1.0F, paramInt1);
        drawHLine(paramFloat1 + 1.0F, paramFloat1 + 1.0F, paramFloat2 + 1.0F, paramInt1);
        drawHLine(paramFloat3 - 2.0F, paramFloat3 - 2.0F, paramFloat2 + 1.0F, paramInt1);
        drawHLine(paramFloat3 - 2.0F, paramFloat3 - 2.0F, paramFloat4 - 2.0F, paramInt1);
        drawHLine(paramFloat1 + 1.0F, paramFloat1 + 1.0F, paramFloat4 - 2.0F, paramInt1);
        drawRect(paramFloat1 + 1.0F, paramFloat2 + 1.0F, paramFloat3 - 1.0F, paramFloat4 - 1.0F, paramInt2);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static double getDiff(double paramDouble1, double paramDouble2, float paramFloat, double paramDouble3) {
        return paramDouble1 + (paramDouble2 - paramDouble1) * paramFloat - paramDouble3;
    }

    public static void drawRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        GL11.glBegin(7);
        GL11.glVertex2f(paramFloat1, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glEnd();
    }

    public static void renderCrosses(AxisAlignedBB paramAxisAlignedBB) {
        GL11.glBegin(1);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.maxY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.minX, paramAxisAlignedBB.minY, paramAxisAlignedBB.minZ);
        GL11.glVertex3d(paramAxisAlignedBB.maxX, paramAxisAlignedBB.minY, paramAxisAlignedBB.maxZ);
        GL11.glEnd();
    }

    public static void glColor(int paramInt) {
        float f1 = (paramInt >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt & 0xFF) / 255.0F;
        GL11.glColor4f(f2, f3, f4, f1);
    }

    public static void drawHLine(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
        if (paramFloat2 < paramFloat1) {
            float f = paramFloat1;
            paramFloat1 = paramFloat2;
            paramFloat2 = f;
        }
        drawRect(paramFloat1, paramFloat3, paramFloat2 + 1.0F, paramFloat3 + 1.0F, paramInt);
    }

    public static void drawLine(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        GL11.glDisable(3553);
        GL11.glLineWidth(paramFloat5);
        GL11.glBegin(1);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public static void drawRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt) {
        enableGL2D();
        glColor(paramInt);
        drawRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        disableGL2D();
    }

    public static void drawGradientHRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        glColor(paramInt1);
        GL11.glVertex2f(paramFloat1, paramFloat2);
        GL11.glVertex2f(paramFloat1, paramFloat4);
        glColor(paramInt2);
        GL11.glVertex2f(paramFloat3, paramFloat4);
        GL11.glVertex2f(paramFloat3, paramFloat2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        disableGL2D();
    }

    public static void rectangle(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt) {
        if (paramDouble1 < paramDouble3) {
            double d = paramDouble1;
            paramDouble1 = paramDouble3;
            paramDouble3 = d;
        }
        if (paramDouble2 < paramDouble4) {
            double d = paramDouble2;
            paramDouble2 = paramDouble4;
            paramDouble4 = d;
        }
        float f1 = (paramInt >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f3, f4, f1);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferBuilder.pos(paramDouble1, paramDouble4, 0.0D);
        bufferBuilder.pos(paramDouble3, paramDouble4, 0.0D);
        bufferBuilder.pos(paramDouble3, paramDouble2, 0.0D);
        bufferBuilder.pos(paramDouble1, paramDouble2, 0.0D);
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
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

    public static void drawFullCircle(int paramInt1, int paramInt2, double paramDouble, int paramInt3) {
        paramDouble *= 2.0D;
        paramInt1 *= 2;
        paramInt2 *= 2;
        float f1 = (paramInt3 >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt3 >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt3 >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt3 & 0xFF) / 255.0F;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glBegin(6);
        for (byte b = 0; b <= '\u0168'; b++) {
            double d1 = Math.sin(b * Math.PI / 180.0D) * paramDouble;
            double d2 = Math.cos(b * Math.PI / 180.0D) * paramDouble;
            GL11.glVertex2d(paramInt1 + d1, paramInt2 + d2);
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static void drawTriangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        float f1 = (paramInt5 >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt5 >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt5 >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt5 & 0xFF) / 255.0F;
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0F);
        GL11.glShadeModel(7425);
        switch (paramInt3) {
            case 0:
                GL11.glBegin(2);
                GL11.glVertex2d(paramInt1, (paramInt2 + paramInt4));
                GL11.glVertex2d((paramInt1 + paramInt4), (paramInt2 - paramInt4));
                GL11.glVertex2d((paramInt1 - paramInt4), (paramInt2 - paramInt4));
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d(paramInt1, (paramInt2 + paramInt4));
                GL11.glVertex2d((paramInt1 + paramInt4), (paramInt2 - paramInt4));
                GL11.glVertex2d((paramInt1 - paramInt4), (paramInt2 - paramInt4));
                GL11.glEnd();
                break;
            case 1:
                GL11.glBegin(2);
                GL11.glVertex2d(paramInt1, paramInt2);
                GL11.glVertex2d(paramInt1, (paramInt2 + paramInt4 / 2));
                GL11.glVertex2d((paramInt1 + paramInt4 + paramInt4 / 2), paramInt2);
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d(paramInt1, paramInt2);
                GL11.glVertex2d(paramInt1, (paramInt2 + paramInt4 / 2));
                GL11.glVertex2d((paramInt1 + paramInt4 + paramInt4 / 2), paramInt2);
                GL11.glEnd();
                break;
            case 3:
                GL11.glBegin(2);
                GL11.glVertex2d(paramInt1, paramInt2);
                GL11.glVertex2d(paramInt1 + paramInt4 * 1.25D, (paramInt2 - paramInt4 / 2));
                GL11.glVertex2d(paramInt1 + paramInt4 * 1.25D, (paramInt2 + paramInt4 / 2));
                GL11.glEnd();
                GL11.glBegin(4);
                GL11.glVertex2d(paramInt1 + paramInt4 * 1.25D, (paramInt2 - paramInt4 / 2));
                GL11.glVertex2d(paramInt1, paramInt2);
                GL11.glVertex2d(paramInt1 + paramInt4 * 1.25D, (paramInt2 + paramInt4 / 2));
                GL11.glEnd();
                break;
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawGradientRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt1, int paramInt2) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(paramInt1);
        GL11.glVertex2d(paramDouble3, paramDouble2);
        GL11.glVertex2d(paramDouble1, paramDouble2);
        glColor(paramInt2);
        GL11.glVertex2d(paramDouble1, paramDouble4);
        GL11.glVertex2d(paramDouble3, paramDouble4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static Color rainbow(long paramLong, float paramFloat) {
        float f = (float)(System.nanoTime() + paramLong) / 1.0E10F % 1.0F;
        long l = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f, 1.0F, 1.0F)), 16);
        Color color = new Color((int)l);
        return new Color(color.getRed() / 255.0F * paramFloat, color.getGreen() / 255.0F * paramFloat, color.getBlue() / 255.0F * paramFloat, color.getAlpha() / 255.0F);
    }

    public static void drawRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8) {
        enableGL2D();
        GL11.glColor4f(paramFloat5, paramFloat6, paramFloat7, paramFloat8);
        drawRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        disableGL2D();
    }

    public static void glColor(float paramFloat, int paramInt1, int paramInt2, int paramInt3) {
        float f1 = 0.003921569F * paramInt1;
        float f2 = 0.003921569F * paramInt2;
        float f3 = 0.003921569F * paramInt3;
        GL11.glColor4f(f1, f2, f3, paramFloat);
    }

    public static void drawRect(Rectangle paramRectangle, int paramInt) {
        drawRect(paramRectangle.x, paramRectangle.y, (paramRectangle.x + paramRectangle.width), (paramRectangle.y + paramRectangle.height), paramInt);
    }

    public static Color blend(Color paramColor1, Color paramColor2, float paramFloat) {
        float f = 1.0F - paramFloat;
        float[] arrayOfFloat1 = new float[3];
        float[] arrayOfFloat2 = new float[3];
        paramColor1.getColorComponents(arrayOfFloat1);
        paramColor2.getColorComponents(arrayOfFloat2);
        return new Color(arrayOfFloat1[0] * paramFloat + arrayOfFloat2[0] * f, arrayOfFloat1[1] * paramFloat + arrayOfFloat2[1] * f, arrayOfFloat1[2] * paramFloat + arrayOfFloat2[2] * f);
    }

    public static void drawHLine(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2) {
        if (paramFloat2 < paramFloat1) {
            float f = paramFloat1;
            paramFloat1 = paramFloat2;
            paramFloat2 = f;
        }
        drawGradientRect(paramFloat1, paramFloat3, paramFloat2 + 1.0F, paramFloat3 + 1.0F, paramInt1, paramInt2);
    }

    public static void drawBorderedRect(Rectangle paramRectangle, float paramFloat, int paramInt1, int paramInt2) {
        float f1 = paramRectangle.x;
        float f2 = paramRectangle.y;
        float f3 = (paramRectangle.x + paramRectangle.width);
        float f4 = (paramRectangle.y + paramRectangle.height);
        enableGL2D();
        glColor(paramInt1);
        drawRect(f1 + paramFloat, f2 + paramFloat, f3 - paramFloat, f4 - paramFloat);
        glColor(paramInt2);
        drawRect(f1 + 1.0F, f2, f3 - 1.0F, f2 + paramFloat);
        drawRect(f1, f2, f1 + paramFloat, f4);
        drawRect(f3 - paramFloat, f2, f3, f4);
        drawRect(f1 + 1.0F, f4 - paramFloat, f3 - 1.0F, f4);
        disableGL2D();
    }

    public static void drawGradientBorderedRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, float paramFloat, int paramInt1, int paramInt2, int paramInt3) {
        enableGL2D();
        GL11.glPushMatrix();
        glColor(paramInt1);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(1);
        GL11.glVertex2d(paramDouble1, paramDouble2);
        GL11.glVertex2d(paramDouble1, paramDouble4);
        GL11.glVertex2d(paramDouble3, paramDouble4);
        GL11.glVertex2d(paramDouble3, paramDouble2);
        GL11.glVertex2d(paramDouble1, paramDouble2);
        GL11.glVertex2d(paramDouble3, paramDouble2);
        GL11.glVertex2d(paramDouble1, paramDouble4);
        GL11.glVertex2d(paramDouble3, paramDouble4);
        GL11.glEnd();
        GL11.glPopMatrix();
        drawGradientRect(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramInt2, paramInt3);
        disableGL2D();
    }

    public static void drawCircle(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2) {
        paramFloat3 *= 2.0F;
        paramFloat1 *= 2.0F;
        paramFloat2 *= 2.0F;
        float f1 = (paramInt2 >> 24 & 0xFF) / 255.0F;
        float f2 = (paramInt2 >> 16 & 0xFF) / 255.0F;
        float f3 = (paramInt2 >> 8 & 0xFF) / 255.0F;
        float f4 = (paramInt2 & 0xFF) / 255.0F;
        float f5 = (float)(6.2831852D / paramInt1);
        float f6 = (float)Math.cos(f5);
        float f7 = (float)Math.sin(f5);
        float f8 = paramFloat3;
        float f9 = 0.0F;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glBegin(2);
        for (byte b = 0; b < paramInt1; b++) {
            GL11.glVertex2f(f8 + paramFloat1, f9 + paramFloat2);
            float f = f8;
            f8 = f6 * f8 - f7 * f9;
            f9 = f7 * f + f6 * f9;
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static void drawVLine(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
        if (paramFloat3 < paramFloat2) {
            float f = paramFloat2;
            paramFloat2 = paramFloat3;
            paramFloat3 = f;
        }
        drawRect(paramFloat1, paramFloat2 + 1.0F, paramFloat1 + 1.0F, paramFloat3, paramInt);
    }
}
