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

public class WrappedList
extends WrappedCollection
implements KMappedMarker,
List {
    @Override
    public int indexOf(Object object) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(object));
    }

    public void replaceAll(UnaryOperator unaryOperator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public Object get(int n) {
        return this.getWrapper().invoke(((List)this.getWrapped()).get(n));
    }

    public Object remove(int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public ListIterator listIterator(int n) {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(n), this.getWrapper());
    }

    public boolean addAll(int n, Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public ListIterator listIterator() {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper());
    }

    public WrappedList(List list, Function1 function1, Function1 function12) {
        super(list, function1, function12);
    }

    public List subList(int n, int n2) {
        return new WrappedList(((List)this.getWrapped()).subList(n, n2), this.getUnwrapper(), this.getWrapper());
    }

    public void add(int n, Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public int lastIndexOf(Object object) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(object));
    }

    public Object set(int n, Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void sort(Comparator comparator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public static final class WrappedCollectionIterator
    implements KMappedMarker,
    ListIterator {
        private final Function1 wrapper;
        private final ListIterator wrapped;

        @Override
        public int nextIndex() {
            return this.wrapped.nextIndex();
        }

        public void set(Object object) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public boolean hasPrevious() {
            return this.wrapped.hasPrevious();
        }

        public final Function1 getWrapper() {
            return this.wrapper;
        }

        public Object previous() {
            return this.wrapper.invoke(this.wrapped.previous());
        }

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        public void add(Object object) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public final ListIterator getWrapped() {
            return this.wrapped;
        }

        @Override
        public int previousIndex() {
            return this.wrapped.previousIndex();
        }

        @Override
        public Object next() {
            return this.wrapper.invoke(this.wrapped.next());
        }

        public WrappedCollectionIterator(ListIterator listIterator, Function1 function1) {
            this.wrapped = listIterator;
            this.wrapper = function1;
        }
    }
}

