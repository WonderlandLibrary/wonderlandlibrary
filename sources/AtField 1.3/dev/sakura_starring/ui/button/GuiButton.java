/*
 * Decompiled with CFR 0.152.
 */
package dev.sakura_starring.ui.button;

public interface GuiButton {
    public void draw(int var1, int var2, float var3);

    public boolean getEnabled();

    public void setEnabled(boolean var1);

    default public boolean isHover(int n, int n2, int n3, int n4, int n5, int n6) {
        return n >= n3 && n2 >= n4 && n < n3 + n5 && n2 < n4 + n6;
    }
}

