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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.internal.HidesMembers;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000h\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\u001aG\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001a$\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aG\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001a9\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\n\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001a6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001a'\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001aG\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001aY\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\n0\u0006H\u0086\b\u001ar\u0010\u0013\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\n0\u0006H\u0086\b\u00a2\u0006\u0002\u0010\u0017\u001aG\u0010\u0018\u001a\u00020\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00190\u0006H\u0087\b\u001aS\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b\u001aY\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0011*\u00020\u001d*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00110\u0006H\u0086\b\u001ar\u0010\u001e\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0011*\u00020\u001d\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00110\u0006H\u0086\b\u00a2\u0006\u0002\u0010\u0017\u001al\u0010\u001f\u001a\u0002H\u0014\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0011\"\u0010\b\u0003\u0010\u0014*\n\u0012\u0006\b\u0000\u0012\u0002H\u00110\u0015*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0016\u001a\u0002H\u00142\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b\u00a2\u0006\u0002\u0010\u0017\u001ae\u0010 \u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u00110!*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\"\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0087\b\u001ai\u0010#\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u0010$\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070%j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`&H\u0087\b\u001ae\u0010'\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u00110!*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\"\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00110\u0006H\u0086\b\u001af\u0010(\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u0010$\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070%j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`&\u001a$\u0010)\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aG\u0010)\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u001aV\u0010*\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010+*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002H+2\u001e\u0010\u001a\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00190\u0006H\u0087\b\u00a2\u0006\u0002\u0010,\u001a6\u0010-\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030.0\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u00a8\u0006/"}, d2={"all", "", "K", "V", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "count", "", "flatMap", "", "R", "transform", "flatMapTo", "C", "", "destination", "(Ljava/util/Map;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "forEach", "", "action", "map", "mapNotNull", "", "mapNotNullTo", "mapTo", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "minBy", "minWith", "none", "onEach", "M", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "toList", "Lkotlin/Pair;", "kotlin-stdlib"}, xs="kotlin/collections/MapsKt")
class MapsKt___MapsKt
extends MapsKt__MapsKt {
    @NotNull
    public static final <K, V> List<Pair<K, V>> toList(@NotNull Map<? extends K, ? extends V> $this$toList) {
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        if ($this$toList.size() == 0) {
            return CollectionsKt.emptyList();
        }
        Iterator<Map.Entry<K, V>> iterator2 = $this$toList.entrySet().iterator();
        if (!iterator2.hasNext()) {
            return CollectionsKt.emptyList();
        }
        Map.Entry<K, V> first = iterator2.next();
        if (!iterator2.hasNext()) {
            Map.Entry<K, V> entry = first;
            boolean bl = false;
            return CollectionsKt.listOf(new Pair<K, V>(entry.getKey(), entry.getValue()));
        }
        ArrayList<Pair<K, V>> result = new ArrayList<Pair<K, V>>($this$toList.size());
        Map.Entry<K, V> entry = first;
        ArrayList<Pair<K, V>> arrayList = result;
        boolean bl = false;
        Pair<K, V> pair = new Pair<K, V>(entry.getKey(), entry.getValue());
        arrayList.add(pair);
        do {
            entry = iterator2.next();
            arrayList = result;
            bl = false;
            pair = new Pair<K, V>(entry.getKey(), entry.getValue());
            arrayList.add(pair);
        } while (iterator2.hasNext());
        return result;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> flatMap(@NotNull Map<? extends K, ? extends V> $this$flatMap, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        void $this$flatMapTo$iv;
        int $i$f$flatMap = 0;
        Intrinsics.checkParameterIsNotNull($this$flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<? extends K, ? extends V> map = $this$flatMap;
        Collection destination$iv = new ArrayList();
        boolean $i$f$flatMapTo = false;
        void var6_6 = $this$flatMapTo$iv;
        boolean bl = false;
        for (Map.Entry element$iv : var6_6.entrySet()) {
            Iterable<? extends R> list$iv = transform.invoke(element$iv);
            CollectionsKt.addAll(destination$iv, list$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapTo(@NotNull Map<? extends K, ? extends V> $this$flatMapTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> transform) {
        int $i$f$flatMapTo = 0;
        Intrinsics.checkParameterIsNotNull($this$flatMapTo, "$this$flatMapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<K, V> map = $this$flatMapTo;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Iterable<R> list = transform.invoke(entry);
            CollectionsKt.addAll(destination, list);
        }
        return destination;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> map(@NotNull Map<? extends K, ? extends V> $this$map, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        void $this$mapTo$iv;
        int $i$f$map = 0;
        Intrinsics.checkParameterIsNotNull($this$map, "$this$map");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<K, V> map = $this$map;
        Collection destination$iv = new ArrayList($this$map.size());
        boolean $i$f$mapTo = false;
        void var6_6 = $this$mapTo$iv;
        boolean bl = false;
        for (Map.Entry item$iv : var6_6.entrySet()) {
            destination$iv.add(transform.invoke(item$iv));
        }
        return (List)destination$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final <K, V, R> List<R> mapNotNull(@NotNull Map<? extends K, ? extends V> $this$mapNotNull, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        void $this$mapNotNullTo$iv;
        int $i$f$mapNotNull = 0;
        Intrinsics.checkParameterIsNotNull($this$mapNotNull, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<? extends K, ? extends V> map = $this$mapNotNull;
        Collection destination$iv = new ArrayList();
        boolean $i$f$mapNotNullTo = false;
        void $this$forEach$iv$iv = $this$mapNotNullTo$iv;
        boolean $i$f$forEach = false;
        void var8_8 = $this$forEach$iv$iv;
        boolean bl = false;
        Iterator iterator2 = var8_8.entrySet().iterator();
        while (iterator2.hasNext()) {
            R r;
            Map.Entry element$iv$iv;
            Map.Entry element$iv = element$iv$iv = iterator2.next();
            boolean bl2 = false;
            if (transform.invoke(element$iv) == null) continue;
            boolean bl3 = false;
            boolean bl4 = false;
            R it$iv = r;
            boolean bl5 = false;
            destination$iv.add(it$iv);
        }
        return (List)destination$iv;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapNotNullTo(@NotNull Map<? extends K, ? extends V> $this$mapNotNullTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        int $i$f$mapNotNullTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapNotNullTo, "$this$mapNotNullTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<K, V> $this$forEach$iv = $this$mapNotNullTo;
        boolean $i$f$forEach = false;
        Map<K, V> map = $this$forEach$iv;
        boolean bl = false;
        Iterator<Map.Entry<K, V>> iterator2 = map.entrySet().iterator();
        while (iterator2.hasNext()) {
            R r;
            Map.Entry<? extends K, ? extends V> element$iv;
            Map.Entry<? extends K, ? extends V> element = element$iv = iterator2.next();
            boolean bl2 = false;
            if (transform.invoke(element) == null) continue;
            boolean bl3 = false;
            boolean bl4 = false;
            R it = r;
            boolean bl5 = false;
            destination.add(it);
        }
        return destination;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapTo(@NotNull Map<? extends K, ? extends V> $this$mapTo, @NotNull C destination, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        int $i$f$mapTo = 0;
        Intrinsics.checkParameterIsNotNull($this$mapTo, "$this$mapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map<K, V> map = $this$mapTo;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            destination.add(transform.invoke(entry));
        }
        return destination;
    }

    public static final <K, V> boolean all(@NotNull Map<? extends K, ? extends V> $this$all, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        int $i$f$all = 0;
        Intrinsics.checkParameterIsNotNull($this$all, "$this$all");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($this$all.isEmpty()) {
            return true;
        }
        Map<K, V> map = $this$all;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> $this$any) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        return !$this$any.isEmpty();
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> $this$any, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        int $i$f$any = 0;
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($this$any.isEmpty()) {
            return false;
        }
        Map<K, V> map = $this$any;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            return true;
        }
        return false;
    }

    @InlineOnly
    private static final <K, V> int count(@NotNull Map<? extends K, ? extends V> $this$count) {
        int $i$f$count = 0;
        return $this$count.size();
    }

    public static final <K, V> int count(@NotNull Map<? extends K, ? extends V> $this$count, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        int $i$f$count = 0;
        Intrinsics.checkParameterIsNotNull($this$count, "$this$count");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($this$count.isEmpty()) {
            return 0;
        }
        int count = 0;
        Map<K, V> map = $this$count;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            ++count;
        }
        return count;
    }

    @HidesMembers
    public static final <K, V> void forEach(@NotNull Map<? extends K, ? extends V> $this$forEach, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        int $i$f$forEach = 0;
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        Map<K, V> map = $this$forEach;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            action.invoke(entry);
        }
    }

    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxBy(@NotNull Map<? extends K, ? extends V> $this$maxBy, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        int $i$f$maxBy = 0;
        Iterable $this$maxBy$iv = $this$maxBy.entrySet();
        boolean $i$f$maxBy2 = false;
        Iterator iterator$iv = $this$maxBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v0 = null;
        } else {
            Object maxElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v0 = maxElem$iv;
            } else {
                Comparable maxValue$iv = (Comparable)selector.invoke((Map.Entry<K, V>)maxElem$iv);
                do {
                    Object e$iv;
                    Comparable v$iv;
                    if (maxValue$iv.compareTo(v$iv = (Comparable)selector.invoke((Map.Entry<K, V>)(e$iv = iterator$iv.next()))) >= 0) continue;
                    maxElem$iv = e$iv;
                    maxValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v0 = maxElem$iv;
            }
        }
        return v0;
    }

    @InlineOnly
    private static final <K, V> Map.Entry<K, V> maxWith(@NotNull Map<? extends K, ? extends V> $this$maxWith, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        int $i$f$maxWith = 0;
        return CollectionsKt.maxWith((Iterable)$this$maxWith.entrySet(), comparator);
    }

    @Nullable
    public static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minBy(@NotNull Map<? extends K, ? extends V> $this$minBy, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        Object v0;
        int $i$f$minBy = 0;
        Intrinsics.checkParameterIsNotNull($this$minBy, "$this$minBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        Iterable $this$minBy$iv = $this$minBy.entrySet();
        boolean $i$f$minBy2 = false;
        Iterator iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v0 = null;
        } else {
            Object minElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v0 = minElem$iv;
            } else {
                Comparable minValue$iv = (Comparable)selector.invoke((Map.Entry<K, V>)minElem$iv);
                do {
                    Object e$iv;
                    Comparable v$iv;
                    if (minValue$iv.compareTo(v$iv = (Comparable)selector.invoke((Map.Entry<K, V>)(e$iv = iterator$iv.next()))) <= 0) continue;
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v0 = minElem$iv;
            }
        }
        return v0;
    }

    @Nullable
    public static final <K, V> Map.Entry<K, V> minWith(@NotNull Map<? extends K, ? extends V> $this$minWith, @NotNull Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkParameterIsNotNull($this$minWith, "$this$minWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return CollectionsKt.minWith((Iterable)$this$minWith.entrySet(), comparator);
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> $this$none) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        return $this$none.isEmpty();
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> $this$none, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> predicate) {
        int $i$f$none = 0;
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if ($this$none.isEmpty()) {
            return true;
        }
        Map<K, V> map = $this$none;
        boolean bl = false;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (!predicate.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEach(@NotNull M $this$onEach, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> action) {
        int $i$f$onEach = 0;
        Intrinsics.checkParameterIsNotNull($this$onEach, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        M m = $this$onEach;
        boolean bl = false;
        boolean bl2 = false;
        M $this$apply = m;
        boolean bl3 = false;
        M m2 = $this$apply;
        boolean bl4 = false;
        for (Map.Entry<? extends K, ? extends V> entry : m2.entrySet()) {
            action.invoke(entry);
        }
        return m;
    }

    @InlineOnly
    private static final <K, V> Iterable<Map.Entry<K, V>> asIterable(@NotNull Map<? extends K, ? extends V> $this$asIterable) {
        int $i$f$asIterable = 0;
        return $this$asIterable.entrySet();
    }

    @NotNull
    public static final <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull Map<? extends K, ? extends V> $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        return CollectionsKt.asSequence((Iterable)$this$asSequence.entrySet());
    }
}

