/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render;

import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.GaussianFilter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Random;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.Tracers;
import org.celestial.client.helpers.Helper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class RenderHelper
implements Helper {
    private static final Matrix4f modelMatrix = new Matrix4f();
    private static final Matrix4f projectionMatrix = new Matrix4f();
    static Vec3d camPos = new Vec3d(0.0, 0.0, 0.0);
    private static final Frustum frustum = new Frustum();
    private static HashMap<Integer, Integer> shadowCache = new HashMap();
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static ResourceLocation shader;
    public static HashMap<Integer, Integer> blurSpotCache;

    public static void inShaderFBO() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(RenderHelper.mc.displayWidth, RenderHelper.mc.displayHeight);
            buffer = RenderHelper.blurShader.mainFramebuffer;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurAreaBoarder(float x, float f, float width, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            RenderHelper.inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor((int)(x * (float)factor), (int)((float)RenderHelper.mc.displayHeight - f * (float)factor - height * (float)factor) + 1, (int)(width * (float)factor), (int)(height * (float)factor));
        GL11.glEnable(3089);
        RenderHelper.shaderConfigFix(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(RenderHelper.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            RenderHelper.inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor(x * factor, RenderHelper.mc.displayHeight - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        RenderHelper.shaderConfigFix(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(RenderHelper.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }

    public static void scissorRect(float x, float y, float width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        GL11.glScissor((int)(x * (float)factor), (int)(((double)sr.getScaledHeight() - height) * (double)factor), (int)((width - x) * (float)factor), (int)((height - (double)y) * (double)factor));
    }

    public static void renderBlur(int x, int y, int width, int height, int blurWidth, int blurHeight, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RenderHelper.blurAreaBoarder(x, y, width, height, blurRadius, blurWidth, blurHeight);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void renderBlur(int x, int y, int width, int height, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RenderHelper.blurAreaBoarder(x, y, width, height, blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void renderBlurredShadow(Color color, double x, double y, double width, double height, int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RenderHelper.renderBlurredShadow(x, y, width, height, blurRadius, color);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void blurSpot(int x, int y, int width, int height, int blurRadius, int iterations) {
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = 1.0 / (double)sr.getScaleFactor();
        int imageDownscale = 2;
        int imageWidth = (width *= sr.getScaleFactor()) / imageDownscale;
        int imageHeight = (height *= sr.getScaleFactor()) / imageDownscale;
        int bpp = 3;
        int identifier = x * y * width * height * blurRadius + width + height + blurRadius + x + y;
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        if (blurSpotCache.containsKey(identifier)) {
            texId = blurSpotCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp).order(ByteOrder.nativeOrder());
            GL11.glReadPixels(x, RenderHelper.mc.displayHeight - y - height, width, height, 6407, 5121, buffer);
            BufferedImage original = new BufferedImage(width, height, 1);
            for (int xIndex = 0; xIndex < width; ++xIndex) {
                for (int yIndex = 0; yIndex < height; ++yIndex) {
                    int i = (xIndex + width * yIndex) * bpp;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    original.setRGB(xIndex, height - (yIndex + 1), 0xFF000000 | r << 16 | g << 8 | b);
                }
            }
            BoxBlurFilter op = new BoxBlurFilter(blurRadius, blurRadius, iterations);
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, original.getType());
            Graphics g = image.getGraphics();
            g.drawImage(original, 0, 0, imageWidth, imageHeight, null);
            g.dispose();
            BufferedImage blurred = op.filter(image, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            blurSpotCache.put(identifier, texId);
        }
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDisable(3553);
    }

    public static void renderBlurredShadow(double x, double y, double width, double height, int blurRadius, Color color) {
        GlStateManager.alphaFunc(516, 0.01f);
        float _X = (float)((x -= (double)blurRadius) - 0.25);
        float _Y = (float)((y -= (double)blurRadius) + 0.25);
        int identifier = (int)((width += (double)(blurRadius * 2)) * (height += (double)(blurRadius * 2)) + width + (double)(color.hashCode() * blurRadius) + (double)blurRadius);
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            width = MathHelper.clamp(width, 0.01, width);
            height = MathHelper.clamp(height, 0.01, height);
            BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)width - blurRadius * 2, (int)height - blurRadius * 2);
            g.dispose();
            GaussianFilter op = new GaussianFilter(blurRadius);
            BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + (float)((int)height));
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + (float)((int)width), _Y + (float)((int)height));
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + (float)((int)width), _Y);
        GL11.glEnd();
        GL11.glDisable(3553);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius, (double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void setColor(int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }

    public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(3042);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GL11.glDisable(2929);
        GlStateManager.depthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)RenderHelper.mc.timer.renderPartialTicks - RenderHelper.mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)RenderHelper.mc.timer.renderPartialTicks - RenderHelper.mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)RenderHelper.mc.timer.renderPartialTicks - RenderHelper.mc.getRenderManager().renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05, axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15, axisAlignedBB.maxZ - entity.posZ + z + 0.05);
        GlStateManager.glLineWidth(2.0f);
        GL11.glEnable(2848);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
        if (fullBox) {
            RenderHelper.drawColorBox(axisAlignedBB2, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
        }
        RenderHelper.drawSelectionBoundingBox(axisAlignedBB2);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.enableTexture2D();
        GL11.glEnable(2929);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void blockEsp(BlockPos blockPos, Color c, boolean outline) {
        double x = (double)blockPos.getX() - RenderHelper.mc.getRenderManager().renderPosX;
        double y = (double)blockPos.getY() - RenderHelper.mc.getRenderManager().renderPosY;
        double z = (double)blockPos.getZ() - RenderHelper.mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, 0.15f);
        RenderHelper.drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            RenderHelper.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void draw3DRect(float x, float y, float w, float h, Color startColor, Color endColor, float lineWidth) {
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(lineWidth);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(w, h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(w, y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
        Sphere s = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.2f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        s.setDrawStyle(100013);
        GL11.glTranslated(x - RenderHelper.mc.getRenderManager().renderPosX, y - RenderHelper.mc.getRenderManager().renderPosY, z - RenderHelper.mc.getRenderManager().renderPosZ);
        s.draw(size, slices, stacks);
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawCircle(Color color, float x, float y, float radius, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.color(color);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f)), (float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f)));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle3D(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderHelper.mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderHelper.mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderHelper.mc.getRenderManager().renderPosZ;
        RenderHelper.setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos((float)i * ((float)Math.PI * 2) / (float)points), y, z + radius * Math.sin((float)i * ((float)Math.PI * 2) / (float)points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawCircle3D(TileEntity entity, double radius, float partialTicks, int points, float width, int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        double x = (double)entity.getPos().getX() - RenderHelper.mc.getRenderManager().renderPosX;
        double y = (double)entity.getPos().getY() - RenderHelper.mc.getRenderManager().renderPosY;
        double z = (double)entity.getPos().getZ() - RenderHelper.mc.getRenderManager().renderPosZ;
        RenderHelper.setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos((float)i * ((float)Math.PI * 2) / (float)points), y, z + radius * Math.sin((float)i * ((float)Math.PI * 2) / (float)points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void color(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void color(float red, float green, float blue) {
        RenderHelper.color(red, green, blue, 1.0f);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        RenderHelper.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void color(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawArrow(float x, float y, float scale, float width, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        RenderHelper.color(hexColor);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x /= scale, (y /= scale) + (float)(up ? 4 : 0));
        GL11.glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        GL11.glVertex2d(x + 6.0f, y + (float)(up ? 4 : 0));
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawDropCircleShadow(int x, int y, int radius, int segments, int opaque, int shadow) {
        float a1 = (float)(opaque >> 24 & 0xFF) / 255.0f;
        float r1 = (float)(opaque >> 16 & 0xFF) / 255.0f;
        float g1 = (float)(opaque >> 8 & 0xFF) / 255.0f;
        float b1 = (float)(opaque & 0xFF) / 255.0f;
        float a2 = (float)(shadow >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(shadow >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(shadow >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(shadow & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y, 0.0).color(r1, g1, b1, a1).endVertex();
        for (int i = 0; i <= segments; ++i) {
            double a = (double)i / (double)segments * Math.PI * 2.0 - 1.5707963267948966;
            buffer.pos((double)x - Math.cos(a) * (double)radius, (double)y + Math.sin(a) * (double)radius, 0.0).color(r2, g2, b2, a2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRhombus(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, (double)(bottom + top) / 2.0, 0.0).endVertex();
        bufferbuilder.pos((double)(right + left) / 2.0, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, (double)(bottom + top) / 2.0, 0.0).endVertex();
        bufferbuilder.pos((double)(right + left) / 2.0, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawDropCircleShadow(int x, int y, int radius, int offset, int segments, int opaque, int shadow) {
        int i;
        if (offset >= radius) {
            RenderHelper.drawDropCircleShadow(x, y, radius, segments, opaque, shadow);
            return;
        }
        float a1 = (float)(opaque >> 24 & 0xFF) / 255.0f;
        float r1 = (float)(opaque >> 16 & 0xFF) / 255.0f;
        float g1 = (float)(opaque >> 8 & 0xFF) / 255.0f;
        float b1 = (float)(opaque & 0xFF) / 255.0f;
        float a2 = (float)(shadow >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(shadow >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(shadow >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(shadow & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y, 0.0).color(r1, g1, b1, a1).endVertex();
        for (i = 0; i <= segments; ++i) {
            double a = (double)i / (double)segments * Math.PI * 2.0 - 1.5707963267948966;
            buffer.pos((double)x - Math.cos(a) * (double)offset, (double)y + Math.sin(a) * (double)offset, 0.0).color(r1, g1, b1, a1).endVertex();
        }
        tessellator.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        for (i = 0; i < segments; ++i) {
            double alpha1 = (double)i / (double)segments * Math.PI * 2.0 - 1.5707963267948966;
            double alpha2 = (double)(i + 1) / (double)segments * Math.PI * 2.0 - 1.5707963267948966;
            buffer.pos((double)x - Math.cos(alpha2) * (double)offset, (double)y + Math.sin(alpha2) * (double)offset, 0.0).color(r1, g1, b1, a1).endVertex();
            buffer.pos((double)x - Math.cos(alpha1) * (double)offset, (double)y + Math.sin(alpha1) * (double)offset, 0.0).color(r1, g1, b1, a1).endVertex();
            buffer.pos((double)x - Math.cos(alpha1) * (double)radius, (double)y + Math.sin(alpha1) * (double)radius, 0.0).color(r2, g2, b2, a2).endVertex();
            buffer.pos((double)x - Math.cos(alpha2) * (double)radius, (double)y + Math.sin(alpha2) * (double)radius, 0.0).color(r2, g2, b2, a2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawDropShadow(int left, int top, int right, int bottom, int shadowScale, int insideColor, int shadowColor) {
        float a1 = (float)(insideColor >> 24 & 0xFF) / 255.0f;
        float r1 = (float)(insideColor >> 16 & 0xFF) / 255.0f;
        float g1 = (float)(insideColor >> 8 & 0xFF) / 255.0f;
        float b1 = (float)(insideColor & 0xFF) / 255.0f;
        float a2 = (float)(shadowColor >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(shadowColor >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(shadowColor >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(shadowColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos((right += shadowScale) - shadowScale, (top -= shadowScale) + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos((left -= shadowScale) + shadowScale, top + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left + shadowScale, (bottom += shadowScale) - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right - shadowScale, bottom - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right, top, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(left, top, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(left + shadowScale, top + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right - shadowScale, top + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right - shadowScale, bottom - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left + shadowScale, bottom - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(right, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(left + shadowScale, top + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left, top, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(left, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(left + shadowScale, bottom - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right, top, 0.0).color(r2, g2, b2, a2).endVertex();
        buffer.pos(right - shadowScale, top + shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right - shadowScale, bottom - shadowScale, 0.0).color(r1, g1, b1, a1).endVertex();
        buffer.pos(right, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawCylinder(float radius, float height, int segments, boolean flag) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        float[][] vertecesTop = new float[segments][3];
        float[][] vertecesBottom = new float[segments][3];
        for (i = 0; i < segments; ++i) {
            vertecesTop[i][0] = (float)Math.cos(Math.PI * 2 / (double)segments * (double)i) * radius;
            vertecesTop[i][1] = 0.0f;
            vertecesTop[i][2] = (float)Math.sin(Math.PI * 2 / (double)segments * (double)i) * radius;
            vertecesBottom[i][0] = (float)Math.cos(Math.PI * 2 / (double)segments * (double)i) * radius;
            vertecesBottom[i][1] = -height;
            vertecesBottom[i][2] = (float)Math.sin(Math.PI * 2 / (double)segments * (double)i) * radius;
        }
        GL11.glBegin(9);
        for (i = 0; i < segments; ++i) {
            GL11.glVertex3f(vertecesTop[segments - 1 - i][0], vertecesTop[segments - 1 - i][1], vertecesTop[segments - 1 - i][2]);
        }
        GL11.glEnd();
        GL11.glBegin(9);
        for (i = 0; i < segments; ++i) {
            GL11.glVertex3f(vertecesBottom[i][0], vertecesBottom[i][1], vertecesBottom[i][2]);
        }
        GL11.glEnd();
        for (i = 0; i < segments; ++i) {
            GL11.glBegin(7);
            GL11.glVertex3f(vertecesTop[i][0], vertecesTop[i][1], vertecesTop[i][2]);
            GL11.glVertex3f(vertecesTop[(i + 1) % segments][0], vertecesTop[(i + 1) % segments][1], vertecesTop[(i + 1) % segments][2]);
            GL11.glVertex3f(vertecesBottom[(i + 1) % segments][0], vertecesBottom[(i + 1) % segments][1], vertecesBottom[(i + 1) % segments][2]);
            GL11.glVertex3f(vertecesBottom[i][0], vertecesBottom[i][1], vertecesBottom[i][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }

    public static void drawCone(float radius, float height, int segments, boolean flag) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        float[][] verteces = new float[segments][3];
        float[] topVertex = new float[]{0.0f, 0.0f, 0.0f};
        for (i = 0; i < segments; ++i) {
            verteces[i][0] = (float)Math.cos(Math.PI * 2 / (double)segments * (double)i) * radius;
            verteces[i][1] = -height;
            verteces[i][2] = (float)Math.sin(Math.PI * 2 / (double)segments * (double)i) * radius;
        }
        GL11.glBegin(9);
        for (i = 0; i < segments; ++i) {
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
        }
        GL11.glEnd();
        for (i = 0; i < segments; ++i) {
            GL11.glBegin(4);
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
            GL11.glVertex3f(topVertex[0], topVertex[1], topVertex[2]);
            GL11.glVertex3f(verteces[(i + 1) % segments][0], verteces[(i + 1) % segments][1], verteces[(i + 1) % segments][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }

    public static void renderShine(float rotation, int iterations) {
        Random random = new Random(432L);
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.enableCull();
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        float f1 = rotation;
        float f2 = 0.0f;
        if (f1 > 0.8f) {
            f2 = (f1 - 0.8f) / 0.2f;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int i = 0;
        while ((float)i < (float)iterations) {
            GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(random.nextFloat() * 360.0f + f1 * 90.0f, 0.0f, 0.0f, 1.0f);
            float pos1 = random.nextFloat() * 20.0f + 5.0f + f2 * 10.0f;
            float pos2 = random.nextFloat() * 2.0f + 1.0f + f2 * 2.0f;
            buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0.0, 0.0, 0.0).color(255, 255, 255, (int)(255.0f * (1.0f - f2))).endVertex();
            buffer.pos(-0.866 * (double)pos2, pos1, -0.5f * pos2).color(0, 0, 255, 0).endVertex();
            buffer.pos(0.866 * (double)pos2, pos1, -0.5f * pos2).color(0, 0, 255, 0).endVertex();
            buffer.pos(0.0, pos1, 1.0f * pos2).color(0, 0, 255, 0).endVertex();
            buffer.pos(-0.866 * (double)pos2, pos1, -0.5f * pos2).color(0, 0, 255, 0).endVertex();
            tessellator.draw();
            ++i;
        }
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(7424);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    }

    public static void drawCircle(float xx, float yy, float radius, Color col, float width, float position, float round) {
        int sections = 100;
        double dAngle = (double)(round * 2.0f) * Math.PI / (double)sections;
        float x = 0.0f;
        float y = 0.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        int i = (int)position;
        while ((float)i < position + (float)sections) {
            x = (float)((double)radius * Math.cos((double)i * dAngle));
            y = (float)((double)radius * Math.sin((double)i * dAngle));
            GL11.glColor4f((float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, (float)col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
            ++i;
        }
        for (i = (int)(position + (float)sections); i > (int)position; --i) {
            x = (float)((double)radius * Math.cos((double)i * dAngle));
            y = (float)((double)radius * Math.sin((double)i * dAngle));
            GL11.glColor4f((float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, (float)col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawLoadingCircle(Color color, float sizeAmount) {
        float status = (float)((double)System.currentTimeMillis() * 0.1 % 400.0);
        float size = sizeAmount;
        ScaledResolution res = new ScaledResolution(mc);
        float radius = (float)res.getScaledWidth() / 20.0f;
        RenderHelper.drawCircle((float)res.getScaledWidth() / 2.0f, (float)res.getScaledHeight() / 2.0f - 65.0f, radius, new Color(33, 33, 33, 255), 5.0f, 0.0f, 1.0f);
        RenderHelper.drawCircle((float)res.getScaledWidth() / 2.0f, (float)res.getScaledHeight() / 2.0f - 65.0f, radius, new Color(color.getRGB()), 7.0f, status, size);
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color color) {
        GL11.glPushMatrix();
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        mc.getTextureManager().bindTexture(resourceLocation);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    private static Vector4f getTransformedMatrix(Vec3d posIn) {
        Vec3d relativePos = camPos.subtract(posIn);
        Vector4f vector4f = new Vector4f((float)relativePos.x, (float)relativePos.y, (float)relativePos.z, 1.0f);
        RenderHelper.transform(vector4f, modelMatrix);
        RenderHelper.transform(vector4f, projectionMatrix);
        if (vector4f.w > 0.0f) {
            vector4f.x *= -100000.0f;
            vector4f.y *= -100000.0f;
        } else {
            float invert = 1.0f / vector4f.w;
            vector4f.x *= invert;
            vector4f.y *= invert;
        }
        return vector4f;
    }

    private static void transform(Vector4f vec, Matrix4f matrix) {
        float x = vec.x;
        float y = vec.y;
        float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }

    private static boolean isVisible(Vector4f pos, int width, int height) {
        double wid = width;
        double position = pos.x;
        if (position >= 0.0 && position <= wid) {
            wid = height;
            position = pos.y;
            return position >= 0.0 && position <= wid;
        }
        return false;
    }

    public static Vec3d toScaledScreenPos(Vec3d posIn) {
        Vector4f vector4f = RenderHelper.getTransformedMatrix(posIn);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        vector4f.x = (float)width / 2.0f + (0.5f * vector4f.x * (float)width + 0.5f);
        vector4f.y = (float)height / 2.0f - (0.5f * vector4f.y * (float)height + 0.5f);
        double posZ = RenderHelper.isVisible(vector4f, width, height) ? 0.0 : -1.0;
        return new Vec3d(vector4f.x, vector4f.y, posZ);
    }

    public static void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        RenderHelper.mc.getRenderItem().zLevel = -150.0f;
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc.getRenderItem().renderItemOverlays(RenderHelper.mc.fontRendererObj, itemStack, x, y);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        RenderHelper.mc.getRenderItem().zLevel = 0.0f;
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buffer = ts.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static boolean isInViewFrustum(Entity entity) {
        return RenderHelper.isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void blockEsp(BlockPos blockPos, Color c, boolean outline, double length, double length2) {
        double x = (double)blockPos.getX() - RenderHelper.mc.getRenderManager().renderPosX;
        double y = (double)blockPos.getY() - RenderHelper.mc.getRenderManager().renderPosY;
        double z = (double)blockPos.getZ() - RenderHelper.mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, 0.15f);
        RenderHelper.drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            RenderHelper.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void blockEspFrame(BlockPos blockPos, float red, float green, float blue) {
        double x = (double)blockPos.getX() - RenderHelper.mc.getRenderManager().renderPosX;
        double y = (double)blockPos.getY() - RenderHelper.mc.getRenderManager().renderPosY;
        double z = (double)blockPos.getZ() - RenderHelper.mc.getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color(red, green, blue, 1.0f);
        RenderHelper.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void renderTriangle(int color) {
        GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(4, DefaultVertexFormats.POSITION);
        buffer.pos(0.0, 4.0, 0.0).endVertex();
        buffer.pos(4.0, -4.0, 0.0).endVertex();
        buffer.pos(-4.0, -4.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);
    }

    public static void drawTriangle(float x, float y, float size, float vector, int color) {
        GL11.glTranslated(x, y, 0.0);
        GL11.glRotatef(180.0f + vector, 0.0f, 0.0f, 1.0f);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0, size);
        GL11.glVertex2d(1.0f * size, -size);
        GL11.glVertex2d(-(1.0f * size), -size);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(-180.0f - vector, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-x, -y, 0.0);
    }

    public static void tracersEsp(Entity entity, float lineWidth, float partialTicks, int color) {
        if (!Celestial.instance.friendManager.isFriend(entity.getName()) && Tracers.onlyFriend.getCurrentValue() && Celestial.instance.featureManager.getFeatureByClass(Tracers.class).getState()) {
            return;
        }
        boolean old = RenderHelper.mc.gameSettings.viewBobbing;
        RenderHelper.mc.gameSettings.viewBobbing = false;
        RenderHelper.mc.entityRenderer.setupCameraTransform(partialTicks, 2);
        RenderHelper.mc.gameSettings.viewBobbing = old;
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderHelper.mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderHelper.mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderHelper.mc.getRenderManager().viewerPosZ;
        if (Celestial.instance.friendManager.isFriend(entity.getName()) && Tracers.friend.getCurrentValue()) {
            float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
            GlStateManager.color(0.0f, 255.0f, 0.0f, alpha);
        } else {
            RenderHelper.color(color);
        }
        Vec3d vec3d = new Vec3d(0.0, 0.0, 1.0);
        vec3d = vec3d.rotatePitch((float)(-Math.toRadians(RenderHelper.mc.player.rotationPitch)));
        Vec3d vec3d2 = vec3d.rotateYaw(-((float)Math.toRadians(RenderHelper.mc.player.rotationYaw)));
        GL11.glBegin(2);
        GL11.glVertex3d(vec3d2.x, (double)RenderHelper.mc.player.getEyeHeight() + vec3d2.y, vec3d2.z);
        GL11.glVertex3d(x, y + 1.1, z);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo) {
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(xOne, yOne);
        GL11.glVertex2f(xTwo, yTwo);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    static {
        shader = new ResourceLocation("shaders/post/blur.json");
        blurSpotCache = new HashMap();
    }
}

