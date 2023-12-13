/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectObjectImmutablePair;
import java.util.Comparator;

public interface Pair<L, R> {
    public L left();

    public R right();

    default public Pair<L, R> left(L l2) {
        throw new UnsupportedOperationException();
    }

    default public Pair<L, R> right(R r2) {
        throw new UnsupportedOperationException();
    }

    default public L first() {
        return this.left();
    }

    default public R second() {
        return this.right();
    }

    default public Pair<L, R> first(L l2) {
        return this.left(l2);
    }

    default public Pair<L, R> second(R r2) {
        return this.right(r2);
    }

    default public Pair<L, R> key(L l2) {
        return this.left(l2);
    }

    default public Pair<L, R> value(R r2) {
        return this.right(r2);
    }

    default public L key() {
        return this.left();
    }

    default public R value() {
        return this.right();
    }

    public static <L, R> Pair<L, R> of(L l2, R r2) {
        return new ObjectObjectImmutablePair<L, R>(l2, r2);
    }

    public static <L, R> Comparator<Pair<L, R>> lexComparator() {
        return (x2, y2) -> {
            int t2 = ((Comparable)x2.left()).compareTo(y2.left());
            if (t2 != 0) {
                return t2;
            }
            return ((Comparable)x2.right()).compareTo(y2.right());
        };
    }
}

