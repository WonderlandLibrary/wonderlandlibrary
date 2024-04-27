package net.ccbluex.liquidbounce.api.util;

import java.util.ListIterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\b\n*\n\b\n\n\b\n\n\b\n\b\n\b\u0000*\b*\b2\bH0B'\f\b80880¢J\t\f0\rHJ\b0\rHJ8H¢J\b0HJ\r8H¢J\b0HR\b80¢\b\n\u0000\b\b\tR880¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedList$WrappedCollectionIterator;", "O", "T", "", "wrapped", "wrapper", "Lkotlin/Function1;", "(Ljava/util/ListIterator;Lkotlin/jvm/functions/Function1;)V", "getWrapped", "()Ljava/util/ListIterator;", "getWrapper", "()Lkotlin/jvm/functions/Function1;", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "Pride"})
public static final class WrappedList$WrappedCollectionIterator<O, T>
implements ListIterator<T>,
KMappedMarker {
    @NotNull
    private final ListIterator<O> wrapped;
    @NotNull
    private final Function1<O, T> wrapper;

    @Override
    public boolean hasNext() {
        return this.wrapped.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return this.wrapped.hasPrevious();
    }

    @Override
    public T next() {
        return this.wrapper.invoke(this.wrapped.next());
    }

    @Override
    public int nextIndex() {
        return this.wrapped.nextIndex();
    }

    @Override
    public T previous() {
        return this.wrapper.invoke(this.wrapped.previous());
    }

    @Override
    public int previousIndex() {
        return this.wrapped.previousIndex();
    }

    @NotNull
    public final ListIterator<O> getWrapped() {
        return this.wrapped;
    }

    @NotNull
    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedList$WrappedCollectionIterator(@NotNull ListIterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        this.wrapped = wrapped;
        this.wrapper = wrapper;
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
