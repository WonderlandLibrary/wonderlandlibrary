package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.resources.I18n;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiBeacon.CancelButton
extends GuiBeacon.Button {
    public GuiBeacon.CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_) {
        super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.access$000(), 112, 220);
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        GuiBeacon.access$100((GuiBeacon)GuiBeacon.this, (String)I18n.format((String)"gui.cancel", (Object[])new Object[0]), (int)mouseX, (int)mouseY);
    }
}
