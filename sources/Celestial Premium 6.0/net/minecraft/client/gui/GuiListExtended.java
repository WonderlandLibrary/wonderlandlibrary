/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public abstract class GuiListExtended
extends GuiSlot {
    public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
        this.getListEntry(p_192637_1_).func_192634_a(p_192637_1_, p_192637_2_, p_192637_3_, this.getListWidth(), p_192637_4_, p_192637_5_, p_192637_6_, this.isMouseYWithinSlotBounds(p_192637_6_) && this.getSlotIndexFromScreenCoords(p_192637_5_, p_192637_6_) == p_192637_1_, p_192637_7_);
    }

    @Override
    protected void func_192639_a(int p_192639_1_, int p_192639_2_, int p_192639_3_, float p_192639_4_) {
        this.getListEntry(p_192639_1_).func_192633_a(p_192639_1_, p_192639_2_, p_192639_3_, p_192639_4_);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        int i;
        if (this.isMouseYWithinSlotBounds(mouseY) && (i = this.getSlotIndexFromScreenCoords(mouseX, mouseY)) >= 0) {
            int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            int l = mouseX - j;
            int i1 = mouseY - k;
            if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
                this.setEnabled(false);
                return true;
            }
        }
        return false;
    }

    public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
        for (int i = 0; i < this.getSize(); ++i) {
            int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            int l = p_148181_1_ - j;
            int i1 = p_148181_2_ - k;
            this.getListEntry(i).mouseReleased(i, p_148181_1_, p_148181_2_, p_148181_3_, l, i1);
        }
        this.setEnabled(true);
        return false;
    }

    public abstract IGuiListEntry getListEntry(int var1);

    public static interface IGuiListEntry {
        public void func_192633_a(int var1, int var2, int var3, float var4);

        public void func_192634_a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9);

        public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

        public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
    }
}

