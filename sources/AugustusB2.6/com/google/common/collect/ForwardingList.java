// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import java.util.ListIterator;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;
import java.util.List;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E>
{
    protected ForwardingList() {
    }
    
    @Override
    protected abstract List<E> delegate();
    
    @Override
    public void add(final int index, @ParametricNullness final E element) {
        this.delegate().add(index, element);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean addAll(final int index, final Collection<? extends E> elements) {
        return this.delegate().addAll(index, elements);
    }
    
    @ParametricNullness
    @Override
    public E get(final int index) {
        return this.delegate().get(index);
    }
    
    @Override
    public int indexOf(@CheckForNull final Object element) {
        return this.delegate().indexOf(element);
    }
    
    @Override
    public int lastIndexOf(@CheckForNull final Object element) {
        return this.delegate().lastIndexOf(element);
    }
    
    @Override
    public ListIterator<E> listIterator() {
        return this.delegate().listIterator();
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        return this.delegate().listIterator(index);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E remove(final int index) {
        return this.delegate().remove(index);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E set(final int index, @ParametricNullness final E element) {
        return this.delegate().set(index, element);
    }
    
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return this.delegate().subList(fromIndex, toIndex);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected boolean standardAdd(@ParametricNullness final E element) {
        this.add(this.size(), element);
        return true;
    }
    
    protected boolean standardAddAll(final int index, final Iterable<? extends E> elements) {
        return Lists.addAllImpl((List<Object>)this, index, elements);
    }
    
    protected int standardIndexOf(@CheckForNull final Object element) {
        return Lists.indexOfImpl(this, element);
    }
    
    protected int standardLastIndexOf(@CheckForNull final Object element) {
        return Lists.lastIndexOfImpl(this, element);
    }
    
    protected Iterator<E> standardIterator() {
        return this.listIterator();
    }
    
    protected ListIterator<E> standardListIterator() {
        return this.listIterator(0);
    }
    
    @Beta
    protected ListIterator<E> standardListIterator(final int start) {
        return Lists.listIteratorImpl((List<E>)this, start);
    }
    
    @Beta
    protected List<E> standardSubList(final int fromIndex, final int toIndex) {
        return Lists.subListImpl((List<E>)this, fromIndex, toIndex);
    }
    
    @Beta
    protected boolean standardEquals(@CheckForNull final Object object) {
        return Lists.equalsImpl(this, object);
    }
    
    @Beta
    protected int standardHashCode() {
        return Lists.hashCodeImpl(this);
    }
}
