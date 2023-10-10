// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.sequences.SequencesKt___SequencesKt;
import java.util.ServiceLoader;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import java.util.List;

public final class CoroutineExceptionHandlerImplKt
{
    private static final List<CoroutineExceptionHandler> handlers;
    
    public static final void handleCoroutineExceptionImpl(final CoroutineContext context, final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        final Iterator<CoroutineExceptionHandler> iterator = CoroutineExceptionHandlerImplKt.handlers.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        final Thread currentThread;
        final Thread value = currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(value, "currentThread");
        value.getUncaughtExceptionHandler().uncaughtException(currentThread, exception);
    }
    
    static {
        final Iterator<CoroutineExceptionHandler> iterator = ServiceLoader.load(CoroutineExceptionHandler.class, CoroutineExceptionHandler.class.getClassLoader()).iterator();
        Intrinsics.checkExpressionValueIsNotNull(iterator, "ServiceLoader.load(\n    \u2026.classLoader\n).iterator()");
        handlers = SequencesKt___SequencesKt.toList(SequencesKt__SequencesKt.asSequence((Iterator<?>)iterator));
    }
}
