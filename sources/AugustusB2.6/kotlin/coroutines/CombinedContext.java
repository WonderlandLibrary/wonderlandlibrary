// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import java.io.Serializable;

public final class CombinedContext implements Serializable, CoroutineContext
{
    private final CoroutineContext left;
    private final Element element;
    
    @Override
    public final <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        final CombinedContext combinedContext = this;
        while (true) {
            final Element value = combinedContext.element.get((Key<E>)key);
            if (value != null) {
                key = value;
                return (E)value;
            }
            CoroutineContext next;
            if (!((next = combinedContext.left) instanceof CombinedContext)) {
                return next.get((Key<E>)key);
            }
            next = next;
        }
    }
    
    @Override
    public final <R> R fold(final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R)operation.invoke(this.left.fold(initial, operation), this.element);
    }
    
    @Override
    public final CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        final CoroutineContext newLeft;
        if ((newLeft = this.left.minusKey(key)) == this.left) {
            return this;
        }
        if (newLeft == EmptyCoroutineContext.INSTANCE) {
            return this.element;
        }
        return new CombinedContext(newLeft, this.element);
    }
    
    private final int size() {
        CombinedContext cur = this;
        int size = 2;
        while (true) {
            CoroutineContext left;
            if (!((left = cur.left) instanceof CombinedContext)) {
                left = null;
            }
            final CombinedContext combinedContext = (CombinedContext)left;
            if (combinedContext == null) {
                break;
            }
            cur = combinedContext;
            ++size;
        }
        return size;
    }
    
    private final boolean contains(final Element element) {
        return Intrinsics.areEqual(this.get(element.getKey()), element);
    }
    
    @Override
    public final boolean equals(final Object other) {
        if (this != other) {
            if (other instanceof CombinedContext && ((CombinedContext)other).size() == this.size()) {
                final CombinedContext combinedContext = (CombinedContext)other;
                CombinedContext combinedContext2 = this;
                while (true) {
                    while (combinedContext.contains(combinedContext2.element)) {
                        final CoroutineContext left;
                        if ((left = combinedContext2.left) instanceof CombinedContext) {
                            combinedContext2 = (CombinedContext)left;
                        }
                        else {
                            final CombinedContext combinedContext3 = combinedContext;
                            final CombinedContext combinedContext4 = (CombinedContext)left;
                            if (combinedContext4 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                            }
                            final boolean contains = combinedContext3.contains((Element)combinedContext4);
                            if (contains) {
                                return true;
                            }
                            return false;
                        }
                    }
                    final boolean contains = false;
                    continue;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }
    
    @Override
    public final String toString() {
        return "[" + this.fold("", (Function2<? super String, ? super Element, ? extends String>)CombinedContext$toString.CombinedContext$toString$1.INSTANCE) + "]";
    }
    
    public CombinedContext(final CoroutineContext left, final Element element) {
        Intrinsics.checkParameterIsNotNull(left, "left");
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.left = left;
        this.element = element;
    }
    
    @Override
    public final CoroutineContext plus(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return DefaultImpls.plus(this, context);
    }
}
