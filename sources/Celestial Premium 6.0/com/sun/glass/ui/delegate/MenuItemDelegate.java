/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.delegate;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;

public interface MenuItemDelegate {
    public boolean createMenuItem(String var1, MenuItem.Callback var2, int var3, int var4, Pixels var5, boolean var6, boolean var7);

    public boolean setTitle(String var1);

    public boolean setCallback(MenuItem.Callback var1);

    public boolean setShortcut(int var1, int var2);

    public boolean setPixels(Pixels var1);

    public boolean setEnabled(boolean var1);

    public boolean setChecked(boolean var1);
}

