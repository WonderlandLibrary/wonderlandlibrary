// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import java.nio.charset.Charset;

public final class Charsets
{
    public static final Charset UTF_8;
    
    private Charsets() {
    }
    
    static {
        new Charsets();
        final Charset forName = Charset.forName("UTF-8");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-8\")");
        UTF_8 = forName;
        Intrinsics.checkExpressionValueIsNotNull(Charset.forName("UTF-16"), "Charset.forName(\"UTF-16\")");
        Intrinsics.checkExpressionValueIsNotNull(Charset.forName("UTF-16BE"), "Charset.forName(\"UTF-16BE\")");
        Intrinsics.checkExpressionValueIsNotNull(Charset.forName("UTF-16LE"), "Charset.forName(\"UTF-16LE\")");
        Intrinsics.checkExpressionValueIsNotNull(Charset.forName("US-ASCII"), "Charset.forName(\"US-ASCII\")");
        Intrinsics.checkExpressionValueIsNotNull(Charset.forName("ISO-8859-1"), "Charset.forName(\"ISO-8859-1\")");
    }
}
