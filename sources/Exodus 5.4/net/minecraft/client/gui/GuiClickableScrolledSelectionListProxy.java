/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;

public class GuiClickableScrolledSelectionListProxy
extends GuiSlot {
    private final RealmsClickableScrolledSelectionList field_178046_u;

    @Override
    protected int getScrollBarX() {
        return this.field_178046_u.getScrollbarPosition();
    }

    @Override
    protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
        this.field_178046_u.renderItem(n, n2, n3, n4, n5, n6);
    }

    public int func_178042_f() {
        return this.mouseY;
    }

    @Override
    protected boolean isSelected(int n) {
        return this.field_178046_u.isSelectedItem(n);
    }

    @Override
    protected void drawBackground() {
        this.field_178046_u.renderBackground();
    }

    @Override
    protected int getSize() {
        return this.field_178046_u.getItemCount();
    }

    public int func_178045_g() {
        return this.mouseX;
    }

    public void func_178043_a(int n, int n2, int n3, Tezzelator tezzelator) {
        this.field_178046_u.renderSelected(n, n2, n3, tezzelator);
    }

    @Override
    protected void elementClicked(int n, boolean bl, int n2, int n3) {
        this.field_178046_u.selectItem(n, bl, n2, n3);
    }

    @Override
    protected void drawSelectionBox(int n, int n2, int n3, int n4) {
        int n5 = this.getSize();
        int n6 = 0;
        while (n6 < n5) {
            int n7 = n2 + n6 * this.slotHeight + this.headerPadding;
            int n8 = this.slotHeight - 4;
            if (n7 > this.bottom || n7 + n8 < this.top) {
                this.func_178040_a(n6, n, n7);
            }
            if (this.showSelectionBox && this.isSelected(n6)) {
                this.func_178043_a(this.width, n7, n8, Tezzelator.instance);
            }
            this.drawSlot(n6, n, n7, n8, n3, n4);
            ++n6;
        }
    }

    @Override
    protected int getContentHeight() {
        return this.field_178046_u.getMaxPosition();
    }

    public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList realmsClickableScrolledSelectionList, int n, int n2, int n3, int n4, int n5) {
        super(Minecraft.getMinecraft(), n, n2, n3, n4, n5);
        this.field_178046_u = realmsClickableScrolledSelectionList;
    }

    public int func_178044_e() {
        return this.width;
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.scrollMultiplier > 0.0f && Mouse.getEventButtonState()) {
            this.field_178046_u.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }
}

