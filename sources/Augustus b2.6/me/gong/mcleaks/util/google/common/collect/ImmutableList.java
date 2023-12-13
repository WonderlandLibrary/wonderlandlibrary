// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.io.Serializable;
import java.util.ListIterator;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Spliterator;
import java.util.function.UnaryOperator;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.util.stream.Collector;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.RandomAccess;
import java.util.List;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess
{
    @Beta
    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return CollectCollectors.toImmutableList();
    }
    
    public static <E> ImmutableList<E> of() {
        return (ImmutableList<E>)RegularImmutableList.EMPTY;
    }
    
    public static <E> ImmutableList<E> of(final E element) {
        return new SingletonImmutableList<E>(element);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2) {
        return construct(e1, e2);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3) {
        return construct(e1, e2, e3);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4) {
        return construct(e1, e2, e3, e4);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return construct(e1, e2, e3, e4, e5);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6) {
        return construct(e1, e2, e3, e4, e5, e6);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7) {
        return construct(e1, e2, e3, e4, e5, e6, e7);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }
    
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10, final E e11) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
    }
    
    @SafeVarargs
    public static <E> ImmutableList<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E e7, final E e8, final E e9, final E e10, final E e11, final E e12, final E... others) {
        final Object[] array = new Object[12 + others.length];
        array[0] = e1;
        array[1] = e2;
        array[2] = e3;
        array[3] = e4;
        array[4] = e5;
        array[5] = e6;
        array[6] = e7;
        array[7] = e8;
        array[8] = e9;
        array[9] = e10;
        array[10] = e11;
        array[11] = e12;
        System.arraycopy(others, 0, array, 12, others.length);
        return construct(array);
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterable<? extends E> elements) {
        Preconditions.checkNotNull(elements);
        return (elements instanceof Collection) ? copyOf((Collection<? extends E>)(Collection)elements) : copyOf(elements.iterator());
    }
    
    public static <E> ImmutableList<E> copyOf(final Collection<? extends E> elements) {
        if (elements instanceof ImmutableCollection) {
            final ImmutableList<E> list = ((ImmutableCollection)elements).asList();
            return list.isPartialView() ? asImmutableList(list.toArray()) : list;
        }
        return construct(elements.toArray());
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        final E first = (E)elements.next();
        if (!elements.hasNext()) {
            return of(first);
        }
        return new Builder<E>().add(first).addAll(elements).build();
    }
    
    public static <E> ImmutableList<E> copyOf(final E[] elements) {
        switch (elements.length) {
            case 0: {
                return of();
            }
            case 1: {
                return new SingletonImmutableList<E>(elements[0]);
            }
            default: {
                return new RegularImmutableList<E>(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
            }
        }
    }
    
    public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(final Iterable<? extends E> elements) {
        final Comparable[] array = Iterables.toArray(elements, new Comparable[0]);
        ObjectArrays.checkElementsNotNull((Object[])array);
        Arrays.sort(array);
        return asImmutableList(array);
    }
    
    public static <E> ImmutableList<E> sortedCopyOf(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        final E[] array = (E[])Iterables.toArray(elements);
        ObjectArrays.checkElementsNotNull((Object[])array);
        Arrays.sort(array, comparator);
        return asImmutableList(array);
    }
    
    private static <E> ImmutableList<E> construct(final Object... elements) {
        return (ImmutableList<E>)asImmutableList(ObjectArrays.checkElementsNotNull(elements));
    }
    
    static <E> ImmutableList<E> asImmutableList(final Object[] elements) {
        return asImmutableList(elements, elements.length);
    }
    
    static <E> ImmutableList<E> asImmutableList(Object[] elements, final int length) {
        switch (length) {
            case 0: {
                return of();
            }
            case 1: {
                final ImmutableList<E> list = new SingletonImmutableList<E>((E)elements[0]);
                return list;
            }
            default: {
                if (length < elements.length) {
                    elements = Arrays.copyOf(elements, length);
                }
                return new RegularImmutableList<E>(elements);
            }
        }
    }
    
    ImmutableList() {
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.listIterator();
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int index) {
        return new AbstractIndexedListIterator<E>(this.size(), index) {
            @Override
            protected E get(final int index) {
                return ImmutableList.this.get(index);
            }
        };
    }
    
    @Override
    public void forEach(final Consumer<? super E> consumer) {
        Preconditions.checkNotNull(consumer);
        for (int n = this.size(), i = 0; i < n; ++i) {
            consumer.accept(this.get(i));
        }
    }
    
    @Override
    public int indexOf(@Nullable final Object object) {
        return (object == null) ? -1 : Lists.indexOfImpl(this, object);
    }
    
    @Override
    public int lastIndexOf(@Nullable final Object object) {
        return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return this.indexOf(object) >= 0;
    }
    
    @Override
    public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
        Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
        final int length = toIndex - fromIndex;
        if (length == this.size()) {
            return this;
        }
        switch (length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(this.get(fromIndex));
            }
            default: {
                return this.subListUnchecked(fromIndex, toIndex);
            }
        }
    }
    
    ImmutableList<E> subListUnchecked(final int fromIndex, final int toIndex) {
        return new SubList(fromIndex, toIndex - fromIndex);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final boolean addAll(final int index, final Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final E set(final int index, final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void add(final int index, final E element) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final E remove(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void replaceAll(final UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void sort(final Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final ImmutableList<E> asList() {
        return this;
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return CollectSpliterators.indexed(this.size(), 1296, this::get);
    }
    
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        final int size = this.size();
        for (int i = 0; i < size; ++i) {
            dst[offset + i] = this.get(i);
        }
        return offset + size;
    }
    
    public ImmutableList<E> reverse() {
        return (this.size() <= 1) ? this : new ReverseImmutableList<E>(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        return Lists.equalsImpl(this, obj);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (int n = this.size(), i = 0; i < n; ++i) {
            hashCode = 31 * hashCode + this.get(i).hashCode();
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this.toArray());
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    class SubList extends ImmutableList<E>
    {
        final transient int offset;
        final transient int length;
        
        SubList(final int offset, final int length) {
            this.offset = offset;
            this.length = length;
        }
        
        @Override
        public int size() {
            return this.length;
        }
        
        @Override
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.length);
            return ImmutableList.this.get(index + this.offset);
        }
        
        @Override
        public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
            return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private static class ReverseImmutableList<E> extends ImmutableList<E>
    {
        private final transient ImmutableList<E> forwardList;
        
        ReverseImmutableList(final ImmutableList<E> backingList) {
            this.forwardList = backingList;
        }
        
        private int reverseIndex(final int index) {
            return this.size() - 1 - index;
        }
        
        private int reversePosition(final int index) {
            return this.size() - index;
        }
        
        @Override
        public ImmutableList<E> reverse() {
            return this.forwardList;
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            return this.forwardList.contains(object);
        }
        
        @Override
        public int indexOf(@Nullable final Object object) {
            final int index = this.forwardList.lastIndexOf(object);
            return (index >= 0) ? this.reverseIndex(index) : -1;
        }
        
        @Override
        public int lastIndexOf(@Nullable final Object object) {
            final int index = this.forwardList.indexOf(object);
            return (index >= 0) ? this.reverseIndex(index) : -1;
        }
        
        @Override
        public ImmutableList<E> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)).reverse();
        }
        
        @Override
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.forwardList.get(this.reverseIndex(index));
        }
        
        @Override
        public int size() {
            return this.forwardList.size();
        }
        
        @Override
        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }
    }
    
    static class SerializedForm implements Serializable
    {
        final Object[] elements;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Object[] elements) {
            this.elements = elements;
        }
        
        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }
    
    public static final class Builder<E> extends ArrayBasedBuilder<E>
    {
        public Builder() {
            this(4);
        }
        
        Builder(final int capacity) {
            super(capacity);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E element) {
            super.add(element);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<E> combine(final ArrayBasedBuilder<E> builder) {
            super.combine(builder);
            return this;
        }
        
        @Override
        public ImmutableList<E> build() {
            return ImmutableList.asImmutableList(this.contents, this.size);
        }
    }
}
