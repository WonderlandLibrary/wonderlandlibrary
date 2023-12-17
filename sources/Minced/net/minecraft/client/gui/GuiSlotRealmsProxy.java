// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot
{
    private final RealmsScrolledSelectionList selectionList;
    
    public GuiSlotRealmsProxy(final RealmsScrolledSelectionList selectionListIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.selectionList = selectionListIn;
    }
    
    @Override
    protected int getSize() {
        return this.selectionList.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.selectionList.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.selectionList.renderBackground();
    }
    
    @Override
    protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
        this.selectionList.renderItem(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getMouseY() {
        return this.mouseY;
    }
    
    public int getMouseX() {
        return this.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.selectionList.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.selectionList.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }
}
