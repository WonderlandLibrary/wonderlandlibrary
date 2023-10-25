/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import baritone.api.utils.Helper;
import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;

public interface IRenderer {
    public static final Tessellator tessellator = Tessellator.getInstance();
    public static final BufferBuilder buffer = tessellator.getBuffer();
    public static final RenderManager renderManager = Helper.mc.getRenderManager();
    public static final Settings settings = BaritoneAPI.getSettings();

    public static void glColor(Color color, float alpha) {
        float[] colorComponents = color.getColorComponents(null);
        GlStateManager.color(colorComponents[0], colorComponents[1], colorComponents[2], alpha);
    }

    public static void startLines(Color color, float alpha, float lineWidth, boolean ignoreDepth) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        IRenderer.glColor(color, alpha);
        GlStateManager.glLineWidth(lineWidth);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        if (ignoreDepth) {
            GlStateManager.disableDepth();
        }
    }

    public static void startLines(Color color, float lineWidth, boolean ignoreDepth) {
        IRenderer.startLines(color, 0.4f, lineWidth, ignoreDepth);
    }

    public static void endLines(boolean ignoredDepth) {
        if (ignoredDepth) {
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawAABB(AxisAlignedBB aabb) {
        AxisAlignedBB toDraw = aabb.offset(-IRenderer.renderManager.viewerPosX, -IRenderer.renderManager.viewerPosY, -IRenderer.renderManager.viewerPosZ);
        buffer.begin(1, DefaultVertexFormats.POSITION);
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.pos(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawAABB(AxisAlignedBB aabb, double expand) {
        IRenderer.drawAABB(aabb.expand(expand, expand, expand));
    }
}

