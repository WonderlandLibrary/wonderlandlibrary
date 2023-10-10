// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ListIterator;
import javax.annotation.CheckForNull;
import com.google.common.math.IntMath;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;
import java.util.RandomAccess;
import java.util.List;
import java.util.AbstractList;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess
{
    private final transient ImmutableList<List<E>> axes;
    private final transient int[] axesSizeProduct;
    
    static <E> List<List<E>> create(final List<? extends List<? extends E>> lists) {
        final ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder<List<E>>(lists.size());
        for (final List<? extends E> list : lists) {
            final List<E> copy = (List<E>)ImmutableList.copyOf((Collection<?>)list);
            if (copy.isEmpty()) {
                return (List<List<E>>)ImmutableList.of();
            }
            axesBuilder.add(copy);
        }
        return (List<List<E>>)new CartesianList((ImmutableList<List<Object>>)axesBuilder.build());
    }
    
    CartesianList(final ImmutableList<List<E>> axes) {
        this.axes = axes;
        final int[] axesSizeProduct = new int[axes.size() + 1];
        axesSizeProduct[axes.size()] = 1;
        try {
            for (int i = axes.size() - 1; i >= 0; --i) {
                axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], axes.get(i).size());
            }
        }
        catch (ArithmeticException e) {
            throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
        }
        this.axesSizeProduct = axesSizeProduct;
    }
    
    private int getAxisIndexForProductIndex(final int index, final int axis) {
        return index / this.axesSizeProduct[axis + 1] % this.axes.get(axis).size();
    }
    
    @Override
    public int indexOf(@CheckForNull final Object o) {
        if (!(o instanceof List)) {
            return -1;
        }
        final List<?> list = (List<?>)o;
        if (list.size() != this.axes.size()) {
            return -1;
        }
        final ListIterator<?> itr = list.listIterator();
        int computedIndex = 0;
        while (itr.hasNext()) {
            final int axisIndex = itr.nextIndex();
            final int elemIndex = this.axes.get(axisIndex).indexOf(itr.next());
            if (elemIndex == -1) {
                return -1;
            }
            computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1];
        }
        return computedIndex;
    }
    
    @Override
    public int lastIndexOf(@CheckForNull final Object o) {
        if (!(o instanceof List)) {
            return -1;
        }
        final List<?> list = (List<?>)o;
        if (list.size() != this.axes.size()) {
            return -1;
        }
        final ListIterator<?> itr = list.listIterator();
        int computedIndex = 0;
        while (itr.hasNext()) {
            final int axisIndex = itr.nextIndex();
            final int elemIndex = this.axes.get(axisIndex).lastIndexOf(itr.next());
            if (elemIndex == -1) {
                return -1;
            }
            computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1];
        }
        return computedIndex;
    }
    
    @Override
    public ImmutableList<E> get(final int index) {
        Preconditions.checkElementIndex(index, this.size());
        return new ImmutableList<E>() {
            @Override
            public int size() {
                return CartesianList.this.axes.size();
            }
            
            @Override
            public E get(final int axis) {
                Preconditions.checkElementIndex(axis, this.size());
                final int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
                return ((List)CartesianList.this.axes.get(axis)).get(axisIndex);
            }
            
            @Override
            boolean isPartialView() {
                return true;
            }
        };
    }
    
    @Override
    public int size() {
        return this.axesSizeProduct[0];
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        if (!(object instanceof List)) {
            return false;
        }
        final List<?> list = (List<?>)object;
        if (list.size() != this.axes.size()) {
            return false;
        }
        int i = 0;
        for (final Object o : list) {
            if (!this.axes.get(i).contains(o)) {
                return false;
            }
            ++i;
        }
        return true;
    }
}
