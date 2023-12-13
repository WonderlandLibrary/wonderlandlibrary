/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64
extends Number {
    static final ThreadHashCode threadHashCode = new ThreadHashCode();
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    volatile transient Cell[] cells;
    volatile transient long base;
    volatile transient int busy;
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;

    Striped64() {
    }

    final boolean casBase(long cmp, long val2) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val2);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    abstract long fn(long var1, long var3);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final void retryUpdate(long x2, HashCode hc, boolean wasUncontended) {
        int h2 = hc.code;
        boolean collide = false;
        while (true) {
            long v2;
            int n2;
            Cell[] as = this.cells;
            if (this.cells != null && (n2 = as.length) > 0) {
                Cell a2 = as[n2 - 1 & h2];
                if (a2 == null) {
                    if (this.busy == 0) {
                        Cell r2 = new Cell(x2);
                        if (this.busy == 0 && this.casBusy()) {
                            boolean created = false;
                            try {
                                int j2;
                                int m2;
                                Cell[] rs = this.cells;
                                if (this.cells != null && (m2 = rs.length) > 0 && rs[j2 = m2 - 1 & h2] == null) {
                                    rs[j2] = r2;
                                    created = true;
                                }
                            }
                            finally {
                                this.busy = 0;
                            }
                            if (!created) continue;
                            break;
                        }
                    }
                    collide = false;
                } else if (!wasUncontended) {
                    wasUncontended = true;
                } else {
                    v2 = a2.value;
                    if (a2.cas(v2, this.fn(v2, x2))) break;
                    if (n2 >= NCPU || this.cells != as) {
                        collide = false;
                    } else if (!collide) {
                        collide = true;
                    } else if (this.busy == 0 && this.casBusy()) {
                        try {
                            if (this.cells == as) {
                                Cell[] rs = new Cell[n2 << 1];
                                for (int i2 = 0; i2 < n2; ++i2) {
                                    rs[i2] = as[i2];
                                }
                                this.cells = rs;
                            }
                        }
                        finally {
                            this.busy = 0;
                        }
                        collide = false;
                        continue;
                    }
                }
                h2 ^= h2 << 13;
                h2 ^= h2 >>> 17;
                h2 ^= h2 << 5;
                continue;
            }
            if (this.busy == 0 && this.cells == as && this.casBusy()) {
                boolean init = false;
                try {
                    if (this.cells == as) {
                        Cell[] rs = new Cell[2];
                        rs[h2 & 1] = new Cell(x2);
                        this.cells = rs;
                        init = true;
                    }
                }
                finally {
                    this.busy = 0;
                }
                if (!init) continue;
                break;
            }
            v2 = this.base;
            if (this.casBase(v2, this.fn(v2, x2))) break;
        }
        hc.code = h2;
    }

    final void internalReset(long initialValue) {
        Cell[] as = this.cells;
        this.base = initialValue;
        if (as != null) {
            for (Cell a2 : as) {
                if (a2 == null) continue;
                a2.value = initialValue;
            }
        }
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                    @Override
                    public Unsafe run() throws Exception {
                        Class<Unsafe> k2 = Unsafe.class;
                        for (Field f2 : k2.getDeclaredFields()) {
                            f2.setAccessible(true);
                            Object x2 = f2.get(null);
                            if (!k2.isInstance(x2)) continue;
                            return (Unsafe)k2.cast(x2);
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
    }

    static {
        try {
            UNSAFE = Striped64.getUnsafe();
            Class<Striped64> sk = Striped64.class;
            baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
        }
        catch (Exception e2) {
            throw new Error(e2);
        }
    }

    static final class ThreadHashCode
    extends ThreadLocal<HashCode> {
        ThreadHashCode() {
        }

        @Override
        public HashCode initialValue() {
            return new HashCode();
        }
    }

    static final class HashCode {
        static final Random rng = new Random();
        int code;

        HashCode() {
            int h2 = rng.nextInt();
            this.code = h2 == 0 ? 1 : h2;
        }
    }

    static final class Cell {
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long value;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        private static final Unsafe UNSAFE;
        private static final long valueOffset;

        Cell(long x2) {
            this.value = x2;
        }

        final boolean cas(long cmp, long val2) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val2);
        }

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                Class<Cell> ak = Cell.class;
                valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
            }
            catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }
}

