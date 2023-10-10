// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import java.util.Set;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.ObjIntConsumer;
import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.primitives.Ints;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class EnumMultiset<E extends Enum<E>> extends AbstractMultiset<E> implements Serializable
{
    private transient Class<E> type;
    private transient E[] enumConstants;
    private transient int[] counts;
    private transient int distinctElements;
    private transient long size;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Class<E> type) {
        return new EnumMultiset<E>(type);
    }
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Iterable<E> elements) {
        final Iterator<E> iterator = elements.iterator();
        Preconditions.checkArgument(iterator.hasNext(), (Object)"EnumMultiset constructor passed empty Iterable");
        final EnumMultiset<E> multiset = new EnumMultiset<E>(iterator.next().getDeclaringClass());
        Iterables.addAll(multiset, (Iterable<? extends E>)elements);
        return multiset;
    }
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Iterable<E> elements, final Class<E> type) {
        final EnumMultiset<E> result = create(type);
        Iterables.addAll(result, (Iterable<? extends E>)elements);
        return result;
    }
    
    private EnumMultiset(final Class<E> type) {
        this.type = type;
        Preconditions.checkArgument(type.isEnum());
        this.enumConstants = type.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
    }
    
    private boolean isActuallyE(@CheckForNull final Object o) {
        if (o instanceof Enum) {
            final Enum<?> e = (Enum<?>)o;
            final int index = e.ordinal();
            return index < this.enumConstants.length && this.enumConstants[index] == e;
        }
        return false;
    }
    
    private void checkIsE(final Object element) {
        Preconditions.checkNotNull(element);
        if (!this.isActuallyE(element)) {
            final String value = String.valueOf(this.type);
            final String value2 = String.valueOf(element);
            throw new ClassCastException(new StringBuilder(21 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Expected an ").append(value).append(" but got ").append(value2).toString());
        }
    }
    
    @Override
    int distinctElements() {
        return this.distinctElements;
    }
    
    @Override
    public int size() {
        return Ints.saturatedCast(this.size);
    }
    
    @Override
    public int count(@CheckForNull final Object element) {
        if (element == null || !this.isActuallyE(element)) {
            return 0;
        }
        final Enum<?> e = (Enum<?>)element;
        return this.counts[e.ordinal()];
    }
    
    @CanIgnoreReturnValue
    @Override
    public int add(final E element, final int occurrences) {
        this.checkIsE(element);
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return this.count(element);
        }
        final int index = element.ordinal();
        final int oldCount = this.counts[index];
        final long newCount = oldCount + (long)occurrences;
        Preconditions.checkArgument(newCount <= 2147483647L, "too many occurrences: %s", newCount);
        this.counts[index] = (int)newCount;
        if (oldCount == 0) {
            ++this.distinctElements;
        }
        this.size += occurrences;
        return oldCount;
    }
    
    @CanIgnoreReturnValue
    @Override
    public int remove(@CheckForNull final Object element, final int occurrences) {
        if (element == null || !this.isActuallyE(element)) {
            return 0;
        }
        final Enum<?> e = (Enum<?>)element;
        CollectPreconditions.checkNonnegative(occurrences, "occurrences");
        if (occurrences == 0) {
            return this.count(element);
        }
        final int index = e.ordinal();
        final int oldCount = this.counts[index];
        if (oldCount == 0) {
            return 0;
        }
        if (oldCount <= occurrences) {
            this.counts[index] = 0;
            --this.distinctElements;
            this.size -= oldCount;
        }
        else {
            this.counts[index] = oldCount - occurrences;
            this.size -= occurrences;
        }
        return oldCount;
    }
    
    @CanIgnoreReturnValue
    @Override
    public int setCount(final E element, final int count) {
        this.checkIsE(element);
        CollectPreconditions.checkNonnegative(count, "count");
        final int index = element.ordinal();
        final int oldCount = this.counts[index];
        this.counts[index] = count;
        this.size += count - oldCount;
        if (oldCount == 0 && count > 0) {
            ++this.distinctElements;
        }
        else if (oldCount > 0 && count == 0) {
            --this.distinctElements;
        }
        return oldCount;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.counts, 0);
        this.size = 0L;
        this.distinctElements = 0;
    }
    
    @Override
    Iterator<E> elementIterator() {
        return new Itr<E>() {
            @Override
            E output(final int index) {
                return EnumMultiset.this.enumConstants[index];
            }
        };
    }
    
    @Override
    Iterator<Multiset.Entry<E>> entryIterator() {
        return new Itr<Multiset.Entry<E>>() {
            @Override
            Multiset.Entry<E> output(final int index) {
                return new Multisets.AbstractEntry<E>() {
                    @Override
                    public E getElement() {
                        return EnumMultiset.this.enumConstants[index];
                    }
                    
                    @Override
                    public int getCount() {
                        return EnumMultiset.this.counts[index];
                    }
                };
            }
        };
    }
    
    @Override
    public void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        for (int i = 0; i < this.enumConstants.length; ++i) {
            if (this.counts[i] > 0) {
                action.accept((Object)this.enumConstants[i], this.counts[i]);
            }
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return Multisets.iteratorImpl((Multiset<E>)this);
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.type);
        Serialization.writeMultiset((Multiset<Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final Class<E> localType = (Class<E>)stream.readObject();
        this.type = localType;
        this.enumConstants = this.type.getEnumConstants();
        this.counts = new int[this.enumConstants.length];
        Serialization.populateMultiset((Multiset<Object>)this, stream);
    }
    
    abstract class Itr<T> implements Iterator<T>
    {
        int index;
        int toRemove;
        
        Itr() {
            this.index = 0;
            this.toRemove = -1;
        }
        
        abstract T output(final int p0);
        
        @Override
        public boolean hasNext() {
            while (this.index < EnumMultiset.this.enumConstants.length) {
                if (EnumMultiset.this.counts[this.index] > 0) {
                    return true;
                }
                ++this.index;
            }
            return false;
        }
        
        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final T result = this.output(this.index);
            this.toRemove = this.index;
            ++this.index;
            return result;
        }
        
        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.toRemove >= 0);
            if (EnumMultiset.this.counts[this.toRemove] > 0) {
                EnumMultiset.this.distinctElements--;
                EnumMultiset.this.size -= EnumMultiset.this.counts[this.toRemove];
                EnumMultiset.this.counts[this.toRemove] = 0;
            }
            this.toRemove = -1;
        }
    }
}
