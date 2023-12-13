// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Comparator;
import com.viaversion.viaversion.libs.fastutil.Pair;

public interface IntObjectPair<V> extends Pair<Integer, V>
{
    int leftInt();
    
    @Deprecated
    default Integer left() {
        return this.leftInt();
    }
    
    default IntObjectPair<V> left(final int l) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default IntObjectPair<V> left(final Integer l) {
        return this.left((int)l);
    }
    
    default int firstInt() {
        return this.leftInt();
    }
    
    @Deprecated
    default Integer first() {
        return this.firstInt();
    }
    
    default IntObjectPair<V> first(final int l) {
        return this.left(l);
    }
    
    @Deprecated
    default IntObjectPair<V> first(final Integer l) {
        return this.first((int)l);
    }
    
    default int keyInt() {
        return this.firstInt();
    }
    
    @Deprecated
    default Integer key() {
        return this.keyInt();
    }
    
    default IntObjectPair<V> key(final int l) {
        return this.left(l);
    }
    
    @Deprecated
    default IntObjectPair<V> key(final Integer l) {
        return this.key((int)l);
    }
    
    default <V> IntObjectPair<V> of(final int left, final V right) {
        return new IntObjectImmutablePair<V>(left, right);
    }
    
    default <V> Comparator<IntObjectPair<V>> lexComparator() {
        return (x, y) -> {
            final int t = Integer.compare(x.leftInt(), y.leftInt());
            if (t != 0) {
                return t;
            }
            else {
                return ((Comparable)x.right()).compareTo(y.right());
            }
        };
    }
}
