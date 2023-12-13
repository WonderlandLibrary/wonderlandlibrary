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
import dev.rise.setting.impl.BooleanSetting;
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

    private final ModeSetting mode = new ModeSetting("Mode", this, "Theme", "Theme", "White");

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        GL11.glPushMatrix();

        final ScaledResolution scaledresolution = new ScaledResolution(mc);
        final EntityRenderer entityRenderer = mc.entityRenderer;
        final int scaleFactor = scaledresolution.getScaleFactor();
        final RenderManager renderMng = mc.getRenderManager();

        int amount = 0;
        for (final EntityPlayer p : mc.theWorld.playerEntities) {
            if (p != null) {
                final String name = p.getName();
                if (!p.isDead && p != mc.thePlayer && !p.bot && !name.isEmpty() && !name.equals(" ") && RenderUtil.isInViewFrustrum(p)) {
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
                        final double posX = Math.round(position.x * 2) / 2D + 0.25;
                        final double posY = Math.round(position.y * 2) / 2D + 0.25;
                        final double endPosX = Math.round(position.z * 2) / 2D + 0.25;
                        final double endPosY = Math.round(position.w * 2) / 2D + 0.25;

                        //Drawing box
                        final Color boxColor = ThemeUtil.getThemeColor(amount, ThemeType.GENERAL, 0.5f);

                        final double percentage = (endPosY - posY) * p.getHealth() / p.getMaxHealth();

                        final double distance = 2.5;

                        final float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
                        final Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                        final float progress = p.getHealth() / p.getMaxHealth();
                        final Color healthColor = p.getHealth() >= 0.0F ? ColorUtil.blendColors(fractions, colors, progress).brighter() : Color.RED;

                        final boolean white = !mode.getMode().equals("Theme");

                        // Box out
                        RenderUtil.lineNoGl(posX, posY, posX, endPosY, Color.BLACK); // left
                        RenderUtil.lineNoGl(posX, endPosY, endPosX, endPosY, Color.BLACK); // bottom
                        RenderUtil.lineNoGl(posX, posY, endPosX, posY, Color.BLACK); // top
                        RenderUtil.lineNoGl(endPosX, posY, endPosX, endPosY, Color.BLACK); // right

                        // Box main
                        RenderUtil.gradient(posX + 0.5, posY + 0.5, 0.5, (endPosY - posY - 1), // left
                                white ? Color.white : ThemeUtil.getThemeColor(2.35F, ThemeType.GENERAL, 0.5F), white ? Color.white : ThemeUtil.getThemeColor(3.35F, ThemeType.GENERAL, 0.5F));
                        RenderUtil.gradientSideways(posX + 0.5, endPosY - 1, (endPosX - posX - 1), 0.5, // bottom
                                white ? Color.white : ThemeUtil.getThemeColor(-1, ThemeType.GENERAL, 0.5F), white ? Color.white : ThemeUtil.getThemeColor(0, ThemeType.GENERAL, 0.5F));
                        RenderUtil.gradientSideways(posX + 0.5, posY, (endPosX - posX - 0.5), 0.5, // top
                                white ? Color.white : ThemeUtil.getThemeColor(2, ThemeType.GENERAL, 0.5F), white ? Color.white : ThemeUtil.getThemeColor(1, ThemeType.GENERAL, 0.5F));
                        RenderUtil.gradient(endPosX - 0.5, posY + 0.5, 0.5, (endPosY - posY - 1), // right
                                white ? Color.white : ThemeUtil.getThemeColor(1, ThemeType.GENERAL, 0.5F), white ? Color.white : ThemeUtil.getThemeColor(0, ThemeType.GENERAL, 0.5F));

                        // Box in
                        RenderUtil.lineNoGl(posX + 1, posY + 1, posX + 1, endPosY - 1, Color.BLACK); // left
                        RenderUtil.lineNoGl(posX + 1, endPosY - 1, endPosX - 1, endPosY - 1, Color.BLACK); // bottom
                        RenderUtil.lineNoGl(posX + 1, posY + 1, endPosX - 1, posY + 1, Color.BLACK); // top
                        RenderUtil.lineNoGl(endPosX - 1, posY + 1, endPosX - 1, endPosY - 1, Color.BLACK); // right

                        // Health
                        RenderUtil.lineNoGl(posX - distance - 0.5, endPosY - percentage, posX - distance - 0.5, endPosY, Color.BLACK);
                        RenderUtil.lineNoGl(posX - distance, endPosY - percentage, posX - distance, endPosY, Color.BLACK);
                        RenderUtil.lineNoGl(posX - distance + 0.5, endPosY - percentage, posX - distance + 0.5, endPosY, Color.BLACK);

                        RenderUtil.lineNoGl(posX - distance, endPosY - percentage + 0.5, posX - distance, endPosY - 0.5, healthColor);
                    }
                    amount++;
                }
            }
        }

        GL11.glPopMatrix();
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