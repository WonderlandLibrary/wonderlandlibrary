package net.ccbluex.liquidbounce.api.util;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\b\n)\n\b\n\n\b\n\n\b\n\n\u0000\u0000*\b*\b2\bH0B'\f\b80880¢J\t\f0\rHJ8H¢J\b0HR880¢\b\n\u0000\b\b\tR\b80¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableCollection$WrappedCollectionIterator;", "O", "T", "", "wrapped", "unwrapper", "Lkotlin/Function1;", "(Ljava/util/Iterator;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/Iterator;", "hasNext", "", "next", "()Ljava/lang/Object;", "remove", "", "Pride"})
public static final class WrappedMutableCollection$WrappedCollectionIterator<O, T>
implements Iterator<T>,
KMutableIterator {
    @NotNull
    private final Iterator<O> wrapped;
    @NotNull
    private final Function1<O, T> unwrapper;

    @Override
    public boolean hasNext() {
        return this.wrapped.hasNext();
    }

    @Override
    public T next() {
        return this.unwrapper.invoke(this.wrapped.next());
    }

    @Override
    public void remove() {
        this.wrapped.remove();
    }

    @NotNull
    public final Iterator<O> getWrapped() {
        return this.wrapped;
    }

    @NotNull
    public final Function1<O, T> getUnwrapper() {
        return this.unwrapper;
    }

    public WrappedMutableCollection$WrappedCollectionIterator(@NotNull Iterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> unwrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
    }
}
