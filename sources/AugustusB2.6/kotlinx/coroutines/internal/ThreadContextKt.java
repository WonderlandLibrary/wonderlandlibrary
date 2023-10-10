// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ThreadContextElement;
import kotlin.jvm.functions.Function2;
import kotlin.coroutines.CoroutineContext;

public final class ThreadContextKt
{
    private static final CoroutineContext.Element.DefaultImpls ZERO$4fdbb1f;
    private static final Function2<Object, CoroutineContext.Element, Object> countAll;
    private static final Function2<ThreadContextElement<?>, CoroutineContext.Element, ThreadContextElement<?>> findOne;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> updateState;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> restoreState;
    
    public static final Object threadContextElements(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        final Integer fold = context.fold(0, (Function2<? super Integer, ? super CoroutineContext.Element, ? extends Integer>)ThreadContextKt.countAll);
        if (fold == null) {
            Intrinsics.throwNpe();
        }
        return fold;
    }
    
    public static final Object updateThreadContext(final CoroutineContext context, Object countOrElement) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Object o;
        Object threadContextElements;
        if ((threadContextElements = (o = countOrElement)) == null) {
            o = (threadContextElements = threadContextElements(context));
        }
        countOrElement = threadContextElements;
        if (o == Integer.valueOf(0)) {
            return ThreadContextKt.ZERO$4fdbb1f;
        }
        if (countOrElement instanceof Integer) {
            return context.fold(new ThreadState(context, ((Number)countOrElement).intValue()), (Function2<? super ThreadState, ? super CoroutineContext.Element, ? extends ThreadState>)ThreadContextKt.updateState);
        }
        final Object o2 = countOrElement;
        if (o2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        }
        return ((ThreadContextElement<Object>)o2).updateThreadContext(context);
    }
    
    public static final void restoreThreadContext(final CoroutineContext context, final Object oldState) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (oldState == ThreadContextKt.ZERO$4fdbb1f) {
            return;
        }
        if (oldState instanceof ThreadState) {
            ((ThreadState)oldState).start();
            context.fold(oldState, (Function2<? super Object, ? super CoroutineContext.Element, ?>)ThreadContextKt.restoreState);
            return;
        }
        final ThreadContextElement<Object> fold = context.fold((ThreadContextElement<Object>)null, (Function2<? super ThreadContextElement<Object>, ? super CoroutineContext.Element, ? extends ThreadContextElement<Object>>)ThreadContextKt.findOne);
        if (fold == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        }
        fold.restoreThreadContext(context, oldState);
    }
    
    static {
        ZERO$4fdbb1f = new CoroutineContext.Element.DefaultImpls("ZERO");
        countAll = (Function2)ThreadContextKt$countAll.ThreadContextKt$countAll$1.INSTANCE;
        findOne = (Function2)ThreadContextKt$findOne.ThreadContextKt$findOne$1.INSTANCE;
        updateState = (Function2)ThreadContextKt$updateState.ThreadContextKt$updateState$1.INSTANCE;
        restoreState = (Function2)ThreadContextKt$restoreState.ThreadContextKt$restoreState$1.INSTANCE;
    }
}
