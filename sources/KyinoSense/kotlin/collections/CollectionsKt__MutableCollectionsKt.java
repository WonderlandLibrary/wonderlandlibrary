/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.random.Random;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000^\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002\u00a2\u0006\u0002\b\u000e\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002\u00a2\u0006\u0002\b\u000e\u001a(\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010\u0013\u001a.\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n\u00a2\u0006\u0002\u0010\u0014\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010\u0013\u001a.\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n\u00a2\u0006\u0002\u0010\u0014\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0016\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u0018\u001a&\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0087\b\u00a2\u0006\u0002\u0010\u001b\u001a-\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001c\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a\u0015\u0010\u001f\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002\u00a2\u0006\u0002\b \u001a \u0010!\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\"\u001a\u00020#H\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00020%\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00072\u0006\u0010\"\u001a\u00020#H\u0007\u00a8\u0006&"}, d2={"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "shuffle", "random", "Lkotlin/random/Random;", "shuffled", "", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__MutableCollectionsKt
extends CollectionsKt__MutableCollectionsJVMKt {
    @InlineOnly
    private static final <T> boolean remove(@NotNull Collection<? extends T> $this$remove, T element) {
        int $i$f$remove = 0;
        Collection<? extends T> collection = $this$remove;
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).remove(element);
    }

    @InlineOnly
    private static final <T> boolean removeAll(@NotNull Collection<? extends T> $this$removeAll, Collection<? extends T> elements) {
        int $i$f$removeAll = 0;
        Collection<? extends T> collection = $this$removeAll;
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).removeAll(elements);
    }

    @InlineOnly
    private static final <T> boolean retainAll(@NotNull Collection<? extends T> $this$retainAll, Collection<? extends T> elements) {
        int $i$f$retainAll = 0;
        Collection<? extends T> collection = $this$retainAll;
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).retainAll(elements);
    }

    @Deprecated(message="Use removeAt(index) instead.", replaceWith=@ReplaceWith(imports={}, expression="removeAt(index)"), level=DeprecationLevel.ERROR)
    @InlineOnly
    private static final <T> T remove(@NotNull List<T> $this$remove, int index) {
        int $i$f$remove = 0;
        return $this$remove.remove(index);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> $this$plusAssign, T element) {
        int $i$f$plusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$plusAssign, "$this$plusAssign");
        $this$plusAssign.add(element);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> $this$plusAssign, Iterable<? extends T> elements) {
        int $i$f$plusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$plusAssign, "$this$plusAssign");
        CollectionsKt.addAll($this$plusAssign, elements);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> $this$plusAssign, T[] elements) {
        int $i$f$plusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$plusAssign, "$this$plusAssign");
        CollectionsKt.addAll($this$plusAssign, elements);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> $this$plusAssign, Sequence<? extends T> elements) {
        int $i$f$plusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$plusAssign, "$this$plusAssign");
        CollectionsKt.addAll($this$plusAssign, elements);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> $this$minusAssign, T element) {
        int $i$f$minusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        $this$minusAssign.remove(element);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> $this$minusAssign, Iterable<? extends T> elements) {
        int $i$f$minusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        CollectionsKt.removeAll($this$minusAssign, elements);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> $this$minusAssign, T[] elements) {
        int $i$f$minusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        CollectionsKt.removeAll($this$minusAssign, elements);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> $this$minusAssign, Sequence<? extends T> elements) {
        int $i$f$minusAssign = 0;
        Intrinsics.checkParameterIsNotNull($this$minusAssign, "$this$minusAssign");
        CollectionsKt.removeAll($this$minusAssign, elements);
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> $this$addAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$addAll, "$this$addAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable<T> iterable = elements;
        if (iterable instanceof Collection) {
            return $this$addAll.addAll((Collection)elements);
        }
        boolean result = false;
        for (T item : elements) {
            if (!$this$addAll.add(item)) continue;
            result = true;
        }
        return result;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> $this$addAll, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$addAll, "$this$addAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        boolean result = false;
        Iterator<T> iterator2 = elements.iterator();
        while (iterator2.hasNext()) {
            T item = iterator2.next();
            if (!$this$addAll.add(item)) continue;
            result = true;
        }
        return result;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> $this$addAll, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull($this$addAll, "$this$addAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return $this$addAll.addAll((Collection)ArraysKt.asList(elements));
    }

    public static final <T> boolean removeAll(@NotNull Iterable<? extends T> $this$removeAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$removeAll, "$this$removeAll");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt($this$removeAll, predicate, true);
    }

    public static final <T> boolean retainAll(@NotNull Iterable<? extends T> $this$retainAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$retainAll, "$this$retainAll");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt($this$retainAll, predicate, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull Iterable<? extends T> $this$filterInPlace, Function1<? super T, Boolean> predicate, boolean predicateResultToRemove) {
        boolean result = false;
        Iterator<T> iterator2 = $this$filterInPlace.iterator();
        boolean bl = false;
        boolean bl2 = false;
        Iterator<T> $this$with = iterator2;
        boolean bl3 = false;
        while ($this$with.hasNext()) {
            if (predicate.invoke($this$with.next()) != predicateResultToRemove) continue;
            $this$with.remove();
            result = true;
        }
        return result;
    }

    public static final <T> boolean removeAll(@NotNull List<T> $this$removeAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$removeAll, "$this$removeAll");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt($this$removeAll, predicate, true);
    }

    public static final <T> boolean retainAll(@NotNull List<T> $this$retainAll, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$retainAll, "$this$retainAll");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt($this$retainAll, predicate, false);
    }

    /*
     * WARNING - void declaration
     */
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull List<T> $this$filterInPlace, Function1<? super T, Boolean> predicate, boolean predicateResultToRemove) {
        int readIndex;
        if (!($this$filterInPlace instanceof RandomAccess)) {
            List<T> list = $this$filterInPlace;
            if (list == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
            }
            return CollectionsKt__MutableCollectionsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list), predicate, predicateResultToRemove);
        }
        int writeIndex = 0;
        int n = 0;
        int n2 = CollectionsKt.getLastIndex($this$filterInPlace);
        if (n <= n2) {
            while (true) {
                T element;
                if (predicate.invoke(element = $this$filterInPlace.get(readIndex)) != predicateResultToRemove) {
                    if (writeIndex != readIndex) {
                        $this$filterInPlace.set(writeIndex, element);
                    }
                    ++writeIndex;
                }
                if (readIndex == n2) break;
                ++readIndex;
            }
        }
        if (writeIndex < $this$filterInPlace.size()) {
            readIndex = CollectionsKt.getLastIndex($this$filterInPlace);
            if (readIndex >= (n2 = writeIndex)) {
                while (true) {
                    void removeIndex;
                    $this$filterInPlace.remove((int)removeIndex);
                    if (removeIndex == n2) break;
                    --removeIndex;
                }
            }
            return true;
        }
        return false;
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> $this$removeAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$removeAll, "$this$removeAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Collection<? super T> collection = $this$removeAll;
        Collection<? extends T> collection2 = CollectionsKt.convertToSetForSetOperationWith(elements, (Iterable)$this$removeAll);
        boolean bl = false;
        return TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> $this$removeAll, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$removeAll, "$this$removeAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        HashSet<? extends T> set = SequencesKt.toHashSet(elements);
        Collection collection = set;
        boolean bl = false;
        return !collection.isEmpty() && $this$removeAll.removeAll((Collection)set);
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> $this$removeAll, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull($this$removeAll, "$this$removeAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        T[] TArray = elements;
        boolean bl = false;
        T[] TArray2 = TArray;
        boolean bl2 = false;
        return !(TArray2.length == 0) && $this$removeAll.removeAll((Collection)ArraysKt.toHashSet(elements));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> $this$retainAll, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$retainAll, "$this$retainAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Collection<? super T> collection = $this$retainAll;
        Collection<? extends T> collection2 = CollectionsKt.convertToSetForSetOperationWith(elements, (Iterable)$this$retainAll);
        boolean bl = false;
        return TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> $this$retainAll, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull($this$retainAll, "$this$retainAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        T[] TArray = elements;
        boolean bl = false;
        T[] TArray2 = TArray;
        boolean bl2 = false;
        if (!(TArray2.length == 0)) {
            return $this$retainAll.retainAll((Collection)ArraysKt.toHashSet(elements));
        }
        return CollectionsKt__MutableCollectionsKt.retainNothing$CollectionsKt__MutableCollectionsKt($this$retainAll);
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> $this$retainAll, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$retainAll, "$this$retainAll");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        HashSet<? extends T> set = SequencesKt.toHashSet(elements);
        Collection collection = set;
        boolean bl = false;
        if (!collection.isEmpty()) {
            return $this$retainAll.retainAll((Collection)set);
        }
        return CollectionsKt__MutableCollectionsKt.retainNothing$CollectionsKt__MutableCollectionsKt($this$retainAll);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(@NotNull Collection<?> $this$retainNothing) {
        Collection<?> collection = $this$retainNothing;
        boolean bl = false;
        boolean result = !collection.isEmpty();
        $this$retainNothing.clear();
        return result;
    }

    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.3")
    public static final <T> void shuffle(@NotNull List<T> $this$shuffle, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$shuffle, "$this$shuffle");
        Intrinsics.checkParameterIsNotNull(random, "random");
        int n = CollectionsKt.getLastIndex($this$shuffle);
        int n2 = 1;
        while (n >= n2) {
            void i;
            int j = random.nextInt((int)(i + true));
            T copy = $this$shuffle.get((int)i);
            $this$shuffle.set((int)i, $this$shuffle.get(j));
            $this$shuffle.set(j, copy);
            --i;
        }
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> $this$shuffled, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$shuffled, "$this$shuffled");
        Intrinsics.checkParameterIsNotNull(random, "random");
        List<? extends T> list = CollectionsKt.toMutableList($this$shuffled);
        boolean bl = false;
        boolean bl2 = false;
        List<? extends T> $this$apply = list;
        boolean bl3 = false;
        CollectionsKt.shuffle($this$apply, random);
        return list;
    }
}

