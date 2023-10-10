// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
    final transient E element;
    
    SingletonImmutableSet(final E element) {
        this.element = Preconditions.checkNotNull(element);
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public boolean contains(@CheckForNull final Object target) {
        return this.element.equals(target);
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }
    
    @Override
    public ImmutableList<E> asList() {
        return ImmutableList.of(this.element);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        dst[offset] = this.element;
        return offset + 1;
    }
    
    @Override
    public final int hashCode() {
        return this.element.hashCode();
    }
    
    @Override
    public String toString() {
        final String string = this.element.toString();
        return new StringBuilder(2 + String.valueOf(string).length()).append('[').append(string).append(']').toString();
    }
}
