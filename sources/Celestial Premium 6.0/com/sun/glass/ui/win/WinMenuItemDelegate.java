/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.glass.ui.win.WinMenuImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class WinMenuItemDelegate
implements MenuItemDelegate {
    private final MenuItem owner;
    private WinMenuImpl parent = null;
    private int cmdID = -1;

    public WinMenuItemDelegate(MenuItem menuItem) {
        this.owner = menuItem;
    }

    public MenuItem getOwner() {
        return this.owner;
    }

    @Override
    public boolean createMenuItem(String string, MenuItem.Callback callback, int n, int n2, Pixels pixels, boolean bl, boolean bl2) {
        return true;
    }

    @Override
    public boolean setTitle(String string) {
        if (this.parent != null) {
            string = this.getTitle(string, this.getOwner().getShortcutKey(), this.getOwner().getShortcutModifiers());
            return this.parent.setItemTitle(this, string);
        }
        return true;
    }

    @Override
    public boolean setCallback(MenuItem.Callback callback) {
        return true;
    }

    @Override
    public boolean setShortcut(int n, int n2) {
        if (this.parent != null) {
            String string = this.getTitle(this.getOwner().getTitle(), n, n2);
            return this.parent.setItemTitle(this, string);
        }
        return true;
    }

    @Override
    public boolean setPixels(Pixels pixels) {
        return false;
    }

    @Override
    public boolean setEnabled(boolean bl) {
        if (this.parent != null) {
            return this.parent.enableItem(this, bl);
        }
        return true;
    }

    @Override
    public boolean setChecked(boolean bl) {
        if (this.parent != null) {
            return this.parent.checkItem(this, bl);
        }
        return true;
    }

    private String getTitle(String string, int n, int n2) {
        if (n == 0) {
            return string;
        }
        return string;
    }

    WinMenuImpl getParent() {
        return this.parent;
    }

    void setParent(WinMenuImpl winMenuImpl) {
        if (this.parent != null) {
            CommandIDManager.freeID(this.cmdID);
            this.cmdID = -1;
        }
        if (winMenuImpl != null) {
            this.cmdID = CommandIDManager.getID(this);
        }
        this.parent = winMenuImpl;
    }

    int getCmdID() {
        return this.cmdID;
    }

    static class CommandIDManager {
        private static final int FIRST_ID = 1;
        private static final int LAST_ID = 65535;
        private static List<Integer> freeList = new ArrayList<Integer>();
        private static final Map<Integer, WinMenuItemDelegate> map = new HashMap<Integer, WinMenuItemDelegate>();
        private static int nextID = 1;

        CommandIDManager() {
        }

        public static synchronized int getID(WinMenuItemDelegate winMenuItemDelegate) {
            Integer n;
            if (freeList.isEmpty()) {
                if (nextID > 65535) {
                    nextID = 1;
                }
                n = nextID;
                ++nextID;
            } else {
                n = freeList.remove(freeList.size() - 1);
            }
            map.put(n, winMenuItemDelegate);
            return n;
        }

        public static synchronized void freeID(int n) {
            Integer n2 = n;
            if (map.remove(n2) != null) {
                freeList.add(n2);
            }
        }

        public static WinMenuItemDelegate getHandler(int n) {
            return map.get(n);
        }
    }
}

