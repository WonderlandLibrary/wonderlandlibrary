package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
  static final int MAX_TABLE_SIZE = 1073741824;
  
  private static final double DESIRED_LOAD_FACTOR = 0.7D;
  
  private static final int CUTOFF = 751619276;
  
  public static <E> ImmutableSet<E> of() {
    return EmptyImmutableSet.INSTANCE;
  }
  
  public static <E> ImmutableSet<E> of(E element) {
    return new SingletonImmutableSet<E>(element);
  }
  
  public static <E> ImmutableSet<E> of(E e1, E e2) {
    return construct(2, new Object[] { e1, e2 });
  }
  
  public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
    return construct(3, new Object[] { e1, e2, e3 });
  }
  
  public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
    return construct(4, new Object[] { e1, e2, e3, e4 });
  }
  
  public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
    return construct(5, new Object[] { e1, e2, e3, e4, e5 });
  }
  
  public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
    int paramCount = 6;
    Object[] elements = new Object[6 + others.length];
    elements[0] = e1;
    elements[1] = e2;
    elements[2] = e3;
    elements[3] = e4;
    elements[4] = e5;
    elements[5] = e6;
    System.arraycopy(others, 0, elements, 6, others.length);
    return construct(elements.length, elements);
  }
  
  private static <E> ImmutableSet<E> construct(int n, Object... elements) {
    E elem;
    switch (n) {
      case 0:
        return of();
      case 1:
        elem = (E)elements[0];
        return of(elem);
    } 
    int tableSize = chooseTableSize(n);
    Object[] table = new Object[tableSize];
    int mask = tableSize - 1;
    int hashCode = 0;
    int uniques = 0;
    for (int i = 0; i < n; i++) {
      Object element = ObjectArrays.checkElementNotNull(elements[i], i);
      int hash = element.hashCode();
      for (int j = Hashing.smear(hash);; j++) {
        int index = j & mask;
        Object value = table[index];
        if (value == null) {
          elements[uniques++] = element;
          table[index] = element;
          hashCode += hash;
          break;
        } 
        if (value.equals(element))
          break; 
      } 
    } 
    Arrays.fill(elements, uniques, n, (Object)null);
    if (uniques == 1) {
      E element = (E)elements[0];
      return new SingletonImmutableSet<E>(element, hashCode);
    } 
    if (tableSize != chooseTableSize(uniques))
      return construct(uniques, elements); 
    Object[] uniqueElements = (uniques < elements.length) ? ObjectArrays.<Object>arraysCopyOf(elements, uniques) : elements;
    return new RegularImmutableSet<E>(uniqueElements, hashCode, table, mask);
  }
  
  @VisibleForTesting
  static int chooseTableSize(int setSize) {
    if (setSize < 751619276) {
      int tableSize = Integer.highestOneBit(setSize - 1) << 1;
      while (tableSize * 0.7D < setSize)
        tableSize <<= 1; 
      return tableSize;
    } 
    Preconditions.checkArgument((setSize < 1073741824), "collection too large");
    return 1073741824;
  }
  
  public static <E> ImmutableSet<E> copyOf(E[] elements) {
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        return of(elements[0]);
    } 
    return construct(elements.length, (Object[])elements.clone());
  }
  
  public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
    return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
  }
  
  public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
    if (!elements.hasNext())
      return of(); 
    E first = elements.next();
    if (!elements.hasNext())
      return of(first); 
    return (new Builder<E>()).add(first).addAll(elements).build();
  }
  
  public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
    if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {
      ImmutableSet<E> set = (ImmutableSet)elements;
      if (!set.isPartialView())
        return set; 
    } else if (elements instanceof EnumSet) {
      return (ImmutableSet)copyOfEnumSet((EnumSet)elements);
    } 
    Object[] array = elements.toArray();
    return construct(array.length, array);
  }
  
  private static <E extends Enum<E>> ImmutableSet<E> copyOfEnumSet(EnumSet<E> enumSet) {
    return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
  }
  
  boolean isHashCodeFast() {
    return false;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this)
      return true; 
    if (object instanceof ImmutableSet && isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && hashCode() != object.hashCode())
      return false; 
    return Sets.equalsImpl(this, object);
  }
  
  public int hashCode() {
    return Sets.hashCodeImpl(this);
  }
  
  private static class SerializedForm implements Serializable {
    final Object[] elements;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(Object[] elements) {
      this.elements = elements;
    }
    
    Object readResolve() {
      return ImmutableSet.copyOf(this.elements);
    }
  }
  
  Object writeReplace() {
    return new SerializedForm(toArray());
  }
  
  public static <E> Builder<E> builder() {
    return new Builder<E>();
  }
  
  public abstract UnmodifiableIterator<E> iterator();
  
  public static class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
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
    
    public Builder<E> add(E... elements) {
      super.add(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterable<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterator<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public ImmutableSet<E> build() {
      ImmutableSet<E> result = ImmutableSet.construct(this.size, this.contents);
      this.size = result.size();
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */