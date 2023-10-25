/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

public final class CssFlags
extends Enum<CssFlags> {
    public static final /* enum */ CssFlags CLEAN = new CssFlags();
    public static final /* enum */ CssFlags DIRTY_BRANCH = new CssFlags();
    public static final /* enum */ CssFlags UPDATE = new CssFlags();
    public static final /* enum */ CssFlags REAPPLY = new CssFlags();
    private static final /* synthetic */ CssFlags[] $VALUES;

    public static CssFlags[] values() {
        return (CssFlags[])$VALUES.clone();
    }

    public static CssFlags valueOf(String string) {
        return Enum.valueOf(CssFlags.class, string);
    }

    private static /* synthetic */ CssFlags[] $values() {
        return new CssFlags[]{CLEAN, DIRTY_BRANCH, UPDATE, REAPPLY};
    }

    static {
        $VALUES = CssFlags.$values();
    }
}

