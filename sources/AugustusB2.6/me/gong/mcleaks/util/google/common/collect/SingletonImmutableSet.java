// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Iterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
    final transient E element;
    @LazyInit
    private transient int cachedHashCode;
    
    SingletonImmutableSet(final E element) {
        this.element = Preconditions.checkNotNull(element);
    }
    
    SingletonImmutableSet(final E element, final int hashCode) {
        this.element = element;
        this.cachedHashCode = hashCode;
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public boolean contains(final Object target) {
        return this.element.equals(target);
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }
    
    @Override
    ImmutableList<E> createAsList() {
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
        int code = this.cachedHashCode;
        if (code == 0) {
            code = (this.cachedHashCode = this.element.hashCode());
        }
        return code;
    }
    
    @Override
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }
    
    @Override
    public String toString() {
        return '[' + this.element.toString() + ']';
    }
}
