// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;

public final class AtomicKt
{
    private static final Object NO_DECISION;
    
    static {
        NO_DECISION = new CoroutineContext.Element.DefaultImpls("NO_DECISION");
    }
}
