// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public abstract class AbstractCoroutineContextElement implements Element
{
    private final Key<?> key;
    
    @Override
    public final Key<?> getKey() {
        return this.key;
    }
    
    public AbstractCoroutineContextElement(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        this.key = key;
    }
    
    @Override
    public <E extends Element> E get(final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get((CoroutineContext.Element)this, key);
    }
    
    @Override
    public <R> R fold(final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return DefaultImpls.fold((CoroutineContext.Element)this, initial, operation);
    }
    
    @Override
    public CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey((CoroutineContext.Element)this, key);
    }
    
    @Override
    public CoroutineContext plus(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus((CoroutineContext.Element)this, context);
    }
}
