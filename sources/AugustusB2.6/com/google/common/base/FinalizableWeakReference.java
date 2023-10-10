// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.WeakReference;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference
{
    protected FinalizableWeakReference(@CheckForNull final T referent, final FinalizableReferenceQueue queue) {
        super(referent, queue.queue);
        queue.cleanUp();
    }
}
