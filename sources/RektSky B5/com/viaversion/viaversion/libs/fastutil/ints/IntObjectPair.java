/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Pair;
import com.viaversion.viaversion.libs.fastutil.ints.IntObjectImmutablePair;
import java.util.Comparator;

public interface IntObjectPair<V>
extends Pair<Integer, V> {
    public int leftInt();

    @Override
    @Deprecated
    default public Integer left() {
        return this.leftInt();
    }

    default public IntObjectPair<V> left(int l2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public IntObjectPair<V> left(Integer l2) {
        return this.left((int)l2);
    }

    default public int firstInt() {
        return this.leftInt();
    }

    @Override
    @Deprecated
    default public Integer first() {
        return this.firstInt();
    }

    default public IntObjectPair<V> first(int l2) {
        return this.left(l2);
    }

    @Deprecated
    default public IntObjectPair<V> first(Integer l2) {
        return this.first((int)l2);
    }

    default public int keyInt() {
        return this.firstInt();
    }

    @Override
    @Deprecated
    default public Integer key() {
        return this.keyInt();
    }

    default public IntObjectPair<V> key(int l2) {
        return this.left(l2);
    }

    @Deprecated
    default public IntObjectPair<V> key(Integer l2) {
        return this.key((int)l2);
    }

    public static <V> IntObjectPair<V> of(int left, V right) {
        return new IntObjectImmutablePair<V>(left, right);
    }

    public static <V> Comparator<IntObjectPair<V>> lexComparator() {
        return (x2, y2) -> {
            int t2 = Integer.compare(x2.leftInt(), y2.leftInt());
            if (t2 != 0) {
                return t2;
            }
            return ((Comparable)x2.right()).compareTo(y2.right());
        };
    }
}

