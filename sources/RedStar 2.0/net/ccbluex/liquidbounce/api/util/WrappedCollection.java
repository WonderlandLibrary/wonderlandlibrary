package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\b\n\n\b\n\n\b\n\b\n\b\t\n\n\b\n(\n\b\b\u0000*\b\u0000*\b*\b*\bH02\bH0:B5888\u00000\b8\u000080¢\tJ028H¢J02\f\b80HJ\b0HJ\b80HR\n08VX¢\b\f\rR88\u00000¢\b\n\u0000\bR8¢\n\n\bR\b8\u000080¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedCollection;", "O", "T", "C", "", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "size", "", "getSize", "()I", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/Collection;", "Ljava/util/Collection;", "getWrapper", "contains", "", "element", "(Ljava/lang/Object;)Z", "containsAll", "elements", "isEmpty", "iterator", "", "WrappedCollectionIterator", "Pride"})
public class WrappedCollection<O, T, C extends Collection<? extends O>>
implements Collection<T>,
KMappedMarker {
    @NotNull
    private final C wrapped;
    @NotNull
    private final Function1<T, O> unwrapper;
    @NotNull
    private final Function1<O, T> wrapper;

    public int getSize() {
        return this.wrapped.size();
    }

    @Override
    public boolean contains(Object element) {
        return this.wrapped.contains(this.unwrapper.invoke(element));
    }

    @Override
    public boolean containsAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable $this$forEach$iv = elements;
        boolean $i$f$forEach = false;
        Iterator iterator = $this$forEach$iv.iterator();
        while (iterator.hasNext()) {
            Object element$iv;
            Object it = element$iv = iterator.next();
            boolean bl = false;
            if (!this.wrapped.contains(this.unwrapper.invoke(it))) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.wrapped.isEmpty();
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new WrappedCollectionIterator(this.wrapped.iterator(), this.wrapper);
    }

    @NotNull
    public final C getWrapped() {
        return this.wrapped;
    }

    @NotNull
    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    @NotNull
    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedCollection(@NotNull C wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        this.wrapped = wrapped;
        this.unwrapper = unwrapper;
        this.wrapper = wrapper;
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        return CollectionToArray.toArray(this, TArray);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\b\n(\n\b\n\n\b\n\n\b\u0000*\b*\b2\bH0B'\f\b80880¢J\t\f0\rHJ8H¢R880¢\b\n\u0000\b\b\tR\b80¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedCollection$WrappedCollectionIterator;", "O", "T", "", "wrapped", "unwrapper", "Lkotlin/Function1;", "(Ljava/util/Iterator;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/Iterator;", "hasNext", "", "next", "()Ljava/lang/Object;", "Pride"})
    public static final class WrappedCollectionIterator<O, T>
    implements Iterator<T>,
    KMappedMarker {
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

        @NotNull
        public final Iterator<O> getWrapped() {
            return this.wrapped;
        }

        @NotNull
        public final Function1<O, T> getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedCollectionIterator(@NotNull Iterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> unwrapper) {
            Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
            Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
            this.wrapped = wrapped;
            this.unwrapper = unwrapper;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }
}
