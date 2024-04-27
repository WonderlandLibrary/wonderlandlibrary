package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\b\n \n\n\b\n\n\b\n\b\n\b\n*\n\b\b\u0000*\b\u0000*\b*\b*\bH02HHH02\bH0:B5888\u00000\b\t8\u000080\b¢\nJ82\f0\rH¢J0\r28H¢J0\r28H¢J\b80HJ\b802\f0\rHJ\b8020\r20\rH¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedList;", "O", "T", "C", "", "Lnet/ccbluex/liquidbounce/api/util/WrappedCollection;", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "get", "index", "", "(I)Ljava/lang/Object;", "indexOf", "element", "(Ljava/lang/Object;)I", "lastIndexOf", "listIterator", "", "subList", "fromIndex", "toIndex", "WrappedCollectionIterator", "Pride"})
public class WrappedList<O, T, C extends List<? extends O>>
extends WrappedCollection<O, T, C>
implements List<T>,
KMappedMarker {
    @Override
    public T get(int index) {
        return this.getWrapper().invoke(((List)this.getWrapped()).get(index));
    }

    @Override
    public int indexOf(Object element) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    public int lastIndexOf(Object element) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator() {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper());
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator(int index) {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(index), this.getWrapper());
    }

    @Override
    @NotNull
    public List<T> subList(int fromIndex, int toIndex) {
        return new WrappedList(((List)this.getWrapped()).subList(fromIndex, toIndex), this.getUnwrapper(), this.getWrapper());
    }

    public WrappedList(@NotNull C wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        super((Collection)wrapped, unwrapper, wrapper);
    }

    @Override
    public void add(int n, T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(int n, Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public T remove(int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void replaceAll(UnaryOperator<T> unaryOperator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public T set(int n, T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\b\n*\n\b\n\n\b\n\n\b\n\b\n\b\u0000*\b*\b2\bH0B'\f\b80880¢J\t\f0\rHJ\b0\rHJ8H¢J\b0HJ\r8H¢J\b0HR\b80¢\b\n\u0000\b\b\tR880¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedList$WrappedCollectionIterator;", "O", "T", "", "wrapped", "wrapper", "Lkotlin/Function1;", "(Ljava/util/ListIterator;Lkotlin/jvm/functions/Function1;)V", "getWrapped", "()Ljava/util/ListIterator;", "getWrapper", "()Lkotlin/jvm/functions/Function1;", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "Pride"})
    public static final class WrappedCollectionIterator<O, T>
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

        public WrappedCollectionIterator(@NotNull ListIterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> wrapper) {
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
}
