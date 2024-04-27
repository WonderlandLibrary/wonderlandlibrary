/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IndexedValue;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.IndexingSequence;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u00a2\u0006\u0002\u0010\u0005J\u0015\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00030\u0007H\u0096\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/sequences/IndexingSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/collections/IndexedValue;", "sequence", "(Lkotlin/sequences/Sequence;)V", "iterator", "", "kotlin-stdlib"})
public final class IndexingSequence<T>
implements Sequence<IndexedValue<? extends T>> {
    @NotNull
    private final Sequence<T> sequence;

    public IndexingSequence(@NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        this.sequence = sequence;
    }

    @Override
    @NotNull
    public Iterator<IndexedValue<T>> iterator() {
        return new Iterator<IndexedValue<? extends T>>(this){
            @NotNull
            private final Iterator<T> iterator;
            private int index;
            final /* synthetic */ IndexingSequence<T> this$0;
            {
                this.this$0 = $receiver;
                this.iterator = IndexingSequence.access$getSequence$p(this.this$0).iterator();
            }

            @NotNull
            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getIndex() {
                return this.index;
            }

            public final void setIndex(int n) {
                this.index = n;
            }

            @NotNull
            public IndexedValue<T> next() {
                iterator.1 var1_1 = this;
                int n = var1_1.index;
                var1_1.index = n + 1;
                int n2 = n;
                if (n2 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                return new IndexedValue<T>(n2, this.iterator.next());
            }

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }

    public static final /* synthetic */ Sequence access$getSequence$p(IndexingSequence $this) {
        return $this.sequence;
    }
}

