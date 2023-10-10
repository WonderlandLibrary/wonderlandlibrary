// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.CoroutineContext;

public final class GlobalScope implements CoroutineScope
{
    public static final GlobalScope INSTANCE;
    
    @Override
    public final CoroutineContext getCoroutineContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
    
    private GlobalScope() {
    }
    
    static {
        INSTANCE = new GlobalScope();
    }
}
