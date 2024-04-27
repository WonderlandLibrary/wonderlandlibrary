/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public class WrappedList<O, T, C extends List<? extends O>>
extends WrappedCollection<O, T, C>
implements List<T>,
KMappedMarker {
    @Override
    public T get(int index) {
        return (T)this.getWrapper().invoke(((List)this.getWrapped()).get(index));
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
    public ListIterator<T> listIterator() {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(index), this.getWrapper());
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new WrappedList(((List)this.getWrapped()).subList(fromIndex, toIndex), this.getUnwrapper(), this.getWrapper());
    }

    public WrappedList(C wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
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

    public static final class WrappedCollectionIterator<O, T>
    implements ListIterator<T>,
    KMappedMarker {
        private final ListIterator<O> wrapped;
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
            return (T)this.wrapper.invoke(this.wrapped.next());
        }

        @Override
        public int nextIndex() {
            return this.wrapped.nextIndex();
        }

        @Override
        public T previous() {
            return (T)this.wrapper.invoke(this.wrapped.previous());
        }

        @Override
        public int previousIndex() {
            return this.wrapped.previousIndex();
        }

        public final ListIterator<O> getWrapped() {
            return this.wrapped;
        }

        public final Function1<O, T> getWrapper() {
            return this.wrapper;
        }

        public WrappedCollectionIterator(ListIterator<? extends O> wrapped, Function1<? super O, ? extends T> wrapper) {
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

