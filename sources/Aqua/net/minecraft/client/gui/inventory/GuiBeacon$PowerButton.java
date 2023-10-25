package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiBeacon.PowerButton
extends GuiBeacon.Button {
    private final int field_146149_p;
    private final int field_146148_q;

    public GuiBeacon.PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_) {
        super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
        this.field_146149_p = p_i1076_5_;
        this.field_146148_q = p_i1076_6_;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format((String)Potion.potionTypes[this.field_146149_p].getName(), (Object[])new Object[0]);
        if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id) {
            s = s + " II";
        }
        GuiBeacon.access$300((GuiBeacon)GuiBeacon.this, (String)s, (int)mouseX, (int)mouseY);
    }
}
