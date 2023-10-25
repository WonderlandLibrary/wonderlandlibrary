package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiSnooper;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiSnooper.List
extends GuiSlot {
    public GuiSnooper.List() {
        FontRenderer cfr_ignored_0 = GuiSnooper.this.fontRendererObj;
        super(GuiSnooper.this.mc, GuiSnooper.width, GuiSnooper.height, 80, GuiSnooper.height - 40, FontRenderer.FONT_HEIGHT + 1);
    }

    protected int getSize() {
        return GuiSnooper.access$000((GuiSnooper)GuiSnooper.this).size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.access$000((GuiSnooper)GuiSnooper.this).get(entryID), 10, p_180791_3_, 0xFFFFFF);
        GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.access$100((GuiSnooper)GuiSnooper.this).get(entryID), 230, p_180791_3_, 0xFFFFFF);
    }

    protected int getScrollBarX() {
        return this.width - 10;
    }
}
