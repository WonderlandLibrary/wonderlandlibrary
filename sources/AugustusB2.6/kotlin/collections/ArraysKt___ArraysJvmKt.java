// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.Comparator;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;

class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysJVMKt
{
    public static final <T> List<T> asList(T[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        $this$asList = $this$asList;
        final List<T> list = Arrays.asList($this$asList);
        Intrinsics.checkExpressionValueIsNotNull(list, "ArraysUtilJVM.asList(this)");
        return list;
    }
    
    public static <T> List<T> sortedWith(T[] $this$sortedWith, Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull($this$sortedWith, "$this$sortedWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        comparator = (Comparator<? super Object>)comparator;
        $this$sortedWith = $this$sortedWith;
        Intrinsics.checkParameterIsNotNull($this$sortedWith, "$this$sortedArrayWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        T[] $this$asList;
        if ($this$sortedWith.length == 0) {
            $this$asList = $this$sortedWith;
        }
        else {
            final T[] original = $this$sortedWith;
            final T[] copy = Arrays.copyOf(original, original.length);
            Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
            final T[] array = copy;
            comparator = comparator;
            final T[] a = copy;
            Intrinsics.checkParameterIsNotNull(copy, "$this$sortWith");
            Intrinsics.checkParameterIsNotNull(comparator, "comparator");
            if (a.length > 1) {
                Arrays.sort(a, comparator);
            }
            $this$asList = array;
        }
        return asList($this$asList);
    }
}
