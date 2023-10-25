/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.gui.GuiClickableScrolledSelectionListProxy;
import net.minecraft.realms.Tezzelator;

public class RealmsClickableScrolledSelectionList {
    private final GuiClickableScrolledSelectionListProxy proxy;

    public void itemClicked(int n, int n2, int n3, int n4, int n5) {
    }

    public int xm() {
        return this.proxy.func_178045_g();
    }

    public int getItemCount() {
        return 0;
    }

    public int getScroll() {
        return this.proxy.getAmountScrolled();
    }

    public int getMaxPosition() {
        return 0;
    }

    public int ym() {
        return this.proxy.func_178042_f();
    }

    public void customMouseEvent(int n, int n2, int n3, float f, int n4) {
    }

    public int width() {
        return this.proxy.func_178044_e();
    }

    protected void renderItem(int n, int n2, int n3, int n4, Tezzelator tezzelator, int n5, int n6) {
    }

    public RealmsClickableScrolledSelectionList(int n, int n2, int n3, int n4, int n5) {
        this.proxy = new GuiClickableScrolledSelectionListProxy(this, n, n2, n3, n4, n5);
    }

    public void setLeftPos(int n) {
        this.proxy.setSlotXBoundsFromLeft(n);
    }

    public void scroll(int n) {
        this.proxy.scrollBy(n);
    }

    public int getScrollbarPosition() {
        return this.proxy.func_178044_e() / 2 + 124;
    }

    public void renderItem(int n, int n2, int n3, int n4, int n5, int n6) {
        this.renderItem(n, n2, n3, n4, Tezzelator.instance, n5, n6);
    }

    public void mouseEvent() {
        this.proxy.handleMouseInput();
    }

    public void selectItem(int n, boolean bl, int n2, int n3) {
    }

    public boolean isSelectedItem(int n) {
        return false;
    }

    public void renderSelected(int n, int n2, int n3, Tezzelator tezzelator) {
    }

    protected void renderList(int n, int n2, int n3, int n4) {
    }

    public void render(int n, int n2, float f) {
        this.proxy.drawScreen(n, n2, f);
    }

    public void renderBackground() {
    }
}

