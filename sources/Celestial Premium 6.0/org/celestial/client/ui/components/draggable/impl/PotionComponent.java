/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import optifine.CustomColors;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class PotionComponent
extends DraggableModule {
    protected Gui gui = new Gui();

    public PotionComponent() {
        super("PotionComponent", 2, 77);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 150;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        int xOff = 21;
        int yOff = 14;
        int counter = 16;
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
        if (collection.isEmpty()) {
            this.drag.setCanRender(false);
        } else {
            this.drag.setCanRender(true);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int listOffset = 23;
            if (collection.size() > 5) {
                listOffset = 132 / (collection.size() - 1);
            }
            ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>(this.mc.player.getActivePotionEffects());
            potions.sort(Comparator.comparingDouble(effect -> !ClientFont.minecraftFont.getCurrentValue() ? (double)ClientHelper.getFontRender().getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()) : (double)this.mc.fontRendererObj.getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName())));
            for (PotionEffect potion : potions) {
                Potion effect2 = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                assert (effect2 != null);
                if (effect2.hasStatusIcon() && HUD.potionIcons.getCurrentValue()) {
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                    int statusIconIndex = effect2.getStatusIconIndex();
                    this.gui.drawTexturedModalRect((float)(this.getX() + xOff - 20), (float)(this.getY() + counter - yOff), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                }
                String level = I18n.format(effect2.getName(), new Object[0]);
                if (potion.getAmplifier() == 1) {
                    level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
                } else if (potion.getAmplifier() == 2) {
                    level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
                } else if (potion.getAmplifier() == 3) {
                    level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                int getPotionColor = -1;
                if (potion.getDuration() < 200) {
                    getPotionColor = new Color(215, 59, 59).getRGB();
                } else if (potion.getDuration() < 400) {
                    getPotionColor = new Color(231, 143, 32).getRGB();
                } else if (potion.getDuration() > 400) {
                    getPotionColor = new Color(172, 171, 171).getRGB();
                }
                String durationString = Potion.getDurationString(potion);
                if (ClientFont.minecraftFont.getCurrentValue()) {
                    this.mc.fontRendererObj.drawStringWithShadow(level, this.getX() + xOff, this.getY() + counter - yOff, -1);
                    this.mc.fontRendererObj.drawStringWithShadow(durationString, this.getX() + xOff, this.getY() + counter + 10 - yOff, HUD.potionTimeColor.getCurrentValue() ? getPotionColor : -1);
                } else {
                    ClientHelper.getFontRender().drawStringWithShadow(level, this.getX() + xOff, this.getY() + counter - yOff, -1);
                    ClientHelper.getFontRender().drawStringWithShadow(durationString, this.getX() + xOff, this.getY() + counter + 10 - yOff, HUD.potionTimeColor.getCurrentValue() ? getPotionColor : -1);
                }
                counter += listOffset;
            }
            super.draw();
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.potion.getCurrentValue()) {
            int xOff = 21;
            int yOff = 14;
            int counter = 16;
            Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
            if (collection.isEmpty()) {
                this.drag.setCanRender(false);
            } else {
                this.drag.setCanRender(true);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableLighting();
                int listOffset = 23;
                if (collection.size() > 5) {
                    listOffset = 132 / (collection.size() - 1);
                }
                ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>(this.mc.player.getActivePotionEffects());
                potions.sort(Comparator.comparingDouble(effect -> !ClientFont.minecraftFont.getCurrentValue() ? (double)ClientHelper.getFontRender().getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()) : (double)this.mc.fontRendererObj.getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName())));
                for (PotionEffect potion : potions) {
                    Potion effect2 = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    assert (effect2 != null);
                    if (effect2.hasStatusIcon() && HUD.potionIcons.getCurrentValue()) {
                        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                        int statusIconIndex = effect2.getStatusIconIndex();
                        this.gui.drawTexturedModalRect((float)(this.getX() + xOff - 20), (float)(this.getY() + counter - yOff), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                    }
                    String level = I18n.format(effect2.getName(), new Object[0]);
                    if (potion.getAmplifier() == 1) {
                        level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
                    } else if (potion.getAmplifier() == 2) {
                        level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
                    } else if (potion.getAmplifier() == 3) {
                        level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
                    }
                    int getPotionColor = -1;
                    if (potion.getDuration() < 200) {
                        getPotionColor = new Color(215, 59, 59).getRGB();
                    } else if (potion.getDuration() < 400) {
                        getPotionColor = new Color(231, 143, 32).getRGB();
                    } else if (potion.getDuration() > 400) {
                        getPotionColor = new Color(172, 171, 171).getRGB();
                    }
                    String durationString = Potion.getDurationString(potion);
                    if (ClientFont.minecraftFont.getCurrentValue()) {
                        this.mc.fontRendererObj.drawStringWithShadow(level, this.getX() + xOff, this.getY() + counter - yOff, -1);
                        this.mc.fontRendererObj.drawStringWithShadow(durationString, this.getX() + xOff, this.getY() + counter + 10 - yOff, HUD.potionTimeColor.getCurrentValue() ? getPotionColor : -1);
                    } else {
                        ClientHelper.getFontRender().drawStringWithShadow(level, this.getX() + xOff, this.getY() + counter - yOff, -1);
                        ClientHelper.getFontRender().drawStringWithShadow(durationString, this.getX() + xOff, this.getY() + counter + 10 - yOff, HUD.potionTimeColor.getCurrentValue() ? getPotionColor : -1);
                    }
                    counter += listOffset;
                }
            }
            super.draw();
        }
    }
}

