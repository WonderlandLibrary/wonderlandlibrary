/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class InventoryEffectRenderer
extends GuiContainer {
    private boolean hasActivePotionEffects;

    @Override
    public void initGui() {
        super.initGui();
        this.updateActivePotionEffects();
    }

    public InventoryEffectRenderer(Container container) {
        super(container);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    protected void updateActivePotionEffects() {
        if (!Minecraft.thePlayer.getActivePotionEffects().isEmpty()) {
            this.guiLeft = 160 + (width - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        } else {
            this.guiLeft = (width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
    }

    private void drawActivePotionEffects() {
        int n = this.guiLeft - 124;
        int n2 = this.guiTop;
        int n3 = 166;
        Collection<PotionEffect> collection = Minecraft.thePlayer.getActivePotionEffects();
        if (!collection.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int n4 = 33;
            if (collection.size() > 5) {
                n4 = 132 / (collection.size() - 1);
            }
            for (PotionEffect potionEffect : Minecraft.thePlayer.getActivePotionEffects()) {
                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.mc.getTextureManager().bindTexture(inventoryBackground);
                this.drawTexturedModalRect(n, n2, 0, 166, 140, 32);
                if (potion.hasStatusIcon()) {
                    int n5 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(n + 6, n2 + 7, 0 + n5 % 8 * 18, 198 + n5 / 8 * 18, 18, 18);
                }
                String string = I18n.format(potion.getName(), new Object[0]);
                if (potionEffect.getAmplifier() == 1) {
                    string = String.valueOf(string) + " " + I18n.format("enchantment.level.2", new Object[0]);
                } else if (potionEffect.getAmplifier() == 2) {
                    string = String.valueOf(string) + " " + I18n.format("enchantment.level.3", new Object[0]);
                } else if (potionEffect.getAmplifier() == 3) {
                    string = String.valueOf(string) + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                this.fontRendererObj.drawStringWithShadow(string, n + 10 + 18, n2 + 6, 0xFFFFFF);
                String string2 = Potion.getDurationString(potionEffect);
                this.fontRendererObj.drawStringWithShadow(string2, n + 10 + 18, n2 + 6 + 10, 0x7F7F7F);
                n2 += n4;
            }
        }
    }
}

