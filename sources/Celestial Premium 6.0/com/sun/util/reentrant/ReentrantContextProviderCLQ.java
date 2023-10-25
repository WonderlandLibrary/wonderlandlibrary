/*
 * Decompiled with CFR 0.150.
 */
package com.sun.util.reentrant;

import com.sun.util.reentrant.ReentrantContext;
import com.sun.util.reentrant.ReentrantContextProvider;
import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ReentrantContextProviderCLQ<K extends ReentrantContext>
extends ReentrantContextProvider<K> {
    private final ConcurrentLinkedQueue<Reference<K>> ctxQueue = new ConcurrentLinkedQueue();

    public ReentrantContextProviderCLQ(int n) {
        super(n);
    }

    @Override
    public final K acquire() {
        ReentrantContext reentrantContext = null;
        Reference<K> reference = null;
        while (reentrantContext == null && (reference = this.ctxQueue.poll()) != null) {
            reentrantContext = (ReentrantContext)reference.get();
        }
        if (reentrantContext == null) {
            reentrantContext = (ReentrantContext)this.newContext();
            reentrantContext.usage = (byte)2;
        }
        return (K)reentrantContext;
    }

    @Override
    public final void release(K k) {
        if (((ReentrantContext)k).usage == 2) {
            this.ctxQueue.offer(this.getOrCreateReference(k));
        }
    }
}

