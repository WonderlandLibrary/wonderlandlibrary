package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.ColorValue;
import com.ohare.client.utils.value.impl.StringValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;

import java.awt.*;


/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class WatermarkComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();
    public StringValue name = new StringValue("Name","&coHare&7Ware");

    public WatermarkComp() {
        super("WatermarkComp", 2, 2, 100, Fonts.hudfont.getHeight());
        addValues(name);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        setWidth(Fonts.hudfont.getStringWidth(name.getValue().replaceAll("&","\247")));
        Fonts.hudfont.drawStringWithShadow(hud.staticRainbow.isEnabled() ? StringUtils.stripControlCodes(name.getValue().replaceAll("&","\247")) : name.getValue().replaceAll("&","\247"), getX(), getY(), hud.staticRainbow.isEnabled() ? color(2, 100) : -1);

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
}