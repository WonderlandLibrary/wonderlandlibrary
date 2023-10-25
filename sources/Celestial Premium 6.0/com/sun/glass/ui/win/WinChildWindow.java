/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.win.WinWindow;

final class WinChildWindow
extends WinWindow {
    protected WinChildWindow(long l) {
        super(l);
    }

    @Override
    protected long _createWindow(long l, long l2, int n) {
        return 0L;
    }

    @Override
    protected boolean _setMenubar(long l, long l2) {
        return false;
    }

    @Override
    protected boolean _minimize(long l, boolean bl) {
        return false;
    }

    @Override
    protected boolean _maximize(long l, boolean bl, boolean bl2) {
        return false;
    }

    @Override
    protected boolean _setResizable(long l, boolean bl) {
        return false;
    }

    @Override
    protected boolean _setTitle(long l, String string) {
        return false;
    }

    @Override
    protected void _setLevel(long l, int n) {
    }

    @Override
    protected void _setAlpha(long l, float f) {
    }

    @Override
    protected boolean _setMinimumSize(long l, int n, int n2) {
        return false;
    }

    @Override
    protected boolean _setMaximumSize(long l, int n, int n2) {
        return false;
    }

    @Override
    protected void _setIcon(long l, Pixels pixels) {
    }

    @Override
    protected void _enterModal(long l) {
    }

    @Override
    protected void _enterModalWithWindow(long l, long l2) {
    }

    @Override
    protected void _exitModal(long l) {
    }
}

