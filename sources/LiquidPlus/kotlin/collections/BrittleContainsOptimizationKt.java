/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionSystemProperties;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000 \n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0000\u00a2\u0006\u0002\u0010\u0004\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0000\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0000\u001a,\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00052\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0000\u001a\u0018\u0010\t\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0002\u00a8\u0006\u000b"}, d2={"convertToSetForSetOperation", "", "T", "", "([Ljava/lang/Object;)Ljava/util/Collection;", "", "Lkotlin/sequences/Sequence;", "convertToSetForSetOperationWith", "source", "safeToConvertToSet", "", "kotlin-stdlib"})
public final class BrittleContainsOptimizationKt {
    private static final <T> boolean safeToConvertToSet(Collection<? extends T> $this$safeToConvertToSet) {
        boolean $i$f$brittleContainsOptimizationEnabled = false;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled && $this$safeToConvertToSet.size() > 2 && $this$safeToConvertToSet instanceof ArrayList;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperationWith(@NotNull Iterable<? extends T> $this$convertToSetForSetOperationWith, @NotNull Iterable<? extends T> source) {
        Collection collection;
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperationWith, "<this>");
        Intrinsics.checkNotNullParameter(source, "source");
        Iterable<? extends T> iterable = $this$convertToSetForSetOperationWith;
        if (iterable instanceof Set) {
            collection = (Collection)$this$convertToSetForSetOperationWith;
        } else if (iterable instanceof Collection) {
            collection = source instanceof Collection && ((Collection)source).size() < 2 ? (Collection)$this$convertToSetForSetOperationWith : (BrittleContainsOptimizationKt.safeToConvertToSet((Collection)$this$convertToSetForSetOperationWith) ? (Collection)CollectionsKt.toHashSet($this$convertToSetForSetOperationWith) : (Collection)$this$convertToSetForSetOperationWith);
        } else {
            boolean $i$f$brittleContainsOptimizationEnabled = false;
            collection = CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection)CollectionsKt.toHashSet($this$convertToSetForSetOperationWith) : (Collection)CollectionsKt.toList($this$convertToSetForSetOperationWith);
        }
        return collection;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull Iterable<? extends T> $this$convertToSetForSetOperation) {
        Collection collection;
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        Iterable<? extends T> iterable = $this$convertToSetForSetOperation;
        if (iterable instanceof Set) {
            collection = (Collection)$this$convertToSetForSetOperation;
        } else if (iterable instanceof Collection) {
            collection = BrittleContainsOptimizationKt.safeToConvertToSet((Collection)$this$convertToSetForSetOperation) ? (Collection)CollectionsKt.toHashSet($this$convertToSetForSetOperation) : (Collection)$this$convertToSetForSetOperation;
        } else {
            boolean $i$f$brittleContainsOptimizationEnabled = false;
            collection = CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection)CollectionsKt.toHashSet($this$convertToSetForSetOperation) : (Collection)CollectionsKt.toList($this$convertToSetForSetOperation);
        }
        return collection;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull Sequence<? extends T> $this$convertToSetForSetOperation) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        boolean $i$f$brittleContainsOptimizationEnabled = false;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection)SequencesKt.toHashSet($this$convertToSetForSetOperation) : (Collection)SequencesKt.toList($this$convertToSetForSetOperation);
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull T[] $this$convertToSetForSetOperation) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        boolean $i$f$brittleContainsOptimizationEnabled = false;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection)ArraysKt.toHashSet($this$convertToSetForSetOperation) : (Collection)ArraysKt.asList($this$convertToSetForSetOperation);
    }
}

