/*
 * Decompiled with CFR 0.150.
 */
package com.sun.util.reentrant;

import com.sun.util.reentrant.ReentrantContext;
import com.sun.util.reentrant.ReentrantContextProvider;
import com.sun.util.reentrant.ReentrantContextProviderCLQ;
import java.lang.ref.Reference;

public abstract class ReentrantContextProviderTL<K extends ReentrantContext>
extends ReentrantContextProvider<K> {
    private final ThreadLocal<Reference<K>> ctxTL = new ThreadLocal();
    private final ReentrantContextProviderCLQ<K> ctxProviderCLQ;

    public ReentrantContextProviderTL(int n) {
        this(n, 2);
    }

    public ReentrantContextProviderTL(int n, int n2) {
        super(n);
        final ReentrantContextProviderTL reentrantContextProviderTL = this;
        this.ctxProviderCLQ = new ReentrantContextProviderCLQ<K>(n2){

            @Override
            protected K newContext() {
                return reentrantContextProviderTL.newContext();
            }
        };
    }

    @Override
    public final K acquire() {
        ReentrantContext reentrantContext = null;
        Reference<K> reference = this.ctxTL.get();
        if (reference != null) {
            reentrantContext = (ReentrantContext)reference.get();
        }
        if (reentrantContext == null) {
            reentrantContext = this.newContext();
            this.ctxTL.set(this.getOrCreateReference(reentrantContext));
        }
        if (reentrantContext.usage == 0) {
            reentrantContext.usage = 1;
        } else {
            reentrantContext = this.ctxProviderCLQ.acquire();
        }
        return (K)reentrantContext;
    }

    @Override
    public final void release(K k) {
        if (((ReentrantContext)k).usage == 1) {
            ((ReentrantContext)k).usage = 0;
        } else {
            this.ctxProviderCLQ.release(k);
        }
    }
}

