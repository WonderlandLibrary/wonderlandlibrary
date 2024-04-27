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

public final class WrappedMutableList<O, T, C extends List<O>>
extends WrappedMutableCollection<O, T, C>
implements List<T>,
KMutableList {
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
        return ((List)this.getWrapped()).lastIndexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    public void add(int index, T element) {
        ((List)this.getWrapped()).add(index, this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        int n = index;
        List list = (List)this.getWrapped();
        boolean $i$f$map = false;
        void var6_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list2 = (List)destination$iv$iv;
        return list.addAll(n, list2);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper(), this.getUnwrapper());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(index), this.getWrapper(), this.getUnwrapper());
    }

    public T removeAt(int index) {
        return (T)this.getWrapper().invoke(((List)this.getWrapped()).remove(index));
    }

    @Override
    public T set(int index, T element) {
        return (T)this.getWrapper().invoke(((List)this.getWrapped()).set(index, this.getUnwrapper().invoke(element)));
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new WrappedMutableList(((List)this.getWrapped()).subList(fromIndex, toIndex), this.getUnwrapper(), this.getWrapper());
    }

    public WrappedMutableList(C wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
        super((Collection)wrapped, unwrapper, wrapper);
    }

    public static final class WrappedMutableCollectionIterator<O, T>
    implements ListIterator<T>,
    KMutableListIterator {
        private final ListIterator<O> wrapped;
        private final Function1<O, T> wrapper;
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

        public final ListIterator<O> getWrapped() {
            return this.wrapped;
        }

        public final Function1<O, T> getWrapper() {
            return this.wrapper;
        }

        public final Function1<T, O> getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedMutableCollectionIterator(ListIterator<O> wrapped, Function1<? super O, ? extends T> wrapper, Function1<? super T, ? extends O> unwrapper) {
            this.wrapped = wrapped;
            this.wrapper = wrapper;
            this.unwrapper = unwrapper;
        }
    }
}

