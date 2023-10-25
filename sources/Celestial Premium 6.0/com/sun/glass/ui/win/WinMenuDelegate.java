/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.glass.ui.win.WinMenuImpl;
import com.sun.glass.ui.win.WinMenuItemDelegate;

final class WinMenuDelegate
extends WinMenuImpl
implements MenuDelegate {
    private final Menu owner;
    private WinMenuImpl parent = null;

    public WinMenuDelegate(Menu menu) {
        this.owner = menu;
    }

    public Menu getOwner() {
        return this.owner;
    }

    @Override
    public boolean createMenu(String string, boolean bl) {
        return this.create();
    }

    public void dispose() {
        this.destroy();
    }

    @Override
    public boolean setTitle(String string) {
        if (this.parent != null) {
            return this.parent.setSubmenuTitle(this, string);
        }
        return true;
    }

    @Override
    public boolean setEnabled(boolean bl) {
        if (this.parent != null) {
            return this.parent.enableSubmenu(this, bl);
        }
        return true;
    }

    @Override
    public boolean setPixels(Pixels pixels) {
        return false;
    }

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n) {
        return this.insertSubmenu((WinMenuDelegate)menuDelegate, n);
    }

    @Override
    public boolean insert(MenuItemDelegate menuItemDelegate, int n) {
        return this.insertItem((WinMenuItemDelegate)menuItemDelegate, n);
    }

    @Override
    public boolean remove(MenuDelegate menuDelegate, int n) {
        return this.removeMenu((WinMenuDelegate)menuDelegate, n);
    }

    @Override
    public boolean remove(MenuItemDelegate menuItemDelegate, int n) {
        return this.removeItem((WinMenuItemDelegate)menuItemDelegate, n);
    }

    WinMenuImpl getParent() {
        return this.parent;
    }

    void setParent(WinMenuImpl winMenuImpl) {
        this.parent = winMenuImpl;
    }
}

