/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy
extends GuiSlot {
    private final RealmsScrolledSelectionList selectionList;

    @Override
    protected boolean isSelected(int n) {
        return this.selectionList.isSelectedItem(n);
    }

    public GuiSlotRealmsProxy(RealmsScrolledSelectionList realmsScrolledSelectionList, int n, int n2, int n3, int n4, int n5) {
        super(Minecraft.getMinecraft(), n, n2, n3, n4, n5);
        this.selectionList = realmsScrolledSelectionList;
    }

    public int func_154338_k() {
        return this.width;
    }

    @Override
    protected void elementClicked(int n, boolean bl, int n2, int n3) {
        this.selectionList.selectItem(n, bl, n2, n3);
    }

    @Override
    protected int getSize() {
        return this.selectionList.getItemCount();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }

    @Override
    protected void drawBackground() {
        this.selectionList.renderBackground();
    }

    public int func_154339_l() {
        return this.mouseY;
    }

    @Override
    protected int getScrollBarX() {
        return this.selectionList.getScrollbarPosition();
    }

    public int func_154337_m() {
        return this.mouseX;
    }

    @Override
    protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
        this.selectionList.renderItem(n, n2, n3, n4, n5, n6);
    }

    @Override
    protected int getContentHeight() {
        return this.selectionList.getMaxPosition();
    }
}

