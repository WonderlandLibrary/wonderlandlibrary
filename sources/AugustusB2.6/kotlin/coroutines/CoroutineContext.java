// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;

public interface CoroutineContext
{
     <E extends Element> E get(final Key<E> p0);
    
     <R> R fold(final R p0, final Function2<? super R, ? super Element, ? extends R> p1);
    
    CoroutineContext plus(final CoroutineContext p0);
    
    CoroutineContext minusKey(final Key<?> p0);
    
    public static final class DefaultImpls
    {
        private final float x1;
        private final float y1;
        private final float x2;
        private final float y2;
        
        public static CoroutineContext plus(final CoroutineContext $this, final CoroutineContext context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            if (context == EmptyCoroutineContext.INSTANCE) {
                return $this;
            }
            return context.fold($this, (Function2<? super CoroutineContext, ? super Element, ? extends CoroutineContext>)CoroutineContext$plus.CoroutineContext$plus$1.INSTANCE);
        }
        
        @Override
        public String toString() {
            return "(" + this.x1 + ", " + this.y1 + "); " + this.x2 + ", " + this.y2;
        }
        
        public DefaultImpls(final float x1, final float y1, final float x2, final float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
    
    public interface Key<E extends Element>
    {
    }
    
    public interface Element extends CoroutineContext
    {
        Key<?> getKey();
        
         <E extends Element> E get(final Key<E> p0);
        
        public static final class DefaultImpls
        {
            private final String symbol;
            
            public static <E extends Element> E get(final Element $this, final Key<E> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                Element element;
                if (Intrinsics.areEqual($this.getKey(), key)) {
                    if ((element = $this) == null) {
                        throw new TypeCastException("null cannot be cast to non-null type E");
                    }
                }
                else {
                    element = null;
                }
                return (E)element;
            }
            
            public static <R> R fold(final Element $this, final R initial, final Function2<? super R, ? super Element, ? extends R> operation) {
                Intrinsics.checkParameterIsNotNull(operation, "operation");
                return (R)operation.invoke(initial, $this);
            }
            
            public static CoroutineContext minusKey(final Element $this, final Key<?> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                if (Intrinsics.areEqual($this.getKey(), key)) {
                    return EmptyCoroutineContext.INSTANCE;
                }
                return $this;
            }
            
            public static CoroutineContext plus(final Element $this, final CoroutineContext context) {
                Intrinsics.checkParameterIsNotNull(context, "context");
                return CoroutineContext.DefaultImpls.plus($this, context);
            }
            
            @Override
            public String toString() {
                return this.symbol;
            }
            
            public DefaultImpls(final String symbol) {
                Intrinsics.checkParameterIsNotNull(symbol, "symbol");
                this.symbol = symbol;
            }
        }
    }
}
