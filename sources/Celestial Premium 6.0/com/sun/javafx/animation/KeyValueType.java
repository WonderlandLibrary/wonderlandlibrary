/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.animation;

public final class KeyValueType
extends Enum<KeyValueType> {
    public static final /* enum */ KeyValueType BOOLEAN = new KeyValueType();
    public static final /* enum */ KeyValueType DOUBLE = new KeyValueType();
    public static final /* enum */ KeyValueType FLOAT = new KeyValueType();
    public static final /* enum */ KeyValueType INTEGER = new KeyValueType();
    public static final /* enum */ KeyValueType LONG = new KeyValueType();
    public static final /* enum */ KeyValueType OBJECT = new KeyValueType();
    private static final /* synthetic */ KeyValueType[] $VALUES;

    public static KeyValueType[] values() {
        return (KeyValueType[])$VALUES.clone();
    }

    public static KeyValueType valueOf(String string) {
        return Enum.valueOf(KeyValueType.class, string);
    }

    private static /* synthetic */ KeyValueType[] $values() {
        return new KeyValueType[]{BOOLEAN, DOUBLE, FLOAT, INTEGER, LONG, OBJECT};
    }

    static {
        $VALUES = KeyValueType.$values();
    }
}

