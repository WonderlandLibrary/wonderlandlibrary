// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.render.GLUtil;
import java.util.Iterator;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ColorUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import java.awt.Color;
import dev.tenacity.module.Category;
import net.minecraft.util.Vec3;
import java.util.List;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Breadcrumbs extends Module
{
    private final ModeSetting mode;
    private final NumberSetting particleAmount;
    private final BooleanSetting seeThroughWalls;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    private final List<Vec3> path;
    
    public Breadcrumbs() {
        super("Breadcrumbs", "Breadcrumbs", Category.RENDER, "shows where you've walked");
        this.mode = new ModeSetting("Mode", "Rise", new String[] { "Rise", "Line" });
        this.particleAmount = new NumberSetting("Particle Amount", 15.0, 500.0, 1.0, 1.0);
        this.seeThroughWalls = new BooleanSetting("Walls", true);
        this.colorMode = new ModeSetting("Color Mode", "Sync", new String[] { "Sync", "Custom" });
        this.color = new ColorSetting("Color", Color.WHITE);
        this.path = new ArrayList<Vec3>();
        this.color.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom"));
        this.seeThroughWalls.addParent(this.mode, mode -> mode.getMode().equals("Rise"));
        this.addSettings(this.mode, this.particleAmount, this.seeThroughWalls, this.colorMode, this.color);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPre()) {
            if (Breadcrumbs.mc.thePlayer.lastTickPosX != Breadcrumbs.mc.thePlayer.posX || Breadcrumbs.mc.thePlayer.lastTickPosY != Breadcrumbs.mc.thePlayer.posY || Breadcrumbs.mc.thePlayer.lastTickPosZ != Breadcrumbs.mc.thePlayer.posZ) {
                this.path.add(new Vec3(Breadcrumbs.mc.thePlayer.posX, Breadcrumbs.mc.thePlayer.posY, Breadcrumbs.mc.thePlayer.posZ));
            }
            while (this.path.size() > this.particleAmount.getValue()) {
                this.path.remove(0);
            }
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        int i = 0;
        final Pair<Color, Color> colors = this.colorMode.is("Custom") ? Pair.of(this.color.getColor(), this.color.getAltColor()) : HUDMod.getClientColors();
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Rise": {
                if (this.seeThroughWalls.isEnabled()) {
                    GlStateManager.disableDepth();
                }
                GL11.glEnable(3042);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glBlendFunc(770, 771);
                for (final Vec3 v : this.path) {
                    ++i;
                    boolean draw = true;
                    final double x = v.xCoord - Breadcrumbs.mc.getRenderManager().renderPosX;
                    final double y = v.yCoord - Breadcrumbs.mc.getRenderManager().renderPosY;
                    final double z = v.zCoord - Breadcrumbs.mc.getRenderManager().renderPosZ;
                    final double distanceFromPlayer = Breadcrumbs.mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1.0, v.zCoord);
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
                    if (draw) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(x, y, z);
                        final float scale = 0.06f;
                        GL11.glScalef(-0.06f, -0.06f, -0.06f);
                        GL11.glRotated((double)(-Breadcrumbs.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                        GL11.glRotated((double)Breadcrumbs.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
                        Color c = ColorUtil.interpolateColorsBackAndForth(7, 3 + i * 20, colors.getFirst(), colors.getSecond(), false);
                        if (this.colorMode.is("Custom") && this.color.isRainbow()) {
                            c = this.color.getRainbow().getColor(3 + i * 20);
                        }
                        RenderUtil.drawFilledCircleNoGL(0, -2, 0.7, ColorUtil.applyOpacity(c.getRGB(), 0.6f), quality);
                        if (distanceFromPlayer < 4.0) {
                            RenderUtil.drawFilledCircleNoGL(0, -2, 1.4, ColorUtil.applyOpacity(c.getRGB(), 0.25f), quality);
                        }
                        if (distanceFromPlayer < 20.0) {
                            RenderUtil.drawFilledCircleNoGL(0, -2, 2.3, ColorUtil.applyOpacity(c.getRGB(), 0.15f), quality);
                        }
                        GL11.glScalef(0.8f, 0.8f, 0.8f);
                        GL11.glPopMatrix();
                    }
                }
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                if (this.seeThroughWalls.isEnabled()) {
                    GlStateManager.enableDepth();
                }
                GL11.glColor3d(255.0, 255.0, 255.0);
                break;
            }
            case "Line": {
                this.renderLine(this.path, colors);
                break;
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.path.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.path.clear();
        super.onDisable();
    }
    
    public void renderLine(final List<Vec3> path) {
        this.renderLine(path, Pair.of(Color.WHITE));
    }
    
    public void renderLine(final List<Vec3> path, final Pair<Color, Color> colors) {
        GlStateManager.disableDepth();
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.setup2DRendering();
        GLUtil.startBlend();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(3);
        final boolean custom = this.colorMode.is("Custom") && this.color.isRainbow();
        int count = 0;
        int alpha = 200;
        final int fadeOffset = 15;
        for (final Vec3 v : path) {
            if (fadeOffset > count) {
                alpha = count * (200 / fadeOffset);
            }
            RenderUtil.resetColor();
            if (custom || HUDMod.isRainbowTheme()) {
                if (custom) {
                    RenderUtil.color(this.color.getRainbow().getColor(count * 2).getRGB(), alpha / 255.0f);
                }
                else {
                    RenderUtil.color(HUDMod.color1.getRainbow().getColor(count * 2).getRGB(), alpha / 255.0f);
                }
            }
            else {
                RenderUtil.color(ColorUtil.interpolateColorsBackAndForth(15, count * 5, colors.getFirst(), colors.getSecond(), false).getRGB(), alpha / 255.0f);
            }
            final double x = v.xCoord - Breadcrumbs.mc.getRenderManager().renderPosX;
            final double y = v.yCoord - Breadcrumbs.mc.getRenderManager().renderPosY;
            final double z = v.zCoord - Breadcrumbs.mc.getRenderManager().renderPosZ;
            GL11.glVertex3d(x, y, z);
            ++count;
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GLUtil.end2DRendering();
        GlStateManager.enableDepth();
    }
}
