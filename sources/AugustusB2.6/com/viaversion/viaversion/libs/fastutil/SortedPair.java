// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectObjectImmutableSortedPair;

public interface SortedPair<K extends Comparable<K>> extends Pair<K, K>
{
    default <K extends Comparable<K>> SortedPair<K> of(final K l, final K r) {
        return ObjectObjectImmutableSortedPair.of(l, r);
    }
    
    default boolean contains(final Object o) {
        return Objects.equals(o, this.left()) || Objects.equals(o, this.right());
    }
}
