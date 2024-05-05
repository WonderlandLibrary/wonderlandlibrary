/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.CollectionToArray
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.markers.KMappedMarker;

public class WrappedCollection<O, T, C extends Collection<? extends O>>
implements Collection<T>,
KMappedMarker {
    private final C wrapped;
    private final Function1<T, O> unwrapper;
    private final Function1<O, T> wrapper;

    public int getSize() {
        return this.wrapped.size();
    }

    @Override
    public boolean contains(Object element) {
        return this.wrapped.contains(this.unwrapper.invoke(element));
    }

    @Override
    public boolean containsAll(Collection<? extends Object> elements) {
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
    public Iterator<T> iterator() {
        return new WrappedCollectionIterator(this.wrapped.iterator(), this.wrapper);
    }

    public final C getWrapped() {
        return this.wrapped;
    }

    public final Function1<T, O> getUnwrapper() {
        return this.unwrapper;
    }

    public final Function1<O, T> getWrapper() {
        return this.wrapper;
    }

    public WrappedCollection(C wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
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
        return CollectionToArray.toArray((Collection)this);
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        return CollectionToArray.toArray((Collection)this, (Object[])TArray);
    }

    public static final class WrappedCollectionIterator<O, T>
    implements Iterator<T>,
    KMappedMarker {
        private final Iterator<O> wrapped;
        private final Function1<O, T> unwrapper;

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        @Override
        public T next() {
            return (T)this.unwrapper.invoke(this.wrapped.next());
        }

        public final Iterator<O> getWrapped() {
            return this.wrapped;
        }

        public final Function1<O, T> getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedCollectionIterator(Iterator<? extends O> wrapped, Function1<? super O, ? extends T> unwrapper) {
            this.wrapped = wrapped;
            this.unwrapper = unwrapper;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }
}

