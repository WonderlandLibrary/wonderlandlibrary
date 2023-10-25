/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Menu;
import com.sun.javafx.menu.MenuBase;

class GlassMenuEventHandler
extends Menu.EventHandler {
    private MenuBase menuBase;
    private boolean menuOpen;

    public GlassMenuEventHandler(MenuBase menuBase) {
        this.menuBase = menuBase;
    }

    @Override
    public void handleMenuOpening(Menu menu, long l) {
        this.menuBase.show();
        this.menuOpen = true;
    }

    @Override
    public void handleMenuClosed(Menu menu, long l) {
        this.menuBase.hide();
        this.menuOpen = false;
    }

    protected boolean isMenuOpen() {
        return this.menuOpen;
    }
}

