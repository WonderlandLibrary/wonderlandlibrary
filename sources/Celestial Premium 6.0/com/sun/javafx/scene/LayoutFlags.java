/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

public final class LayoutFlags
extends Enum<LayoutFlags> {
    public static final /* enum */ LayoutFlags CLEAN = new LayoutFlags();
    public static final /* enum */ LayoutFlags DIRTY_BRANCH = new LayoutFlags();
    public static final /* enum */ LayoutFlags NEEDS_LAYOUT = new LayoutFlags();
    private static final /* synthetic */ LayoutFlags[] $VALUES;

    public static LayoutFlags[] values() {
        return (LayoutFlags[])$VALUES.clone();
    }

    public static LayoutFlags valueOf(String string) {
        return Enum.valueOf(LayoutFlags.class, string);
    }

    private static /* synthetic */ LayoutFlags[] $values() {
        return new LayoutFlags[]{CLEAN, DIRTY_BRANCH, NEEDS_LAYOUT};
    }

    static {
        $VALUES = LayoutFlags.$values();
    }
}

