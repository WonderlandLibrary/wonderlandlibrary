/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCounted
implements ReferenceCounted {
    private volatile int refCnt = 1;
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater;

    protected final void setRefCnt(int n) {
        this.refCnt = n;
    }

    static {
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCounted.class, "refCnt");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");
        }
        refCntUpdater = atomicIntegerFieldUpdater;
    }

    @Override
    public final int refCnt() {
        return this.refCnt;
    }

    @Override
    public ReferenceCounted retain(int n) {
        int n2;
        if (n <= 0) {
            throw new IllegalArgumentException("increment: " + n + " (expected: > 0)");
        }
        do {
            if ((n2 = this.refCnt) == 0) {
                throw new IllegalReferenceCountException(0, 1);
            }
            if (n2 <= Integer.MAX_VALUE - n) continue;
            throw new IllegalReferenceCountException(n2, n);
        } while (!refCntUpdater.compareAndSet(this, n2, n2 + n));
        return this;
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

    protected abstract void deallocate();

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
    public ReferenceCounted retain() {
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
}

