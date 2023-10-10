// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.CoroutineDispatcher;

public final class DefaultScheduler extends ExperimentalCoroutineDispatcher
{
    private static final CoroutineDispatcher IO;
    public static final DefaultScheduler INSTANCE;
    
    @Override
    public final void close() {
        throw new UnsupportedOperationException("DefaultDispatcher cannot be closed");
    }
    
    @Override
    public final String toString() {
        return "DefaultDispatcher";
    }
    
    private DefaultScheduler() {
        super(0, 0, null, 7);
    }
    
    static {
        final DefaultScheduler defaultScheduler = INSTANCE = new DefaultScheduler();
        final int systemProp$default = LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.io.parallelism", RangesKt___RangesKt.coerceAtLeast(64, LockFreeTaskQueueCore.Companion.getAVAILABLE_PROCESSORS()), 0, 0, 12, null);
        final DefaultScheduler dispatcher = defaultScheduler;
        final boolean b;
        if (!(b = (systemProp$default > 0))) {
            throw new IllegalArgumentException(("Expected positive parallelism level, but have " + systemProp$default).toString());
        }
        IO = new LimitingDispatcher(dispatcher, systemProp$default, TaskMode.PROBABLY_BLOCKING);
    }
}
