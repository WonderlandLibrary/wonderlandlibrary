// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

final class SystemProperties
{
    public static final String LINE_SEPARATOR;
    
    private SystemProperties() {
    }
    
    static {
        new SystemProperties();
        final String property = System.getProperty("line.separator");
        if (property == null) {
            Intrinsics.throwNpe();
        }
        LINE_SEPARATOR = property;
    }
}
