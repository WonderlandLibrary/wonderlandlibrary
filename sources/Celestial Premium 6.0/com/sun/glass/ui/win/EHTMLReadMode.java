/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

final class EHTMLReadMode
extends Enum<EHTMLReadMode> {
    public static final /* enum */ EHTMLReadMode HTML_READ_ALL = new EHTMLReadMode();
    public static final /* enum */ EHTMLReadMode HTML_READ_FRAGMENT = new EHTMLReadMode();
    public static final /* enum */ EHTMLReadMode HTML_READ_SELECTION = new EHTMLReadMode();
    private static final /* synthetic */ EHTMLReadMode[] $VALUES;

    public static EHTMLReadMode[] values() {
        return (EHTMLReadMode[])$VALUES.clone();
    }

    public static EHTMLReadMode valueOf(String string) {
        return Enum.valueOf(EHTMLReadMode.class, string);
    }

    private static /* synthetic */ EHTMLReadMode[] $values() {
        return new EHTMLReadMode[]{HTML_READ_ALL, HTML_READ_FRAGMENT, HTML_READ_SELECTION};
    }

    static {
        $VALUES = EHTMLReadMode.$values();
    }
}

