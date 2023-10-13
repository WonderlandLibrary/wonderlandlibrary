/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import java.util.Collection;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class InventoryEffectRenderer
extends GuiContainer {
    private boolean hasActivePotionEffects;
    private static final String __OBFID = "CL_00000755";

    public InventoryEffectRenderer(Container p_i1089_1_) {
        super(p_i1089_1_);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.func_175378_g();
    }

    protected void func_175378_g() {
        if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
            this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        } else {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    private void drawActivePotionEffects() {
        int var1 = this.guiLeft - 124;
        int var2 = this.guiTop;
        boolean var3 = true;
        Collection var4 = this.mc.thePlayer.getActivePotionEffects();
        if (!var4.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int var5 = 33;
            if (var4.size() > 5) {
                var5 = 132 / (var4.size() - 1);
            }
            for (PotionEffect var7 : this.mc.thePlayer.getActivePotionEffects()) {
                Potion var8 = Potion.potionTypes[var7.getPotionID()];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.mc.getTextureManager().bindTexture(inventoryBackground);
                this.drawTexturedModalRect(var1, var2, 0, 166, 140, 32);
                if (var8.hasStatusIcon()) {
                    int var9 = var8.getStatusIconIndex();
                    this.drawTexturedModalRect(var1 + 6, var2 + 7, 0 + var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
                }
                String var11 = I18n.format(var8.getName(), new Object[0]);
                if (var7.getAmplifier() == 1) {
                    var11 = String.valueOf(var11) + " " + I18n.format("enchantment.level.2", new Object[0]);
                } else if (var7.getAmplifier() == 2) {
                    var11 = String.valueOf(var11) + " " + I18n.format("enchantment.level.3", new Object[0]);
                } else if (var7.getAmplifier() == 3) {
                    var11 = String.valueOf(var11) + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                this.fontRendererObj.drawStringWithShadow(var11, var1 + 10 + 18, var2 + 6, 0xFFFFFF);
                String var10 = Potion.getDurationString(var7);
                this.fontRendererObj.drawStringWithShadow(var10, var1 + 10 + 18, var2 + 6 + 10, 0x7F7F7F);
                var2 += var5;
            }
        }
    }
}

