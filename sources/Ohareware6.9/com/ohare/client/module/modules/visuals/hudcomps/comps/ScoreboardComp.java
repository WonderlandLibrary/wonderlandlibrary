package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 7/14/2019
 **/
public class ScoreboardComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();
    public ScoreboardComp() {
        super("ScoreboardComp", RenderUtil.getResolution().getScaledWidth() - 62,RenderUtil.getResolution().getScaledHeight() / 2 + 60,80,100);
    }
    @Override
    public void onRender(ScaledResolution scaledResolution) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo || Minecraft.getMinecraft().thePlayer == null ) return;
        if (getX() + getWidth() > scaledResolution.getScaledWidth()) setX(scaledResolution.getScaledWidth() - getWidth() - 2);
        if (getX() < 0) setX(0);
        if (getY() + getHeight() > scaledResolution.getScaledHeight()) setY(scaledResolution.getScaledHeight() - getHeight() - 2);
        if (getY() < 0) setY(0);
    }
    @Override
    public void onResize(ScaledResolution scaledResolution) {
        if (sr.getScaledWidth() < scaledResolution.getScaledWidth() && getX() > sr.getScaledWidth() - getWidth() - 20) {
            setX(scaledResolution.getScaledWidth() - getWidth() - 2);
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
        if (sr.getScaledHeight() != new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            sr = new ScaledResolution(Minecraft.getMinecraft());
        }
    }
}