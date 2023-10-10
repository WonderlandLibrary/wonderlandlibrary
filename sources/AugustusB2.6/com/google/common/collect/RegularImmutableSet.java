// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.Spliterator;
import javax.annotation.CheckForNull;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSet<E> extends CachingAsList<E>
{
    private static final Object[] EMPTY_ARRAY;
    static final RegularImmutableSet<Object> EMPTY;
    private final transient Object[] elements;
    private final transient int hashCode;
    @VisibleForTesting
    final transient Object[] table;
    private final transient int mask;
    
    RegularImmutableSet(final Object[] elements, final int hashCode, final Object[] table, final int mask) {
        this.elements = elements;
        this.hashCode = hashCode;
        this.table = table;
        this.mask = mask;
    }
    
    @Override
    public boolean contains(@CheckForNull final Object target) {
        final Object[] table = this.table;
        if (target == null || table.length == 0) {
            return false;
        }
        int i = Hashing.smearedHash(target);
        while (true) {
            i &= this.mask;
            final Object candidate = table[i];
            if (candidate == null) {
                return false;
            }
            if (candidate.equals(target)) {
                return true;
            }
            ++i;
        }
    }
    
    @Override
    public int size() {
        return this.elements.length;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.forArray((E[])this.elements);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this.elements, 1297);
    }
    
    @Override
    Object[] internalArray() {
        return this.elements;
    }
    
    @Override
    int internalArrayStart() {
        return 0;
    }
    
    @Override
    int internalArrayEnd() {
        return this.elements.length;
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
        return offset + this.elements.length;
    }
    
    @Override
    ImmutableList<E> createAsList() {
        return (this.table.length == 0) ? ImmutableList.of() : new RegularImmutableAsList<E>(this, this.elements);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    static {
        EMPTY_ARRAY = new Object[0];
        EMPTY = new RegularImmutableSet<Object>(RegularImmutableSet.EMPTY_ARRAY, 0, RegularImmutableSet.EMPTY_ARRAY, 0);
    }
}
