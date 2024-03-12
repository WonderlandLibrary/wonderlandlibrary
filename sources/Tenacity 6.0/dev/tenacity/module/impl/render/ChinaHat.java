// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.util.MathHelper;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.module.impl.combat.KillAura;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.entity.Entity;
import dev.tenacity.utils.render.ESPUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.module.settings.Setting;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class ChinaHat extends Module
{
    private final BooleanSetting firstPerson;
    private final BooleanSetting allPlayers;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    
    public ChinaHat() {
        super("ChinaHat", "China Hat", Category.RENDER, "epic hat");
        this.firstPerson = new BooleanSetting("Show in first person", false);
        this.allPlayers = new BooleanSetting("All players", false);
        this.colorMode = new ModeSetting("Color Mode", "Sync", new String[] { "Sync", "Custom" });
        (this.color = new ColorSetting("Color", Color.WHITE)).addParent(this.colorMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(this.firstPerson, this.allPlayers, this.colorMode, this.color);
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (ChinaHat.mc.thePlayer == null || ChinaHat.mc.theWorld == null) {
            return;
        }
        final float partialTicks = event.getTicks();
        final double renderPosX = ChinaHat.mc.getRenderManager().renderPosX;
        final double renderPosY = ChinaHat.mc.getRenderManager().renderPosY;
        final double renderPosZ = ChinaHat.mc.getRenderManager().renderPosZ;
        GL11.glShadeModel(7425);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        for (final EntityPlayer player : ChinaHat.mc.theWorld.playerEntities) {
            final boolean self = player == ChinaHat.mc.thePlayer;
            if ((this.allPlayers.isEnabled() || self) && (!self || this.firstPerson.isEnabled() || ChinaHat.mc.gameSettings.thirdPersonView != 0) && !player.isDead && !player.isInvisible()) {
                if (!self) {
                    if (!ESPUtil.isInView(player)) {
                        continue;
                    }
                    if (!ChinaHat.mc.thePlayer.canEntityBeSeen(player)) {
                        continue;
                    }
                }
                GL11.glPushMatrix();
                final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - renderPosX;
                final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - renderPosY;
                final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - renderPosZ;
                final AxisAlignedBB bb = player.getEntityBoundingBox();
                final boolean lowerHeight = CustomModel.enabled && ChinaHat.mc.gameSettings.thirdPersonView != 0;
                final double height = (lowerHeight ? (-CustomModel.getYOffset()) : 0.0) + bb.maxY - bb.minY + 0.02;
                final double radius = bb.maxX - bb.minX;
                float yaw = MathUtils.interpolate(player.prevRotationYawHead, player.rotationYawHead, partialTicks).floatValue();
                if (Tenacity.INSTANCE.isEnabled(KillAura.class) || Tenacity.INSTANCE.isEnabled(Scaffold.class)) {
                    yaw = MathUtils.interpolate(MathHelper.wrapAngleTo180_float(player.prevRotationYawHead), MathHelper.wrapAngleTo180_float(player.rotationYawHead), partialTicks).floatValue();
                }
                final float pitch = MathUtils.interpolate(player.prevRotationPitchHead, player.rotationPitchHead, partialTicks).floatValue();
                GL11.glTranslated(0.0, posY + height, 0.0);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                if (self) {
                    GL11.glRotated((double)yaw, 0.0, -1.0, 0.0);
                }
                GL11.glRotated(pitch / 3.0, 1.0, 0.0, 0.0);
                GL11.glTranslated(0.0, 0.0, pitch / 270.0);
                GL11.glLineWidth(2.0f);
                GL11.glBegin(2);
                for (int i = 0; i <= 180; ++i) {
                    final int color1 = this.getColor(i * 4, 0.5f).getRGB();
                    RenderUtil.color(color1);
                    GL11.glVertex3d(posX - Math.sin(i * MathHelper.PI2 / 90.0f) * radius, -(player.isSneaking() ? 0.2 : 0.0) - 0.002, posZ + Math.cos(i * MathHelper.PI2 / 90.0f) * radius);
                }
                GL11.glEnd();
                GL11.glBegin(6);
                final int color2 = this.getColor(4, 0.7f).getRGB();
                RenderUtil.color(color2);
                GL11.glVertex3d(posX, 0.3 - (player.isSneaking() ? 0.23 : 0.0), posZ);
                for (int j = 0; j <= 180; ++j) {
                    final int color3 = this.getColor(j * 4, 0.2f).getRGB();
                    RenderUtil.color(color3);
                    GL11.glVertex3d(posX - Math.sin(j * MathHelper.PI2 / 90.0f) * radius, (double)(-(player.isSneaking() ? 0.23f : 0.0f)), posZ + Math.cos(j * MathHelper.PI2 / 90.0f) * radius);
                }
                GL11.glVertex3d(posX, 0.3 - (player.isSneaking() ? 0.23 : 0.0), posZ);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
        }
        RenderUtil.resetColor();
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    private Color getColor(final int index, final float alpha) {
        Color returnColor;
        if (this.colorMode.is("Custom")) {
            returnColor = (this.color.isRainbow() ? this.color.getRainbow().getColor(index) : this.color.getColor());
        }
        else {
            final Pair<Color, Color> colors = HUDMod.getClientColors();
            if (HUDMod.isRainbowTheme()) {
                returnColor = HUDMod.color1.getRainbow().getColor(index);
            }
            else {
                returnColor = ColorUtil.interpolateColorsBackAndForth(7, index, colors.getFirst(), colors.getSecond(), false);
            }
        }
        return ColorUtil.applyOpacity(returnColor, alpha);
    }
}
