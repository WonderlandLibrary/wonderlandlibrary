package rosegoldclient.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import rosegoldclient.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();
    private static final int[] DISPLAY_LISTS_2D = new int[4];
    private static final Minecraft mc = Main.mc;

    static {
        for (int i = 0; i < DISPLAY_LISTS_2D.length; i++) {
            DISPLAY_LISTS_2D[i] = glGenLists(1);
        }

        glNewList(DISPLAY_LISTS_2D[0], GL_COMPILE);

        quickDrawRect(-7F, 2F, -4F, 3F);
        quickDrawRect(4F, 2F, 7F, 3F);
        quickDrawRect(-7F, 0.5F, -6F, 3F);
        quickDrawRect(6F, 0.5F, 7F, 3F);

        glEndList();

        glNewList(DISPLAY_LISTS_2D[1], GL_COMPILE);

        quickDrawRect(-7F, 3F, -4F, 3.3F);
        quickDrawRect(4F, 3F, 7F, 3.3F);
        quickDrawRect(-7.3F, 0.5F, -7F, 3.3F);
        quickDrawRect(7F, 0.5F, 7.3F, 3.3F);

        glEndList();

        glNewList(DISPLAY_LISTS_2D[2], GL_COMPILE);

        quickDrawRect(4F, -20F, 7F, -19F);
        quickDrawRect(-7F, -20F, -4F, -19F);
        quickDrawRect(6F, -20F, 7F, -17.5F);
        quickDrawRect(-7F, -20F, -6F, -17.5F);

        glEndList();

        glNewList(DISPLAY_LISTS_2D[3], GL_COMPILE);

        quickDrawRect(7F, -20F, 7.3F, -17.5F);
        quickDrawRect(-7.3F, -20F, -7F, -17.5F);
        quickDrawRect(4F, -20.3F, 7.3F, -20F);
        quickDrawRect(-7.3F, -20.3F, -4F, -20F);

        glEndList();
    }

    private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    public static void drawEntityESP(Entity entity, int color, float partialTicks) {
        drawEntityESP(entity, new Color((color >> 24 & 0xFF) / 255.0f, (color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f), partialTicks);
    }

    public static void drawEntityESP(Entity entity, Color color, float partialTicks) {
        drawEntityESP(entity, color, partialTicks, new AxisAlignedBB(0, 0, 0, 0, 0.15, 0));
    }

    public static void drawEntityESP(Entity entity, Color color, float partialTicks, AxisAlignedBB inflate) {
        if (Main.configFile.silentMode) return;
        final RenderManager renderManager = mc.getRenderManager();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
                - renderManager.viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
                - renderManager.viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
                - renderManager.viewerPosZ;

        final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBox.minX - entity.posX + x + inflate.minX,
                entityBox.minY - entity.posY + y + inflate.minY,
                entityBox.minZ - entity.posZ + z + inflate.minZ,
                entityBox.maxX - entity.posX + x + inflate.maxX,
                entityBox.maxY - entity.posY + y + inflate.maxY,
                entityBox.maxZ - entity.posZ + z + inflate.maxZ
        );

        glLineWidth((float) 3);
        enableGlCap(GL_LINE_SMOOTH);
        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espEntityOutlineAlpha);
        drawOutlinedBox(axisAlignedBB);

        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espEntityBoxAlpha);
        drawSolidBox(axisAlignedBB);


        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDepthMask(true);
        resetCaps();
    }

    public static void drawPointESP(Vec3d vec3d, int color, float partialTicks) {
        drawPointESP(vec3d, new Color((color >> 24 & 0xFF) / 255.0f, (color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f), partialTicks);
    }

    public static void drawPointESP(Vec3d vec3d, Color color, float partialTicks) {
        if (Main.configFile.silentMode) return;
        RenderManager renderManager = mc.getRenderManager();

        double x = vec3d.x - renderManager.viewerPosX;
        double y = vec3d.y - renderManager.viewerPosY;
        double z = vec3d.z - renderManager.viewerPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - 0.05, y - 0.05, z - 0.05, x + 0.05, y + 0.05, z + 0.05);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        glLineWidth((float) 3);
        enableGlCap(GL_LINE_SMOOTH);
        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espBlockOutlineAlpha);
        drawOutlinedBox(axisAlignedBB);

        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espBlockBoxAlpha);
        drawSolidBox(axisAlignedBB);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDepthMask(true);
        resetCaps();
    }

    public static void drawBlockESP(BlockPos blockPos, int color, float partialTicks) {
        drawBlockESP(blockPos, new Color((color >> 24 & 0xFF) / 255.0f, (color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f), partialTicks);
    }

    public static void drawBlockESP(BlockPos blockPos, Color color, float partialTicks) {
        if (Main.configFile.silentMode) return;
        final RenderManager renderManager = mc.getRenderManager();

        final double x = blockPos.getX() - renderManager.viewerPosX;
        final double y = blockPos.getY() - renderManager.viewerPosY;
        final double z = blockPos.getZ() - renderManager.viewerPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final IBlockState iBlockState = mc.world.getBlockState(blockPos);

        if (iBlockState != null) {
            final EntityPlayer player = mc.player;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            axisAlignedBB = iBlockState.getSelectedBoundingBox(mc.world, blockPos)
                    .offset(-posX, -posY, -posZ);
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        glLineWidth((float) 3);
        enableGlCap(GL_LINE_SMOOTH);
        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espBlockOutlineAlpha);
        drawOutlinedBox(axisAlignedBB);

        glColor(color.getRed(), color.getGreen(), color.getBlue(), Main.configFile.espBlockBoxAlpha);
        drawSolidBox(axisAlignedBB);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDepthMask(true);
        resetCaps();
    }

    public static void drawLine(final Vec3d start, final Vec3d end, final float thickness, final float partialTicks) {
        final Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        final BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();

        final double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        final double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        final double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDisable(3553);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glLineWidth(thickness);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(start.x, start.y, start.z).color(1.0f, 0.65f, 0.0f, 1.0f).endVertex();
        worldRenderer.pos(end.x, end.y, end.z).color(1.0f, 0.65f, 0.0f, 1.0f).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public static void drawLines(ArrayList<Vec3d> poses, final float thickness, final float partialTicks) {
        final Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        final BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
        final double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        final double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        final double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDisable(3553);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glLineWidth(thickness);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        int num = 0;
        for (final Vec3d pos : poses) {
            final int i = ColorUtils.getChroma(2500.0f, num++ * 5);
            worldRenderer.pos(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5).color((i >> 16 & 0xFF) / 255.0f, (i >> 8 & 0xFF) / 255.0f, (i & 0xFF) / 255.0f, (i >> 24 & 0xFF) / 255.0f).endVertex();
        }
        Tessellator.getInstance().draw();
        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    /**
     * Modified from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     */
    public static void renderWaypointText(String str, double X, double Y, double Z, float partialTicks) {
        renderWaypointText(str, X, Y, Z, partialTicks, true);
    }

    public static void renderWaypointText(String str, double X, double Y, double Z, float partialTicks, boolean showDist) {
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = Main.mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = X - viewerX;
        double y = Y - viewerY - viewer.getEyeHeight();
        double z = Z - viewerZ;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        drawNametag(str);

        GlStateManager.rotate(-Main.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Main.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-Main.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(Main.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        if (showDist) {
            if (Main.configFile.nametagDistanceDecimalPoints == 0) {
                drawNametag("§e" + Math.round(dist) + " blocks");
            } else {
                double modifier = Math.pow(10, Main.configFile.nametagDistanceDecimalPoints);
                drawNametag("§e" + Math.round(dist * modifier) / modifier + " blocks");
            }
        }

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
    }

    public static void drawNametag(String str) {
        FontRenderer fontrenderer = Main.mc.fontRenderer;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Main.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Main.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void drawRoundRect(float left, float top, float right, float bottom, final float radius, final int color) {
        left += radius;
        right -= radius;
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, top, 0.0).endVertex();
        bufferBuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(right - radius, top - radius, 0.0).endVertex();
        bufferBuilder.pos(right, top - radius, 0.0).endVertex();
        bufferBuilder.pos(right, bottom + radius, 0.0).endVertex();
        bufferBuilder.pos(right - radius, bottom + radius, 0.0).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, top - radius, 0.0).endVertex();
        bufferBuilder.pos(left + radius, top - radius, 0.0).endVertex();
        bufferBuilder.pos(left + radius, bottom + radius, 0.0).endVertex();
        bufferBuilder.pos(left, bottom + radius, 0.0).endVertex();
        tessellator.draw();
        drawArc(right, bottom + radius, radius, 180);
        drawArc(left, bottom + radius, radius, 90);
        drawArc(right, top - radius, radius, 270);
        drawArc(left, top - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundRect2(float x, float y, float width, float height, final float radius, final int color) {
        width += x;
        x += radius;
        width -= radius;
        if (x < width) {
            final float i = x;
            x = width;
            width = i;
        }
        height += y;
        if (y < height) {
            final float j = y;
            y = height;
            height = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(x, height, 0.0).endVertex();
        bufferBuilder.pos(width, height, 0.0).endVertex();
        bufferBuilder.pos(width, y, 0.0).endVertex();
        bufferBuilder.pos(x, y, 0.0).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(width - radius, y - radius, 0.0).endVertex();
        bufferBuilder.pos(width, y - radius, 0.0).endVertex();
        bufferBuilder.pos(width, height + radius, 0.0).endVertex();
        bufferBuilder.pos(width - radius, height + radius, 0.0).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(x, y - radius, 0.0).endVertex();
        bufferBuilder.pos(x + radius, y - radius, 0.0).endVertex();
        bufferBuilder.pos(x + radius, height + radius, 0.0).endVertex();
        bufferBuilder.pos(x, height + radius, 0.0).endVertex();
        tessellator.draw();
        drawArc(width, height + radius, radius, 180);
        drawArc(x, height + radius, radius, 90);
        drawArc(width, y - radius, radius, 270);
        drawArc(x, y - radius, radius, 0);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawArc(final float x, final float y, final float radius, final int angleStart) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(6, DefaultVertexFormats.POSITION);
        GlStateManager.translate(x, y, 0.0);
        bufferBuilder.pos(0.0, 0.0, 0.0).endVertex();
        final int points = 21;
        for (double i = 0.0; i < points; ++i) {
            final double radians = Math.toRadians(i / points * 90.0 + angleStart);
            bufferBuilder.pos(radius * Math.sin(radians), radius * Math.cos(radians), 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.translate(-x, -y, 0.0);
    }

    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, top, 0.0).endVertex();
        bufferBuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedBox() {
        drawOutlinedBox(DEFAULT_AABB);
    }

    public static void drawOutlinedBox(AxisAlignedBB bb) {

        glBegin(GL_LINES);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }

    public static void drawSolidBox() {
        drawSolidBox(DEFAULT_AABB);
    }

    public static void drawSolidBox(AxisAlignedBB bb) {

        glBegin(GL_QUADS);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }

    public static void drawRotatedTexture(ResourceLocation resourceLocation, int x, int y, int width, int height, int angle) {
        drawRotatedTexture(resourceLocation, x, y, width, height, width, height, 0, 0, angle);
    }

    public static void drawRotatedTexture(ResourceLocation resourceLocation, int x, int y, int width, int height, int textureWidth, int textureHeight, int textureX, int textureY, int angle) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + width / 2f, y + height / 2f, 0);
        GlStateManager.rotate(angle, 0, 0, 1);
        GlStateManager.translate(-x - width / 2f, -y - height / 2f, 0);

        drawTexture(resourceLocation, x, y, width, height, textureWidth, textureHeight, textureX, textureY);

        GlStateManager.popMatrix();
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int width, int height, int textureWidth, int textureHeight, int textureX, int textureY) {
        Main.mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(255, 255, 255);
        Gui.drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, textureWidth, textureHeight);
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int width, int height) {
        drawTexture(resourceLocation, x, y, width, height, width, height, 0, 0);
    }

    public static void quickDrawRect(final float x, final float y, final float x2, final float y2) {
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();
    }

    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void enableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, true);
    }

    public static void disableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void disableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, false);
    }

    public static void setGlCap(final int cap, final boolean state) {
        glCapMap.put(cap, glGetBoolean(cap));
        setGlState(cap, state);
    }

    public static void setGlState(final int cap, final boolean state) {
        if (state)
            glEnable(cap);
        else
            glDisable(cap);
    }

    public static void glColor(final int red, final int green, final int blue, final int alpha) {
        GL11.glColor4f(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void glColor(final Color color) {
        glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private static void glColor(final int hex) {
        glColor(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, hex >> 24 & 0xFF);
    }
}
