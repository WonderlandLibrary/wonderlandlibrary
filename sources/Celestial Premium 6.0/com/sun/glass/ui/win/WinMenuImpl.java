/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.win.WinMenuDelegate;
import com.sun.glass.ui.win.WinMenuItemDelegate;

class WinMenuImpl {
    private long ptr = 0L;

    private static native void _initIDs();

    WinMenuImpl() {
    }

    long getHMENU() {
        return this.ptr;
    }

    boolean create() {
        this.ptr = this._create();
        return this.ptr != 0L;
    }

    void destroy() {
        if (this.ptr != 0L) {
            this._destroy(this.ptr);
            this.ptr = 0L;
        }
    }

    boolean insertSubmenu(WinMenuDelegate winMenuDelegate, int n) {
        winMenuDelegate.setParent(this);
        if (!this._insertSubmenu(this.ptr, n, winMenuDelegate.getHMENU(), winMenuDelegate.getOwner().getTitle(), winMenuDelegate.getOwner().isEnabled())) {
            winMenuDelegate.setParent(null);
            return false;
        }
        return true;
    }

    boolean insertItem(WinMenuItemDelegate winMenuItemDelegate, int n) {
        if (winMenuItemDelegate == null) {
            return this._insertSeparator(this.ptr, n);
        }
        winMenuItemDelegate.setParent(this);
        if (!this._insertItem(this.ptr, n, winMenuItemDelegate.getCmdID(), winMenuItemDelegate.getOwner().getTitle(), winMenuItemDelegate.getOwner().isEnabled(), winMenuItemDelegate.getOwner().isChecked(), winMenuItemDelegate.getOwner().getCallback(), winMenuItemDelegate.getOwner().getShortcutKey(), winMenuItemDelegate.getOwner().getShortcutModifiers())) {
            winMenuItemDelegate.setParent(null);
            return false;
        }
        return true;
    }

    boolean removeMenu(WinMenuDelegate winMenuDelegate, int n) {
        if (this._removeAtPos(this.ptr, n)) {
            winMenuDelegate.setParent(null);
            return true;
        }
        return false;
    }

    boolean removeItem(WinMenuItemDelegate winMenuItemDelegate, int n) {
        if (this._removeAtPos(this.ptr, n)) {
            if (winMenuItemDelegate != null) {
                winMenuItemDelegate.setParent(null);
            }
            return true;
        }
        return false;
    }

    boolean setSubmenuTitle(WinMenuDelegate winMenuDelegate, String string) {
        return this._setSubmenuTitle(this.ptr, winMenuDelegate.getHMENU(), string);
    }

    boolean setItemTitle(WinMenuItemDelegate winMenuItemDelegate, String string) {
        return this._setItemTitle(this.ptr, winMenuItemDelegate.getCmdID(), string);
    }

    boolean enableSubmenu(WinMenuDelegate winMenuDelegate, boolean bl) {
        return this._enableSubmenu(this.ptr, winMenuDelegate.getHMENU(), bl);
    }

    boolean enableItem(WinMenuItemDelegate winMenuItemDelegate, boolean bl) {
        return this._enableItem(this.ptr, winMenuItemDelegate.getCmdID(), bl);
    }

    public boolean checkItem(WinMenuItemDelegate winMenuItemDelegate, boolean bl) {
        return this._checkItem(this.ptr, winMenuItemDelegate.getCmdID(), bl);
    }

    private static boolean notifyCommand(Window window, int n) {
        MenuItem.Callback callback;
        WinMenuItemDelegate winMenuItemDelegate = WinMenuItemDelegate.CommandIDManager.getHandler(n);
        if (winMenuItemDelegate != null && (callback = winMenuItemDelegate.getOwner().getCallback()) != null) {
            callback.action();
            return true;
        }
        return false;
    }

    private native long _create();

    private native void _destroy(long var1);

    private native boolean _insertItem(long var1, int var3, int var4, String var5, boolean var6, boolean var7, MenuItem.Callback var8, int var9, int var10);

    private native boolean _insertSubmenu(long var1, int var3, long var4, String var6, boolean var7);

    private native boolean _insertSeparator(long var1, int var3);

    private native boolean _removeAtPos(long var1, int var3);

    private native boolean _setItemTitle(long var1, int var3, String var4);

    private native boolean _setSubmenuTitle(long var1, long var3, String var5);

    private native boolean _enableItem(long var1, int var3, boolean var4);

    private native boolean _enableSubmenu(long var1, long var3, boolean var5);

    private native boolean _checkItem(long var1, int var3, boolean var4);

    static {
        WinMenuImpl._initIDs();
    }
}

