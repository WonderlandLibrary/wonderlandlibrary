// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

public interface Continuation<T>
{
    CoroutineContext getContext();
    
    void resumeWith(final Object p0);
}
