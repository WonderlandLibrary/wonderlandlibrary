// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import java.util.Collections;
import java.util.List;

class ArraysKt__ArraysJVMKt
{
    public static <T> List<T> listOf(final T element) {
        final List<T> singletonList = Collections.singletonList(element);
        Intrinsics.checkExpressionValueIsNotNull(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }
    
    public static <T> List<T> listOf(final T... elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        if (elements.length > 0) {
            return ArraysKt___ArraysJvmKt.asList(elements);
        }
        return (List<T>)EmptyList.INSTANCE;
    }
    
    public static <T> List<T> optimizeReadOnlyList(final List<? extends T> $this$optimizeReadOnlyList) {
        Intrinsics.checkParameterIsNotNull($this$optimizeReadOnlyList, "$this$optimizeReadOnlyList");
        switch ($this$optimizeReadOnlyList.size()) {
            case 0: {
                return (List<T>)EmptyList.INSTANCE;
            }
            case 1: {
                return listOf($this$optimizeReadOnlyList.get(0));
            }
            default: {
                return (List<T>)$this$optimizeReadOnlyList;
            }
        }
    }
    
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> sortedMapOf(final Pair<? extends K, ? extends V>... pairs) {
        Intrinsics.checkParameterIsNotNull(pairs, "pairs");
        final TreeMap<K, V> treeMap;
        MapsKt__MapsKt.putAll((Map<? super Object, ? super Object>)(treeMap = new TreeMap<K, V>()), (Pair<?, ?>[])pairs);
        return treeMap;
    }
}
