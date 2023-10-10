// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

public final class EventLoop_commonKt
{
    private static final CoroutineContext.Element.DefaultImpls DISPOSED_TASK$4fdbb1f;
    private static final CoroutineContext.Element.DefaultImpls CLOSED_EMPTY$4fdbb1f;
    
    static {
        DISPOSED_TASK$4fdbb1f = new CoroutineContext.Element.DefaultImpls("REMOVED_TASK");
        CLOSED_EMPTY$4fdbb1f = new CoroutineContext.Element.DefaultImpls("CLOSED_EMPTY");
    }
}
