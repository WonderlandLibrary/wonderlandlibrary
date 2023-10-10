// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;

final class ThreadState
{
    private Object[] a;
    private int i;
    private final CoroutineContext context;
    
    public final void append(final Object value) {
        final Object[] a = this.a;
        final int i;
        this.i = (i = this.i) + 1;
        a[i] = value;
    }
    
    public final Object take() {
        final Object[] a = this.a;
        final int i;
        this.i = (i = this.i) + 1;
        return a[i];
    }
    
    public final void start() {
        this.i = 0;
    }
    
    public final CoroutineContext getContext() {
        return this.context;
    }
    
    public ThreadState(final CoroutineContext context, final int n) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.context = context;
        this.a = new Object[n];
    }
}
