/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines;

import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.coroutines.AbstractCoroutineContextKey;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u0018\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a+\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a\u0018\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0007\u00a8\u0006\b"}, d2={"getPolymorphicElement", "E", "Lkotlin/coroutines/CoroutineContext$Element;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Element;Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusPolymorphicKey", "Lkotlin/coroutines/CoroutineContext;", "kotlin-stdlib"})
public final class CoroutineContextImplKt {
    @SinceKotlin(version="1.3")
    @ExperimentalStdlibApi
    @Nullable
    public static final <E extends CoroutineContext.Element> E getPolymorphicElement(@NotNull CoroutineContext.Element $this$getPolymorphicElement, @NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkNotNullParameter($this$getPolymorphicElement, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        if (key instanceof AbstractCoroutineContextKey) {
            Object e;
            return ((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib($this$getPolymorphicElement.getKey()) ? ((e = ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib($this$getPolymorphicElement)) instanceof CoroutineContext.Element ? e : null) : null;
        }
        return (E)($this$getPolymorphicElement.getKey() == key ? $this$getPolymorphicElement : null);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalStdlibApi
    @NotNull
    public static final CoroutineContext minusPolymorphicKey(@NotNull CoroutineContext.Element $this$minusPolymorphicKey, @NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkNotNullParameter($this$minusPolymorphicKey, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        if (key instanceof AbstractCoroutineContextKey) {
            return ((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib($this$minusPolymorphicKey.getKey()) && ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib($this$minusPolymorphicKey) != null ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)$this$minusPolymorphicKey;
        }
        return $this$minusPolymorphicKey.getKey() == key ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)$this$minusPolymorphicKey;
    }
}

