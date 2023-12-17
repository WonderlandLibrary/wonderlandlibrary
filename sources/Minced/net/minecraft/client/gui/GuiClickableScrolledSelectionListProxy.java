// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;

public class GuiClickableScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsClickableScrolledSelectionList proxy;
    
    public GuiClickableScrolledSelectionListProxy(final RealmsClickableScrolledSelectionList selectionList, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.proxy = selectionList;
    }
    
    @Override
    protected int getSize() {
        return this.proxy.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.proxy.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.proxy.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.proxy.renderBackground();
    }
    
    @Override
    protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
        this.proxy.renderItem(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }
    
    public int width() {
        return this.width;
    }
    
    public int mouseY() {
        return this.mouseY;
    }
    
    public int mouseX() {
        return this.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.proxy.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.proxy.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.scrollMultiplier > 0.0f && Mouse.getEventButtonState()) {
            this.proxy.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }
    
    public void renderSelected(final int p_178043_1_, final int p_178043_2_, final int p_178043_3_, final Tezzelator p_178043_4_) {
        this.proxy.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }
    
    @Override
    protected void drawSelectionBox(final int insideLeft, final int insideTop, final int mouseXIn, final int mouseYIn, final float partialTicks) {
        for (int i = this.getSize(), j = 0; j < i; ++j) {
            final int k = insideTop + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.updateItemPos(j, insideLeft, k, partialTicks);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                this.renderSelected(this.width, k, l, Tezzelator.instance);
            }
            this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
        }
    }
}
