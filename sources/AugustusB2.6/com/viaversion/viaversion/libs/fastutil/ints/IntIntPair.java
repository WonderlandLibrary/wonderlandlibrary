// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Comparator;
import com.viaversion.viaversion.libs.fastutil.Pair;

public interface IntIntPair extends Pair<Integer, Integer>
{
    int leftInt();
    
    @Deprecated
    default Integer left() {
        return this.leftInt();
    }
    
    default IntIntPair left(final int l) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default IntIntPair left(final Integer l) {
        return this.left((int)l);
    }
    
    default int firstInt() {
        return this.leftInt();
    }
    
    @Deprecated
    default Integer first() {
        return this.firstInt();
    }
    
    default IntIntPair first(final int l) {
        return this.left(l);
    }
    
    @Deprecated
    default IntIntPair first(final Integer l) {
        return this.first((int)l);
    }
    
    default int keyInt() {
        return this.firstInt();
    }
    
    @Deprecated
    default Integer key() {
        return this.keyInt();
    }
    
    default IntIntPair key(final int l) {
        return this.left(l);
    }
    
    @Deprecated
    default IntIntPair key(final Integer l) {
        return this.key((int)l);
    }
    
    int rightInt();
    
    @Deprecated
    default Integer right() {
        return this.rightInt();
    }
    
    default IntIntPair right(final int r) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default IntIntPair right(final Integer l) {
        return this.right((int)l);
    }
    
    default int secondInt() {
        return this.rightInt();
    }
    
    @Deprecated
    default Integer second() {
        return this.secondInt();
    }
    
    default IntIntPair second(final int r) {
        return this.right(r);
    }
    
    @Deprecated
    default IntIntPair second(final Integer l) {
        return this.second((int)l);
    }
    
    default int valueInt() {
        return this.rightInt();
    }
    
    @Deprecated
    default Integer value() {
        return this.valueInt();
    }
    
    default IntIntPair value(final int r) {
        return this.right(r);
    }
    
    @Deprecated
    default IntIntPair value(final Integer l) {
        return this.value((int)l);
    }
    
    default IntIntPair of(final int left, final int right) {
        return new IntIntImmutablePair(left, right);
    }
    
    default Comparator<IntIntPair> lexComparator() {
        final int t;
        return (x, y) -> {
            t = Integer.compare(x.leftInt(), y.leftInt());
            if (t != 0) {
                return t;
            }
            else {
                return Integer.compare(x.rightInt(), y.rightInt());
            }
        };
    }
}
