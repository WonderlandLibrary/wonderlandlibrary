/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import kotlin.DeepRecursiveFunction;
import kotlin.DeepRecursiveScopeImpl;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.SinceKotlin;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0006\u001a\u0002H\u0007\"\u0004\b\u0000\u0010\b\"\u0004\b\u0001\u0010\u0007*\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\u00070\t2\u0006\u0010\n\u001a\u0002H\bH\u0087\u0002\u00a2\u0006\u0002\u0010\u000b\"!\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00018\u0002X\u0083\u0004\u00f8\u0001\u0000\u00a2\u0006\n\n\u0002\u0010\u0005\u0012\u0004\b\u0003\u0010\u0004*v\b\u0003\u0010\f\"5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r\u00a2\u0006\u0002\b\u001025\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r\u00a2\u0006\u0002\b\u0010B\u0002\b\u0011\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2={"UNDEFINED_RESULT", "Lkotlin/Result;", "", "getUNDEFINED_RESULT$annotations", "()V", "Ljava/lang/Object;", "invoke", "R", "T", "Lkotlin/DeepRecursiveFunction;", "value", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;)Ljava/lang/Object;", "DeepRecursiveFunctionBlock", "Lkotlin/Function3;", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "Lkotlin/ExperimentalStdlibApi;", "kotlin-stdlib"})
public final class DeepRecursiveKt {
    @NotNull
    private static final Object UNDEFINED_RESULT;

    @SinceKotlin(version="1.4")
    @ExperimentalStdlibApi
    public static final <T, R> R invoke(@NotNull DeepRecursiveFunction<T, R> $this$invoke, T value) {
        Intrinsics.checkNotNullParameter($this$invoke, "<this>");
        return new DeepRecursiveScopeImpl<T, R>($this$invoke.getBlock$kotlin_stdlib(), value).runCallLoop();
    }

    private static /* synthetic */ void getUNDEFINED_RESULT$annotations() {
    }

    @ExperimentalStdlibApi
    private static /* synthetic */ void DeepRecursiveFunctionBlock$annotations() {
    }

    public static final /* synthetic */ Object access$getUNDEFINED_RESULT$p() {
        return UNDEFINED_RESULT;
    }

    static {
        Result.Companion companion = Result.Companion;
        Object object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        UNDEFINED_RESULT = Result.constructor-impl(object);
    }
}

