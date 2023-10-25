/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public abstract class GuiListExtended
extends GuiSlot {
    @Override
    protected void drawBackground() {
    }

    public boolean mouseReleased(int n, int n2, int n3) {
        int n4 = 0;
        while (n4 < this.getSize()) {
            int n5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int n6 = this.top + 4 - this.getAmountScrolled() + n4 * this.slotHeight + this.headerPadding;
            int n7 = n - n5;
            int n8 = n2 - n6;
            this.getListEntry(n4).mouseReleased(n4, n, n2, n3, n7, n8);
            ++n4;
        }
        this.setEnabled(true);
        return false;
    }

    @Override
    protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
        this.getListEntry(n).drawEntry(n, n2, n3, this.getListWidth(), n4, n5, n6, this.getSlotIndexFromScreenCoords(n5, n6) == n);
    }

    public GuiListExtended(Minecraft minecraft, int n, int n2, int n3, int n4, int n5) {
        super(minecraft, n, n2, n3, n4, n5);
    }

    @Override
    protected void func_178040_a(int n, int n2, int n3) {
        this.getListEntry(n).setSelected(n, n2, n3);
    }

    @Override
    protected boolean isSelected(int n) {
        return false;
    }

    public abstract IGuiListEntry getListEntry(int var1);

    public boolean mouseClicked(int n, int n2, int n3) {
        int n4;
        if (this.isMouseYWithinSlotBounds(n2) && (n4 = this.getSlotIndexFromScreenCoords(n, n2)) >= 0) {
            int n5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int n6 = this.top + 4 - this.getAmountScrolled() + n4 * this.slotHeight + this.headerPadding;
            int n7 = n - n5;
            int n8 = n2 - n6;
            if (this.getListEntry(n4).mousePressed(n4, n, n2, n3, n7, n8)) {
                this.setEnabled(false);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void elementClicked(int n, boolean bl, int n2, int n3) {
    }

    public static interface IGuiListEntry {
        public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8);

        public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

        public void setSelected(int var1, int var2, int var3);

        public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
    }
}

