/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

public final class CompositeMode
extends Enum<CompositeMode> {
    public static final /* enum */ CompositeMode CLEAR = new CompositeMode();
    public static final /* enum */ CompositeMode SRC = new CompositeMode();
    public static final /* enum */ CompositeMode SRC_OVER = new CompositeMode();
    public static final /* enum */ CompositeMode DST_OUT = new CompositeMode();
    public static final /* enum */ CompositeMode ADD = new CompositeMode();
    private static final /* synthetic */ CompositeMode[] $VALUES;

    public static CompositeMode[] values() {
        return (CompositeMode[])$VALUES.clone();
    }

    public static CompositeMode valueOf(String string) {
        return Enum.valueOf(CompositeMode.class, string);
    }

    private static /* synthetic */ CompositeMode[] $values() {
        return new CompositeMode[]{CLEAR, SRC, SRC_OVER, DST_OUT, ADD};
    }

    static {
        $VALUES = CompositeMode.$values();
    }
}

