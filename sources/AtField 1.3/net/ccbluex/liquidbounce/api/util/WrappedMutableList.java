/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.markers.KMutableList
 *  kotlin.jvm.internal.markers.KMutableListIterator
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;
import net.ccbluex.liquidbounce.api.util.WrappedMutableCollection;

public final class WrappedMutableList
extends WrappedMutableCollection
implements List,
KMutableList {
    public boolean addAll(int n, Collection collection) {
        Iterable iterable = collection;
        Function1 function1 = this.getUnwrapper();
        int n2 = n;
        List list = (List)this.getWrapped();
        boolean bl = false;
        Iterable iterable2 = iterable;
        Collection collection2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
        boolean bl2 = false;
        for (Object t : iterable2) {
            collection2.add(function1.invoke(t));
        }
        List list2 = (List)collection2;
        return list.addAll(n2, list2);
    }

    public final Object remove(int n) {
        return this.removeAt(n);
    }

    public Object get(int n) {
        return this.getWrapper().invoke(((List)this.getWrapped()).get(n));
    }

    public ListIterator listIterator() {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper(), this.getUnwrapper());
    }

    @Override
    public int indexOf(Object object) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(object));
    }

    public List subList(int n, int n2) {
        return new WrappedMutableList(((List)this.getWrapped()).subList(n, n2), this.getUnwrapper(), this.getWrapper());
    }

    public void add(int n, Object object) {
        ((List)this.getWrapped()).add(n, this.getUnwrapper().invoke(object));
    }

    public Object removeAt(int n) {
        return this.getWrapper().invoke(((List)this.getWrapped()).remove(n));
    }

    public WrappedMutableList(List list, Function1 function1, Function1 function12) {
        super(list, function1, function12);
    }

    @Override
    public int lastIndexOf(Object object) {
        return ((List)this.getWrapped()).lastIndexOf(this.getUnwrapper().invoke(object));
    }

    public Object set(int n, Object object) {
        return this.getWrapper().invoke(((List)this.getWrapped()).set(n, this.getUnwrapper().invoke(object)));
    }

    public ListIterator listIterator(int n) {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(n), this.getWrapper(), this.getUnwrapper());
    }

    public static final class WrappedMutableCollectionIterator
    implements KMutableListIterator,
    ListIterator {
        private final ListIterator wrapped;
        private final Function1 wrapper;
        private final Function1 unwrapper;

        @Override
        public Object next() {
            return this.wrapper.invoke(this.wrapped.next());
        }

        public Object previous() {
            return this.wrapper.invoke(this.wrapped.previous());
        }

        @Override
        public int nextIndex() {
            return this.wrapped.nextIndex();
        }

        @Override
        public int previousIndex() {
            return this.wrapped.previousIndex();
        }

        @Override
        public boolean hasPrevious() {
            return this.wrapped.hasPrevious();
        }

        public WrappedMutableCollectionIterator(ListIterator listIterator, Function1 function1, Function1 function12) {
            this.wrapped = listIterator;
            this.wrapper = function1;
            this.unwrapper = function12;
        }

        public final ListIterator getWrapped() {
            return this.wrapped;
        }

        public void set(Object object) {
            this.wrapped.set(this.unwrapper.invoke(object));
        }

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        public void add(Object object) {
            this.wrapped.add(this.unwrapper.invoke(object));
        }

        public final Function1 getUnwrapper() {
            return this.unwrapper;
        }

        public final Function1 getWrapper() {
            return this.wrapper;
        }

        @Override
        public void remove() {
            this.wrapped.remove();
        }
    }
}

