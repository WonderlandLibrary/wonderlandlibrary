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

public class WrappedMutableCollection
extends WrappedCollection
implements KMutableCollection,
Collection {
    public WrappedMutableCollection(Collection collection, Function1 function1, Function1 function12) {
        super(collection, function1, function12);
    }

    @Override
    public boolean remove(Object object) {
        return this.getWrapped().remove(this.getUnwrapper().invoke(object));
    }

    @Override
    public boolean addAll(Collection collection) {
        Iterable iterable = collection;
        Function1 function1 = this.getUnwrapper();
        Collection collection2 = this.getWrapped();
        boolean bl = false;
        Iterable iterable2 = iterable;
        Collection collection3 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
        boolean bl2 = false;
        for (Object t : iterable2) {
            collection3.add(function1.invoke(t));
        }
        List list = (List)collection3;
        return collection2.addAll(list);
    }

    @Override
    public boolean retainAll(Collection collection) {
        Iterable iterable = collection;
        Function1 function1 = this.getUnwrapper();
        Collection collection2 = this.getWrapped();
        boolean bl = false;
        Iterable iterable2 = iterable;
        Collection collection3 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
        boolean bl2 = false;
        for (Object t : iterable2) {
            collection3.add(function1.invoke(t));
        }
        List list = (List)collection3;
        return collection2.addAll(list);
    }

    @Override
    public boolean removeAll(Collection collection) {
        Iterable iterable = collection;
        Function1 function1 = this.getUnwrapper();
        Collection collection2 = this.getWrapped();
        boolean bl = false;
        Iterable iterable2 = iterable;
        Collection collection3 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
        boolean bl2 = false;
        for (Object t : iterable2) {
            collection3.add(function1.invoke(t));
        }
        List list = (List)collection3;
        return collection2.addAll(list);
    }

    @Override
    public boolean add(Object object) {
        return this.getWrapped().add(this.getUnwrapper().invoke(object));
    }

    @Override
    public void clear() {
        this.getWrapped().clear();
    }

    @Override
    public Iterator iterator() {
        return new WrappedCollectionIterator(this.getWrapped().iterator(), this.getWrapper());
    }

    public static final class WrappedCollectionIterator
    implements Iterator,
    KMutableIterator {
        private final Iterator wrapped;
        private final Function1 unwrapper;

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        public final Function1 getUnwrapper() {
            return this.unwrapper;
        }

        @Override
        public void remove() {
            this.wrapped.remove();
        }

        public Object next() {
            return this.unwrapper.invoke(this.wrapped.next());
        }

        public final Iterator getWrapped() {
            return this.wrapped;
        }

        public WrappedCollectionIterator(Iterator iterator2, Function1 function1) {
            this.wrapped = iterator2;
            this.unwrapper = function1;
        }
    }
}

