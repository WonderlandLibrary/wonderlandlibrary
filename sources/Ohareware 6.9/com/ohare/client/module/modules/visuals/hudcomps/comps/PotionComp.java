package com.ohare.client.module.modules.visuals.hudcomps.comps;

import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import com.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * made by Xen for OhareWare
 *
 * @since 6/10/2019
 **/
public class PotionComp extends HudComp {
    private static final ResourceLocation INVENTORY_RESOURCE = new ResourceLocation("textures/gui/container/inventory.png");
    private ScaledResolution sr = RenderUtil.getResolution();

    private BooleanValue icon = new BooleanValue("Icons",true);

    public PotionComp() {
        super("PotionComp", RenderUtil.getResolution().getScaledWidth() - 102, RenderUtil.getResolution().getScaledHeight() - 102, 100, 100);
        this.y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2 - this.getHeight();
        addValues(icon);
    }

    @Override
    public void onRender(ScaledResolution scaledResolution) {
        if (getY() < scaledResolution.getScaledHeight() / 2) renderPotionsUp(scaledResolution);
        else renderPotionsDown(scaledResolution);
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
    private void renderPotionsUp(ScaledResolution sr) {
        int height = 0;
        int maxWidth = 0;
        for (Potion potion : Potion.potionTypes) {
            if (potion != null) {
                PotionEffect eff = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion);

                if (Minecraft.getMinecraft().thePlayer.isPotionActive(potion)) {
                    String label = StatCollector.translateToLocal(potion.getName()) + this.getAmplifierNumerals(eff.getAmplifier());

                    int width = Fonts.hudfont.getStringWidth(label) + 24;

                    if(potion.hasStatusIcon()) {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                        Minecraft.getMinecraft().getTextureManager().bindTexture(INVENTORY_RESOURCE);

                        int index = potion.getStatusIconIndex();

                        RenderUtil.drawTexturedModalRect((int)this.getX(), (int)this.getY() + height + 2, index % 8 * 18,
                                198 + index / 8 * 18, 18, 18,-150);
                    }
                    Fonts.hudfont.drawStringWithShadow(label, getX() + ((getX() + getWidth() / 2 > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth(label))  : 0), (int) (this.getY() + height), potion.getLiquidColor());

                    if (width > maxWidth) {
                        maxWidth = width;
                    }


                    String duration = Potion.getDurationString(eff);
                    int width2 = Fonts.hudfont.getStringWidth(duration) + 24;

                    Fonts.hudfont.drawStringWithShadow(EnumChatFormatting.GRAY + duration, getX() + ((getX() + getWidth() / 2 > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth(duration)) : 0), (int) (this.getY() + height + 10), 0xFFFF0000);

                    if (width2 > maxWidth) {
                        maxWidth = width2;
                    }
                    height += 20;
                }
            }
        }
    }

    private void renderPotionsDown(ScaledResolution sr) {
        double height = getHeight() - 24;
        int maxWidth = 0;
        for (Potion potion : Potion.potionTypes) {
            if (potion != null) {
                PotionEffect eff = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion);

                if (Minecraft.getMinecraft().thePlayer.isPotionActive(potion)) {
                    String label = StatCollector.translateToLocal(potion.getName()) + this.getAmplifierNumerals(eff.getAmplifier());

                    int width = Fonts.hudfont.getStringWidth(label) + 24;

                    if(potion.hasStatusIcon() && icon.isEnabled()) {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                        Minecraft.getMinecraft().getTextureManager().bindTexture(INVENTORY_RESOURCE);

                        int index = potion.getStatusIconIndex();

                        RenderUtil.drawTexturedModalRect((int)this.getX(), (int)(this.getY() + height + 2), index % 8 * 18,
                                198 + index / 8 * 18, 18, 18,-150);
                    }
                    double x = getX() + ((getX() + getWidth() / 2 > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth(label)) : 0);

                    Fonts.hudfont.drawStringWithShadow(label, icon.isEnabled() ? x + 20 : x, (int) (this.getY() + height + 6)- ((getY() + getHeight() > sr.getScaledHeight() - 10 && Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) ? 12:0), potion.getLiquidColor());

                    if (width > maxWidth) {
                        maxWidth = width;
                    }


                    String duration = Potion.getDurationString(eff);
                    int width2 = Fonts.hudfont.getStringWidth(duration) + 24;
                    double x2 = getX() + ((getX() + getWidth() / 2 > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) ? (getWidth() - Fonts.hudfont.getStringWidth(duration)) : 0);
                    Fonts.hudfont.drawStringWithShadow(EnumChatFormatting.GRAY + duration, icon.isEnabled() ? x + 20 : x, (int) (this.getY() + height + 16)- ((getY() + getHeight() > sr.getScaledHeight() - 10 && Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) ? 12:0), 0xFFFF0000);

                    if (width2 > maxWidth) {
                        maxWidth = width2;
                    }

                    height -= 20;
                }
            }
        }
    }

    private String getAmplifierNumerals(int amplifier) {
        switch (amplifier) {
            case 0:
                return " I";
            case 1:
                return " II";
            case 2:
                return " III";
            case 3:
                return " IV";
            case 4:
                return " V";
            case 5:
                return " VI";
            case 6:
                return " VII";
            case 7:
                return " VIII";
            case 8:
                return " IX";
            case 9:
                return " X";
            default:
                if (amplifier < 1) {
                    return "";
                }
                return " " + amplifier + 1;
        }
    }
}