package com.ohare.client.module.modules.visuals.hudcomps.comps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.ohare.client.Client;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class ArrayListComp extends HudComp {
    private ScaledResolution sr = RenderUtil.getResolution();

    public ArrayListComp() {
        super("ArrayListComp", RenderUtil.getResolution().getScaledWidth() - 102, 3, 100, 12);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        double y = (getY() + getHeight() / 2) > scaledResolution.getScaledHeight() / 2 ? getY() + getHeight() - Fonts.hudfont.getHeight()- ((getY() + getHeight() > scaledResolution.getScaledHeight() - 10 && Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) ? 12:0):getY();
        ArrayList<Module> mods = new ArrayList(Client.INSTANCE.getModuleManager().getModuleMap().values());
        mods.sort(Comparator.comparingDouble(m -> -Fonts.hudfont.getStringWidth((Objects.nonNull(m.getRenderlabel()) ? m.getRenderlabel():m.getLabel()) + (m.getSuffix() != null ? " " + ChatFormatting.GRAY + m.getSuffix() : ""))));
        if ((getY() + getHeight() / 2) > scaledResolution.getScaledHeight() / 2 ) {
            for (Module mod : mods) {
                if (mod.isEnabled() && !mod.isHidden()) {
                    Fonts.hudfont.drawStringWithShadow((Objects.nonNull(mod.getRenderlabel())? mod.getRenderlabel():mod.getLabel()) + (mod.getSuffix() != null ? " " + ChatFormatting.GRAY + mod.getSuffix() : ""), getX() + ((getX() + getWidth() / 2 > scaledResolution.getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth((Objects.nonNull(mod.getRenderlabel()) ? mod.getRenderlabel():mod.getLabel()) + (mod.getSuffix() != null ? " " + ChatFormatting.GRAY + mod.getSuffix() : ""))) : 0),  y, hud.staticRainbow.isEnabled() ? color(5, mods.indexOf(mod) + mods.indexOf(mod) + 2) : mod.getColor());
                    y -= Fonts.hudfont.getHeight() + 2;
                }
            }
        } else {
            for (Module mod : mods) {
                if (mod.isEnabled() && !mod.isHidden()) {
                    Fonts.hudfont.drawStringWithShadow((Objects.nonNull(mod.getRenderlabel()) ? mod.getRenderlabel():mod.getLabel()) + (mod.getSuffix() != null ? " " + ChatFormatting.GRAY + mod.getSuffix() : ""), getX() + ((getX() + getWidth() / 2 > scaledResolution.getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth((Objects.nonNull(mod.getRenderlabel()) ? mod.getRenderlabel():mod.getLabel()) + (mod.getSuffix() != null ? " " + ChatFormatting.GRAY + mod.getSuffix() : ""))) : 0), y, hud.staticRainbow.isEnabled() ? color(5, mods.indexOf(mod) + mods.indexOf(mod) + 2) : mod.getColor());
                    y += Fonts.hudfont.getHeight() + 2;
                }
            }
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

}