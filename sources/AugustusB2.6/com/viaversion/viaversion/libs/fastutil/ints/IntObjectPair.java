// 
// Decompiled by Procyon v0.5.36
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
        final int t;
        return (x, y) -> {
            t = Integer.compare(x.leftInt(), ((IntObjectPair)y).leftInt());
            if (t != 0) {
                return t;
            }
            else {
                return x.right().compareTo(y.right());
            }
        };
    }
}
