/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.intrinsics;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b\u00a2\u0006\u0002\b\b\u001aD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a]\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aA\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001aZ\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001an\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0014\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0015\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\u0006\u0010\u0016\u001a\u0002H\u00142\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2={"createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", "T", "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "P", "Lkotlin/Function3;", "param", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, xs="kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt {
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> Object startCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 1)).invoke(completion);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <R, T> Object startCoroutineUninterceptedOrReturn(Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, R receiver, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 2)).invoke(receiver, completion);
    }

    @InlineOnly
    private static final <R, P, T> Object startCoroutineUninterceptedOrReturn(Function3<? super R, ? super P, ? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, R receiver, P param, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function3)TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 3)).invoke(receiver, param, completion);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function1<? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, @NotNull Continuation<? super T> completion) {
        Continuation continuation;
        Intrinsics.checkNotNullParameter($this$createCoroutineUnintercepted, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        Continuation<T> probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            continuation = ((BaseContinuationImpl)((Object)$this$createCoroutineUnintercepted)).create(probeCompletion);
        } else {
            boolean $i$f$createCoroutineFromSuspendFunction = false;
            CoroutineContext context$iv = probeCompletion.getContext();
            continuation = context$iv == EmptyCoroutineContext.INSTANCE ? (Continuation)new RestrictedContinuationImpl(probeCompletion, $this$createCoroutineUnintercepted){
                private int label;
                final /* synthetic */ Continuation $completion;
                final /* synthetic */ Function1 $this_createCoroutineUnintercepted$inlined;
                {
                    this.$completion = $completion;
                    this.$this_createCoroutineUnintercepted$inlined = function1;
                    super($completion);
                }

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    int n = this.label;
                    switch (n) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            ResultKt.throwOnFailure(object2);
                            Continuation it = this;
                            boolean bl = false;
                            object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 1)).invoke(it);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                    return object;
                }
            } : (Continuation)new ContinuationImpl(probeCompletion, context$iv, $this$createCoroutineUnintercepted){
                private int label;
                final /* synthetic */ Continuation $completion;
                final /* synthetic */ CoroutineContext $context;
                final /* synthetic */ Function1 $this_createCoroutineUnintercepted$inlined;
                {
                    this.$completion = $completion;
                    this.$context = $context;
                    this.$this_createCoroutineUnintercepted$inlined = function1;
                    super($completion, $context);
                }

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    int n = this.label;
                    switch (n) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            ResultKt.throwOnFailure(object2);
                            Continuation it = this;
                            boolean bl = false;
                            object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 1)).invoke(it);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                    return object;
                }
            };
        }
        return continuation;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, R receiver, @NotNull Continuation<? super T> completion) {
        Continuation continuation;
        Intrinsics.checkNotNullParameter($this$createCoroutineUnintercepted, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        Continuation<T> probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            continuation = ((BaseContinuationImpl)((Object)$this$createCoroutineUnintercepted)).create(receiver, probeCompletion);
        } else {
            boolean $i$f$createCoroutineFromSuspendFunction = false;
            CoroutineContext context$iv = probeCompletion.getContext();
            continuation = context$iv == EmptyCoroutineContext.INSTANCE ? (Continuation)new RestrictedContinuationImpl(probeCompletion, $this$createCoroutineUnintercepted, receiver){
                private int label;
                final /* synthetic */ Continuation $completion;
                final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
                final /* synthetic */ Object $receiver$inlined;
                {
                    this.$completion = $completion;
                    this.$this_createCoroutineUnintercepted$inlined = function2;
                    this.$receiver$inlined = object;
                    super($completion);
                }

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    int n = this.label;
                    switch (n) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            ResultKt.throwOnFailure(object2);
                            Continuation it = this;
                            boolean bl = false;
                            object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                    return object;
                }
            } : (Continuation)new ContinuationImpl(probeCompletion, context$iv, $this$createCoroutineUnintercepted, receiver){
                private int label;
                final /* synthetic */ Continuation $completion;
                final /* synthetic */ CoroutineContext $context;
                final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
                final /* synthetic */ Object $receiver$inlined;
                {
                    this.$completion = $completion;
                    this.$context = $context;
                    this.$this_createCoroutineUnintercepted$inlined = function2;
                    this.$receiver$inlined = object;
                    super($completion, $context);
                }

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    int n = this.label;
                    switch (n) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            ResultKt.throwOnFailure(object2);
                            Continuation it = this;
                            boolean bl = false;
                            object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 2)).invoke(this.$receiver$inlined, it);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            throw new IllegalStateException("This coroutine had already completed".toString());
                        }
                    }
                    return object;
                }
            };
        }
        return continuation;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Continuation<T> intercepted(@NotNull Continuation<? super T> $this$intercepted) {
        Intrinsics.checkNotNullParameter($this$intercepted, "<this>");
        ContinuationImpl continuationImpl = $this$intercepted instanceof ContinuationImpl ? (ContinuationImpl)$this$intercepted : null;
        return continuationImpl == null ? $this$intercepted : continuationImpl.intercepted();
    }

    @SinceKotlin(version="1.3")
    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(Continuation<? super T> completion, Function1<? super Continuation<? super T>, ? extends Object> block) {
        boolean $i$f$createCoroutineFromSuspendFunction = false;
        CoroutineContext context = completion.getContext();
        return context == EmptyCoroutineContext.INSTANCE ? (Continuation)new RestrictedContinuationImpl(completion, block){
            private int label;
            final /* synthetic */ Continuation<T> $completion;
            final /* synthetic */ Function1<Continuation<? super T>, Object> $block;
            {
                this.$completion = $completion;
                this.$block = $block;
                super($completion);
            }

            @Nullable
            protected Object invokeSuspend(@NotNull Object result) {
                Object object;
                int n = this.label;
                switch (n) {
                    case 0: {
                        this.label = 1;
                        Object object2 = result;
                        ResultKt.throwOnFailure(object2);
                        object = this.$block.invoke(this);
                        break;
                    }
                    case 1: {
                        this.label = 2;
                        Object object3 = result;
                        ResultKt.throwOnFailure(object3);
                        object = object3;
                        break;
                    }
                    default: {
                        throw new IllegalStateException("This coroutine had already completed".toString());
                    }
                }
                return object;
            }
        } : (Continuation)new ContinuationImpl(completion, context, block){
            private int label;
            final /* synthetic */ Continuation<T> $completion;
            final /* synthetic */ CoroutineContext $context;
            final /* synthetic */ Function1<Continuation<? super T>, Object> $block;
            {
                this.$completion = $completion;
                this.$context = $context;
                this.$block = $block;
                super($completion, $context);
            }

            @Nullable
            protected Object invokeSuspend(@NotNull Object result) {
                Object object;
                int n = this.label;
                switch (n) {
                    case 0: {
                        this.label = 1;
                        Object object2 = result;
                        ResultKt.throwOnFailure(object2);
                        object = this.$block.invoke(this);
                        break;
                    }
                    case 1: {
                        this.label = 2;
                        Object object3 = result;
                        ResultKt.throwOnFailure(object3);
                        object = object3;
                        break;
                    }
                    default: {
                        throw new IllegalStateException("This coroutine had already completed".toString());
                    }
                }
                return object;
            }
        };
    }
}

