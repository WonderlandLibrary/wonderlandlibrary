// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public final class ConstrainedOnceSequence<T> implements Sequence<T>
{
    private final AtomicReference<Sequence<T>> sequenceRef;
    
    @Override
    public final Iterator<T> iterator() {
        final Sequence<T> sequence = this.sequenceRef.getAndSet(null);
        if (sequence == null) {
            throw new IllegalStateException("This sequence can be consumed only once.");
        }
        return sequence.iterator();
    }
    
    public ConstrainedOnceSequence(final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        this.sequenceRef = new AtomicReference<Sequence<T>>((Sequence<T>)sequence);
    }
}
