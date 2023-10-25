/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.Tengoku.Terror.util;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.vecmath.Vector3d;
import me.Tengoku.Terror.util.GLUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final FloatBuffer modelview;
    private static final IntBuffer viewport;
    private static final FloatBuffer projection;
    private static final Frustum frustrum;

    public static void drawEntityESP(double d, double d2, double d3, double d4, double d5, Color color, boolean bl) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask((boolean)true);
        RenderUtil.BB(new AxisAlignedBB(d - d5 + 0.25, d2 + 0.1, d3 - d5 + 0.25, d + d5 - 0.25, d2 + d4 + 0.25, d3 + d5 - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        if (bl) {
            RenderUtil.OutlinedBB(new AxisAlignedBB(d - d5 + 0.25, d2 + 0.1, d3 - d5 + 0.25, d + d5 - 0.25, d2 + d4 + 0.25, d3 + d5 - 0.25), 1.0f, color.getRGB());
        }
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawUnfilledCircle(float f, float f2, float f3, int n) {
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)2);
        int n2 = 0;
        while (n2 <= 360) {
            double d = Math.sin((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            double d2 = Math.cos((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            GL11.glVertex2d((double)((double)(f + f3 / 2.0f) + d), (double)((double)(f2 + f3 / 2.0f) + d2));
            ++n2;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void prepareScissorBox(ScaledResolution scaledResolution, float f, float f2, float f3, float f4) {
        float f5 = f + f3;
        float f6 = f2 + f4;
        int n = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(f * (float)n)), (int)((int)(((float)scaledResolution.getScaledHeight() - f6) * (float)n)), (int)((int)((f5 - f) * (float)n)), (int)((int)((f6 - f2) * (float)n)));
    }

    public static void hexColor(int n) {
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
    }

    public static Vector3d project(double d, double d2, double d3) {
        FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        if (GLU.gluProject((float)((float)d), (float)((float)d2), (float)((float)d3), (FloatBuffer)modelview, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)floatBuffer)) {
            return new Vector3d((double)(floatBuffer.get(0) / (float)RenderUtil.getResolution().getScaleFactor()), (double)(((float)Display.getHeight() - floatBuffer.get(1)) / (float)RenderUtil.getResolution().getScaleFactor()), (double)floatBuffer.get(2));
        }
        return null;
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static Vec3 to2D(double d, double d2, double d3) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)3);
        IntBuffer intBuffer = BufferUtils.createIntBuffer((int)16);
        FloatBuffer floatBuffer2 = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer floatBuffer3 = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)floatBuffer2);
        GL11.glGetFloat((int)2983, (FloatBuffer)floatBuffer3);
        GL11.glGetInteger((int)2978, (IntBuffer)intBuffer);
        boolean bl = GLU.gluProject((float)((float)d), (float)((float)d2), (float)((float)d3), (FloatBuffer)floatBuffer2, (FloatBuffer)floatBuffer3, (IntBuffer)intBuffer, (FloatBuffer)floatBuffer);
        if (bl) {
            return new Vec3(floatBuffer.get(0), (float)Display.getHeight() - floatBuffer.get(1), floatBuffer.get(2));
        }
        return null;
    }

    public static void BB(AxisAlignedBB axisAlignedBB, int n) {
        RenderUtil.enable3D();
        RenderUtil.color(n);
        RenderUtil.drawBoundingBox(axisAlignedBB);
        RenderUtil.disable3D();
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void enable3D() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
    }

    public static void drawBordered(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        double d6 = 0.0;
        if (d5 < 1.0) {
            d6 = 1.0;
        }
        RenderUtil.drawRect2(d + d5, d2 + d5, d3 - d5, d4 - d5, n);
        RenderUtil.drawRect2(d, d2 + 1.0 - d6, d + d5, d4, n2);
        RenderUtil.drawRect2(d, d2, d3 - 1.0 + d6, d2 + d5, n2);
        RenderUtil.drawRect2(d3 - d5, d2, d3, d4 - 1.0 + d6, n2);
        RenderUtil.drawRect2(d + 1.0 - d6, d4 - d5, d3, d4, n2);
    }

    public static void color(int n) {
        GL11.glColor4f((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)((float)(n >> 24 & 0xFF) / 255.0f));
    }

    public static void OutlinedBB(AxisAlignedBB axisAlignedBB, float f, int n) {
        RenderUtil.enable3D();
        GL11.glLineWidth((float)f);
        RenderUtil.color(n);
        RenderUtil.drawOutlinedBoundingBox(axisAlignedBB);
        RenderUtil.disable3D();
    }

    public static boolean isInViewFrustrum(AxisAlignedBB axisAlignedBB) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(entity.posX, entity.posY, entity.posZ);
        return frustrum.isBoundingBoxInFrustum(axisAlignedBB);
    }

    static {
        frustrum = new Frustum();
        viewport = GLAllocation.createDirectIntBuffer(16);
        modelview = GLAllocation.createDirectFloatBuffer(16);
        projection = GLAllocation.createDirectFloatBuffer(16);
    }

    public static void drawBorderedRoundedRect(float f, float f2, float f3, float f4, float f5, float f6, int n, int n2) {
        RenderUtil.drawRoundedRect(f, f2, f3, f4, f5, n2);
        RenderUtil.drawOutlinedRoundedRect(f, f2, f3, f4, f5, f6, n);
    }

    public static void drawImage(ResourceLocation resourceLocation, float f, float f2, int n, int n2) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(f, f2, 0.0f, 0.0f, n, n2, n, n2);
    }

    public static void drawCircle(float f, float f2, float f3, int n) {
        double d;
        double d2;
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)2848);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glBegin((int)6);
        int n2 = 0;
        while (n2 <= 360) {
            d2 = Math.sin((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            d = Math.cos((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            GL11.glVertex2d((double)((double)(f + f3 / 2.0f) + d2), (double)((double)(f2 + f3 / 2.0f) + d));
            ++n2;
        }
        GL11.glEnd();
        GL11.glBegin((int)2);
        n2 = 0;
        while (n2 <= 360) {
            d2 = Math.sin((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            d = Math.cos((double)n2 * Math.PI / 180.0) * (double)(f3 / 2.0f);
            GL11.glVertex2d((double)((double)(f + f3 / 2.0f) + d2), (double)((double)(f2 + f3 / 2.0f) + d));
            ++n2;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static void drawRoundedRectWithShadow(double d, double d2, double d3, double d4, double d5, int n) {
        RenderUtil.drawRoundedRect(d + 2.0, d2 + 1.0, d3, d4 + 1.0, d5, new Color(0).getRGB());
        RenderUtil.drawRoundedRect(d, d2, d3, d4, d5, n);
    }

    public static void drawRoundedRect(double d, double d2, double d3, double d4, double d5, int n) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double d6 = d + d3;
        double d7 = d2 + d4;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        d *= 2.0;
        d2 *= 2.0;
        d6 *= 2.0;
        d7 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        int n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * (d5 * -1.0)), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * (d5 * -1.0)));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * (d5 * -1.0)), (double)(d7 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * (d5 * -1.0)));
            n2 += 3;
        }
        n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d6 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d7 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d6 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void disable3D() {
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawBorderedRect(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        Gui.drawRect(d, d2, d + d3, d2 + d4, n2);
        Gui.drawRect(d, d2, d + d3, d2 + d5, n);
        Gui.drawRect(d, d2, d + d5, d2 + d4, n);
        Gui.drawRect(d + d3, d2, d + d3 - d5, d2 + d4, n);
        Gui.drawRect(d, d2 + d4, d + d3, d2 + d4 - d5, n);
    }

    public static int getRainbow(int n, int n2, float f) {
        float f2 = (System.currentTimeMillis() + (long)n2) % (long)n;
        return Color.getHSBColor(f2 /= (float)n, f, 1.0f).getRGB();
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        Gui.drawRect(d, d2, d + d3, d2 + d4, n);
    }

    public static void drawCornerRect(double d, double d2, double d3, double d4, double d5, int n, boolean bl, double d6) {
        double d7 = d3 / 4.0;
        double d8 = d4 / 4.0;
        RenderUtil.drawRect(d, d2, d7 + (bl ? d6 : 0.0), d5, n);
        RenderUtil.drawRect(d + d3 - (d7 + (bl ? d6 : 0.0)), d2, d7, d5, n);
        RenderUtil.drawRect(d, d2 + d4 - d5, d7 + (bl ? d6 : 0.0), d5, n);
        RenderUtil.drawRect(d + d3 - (d7 + (bl ? d6 : 0.0)), d2 + d4 - d5, d7, d5, n);
        RenderUtil.drawRect(d, d2, d5, d8 + (bl ? d6 : 0.0), n);
        RenderUtil.drawRect(d + d3 - d5, d2, d5, d8 + (bl ? d6 : 0.0), n);
        RenderUtil.drawRect(d, d2 + d4 - (d8 + (bl ? d6 : 0.0)), d5, d8, n);
        RenderUtil.drawRect(d + d3 - d5, d2 + d4 - (d8 + (bl ? d6 : 0.0)), d5, d8, n);
    }

    public static double interpolate(double d, double d2, double d3) {
        return d2 + (d - d2) * d3;
    }

    public static void drawOutlinedRoundedRect(double d, double d2, double d3, double d4, double d5, float f, int n) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double d6 = d + d3;
        double d7 = d2 + d4;
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        d *= 2.0;
        d2 *= 2.0;
        d6 *= 2.0;
        d7 *= 2.0;
        GL11.glLineWidth((float)f);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        int n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * (d5 * -1.0)), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * (d5 * -1.0)));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d + d5 + Math.sin((double)n2 * Math.PI / 180.0) * (d5 * -1.0)), (double)(d7 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * (d5 * -1.0)));
            n2 += 3;
        }
        n2 = 0;
        while (n2 <= 90) {
            GL11.glVertex2d((double)(d6 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d7 - d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        n2 = 90;
        while (n2 <= 180) {
            GL11.glVertex2d((double)(d6 - d5 + Math.sin((double)n2 * Math.PI / 180.0) * d5), (double)(d2 + d5 + Math.cos((double)n2 * Math.PI / 180.0) * d5));
            n2 += 3;
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBar(float f, float f2, float f3, float f4, float f5, float f6, int n) {
        float f7 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f8 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f9 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f10 = (float)(n & 0xFF) / 255.0f;
        float f11 = f4 / f5;
        GL11.glColor4f((float)f8, (float)f9, (float)f10, (float)f7);
        RenderUtil.drawBorderedRect(f, f2, f3, f4, 0.5, -16777216, 0);
        float f12 = f2 + f4 - f11;
        int n2 = 0;
        while ((float)n2 < f6) {
            RenderUtil.drawBorderedRect(f + 0.25f, f12, f3 - 0.5f, f11, 0.25, -16777216, n);
            f12 -= f11;
            ++n2;
        }
    }

    public static void drawArrow(float f, float f2, boolean bl, int n) {
        GL11.glPushMatrix();
        GL11.glScaled((double)1.3, (double)1.3, (double)1.3);
        f = (float)((double)f / 1.3);
        f2 = (float)((double)f2 / 1.3);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        RenderUtil.hexColor(n);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)f, (double)(f2 + (float)(bl ? 4 : 0)));
        GL11.glVertex2d((double)(f + 3.0f), (double)(f2 + (float)(bl ? 0 : 4)));
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)(f + 3.0f), (double)(f2 + (float)(bl ? 0 : 4)));
        GL11.glVertex2d((double)(f + 6.0f), (double)(f2 + (float)(bl ? 4 : 0)));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawRect2(double d, double d2, double d3, double d4, int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        Gui.drawRect(d, d2, d3, d4, n);
    }

    public static void drawCheckMark(float f, float f2, int n, int n2) {
        float f3 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)3);
        GL11.glColor4f((float)f4, (float)f5, (float)f6, (float)f3);
        GL11.glVertex2d((double)((double)(f + (float)n) - 6.5), (double)(f2 + 3.0f));
        GL11.glVertex2d((double)((double)(f + (float)n) - 11.5), (double)(f2 + 10.0f));
        GL11.glVertex2d((double)((double)(f + (float)n) - 13.5), (double)(f2 + 8.0f));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static float[] getRGBAs(int n) {
        return new float[]{(float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, (float)(n >> 24 & 0xFF) / 255.0f};
    }

    public static void drawTracerPointer(float f, float f2, float f3, float f4, float f5, int n) {
        boolean bl = GL11.glIsEnabled((int)3042);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.hexColor(n);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)(f - f3 / f4), (double)(f2 + f3));
        GL11.glVertex2d((double)f, (double)(f2 + f3 / f5));
        GL11.glVertex2d((double)(f + f3 / f4), (double)(f2 + f3));
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glEnd();
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.8f);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)(f - f3 / f4), (double)(f2 + f3));
        GL11.glVertex2d((double)f, (double)(f2 + f3 / f5));
        GL11.glVertex2d((double)(f + f3 / f4), (double)(f2 + f3));
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        if (!bl) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2848);
    }
}

