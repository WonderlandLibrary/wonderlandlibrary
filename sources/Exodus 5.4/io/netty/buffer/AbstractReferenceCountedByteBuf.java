/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf
extends AbstractByteBuf {
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;
    private volatile int refCnt = 1;

    protected final void setRefCnt(int n) {
        this.refCnt = n;
    }

    @Override
    public final boolean release() {
        int n;
        do {
            if ((n = this.refCnt) != 0) continue;
            throw new IllegalReferenceCountException(0, -1);
        } while (!refCntUpdater.compareAndSet(this, n, n - 1));
        if (n == 1) {
            this.deallocate();
            return true;
        }
        return false;
    }

    @Override
    public ByteBuf retain(int n) {
        int n2;
        if (n <= 0) {
            throw new IllegalArgumentException("increment: " + n + " (expected: > 0)");
        }
        do {
            if ((n2 = this.refCnt) == 0) {
                throw new IllegalReferenceCountException(0, n);
            }
            if (n2 <= Integer.MAX_VALUE - n) continue;
            throw new IllegalReferenceCountException(n2, n);
        } while (!refCntUpdater.compareAndSet(this, n2, n2 + n));
        return this;
    }

    @Override
    public ByteBuf retain() {
        int n;
        do {
            if ((n = this.refCnt) == 0) {
                throw new IllegalReferenceCountException(0, 1);
            }
            if (n != Integer.MAX_VALUE) continue;
            throw new IllegalReferenceCountException(Integer.MAX_VALUE, 1);
        } while (!refCntUpdater.compareAndSet(this, n, n + 1));
        return this;
    }

    protected abstract void deallocate();

    static {
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
        }
        refCntUpdater = atomicIntegerFieldUpdater;
    }

    protected AbstractReferenceCountedByteBuf(int n) {
        super(n);
    }

    @Override
    public final int refCnt() {
        return this.refCnt;
    }

    @Override
    public final boolean release(int n) {
        int n2;
        if (n <= 0) {
            throw new IllegalArgumentException("decrement: " + n + " (expected: > 0)");
        }
        do {
            if ((n2 = this.refCnt) >= n) continue;
            throw new IllegalReferenceCountException(n2, -n);
        } while (!refCntUpdater.compareAndSet(this, n2, n2 - n));
        if (n2 == n) {
            this.deallocate();
            return true;
        }
        return false;
    }
}

