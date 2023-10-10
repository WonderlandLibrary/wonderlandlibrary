// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import javax.annotation.CheckForNull;
import java.util.function.Consumer;
import java.util.Spliterator;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E>
{
    private final transient EnumSet<E> delegate;
    @LazyInit
    private transient int hashCode;
    
    static ImmutableSet asImmutable(final EnumSet set) {
        switch (set.size()) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                return ImmutableSet.of((Object)Iterables.getOnlyElement((Iterable<E>)set));
            }
            default: {
                return new ImmutableEnumSet(set);
            }
        }
    }
    
    private ImmutableEnumSet(final EnumSet<E> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return this.delegate.spliterator();
    }
    
    @Override
    public void forEach(final Consumer<? super E> action) {
        this.delegate.forEach(action);
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        return this.delegate.contains(object);
    }
    
    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof ImmutableEnumSet) {
            collection = ((ImmutableEnumSet)collection).delegate;
        }
        return this.delegate.containsAll(collection);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    @Override
    public boolean equals(@CheckForNull Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableEnumSet) {
            object = ((ImmutableEnumSet)object).delegate;
        }
        return this.delegate.equals(object);
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    public int hashCode() {
        final int result = this.hashCode;
        return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
    }
    
    @Override
    public String toString() {
        return this.delegate.toString();
    }
    
    @Override
    Object writeReplace() {
        return new EnumSerializedForm((EnumSet<Enum>)this.delegate);
    }
    
    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable
    {
        final EnumSet<E> delegate;
        private static final long serialVersionUID = 0L;
        
        EnumSerializedForm(final EnumSet<E> delegate) {
            this.delegate = delegate;
        }
        
        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone(), null);
        }
    }
}
