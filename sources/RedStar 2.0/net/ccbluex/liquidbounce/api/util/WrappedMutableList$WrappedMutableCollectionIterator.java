package net.ccbluex.liquidbounce.api.util;

import java.util.ListIterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableListIterator;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\b\n+\n\b\n\n\b\b\n\n\b\n\n\b\n\b\n\b\u0000*\b*\b2\bH0B;\f\b80880880¢\bJ028H¢J\t0HJ\b0HJ8H¢J\b0HJ\r8H¢J\b0HJ\b0HJ028H¢R880¢\b\n\u0000\b\t\nR\b80¢\b\n\u0000\b\fR880¢\b\n\u0000\b\r\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableList$WrappedMutableCollectionIterator;", "O", "T", "", "wrapped", "wrapper", "Lkotlin/Function1;", "unwrapper", "(Ljava/util/ListIterator;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/ListIterator;", "getWrapper", "add", "", "element", "(Ljava/lang/Object;)V", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "remove", "set", "Pride"})
public static final class WrappedMutableList$WrappedMutableCollectionIterator<O, T>
implements ListIterator<T>,
KMutableListIterator {
    @NotNull
    private final ListIterator<O> wrapped;
    @NotNull
    private final Function1<O, T> wrapper;
    @NotNull
    private final Function1<T, O> unwrapper;

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

    @Override
    public void add(T element) {
        this.wrapped.add(this.unwrapper.invoke(element));
    }

    @Override
    public void remove() {
        this.wrapped.remove();
    }

    @Override
    public void set(T element) {
        this.wrapped.set(this.unwrapper.invoke(element));
    }

    @NotNull
    public final ListIterator<O> getWrapped() {
        return this.wrapped;
    }

    @NotNull
    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    @NotNull
    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    public WrappedMutableList$WrappedMutableCollectionIterator(@NotNull ListIterator<O> wrapped, @NotNull Function1<? super O, ? extends T> wrapper, @NotNull Function1<? super T, ? extends O> unwrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        this.wrapped = wrapped;
        this.wrapper = wrapper;
        this.unwrapper = unwrapper;
    }
}
