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
import java.util.Random;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.collections.CollectionSystemProperties;
import kotlin.collections.CollectionsKt;
import kotlin.collections.builders.ListBuilder;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000R\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\f\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0080\b\u001a\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0006H\u0001\u001a?\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\u0006\u0010\b\u001a\u00020\t2\u001d\u0010\n\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0006\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\b\rH\u0081\b\u00f8\u0001\u0000\u001a7\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\u001d\u0010\n\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0006\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\b\rH\u0081\b\u00f8\u0001\u0000\u001a\u0011\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\tH\u0081\b\u001a\u0011\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tH\u0081\b\u001a\"\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u00132\n\u0010\u0015\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0081\b\u00a2\u0006\u0002\u0010\u0017\u001a4\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0013\"\u0004\b\u0000\u0010\u00182\n\u0010\u0015\u001a\u0006\u0012\u0002\b\u00030\u00162\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0013H\u0081\b\u00a2\u0006\u0002\u0010\u001a\u001a\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0006\"\u0004\b\u0000\u0010\u0004H\u0001\u001a\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0006\"\u0004\b\u0000\u0010\u00042\u0006\u0010\b\u001a\u00020\tH\u0001\u001a\u001f\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0003\"\u0004\b\u0000\u0010\u00182\u0006\u0010\u001d\u001a\u0002H\u0018\u00a2\u0006\u0002\u0010\u001e\u001a1\u0010\u001f\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00140\u0013\"\u0004\b\u0000\u0010\u0018*\n\u0012\u0006\b\u0001\u0012\u0002H\u00180\u00132\u0006\u0010 \u001a\u00020\u0001H\u0000\u00a2\u0006\u0002\u0010!\u001a\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0003\"\u0004\b\u0000\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00180#H\u0007\u001a&\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0003\"\u0004\b\u0000\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00180#2\u0006\u0010$\u001a\u00020%H\u0007\u001a\u001f\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0003\"\u0004\b\u0000\u0010\u0018*\b\u0012\u0004\u0012\u0002H\u00180'H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006("}, d2={"brittleContainsOptimizationEnabled", "", "build", "", "E", "builder", "", "buildListInternal", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "checkCountOverflow", "count", "checkIndexOverflow", "index", "copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "createListBuilder", "listOf", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "shuffled", "", "random", "Ljava/util/Random;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> List<T> listOf(T element) {
        List<T> list = Collections.singletonList(element);
        Intrinsics.checkNotNullExpressionValue(list, "singletonList(element)");
        return list;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <E> List<E> buildListInternal(Function1<? super List<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        List<E> list = CollectionsKt.createListBuilder();
        builderAction.invoke(list);
        return CollectionsKt.build(list);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <E> List<E> buildListInternal(int capacity, Function1<? super List<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        List<E> list = CollectionsKt.createListBuilder(capacity);
        builderAction.invoke(list);
        return CollectionsKt.build(list);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> createListBuilder() {
        return new ListBuilder();
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> createListBuilder(int capacity) {
        return new ListBuilder(capacity);
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <E> List<E> build(@NotNull List<E> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return ((ListBuilder)builder).build();
    }

    @InlineOnly
    private static final <T> List<T> toList(Enumeration<T> $this$toList) {
        Intrinsics.checkNotNullParameter($this$toList, "<this>");
        ArrayList<T> arrayList = Collections.list($this$toList);
        Intrinsics.checkNotNullExpressionValue(arrayList, "list(this)");
        return arrayList;
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> $this$shuffled) {
        List<T> list;
        Intrinsics.checkNotNullParameter($this$shuffled, "<this>");
        List<T> $this$shuffled_u24lambda_u2d0 = list = CollectionsKt.toMutableList($this$shuffled);
        boolean bl = false;
        Collections.shuffle($this$shuffled_u24lambda_u2d0);
        return list;
    }

    @SinceKotlin(version="1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> $this$shuffled, @NotNull Random random) {
        List<T> list;
        Intrinsics.checkNotNullParameter($this$shuffled, "<this>");
        Intrinsics.checkNotNullParameter(random, "random");
        List<T> $this$shuffled_u24lambda_u2d1 = list = CollectionsKt.toMutableList($this$shuffled);
        boolean bl = false;
        List<T> list2 = $this$shuffled_u24lambda_u2d1;
        Collections.shuffle(list2, random);
        return list;
    }

    @InlineOnly
    private static final Object[] copyToArrayImpl(Collection<?> collection) {
        Intrinsics.checkNotNullParameter(collection, "collection");
        return CollectionToArray.toArray(collection);
    }

    @InlineOnly
    private static final <T> T[] copyToArrayImpl(Collection<?> collection, T[] array) {
        Intrinsics.checkNotNullParameter(collection, "collection");
        Intrinsics.checkNotNullParameter(array, "array");
        return CollectionToArray.toArray(collection, array);
    }

    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] $this$copyToArrayOfAny, boolean isVarargs) {
        Object[] objectArray;
        Intrinsics.checkNotNullParameter($this$copyToArrayOfAny, "<this>");
        if (isVarargs && Intrinsics.areEqual($this$copyToArrayOfAny.getClass(), Object[].class)) {
            objectArray = $this$copyToArrayOfAny;
        } else {
            T[] TArray = Arrays.copyOf($this$copyToArrayOfAny, $this$copyToArrayOfAny.length, Object[].class);
            Intrinsics.checkNotNullExpressionValue(TArray, "copyOf(this, this.size, Array<Any?>::class.java)");
            objectArray = TArray;
        }
        return objectArray;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkIndexOverflow(int index) {
        if (index < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwIndexOverflow();
            } else {
                throw new ArithmeticException("Index overflow has happened.");
            }
        }
        return index;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int checkCountOverflow(int count) {
        if (count < 0) {
            if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                CollectionsKt.throwCountOverflow();
            } else {
                throw new ArithmeticException("Count overflow has happened.");
            }
        }
        return count;
    }

    public static final boolean brittleContainsOptimizationEnabled() {
        boolean $i$f$brittleContainsOptimizationEnabled = false;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled;
    }
}

