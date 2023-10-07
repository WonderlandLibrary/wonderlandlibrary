// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Comparator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectObjectImmutablePair;

public interface Pair<L, R>
{
    L left();
    
    R right();
    
    default Pair<L, R> left(final L l) {
        throw new UnsupportedOperationException();
    }
    
    default Pair<L, R> right(final R r) {
        throw new UnsupportedOperationException();
    }
    
    default L first() {
        return this.left();
    }
    
    default R second() {
        return this.right();
    }
    
    default Pair<L, R> first(final L l) {
        return this.left(l);
    }
    
    default Pair<L, R> second(final R r) {
        return this.right(r);
    }
    
    default Pair<L, R> key(final L l) {
        return this.left(l);
    }
    
    default Pair<L, R> value(final R r) {
        return this.right(r);
    }
    
    default L key() {
        return this.left();
    }
    
    default R value() {
        return this.right();
    }
    
    default <L, R> Pair<L, R> of(final L l, final R r) {
        return new ObjectObjectImmutablePair<L, R>(l, r);
    }
    
    default <L, R> Comparator<Pair<L, R>> lexComparator() {
        return (x, y) -> {
            final int t = x.left().compareTo(y.left());
            if (t != 0) {
                return t;
            }
            else {
                return x.right().compareTo(y.right());
            }
        };
    }
}
