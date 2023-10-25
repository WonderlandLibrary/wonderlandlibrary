/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

public final class AlphaType
extends Enum<AlphaType> {
    public static final /* enum */ AlphaType OPAQUE = new AlphaType();
    public static final /* enum */ AlphaType PREMULTIPLIED = new AlphaType();
    public static final /* enum */ AlphaType NONPREMULTIPLIED = new AlphaType();
    private static final /* synthetic */ AlphaType[] $VALUES;

    public static AlphaType[] values() {
        return (AlphaType[])$VALUES.clone();
    }

    public static AlphaType valueOf(String string) {
        return Enum.valueOf(AlphaType.class, string);
    }

    private static /* synthetic */ AlphaType[] $values() {
        return new AlphaType[]{OPAQUE, PREMULTIPLIED, NONPREMULTIPLIED};
    }

    static {
        $VALUES = AlphaType.$values();
    }
}

