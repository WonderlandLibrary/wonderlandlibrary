/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Buffer;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class StripedBuffer<E>
implements Buffer<E> {
    static final long TABLE_BUSY = UnsafeAccess.objectFieldOffset(StripedBuffer.class, "tableBusy");
    static final long PROBE = UnsafeAccess.objectFieldOffset(Thread.class, "threadLocalRandomProbe");
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int MAXIMUM_TABLE_SIZE = 4 * Caffeine.ceilingPowerOfTwo(NCPU);
    static final int ATTEMPTS = 3;
    volatile transient Buffer<E> @Nullable [] table;
    volatile transient int tableBusy;

    StripedBuffer() {
    }

    final boolean casTableBusy() {
        return UnsafeAccess.UNSAFE.compareAndSwapInt(this, TABLE_BUSY, 0, 1);
    }

    static final int getProbe() {
        return UnsafeAccess.UNSAFE.getInt((Object)Thread.currentThread(), PROBE);
    }

    static final int advanceProbe(int probe) {
        probe ^= probe << 13;
        probe ^= probe >>> 17;
        probe ^= probe << 5;
        UnsafeAccess.UNSAFE.putInt((Object)Thread.currentThread(), PROBE, probe);
        return probe;
    }

    protected abstract Buffer<E> create(E var1);

    @Override
    public int offer(E e) {
        Buffer<E> buffer;
        int mask;
        int result = 0;
        boolean uncontended = true;
        Buffer<E>[] buffers = this.table;
        if (buffers == null || (mask = buffers.length - 1) < 0 || (buffer = buffers[StripedBuffer.getProbe() & mask]) == null || !(uncontended = (result = buffer.offer(e)) != -1)) {
            this.expandOrRetry(e, uncontended);
        }
        return result;
    }

    @Override
    public void drainTo(Consumer<E> consumer) {
        Buffer<E>[] buffers = this.table;
        if (buffers == null) {
            return;
        }
        for (Buffer<E> buffer : buffers) {
            if (buffer == null) continue;
            buffer.drainTo(consumer);
        }
    }

    @Override
    public int reads() {
        Buffer<E>[] buffers = this.table;
        if (buffers == null) {
            return 0;
        }
        int reads = 0;
        for (Buffer<E> buffer : buffers) {
            if (buffer == null) continue;
            reads += buffer.reads();
        }
        return reads;
    }

    @Override
    public int writes() {
        Buffer<E>[] buffers = this.table;
        if (buffers == null) {
            return 0;
        }
        int writes = 0;
        for (Buffer<E> buffer : buffers) {
            if (buffer == null) continue;
            writes += buffer.writes();
        }
        return writes;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final void expandOrRetry(E e, boolean wasUncontended) {
        int h = StripedBuffer.getProbe();
        if (h == 0) {
            ThreadLocalRandom.current();
            h = StripedBuffer.getProbe();
            wasUncontended = true;
        }
        boolean collide = false;
        for (int attempt = 0; attempt < 3; ++attempt) {
            Buffer[] rs;
            int n;
            Buffer<E>[] buffers = this.table;
            if (this.table != null && (n = buffers.length) > 0) {
                Buffer<E> buffer = buffers[n - 1 & h];
                if (buffer == null) {
                    if (this.tableBusy == 0 && this.casTableBusy()) {
                        boolean created = false;
                        try {
                            int j;
                            int mask;
                            rs = this.table;
                            if (this.table != null && (mask = rs.length) > 0 && rs[j = mask - 1 & h] == null) {
                                rs[j] = this.create(e);
                                created = true;
                            }
                        }
                        finally {
                            this.tableBusy = 0;
                        }
                        if (!created) continue;
                        break;
                    }
                    collide = false;
                } else if (!wasUncontended) {
                    wasUncontended = true;
                } else {
                    if (buffer.offer(e) != -1) break;
                    if (n >= MAXIMUM_TABLE_SIZE || this.table != buffers) {
                        collide = false;
                    } else if (!collide) {
                        collide = true;
                    } else if (this.tableBusy == 0 && this.casTableBusy()) {
                        try {
                            if (this.table == buffers) {
                                this.table = Arrays.copyOf(buffers, n << 1);
                            }
                        }
                        finally {
                            this.tableBusy = 0;
                        }
                        collide = false;
                        continue;
                    }
                }
                h = StripedBuffer.advanceProbe(h);
                continue;
            }
            if (this.tableBusy != 0 || this.table != buffers || !this.casTableBusy()) continue;
            boolean init = false;
            try {
                if (this.table == buffers) {
                    rs = new Buffer[]{this.create(e)};
                    this.table = rs;
                    init = true;
                }
            }
            finally {
                this.tableBusy = 0;
            }
            if (init) break;
        }
    }
}

