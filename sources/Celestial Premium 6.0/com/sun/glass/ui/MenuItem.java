/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.MenuItemDelegate;

public final class MenuItem {
    public static final MenuItem Separator = null;
    private final MenuItemDelegate delegate;
    private String title;
    private Callback callback;
    private boolean enabled;
    private boolean checked;
    private int shortcutKey;
    private int shortcutModifiers;

    protected MenuItem(String string) {
        this(string, null);
    }

    protected MenuItem(String string, Callback callback) {
        this(string, callback, 0, 0);
    }

    protected MenuItem(String string, Callback callback, int n, int n2) {
        this(string, callback, n, n2, null);
    }

    protected MenuItem(String string, Callback callback, int n, int n2, Pixels pixels) {
        Application.checkEventThread();
        this.title = string;
        this.callback = callback;
        this.shortcutKey = n;
        this.shortcutModifiers = n2;
        this.enabled = true;
        this.checked = false;
        this.delegate = PlatformFactory.getPlatformFactory().createMenuItemDelegate(this);
        if (!this.delegate.createMenuItem(string, callback, n, n2, pixels, this.enabled, this.checked)) {
            throw new RuntimeException("MenuItem creation error.");
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

    public Callback getCallback() {
        Application.checkEventThread();
        return this.callback;
    }

    public void setCallback(Callback callback) {
        Application.checkEventThread();
        if (this.delegate.setCallback(callback)) {
            this.callback = callback;
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

    public boolean isChecked() {
        Application.checkEventThread();
        return this.checked;
    }

    public void setChecked(boolean bl) {
        Application.checkEventThread();
        if (this.delegate.setChecked(bl)) {
            this.checked = bl;
        }
    }

    public int getShortcutKey() {
        Application.checkEventThread();
        return this.shortcutKey;
    }

    public int getShortcutModifiers() {
        Application.checkEventThread();
        return this.shortcutModifiers;
    }

    public void setShortcut(int n, int n2) {
        Application.checkEventThread();
        if (this.delegate.setShortcut(n, n2)) {
            this.shortcutKey = n;
            this.shortcutModifiers = n2;
        }
    }

    public boolean setPixels(Pixels pixels) {
        Application.checkEventThread();
        return this.delegate.setPixels(pixels);
    }

    MenuItemDelegate getDelegate() {
        return this.delegate;
    }

    public static interface Callback {
        public void action();

        public void validate();
    }
}

