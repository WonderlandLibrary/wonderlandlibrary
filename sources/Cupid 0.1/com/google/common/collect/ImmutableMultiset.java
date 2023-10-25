package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
  private static final ImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
  
  private transient ImmutableSet<Multiset.Entry<E>> entrySet;
  
  public static <E> ImmutableMultiset<E> of() {
    return (ImmutableMultiset)EMPTY;
  }
  
  public static <E> ImmutableMultiset<E> of(E element) {
    return copyOfInternal((E[])new Object[] { element });
  }
  
  public static <E> ImmutableMultiset<E> of(E e1, E e2) {
    return copyOfInternal((E[])new Object[] { e1, e2 });
  }
  
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
    return copyOfInternal((E[])new Object[] { e1, e2, e3 });
  }
  
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
    return copyOfInternal((E[])new Object[] { e1, e2, e3, e4 });
  }
  
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
    return copyOfInternal((E[])new Object[] { e1, e2, e3, e4, e5 });
  }
  
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
    return (new Builder<E>()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
  }
  
  public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
    return copyOf(Arrays.asList(elements));
  }
  
  public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
    if (elements instanceof ImmutableMultiset) {
      ImmutableMultiset<E> result = (ImmutableMultiset)elements;
      if (!result.isPartialView())
        return result; 
    } 
    Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);
    return copyOfInternal(multiset);
  }
  
  private static <E> ImmutableMultiset<E> copyOfInternal(E... elements) {
    return copyOf(Arrays.asList(elements));
  }
  
  private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
    return copyFromEntries(multiset.entrySet());
  }
  
  static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
    long size = 0L;
    ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
    for (Multiset.Entry<? extends E> entry : entries) {
      int count = entry.getCount();
      if (count > 0) {
        builder.put(entry.getElement(), Integer.valueOf(count));
        size += count;
      } 
    } 
    if (size == 0L)
      return of(); 
    return new RegularImmutableMultiset<E>(builder.build(), Ints.saturatedCast(size));
  }
  
  public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
    Multiset<E> multiset = LinkedHashMultiset.create();
    Iterators.addAll(multiset, elements);
    return copyOfInternal(multiset);
  }
  
  public UnmodifiableIterator<E> iterator() {
    final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
    return new UnmodifiableIterator<E>() {
        int remaining;
        
        E element;
        
        public boolean hasNext() {
          return (this.remaining > 0 || entryIterator.hasNext());
        }
        
        public E next() {
          if (this.remaining <= 0) {
            Multiset.Entry<E> entry = entryIterator.next();
            this.element = entry.getElement();
            this.remaining = entry.getCount();
          } 
          this.remaining--;
          return this.element;
        }
      };
  }
  
  public boolean contains(@Nullable Object object) {
    return (count(object) > 0);
  }
  
  public boolean containsAll(Collection<?> targets) {
    return elementSet().containsAll(targets);
  }
  
  @Deprecated
  public final int add(E element, int occurrences) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final int remove(Object element, int occurrences) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final int setCount(E element, int count) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final boolean setCount(E element, int oldCount, int newCount) {
    throw new UnsupportedOperationException();
  }
  
  @GwtIncompatible("not present in emulated superclass")
  int copyIntoArray(Object[] dst, int offset) {
    for (Multiset.Entry<E> entry : entrySet()) {
      Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
      offset += entry.getCount();
    } 
    return offset;
  }
  
  public boolean equals(@Nullable Object object) {
    return Multisets.equalsImpl(this, object);
  }
  
  public int hashCode() {
    return Sets.hashCodeImpl(entrySet());
  }
  
  public String toString() {
    return entrySet().toString();
  }
  
  public ImmutableSet<Multiset.Entry<E>> entrySet() {
    ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
    return (es == null) ? (this.entrySet = createEntrySet()) : es;
  }
  
  private final ImmutableSet<Multiset.Entry<E>> createEntrySet() {
    return isEmpty() ? ImmutableSet.<Multiset.Entry<E>>of() : new EntrySet();
  }
  
  private final class EntrySet extends ImmutableSet<Multiset.Entry<E>> {
    private static final long serialVersionUID = 0L;
    
    private EntrySet() {}
    
    boolean isPartialView() {
      return ImmutableMultiset.this.isPartialView();
    }
    
    public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
      return asList().iterator();
    }
    
    ImmutableList<Multiset.Entry<E>> createAsList() {
      return (ImmutableList)new ImmutableAsList<Multiset.Entry<Multiset.Entry<E>>>() {
          public Multiset.Entry<E> get(int index) {
            return ImmutableMultiset.this.getEntry(index);
          }
          
          ImmutableCollection<Multiset.Entry<E>> delegateCollection() {
            return ImmutableMultiset.EntrySet.this;
          }
        };
    }
    
    public int size() {
      return ImmutableMultiset.this.elementSet().size();
    }
    
    public boolean contains(Object o) {
      if (o instanceof Multiset.Entry) {
        Multiset.Entry<?> entry = (Multiset.Entry)o;
        if (entry.getCount() <= 0)
          return false; 
        int count = ImmutableMultiset.this.count(entry.getElement());
        return (count == entry.getCount());
      } 
      return false;
    }
    
    public int hashCode() {
      return ImmutableMultiset.this.hashCode();
    }
    
    Object writeReplace() {
      return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
    }
  }
  
  static class EntrySetSerializedForm<E> implements Serializable {
    final ImmutableMultiset<E> multiset;
    
    EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
      this.multiset = multiset;
    }
    
    Object readResolve() {
      return this.multiset.entrySet();
    }
  }
  
  private static class SerializedForm implements Serializable {
    final Object[] elements;
    
    final int[] counts;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(Multiset<?> multiset) {
      int distinct = multiset.entrySet().size();
      this.elements = new Object[distinct];
      this.counts = new int[distinct];
      int i = 0;
      for (Multiset.Entry<?> entry : multiset.entrySet()) {
        this.elements[i] = entry.getElement();
        this.counts[i] = entry.getCount();
        i++;
      } 
    }
    
    Object readResolve() {
      LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
      for (int i = 0; i < this.elements.length; i++)
        multiset.add(this.elements[i], this.counts[i]); 
      return ImmutableMultiset.copyOf(multiset);
    }
  }
  
  Object writeReplace() {
    return new SerializedForm(this);
  }
  
  public static <E> Builder<E> builder() {
    return new Builder<E>();
  }
  
  abstract Multiset.Entry<E> getEntry(int paramInt);
  
  public static class Builder<E> extends ImmutableCollection.Builder<E> {
    final Multiset<E> contents;
    
    public Builder() {
      this(LinkedHashMultiset.create());
    }
    
    Builder(Multiset<E> contents) {
      this.contents = contents;
    }
    
    public Builder<E> add(E element) {
      this.contents.add((E)Preconditions.checkNotNull(element));
      return this;
    }
    
    public Builder<E> addCopies(E element, int occurrences) {
      this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
      return this;
    }
    
    public Builder<E> setCount(E element, int count) {
      this.contents.setCount((E)Preconditions.checkNotNull(element), count);
      return this;
    }
    
    public Builder<E> add(E... elements) {
      super.add(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterable<? extends E> elements) {
      if (elements instanceof Multiset) {
        Multiset<? extends E> multiset = Multisets.cast(elements);
        for (Multiset.Entry<? extends E> entry : multiset.entrySet())
          addCopies(entry.getElement(), entry.getCount()); 
      } else {
        super.addAll(elements);
      } 
      return this;
    }
    
    public Builder<E> addAll(Iterator<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public ImmutableMultiset<E> build() {
      return ImmutableMultiset.copyOf(this.contents);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */