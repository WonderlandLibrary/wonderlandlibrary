// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import java.lang.ref.ReferenceQueue;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.lang.ref.SoftReference;

@GwtIncompatible
public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference
{
    protected FinalizableSoftReference(final T referent, final FinalizableReferenceQueue queue) {
        super(referent, queue.queue);
        queue.cleanUp();
    }
}
