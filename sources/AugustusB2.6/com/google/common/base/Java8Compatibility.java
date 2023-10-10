// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.nio.Buffer;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class Java8Compatibility
{
    static void clear(final Buffer b) {
        b.clear();
    }
    
    static void flip(final Buffer b) {
        b.flip();
    }
    
    static void limit(final Buffer b, final int limit) {
        b.limit(limit);
    }
    
    static void position(final Buffer b, final int position) {
        b.position(position);
    }
    
    private Java8Compatibility() {
    }
}
