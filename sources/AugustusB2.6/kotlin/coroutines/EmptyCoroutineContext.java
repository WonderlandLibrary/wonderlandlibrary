// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import java.io.Serializable;

public final class EmptyCoroutineContext implements Serializable, CoroutineContext
{
    public static final EmptyCoroutineContext INSTANCE;
    
    @Override
    public final <E extends Element> E get(final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return null;
    }
    
    @Override
    public final <R> R fold(final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return initial;
    }
    
    @Override
    public final CoroutineContext plus(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return context;
    }
    
    @Override
    public final CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return this;
    }
    
    @Override
    public final int hashCode() {
        return 0;
    }
    
    @Override
    public final String toString() {
        return "EmptyCoroutineContext";
    }
    
    private EmptyCoroutineContext() {
    }
    
    static {
        INSTANCE = new EmptyCoroutineContext();
    }
}
