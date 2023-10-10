// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.scheduling.DefaultScheduler;

public final class CoroutineContextKt
{
    private static final boolean useCoroutinesScheduler;
    
    public static final CoroutineDispatcher createDefaultDispatcher() {
        if (CoroutineContextKt.useCoroutinesScheduler) {
            return DefaultScheduler.INSTANCE;
        }
        return CommonPool.INSTANCE;
    }
    
    public static final CoroutineContext newCoroutineContext(final CoroutineScope $this$newCoroutineContext, CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull($this$newCoroutineContext, "$this$newCoroutineContext");
        Intrinsics.checkParameterIsNotNull(context, "context");
        final CoroutineContext combined = $this$newCoroutineContext.getCoroutineContext().plus(context);
        context = (DebugKt.getDEBUG() ? combined.plus(new CoroutineId(DebugKt.getCOROUTINE_ID().incrementAndGet())) : combined);
        if (combined != Dispatchers.getDefault() && combined.get((CoroutineContext.Key<CoroutineContext.Element>)ContinuationInterceptor.Key) == null) {
            return context.plus(Dispatchers.getDefault());
        }
        return context;
    }
    
    public static final String getCoroutineName(final CoroutineContext $this$coroutineName) {
        Intrinsics.checkParameterIsNotNull($this$coroutineName, "$this$coroutineName");
        if (!DebugKt.getDEBUG()) {
            return null;
        }
        final CoroutineId coroutineId2 = $this$coroutineName.get((CoroutineContext.Key<CoroutineId>)CoroutineId.Key);
        if (coroutineId2 == null) {
            return null;
        }
        final CoroutineId coroutineId = coroutineId2;
        final CoroutineName coroutineName2 = (CoroutineName)$this$coroutineName.get((CoroutineContext.Key<CoroutineContext.Element>)CoroutineName.Key);
        String name;
        if (coroutineName2 == null || (name = coroutineName2.getName()) == null) {
            name = "coroutine";
        }
        final String coroutineName = name;
        return coroutineName + '#' + coroutineId.getId();
    }
    
    static {
        final String value;
        final String s = value = LockFreeTaskQueueCore.Companion.systemProp("kotlinx.coroutines.scheduler");
        boolean useCoroutinesScheduler2 = false;
        Label_0134: {
            Label_0089: {
                if (s != null) {
                    final String s2 = s;
                    switch (s.hashCode()) {
                        case 0: {
                            if (s2.equals("")) {
                                break Label_0089;
                            }
                            break;
                        }
                        case 109935: {
                            if (s2.equals("off")) {
                                useCoroutinesScheduler2 = false;
                                break Label_0134;
                            }
                            break;
                        }
                        case 3551: {
                            if (s2.equals("on")) {
                                break Label_0089;
                            }
                            break;
                        }
                    }
                    throw new IllegalStateException(("System property 'kotlinx.coroutines.scheduler' has unrecognized value '" + value + '\'').toString());
                }
            }
            useCoroutinesScheduler2 = true;
        }
        useCoroutinesScheduler = useCoroutinesScheduler2;
    }
}
