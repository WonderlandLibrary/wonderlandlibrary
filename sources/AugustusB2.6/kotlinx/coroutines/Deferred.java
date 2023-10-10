// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

public interface Deferred<T> extends Job
{
    T getCompleted();
}
