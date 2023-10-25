package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
  private static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
  
  public static <E> ImmutableList<E> of() {
    return (ImmutableList)EMPTY;
  }
  
  public static <E> ImmutableList<E> of(E element) {
    return new SingletonImmutableList<E>(element);
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2) {
    return construct(new Object[] { e1, e2 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
    return construct(new Object[] { e1, e2, e3 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
    return construct(new Object[] { e1, e2, e3, e4 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
    return construct(new Object[] { e1, e2, e3, e4, e5 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
    return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
    return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
    return construct(new Object[] { 
          e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, 
          e11 });
  }
  
  public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
    Object[] array = new Object[12 + others.length];
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
  
  public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
    Preconditions.checkNotNull(elements);
    return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
  }
  
  public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
    if (elements instanceof ImmutableCollection) {
      ImmutableList<E> list = ((ImmutableCollection)elements).asList();
      return list.isPartialView() ? asImmutableList(list.toArray()) : list;
    } 
    return construct(elements.toArray());
  }
  
  public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
    if (!elements.hasNext())
      return of(); 
    E first = elements.next();
    if (!elements.hasNext())
      return of(first); 
    return (new Builder<E>()).add(first).addAll(elements).build();
  }
  
  public static <E> ImmutableList<E> copyOf(E[] elements) {
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(elements[0]);
    } 
    return new RegularImmutableList<E>(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
  }
  
  private static <E> ImmutableList<E> construct(Object... elements) {
    return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
  }
  
  static <E> ImmutableList<E> asImmutableList(Object[] elements) {
    return asImmutableList(elements, elements.length);
  }
  
  static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
    ImmutableList<E> list;
    switch (length) {
      case 0:
        return of();
      case 1:
        list = new SingletonImmutableList<E>((E)elements[0]);
        return list;
    } 
    if (length < elements.length)
      elements = ObjectArrays.arraysCopyOf(elements, length); 
    return new RegularImmutableList<E>(elements);
  }
  
  public UnmodifiableIterator<E> iterator() {
    return listIterator();
  }
  
  public UnmodifiableListIterator<E> listIterator() {
    return listIterator(0);
  }
  
  public UnmodifiableListIterator<E> listIterator(int index) {
    return new AbstractIndexedListIterator<E>(size(), index) {
        protected E get(int index) {
          return ImmutableList.this.get(index);
        }
      };
  }
  
  public int indexOf(@Nullable Object object) {
    return (object == null) ? -1 : Lists.indexOfImpl(this, object);
  }
  
  public int lastIndexOf(@Nullable Object object) {
    return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
  }
  
  public boolean contains(@Nullable Object object) {
    return (indexOf(object) >= 0);
  }
  
  public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
    } 
    return subListUnchecked(fromIndex, toIndex);
  }
  
  ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
    return new SubList(fromIndex, toIndex - fromIndex);
  }
  
  class SubList extends ImmutableList<E> {
    final transient int offset;
    
    final transient int length;
    
    SubList(int offset, int length) {
      this.offset = offset;
      this.length = length;
    }
    
    public int size() {
      return this.length;
    }
    
    public E get(int index) {
      Preconditions.checkElementIndex(index, this.length);
      return ImmutableList.this.get(index + this.offset);
    }
    
    public ImmutableList<E> subList(int fromIndex, int toIndex) {
      Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
      return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
    }
    
    boolean isPartialView() {
      return true;
    }
  }
  
  @Deprecated
  public final boolean addAll(int index, Collection<? extends E> newElements) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final E set(int index, E element) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final void add(int index, E element) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final E remove(int index) {
    throw new UnsupportedOperationException();
  }
  
  public final ImmutableList<E> asList() {
    return this;
  }
  
  int copyIntoArray(Object[] dst, int offset) {
    int size = size();
    for (int i = 0; i < size; i++)
      dst[offset + i] = get(i); 
    return offset + size;
  }
  
  public ImmutableList<E> reverse() {
    return new ReverseImmutableList<E>(this);
  }
  
  private static class ReverseImmutableList<E> extends ImmutableList<E> {
    private final transient ImmutableList<E> forwardList;
    
    ReverseImmutableList(ImmutableList<E> backingList) {
      this.forwardList = backingList;
    }
    
    private int reverseIndex(int index) {
      return size() - 1 - index;
    }
    
    private int reversePosition(int index) {
      return size() - index;
    }
    
    public ImmutableList<E> reverse() {
      return this.forwardList;
    }
    
    public boolean contains(@Nullable Object object) {
      return this.forwardList.contains(object);
    }
    
    public int indexOf(@Nullable Object object) {
      int index = this.forwardList.lastIndexOf(object);
      return (index >= 0) ? reverseIndex(index) : -1;
    }
    
    public int lastIndexOf(@Nullable Object object) {
      int index = this.forwardList.indexOf(object);
      return (index >= 0) ? reverseIndex(index) : -1;
    }
    
    public ImmutableList<E> subList(int fromIndex, int toIndex) {
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
      return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
    }
    
    public E get(int index) {
      Preconditions.checkElementIndex(index, size());
      return this.forwardList.get(reverseIndex(index));
    }
    
    public int size() {
      return this.forwardList.size();
    }
    
    boolean isPartialView() {
      return this.forwardList.isPartialView();
    }
  }
  
  public boolean equals(@Nullable Object obj) {
    return Lists.equalsImpl(this, obj);
  }
  
  public int hashCode() {
    int hashCode = 1;
    int n = size();
    for (int i = 0; i < n; i++) {
      hashCode = 31 * hashCode + get(i).hashCode();
      hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
    } 
    return hashCode;
  }
  
  static class SerializedForm implements Serializable {
    final Object[] elements;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(Object[] elements) {
      this.elements = elements;
    }
    
    Object readResolve() {
      return ImmutableList.copyOf(this.elements);
    }
  }
  
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Use SerializedForm");
  }
  
  Object writeReplace() {
    return new SerializedForm(toArray());
  }
  
  public static <E> Builder<E> builder() {
    return new Builder<E>();
  }
  
  public static final class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
    public Builder() {
      this(4);
    }
    
    Builder(int capacity) {
      super(capacity);
    }
    
    public Builder<E> add(E element) {
      super.add(element);
      return this;
    }
    
    public Builder<E> addAll(Iterable<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public Builder<E> add(E... elements) {
      super.add(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterator<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public ImmutableList<E> build() {
      return ImmutableList.asImmutableList(this.contents, this.size);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */