/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.kyori.adventure.text;

public final class PatternReplacementResult
extends Enum<PatternReplacementResult> {
    public static final /* enum */ PatternReplacementResult REPLACE = new PatternReplacementResult();
    public static final /* enum */ PatternReplacementResult CONTINUE = new PatternReplacementResult();
    public static final /* enum */ PatternReplacementResult STOP = new PatternReplacementResult();
    private static final /* synthetic */ PatternReplacementResult[] $VALUES;

    public static PatternReplacementResult[] values() {
        return (PatternReplacementResult[])$VALUES.clone();
    }

    public static PatternReplacementResult valueOf(String name) {
        return Enum.valueOf(PatternReplacementResult.class, name);
    }

    private static /* synthetic */ PatternReplacementResult[] $values() {
        return new PatternReplacementResult[]{REPLACE, CONTINUE, STOP};
    }

    static {
        $VALUES = PatternReplacementResult.$values();
    }
}

