// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ListIterator;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.util.function.Consumer;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
class RegularImmutableAsList<E> extends ImmutableAsList<E>
{
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;
    
    RegularImmutableAsList(final ImmutableCollection<E> delegate, final ImmutableList<? extends E> delegateList) {
        this.delegate = delegate;
        this.delegateList = delegateList;
    }
    
    RegularImmutableAsList(final ImmutableCollection<E> delegate, final Object[] array) {
        this(delegate, ImmutableList.asImmutableList(array));
    }
    
    @Override
    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }
    
    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return (UnmodifiableListIterator<E>)this.delegateList.listIterator(index);
    }
    
    @GwtIncompatible
    @Override
    public void forEach(final Consumer<? super E> action) {
        this.delegateList.forEach(action);
    }
    
    @GwtIncompatible
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return this.delegateList.copyIntoArray(dst, offset);
    }
    
    @CheckForNull
    @Override
    Object[] internalArray() {
        return this.delegateList.internalArray();
    }
    
    @Override
    int internalArrayStart() {
        return this.delegateList.internalArrayStart();
    }
    
    @Override
    int internalArrayEnd() {
        return this.delegateList.internalArrayEnd();
    }
    
    @Override
    public E get(final int index) {
        return (E)this.delegateList.get(index);
    }
}
