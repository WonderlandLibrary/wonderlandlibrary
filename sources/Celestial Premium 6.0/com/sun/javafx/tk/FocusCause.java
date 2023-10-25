/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

public final class FocusCause
extends Enum<FocusCause> {
    public static final /* enum */ FocusCause TRAVERSED_FORWARD = new FocusCause();
    public static final /* enum */ FocusCause TRAVERSED_BACKWARD = new FocusCause();
    public static final /* enum */ FocusCause ACTIVATED = new FocusCause();
    public static final /* enum */ FocusCause DEACTIVATED = new FocusCause();
    private static final /* synthetic */ FocusCause[] $VALUES;

    public static FocusCause[] values() {
        return (FocusCause[])$VALUES.clone();
    }

    public static FocusCause valueOf(String string) {
        return Enum.valueOf(FocusCause.class, string);
    }

    private static /* synthetic */ FocusCause[] $values() {
        return new FocusCause[]{TRAVERSED_FORWARD, TRAVERSED_BACKWARD, ACTIVATED, DEACTIVATED};
    }

    static {
        $VALUES = FocusCause.$values();
    }
}

