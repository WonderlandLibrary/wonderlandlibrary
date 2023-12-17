// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.render;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import ru.tuskevich.util.color.ColorUtility;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import net.minecraft.client.renderer.GLAllocation;
import javax.vecmath.Vector3d;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import ru.tuskevich.util.aliasing.AntiAliasingUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.shader.GaussianBlur;
import ru.tuskevich.util.shader.StencilUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustum;
import ru.tuskevich.util.shader.ShaderUtility;
import ru.tuskevich.util.Utility;

public class RenderUtility implements Utility
{
    public static ShaderUtility roundedShader;
    private static final ShaderUtility roundedGradientShader;
    public static ShaderUtility roundedOutlineShader;
    public static ShaderUtility roundedTexturedShader;
    public static Frustum frustum;
    
    public static void scissorRect(final float x, final float y, final float width, final double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtility.mc);
        final int factor = ScaledResolution.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)(((float)sr.getScaledHeight() - height) * (float)factor), (int)((width - x) * factor), (int)((height - y) * (float)factor));
    }
    
    public static void sizeAnimation(final double width, final double height, final double animation) {
        GL11.glTranslated(width / 2.0, height / 2.0, 0.0);
        GL11.glScaled(animation, animation, 1.0);
        GL11.glTranslated(-width / 2.0, -height / 2.0, 0.0);
    }
    
    public static void drawBlur(final float radius, final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void color(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void color(final float red, final float green, final float blue) {
        color(red, green, blue, 1.0f);
    }
    
    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void color(final int hexColor) {
        final float red = (hexColor >> 16 & 0xFF) / 255.0f;
        final float green = (hexColor >> 8 & 0xFF) / 255.0f;
        final float blue = (hexColor & 0xFF) / 255.0f;
        final float alpha = (hexColor >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void drawTriangle() {
        final boolean needBlend = !GL11.glIsEnabled(3042);
        if (needBlend) {
            GL11.glEnable(3042);
        }
        final int alpha = 255;
        final int red_1 = 255;
        final int green_1 = 255;
        final int blue_1 = 255;
        final int red_2 = Math.max(red_1 - 40, 0);
        final int green_2 = Math.max(green_1 - 40, 0);
        final int blue_2 = Math.max(blue_1 - 40, 0);
        final float width = 6.0f;
        final float height = 12.0f;
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glShadeModel(7425);
        GL11.glBegin(9);
        GL11.glColor4f(red_1 / 255.0f, green_1 / 255.0f, blue_1 / 255.0f, alpha / 255.0f);
        GL11.glVertex2d(0.0, (double)(0.0f - height));
        GL11.glVertex2d((double)(0.0f - width), 0.0);
        GL11.glVertex2d(0.0, -3.0);
        GL11.glEnd();
        GL11.glBegin(9);
        GL11.glColor4f(red_2 / 255.0f, green_2 / 255.0f, blue_2 / 255.0f, alpha / 255.0f);
        GL11.glVertex2d(0.0, (double)(0.0f - height));
        GL11.glVertex2d(0.0, -3.0);
        GL11.glVertex2d((double)(0.0f + width), 0.0);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        if (needBlend) {
            GL11.glDisable(3042);
        }
    }
    
    public static void drawTriangle(final double x, final double y, final double width, final double height, final double rotation) {
        final boolean needBlend = !GL11.glIsEnabled(3042);
        if (needBlend) {
            GL11.glEnable(3042);
        }
        final int alpha = 255;
        final int red_1 = 255;
        final int green_1 = 255;
        final int blue_1 = 255;
        final int red_2 = Math.max(red_1 - 40, 0);
        final int green_2 = Math.max(green_1 - 40, 0);
        final int blue_2 = Math.max(blue_1 - 40, 0);
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glShadeModel(7425);
        GL11.glBegin(9);
        GL11.glColor4f(red_1 / 255.0f, green_1 / 255.0f, blue_1 / 255.0f, alpha / 255.0f);
        GL11.glVertex2d(x, y - height);
        GL11.glVertex2d(x - width, y);
        GL11.glVertex2d(x, y - 3.0);
        GL11.glEnd();
        GL11.glBegin(9);
        GL11.glColor4f(red_2 / 255.0f, green_2 / 255.0f, blue_2 / 255.0f, alpha / 255.0f);
        GL11.glVertex2d(x, y - height);
        GL11.glVertex2d(x, y - 3.0);
        GL11.glVertex2d(x + width, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        if (needBlend) {
            GL11.glDisable(3042);
        }
    }
    
    public static void drawBlockBox(final BlockPos blockPos, final int red, final float green, final float blue, final float alpha) {
        final Minecraft mc = RenderUtility.mc;
        final double lastTickPosX = Minecraft.player.lastTickPosX;
        final Minecraft mc2 = RenderUtility.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc3 = RenderUtility.mc;
        final double x = lastTickPosX + (posX - Minecraft.player.lastTickPosX) * RenderUtility.mc.timer.renderPartialTicks;
        final Minecraft mc4 = RenderUtility.mc;
        final double lastTickPosY = Minecraft.player.lastTickPosY;
        final Minecraft mc5 = RenderUtility.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc6 = RenderUtility.mc;
        final double y = lastTickPosY + (posY - Minecraft.player.lastTickPosY) * RenderUtility.mc.timer.renderPartialTicks;
        final Minecraft mc7 = RenderUtility.mc;
        final double lastTickPosZ = Minecraft.player.lastTickPosZ;
        final Minecraft mc8 = RenderUtility.mc;
        final double posZ = Minecraft.player.posZ;
        final Minecraft mc9 = RenderUtility.mc;
        final double z = lastTickPosZ + (posZ - Minecraft.player.lastTickPosZ) * RenderUtility.mc.timer.renderPartialTicks;
        RenderUtility.mc.entityRenderer.setupCameraTransform(RenderUtility.mc.getRenderPartialTicks(), 6);
        GL11.glPushMatrix();
        AntiAliasingUtility.hook(true, true, true);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
        drawSelectionBoundingBox(RenderUtility.mc.world.getBlockState(blockPos).getSelectedBoundingBox(RenderUtility.mc.world, blockPos).grow(0.0020000000949949026).offset(-x, -y, -z));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        AntiAliasingUtility.unhook(true, true, true);
        GL11.glPopMatrix();
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB boundingBox) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexbuffer = tessellator.getBuffer();
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
    
    public static void drawTriangle(final float x, final float y, final float vector, final int color) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)y, 0.0);
        GL11.glRotatef(180.0f + vector, 0.0f, 0.0f, 1.0f);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3155, 4354);
        GL11.glBlendFunc(770, 771);
        final float size = 0.9f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(4, DefaultVertexFormats.POSITION);
        buffer.pos(0.0, 3.1 * size, 0.0).endVertex();
        buffer.pos(2.0f * size, -1.0f * size, 0.0).endVertex();
        buffer.pos(-2.3 * size, -1.0f * size, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glRotatef(-180.0f - vector, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated((double)(-x), (double)(-y), 0.0);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
    
    public static Vector3d vectorTo2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float)x;
        final float yPos = (float)y;
        final float zPos = (float)z;
        final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaleFactor, (Display.getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }
    
    public static void drawColoredCircle(final double x, final double y, final double radius, final float brightness) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glShadeModel(7425);
        GlStateManager.blendFunc(770, 771);
        GL11.glBegin(3);
        for (int i = 0; i < 360; ++i) {
            ColorUtility.setColor(Color.HSBtoRGB(i / 360.0f, 0.0f, brightness));
            GL11.glVertex2d(x, y);
            ColorUtility.setColor(Color.HSBtoRGB(i / 360.0f, 1.0f, brightness));
            GL11.glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
    }
    
    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glEnd();
        GL11.glPopMatrix();
        GlStateManager.resetColor();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static boolean isInViewFrustum(final Entity entity) {
        return isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    
    private static boolean isInViewFrustum(final AxisAlignedBB bb) {
        final Entity current = RenderUtility.mc.getRenderViewEntity();
        if (current != null) {
            RenderUtility.frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return RenderUtility.frustum.isBoundingBoxInFrustum(bb);
    }
    
    public static void drawFace(final float d, final float y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final AbstractClientPlayer target) {
        try {
            final ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            GL11.glEnable(3042);
            final float hurtPercent = getHurtPercent(target);
            GL11.glColor4f(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
            Gui.drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static float getRenderHurtTime(final EntityLivingBase hurt) {
        return hurt.hurtTime - ((hurt.hurtTime != 0) ? RenderUtility.mc.timer.renderPartialTicks : 0.0f);
    }
    
    public static float getHurtPercent(final EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / 10.0f;
    }
    
    public static void drawRect(final double left, final double top, double right, double bottom, final int color) {
        right += left;
        bottom += top;
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRectNotWH(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float x, final float y, final float width, final float height, final Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        ColorUtility.setColor(color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
    
    public static void drawCircle(final float x, final float y, float start, float end, final float radius, final float width, final boolean filled, final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        ColorUtility.setColor(color.getRGB());
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)(Math.cos(i * 3.141592653589793 / 180.0) * radius * 1.0);
            final float sin = (float)(Math.sin(i * 3.141592653589793 / 180.0) * radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2848);
        GL11.glBegin(filled ? 6 : 3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)Math.cos(i * 3.141592653589793 / 180.0) * radius;
            final float sin = (float)Math.sin(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawGradientHorizontal(final float x, final float y, final float width, final float height, final float radius, final Color left, final Color right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }
    
    public static void drawRoundCircle(final float x, final float y, final float radius, final Color color) {
        drawRoundOutline(x - radius / 2.0f, y - radius / 2.0f, radius, radius, radius / 2.0f - 0.5f, 0.1f, new Color(0, 0, 0, 0), color);
    }
    
    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final Color color) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RenderUtility.roundedShader.init();
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, RenderUtility.roundedShader);
        RenderUtility.roundedShader.setUniformi("blur", 0);
        RenderUtility.roundedShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        ShaderUtility.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RenderUtility.roundedShader.unload();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }
    
    public static void drawGradientRound(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RenderUtility.roundedGradientShader.init();
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, RenderUtility.roundedGradientShader);
        RenderUtility.roundedGradientShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f, bottomLeft.getAlpha() / 255.0f);
        RenderUtility.roundedGradientShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f, topLeft.getAlpha() / 255.0f);
        RenderUtility.roundedGradientShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f, bottomRight.getAlpha() / 255.0f);
        RenderUtility.roundedGradientShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f, topRight.getAlpha() / 255.0f);
        ShaderUtility.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RenderUtility.roundedGradientShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void drawRoundOutline(final float x, final float y, final float width, final float height, final float radius, final float outlineThickness, final Color color, final Color outlineColor) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        RenderUtility.roundedOutlineShader.init();
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        ShaderUtility.setupRoundedRectUniforms(x, y, width, height, radius, RenderUtility.roundedOutlineShader);
        RenderUtility.roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * ScaledResolution.getScaleFactor());
        RenderUtility.roundedOutlineShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RenderUtility.roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255.0f, outlineColor.getGreen() / 255.0f, outlineColor.getBlue() / 255.0f, outlineColor.getAlpha() / 255.0f);
        ShaderUtility.drawQuads(x - (2.0f + outlineThickness), y - (2.0f + outlineThickness), width + (4.0f + outlineThickness * 2.0f), height + (4.0f + outlineThickness * 2.0f));
        RenderUtility.roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void horizontalGradient(final double x1, final double y1, double x2, double y2, final int startColor, final int endColor) {
        x2 += x1;
        y2 += y1;
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x1, y1, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(x1, y2, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(x2, y2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(x2, y1, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void verticalGradient(final double x1, final double y1, double x2, double y2, final int startColor, final int endColor) {
        x2 += x1;
        y2 += y1;
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x1, y1, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(x1, y2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(x2, y2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(x2, y1, 0.0).color(f2, f3, f4, f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void renderBreadCrumbs(final List<Vec3d> vec3s) {
        GlStateManager.disableDepth();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        int i = 0;
        try {
            for (final Vec3d v : vec3s) {
                ++i;
                boolean draw = true;
                final double x = v.x - RenderUtility.mc.getRenderManager().renderPosX;
                final double y = v.y - RenderUtility.mc.getRenderManager().renderPosY;
                final double z = v.z - RenderUtility.mc.getRenderManager().renderPosZ;
                final Minecraft mc = RenderUtility.mc;
                final double distanceFromPlayer = Minecraft.player.getDistance(v.x, v.y - 1.0, v.z);
                int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
                if (quality > 350) {
                    quality = 350;
                }
                if (i % 10 != 0 && distanceFromPlayer > 25.0) {
                    draw = false;
                }
                if (i % 3 == 0 && distanceFromPlayer > 15.0) {
                    draw = false;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                final float scale = 0.04f;
                GL11.glScalef(-0.04f, -0.04f, -0.04f);
                GL11.glRotated((double)(-RenderUtility.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GL11.glRotated((double)RenderUtility.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
                final Color c = Color.WHITE;
                final Color firstcolor = new Color(Color.white.getRGB());
                drawFilledCircleNoGL(0, 0, 0.0, c.hashCode(), quality);
                if (distanceFromPlayer < 4.0) {
                    drawFilledCircleNoGL(0, 0, 0.0, new Color(c.getRed(), c.getGreen(), c.getBlue(), 255).hashCode(), quality);
                }
                if (distanceFromPlayer < 20.0) {
                    drawFilledCircleNoGL(0, 0, 0.0, new Color(c.getRed(), c.getGreen(), c.getBlue(), 255).hashCode(), quality);
                }
                GL11.glScalef(0.8f, 0.8f, 0.8f);
                GL11.glPopMatrix();
            }
        }
        catch (ConcurrentModificationException ex) {}
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableDepth();
        GL11.glColor3d(255.0, 255.0, 255.0);
    }
    
    public static void resetColor() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
    }
    
    static {
        RenderUtility.roundedShader = new ShaderUtility("roundedRect");
        roundedGradientShader = new ShaderUtility("roundedRectGradient");
        RenderUtility.roundedOutlineShader = new ShaderUtility("roundRectOutline");
        RenderUtility.roundedTexturedShader = new ShaderUtility("roundedTexturedShader");
        RenderUtility.frustum = new Frustum();
    }
}
