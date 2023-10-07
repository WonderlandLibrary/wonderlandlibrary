/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u00002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0081\b\u001a\u0011\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0081\b\u001a\"\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0081\b\u00a2\u0006\u0002\u0010\n\u001a4\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0006\"\u0004\b\u0000\u0010\u000b2\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\t2\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0006H\u0081\b\u00a2\u0006\u0002\u0010\r\u001a\u001f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u000f\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0010\u001a\u0002H\u000b\u00a2\u0006\u0002\u0010\u0011\u001a1\u0010\u0012\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00070\u0006\"\u0004\b\u0000\u0010\u000b*\n\u0012\u0006\b\u0001\u0012\u0002H\u000b0\u00062\u0006\u0010\u0013\u001a\u00020\u0014H\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u000f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u0017H\u0087\b\u00a8\u0006\u0018"}, d2={"checkCountOverflow", "", "count", "checkIndexOverflow", "index", "copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "listOf", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> List<T> listOf(T element) {
        List<T> list = Collections.singletonList(element);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.singletonList(element)");
        return list;
    }

    @InlineOnly
    private static final <T> List<T> toList(@NotNull Enumeration<T> $this$toList) {
        int $i$f$toList = 0;
        ArrayList<T> arrayList = Collections.list($this$toList);
        Intrinsics.checkExpressionValueIsNotNull(arrayList, "java.util.Collections.list(this)");
        return arrayList;
    }

    @InlineOnly
    private static final Object[] copyToArrayImpl(Collection<?> collection) {
        int $i$f$copyToArrayImpl = 0;
        return CollectionToArray.toArray(collection);
    }

    @InlineOnly
    private static final <T> T[] copyToArrayImpl(Collection<?> collection, T[] array) {
        int $i$f$copyToArrayImpl = 0;
        if (array == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        Object[] objectArray = CollectionToArray.toArray(collection, array);
        if (objectArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return objectArray;
    }

    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] $this$copyToArrayOfAny, boolean isVarargs) {
        Object[] objectArray;
        Intrinsics.checkParameterIsNotNull($this$copyToArrayOfAny, "$this$copyToArrayOfAny");
        if (isVarargs && Intrinsics.areEqual($this$copyToArrayOfAny.getClass(), Object[].class)) {
            objectArray = $this$copyToArrayOfAny;
        } else {
            T[] TArray = Arrays.copyOf($this$copyToArrayOfAny, $this$copyToArrayOfAny.length, Object[].class);
            objectArray = TArray;
            Intrinsics.checkExpressionValueIsNotNull(TArray, "java.util.Arrays.copyOf(\u2026 Array<Any?>::class.java)");
        }
        return objectArray;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkIndexOverflow(int index) {
        int $i$f$checkIndexOverflow = 0;
        if (index < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwIndexOverflow();
            } else {
                throw (Throwable)new ArithmeticException("Index overflow has happened.");
            }
        }
        return index;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkCountOverflow(int count) {
        int $i$f$checkCountOverflow = 0;
        if (count < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwCountOverflow();
            } else {
                throw (Throwable)new ArithmeticException("Count overflow has happened.");
            }
        }
        return count;
    }
}

