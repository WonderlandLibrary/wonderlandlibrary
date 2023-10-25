/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.cursor;

public final class CursorType
extends Enum<CursorType> {
    public static final /* enum */ CursorType DEFAULT = new CursorType();
    public static final /* enum */ CursorType CROSSHAIR = new CursorType();
    public static final /* enum */ CursorType TEXT = new CursorType();
    public static final /* enum */ CursorType WAIT = new CursorType();
    public static final /* enum */ CursorType SW_RESIZE = new CursorType();
    public static final /* enum */ CursorType SE_RESIZE = new CursorType();
    public static final /* enum */ CursorType NW_RESIZE = new CursorType();
    public static final /* enum */ CursorType NE_RESIZE = new CursorType();
    public static final /* enum */ CursorType N_RESIZE = new CursorType();
    public static final /* enum */ CursorType S_RESIZE = new CursorType();
    public static final /* enum */ CursorType W_RESIZE = new CursorType();
    public static final /* enum */ CursorType E_RESIZE = new CursorType();
    public static final /* enum */ CursorType OPEN_HAND = new CursorType();
    public static final /* enum */ CursorType CLOSED_HAND = new CursorType();
    public static final /* enum */ CursorType HAND = new CursorType();
    public static final /* enum */ CursorType MOVE = new CursorType();
    public static final /* enum */ CursorType DISAPPEAR = new CursorType();
    public static final /* enum */ CursorType H_RESIZE = new CursorType();
    public static final /* enum */ CursorType V_RESIZE = new CursorType();
    public static final /* enum */ CursorType NONE = new CursorType();
    public static final /* enum */ CursorType IMAGE = new CursorType();
    private static final /* synthetic */ CursorType[] $VALUES;

    public static CursorType[] values() {
        return (CursorType[])$VALUES.clone();
    }

    public static CursorType valueOf(String string) {
        return Enum.valueOf(CursorType.class, string);
    }

    private static /* synthetic */ CursorType[] $values() {
        return new CursorType[]{DEFAULT, CROSSHAIR, TEXT, WAIT, SW_RESIZE, SE_RESIZE, NW_RESIZE, NE_RESIZE, N_RESIZE, S_RESIZE, W_RESIZE, E_RESIZE, OPEN_HAND, CLOSED_HAND, HAND, MOVE, DISAPPEAR, H_RESIZE, V_RESIZE, NONE, IMAGE};
    }

    static {
        $VALUES = CursorType.$values();
    }
}

