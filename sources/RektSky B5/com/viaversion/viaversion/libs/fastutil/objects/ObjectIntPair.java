/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Pair;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIntImmutablePair;
import java.util.Comparator;

public interface ObjectIntPair<K>
extends Pair<K, Integer> {
    public int rightInt();

    @Override
    @Deprecated
    default public Integer right() {
        return this.rightInt();
    }

    default public ObjectIntPair<K> right(int r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public ObjectIntPair<K> right(Integer l2) {
        return this.right((int)l2);
    }

    default public int secondInt() {
        return this.rightInt();
    }

    @Override
    @Deprecated
    default public Integer second() {
        return this.secondInt();
    }

    default public ObjectIntPair<K> second(int r2) {
        return this.right(r2);
    }

    @Deprecated
    default public ObjectIntPair<K> second(Integer l2) {
        return this.second((int)l2);
    }

    default public int valueInt() {
        return this.rightInt();
    }

    @Override
    @Deprecated
    default public Integer value() {
        return this.valueInt();
    }

    default public ObjectIntPair<K> value(int r2) {
        return this.right(r2);
    }

    @Deprecated
    default public ObjectIntPair<K> value(Integer l2) {
        return this.value((int)l2);
    }

    public static <K> ObjectIntPair<K> of(K left, int right) {
        return new ObjectIntImmutablePair<K>(left, right);
    }

    public static <K> Comparator<ObjectIntPair<K>> lexComparator() {
        return (x2, y2) -> {
            int t2 = ((Comparable)x2.left()).compareTo(y2.left());
            if (t2 != 0) {
                return t2;
            }
            return Integer.compare(x2.rightInt(), y2.rightInt());
        };
    }
}

