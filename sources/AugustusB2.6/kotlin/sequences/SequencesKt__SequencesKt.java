// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.sequences;

import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;

class SequencesKt__SequencesKt extends SequencesKt__SequenceBuilderKt
{
    public static final <T> Sequence<T> asSequence(final Iterator<? extends T> $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        final Sequence<T> sequence;
        Intrinsics.checkParameterIsNotNull(sequence = (SequencesKt__SequencesKt$asSequence$$inlined$Sequence.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1)new Sequence<T>($this$asSequence) {
            @Override
            public final Iterator<T> iterator() {
                return (Iterator<T>)this.$this_asSequence$inlined;
            }
        }, "$this$constrainOnce");
        return new ConstrainedOnceSequence<T>(sequence);
    }
}
