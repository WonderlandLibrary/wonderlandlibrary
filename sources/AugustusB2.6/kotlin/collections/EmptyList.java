// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import kotlin.jvm.internal.CollectionToArray;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import java.util.RandomAccess;
import java.util.List;
import java.io.Serializable;

public final class EmptyList implements Serializable, List, RandomAccess
{
    public static final EmptyList INSTANCE;
    
    @Override
    public final boolean equals(final Object other) {
        return other instanceof List && ((List)other).isEmpty();
    }
    
    @Override
    public final int hashCode() {
        return 1;
    }
    
    @Override
    public final String toString() {
        return "[]";
    }
    
    @Override
    public final /* bridge */ int size() {
        return 0;
    }
    
    @Override
    public final boolean isEmpty() {
        return true;
    }
    
    @Override
    public final /* bridge */ boolean contains(final Object o) {
        if (!(o instanceof Void)) {
            return false;
        }
        Intrinsics.checkParameterIsNotNull(o, "element");
        return false;
    }
    
    @Override
    public final boolean containsAll(final Collection elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return elements.isEmpty();
    }
    
    @Override
    public final /* bridge */ int indexOf(final Object o) {
        if (!(o instanceof Void)) {
            return -1;
        }
        Intrinsics.checkParameterIsNotNull(o, "element");
        return -1;
    }
    
    @Override
    public final /* bridge */ int lastIndexOf(final Object o) {
        if (!(o instanceof Void)) {
            return -1;
        }
        Intrinsics.checkParameterIsNotNull(o, "element");
        return -1;
    }
    
    @Override
    public final Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }
    
    @Override
    public final ListIterator listIterator() {
        return EmptyIterator.INSTANCE;
    }
    
    @Override
    public final ListIterator listIterator(final int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return EmptyIterator.INSTANCE;
    }
    
    @Override
    public final List subList(final int fromIndex, final int toIndex) {
        if (fromIndex == 0 && toIndex == 0) {
            return this;
        }
        throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex);
    }
    
    private EmptyList() {
    }
    
    static {
        INSTANCE = new EmptyList();
    }
    
    @Override
    public final boolean addAll(final int n, final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final boolean addAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final boolean remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final boolean retainAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final Object[] toArray() {
        return CollectionToArray.toArray(this);
    }
    
    @Override
    public final <T> T[] toArray(final T[] a) {
        return (T[])CollectionToArray.toArray(this, a);
    }
}
