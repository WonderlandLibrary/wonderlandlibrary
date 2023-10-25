/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.effect;

public final class EffectDirtyBits
extends Enum<EffectDirtyBits> {
    public static final /* enum */ EffectDirtyBits EFFECT_DIRTY = new EffectDirtyBits();
    public static final /* enum */ EffectDirtyBits BOUNDS_CHANGED = new EffectDirtyBits();
    private int mask = 1 << this.ordinal();
    private static final /* synthetic */ EffectDirtyBits[] $VALUES;

    public static EffectDirtyBits[] values() {
        return (EffectDirtyBits[])$VALUES.clone();
    }

    public static EffectDirtyBits valueOf(String string) {
        return Enum.valueOf(EffectDirtyBits.class, string);
    }

    public final int getMask() {
        return this.mask;
    }

    public static boolean isSet(int n, EffectDirtyBits effectDirtyBits) {
        return (n & effectDirtyBits.getMask()) != 0;
    }

    private static /* synthetic */ EffectDirtyBits[] $values() {
        return new EffectDirtyBits[]{EFFECT_DIRTY, BOUNDS_CHANGED};
    }

    static {
        $VALUES = EffectDirtyBits.$values();
    }
}

