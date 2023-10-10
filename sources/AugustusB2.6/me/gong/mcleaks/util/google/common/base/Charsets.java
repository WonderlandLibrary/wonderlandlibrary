// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Charsets
{
    @GwtIncompatible
    public static final Charset US_ASCII;
    public static final Charset ISO_8859_1;
    public static final Charset UTF_8;
    @GwtIncompatible
    public static final Charset UTF_16BE;
    @GwtIncompatible
    public static final Charset UTF_16LE;
    @GwtIncompatible
    public static final Charset UTF_16;
    
    private Charsets() {
    }
    
    static {
        US_ASCII = Charset.forName("US-ASCII");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        UTF_8 = Charset.forName("UTF-8");
        UTF_16BE = Charset.forName("UTF-16BE");
        UTF_16LE = Charset.forName("UTF-16LE");
        UTF_16 = Charset.forName("UTF-16");
    }
}
