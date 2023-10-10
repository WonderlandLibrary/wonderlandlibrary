// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import java.util.Collection;

public class SequencesKt___SequencesKt extends SequencesKt__SequencesKt
{
    private static <T, C extends Collection<? super T>> C toCollection(final Sequence<? extends T> $this$toCollection, final C destination) {
        Intrinsics.checkParameterIsNotNull($this$toCollection, "$this$toCollection");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        for (final Object item : $this$toCollection) {
            destination.add((Object)item);
        }
        return destination;
    }
    
    public static final <T> List<T> toList(Sequence<? extends T> $this$toList) {
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        $this$toList = (Sequence<? extends T>)$this$toList;
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toMutableList");
        return (List<T>)ArraysKt__ArraysJVMKt.optimizeReadOnlyList((List<?>)toCollection((Sequence<?>)$this$toList, (ArrayList<? extends T>)new ArrayList<Object>()));
    }
}
