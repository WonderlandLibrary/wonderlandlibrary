/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.gui.GuiSlotRealmsProxy;
import net.minecraft.realms.Tezzelator;

public class RealmsScrolledSelectionList {
    private final GuiSlotRealmsProxy proxy;

    public int xm() {
        return this.proxy.func_154337_m();
    }

    public int getScroll() {
        return this.proxy.getAmountScrolled();
    }

    public void scroll(int n) {
        this.proxy.scrollBy(n);
    }

    public boolean isSelectedItem(int n) {
        return false;
    }

    public void render(int n, int n2, float f) {
        this.proxy.drawScreen(n, n2, f);
    }

    protected void renderItem(int n, int n2, int n3, int n4, Tezzelator tezzelator, int n5, int n6) {
    }

    public int width() {
        return this.proxy.func_154338_k();
    }

    protected void renderList(int n, int n2, int n3, int n4) {
    }

    public RealmsScrolledSelectionList(int n, int n2, int n3, int n4, int n5) {
        this.proxy = new GuiSlotRealmsProxy(this, n, n2, n3, n4, n5);
    }

    public void renderItem(int n, int n2, int n3, int n4, int n5, int n6) {
        this.renderItem(n, n2, n3, n4, Tezzelator.instance, n5, n6);
    }

    public int ym() {
        return this.proxy.func_154339_l();
    }

    public void renderBackground() {
    }

    public void selectItem(int n, boolean bl, int n2, int n3) {
    }

    public void mouseEvent() {
        this.proxy.handleMouseInput();
    }

    public int getMaxPosition() {
        return 0;
    }

    public int getScrollbarPosition() {
        return this.proxy.func_154338_k() / 2 + 124;
    }

    public int getItemCount() {
        return 0;
    }
}

