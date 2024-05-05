package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiKeyBindingList.CategoryEntry
implements GuiListExtended.IGuiListEntry {
    private final String labelText;
    private final int labelWidth;

    public GuiKeyBindingList.CategoryEntry(String p_i45028_2_) {
        this.labelText = I18n.format((String)p_i45028_2_, (Object[])new Object[0]);
        this.labelWidth = GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).fontRendererObj.getStringWidth(this.labelText);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        GuiScreen cfr_ignored_0 = GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).currentScreen;
        FontRenderer cfr_ignored_1 = GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).fontRendererObj;
        GuiKeyBindingList.access$100((GuiKeyBindingList)GuiKeyBindingList.this).fontRendererObj.drawString(this.labelText, GuiScreen.width / 2 - this.labelWidth / 2, y + slotHeight - FontRenderer.FONT_HEIGHT - 1, 0xFFFFFF);
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }
}
