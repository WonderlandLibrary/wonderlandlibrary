/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.markers.KMutableCollection
 *  kotlin.jvm.internal.markers.KMutableIterator
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMutableCollection;
import kotlin.jvm.internal.markers.KMutableIterator;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public class WrappedMutableCollection<O, T, C extends Collection<O>>
extends WrappedCollection<O, T, C>
implements Collection<T>,
KMutableCollection {
    @Override
    public boolean add(T element) {
        return this.getWrapped().add((Object)this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean addAll(Collection<? extends T> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    @Override
    public void clear() {
        this.getWrapped().clear();
    }

    @Override
    public Iterator<T> iterator() {
        return new WrappedCollectionIterator(this.getWrapped().iterator(), this.getWrapper());
    }

    @Override
    public boolean remove(Object element) {
        return this.getWrapped().remove(this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean removeAll(Collection<? extends Object> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean retainAll(Collection<? extends Object> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    public WrappedMutableCollection(C wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
        super(wrapped, unwrapper, wrapper);
    }

    public static final class WrappedCollectionIterator<O, T>
    implements Iterator<T>,
    KMutableIterator {
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

        @Override
        public void remove() {
            this.wrapped.remove();
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
    }
}

