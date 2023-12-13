/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.cache.LongAddable;
import com.google.common.cache.Striped64;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@GwtCompatible(emulated=true)
final class LongAdder
extends Striped64
implements Serializable,
LongAddable {
    private static final long serialVersionUID = 7249069246863182397L;

    @Override
    final long fn(long v2, long x2) {
        return v2 + x2;
    }

    @Override
    public void add(long x2) {
        long b2;
        Striped64.Cell[] as = this.cells;
        if (this.cells != null || !this.casBase(b2 = this.base, b2 + x2)) {
            long v2;
            Striped64.Cell a2;
            int n2;
            boolean uncontended = true;
            Striped64.HashCode hc = (Striped64.HashCode)threadHashCode.get();
            int h2 = hc.code;
            if (as == null || (n2 = as.length) < 1 || (a2 = as[n2 - 1 & h2]) == null || !(uncontended = a2.cas(v2 = a2.value, v2 + x2))) {
                this.retryUpdate(x2, hc, uncontended);
            }
        }
    }

    @Override
    public void increment() {
        this.add(1L);
    }

    public void decrement() {
        this.add(-1L);
    }

    @Override
    public long sum() {
        long sum = this.base;
        Striped64.Cell[] as = this.cells;
        if (as != null) {
            for (Striped64.Cell a2 : as) {
                if (a2 == null) continue;
                sum += a2.value;
            }
        }
        return sum;
    }

    public void reset() {
        this.internalReset(0L);
    }

    public long sumThenReset() {
        long sum = this.base;
        Striped64.Cell[] as = this.cells;
        this.base = 0L;
        if (as != null) {
            for (Striped64.Cell a2 : as) {
                if (a2 == null) continue;
                sum += a2.value;
                a2.value = 0L;
            }
        }
        return sum;
    }

    public String toString() {
        return Long.toString(this.sum());
    }

    @Override
    public long longValue() {
        return this.sum();
    }

    @Override
    public int intValue() {
        return (int)this.sum();
    }

    @Override
    public float floatValue() {
        return this.sum();
    }

    @Override
    public double doubleValue() {
        return this.sum();
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        s2.writeLong(this.sum());
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.busy = 0;
        this.cells = null;
        this.base = s2.readLong();
    }
}

