// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.function.Predicate;
import com.google.errorprone.annotations.DoNotCall;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.AbstractCollection;

@DoNotMock("Use ImmutableList.of or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable
{
    static final int SPLITERATOR_CHARACTERISTICS = 1296;
    private static final Object[] EMPTY_ARRAY;
    
    ImmutableCollection() {
    }
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator((Collection<? extends E>)this, 1296);
    }
    
    @Override
    public final Object[] toArray() {
        return this.toArray(ImmutableCollection.EMPTY_ARRAY);
    }
    
    @CanIgnoreReturnValue
    @Override
    public final <T> T[] toArray(T[] other) {
        Preconditions.checkNotNull(other);
        final int size = this.size();
        if (other.length < size) {
            final Object[] internal = this.internalArray();
            if (internal != null) {
                return Platform.copy(internal, this.internalArrayStart(), this.internalArrayEnd(), other);
            }
            other = ObjectArrays.newArray(other, size);
        }
        else if (other.length > size) {
            other[size] = null;
        }
        this.copyIntoArray(other, 0);
        return other;
    }
    
    @CheckForNull
    Object[] internalArray() {
        return null;
    }
    
    int internalArrayStart() {
        throw new UnsupportedOperationException();
    }
    
    int internalArrayEnd() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract boolean contains(@CheckForNull final Object p0);
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean remove(@CheckForNull final Object object) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean addAll(final Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean removeAll(final Collection<?> oldElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean retainAll(final Collection<?> elementsToKeep) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableList<E> asList() {
        switch (this.size()) {
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return ImmutableList.of(this.iterator().next());
            }
            default: {
                return new RegularImmutableAsList<E>(this, this.toArray());
            }
        }
    }
    
    abstract boolean isPartialView();
    
    @CanIgnoreReturnValue
    int copyIntoArray(final Object[] dst, int offset) {
        for (final E e : this) {
            dst[offset++] = e;
        }
        return offset;
    }
    
    Object writeReplace() {
        return new ImmutableList.SerializedForm(this.toArray());
    }
    
    static {
        EMPTY_ARRAY = new Object[0];
    }
    
    @DoNotMock
    public abstract static class Builder<E>
    {
        static final int DEFAULT_INITIAL_CAPACITY = 4;
        
        static int expandedCapacity(final int oldCapacity, final int minCapacity) {
            if (minCapacity < 0) {
                throw new AssertionError((Object)"cannot store more than MAX_VALUE elements");
            }
            int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            return newCapacity;
        }
        
        Builder() {
        }
        
        @CanIgnoreReturnValue
        public abstract Builder<E> add(final E p0);
        
        @CanIgnoreReturnValue
        public Builder<E> add(final E... elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            for (final E element : elements) {
                this.add(element);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                this.add(elements.next());
            }
            return this;
        }
        
        public abstract ImmutableCollection<E> build();
    }
}
