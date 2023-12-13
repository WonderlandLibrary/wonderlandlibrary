/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Pair;
import com.viaversion.viaversion.libs.fastutil.ints.IntIntImmutablePair;
import java.util.Comparator;

public interface IntIntPair
extends Pair<Integer, Integer> {
    public int leftInt();

    @Override
    @Deprecated
    default public Integer left() {
        return this.leftInt();
    }

    default public IntIntPair left(int l2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public IntIntPair left(Integer l2) {
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

    default public IntIntPair first(int l2) {
        return this.left(l2);
    }

    @Deprecated
    default public IntIntPair first(Integer l2) {
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

    default public IntIntPair key(int l2) {
        return this.left(l2);
    }

    @Deprecated
    default public IntIntPair key(Integer l2) {
        return this.key((int)l2);
    }

    public int rightInt();

    @Override
    @Deprecated
    default public Integer right() {
        return this.rightInt();
    }

    default public IntIntPair right(int r2) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public IntIntPair right(Integer l2) {
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

    default public IntIntPair second(int r2) {
        return this.right(r2);
    }

    @Deprecated
    default public IntIntPair second(Integer l2) {
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

    default public IntIntPair value(int r2) {
        return this.right(r2);
    }

    @Deprecated
    default public IntIntPair value(Integer l2) {
        return this.value((int)l2);
    }

    public static IntIntPair of(int left, int right) {
        return new IntIntImmutablePair(left, right);
    }

    public static Comparator<IntIntPair> lexComparator() {
        return (x2, y2) -> {
            int t2 = Integer.compare(x2.leftInt(), y2.leftInt());
            if (t2 != 0) {
                return t2;
            }
            return Integer.compare(x2.rightInt(), y2.rightInt());
        };
    }
}

