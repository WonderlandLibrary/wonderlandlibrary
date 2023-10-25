/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.win.WinMenuDelegate;
import com.sun.glass.ui.win.WinMenuImpl;

final class WinMenuBarDelegate
extends WinMenuImpl
implements MenuBarDelegate {
    private final MenuBar owner;

    WinMenuBarDelegate(MenuBar menuBar) {
        this.owner = menuBar;
    }

    public MenuBar getOwner() {
        return this.owner;
    }

    @Override
    public boolean createMenuBar() {
        return this.create();
    }

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n) {
        WinMenuDelegate winMenuDelegate = (WinMenuDelegate)menuDelegate;
        if (winMenuDelegate.getParent() != null) {
            // empty if block
        }
        return this.insertSubmenu(winMenuDelegate, n);
    }

    @Override
    public boolean remove(MenuDelegate menuDelegate, int n) {
        WinMenuDelegate winMenuDelegate = (WinMenuDelegate)menuDelegate;
        return this.removeMenu(winMenuDelegate, n);
    }

    @Override
    public long getNativeMenu() {
        return this.getHMENU();
    }
}

