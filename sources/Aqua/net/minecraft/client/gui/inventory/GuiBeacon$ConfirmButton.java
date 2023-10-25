package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.resources.I18n;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiBeacon.ConfirmButton
extends GuiBeacon.Button {
    public GuiBeacon.ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_) {
        super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.access$000(), 90, 220);
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        GuiBeacon.access$200((GuiBeacon)GuiBeacon.this, (String)I18n.format((String)"gui.done", (Object[])new Object[0]), (int)mouseX, (int)mouseY);
    }
}
