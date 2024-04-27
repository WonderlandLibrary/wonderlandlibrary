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

public class WrappedCollection
implements Collection,
KMappedMarker {
    private final Function1 unwrapper;
    private final Function1 wrapper;
    private final Collection wrapped;

    @Override
    public boolean isEmpty() {
        return this.wrapped.isEmpty();
    }

    @Override
    public boolean contains(Object object) {
        return this.wrapped.contains(this.unwrapper.invoke(object));
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean containsAll(Collection collection) {
        Iterable iterable = collection;
        boolean bl = false;
        Iterator iterator2 = iterable.iterator();
        while (iterator2.hasNext()) {
            Object t;
            Object t2 = t = iterator2.next();
            boolean bl2 = false;
            if (!this.wrapped.contains(this.unwrapper.invoke(t2))) continue;
            return true;
        }
        return false;
    }

    public boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final Collection getWrapped() {
        return this.wrapped;
    }

    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray((Collection)this);
    }

    public final Function1 getUnwrapper() {
        return this.unwrapper;
    }

    public boolean removeIf(Predicate predicate) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public WrappedCollection(Collection collection, Function1 function1, Function1 function12) {
        this.wrapped = collection;
        this.unwrapper = function1;
        this.wrapper = function12;
    }

    @Override
    public Iterator iterator() {
        return new WrappedCollectionIterator(this.wrapped.iterator(), this.wrapper);
    }

    @Override
    public final int size() {
        return this.getSize();
    }

    public int getSize() {
        return this.wrapped.size();
    }

    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final Function1 getWrapper() {
        return this.wrapper;
    }

    public Object[] toArray(Object[] objectArray) {
        return CollectionToArray.toArray((Collection)this, (Object[])objectArray);
    }

    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public static final class WrappedCollectionIterator
    implements KMappedMarker,
    Iterator {
        private final Iterator wrapped;
        private final Function1 unwrapper;

        public final Function1 getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedCollectionIterator(Iterator iterator2, Function1 function1) {
            this.wrapped = iterator2;
            this.unwrapper = function1;
        }

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public Object next() {
            return this.unwrapper.invoke(this.wrapped.next());
        }

        public final Iterator getWrapped() {
            return this.wrapped;
        }
    }
}

