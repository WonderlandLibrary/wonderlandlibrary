// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

public interface ThreadContextElement<S> extends Element
{
    S updateThreadContext(final CoroutineContext p0);
    
    void restoreThreadContext(final CoroutineContext p0, final S p1);
}
