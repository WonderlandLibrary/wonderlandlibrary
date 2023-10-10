// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.ranges.IntRange;

class CharsKt__CharJVMKt
{
    public static final int checkRadix(final int radix) {
        final int n = 36;
        if (2 <= radix) {
            if (n >= radix) {
                return radix;
            }
        }
        throw new IllegalArgumentException("radix " + radix + " was not in valid range " + new IntRange(2, 36));
    }
    
    public static boolean equals(final char $this$equals, final char other, final boolean ignoreCase) {
        return $this$equals == other || (ignoreCase && (Character.toUpperCase($this$equals) == Character.toUpperCase(other) || Character.toLowerCase($this$equals) == Character.toLowerCase(other)));
    }
}
