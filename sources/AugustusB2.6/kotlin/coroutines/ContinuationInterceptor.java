// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;

public interface ContinuationInterceptor extends Element
{
    public static final Key Key = ContinuationInterceptor.Key.$$INSTANCE;
    
     <T> Continuation<T> interceptContinuation(final Continuation<? super T> p0);
    
    void releaseInterceptedContinuation(final Continuation<?> p0);
    
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        private Key() {
        }
        
        static {
            $$INSTANCE = new Key();
        }
    }
    
    public static final class DefaultImpls
    {
        private final float x;
        private final float y;
        
        @Override
        public String toString() {
            return this.x + ", " + this.y;
        }
        
        public DefaultImpls(final float x, final float y) {
            this.x = x;
            this.y = y;
        }
        
        public static <R, T> void startCoroutine(final Function2<? super R, ? super Continuation<? super T>, ?> $this$startCoroutine, final R receiver, final Continuation<? super T> completion) {
            Intrinsics.checkParameterIsNotNull($this$startCoroutine, "$this$startCoroutine");
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            final Continuation<Object> intercepted = IntrinsicsKt__IntrinsicsJvmKt.intercepted((Continuation<? super Object>)IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted((Function2<? super R, ? super Continuation<? super Object>, ?>)$this$startCoroutine, receiver, (Continuation<? super Object>)completion));
            final Unit instance = Unit.INSTANCE;
            final Continuation<Object> continuation = intercepted;
            final Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.constructor-impl(instance));
        }
    }
}
