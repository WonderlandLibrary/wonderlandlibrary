/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.collections.Grouping;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aZ\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\r"}, d2={"eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", "V", "f", "Lkotlin/Function1;", "", "kotlin-stdlib"}, xs="kotlin/collections/GroupingKt")
class GroupingKt__GroupingJVMKt {
    /*
     * WARNING - void declaration
     */
    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K> Map<K, Integer> eachCount(@NotNull Grouping<T, ? extends K> $this$eachCount) {
        Serializable serializable;
        Map.Entry entry;
        Object e$iv$iv;
        Iterator iterator2;
        void $this$foldTo$iv;
        Intrinsics.checkNotNullParameter($this$eachCount, "<this>");
        Object object = $this$eachCount;
        Map destination$iv = new LinkedHashMap();
        boolean $i$f$foldTo = false;
        void $this$aggregateTo$iv$iv = $this$foldTo$iv;
        boolean $i$f$aggregateTo = false;
        Iterator iterator3 = iterator2 = $this$aggregateTo$iv$iv.sourceIterator();
        while (iterator3.hasNext()) {
            void acc;
            void var20_24;
            Serializable serializable2;
            void e$iv;
            void first$iv;
            e$iv$iv = iterator3.next();
            Object key$iv$iv = $this$aggregateTo$iv$iv.keyOf(e$iv$iv);
            Object accumulator$iv$iv = destination$iv.get(key$iv$iv);
            Map map = destination$iv;
            boolean bl = accumulator$iv$iv == null && !destination$iv.containsKey(key$iv$iv);
            Object t = e$iv$iv;
            Object v = accumulator$iv$iv;
            Object key$iv = key$iv$iv;
            boolean bl2 = false;
            Object object2 = key$iv;
            if (first$iv != false) {
                void var16_19 = e$iv;
                Object k = key$iv;
                entry = object2;
                boolean bl3 = false;
                serializable = new Ref.IntRef();
                object2 = entry;
                serializable2 = serializable;
            } else {
                void acc$iv;
                serializable2 = acc$iv;
            }
            void bl3 = e$iv;
            Ref.IntRef $noName_1 = (Ref.IntRef)serializable2;
            Object $noName_0 = object2;
            boolean bl4 = false;
            void $this$eachCount_u24lambda_u2d2_u24lambda_u2d1 = var20_24 = acc;
            boolean bl5 = false;
            void var23_27 = $this$eachCount_u24lambda_u2d2_u24lambda_u2d1;
            ++var23_27.element;
            void var14_17 = var20_24;
            map.put(key$iv$iv, var14_17);
        }
        object = destination$iv;
        Iterable iterable = object.entrySet();
        for (Object t : iterable) {
            void it;
            Map.Entry entry2 = (Map.Entry)t;
            e$iv$iv = entry2;
            entry = entry2;
            boolean bl = false;
            serializable = ((Ref.IntRef)it.getValue()).element;
            entry.setValue(serializable);
        }
        return object;
    }

    @PublishedApi
    @InlineOnly
    private static final <K, V, R> Map<K, R> mapValuesInPlace(Map<K, V> $this$mapValuesInPlace, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> f) {
        Intrinsics.checkNotNullParameter($this$mapValuesInPlace, "<this>");
        Intrinsics.checkNotNullParameter(f, "f");
        Iterable $this$forEach$iv = $this$mapValuesInPlace.entrySet();
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Map.Entry it = (Map.Entry)element$iv;
            boolean bl = false;
            it.setValue(f.invoke(it));
        }
        return $this$mapValuesInPlace;
    }
}

