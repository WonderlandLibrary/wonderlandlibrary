/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

public abstract class Cursor {
    public static final int CURSOR_NONE = -1;
    public static final int CURSOR_CUSTOM = 0;
    public static final int CURSOR_DEFAULT = 1;
    public static final int CURSOR_TEXT = 2;
    public static final int CURSOR_CROSSHAIR = 3;
    public static final int CURSOR_CLOSED_HAND = 4;
    public static final int CURSOR_OPEN_HAND = 5;
    public static final int CURSOR_POINTING_HAND = 6;
    public static final int CURSOR_RESIZE_LEFT = 7;
    public static final int CURSOR_RESIZE_RIGHT = 8;
    public static final int CURSOR_RESIZE_UP = 9;
    public static final int CURSOR_RESIZE_DOWN = 10;
    public static final int CURSOR_RESIZE_LEFTRIGHT = 11;
    public static final int CURSOR_RESIZE_UPDOWN = 12;
    public static final int CURSOR_DISAPPEAR = 13;
    public static final int CURSOR_WAIT = 14;
    public static final int CURSOR_RESIZE_SOUTHWEST = 15;
    public static final int CURSOR_RESIZE_SOUTHEAST = 16;
    public static final int CURSOR_RESIZE_NORTHWEST = 17;
    public static final int CURSOR_RESIZE_NORTHEAST = 18;
    public static final int CURSOR_MOVE = 19;
    private static final int CURSOR_MAX = 19;
    private final int type;
    private long ptr;

    protected Cursor(int n) {
        Application.checkEventThread();
        this.type = n;
    }

    protected Cursor(int n, int n2, Pixels pixels) {
        this(0);
        this.ptr = this._createCursor(n, n2, pixels);
    }

    public final int getType() {
        Application.checkEventThread();
        return this.type;
    }

    protected final long getNativeCursor() {
        Application.checkEventThread();
        return this.ptr;
    }

    public static void setVisible(boolean bl) {
        Application.checkEventThread();
        Application.GetApplication().staticCursor_setVisible(bl);
    }

    public static Size getBestSize(int n, int n2) {
        Application.checkEventThread();
        return Application.GetApplication().staticCursor_getBestSize(n, n2);
    }

    protected abstract long _createCursor(int var1, int var2, Pixels var3);
}

