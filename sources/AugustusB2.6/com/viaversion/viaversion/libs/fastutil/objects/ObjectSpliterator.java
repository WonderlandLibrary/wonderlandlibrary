// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Spliterator;

public interface ObjectSpliterator<K> extends Spliterator<K>
{
    default long skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i = n;
        while (i-- != 0L && this.tryAdvance(unused -> {})) {}
        return n - i - 1L;
    }
    
    ObjectSpliterator<K> trySplit();
}
