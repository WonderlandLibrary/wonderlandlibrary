// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;

public class XuluTessellator extends Tessellator
{
    public static XuluTessellator INSTANCE;
    
    public XuluTessellator() {
        super(2097152);
    }
    
    public static void prepare(final int mode) {
        prepareGL();
        begin(mode);
    }
    
    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    public static void begin(final int mode) {
        XuluTessellator.INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }
    
    public static void release() {
        render();
        releaseGL();
    }
    
    public static void render() {
        XuluTessellator.INSTANCE.draw();
    }
    
    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }
    
    public static void drawRectDouble(double left, double top, double right, double bottom, final int color) {
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
    
    public static void drawRectGradient(double left, double top, double right, double bottom, final int color, final int color2) {
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
        final float a = (color2 >> 24 & 0xFF) / 255.0f;
        final float r = (color2 >> 16 & 0xFF) / 255.0f;
        final float g = (color2 >> 8 & 0xFF) / 255.0f;
        final float b = (color2 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GL11.glShadeModel(7424);
    }
    
    public static void drawOutlineLine(double left, double top, double right, double bottom, final float width, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
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
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(r, g, b, a).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawRectOutline(final double left, final double top, final double right, final double bottom, final double width, final int color) {
        drawRectOutline(left - width, top - width, right + width, bottom + width, left, top, right, bottom, color);
    }
    
    public static void drawRectOutline(final double left, final double top, final double right, final double bottom, final double left2, final double top2, final double right2, final double bottom2, final int color) {
        drawRectDouble(left, top, right, top2, color);
        drawRectDouble(right2, top2, right, bottom, color);
        drawRectDouble(left, bottom2, right2, bottom, color);
        drawRectDouble(left, top2, left2, bottom2, color);
    }
    
    public static void drawBox(final BlockPos blockPos, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBox(blockPos, r, g, b, a, sides);
    }
    
    public static void drawBox(final float x, final float y, final float z, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBox(XuluTessellator.INSTANCE.getBuffer(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawBox(final BlockPos blockPos, final int r, final int g, final int b, final int a, final int sides) {
        drawBox(XuluTessellator.INSTANCE.getBuffer(), (float)blockPos.x, (float)blockPos.y, (float)blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static BufferBuilder getBufferBuilder() {
        return XuluTessellator.INSTANCE.getBuffer();
    }
    
    public static void drawBox(final BufferBuilder buffer, final float x, final float y, final float z, final float w, final float h, final float d, final int r, final int g, final int b, final int a, final int sides) {
        if ((sides & 0x1) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x2) != 0x0) {
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x4) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x8) != 0x0) {
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x10) != 0x0) {
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x20) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
    }
    
    public static void drawFace(final BlockPos blockPos, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFace(blockPos, r, g, b, a, sides);
    }
    
    public static void drawFace(final BlockPos blockPos, final int r, final int g, final int b, final int a, final int sides) {
        drawFace(XuluTessellator.INSTANCE.getBuffer(), (float)blockPos.x, (float)blockPos.y, (float)blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawFace(final BufferBuilder buffer, final float x, final float y, final float z, final float w, final float h, final float d, final int r, final int g, final int b, final int a, final int sides) {
        if ((sides & 0x1) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
        }
    }
    
    public static void drawFaceOutline(final BlockPos blockPos, final int r, final int g, final int b, final int a, final int sides) {
        drawFaceOutline(XuluTessellator.INSTANCE.getBuffer(), (float)blockPos.x, (float)blockPos.y, (float)blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawFaceOutline(final BufferBuilder buffer, final float x, final float y, final float z, final float w, final float h, final float d, final int r, final int g, final int b, final int a, final int sides) {
        if ((sides & 0x1) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos(x + 0.02, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos(x + 0.02, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, z + d - 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, z + d - 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos(x + w - 0.02, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos(x + w - 0.02, (double)y, (double)z).color(r, g, b, a).endVertex();
        }
    }
    
    public static void drawBoxOutline(final BlockPos blockPos, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBoxOutline(blockPos, r, g, b, a, sides);
    }
    
    public static void drawBoxOutline(final float x, final float y, final float z, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBoxOutline(XuluTessellator.INSTANCE.getBuffer(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawBoxOutline(final BlockPos blockPos, final int r, final int g, final int b, final int a, final int sides) {
        drawBoxOutline(XuluTessellator.INSTANCE.getBuffer(), (float)blockPos.x, (float)blockPos.y, (float)blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawBoxOutline(final BufferBuilder buffer, final float x, final float y, final float z, final float w, final float h, final float d, final int r, final int g, final int b, final int a, final int sides) {
        if ((sides & 0x1) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos(x + 0.02, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos(x + 0.02, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, z + d - 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, z + d - 0.02).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos(x + w - 0.02, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos(x + w - 0.02, (double)y, (double)z).color(r, g, b, a).endVertex();
        }
    }
    
    public static void drawLines(final BlockPos blockPos, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawLines(blockPos, r, g, b, a, sides);
    }
    
    public static void drawLines(final BlockPos blockPos, final int r, final int g, final int b, final int a, final int sides) {
        drawLines(XuluTessellator.INSTANCE.getBuffer(), (float)blockPos.x, (float)blockPos.y, (float)blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }
    
    public static void drawLines(final BufferBuilder buffer, final float x, final float y, final float z, final float w, final float h, final float d, final int r, final int g, final int b, final int a, final int sides) {
        if ((sides & 0x11) != 0x0) {
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x12) != 0x0) {
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x21) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x22) != 0x0) {
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x5) != 0x0) {
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x6) != 0x0) {
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x9) != 0x0) {
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0xA) != 0x0) {
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x14) != 0x0) {
            buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x24) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x18) != 0x0) {
            buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x28) != 0x0) {
            buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
        }
    }
    
    public static void drawBox2(final AxisAlignedBB bb, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBox2(bb, r, g, b, a, sides);
    }
    
    public static void drawBox2(final AxisAlignedBB bb, final int r, final int g, final int b, final int a, final int sides) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        if ((sides & 0x1) != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x2) != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x4) != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x8) != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x10) != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x20) != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }
        tessellator.draw();
    }
    
    public static void drawFullBox2(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int argb) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFullBox2(bb, blockPos, width, r, g, b, a, 255);
    }
    
    public static void drawFullBox2(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int argb, final int alpha2) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFullBox2(bb, blockPos, width, r, g, b, a, alpha2);
    }
    
    public static void drawFullBox2(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int red, final int green, final int blue, final int alpha, final int alpha2) {
        prepare(7);
        drawBox2(bb, red, green, blue, alpha, 63);
        release();
        drawBoundingBox(bb, width, red, green, blue, alpha2);
    }
    
    public static void drawFullBox(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int argb, final int alpha2) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFullBox(bb, blockPos, width, r, g, b, a, alpha2);
    }
    
    public static void drawFullBox(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int red, final int green, final int blue, final int alpha, final int alpha2) {
        prepare(7);
        drawBox(blockPos, red, green, blue, alpha, 63);
        release();
        drawBoundingBox(bb, width, red, green, blue, alpha2);
    }
    
    public static void drawFullBoxAA(final AxisAlignedBB bb, final float width, final int argb, final int alpha2) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFullBoxAA(bb, width, r, g, b, a, alpha2);
    }
    
    public static void drawFullBoxAA(final AxisAlignedBB bb, final float width, final int red, final int green, final int blue, final int alpha, final int alpha2) {
        drawBox2(bb, red, green, blue, alpha, 63);
        drawBoundingBox(bb, width, red, green, blue, alpha2);
    }
    
    public static void drawFullFace(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int argb, final int alpha2) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawFullFace(bb, blockPos, width, r, g, b, a, alpha2);
    }
    
    public static void drawFullFace(final AxisAlignedBB bb, final BlockPos blockPos, final float width, final int red, final int green, final int blue, final int alpha, final int alpha2) {
        prepare(7);
        drawFace(blockPos, red, green, blue, alpha, 63);
        release();
        drawBoundingBoxFace(bb, width, red, green, blue, alpha2);
    }
    
    public static void drawBoundingBox(final AxisAlignedBB bb, final float width, final int argb) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawBoundingBox(bb, width, r, g, b, a);
    }
    
    public static void drawBoundingBox(final AxisAlignedBB bb, final float width, final int red, final int green, final int blue, final int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawIndicator(final AxisAlignedBB bb, final int argb, final int sides) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawIndicator(bb, r, g, b, a, sides);
    }
    
    public static void drawIndicator(final AxisAlignedBB bb, final int r, final int g, final int b, final int a, final int sides) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        if ((sides & 0x1) != 0x0) {
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x4) != 0x0) {
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, 0).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, 0).endVertex();
        }
        if ((sides & 0x8) != 0x0) {
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, 0).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, 0).endVertex();
        }
        if ((sides & 0x10) != 0x0) {
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, 0).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, 0).endVertex();
        }
        if ((sides & 0x20) != 0x0) {
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, 0).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, 0).endVertex();
        }
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawBoundingBoxFace(final AxisAlignedBB bb, final float width, final int red, final int green, final int blue, final int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawForgehaxLines(final BufferBuilder buffer, final double x0, final double y0, final double z0, final double x1, final double y1, final double z1, final int sides, final int argb) {
        final int a = argb >>> 24 & 0xFF;
        final int r = argb >>> 16 & 0xFF;
        final int g = argb >>> 8 & 0xFF;
        final int b = argb & 0xFF;
        drawForgehaxLines(buffer, x0, y0, z0, x1, y1, z1, sides, a, r, g, b);
    }
    
    public static void drawForgehaxLines(final BufferBuilder buffer, final double x0, final double y0, final double z0, final double x1, final double y1, final double z1, final int sides, final int a, final int r, final int g, final int b) {
        if ((sides & 0x11) != 0x0) {
            buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex();
            buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x12) != 0x0) {
            buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex();
            buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x21) != 0x0) {
            buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex();
            buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x22) != 0x0) {
            buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex();
            buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x5) != 0x0) {
            buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex();
            buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x6) != 0x0) {
            buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex();
            buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x9) != 0x0) {
            buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex();
            buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0xA) != 0x0) {
            buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex();
            buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x14) != 0x0) {
            buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex();
            buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x24) != 0x0) {
            buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex();
            buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x18) != 0x0) {
            buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex();
            buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex();
        }
        if ((sides & 0x28) != 0x0) {
            buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex();
            buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        }
    }
    
    static {
        XuluTessellator.INSTANCE = new XuluTessellator();
    }
}
