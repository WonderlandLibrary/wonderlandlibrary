package com.ohare.client.module.modules.visuals.hudcomps.comps;

import java.awt.Color;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class SpeedComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    public SpeedComp() {
        super("SpeedComp", 2, 14, 100, Fonts.hudfont.getHeight());
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        if (Minecraft.getMinecraft().thePlayer == null) return;
        Fonts.hudfont.drawStringWithShadow(String.valueOf(MathUtils.round(getSpeed() * 36.63, 2)) + " blocks/sec", getX(), getY()- ((getY() + getHeight() > scaledResolution.getScaledHeight() - 10 && Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) ? 12:0), hud.staticRainbow.isEnabled() ? color(3, 100) :  new Color(0x616161).getRGB());
        if (getWidth() != Fonts.hudfont.getStringWidth(String.valueOf(MathUtils.round(36.63 * getSpeed(), 2)) + " blocks/sec")) {
            setWidth(Fonts.hudfont.getStringWidth(String.valueOf(MathUtils.round(36.63 * getSpeed(), 2)) + " blocks/sec"));
        }
        if (getX() + getWidth() > sr.getScaledWidth()) setX(sr.getScaledWidth() - getWidth() - 2);
        if (getX() < 0) setX(0);
        if (getY() + getHeight() > sr.getScaledHeight()) setY(sr.getScaledHeight() - getHeight() - 2);
        if (getY() < 0) setY(0);
    }
    @Override
    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledWidth() < scaledResolution.getScaledWidth() && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(scaledResolution.getScaledWidth() - getWidth() - 2);
        }
        if (sr.getScaledHeight() < scaledResolution.getScaledHeight() && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(scaledResolution.getScaledHeight() - getHeight() - 2);
        }
        if (sr.getScaledHeight() != scaledResolution.getScaledHeight()) {
            sr = scaledResolution;
        }
    }
    @Override
    public void onFullScreen(float w, float h) {
        if (sr.getScaledWidth() < w && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(w - (sr.getScaledWidth() - getWidth()) - 2);
        }
        if (sr.getScaledHeight() < h && getY() > sr.getScaledHeight() - getHeight() - 20) {
            setY(h - (sr.getScaledHeight() - getHeight()) - 2);
        }
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }
    
    private float getSpeed() {
        return (float) Math.sqrt((Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX) + (Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ) + (Minecraft.getMinecraft().thePlayer.motionY > 0 ? Minecraft.getMinecraft().thePlayer.motionY * Minecraft.getMinecraft().thePlayer.motionY : 0));
    }
}