/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuBar {
    private final MenuBarDelegate delegate;
    private final List<Menu> menus = new ArrayList<Menu>();

    protected MenuBar() {
        Application.checkEventThread();
        this.delegate = PlatformFactory.getPlatformFactory().createMenuBarDelegate(this);
        if (!this.delegate.createMenuBar()) {
            throw new RuntimeException("MenuBar creation error.");
        }
    }

    long getNativeMenu() {
        return this.delegate.getNativeMenu();
    }

    public void add(Menu menu) {
        Application.checkEventThread();
        this.insert(menu, this.menus.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void insert(Menu menu, int n) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            if (this.delegate.insert(menu.getDelegate(), n)) {
                this.menus.add(n, menu);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove(int n) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            Menu menu = this.menus.get(n);
            if (this.delegate.remove(menu.getDelegate(), n)) {
                this.menus.remove(n);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove(Menu menu) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            int n = this.menus.indexOf(menu);
            if (n >= 0 && this.delegate.remove(menu.getDelegate(), n)) {
                this.menus.remove(n);
            }
        }
    }

    public List<Menu> getMenus() {
        Application.checkEventThread();
        return Collections.unmodifiableList(this.menus);
    }
}

