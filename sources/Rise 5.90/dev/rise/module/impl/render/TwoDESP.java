/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "2DESP", description = "2D version of ESP", category = Category.RENDER)
public final class TwoDESP extends Module {

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    private final ModeSetting mode = new ModeSetting("Mode", this, "Real", "Real", "Classic");

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (mode.is("Classic")) {
            int amount = 0;
            for (final EntityPlayer entity : mc.theWorld.playerEntities) {
                if (entity != null) {
                    final String name = entity.getName();
                    if (!entity.isDead && entity != mc.thePlayer && !name.isEmpty() && !entity.bot && !name.equals(" ") && RenderUtil.isInViewFrustrum(entity)) {
                        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
                        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY);
                        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;

                        GL11.glPushMatrix();
                        GL11.glTranslated(x, y - 0.2, z);
                        GlStateManager.disableDepth();

                        GL11.glRotated(-(mc.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);

                        final float width = 1.1f;
                        final float height = 2.2f;

                        final float lineWidth = 0.07f;

                        draw2DBox(width, height, lineWidth, 0.04f, new Color(0, 0, 0, 165));

                        if (entity.hurtTime > 0)
                            draw2DBox(width, height, lineWidth, 0, new Color(255, 30, 30, 255));
                        else
                            draw2DBox(width, height, lineWidth, 0, ThemeUtil.getThemeColor(amount, ThemeType.GENERAL, 0.5f));

                        GlStateManager.enableDepth();
                        GL11.glPopMatrix();
                        amount++;
                    }
                }
            }
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mode.is("Real")) {
            GL11.glPushMatrix();

            final ScaledResolution scaledresolution = new ScaledResolution(mc);
            final EntityRenderer entityRenderer = mc.entityRenderer;
            final int scaleFactor = scaledresolution.getScaleFactor();
            final RenderManager renderMng = mc.getRenderManager();

            int amount = 0;
            for (final EntityPlayer p : mc.theWorld.playerEntities) {
                if (p != null) {
                    final String name = p.getName();
                    if (!p.isDead && p != mc.thePlayer && !p.isInvisible() && !p.bot && !name.isEmpty() && !name.equals(" ") && RenderUtil.isInViewFrustrum(p)) {
                        final float partialTicks = mc.timer.renderPartialTicks;
                        final double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * partialTicks;
                        final double y = (p.lastTickPosY + (p.posY - p.lastTickPosY) * partialTicks);
                        final double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * partialTicks;

                        final double width = p.width / 1.5D;
                        final double height = p.height + (p.isSneaking() ? -0.3D : 0.2D);
                        final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                        final List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));

                        entityRenderer.setupCameraTransform(partialTicks, 0);

                        Vector4d position = null;
                        for (Vector3d v : vectors) {

                            v = project2D(scaleFactor, v.x - renderMng.viewerPosX, v.y - renderMng.viewerPosY, v.z - renderMng.viewerPosZ);
                            if (v != null && v.z >= 0.0D && v.z < 1.0D) {
                                if (position == null)
                                    position = new Vector4d(v.x, v.y, v.z, 0.0D);
                                position.x = Math.min(v.x, position.x);
                                position.y = Math.min(v.y, position.y);
                                position.z = Math.max(v.x, position.z);
                                position.w = Math.max(v.y, position.w);
                            }

                        }

                        if (position != null) {

                            entityRenderer.setupOverlayRendering();
                            final double posX = position.x;
                            final double posY = position.y;
                            final double endPosX = position.z;
                            final double endPosY = position.w;

                            final float w = 0.5f;

                            //Drawing box
                            final Color c = ThemeUtil.getThemeColor(amount, ThemeType.GENERAL, 0.5f);

                            RenderUtil.lineNoGl(posX - w, posY, posX + w - w, endPosY, c);
                            RenderUtil.lineNoGl(posX, endPosY - w, endPosX, endPosY, c);
                            RenderUtil.lineNoGl(posX - w, posY, endPosX, posY + w, c);
                            RenderUtil.lineNoGl(endPosX - w, posY, endPosX, endPosY, c);

                            final double percentage = (endPosY - posY) * p.getHealth() / p.getMaxHealth();

                            final double distance = 2;

                            final float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
                            final Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                            final float progress = p.getHealth() / p.getMaxHealth();
                            final Color healthColor = p.getHealth() >= 0.0F ? ColorUtil.blendColors(fractions, colors, progress).brighter() : Color.RED;

                            RenderUtil.lineNoGl(posX - w - distance, endPosY - percentage, posX + w - w - distance, endPosY, healthColor);
                        }
                        amount++;
                    }
                }
            }

            GL11.glPopMatrix();
        }
    }

    private void draw2DBox(final float width, final float height, final float lineWidth, final float offset, final Color c) {
        RenderUtil.rect(-width / 2 - offset, -offset, width / 4, lineWidth, c);
        RenderUtil.rect(width / 2 - offset, -offset, -width / 4, lineWidth, c);
        RenderUtil.rect(width / 2 - offset, height - offset, -width / 4, lineWidth, c);
        RenderUtil.rect(-width / 2 - offset, height - offset, width / 4, lineWidth, c);

        RenderUtil.rect(-width / 2 - offset, height - offset, lineWidth, -height / 4, c);
        RenderUtil.rect(width / 2 - lineWidth - offset, height - offset, lineWidth, -height / 4, c);
        RenderUtil.rect(width / 2 - lineWidth - offset, -offset, lineWidth, height / 4, c);
        RenderUtil.rect(-width / 2 - offset, -offset, lineWidth, height / 4, c);
    }

    private Vector3d project2D(final int scaleFactor, final double x, final double y, final double z) {

        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector))
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        return null;
    }
}