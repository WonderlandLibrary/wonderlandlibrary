/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

public final class PendingWrite {
    private static final Recycler<PendingWrite> RECYCLER = new Recycler<PendingWrite>(){

        @Override
        protected PendingWrite newObject(Recycler.Handle handle) {
            return new PendingWrite(handle);
        }
    };
    private Object msg;
    private Promise<Void> promise;
    private final Recycler.Handle handle;

    public Promise<Void> promise() {
        return this.promise;
    }

    private PendingWrite(Recycler.Handle handle) {
        this.handle = handle;
    }

    public Object msg() {
        return this.msg;
    }

    public boolean successAndRecycle() {
        if (this.promise != null) {
            this.promise.setSuccess(null);
        }
        return this.recycle();
    }

    public boolean recycle() {
        this.msg = null;
        this.promise = null;
        return RECYCLER.recycle(this, this.handle);
    }

    public boolean failAndRecycle(Throwable throwable) {
        ReferenceCountUtil.release(this.msg);
        if (this.promise != null) {
            this.promise.setFailure(throwable);
        }
        return this.recycle();
    }

    public static PendingWrite newInstance(Object object, Promise<Void> promise) {
        PendingWrite pendingWrite = RECYCLER.get();
        pendingWrite.msg = object;
        pendingWrite.promise = promise;
        return pendingWrite;
    }

    public Promise<Void> recycleAndGet() {
        Promise<Void> promise = this.promise;
        this.recycle();
        return promise;
    }
}

