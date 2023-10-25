package tech.dort.dortware.impl.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import skidmonke.Minecraft;

import static org.lwjgl.opengl.GL11.glBlendFunc;

public class RenderUtils {

    public static void entityRenderer(Minecraft mc, Entity entity, Runnable runnable) {
        final double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        final double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.translate(x, y, z);
        runnable.run();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor3f(1, 1, 1);
        GL11.glPopMatrix();
    }

    public static void drawChestESP(BlockPos blockPos, float red, float green, float blue, float width) {
        final double x = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        final double y = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
        final double z = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;

        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(width);
        GL11.glColor4d(red, green, blue, 0.15F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, 0.5F);

        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0F, y + 1.0F, z + 1.0F));

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    public static void drawOutlinedRectNoColor(float left, float top, float right, float bottom) {
        float var5;

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

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldRenderer.startDrawing(2);
        worldRenderer.addVertex(left, bottom, 0.0D);
        worldRenderer.addVertex(right, bottom, 0.0D);
        worldRenderer.addVertex(right, top, 0.0D);
        worldRenderer.addVertex(left, top, 0.0D);
        tessellator.draw();
        GlStateManager.func_179098_w();
        GlStateManager.disableBlend();
    }

    public static void drawRectNoColor(double left, double top, double right, double bottom) {
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

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0D);
        worldRenderer.addVertex(right, bottom, 0.0D);
        worldRenderer.addVertex(right, top, 0.0D);
        worldRenderer.addVertex(left, top, 0.0D);
        tessellator.draw();
        GlStateManager.func_179098_w();
        GlStateManager.disableBlend();
    }
}
