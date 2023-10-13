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
    private static final String __OBFID = "CL_00001846";

    public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int p_i1085_2_, int p_i1085_3_, int p_i1085_4_, int p_i1085_5_, int p_i1085_6_) {
        super(Minecraft.getMinecraft(), p_i1085_2_, p_i1085_3_, p_i1085_4_, p_i1085_5_, p_i1085_6_);
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
    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_) {
        this.selectionList.renderItem(p_180791_1_, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_154338_k() {
        return this.width;
    }

    public int func_154339_l() {
        return this.mouseY;
    }

    public int func_154337_m() {
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
    public void func_178039_p() {
        super.func_178039_p();
    }
}

