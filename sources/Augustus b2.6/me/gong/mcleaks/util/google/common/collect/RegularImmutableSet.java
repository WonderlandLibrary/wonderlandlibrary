// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Spliterators;
import java.util.Spliterator;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSet<E> extends Indexed<E>
{
    static final RegularImmutableSet<Object> EMPTY;
    private final transient Object[] elements;
    @VisibleForTesting
    final transient Object[] table;
    private final transient int mask;
    private final transient int hashCode;
    
    RegularImmutableSet(final Object[] elements, final int hashCode, final Object[] table, final int mask) {
        this.elements = elements;
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }
    
    @Override
    public boolean contains(@Nullable final Object target) {
        final Object[] table = this.table;
        if (target == null || table == null) {
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
    E get(final int i) {
        return (E)this.elements[i];
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this.elements, 1297);
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
        return offset + this.elements.length;
    }
    
    @Override
    ImmutableList<E> createAsList() {
        return (this.table == null) ? ImmutableList.of() : new RegularImmutableAsList<E>(this, this.elements);
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
        EMPTY = new RegularImmutableSet<Object>(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
    }
}
