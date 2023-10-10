// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
import javax.annotation.CheckForNull;
import java.util.Random;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class Striped64 extends Number
{
    static final ThreadLocal<int[]> threadHashCode;
    static final Random rng;
    static final int NCPU;
    @CheckForNull
    transient volatile Cell[] cells;
    transient volatile long base;
    transient volatile int busy;
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    
    final boolean casBase(final long cmp, final long val) {
        return Striped64.UNSAFE.compareAndSwapLong(this, Striped64.baseOffset, cmp, val);
    }
    
    final boolean casBusy() {
        return Striped64.UNSAFE.compareAndSwapInt(this, Striped64.busyOffset, 0, 1);
    }
    
    abstract long fn(final long p0, final long p1);
    
    final void retryUpdate(final long x, @CheckForNull int[] hc, boolean wasUncontended) {
        int h;
        if (hc == null) {
            Striped64.threadHashCode.set(hc = new int[] { 0 });
            final int r = Striped64.rng.nextInt();
            final int[] array = hc;
            final int n2 = 0;
            final int n3 = (r == 0) ? 1 : r;
            array[n2] = n3;
            h = n3;
        }
        else {
            h = hc[0];
        }
        boolean collide = false;
        while (true) {
            final Cell[] as;
            final int n;
            if ((as = this.cells) != null && (n = as.length) > 0) {
                final Cell a;
                if ((a = as[n - 1 & h]) == null) {
                    if (this.busy == 0) {
                        final Cell r2 = new Cell(x);
                        if (this.busy == 0 && this.casBusy()) {
                            boolean created = false;
                            try {
                                final Cell[] rs;
                                final int m;
                                final int j;
                                if ((rs = this.cells) != null && (m = rs.length) > 0 && rs[j = (m - 1 & h)] == null) {
                                    rs[j] = r2;
                                    created = true;
                                }
                            }
                            finally {
                                this.busy = 0;
                            }
                            if (created) {
                                break;
                            }
                            continue;
                        }
                    }
                    collide = false;
                }
                else if (!wasUncontended) {
                    wasUncontended = true;
                }
                else {
                    final long v;
                    if (a.cas(v = a.value, this.fn(v, x))) {
                        break;
                    }
                    if (n >= Striped64.NCPU || this.cells != as) {
                        collide = false;
                    }
                    else if (!collide) {
                        collide = true;
                    }
                    else if (this.busy == 0 && this.casBusy()) {
                        try {
                            if (this.cells == as) {
                                final Cell[] rs2 = new Cell[n << 1];
                                for (int i = 0; i < n; ++i) {
                                    rs2[i] = as[i];
                                }
                                this.cells = rs2;
                            }
                        }
                        finally {
                            this.busy = 0;
                        }
                        collide = false;
                        continue;
                    }
                }
                h ^= h << 13;
                h ^= h >>> 17;
                h ^= h << 5;
                hc[0] = h;
            }
            else if (this.busy == 0 && this.cells == as && this.casBusy()) {
                boolean init = false;
                try {
                    if (this.cells == as) {
                        final Cell[] rs3 = new Cell[2];
                        rs3[h & 0x1] = new Cell(x);
                        this.cells = rs3;
                        init = true;
                    }
                }
                finally {
                    this.busy = 0;
                }
                if (init) {
                    break;
                }
                continue;
            }
            else {
                final long v;
                if (this.casBase(v = this.base, this.fn(v, x))) {
                    break;
                }
                continue;
            }
        }
    }
    
    final void internalReset(final long initialValue) {
        final Cell[] as = this.cells;
        this.base = initialValue;
        if (as != null) {
            for (final Cell a : as) {
                if (a != null) {
                    a.value = initialValue;
                }
            }
        }
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException ex) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Class<Unsafe> k = Unsafe.class;
                        for (final Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            final Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return k.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
    
    static {
        threadHashCode = new ThreadLocal<int[]>();
        rng = new Random();
        NCPU = Runtime.getRuntime().availableProcessors();
        try {
            UNSAFE = getUnsafe();
            final Class<?> sk = Striped64.class;
            baseOffset = Striped64.UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
            busyOffset = Striped64.UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }
    
    static final class Cell
    {
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
        
        Cell(final long x) {
            this.value = x;
        }
        
        final boolean cas(final long cmp, final long val) {
            return Cell.UNSAFE.compareAndSwapLong(this, Cell.valueOffset, cmp, val);
        }
        
        static {
            try {
                UNSAFE = getUnsafe();
                final Class<?> ak = Cell.class;
                valueOffset = Cell.UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
            }
            catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}
