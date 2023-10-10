// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.intrinsics;

import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.internal.TypeIntrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.TypeCastException;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;

class IntrinsicsKt__IntrinsicsJvmKt
{
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(final Function2<? super R, ? super Continuation<? super T>, ?> $this$createCoroutineUnintercepted, final R receiver, Continuation<? super T> completion) {
        Intrinsics.checkParameterIsNotNull($this$createCoroutineUnintercepted, "$this$createCoroutineUnintercepted");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        completion = completion;
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        final Continuation probeCompletion = completion;
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            return ((BaseContinuationImpl)$this$createCoroutineUnintercepted).create(receiver, probeCompletion);
        }
        final CoroutineContext context$iv;
        if ((context$iv = probeCompletion.getContext()) == EmptyCoroutineContext.INSTANCE) {
            final Continuation continuation = probeCompletion;
            if (continuation == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            return (Continuation<Unit>)new RestrictedContinuationImpl(continuation, continuation, $this$createCoroutineUnintercepted, receiver) {
                private int label;
                
                @Override
                protected final Object invokeSuspend(Object it) {
                    switch (this.label) {
                        case 0: {
                            this.label = 1;
                            Pixmap.throwOnFailure(result = result);
                            it = this;
                            final Function2 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                            if ($this_createCoroutineUnintercepted$inlined == null) {
                                throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                            }
                            return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                        }
                        case 1: {
                            this.label = 2;
                            Pixmap.throwOnFailure(result = result);
                            return result;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                }
            };
        }
        else {
            final Continuation $captured_local_variable$1 = probeCompletion;
            final CoroutineContext $captured_local_variable$2 = context$iv;
            final Continuation $super_call_param$3 = probeCompletion;
            if ($super_call_param$3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            return (Continuation<Unit>)new ContinuationImpl($captured_local_variable$1, $captured_local_variable$2, $super_call_param$3, context$iv, $this$createCoroutineUnintercepted, receiver) {
                private int label;
                
                @Override
                protected final Object invokeSuspend(Object it) {
                    switch (this.label) {
                        case 0: {
                            this.label = 1;
                            Pixmap.throwOnFailure(result = result);
                            it = this;
                            final Function2 $this_createCoroutineUnintercepted$inlined = this.$this_createCoroutineUnintercepted$inlined;
                            if ($this_createCoroutineUnintercepted$inlined == null) {
                                throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                            }
                            return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                        }
                        case 1: {
                            this.label = 2;
                            Pixmap.throwOnFailure(result = result);
                            return result;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                }
            };
        }
    }
    
    public static final <T> Continuation<T> intercepted(final Continuation<? super T> $this$intercepted) {
        Intrinsics.checkParameterIsNotNull($this$intercepted, "$this$intercepted");
        ContinuationImpl continuationImpl = (ContinuationImpl)$this$intercepted;
        if (!($this$intercepted instanceof ContinuationImpl)) {
            continuationImpl = null;
        }
        final ContinuationImpl continuationImpl2 = continuationImpl;
        Continuation<Object> intercepted;
        if (continuationImpl2 == null || (intercepted = continuationImpl2.intercepted()) == null) {
            intercepted = (Continuation<Object>)$this$intercepted;
        }
        return (Continuation<T>)intercepted;
    }
}
