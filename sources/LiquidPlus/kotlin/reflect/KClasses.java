/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.reflect;

import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u0010\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a+\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a-\u0010\u0006\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007\u00a2\u0006\u0002\u0010\u0005\u00a8\u0006\u0007"}, d2={"cast", "T", "", "Lkotlin/reflect/KClass;", "value", "(Lkotlin/reflect/KClass;Ljava/lang/Object;)Ljava/lang/Object;", "safeCast", "kotlin-stdlib"})
@JvmName(name="KClasses")
public final class KClasses {
    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> T cast(@NotNull KClass<T> $this$cast, @Nullable Object value) {
        Intrinsics.checkNotNullParameter($this$cast, "<this>");
        if (!$this$cast.isInstance(value)) {
            KClass<T> $this$qualifiedOrSimpleName$iv = $this$cast;
            boolean $i$f$getQualifiedOrSimpleName = false;
            throw new ClassCastException(Intrinsics.stringPlus("Value cannot be cast to ", $this$qualifiedOrSimpleName$iv.getQualifiedName()));
        }
        if (value == null) {
            throw new NullPointerException("null cannot be cast to non-null type T of kotlin.reflect.KClasses.cast");
        }
        return (T)value;
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @LowPriorityInOverloadResolution
    @Nullable
    public static final <T> T safeCast(@NotNull KClass<T> $this$safeCast, @Nullable Object value) {
        Object object;
        Intrinsics.checkNotNullParameter($this$safeCast, "<this>");
        if ($this$safeCast.isInstance(value)) {
            if (value == null) {
                throw new NullPointerException("null cannot be cast to non-null type T of kotlin.reflect.KClasses.safeCast");
            }
            object = value;
        } else {
            object = null;
        }
        return (T)object;
    }
}

