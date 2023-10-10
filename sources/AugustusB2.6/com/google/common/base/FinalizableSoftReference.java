// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.SoftReference;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference
{
    protected FinalizableSoftReference(@CheckForNull final T referent, final FinalizableReferenceQueue queue) {
        super(referent, queue.queue);
        queue.cleanUp();
    }
}
