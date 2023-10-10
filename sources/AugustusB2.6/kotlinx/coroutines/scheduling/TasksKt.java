// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import java.util.concurrent.TimeUnit;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

public final class TasksKt
{
    public static final long WORK_STEALING_TIME_RESOLUTION_NS;
    public static final int QUEUE_SIZE_OFFLOAD_THRESHOLD;
    public static final int CORE_POOL_SIZE;
    public static final int MAX_POOL_SIZE;
    public static final long IDLE_WORKER_KEEP_ALIVE_NS;
    public static TimeSource schedulerTimeSource;
    
    static {
        WORK_STEALING_TIME_RESOLUTION_NS = LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, null);
        QUEUE_SIZE_OFFLOAD_THRESHOLD = LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.offload.threshold", 96, 0, 128, 4, null);
        LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.blocking.parallelism", 16, 0, 0, 12, null);
        CORE_POOL_SIZE = LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", RangesKt___RangesKt.coerceAtLeast(LockFreeTaskQueueCore.Companion.getAVAILABLE_PROCESSORS(), 2), 1, 0, 8, null);
        final String s = "kotlinx.coroutines.scheduler.max.pool.size";
        final int n = LockFreeTaskQueueCore.Companion.getAVAILABLE_PROCESSORS() << 7;
        final int core_POOL_SIZE = TasksKt.CORE_POOL_SIZE;
        final int n2 = n;
        if (core_POOL_SIZE > 2097150) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + 2097150 + " is less than minimum " + core_POOL_SIZE + '.');
        }
        MAX_POOL_SIZE = LockFreeTaskQueueCore.Companion.systemProp$default(s, (n2 < core_POOL_SIZE) ? core_POOL_SIZE : ((n2 > 2097150) ? 2097150 : n2), 0, 2097150, 4, null);
        IDLE_WORKER_KEEP_ALIVE_NS = TimeUnit.SECONDS.toNanos(LockFreeTaskQueueCore.Companion.systemProp$default("kotlinx.coroutines.scheduler.keep.alive.sec", 5L, 0L, 0L, 12, null));
        TasksKt.schedulerTimeSource = NanoTimeSource.INSTANCE;
    }
}
