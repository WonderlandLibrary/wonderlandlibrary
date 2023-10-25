/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;
import com.sun.prism.impl.ResourcePool;
import java.util.ArrayList;

public abstract class ManagedResource<T>
implements GraphicsResource {
    static final boolean trackLockSources = false;
    protected T resource;
    private final ResourcePool<T> pool;
    private int lockcount;
    private int employcount;
    ArrayList<Throwable> lockedFrom;
    private boolean permanent;
    private boolean mismatchDetected;
    private boolean disposalRequested;
    private int age;

    static boolean _isgone(ManagedResource<?> managedResource) {
        if (managedResource == null) {
            return true;
        }
        if (managedResource.disposalRequested) {
            managedResource.free();
            managedResource.resource = null;
            managedResource.disposalRequested = false;
            return true;
        }
        return !managedResource.isValid();
    }

    protected ManagedResource(T t, ResourcePool<T> resourcePool) {
        this.resource = t;
        this.pool = resourcePool;
        this.manage();
        this.lock();
    }

    private void manage() {
        this.pool.resourceManaged(this);
    }

    public final T getResource() {
        this.assertLocked();
        return this.resource;
    }

    public final ResourcePool<T> getPool() {
        return this.pool;
    }

    public boolean isValid() {
        return this.resource != null && !this.disposalRequested;
    }

    public boolean isDisposalRequested() {
        return this.disposalRequested;
    }

    public final boolean isLocked() {
        return this.lockcount > 0;
    }

    public final int getLockCount() {
        return this.lockcount;
    }

    public final void assertLocked() {
        if (this.lockcount <= 0) {
            throw new IllegalStateException("Operation requires resource lock");
        }
    }

    public final boolean isPermanent() {
        return this.permanent;
    }

    public final boolean isInteresting() {
        return this.employcount > 0;
    }

    public final int getInterestCount() {
        return this.employcount;
    }

    public void free() {
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public final void dispose() {
        if (this.pool.isManagerThread()) {
            T t = this.resource;
            if (t != null) {
                this.free();
                this.disposalRequested = false;
                this.resource = null;
                this.pool.resourceFreed(this);
            }
        } else {
            this.disposalRequested = true;
        }
    }

    public final void makePermanent() {
        this.assertLocked();
        this.permanent = true;
    }

    public final T lock() {
        ++this.lockcount;
        this.age = 0;
        return this.resource;
    }

    void unlockall() {
        this.lockcount = 0;
    }

    public final void unlock() {
        this.assertLocked();
        --this.lockcount;
    }

    public final void contentsUseful() {
        this.assertLocked();
        ++this.employcount;
    }

    public final void contentsNotUseful() {
        if (this.employcount <= 0) {
            throw new IllegalStateException("Resource obsoleted too many times");
        }
        --this.employcount;
    }

    public final boolean wasMismatched() {
        return this.mismatchDetected;
    }

    public final void setMismatched() {
        this.mismatchDetected = true;
    }

    public final void bumpAge(int n) {
        int n2 = this.age;
        if (n2 < n) {
            this.age = n2 + 1;
        }
    }
}

