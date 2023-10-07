package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;

/**
 * @author Xen for OhareWare
 * @since 8/9/2019
 **/
public class PingComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    public PingComp() {
        super("PingComp", 2, 5, 100, Fonts.hudfont.getHeight());
    }

    @Override
    public void onRender(ScaledResolution sr) {
        super.onRender(sr);
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getGameProfile().getId());
        String ping = (networkPlayerInfo == null) ? "0" : (networkPlayerInfo.getResponseTime() + " ms");
        setWidth(Fonts.hudfont.getStringWidth(ping));
        Fonts.hudfont.drawStringWithShadow(ping, getX(), getY(),hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0x616161).getRGB());

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
