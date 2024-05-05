/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Vec3
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class RenderUtil {
    public static float delta;

    public static float smooth(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
        Gui.func_73734_a((int)((int)(x += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(x2 -= (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y2 -= (float)((double)(round / 2.0f) + 0.5))), (int)color);
        RenderUtil.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        RenderUtil.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        RenderUtil.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        RenderUtil.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        Gui.func_73734_a((int)((int)(x - round / 2.0f - 0.5f)), (int)((int)(y + round / 2.0f)), (int)((int)x2), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)x), (int)((int)(y + round / 2.0f)), (int)((int)(x2 + round / 2.0f + 0.5f)), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)(y - round / 2.0f - 0.5f)), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)y), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 + round / 2.0f + 0.5f)), (int)color);
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
        RenderUtil.rectangle(x, y, x + width, y1, borderColor);
        RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor3f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f));
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void circle(float x, float y, float radius, int fill) {
        GL11.glEnable((int)3042);
        R2DUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
        GL11.glDisable((int)3042);
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static void circle(float x, float y, float radius, Color fill) {
        R2DUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color col) {
        int sections = 100;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)((float)col.getRed() / 255.0f), (float)((float)col.getGreen() / 255.0f), (float)((float)col.getBlue() / 255.0f), (float)((float)col.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)xx + x), (float)((float)yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (float)(col >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col & 0xFF) / 255.0f;
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
            GL11.glVertex2f((float)((float)xx + x), (float)((float)yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, int col) {
        float f = (float)(col >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col & 0xFF) / 255.0f;
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
            GL11.glVertex2f((float)(xx + x), (float)(yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col, int xLeft, int yAbove, int xRight, int yUnder) {
        float f = (float)(col >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col & 0xFF) / 255.0f;
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            float xEnd = (float)xx + x;
            float yEnd = (float)yy + y;
            if (xEnd < (float)xLeft) {
                xEnd = xLeft;
            }
            if (xEnd > (float)xRight) {
                xEnd = xRight;
            }
            if (yEnd < (float)yAbove) {
                yEnd = yAbove;
            }
            if (yEnd > (float)yUnder) {
                yEnd = yUnder;
            }
            GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
            GL11.glVertex2f((float)xEnd, (float)yEnd);
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0;
        x1 *= 2.0f;
        y1 *= 2.0f;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)linewidth);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)3);
        int i = startPoint;
        while ((double)i <= arc) {
            double x = Math.sin((double)i * Math.PI / 180.0) * r;
            double y = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)x1 + x), (double)((double)y1 + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
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

    public static void drawRainbowRectVertical(int x, int y, int x1, int y1, int segments, float alpha) {
        if (segments < 1) {
            segments = 1;
        }
        if (segments > y1 - y) {
            segments = y1 - y;
        }
        int segmentLength = (y1 - y) / segments;
        long time = System.nanoTime();
        for (int i = 0; i < segments; ++i) {
            Minecraft.func_71410_x().field_71456_v.func_73729_b(x, y + segmentLength * i - 1, x1, y + (segmentLength + 1) * i, ColorManager.rainbow(time, (float)i * 0.1f, alpha).getRGB(), ColorManager.rainbow(time, ((float)i + 0.1f) * 0.1f, alpha).getRGB());
        }
    }

    public static void drawCircle(int xx, int yy, int radius, Color col) {
        int sections = 70;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)2);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.cos((double)i * dAngle));
            float y = (float)((double)radius * Math.sin((double)i * dAngle));
            GL11.glColor4f((float)((float)col.getRed() / 255.0f), (float)((float)col.getGreen() / 255.0f), (float)((float)col.getBlue() / 255.0f), (float)((float)col.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)xx + x), (float)((float)yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)((double)delta * speed);
        if (animation < finalState) {
            if (animation + (double)add >= finalState) {
                return finalState;
            }
            return animation += (double)add;
        }
        if (animation - (double)add <= finalState) {
            return finalState;
        }
        return animation -= (double)add;
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, int color) {
        RenderUtil.drawRect(x, y, x + width, y + lineWidth, color);
        RenderUtil.drawRect(x, y, x + lineWidth, y + height, color);
        RenderUtil.drawRect(x, y + height - lineWidth, x + width, y + height, color);
        RenderUtil.drawRect(x + width - lineWidth, y, x + width, y + height, color);
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double)Minecraft.func_71410_x().field_71428_T.field_74281_c;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float[] getRGBAs(int rgb) {
        return new float[]{(float)(rgb >> 16 & 0xFF) / 255.0f, (float)(rgb >> 8 & 0xFF) / 255.0f, (float)(rgb & 0xFF) / 255.0f, (float)(rgb >> 24 & 0xFF) / 255.0f};
    }

    public static Color makeDarker(Color curColor, int distance) {
        int red = curColor.getRed();
        int green = curColor.getGreen();
        int blue = curColor.getBlue();
        green -= distance;
        blue -= distance;
        if ((red -= distance) < 0) {
            red = 0;
        }
        if (green < 0) {
            green = 0;
        }
        if (blue < 0) {
            blue = 0;
        }
        return new Color(red, green, blue);
    }

    public static void drawBoundingBox(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179097_i();
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        float scale = 0.1f;
        GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
        RenderUtil.drawCircle(x *= 10.0, y *= 10.0, radius *= 10.0f, insideC);
        GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
    }

    public static int getHexRGB(int hex) {
        return 0xFF000000 | hex;
    }

    public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image2) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)sr.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void drawBorderedRect(double x2, double d2, double x22, double e2, float l1, int col1, int col2) {
        RenderUtil.drawRect(x2, d2, x22, e2, col2);
        float f2 = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f22 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x2, (double)d2);
        GL11.glVertex2d((double)x2, (double)e2);
        GL11.glVertex2d((double)x22, (double)e2);
        GL11.glVertex2d((double)x22, (double)d2);
        GL11.glVertex2d((double)x2, (double)d2);
        GL11.glVertex2d((double)x22, (double)d2);
        GL11.glVertex2d((double)x2, (double)e2);
        GL11.glVertex2d((double)x22, (double)e2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        R2DUtils.enableGL2D();
        RenderUtil.glColor(borderColor);
        R2DUtils.drawRect(x + width, y, x1 - width, y + width);
        R2DUtils.drawRect(x, y, x + width, y1);
        R2DUtils.drawRect(x1 - width, y, x1, y1);
        R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        R2DUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
        R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
        R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
        R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        R2DUtils.disableGL2D();
    }

    public static void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }

    public static void setupRender(boolean start) {
        if (start) {
            GlStateManager.func_179147_l();
            GL11.glEnable((int)2848);
            GlStateManager.func_179097_i();
            GlStateManager.func_179090_x();
            GlStateManager.func_179112_b((int)770, (int)771);
            GL11.glHint((int)3154, (int)4354);
        } else {
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
            GL11.glDisable((int)2848);
            GlStateManager.func_179126_j();
        }
        GlStateManager.func_179132_a((!start ? 1 : 0) != 0);
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
        R2DUtils.drawRect(x1, y1, x2, y2, outline);
        R2DUtils.drawRect(x1 + 1.0, y1 + 1.0, x2 - 1.0, y2 - 1.0, inline);
        R2DUtils.drawRect(x1 + 2.0, y1 + 2.0, x2 - 2.0, y2 - 2.0, background);
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float)redRGB;
        float green = 0.003921569f * (float)greenRGB;
        float blue = 0.003921569f * (float)blueRGB;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
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

    public static void drawVerticalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer world = tessellator.func_178180_c();
        world.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        world.func_181662_b(right, top, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        world.func_181662_b(left, top, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        world.func_181662_b(left, bottom, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        world.func_181662_b(right, bottom, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawHorizontalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer world = tessellator.func_178180_c();
        world.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        world.func_181662_b(left, top, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        world.func_181662_b(left, bottom, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        world.func_181662_b(right, bottom, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        world.func_181662_b(right, top, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation image2, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        GL11.glEnable((int)2881);
        GL11.glDisable((int)2881);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        GL11.glEnable((int)32925);
        GL11.glEnable((int)2881);
        float alpha = (float)(c >> 24 & 0xFF) / 255.0f;
        float red = (float)(c >> 16 & 0xFF) / 255.0f;
        float green = (float)(c >> 8 & 0xFF) / 255.0f;
        float blue = (float)(c & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean line = GL11.glIsEnabled((int)2848);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (!line) {
            GL11.glEnable((int)2848);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)(x + Math.sin((double)i * 3.141526 / 180.0) * radius), (double)(y + Math.cos((double)i * 3.141526 / 180.0) * radius));
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!line) {
            GL11.glDisable((int)2848);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2881);
        GL11.glClear((int)0);
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtil.glColor(c);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
        RenderUtil.glColor(c);
        GL11.glPopMatrix();
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldRenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float)(a4 >> 24 & 0xFF) / 255.0f;
        float a8 = (float)(a4 >> 16 & 0xFF) / 255.0f;
        float a9 = (float)(a4 >> 8 & 0xFF) / 255.0f;
        float a10 = (float)(a4 & 0xFF) / 255.0f;
        float a11 = (float)(a5 >> 24 & 0xFF) / 255.0f;
        float a12 = (float)(a5 >> 16 & 0xFF) / 255.0f;
        float a13 = (float)(a5 >> 8 & 0xFF) / 255.0f;
        float a14 = (float)(a5 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)a8, (float)a9, (float)a10, (float)a7);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawBorderedRect(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        RenderUtil.drawRect(x2, y2, x22, y22, col2);
        float f2 = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f22 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x22, (double)y22);
        GL11.glVertex2d((double)x22, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x22, (double)y2);
        GL11.glVertex2d((double)x2, (double)y22);
        GL11.glVertex2d((double)x22, (double)y22);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawBordRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void color(int color, float alpha) {
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static Vec3 interpolateRender(EntityPlayer player) {
        float part = Minecraft.func_71410_x().field_71428_T.field_74281_c;
        double interpX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)part;
        double interpY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)part;
        double interpZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)part;
        return new Vec3(interpX, interpY, interpZ);
    }

    public static void disableSmoothLine() {
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

    public static void enableSmoothLine(float width) {
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
        GL11.glLineWidth((float)width);
    }

    public static void drawCylinderESP(EntityLivingBase entity, double x, double y, double z) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179097_i();
        GL11.glTranslated((double)x, (double)y, (double)z);
        GL11.glRotatef((float)(-entity.field_70130_N), (float)0.0f, (float)1.0f, (float)0.0f);
        RenderUtil.glColor(new Color(1, 89, 1, 150).getRGB());
        RenderUtil.enableSmoothLine(0.1f);
        Cylinder c = new Cylinder();
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        c.setDrawStyle(100011);
        c.draw(0.0f, 0.2f, 0.5f, 5, 300);
        RenderUtil.disableSmoothLine();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)(y + 0.5), (double)z);
        GL11.glRotatef((float)(-entity.field_70130_N), (float)0.0f, (float)1.0f, (float)0.0f);
        RenderUtil.glColor(new Color(2, 168, 2, 150).getRGB());
        RenderUtil.enableSmoothLine(0.1f);
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        c.setDrawStyle(100011);
        c.draw(0.2f, 0.0f, 0.5f, 5, 300);
        RenderUtil.disableSmoothLine();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, 1.0f, 1.0f).getRGB();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.func_152125_a((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GlStateManager.func_179091_B();
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glEnable((int)2881);
        GL11.glDisable((int)2881);
        RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.func_179118_c();
        GlStateManager.func_179101_C();
        GlStateManager.func_179140_f();
        GlStateManager.func_179101_C();
        GL11.glDisable((int)2848);
        GlStateManager.func_179084_k();
        GL11.glPopMatrix();
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        int scaleFactor = 1;
        int k = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && Minecraft.func_71410_x().field_71443_c / (scaleFactor + 1) >= 320 && Minecraft.func_71410_x().field_71440_d / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.func_71410_x().field_71440_d - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    public static class R2DUtils {
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

        public static void disableGL2D() {
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glHint((int)3154, (int)4352);
            GL11.glHint((int)3155, (int)4352);
        }

        public static void drawCircle(double x, double y, double radius, int c) {
            float f2 = (float)(c >> 24 & 0xFF) / 255.0f;
            float f22 = (float)(c >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(c & 0xFF) / 255.0f;
            GlStateManager.func_179092_a((int)516, (float)0.001f);
            GlStateManager.func_179131_c((float)f22, (float)f3, (float)f4, (float)f2);
            GlStateManager.func_179141_d();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            Tessellator tes = Tessellator.func_178181_a();
            for (double i = 0.0; i < 360.0; i += 1.0) {
                double f5 = Math.sin(i * Math.PI / 180.0) * radius;
                double f6 = Math.cos(i * Math.PI / 180.0) * radius;
                GL11.glVertex2d((double)((double)f3 + x), (double)((double)f4 + y));
            }
            GlStateManager.func_179084_k();
            GlStateManager.func_179118_c();
            GlStateManager.func_179098_w();
            GlStateManager.func_179092_a((int)516, (float)0.1f);
        }

        public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
            Gui.func_73734_a((int)((int)(x += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(x2 -= (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y2 -= (float)((double)(round / 2.0f) + 0.5))), (int)color);
            R2DUtils.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
            R2DUtils.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
            R2DUtils.circle(x + round / 2.0f, y + round / 2.0f, round, color);
            R2DUtils.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
            Gui.func_73734_a((int)((int)(x - round / 2.0f - 0.5f)), (int)((int)(y + round / 2.0f)), (int)((int)x2), (int)((int)(y2 - round / 2.0f)), (int)color);
            Gui.func_73734_a((int)((int)x), (int)((int)(y + round / 2.0f)), (int)((int)(x2 + round / 2.0f + 0.5f)), (int)((int)(y2 - round / 2.0f)), (int)color);
            Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)(y - round / 2.0f - 0.5f)), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 - round / 2.0f)), (int)color);
            Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)y), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 + round / 2.0f + 0.5f)), (int)color);
        }

        public static void circle(float x, float y, float radius, int fill) {
            R2DUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
        }

        public static void circle(float x, float y, float radius, Color fill) {
            R2DUtils.arc(x, y, 0.0f, 360.0f, radius, fill);
        }

        public static void arc(float x, float y, float start, float end, float radius, int color) {
            R2DUtils.arcEllipse(x, y, start, end, radius, radius, color);
        }

        public static void arc(float x, float y, float start, float end, float radius, Color color) {
            R2DUtils.arcEllipse(x, y, start, end, radius, radius, color);
        }

        public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
            float ldy;
            float ldx;
            float i;
            GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            float temp = 0.0f;
            if (start > end) {
                temp = end;
                end = start;
                start = temp;
            }
            float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
            float var12 = (float)(color >> 16 & 0xFF) / 255.0f;
            float var13 = (float)(color >> 8 & 0xFF) / 255.0f;
            float var14 = (float)(color & 0xFF) / 255.0f;
            Tessellator var15 = Tessellator.func_178181_a();
            WorldRenderer var16 = var15.func_178180_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179131_c((float)var12, (float)var13, (float)var14, (float)var11);
            if (var11 > 0.5f) {
                GL11.glEnable((int)2848);
                GL11.glLineWidth((float)2.0f);
                GL11.glBegin((int)3);
                for (i = end; i >= start; i -= 4.0f) {
                    ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                    ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                    GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
                }
                GL11.glEnd();
                GL11.glDisable((int)2848);
            }
            GL11.glBegin((int)6);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
        }

        public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
            float ldy;
            float ldx;
            float i;
            GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            float temp = 0.0f;
            if (start > end) {
                temp = end;
                end = start;
                start = temp;
            }
            Tessellator var9 = Tessellator.func_178181_a();
            WorldRenderer var10 = var9.func_178180_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            if ((float)color.getAlpha() > 0.5f) {
                GL11.glEnable((int)2848);
                GL11.glLineWidth((float)2.0f);
                GL11.glBegin((int)3);
                for (i = end; i >= start; i -= 4.0f) {
                    ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                    ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                    GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
                }
                GL11.glEnd();
                GL11.glDisable((int)2848);
            }
            GL11.glBegin((int)6);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
        }

        public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
            Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
        }

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x2, y2, x1, y1);
            R2DUtils.disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2d((double)x2, (double)y1);
            GL11.glVertex2d((double)x1, (double)y1);
            GL11.glVertex2d((double)x1, (double)y2);
            GL11.glVertex2d((double)x2, (double)y2);
            GL11.glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
            float red = (float)(hex >> 16 & 0xFF) / 255.0f;
            float green = (float)(hex >> 8 & 0xFF) / 255.0f;
            float blue = (float)(hex & 0xFF) / 255.0f;
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x, y, x1, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(borderColor);
            R2DUtils.drawRect(x + width, y, x1 - width, y + width);
            R2DUtils.drawRect(x, y, x + width, y1);
            R2DUtils.drawRect(x1 - width, y, x1, y1);
            R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
            R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
            R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
        }

        public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
            R2DUtils.enableGL2D();
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)7);
            R2DUtils.glColor(topColor);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            R2DUtils.glColor(bottomColor);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            R2DUtils.disableGL2D();
        }

        public static void drawHLine(float x, float y, float x1, int y1) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
        }

        public static void drawVLine(float x, float y, float x1, int y1) {
            if (x1 < y) {
                float var5 = y;
                y = x1;
                x1 = var5;
            }
            R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
        }

        public static void drawHLine(float x, float y, float x1, int y1, int y2) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
        }

        public static void drawRect(float x, float y, float x1, float y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
        }
    }

    public static class R3DUtils {
        public static void startDrawing() {
            GL11.glEnable((int)3042);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            Minecraft.func_71410_x().field_71460_t.func_78479_a(Minecraft.func_71410_x().field_71428_T.field_74281_c, 0);
        }

        public static void stopDrawing() {
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
        }

        public static void drawOutlinedBox(AxisAlignedBB box) {
            if (box == null) {
                return;
            }
            Minecraft.func_71410_x().field_71460_t.func_78479_a(Minecraft.func_71410_x().field_71428_T.field_74281_c, 0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72338_b, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72338_b, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72338_b, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72338_b, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72338_b, (double)box.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72337_e, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72337_e, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72337_e, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72337_e, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72337_e, (double)box.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72338_b, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72337_e, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72338_b, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72337_e, (double)box.field_72339_c);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72338_b, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72336_d, (double)box.field_72337_e, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72338_b, (double)box.field_72334_f);
            GL11.glVertex3d((double)box.field_72340_a, (double)box.field_72337_e, (double)box.field_72334_f);
            GL11.glEnd();
        }

        public static void drawFilledBox(AxisAlignedBB mask) {
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glEnd();
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glEnd();
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)4);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72340_a, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72339_c);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72337_e, (double)mask.field_72334_f);
            GL11.glVertex3d((double)mask.field_72336_d, (double)mask.field_72338_b, (double)mask.field_72334_f);
            GL11.glEnd();
        }

        public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72338_b, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72338_b, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72338_b, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72338_b, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72338_b, (double)aabb.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72337_e, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72337_e, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72337_e, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72337_e, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72337_e, (double)aabb.field_72339_c);
            GL11.glEnd();
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72338_b, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72337_e, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72338_b, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72337_e, (double)aabb.field_72339_c);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72338_b, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72336_d, (double)aabb.field_72337_e, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72338_b, (double)aabb.field_72334_f);
            GL11.glVertex3d((double)aabb.field_72340_a, (double)aabb.field_72337_e, (double)aabb.field_72334_f);
            GL11.glEnd();
        }
    }
}

