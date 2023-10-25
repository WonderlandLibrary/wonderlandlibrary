/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.print;

public final class Units
extends Enum<Units> {
    public static final /* enum */ Units MM = new Units();
    public static final /* enum */ Units INCH = new Units();
    public static final /* enum */ Units POINT = new Units();
    private static final /* synthetic */ Units[] $VALUES;

    public static Units[] values() {
        return (Units[])$VALUES.clone();
    }

    public static Units valueOf(String string) {
        return Enum.valueOf(Units.class, string);
    }

    private static /* synthetic */ Units[] $values() {
        return new Units[]{MM, INCH, POINT};
    }

    static {
        $VALUES = Units.$values();
    }
}

