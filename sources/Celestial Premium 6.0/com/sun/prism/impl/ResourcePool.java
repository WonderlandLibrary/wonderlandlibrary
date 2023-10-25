/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.ManagedResource;

public interface ResourcePool<T> {
    public void freeDisposalRequestedAndCheckResources(boolean var1);

    public boolean isManagerThread();

    public long used();

    public long managed();

    public long max();

    public long target();

    public long origTarget();

    public void setTarget(long var1);

    public long size(T var1);

    public void recordAllocated(long var1);

    public void recordFree(long var1);

    public void resourceManaged(ManagedResource<T> var1);

    public void resourceFreed(ManagedResource<T> var1);

    public boolean prepareForAllocation(long var1);
}

