/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

final class WinCursor
extends Cursor {
    private static native void _initIDs();

    protected WinCursor(int n) {
        super(n);
    }

    protected WinCursor(int n, int n2, Pixels pixels) {
        super(n, n2, pixels);
    }

    @Override
    protected native long _createCursor(int var1, int var2, Pixels var3);

    private static native void _setVisible(boolean var0);

    private static native Size _getBestSize(int var0, int var1);

    static void setVisible_impl(boolean bl) {
        WinCursor._setVisible(bl);
    }

    static Size getBestSize_impl(int n, int n2) {
        return WinCursor._getBestSize(n, n2);
    }

    static {
        WinCursor._initIDs();
    }
}

