/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Menu {
    private final MenuDelegate delegate;
    private String title;
    private boolean enabled;
    private final List<Object> items = new ArrayList<Object>();
    private EventHandler eventHandler;

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    protected Menu(String string) {
        this(string, true);
    }

    protected Menu(String string, boolean bl) {
        Application.checkEventThread();
        this.title = string;
        this.enabled = bl;
        this.delegate = PlatformFactory.getPlatformFactory().createMenuDelegate(this);
        if (!this.delegate.createMenu(string, bl)) {
            throw new RuntimeException("Menu creation error.");
        }
    }

    public String getTitle() {
        Application.checkEventThread();
        return this.title;
    }

    public void setTitle(String string) {
        Application.checkEventThread();
        if (this.delegate.setTitle(string)) {
            this.title = string;
        }
    }

    public boolean isEnabled() {
        Application.checkEventThread();
        return this.enabled;
    }

    public void setEnabled(boolean bl) {
        Application.checkEventThread();
        if (this.delegate.setEnabled(bl)) {
            this.enabled = bl;
        }
    }

    public boolean setPixels(Pixels pixels) {
        Application.checkEventThread();
        return this.delegate.setPixels(pixels);
    }

    public List<Object> getItems() {
        Application.checkEventThread();
        return Collections.unmodifiableList(this.items);
    }

    public void add(Menu menu) {
        Application.checkEventThread();
        this.insert(menu, this.items.size());
    }

    public void add(MenuItem menuItem) {
        Application.checkEventThread();
        this.insert(menuItem, this.items.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void insert(Menu menu, int n) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        if (menu == null) {
            throw new IllegalArgumentException();
        }
        List<Object> list = this.items;
        synchronized (list) {
            if (n < 0 || n > this.items.size()) {
                throw new IndexOutOfBoundsException();
            }
            MenuDelegate menuDelegate = menu.getDelegate();
            if (this.delegate.insert(menuDelegate, n)) {
                this.items.add(n, menu);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void insert(MenuItem menuItem, int n) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        List<Object> list = this.items;
        synchronized (list) {
            MenuItemDelegate menuItemDelegate;
            if (n < 0 || n > this.items.size()) {
                throw new IndexOutOfBoundsException();
            }
            MenuItemDelegate menuItemDelegate2 = menuItemDelegate = menuItem != null ? menuItem.getDelegate() : null;
            if (this.delegate.insert(menuItemDelegate, n)) {
                this.items.add(n, menuItem);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove(int n) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        List<Object> list = this.items;
        synchronized (list) {
            Object object = this.items.get(n);
            boolean bl = false;
            bl = object == MenuItem.Separator ? this.delegate.remove((MenuItemDelegate)null, n) : (object instanceof MenuItem ? this.delegate.remove(((MenuItem)object).getDelegate(), n) : this.delegate.remove(((Menu)object).getDelegate(), n));
            if (bl) {
                this.items.remove(n);
            }
        }
    }

    MenuDelegate getDelegate() {
        return this.delegate;
    }

    protected void notifyMenuOpening() {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuOpening(this, System.nanoTime());
        }
    }

    protected void notifyMenuClosed() {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuClosed(this, System.nanoTime());
        }
    }

    public static class EventHandler {
        public void handleMenuOpening(Menu menu, long l) {
        }

        public void handleMenuClosed(Menu menu, long l) {
        }
    }
}

