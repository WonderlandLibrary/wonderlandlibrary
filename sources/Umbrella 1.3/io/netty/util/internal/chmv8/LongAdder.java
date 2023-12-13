/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.Striped64;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LongAdder
extends Striped64
implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    @Override
    final long fn(long v, long x) {
        return v + x;
    }

    public void add(long x) {
        long b;
        Striped64.Cell[] as = this.cells;
        if (this.cells != null || !this.casBase(b = this.base, b + x)) {
            long v;
            Striped64.Cell a;
            int n;
            boolean uncontended = true;
            Striped64.HashCode hc = (Striped64.HashCode)threadHashCode.get();
            int h = hc.code;
            if (as == null || (n = as.length) < 1 || (a = as[n - 1 & h]) == null || !(uncontended = a.cas(v = a.value, v + x))) {
                this.retryUpdate(x, hc, uncontended);
            }
        }
    }

    public void increment() {
        this.add(1L);
    }

    public void decrement() {
        this.add(-1L);
    }

    public long sum() {
        long sum = this.base;
        Striped64.Cell[] as = this.cells;
        if (as != null) {
            for (Striped64.Cell a : as) {
                if (a == null) continue;
                sum += a.value;
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
            for (Striped64.Cell a : as) {
                if (a == null) continue;
                sum += a.value;
                a.value = 0L;
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

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(this.sum());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.busy = 0;
        this.cells = null;
        this.base = s.readLong();
    }
}

