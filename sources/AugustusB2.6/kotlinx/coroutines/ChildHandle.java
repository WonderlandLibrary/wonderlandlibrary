// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

public interface ChildHandle extends DisposableHandle
{
    boolean childCancelled(final Throwable p0);
}
