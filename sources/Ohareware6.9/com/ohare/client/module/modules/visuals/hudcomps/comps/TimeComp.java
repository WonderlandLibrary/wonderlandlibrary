package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class TimeComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    private StringValue style = new StringValue("Style","&7(%time)");

    public TimeComp() {
        super("TimeComp", 2, 16, 100, Fonts.hudfont.getHeight());
        addValues(style);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String date = dateFormat.format(new Date());
        String styleString = style.getValue().replaceAll("&","\247").replaceAll("%time",date);
        Fonts.hudfont.drawStringWithShadow(styleString, getX(), getY(), hud.staticRainbow.isEnabled() ? color(2, 100) : -1);

        setWidth(Fonts.hudfont.getStringWidth(styleString) + 2);

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
