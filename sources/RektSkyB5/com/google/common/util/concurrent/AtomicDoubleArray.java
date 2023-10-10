/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicDoubleArray
implements Serializable {
    private static final long serialVersionUID = 0L;
    private transient AtomicLongArray longs;

    public AtomicDoubleArray(int length) {
        this.longs = new AtomicLongArray(length);
    }

    public AtomicDoubleArray(double[] array) {
        int len = array.length;
        long[] longArray = new long[len];
        for (int i2 = 0; i2 < len; ++i2) {
            longArray[i2] = Double.doubleToRawLongBits(array[i2]);
        }
        this.longs = new AtomicLongArray(longArray);
    }

    public final int length() {
        return this.longs.length();
    }

    public final double get(int i2) {
        return Double.longBitsToDouble(this.longs.get(i2));
    }

    public final void set(int i2, double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        this.longs.set(i2, next);
    }

    public final void lazySet(int i2, double newValue) {
        this.set(i2, newValue);
    }

    public final double getAndSet(int i2, double newValue) {
        long next = Double.doubleToRawLongBits(newValue);
        return Double.longBitsToDouble(this.longs.getAndSet(i2, next));
    }

    public final boolean compareAndSet(int i2, double expect, double update) {
        return this.longs.compareAndSet(i2, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final boolean weakCompareAndSet(int i2, double expect, double update) {
        return this.longs.weakCompareAndSet(i2, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }

    public final double getAndAdd(int i2, double delta) {
        double currentVal;
        double nextVal;
        long next;
        long current;
        while (!this.longs.compareAndSet(i2, current = this.longs.get(i2), next = Double.doubleToRawLongBits(nextVal = (currentVal = Double.longBitsToDouble(current)) + delta))) {
        }
        return currentVal;
    }

    public double addAndGet(int i2, double delta) {
        double currentVal;
        double nextVal;
        long next;
        long current;
        while (!this.longs.compareAndSet(i2, current = this.longs.get(i2), next = Double.doubleToRawLongBits(nextVal = (currentVal = Double.longBitsToDouble(current)) + delta))) {
        }
        return nextVal;
    }

    public String toString() {
        int iMax = this.length() - 1;
        if (iMax == -1) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder(19 * (iMax + 1));
        b2.append('[');
        int i2 = 0;
        while (true) {
            b2.append(Double.longBitsToDouble(this.longs.get(i2)));
            if (i2 == iMax) {
                return b2.append(']').toString();
            }
            b2.append(',').append(' ');
            ++i2;
        }
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        int length = this.length();
        s2.writeInt(length);
        for (int i2 = 0; i2 < length; ++i2) {
            s2.writeDouble(this.get(i2));
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        int length = s2.readInt();
        this.longs = new AtomicLongArray(length);
        for (int i2 = 0; i2 < length; ++i2) {
            this.set(i2, s2.readDouble());
        }
    }
}

