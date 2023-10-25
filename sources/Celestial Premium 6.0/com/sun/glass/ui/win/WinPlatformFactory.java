/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.glass.ui.win.WinApplication;
import com.sun.glass.ui.win.WinClipboardDelegate;
import com.sun.glass.ui.win.WinMenuBarDelegate;
import com.sun.glass.ui.win.WinMenuDelegate;
import com.sun.glass.ui.win.WinMenuItemDelegate;

public final class WinPlatformFactory
extends PlatformFactory {
    @Override
    public WinApplication createApplication() {
        return new WinApplication();
    }

    @Override
    public MenuBarDelegate createMenuBarDelegate(MenuBar menuBar) {
        return new WinMenuBarDelegate(menuBar);
    }

    @Override
    public MenuDelegate createMenuDelegate(Menu menu) {
        return new WinMenuDelegate(menu);
    }

    @Override
    public MenuItemDelegate createMenuItemDelegate(MenuItem menuItem) {
        return new WinMenuItemDelegate(menuItem);
    }

    @Override
    public ClipboardDelegate createClipboardDelegate() {
        return new WinClipboardDelegate();
    }
}

