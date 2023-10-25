/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TriState
extends Enum<TriState> {
    public static final /* enum */ TriState NOT_SET = new TriState();
    public static final /* enum */ TriState FALSE = new TriState();
    public static final /* enum */ TriState TRUE = new TriState();
    private static final /* synthetic */ TriState[] $VALUES;

    public static TriState[] values() {
        return (TriState[])$VALUES.clone();
    }

    public static TriState valueOf(String name) {
        return Enum.valueOf(TriState.class, name);
    }

    @NotNull
    public static TriState byBoolean(boolean value) {
        return value ? TRUE : FALSE;
    }

    @NotNull
    public static TriState byBoolean(@Nullable Boolean value) {
        return value == null ? NOT_SET : TriState.byBoolean((boolean)value);
    }

    private static /* synthetic */ TriState[] $values() {
        return new TriState[]{NOT_SET, FALSE, TRUE};
    }

    static {
        $VALUES = TriState.$values();
    }
}

