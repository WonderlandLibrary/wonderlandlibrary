// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlinx.coroutines.scheduling.DefaultScheduler;

public final class Dispatchers
{
    private static final CoroutineDispatcher Default;
    
    public static final CoroutineDispatcher getDefault() {
        return Dispatchers.Default;
    }
    
    private Dispatchers() {
    }
    
    static {
        new Dispatchers();
        Default = CoroutineContextKt.createDefaultDispatcher();
        final Unconfined instance = Unconfined.INSTANCE;
        final DefaultScheduler instance2 = DefaultScheduler.INSTANCE;
    }
}
