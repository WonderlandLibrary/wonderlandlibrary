// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.potion.Potion;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class InventoryEffectRenderer extends GuiContainer
{
    protected boolean hasActivePotionEffects;
    
    public InventoryEffectRenderer(final Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.updateActivePotionEffects();
    }
    
    protected void updateActivePotionEffects() {
        final Minecraft mc = InventoryEffectRenderer.mc;
        if (Minecraft.player.getActivePotionEffects().isEmpty()) {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
        else {
            this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }
    
    private void drawActivePotionEffects() {
        final int i = this.guiLeft - 124;
        int j = this.guiTop;
        final int k = 166;
        final Minecraft mc = InventoryEffectRenderer.mc;
        final Collection<PotionEffect> collection = Minecraft.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int l = 33;
            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }
            for (final PotionEffect potioneffect : Ordering.natural().sortedCopy((Iterable)collection)) {
                final Potion potion = potioneffect.getPotion();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                InventoryEffectRenderer.mc.getTextureManager().bindTexture(InventoryEffectRenderer.INVENTORY_BACKGROUND);
                this.drawTexturedModalRect(i, j, 0, 166, 140, 32);
                if (potion.hasStatusIcon()) {
                    final int i2 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 6, j + 7, 0 + i2 % 8 * 18, 198 + i2 / 8 * 18, 18, 18);
                }
                String s1 = I18n.format(potion.getName(), new Object[0]);
                if (potioneffect.getAmplifier() == 1) {
                    s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 2) {
                    s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 3) {
                    s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                this.fontRenderer.drawStringWithShadow(s1, (float)(i + 10 + 18), (float)(j + 6), 16777215);
                final String s2 = Potion.getPotionDurationString(potioneffect, 1.0f);
                this.fontRenderer.drawStringWithShadow(s2, (float)(i + 10 + 18), (float)(j + 6 + 10), 8355711);
                j += l;
            }
        }
    }
}
