/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.delegate;

import com.sun.glass.ui.delegate.MenuDelegate;

public interface MenuBarDelegate {
    public boolean createMenuBar();

    public boolean insert(MenuDelegate var1, int var2);

    public boolean remove(MenuDelegate var1, int var2);

    public long getNativeMenu();
}

