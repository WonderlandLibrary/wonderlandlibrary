// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Comparator;
import com.viaversion.viaversion.libs.fastutil.Pair;

public interface ObjectIntPair<K> extends Pair<K, Integer>
{
    int rightInt();
    
    @Deprecated
    default Integer right() {
        return this.rightInt();
    }
    
    default ObjectIntPair<K> right(final int r) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default ObjectIntPair<K> right(final Integer l) {
        return this.right((int)l);
    }
    
    default int secondInt() {
        return this.rightInt();
    }
    
    @Deprecated
    default Integer second() {
        return this.secondInt();
    }
    
    default ObjectIntPair<K> second(final int r) {
        return this.right(r);
    }
    
    @Deprecated
    default ObjectIntPair<K> second(final Integer l) {
        return this.second((int)l);
    }
    
    default int valueInt() {
        return this.rightInt();
    }
    
    @Deprecated
    default Integer value() {
        return this.valueInt();
    }
    
    default ObjectIntPair<K> value(final int r) {
        return this.right(r);
    }
    
    @Deprecated
    default ObjectIntPair<K> value(final Integer l) {
        return this.value((int)l);
    }
    
    default <K> ObjectIntPair<K> of(final K left, final int right) {
        return new ObjectIntImmutablePair<K>(left, right);
    }
    
    default <K> Comparator<ObjectIntPair<K>> lexComparator() {
        final int t;
        return (x, y) -> {
            t = x.left().compareTo(y.left());
            if (t != 0) {
                return t;
            }
            else {
                return Integer.compare(x.rightInt(), y.rightInt());
            }
        };
    }
}
