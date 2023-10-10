// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import java.util.concurrent.RejectedExecutionException;
import kotlin.coroutines.CoroutineContext;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.Intrinsics;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import kotlin.ranges.RangesKt___RangesKt;
import java.util.concurrent.Executor;

public final class CommonPool extends ExecutorCoroutineDispatcher
{
    private static final int requestedParallelism;
    private static volatile Executor pool;
    public static final CommonPool INSTANCE;
    
    private static int getParallelism() {
        final Integer value;
        final Integer n = ((value = CommonPool.requestedParallelism).intValue() > 0) ? value : null;
        if (n != null) {
            return n;
        }
        return RangesKt___RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
    }
    
    private final ExecutorService createPool() {
        if (System.getSecurityManager() != null) {
            return this.createPlainPool();
        }
        Class<?> forName;
        try {
            forName = Class.forName("java.util.concurrent.ForkJoinPool");
        }
        catch (Throwable t) {
            forName = null;
        }
        final Class<?> clazz = forName;
        if (clazz == null) {
            return this.createPlainPool();
        }
        final Class fjpClass = clazz;
        if (CommonPool.requestedParallelism < 0) {
            ExecutorService executorService;
            try {
                final Method method = fjpClass.getMethod("commonPool", (Class[])new Class[0]);
                Object o = (method != null) ? method.invoke(null, new Object[0]) : null;
                if (!(o instanceof ExecutorService)) {
                    o = null;
                }
                executorService = (ExecutorService)o;
            }
            catch (Throwable t2) {
                executorService = null;
            }
            final ExecutorService executorService2 = executorService;
            if (executorService2 != null) {
                final ExecutorService executorService3 = executorService2;
                final ExecutorService it = executorService2;
                ExecutorService executorService4 = null;
                Object o2 = null;
                if (CommonPool.INSTANCE.isGoodCommonPool$kotlinx_coroutines_core(fjpClass, it)) {
                    executorService4 = executorService3;
                }
                else {
                    o2 = null;
                }
                if (o2 != null) {
                    return executorService4;
                }
            }
        }
        ExecutorService executorService5;
        try {
            ExecutorService instance;
            if (!((instance = fjpClass.getConstructor(Integer.TYPE).newInstance(getParallelism())) instanceof ExecutorService)) {
                instance = null;
            }
            executorService5 = instance;
        }
        catch (Throwable t3) {
            executorService5 = null;
        }
        final ExecutorService executorService6 = executorService5;
        if (executorService6 != null) {
            return executorService6;
        }
        return this.createPlainPool();
    }
    
    private boolean isGoodCommonPool$kotlinx_coroutines_core(final Class<?> fjpClass, final ExecutorService executor) {
        Intrinsics.checkParameterIsNotNull(fjpClass, "fjpClass");
        Intrinsics.checkParameterIsNotNull(executor, "executor");
        executor.submit((Runnable)CommonPool$isGoodCommonPool.CommonPool$isGoodCommonPool$1.INSTANCE);
        Integer n;
        try {
            Object invoke;
            if (!((invoke = fjpClass.getMethod("getPoolSize", (Class[])new Class[0]).invoke(executor, new Object[0])) instanceof Integer)) {
                invoke = null;
            }
            n = (Integer)invoke;
        }
        catch (Throwable t) {
            n = null;
        }
        final Integer n2 = n;
        return n2 != null && n2 > 0;
    }
    
    private final ExecutorService createPlainPool() {
        final AtomicInteger threadId = new AtomicInteger();
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(getParallelism(), (ThreadFactory)new CommonPool$createPlainPool.CommonPool$createPlainPool$1(threadId));
        Intrinsics.checkExpressionValueIsNotNull(fixedThreadPool, "Executors.newFixedThread\u2026Daemon = true }\n        }");
        return fixedThreadPool;
    }
    
    private final synchronized Executor getOrCreatePoolSync() {
        Executor pool;
        if ((pool = CommonPool.pool) == null) {
            final ExecutorService pool2;
            CommonPool.pool = (pool2 = this.createPool());
            pool = pool2;
        }
        return pool;
    }
    
    @Override
    public final void dispatch(final CoroutineContext context, final Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            Executor executor;
            if ((executor = CommonPool.pool) == null) {
                executor = this.getOrCreatePoolSync();
            }
            final Executor executor2 = executor;
            final TimeSource timeSource = null;
            Runnable wrapTask$1daae6bf;
            if (timeSource == null || (wrapTask$1daae6bf = timeSource.wrapTask$1daae6bf()) == null) {
                wrapTask$1daae6bf = block;
            }
            executor2.execute(wrapTask$1daae6bf);
        }
        catch (RejectedExecutionException ex) {
            DefaultExecutor.INSTANCE.enqueue(block);
        }
    }
    
    @Override
    public final String toString() {
        return "CommonPool";
    }
    
    @Override
    public final void close() {
        throw new IllegalStateException("Close cannot be invoked on CommonPool".toString());
    }
    
    private CommonPool() {
    }
    
    static {
        INSTANCE = new CommonPool();
        String property2;
        try {
            property2 = System.getProperty("kotlinx.coroutines.default.parallelism");
        }
        catch (Throwable t) {
            property2 = null;
        }
        final String $this$toIntOrNull = property2;
        int intValue;
        if ($this$toIntOrNull == null) {
            intValue = -1;
        }
        else {
            final String property = $this$toIntOrNull;
            Integer parallelism;
            if ((parallelism = StringsKt__StringNumberConversionsKt.toIntOrNull($this$toIntOrNull)) == null || parallelism <= 0) {
                parallelism = (Integer)("Expected positive number in kotlinx.coroutines.default.parallelism, but has " + property);
                throw new IllegalStateException(parallelism.toString());
            }
            intValue = parallelism;
        }
        requestedParallelism = intValue;
    }
}
