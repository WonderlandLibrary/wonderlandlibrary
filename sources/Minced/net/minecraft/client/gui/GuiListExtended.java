// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot
{
    public GuiListExtended(final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return false;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
        this.getListEntry(slotIndex).drawEntry(slotIndex, xPos, yPos, this.getListWidth(), heightIn, mouseXIn, mouseYIn, this.isMouseYWithinSlotBounds(mouseYIn) && this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == slotIndex, partialTicks);
    }
    
    @Override
    protected void updateItemPos(final int entryID, final int insideLeft, final int yPos, final float partialTicks) {
        this.getListEntry(entryID).updatePosition(entryID, insideLeft, yPos, partialTicks);
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseEvent) {
        if (this.isMouseYWithinSlotBounds(mouseY)) {
            final int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
            if (i >= 0) {
                final int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                final int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
                final int l = mouseX - j;
                final int i2 = mouseY - k;
                if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i2)) {
                    this.setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean mouseReleased(final int x, final int y, final int mouseEvent) {
        for (int i = 0; i < this.getSize(); ++i) {
            final int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            final int l = x - j;
            final int i2 = y - k;
            this.getListEntry(i).mouseReleased(i, x, y, mouseEvent, l, i2);
        }
        this.setEnabled(true);
        return false;
    }
    
    public abstract IGuiListEntry getListEntry(final int p0);
    
    public interface IGuiListEntry
    {
        void updatePosition(final int p0, final int p1, final int p2, final float p3);
        
        void drawEntry(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7, final float p8);
        
        boolean mousePressed(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
        
        void mouseReleased(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    }
}
