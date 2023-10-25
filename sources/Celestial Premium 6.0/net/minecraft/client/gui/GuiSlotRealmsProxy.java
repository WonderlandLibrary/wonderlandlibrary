/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy
extends GuiSlot {
    private final RealmsScrolledSelectionList selectionList;

    public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.selectionList = selectionListIn;
    }

    @Override
    protected int getSize() {
        return this.selectionList.getItemCount();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return this.selectionList.isSelectedItem(slotIndex);
    }

    @Override
    protected void drawBackground() {
        this.selectionList.renderBackground();
    }

    @Override
    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
        this.selectionList.renderItem(p_192637_1_, p_192637_2_, p_192637_3_, p_192637_4_, p_192637_5_, p_192637_6_);
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

