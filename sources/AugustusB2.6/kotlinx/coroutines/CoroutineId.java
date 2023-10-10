// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.AbstractCoroutineContextElement;

public final class CoroutineId extends AbstractCoroutineContextElement implements ThreadContextElement<String>
{
    private final long id;
    public static final Key Key;
    
    @Override
    public final String toString() {
        return "CoroutineId(" + this.id + ')';
    }
    
    public final long getId() {
        return this.id;
    }
    
    public CoroutineId(final long id) {
        super(CoroutineId.Key);
        this.id = id;
    }
    
    static {
        Key = new Key((byte)0);
    }
    
    @Override
    public final <R> R fold(final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return CoroutineContext.Element.DefaultImpls.fold((CoroutineContext.Element)this, initial, operation);
    }
    
    @Override
    public final <E extends Element> E get(final CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        return CoroutineContext.Element.DefaultImpls.get((CoroutineContext.Element)this, key);
    }
    
    @Override
    public final CoroutineContext minusKey(final CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        return CoroutineContext.Element.DefaultImpls.minusKey((CoroutineContext.Element)this, key);
    }
    
    @Override
    public final CoroutineContext plus(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(context, "context");
        return CoroutineContext.Element.DefaultImpls.plus((CoroutineContext.Element)this, context);
    }
    
    @Override
    public final int hashCode() {
        final long id = this.id;
        return (int)(id ^ id >>> 32);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof CoroutineId && this.id == ((CoroutineId)o).id);
    }
    
    public static final class Key implements CoroutineContext.Key<CoroutineId>
    {
        private Key() {
        }
    }
}
