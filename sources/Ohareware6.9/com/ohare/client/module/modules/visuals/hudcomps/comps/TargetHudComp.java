package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.Client;
import com.ohare.client.module.modules.combat.KillAura;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;


/**
 * made by oHare for oHareWare
 *
 * @since 7/9/2019
 **/

public class TargetHudComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    public TargetHudComp() {
        super("TargetHudComp", 415, 320, 140, 45);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule("killaura");
        if (Minecraft.getMinecraft().thePlayer != null && killAura.target instanceof EntityPlayer) {
            NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(killAura.target.getUniqueID());

            final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");

            final String playerName = "Name: " + StringUtils.stripControlCodes(killAura.target.getName());
            RenderUtil.drawBorderedRect(x, y, width, height, 0.5, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 90).getRGB());
            RenderUtil.drawRect(x, y, 45, 45, new Color(0, 0, 0).getRGB());
            Fonts.clickfont.drawStringWithShadow(playerName, x + 46.5, y + 4, -1);
            Fonts.clickfont.drawStringWithShadow("Distance: " + MathUtils.round(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(killAura.target), 2), x + 46.5, y + 12, -1);
            Fonts.clickfont.drawStringWithShadow(ping, x + 46.5, y + 28, new Color(0x5D5B5C).getRGB());
            Fonts.clickfont.drawStringWithShadow("Health: " + MathUtils.round(killAura.target.getHealth() / 2, 2), x + 46.5, y + 20, getHealthColor(killAura.target));
            drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) killAura.target);
            RenderUtil.drawBorderedRect(x + 46, y + height - 10, 92, 8, 0.5, new Color(0).getRGB(), new Color(35, 35, 35).getRGB());
            double inc = 91 / killAura.target.getMaxHealth();
            double end = inc * (killAura.target.getHealth() > killAura.target.getMaxHealth() ? killAura.target.getMaxHealth() : killAura.target.getHealth());
            RenderUtil.drawRect(x + 46.5, y + height - 9.5, end, 7, getHealthColor(killAura.target));
        }
    }

    @Override
    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight()) {
            sr = scaledResolution;
        }
    }

    @Override
    public void onFullScreen(float w, float h) {
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception e) {
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }
}