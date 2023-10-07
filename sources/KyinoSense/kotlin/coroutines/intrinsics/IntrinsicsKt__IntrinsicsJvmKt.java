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
import kotlin.TypeCastException;
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
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b\u00a2\u0006\u0002\b\b\u001aD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a]\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aA\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001aZ\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0014"}, d2={"createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", "T", "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, xs="kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt {
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> Object startCoroutineUninterceptedOrReturn(@NotNull Function1<? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, Continuation<? super T> completion) {
        int $i$f$startCoroutineUninterceptedOrReturn = 0;
        Function1<? super Continuation<? super T>, ? extends Object> function1 = $this$startCoroutineUninterceptedOrReturn;
        if (function1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(completion);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <R, T> Object startCoroutineUninterceptedOrReturn(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, R receiver, Continuation<? super T> completion) {
        int $i$f$startCoroutineUninterceptedOrReturn = 0;
        Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2 = $this$startCoroutineUninterceptedOrReturn;
        if (function2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
        }
        return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(receiver, completion);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function1<? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, @NotNull Continuation<? super T> completion) {
        Continuation continuation2;
        Intrinsics.checkParameterIsNotNull($this$createCoroutineUnintercepted, "$this$createCoroutineUnintercepted");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        Continuation<T> probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            continuation2 = ((BaseContinuationImpl)((Object)$this$createCoroutineUnintercepted)).create(probeCompletion);
        } else {
            boolean $i$f$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt = false;
            CoroutineContext context$iv = probeCompletion.getContext();
            if (context$iv == EmptyCoroutineContext.INSTANCE) {
                Continuation<T> continuation3 = probeCompletion;
                if (continuation3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                continuation2 = new RestrictedContinuationImpl(probeCompletion, continuation3, $this$createCoroutineUnintercepted){
                    private int label;
                    final /* synthetic */ Continuation $completion;
                    final /* synthetic */ Function1 $this_createCoroutineUnintercepted$inlined;
                    {
                        this.$completion = $captured_local_variable$1;
                        this.$this_createCoroutineUnintercepted$inlined = function1;
                        super($super_call_param$2);
                    }

                    @Nullable
                    protected Object invokeSuspend(@NotNull Object result) {
                        Object object;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                Object object2 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object2);
                                Continuation it = this;
                                boolean bl2 = false;
                                Function1 function1 = this.$this_createCoroutineUnintercepted$inlined;
                                if (function1 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                Object object3 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object3);
                                object = object3;
                                break;
                            }
                            default: {
                                String string = "This coroutine had already completed";
                                boolean bl = false;
                                throw (Throwable)new IllegalStateException(string.toString());
                            }
                        }
                        return object;
                    }
                };
            } else {
                Continuation<T> continuation4 = probeCompletion;
                if (continuation4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                continuation2 = new ContinuationImpl(probeCompletion, context$iv, continuation4, context$iv, $this$createCoroutineUnintercepted){
                    private int label;
                    final /* synthetic */ Continuation $completion;
                    final /* synthetic */ CoroutineContext $context;
                    final /* synthetic */ Function1 $this_createCoroutineUnintercepted$inlined;
                    {
                        this.$completion = $captured_local_variable$1;
                        this.$context = $captured_local_variable$2;
                        this.$this_createCoroutineUnintercepted$inlined = function1;
                        super($super_call_param$3, $super_call_param$4);
                    }

                    @Nullable
                    protected Object invokeSuspend(@NotNull Object result) {
                        Object object;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                Object object2 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object2);
                                Continuation it = this;
                                boolean bl2 = false;
                                Function1 function1 = this.$this_createCoroutineUnintercepted$inlined;
                                if (function1 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                Object object3 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object3);
                                object = object3;
                                break;
                            }
                            default: {
                                String string = "This coroutine had already completed";
                                boolean bl = false;
                                throw (Throwable)new IllegalStateException(string.toString());
                            }
                        }
                        return object;
                    }
                };
            }
        }
        return continuation2;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, R receiver, @NotNull Continuation<? super T> completion) {
        Continuation continuation2;
        Intrinsics.checkParameterIsNotNull($this$createCoroutineUnintercepted, "$this$createCoroutineUnintercepted");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        Continuation<T> probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            continuation2 = ((BaseContinuationImpl)((Object)$this$createCoroutineUnintercepted)).create(receiver, probeCompletion);
        } else {
            boolean $i$f$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt = false;
            CoroutineContext context$iv = probeCompletion.getContext();
            if (context$iv == EmptyCoroutineContext.INSTANCE) {
                Continuation<T> continuation3 = probeCompletion;
                if (continuation3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                continuation2 = new RestrictedContinuationImpl(probeCompletion, continuation3, $this$createCoroutineUnintercepted, receiver){
                    private int label;
                    final /* synthetic */ Continuation $completion;
                    final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
                    final /* synthetic */ Object $receiver$inlined;
                    {
                        this.$completion = $captured_local_variable$1;
                        this.$this_createCoroutineUnintercepted$inlined = function2;
                        this.$receiver$inlined = object;
                        super($super_call_param$2);
                    }

                    @Nullable
                    protected Object invokeSuspend(@NotNull Object result) {
                        Object object;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                Object object2 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object2);
                                Continuation it = this;
                                boolean bl2 = false;
                                Function2 function2 = this.$this_createCoroutineUnintercepted$inlined;
                                if (function2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(this.$receiver$inlined, it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                Object object3 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object3);
                                object = object3;
                                break;
                            }
                            default: {
                                String string = "This coroutine had already completed";
                                boolean bl = false;
                                throw (Throwable)new IllegalStateException(string.toString());
                            }
                        }
                        return object;
                    }
                };
            } else {
                Continuation<T> continuation4 = probeCompletion;
                if (continuation4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                continuation2 = new ContinuationImpl(probeCompletion, context$iv, continuation4, context$iv, $this$createCoroutineUnintercepted, receiver){
                    private int label;
                    final /* synthetic */ Continuation $completion;
                    final /* synthetic */ CoroutineContext $context;
                    final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
                    final /* synthetic */ Object $receiver$inlined;
                    {
                        this.$completion = $captured_local_variable$1;
                        this.$context = $captured_local_variable$2;
                        this.$this_createCoroutineUnintercepted$inlined = function2;
                        this.$receiver$inlined = object;
                        super($super_call_param$3, $super_call_param$4);
                    }

                    @Nullable
                    protected Object invokeSuspend(@NotNull Object result) {
                        Object object;
                        switch (this.label) {
                            case 0: {
                                this.label = 1;
                                Object object2 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object2);
                                Continuation it = this;
                                boolean bl2 = false;
                                Function2 function2 = this.$this_createCoroutineUnintercepted$inlined;
                                if (function2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                                }
                                object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(this.$receiver$inlined, it);
                                break;
                            }
                            case 1: {
                                this.label = 2;
                                Object object3 = result;
                                boolean bl = false;
                                ResultKt.throwOnFailure(object3);
                                object = object3;
                                break;
                            }
                            default: {
                                String string = "This coroutine had already completed";
                                boolean bl = false;
                                throw (Throwable)new IllegalStateException(string.toString());
                            }
                        }
                        return object;
                    }
                };
            }
        }
        return continuation2;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Continuation<T> intercepted(@NotNull Continuation<? super T> $this$intercepted) {
        Continuation<Object> continuation2;
        Intrinsics.checkParameterIsNotNull($this$intercepted, "$this$intercepted");
        Continuation<? super T> continuation3 = $this$intercepted;
        if (!(continuation3 instanceof ContinuationImpl)) {
            continuation3 = null;
        }
        if ((continuation2 = (ContinuationImpl)continuation3) == null || (continuation2 = continuation2.intercepted()) == null) {
            continuation2 = $this$intercepted;
        }
        return continuation2;
    }

    @SinceKotlin(version="1.3")
    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(Continuation<? super T> completion, Function1<? super Continuation<? super T>, ? extends Object> block) {
        Continuation continuation2;
        int $i$f$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt = 0;
        CoroutineContext context = completion.getContext();
        if (context == EmptyCoroutineContext.INSTANCE) {
            Continuation<T> continuation3 = completion;
            if (continuation3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            continuation2 = new RestrictedContinuationImpl(block, completion, continuation3){
                private int label;
                final /* synthetic */ Function1 $block;
                final /* synthetic */ Continuation $completion;

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    switch (this.label) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            boolean bl = false;
                            ResultKt.throwOnFailure(object2);
                            object = this.$block.invoke(this);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            boolean bl = false;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            String string = "This coroutine had already completed";
                            boolean bl = false;
                            throw (Throwable)new IllegalStateException(string.toString());
                        }
                    }
                    return object;
                }
                {
                    this.$block = $captured_local_variable$0;
                    this.$completion = $captured_local_variable$1;
                    super($super_call_param$2);
                }
            };
        } else {
            Continuation<T> continuation4 = completion;
            if (continuation4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
            }
            continuation2 = new ContinuationImpl(block, completion, context, continuation4, context){
                private int label;
                final /* synthetic */ Function1 $block;
                final /* synthetic */ Continuation $completion;
                final /* synthetic */ CoroutineContext $context;

                @Nullable
                protected Object invokeSuspend(@NotNull Object result) {
                    Object object;
                    switch (this.label) {
                        case 0: {
                            this.label = 1;
                            Object object2 = result;
                            boolean bl = false;
                            ResultKt.throwOnFailure(object2);
                            object = this.$block.invoke(this);
                            break;
                        }
                        case 1: {
                            this.label = 2;
                            Object object3 = result;
                            boolean bl = false;
                            ResultKt.throwOnFailure(object3);
                            object = object3;
                            break;
                        }
                        default: {
                            String string = "This coroutine had already completed";
                            boolean bl = false;
                            throw (Throwable)new IllegalStateException(string.toString());
                        }
                    }
                    return object;
                }
                {
                    this.$block = $captured_local_variable$0;
                    this.$completion = $captured_local_variable$1;
                    this.$context = $captured_local_variable$2;
                    super($super_call_param$3, $super_call_param$4);
                }
            };
        }
        return continuation2;
    }
}

