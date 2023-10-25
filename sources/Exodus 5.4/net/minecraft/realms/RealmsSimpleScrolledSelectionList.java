/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.gui.GuiSimpleScrolledSelectionListProxy;
import net.minecraft.realms.Tezzelator;

public class RealmsSimpleScrolledSelectionList {
    private final GuiSimpleScrolledSelectionListProxy proxy;

    public int getItemCount() {
        return 0;
    }

    public void renderItem(int n, int n2, int n3, int n4, int n5, int n6) {
        this.renderItem(n, n2, n3, n4, Tezzelator.instance, n5, n6);
    }

    public void renderBackground() {
    }

    protected void renderItem(int n, int n2, int n3, int n4, Tezzelator tezzelator, int n5, int n6) {
    }

    protected void renderList(int n, int n2, int n3, int n4) {
    }

    public int getScroll() {
        return this.proxy.getAmountScrolled();
    }

    public int getMaxPosition() {
        return 0;
    }

    public void render(int n, int n2, float f) {
        this.proxy.drawScreen(n, n2, f);
    }

    public void scroll(int n) {
        this.proxy.scrollBy(n);
    }

    public void selectItem(int n, boolean bl, int n2, int n3) {
    }

    public int width() {
        return this.proxy.getWidth();
    }

    public int xm() {
        return this.proxy.getMouseX();
    }

    public int getScrollbarPosition() {
        return this.proxy.getWidth() / 2 + 124;
    }

    public boolean isSelectedItem(int n) {
        return false;
    }

    public RealmsSimpleScrolledSelectionList(int n, int n2, int n3, int n4, int n5) {
        this.proxy = new GuiSimpleScrolledSelectionListProxy(this, n, n2, n3, n4, n5);
    }

    public void mouseEvent() {
        this.proxy.handleMouseInput();
    }

    public int ym() {
        return this.proxy.getMouseY();
    }
}

