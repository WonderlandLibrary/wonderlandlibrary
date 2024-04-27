/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.BuilderInference;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.WasExperimental;
import kotlin.collections.ArrayAsCollection;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.EmptyList;
import kotlin.comparisons.ComparisonsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000\u0088\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aC\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u00f8\u0001\u0000\u001aC\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u00f8\u0001\u0000\u001a\u001f\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a5\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\u0019\u001aN\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e\u00a2\u0006\u0002\b\u001fH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aF\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e\u00a2\u0006\u0002\b\u001fH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u0012\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007\u001a\u0015\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\"\u001a%\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020$2\b\u0010%\u001a\u0004\u0018\u0001H\u0007\u00a2\u0006\u0002\u0010&\u001a3\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020$2\u0016\u0010\u0017\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00070\u0018\"\u0004\u0018\u0001H\u0007\u00a2\u0006\u0002\u0010\"\u001a\u0015\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\"\u001a%\u0010(\u001a\u00020\u001e2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b+\u001a\b\u0010,\u001a\u00020\u001eH\u0001\u001a\b\u0010-\u001a\u00020\u001eH\u0001\u001a%\u0010.\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018H\u0000\u00a2\u0006\u0002\u0010/\u001aS\u00100\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\u0006\u0010%\u001a\u0002H\u00072\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000702j\n\u0012\u0006\b\u0000\u0012\u0002H\u0007`32\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u0006\u00a2\u0006\u0002\u00104\u001a>\u00100\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u00062\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00060\u000e\u001aE\u00100\u001a\u00020\u0006\"\u000e\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u000706*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00070\b2\b\u0010%\u001a\u0004\u0018\u0001H\u00072\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u0006\u00a2\u0006\u0002\u00107\u001ag\u00108\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007\"\u000e\b\u0001\u00109*\b\u0012\u0004\u0012\u0002H906*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\u0010:\u001a\u0004\u0018\u0001H92\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u00062\u0016\b\u0004\u0010;\u001a\u0010\u0012\u0004\u0012\u0002H\u0007\u0012\u0006\u0012\u0004\u0018\u0001H90\u000eH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010<\u001a,\u0010=\u001a\u00020>\"\t\b\u0000\u0010\u0007\u00a2\u0006\u0002\b?*\b\u0012\u0004\u0012\u0002H\u00070\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a;\u0010@\u001a\u0002HA\"\u0010\b\u0000\u0010B*\u0006\u0012\u0002\b\u00030\u0002*\u0002HA\"\u0004\b\u0001\u0010A*\u0002HB2\f\u0010C\u001a\b\u0012\u0004\u0012\u0002HA0DH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010E\u001a\u0019\u0010F\u001a\u00020>\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a,\u0010G\u001a\u00020>\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\u001e\u0010H\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\bH\u0000\u001a!\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u001a!\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\bH\u0087\b\u001a&\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070K2\u0006\u0010L\u001a\u00020MH\u0007\"\u0019\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"!\u0010\u0005\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006N"}, d2={"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/util/Collection;)Lkotlin/ranges/IntRange;", "lastIndex", "", "T", "", "getLastIndex", "(Ljava/util/List;)I", "List", "size", "init", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "index", "MutableList", "", "arrayListOf", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "elements", "", "([Ljava/lang/Object;)Ljava/util/ArrayList;", "buildList", "E", "capacity", "builderAction", "", "Lkotlin/ExtensionFunctionType;", "emptyList", "listOf", "([Ljava/lang/Object;)Ljava/util/List;", "listOfNotNull", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "mutableListOf", "rangeCheck", "fromIndex", "toIndex", "rangeCheck$CollectionsKt__CollectionsKt", "throwCountOverflow", "throwIndexOverflow", "asCollection", "([Ljava/lang/Object;)Ljava/util/Collection;", "binarySearch", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/List;Ljava/lang/Object;Ljava/util/Comparator;II)I", "comparison", "", "(Ljava/util/List;Ljava/lang/Comparable;II)I", "binarySearchBy", "K", "key", "selector", "(Ljava/util/List;Ljava/lang/Comparable;IILkotlin/jvm/functions/Function1;)I", "containsAll", "", "Lkotlin/internal/OnlyInputTypes;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNotEmpty", "isNullOrEmpty", "optimizeReadOnlyList", "orEmpty", "shuffled", "", "random", "Lkotlin/random/Random;", "kotlin-stdlib"}, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsKt
extends CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> Collection<T> asCollection(@NotNull T[] $this$asCollection) {
        Intrinsics.checkNotNullParameter($this$asCollection, "<this>");
        return new ArrayAsCollection<T>($this$asCollection, false);
    }

    @NotNull
    public static final <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }

    @NotNull
    public static final <T> List<T> listOf(T ... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length > 0 ? ArraysKt.asList(elements) : CollectionsKt.emptyList();
    }

    @InlineOnly
    private static final <T> List<T> listOf() {
        return CollectionsKt.emptyList();
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <T> List<T> mutableListOf() {
        return new ArrayList();
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <T> ArrayList<T> arrayListOf() {
        return new ArrayList();
    }

    @NotNull
    public static final <T> List<T> mutableListOf(T ... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length == 0 ? (List)new ArrayList() : (List)new ArrayList((Collection)new ArrayAsCollection<T>(elements, true));
    }

    @NotNull
    public static final <T> ArrayList<T> arrayListOf(T ... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length == 0 ? new ArrayList() : new ArrayList((Collection)new ArrayAsCollection<T>(elements, true));
    }

    @NotNull
    public static final <T> List<T> listOfNotNull(@Nullable T element) {
        return element != null ? CollectionsKt.listOf(element) : CollectionsKt.emptyList();
    }

    @NotNull
    public static final <T> List<T> listOfNotNull(T ... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return ArraysKt.filterNotNull(elements);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <T> List<T> List(int size, Function1<? super Integer, ? extends T> init) {
        Intrinsics.checkNotNullParameter(init, "init");
        ArrayList<T> arrayList = new ArrayList<T>(size);
        int n = 0;
        while (n < size) {
            int n2;
            int n3 = n2 = n++;
            arrayList.add(init.invoke(n3));
        }
        return arrayList;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <T> List<T> MutableList(int size, Function1<? super Integer, ? extends T> init) {
        Intrinsics.checkNotNullParameter(init, "init");
        ArrayList<T> list = new ArrayList<T>(size);
        int n = 0;
        while (n < size) {
            int n2;
            int index = n2 = n++;
            boolean bl = false;
            list.add(init.invoke(index));
        }
        return list;
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final <E> List<E> buildList(@BuilderInference Function1<? super List<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        List list = CollectionsKt.createListBuilder();
        builderAction.invoke(list);
        return CollectionsKt.build(list);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final <E> List<E> buildList(int capacity, @BuilderInference Function1<? super List<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        List list = CollectionsKt.createListBuilder(capacity);
        builderAction.invoke(list);
        return CollectionsKt.build(list);
    }

    @NotNull
    public static final IntRange getIndices(@NotNull Collection<?> $this$indices) {
        Intrinsics.checkNotNullParameter($this$indices, "<this>");
        return new IntRange(0, $this$indices.size() - 1);
    }

    public static final <T> int getLastIndex(@NotNull List<? extends T> $this$lastIndex) {
        Intrinsics.checkNotNullParameter($this$lastIndex, "<this>");
        return $this$lastIndex.size() - 1;
    }

    @InlineOnly
    private static final <T> boolean isNotEmpty(Collection<? extends T> $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return !$this$isNotEmpty.isEmpty();
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> boolean isNullOrEmpty(Collection<? extends T> $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.isEmpty();
    }

    @InlineOnly
    private static final <T> Collection<T> orEmpty(Collection<? extends T> $this$orEmpty) {
        Collection collection = $this$orEmpty;
        return collection == null ? (Collection)CollectionsKt.emptyList() : collection;
    }

    @InlineOnly
    private static final <T> List<T> orEmpty(List<? extends T> $this$orEmpty) {
        List<? extends T> list = $this$orEmpty;
        return list == null ? CollectionsKt.emptyList() : list;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends Collection<?> & R, R> R ifEmpty(C $this$ifEmpty, Function0<? extends R> defaultValue) {
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return (R)($this$ifEmpty.isEmpty() ? defaultValue.invoke() : $this$ifEmpty);
    }

    @InlineOnly
    private static final <T> boolean containsAll(Collection<? extends T> $this$containsAll, Collection<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$containsAll, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        return $this$containsAll.containsAll(elements);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> $this$shuffled, @NotNull Random random) {
        List<T> list;
        Intrinsics.checkNotNullParameter($this$shuffled, "<this>");
        Intrinsics.checkNotNullParameter(random, "random");
        List<T> $this$shuffled_u24lambda_u2d4 = list = CollectionsKt.toMutableList($this$shuffled);
        boolean bl = false;
        CollectionsKt.shuffle($this$shuffled_u24lambda_u2d4, random);
        return list;
    }

    @NotNull
    public static final <T> List<T> optimizeReadOnlyList(@NotNull List<? extends T> $this$optimizeReadOnlyList) {
        List<T> list;
        Intrinsics.checkNotNullParameter($this$optimizeReadOnlyList, "<this>");
        int n = $this$optimizeReadOnlyList.size();
        switch (n) {
            case 0: {
                list = CollectionsKt.emptyList();
                break;
            }
            case 1: {
                list = CollectionsKt.listOf($this$optimizeReadOnlyList.get(0));
                break;
            }
            default: {
                list = $this$optimizeReadOnlyList;
            }
        }
        return list;
    }

    public static final <T extends Comparable<? super T>> int binarySearch(@NotNull List<? extends T> $this$binarySearch, @Nullable T element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "<this>");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt($this$binarySearch.size(), fromIndex, toIndex);
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            Comparable midVal = (Comparable)$this$binarySearch.get(mid);
            int cmp = ComparisonsKt.compareValues(midVal, element);
            if (cmp < 0) {
                low = mid + 1;
                continue;
            }
            if (cmp > 0) {
                high = mid - 1;
                continue;
            }
            return mid;
        }
        return -(low + 1);
    }

    public static /* synthetic */ int binarySearch$default(List list, Comparable comparable, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, comparable, n, n2);
    }

    public static final <T> int binarySearch(@NotNull List<? extends T> $this$binarySearch, T element, @NotNull Comparator<? super T> comparator, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt($this$binarySearch.size(), fromIndex, toIndex);
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            T midVal = $this$binarySearch.get(mid);
            int cmp = comparator.compare(midVal, element);
            if (cmp < 0) {
                low = mid + 1;
                continue;
            }
            if (cmp > 0) {
                high = mid - 1;
                continue;
            }
            return mid;
        }
        return -(low + 1);
    }

    public static /* synthetic */ int binarySearch$default(List list, Object object, Comparator comparator, int n, int n2, int n3, Object object2) {
        if ((n3 & 4) != 0) {
            n = 0;
        }
        if ((n3 & 8) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, object, comparator, n, n2);
    }

    public static final <T, K extends Comparable<? super K>> int binarySearchBy(@NotNull List<? extends T> $this$binarySearchBy, @Nullable K key, int fromIndex, int toIndex, @NotNull Function1<? super T, ? extends K> selector) {
        Intrinsics.checkNotNullParameter($this$binarySearchBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        boolean $i$f$binarySearchBy = false;
        return CollectionsKt.binarySearch($this$binarySearchBy, fromIndex, toIndex, new Function1<T, Integer>(selector, key){
            final /* synthetic */ Function1<T, K> $selector;
            final /* synthetic */ K $key;
            {
                this.$selector = $selector;
                this.$key = $key;
                super(1);
            }

            @NotNull
            public final Integer invoke(T it) {
                return ComparisonsKt.compareValues((Comparable)this.$selector.invoke(it), this.$key);
            }
        });
    }

    public static /* synthetic */ int binarySearchBy$default(List $this$binarySearchBy_u24default, Comparable key, int fromIndex, int toIndex, Function1 selector, int n, Object object) {
        if ((n & 2) != 0) {
            fromIndex = 0;
        }
        if ((n & 4) != 0) {
            toIndex = $this$binarySearchBy_u24default.size();
        }
        List $this$binarySearchBy$iv = $this$binarySearchBy_u24default;
        boolean $i$f$binarySearchBy = false;
        return CollectionsKt.binarySearch($this$binarySearchBy$iv, fromIndex, toIndex, new /* invalid duplicate definition of identical inner class */);
    }

    public static final <T> int binarySearch(@NotNull List<? extends T> $this$binarySearch, int fromIndex, int toIndex, @NotNull Function1<? super T, Integer> comparison) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "<this>");
        Intrinsics.checkNotNullParameter(comparison, "comparison");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt($this$binarySearch.size(), fromIndex, toIndex);
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            T midVal = $this$binarySearch.get(mid);
            int cmp = ((Number)comparison.invoke(midVal)).intValue();
            if (cmp < 0) {
                low = mid + 1;
                continue;
            }
            if (cmp > 0) {
                high = mid - 1;
                continue;
            }
            return mid;
        }
        return -(low + 1);
    }

    public static /* synthetic */ int binarySearch$default(List list, int n, int n2, Function1 function1, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, n, n2, function1);
    }

    private static final void rangeCheck$CollectionsKt__CollectionsKt(int size, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex (" + fromIndex + ") is greater than toIndex (" + toIndex + ").");
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + fromIndex + ") is less than zero.");
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final void throwCountOverflow() {
        throw new ArithmeticException("Count overflow has happened.");
    }
}

