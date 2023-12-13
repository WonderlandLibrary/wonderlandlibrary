/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.nio.charset.Charset;

@GwtCompatible(emulated=true)
public final class Charsets {
    @GwtIncompatible(value="Non-UTF-8 Charset")
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    @GwtIncompatible(value="Non-UTF-8 Charset")
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    @GwtIncompatible(value="Non-UTF-8 Charset")
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    @GwtIncompatible(value="Non-UTF-8 Charset")
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    @GwtIncompatible(value="Non-UTF-8 Charset")
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    private Charsets() {
    }
}

