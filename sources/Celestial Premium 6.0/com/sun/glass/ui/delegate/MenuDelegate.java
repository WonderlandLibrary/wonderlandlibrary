/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.delegate;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;

public interface MenuDelegate {
    public boolean createMenu(String var1, boolean var2);

    public boolean setTitle(String var1);

    public boolean setEnabled(boolean var1);

    public boolean setPixels(Pixels var1);

    public boolean insert(MenuDelegate var1, int var2);

    public boolean insert(MenuItemDelegate var1, int var2);

    public boolean remove(MenuDelegate var1, int var2);

    public boolean remove(MenuItemDelegate var1, int var2);
}

