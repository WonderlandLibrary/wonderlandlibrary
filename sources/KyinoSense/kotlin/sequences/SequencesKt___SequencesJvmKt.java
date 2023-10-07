/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.sequences;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u00a2\u0006\u0002\u0010\t\u001a&\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\f0\u000b\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0001\u001a8\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\f0\u000b\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u000fj\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u0010\u00a8\u0006\u0011"}, d2={"filterIsInstance", "Lkotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "toSortedSet", "Ljava/util/SortedSet;", "T", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "kotlin-stdlib"}, xs="kotlin/sequences/SequencesKt")
class SequencesKt___SequencesJvmKt
extends SequencesKt__SequencesKt {
    @NotNull
    public static final <R> Sequence<R> filterIsInstance(@NotNull Sequence<?> $this$filterIsInstance, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($this$filterIsInstance, "$this$filterIsInstance");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        Sequence<?> sequence = SequencesKt.filter($this$filterIsInstance, (Function1)new Function1<Object, Boolean>(klass){
            final /* synthetic */ Class $klass;

            public final boolean invoke(@Nullable Object it) {
                return this.$klass.isInstance(it);
            }
            {
                this.$klass = clazz;
                super(1);
            }
        });
        if (sequence == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.sequences.Sequence<R>");
        }
        return sequence;
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Sequence<?> $this$filterIsInstanceTo, @NotNull C destination, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($this$filterIsInstanceTo, "$this$filterIsInstanceTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        Iterator<?> iterator2 = $this$filterIsInstanceTo.iterator();
        while (iterator2.hasNext()) {
            Object element = iterator2.next();
            if (!klass.isInstance(element)) continue;
            destination.add(element);
        }
        return destination;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet)SequencesKt.toCollection($this$toSortedSet, (Collection)new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> $this$toSortedSet, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (SortedSet)SequencesKt.toCollection($this$toSortedSet, (Collection)new TreeSet<T>(comparator));
    }
}

