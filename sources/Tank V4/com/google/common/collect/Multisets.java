package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets {
   private static final Ordering DECREASING_COUNT_ORDERING = new Ordering() {
      public int compare(Multiset.Entry var1, Multiset.Entry var2) {
         return Ints.compare(var2.getCount(), var1.getCount());
      }

      public int compare(Object var1, Object var2) {
         return this.compare((Multiset.Entry)var1, (Multiset.Entry)var2);
      }
   };

   private Multisets() {
   }

   public static Multiset unmodifiableMultiset(Multiset var0) {
      return (Multiset)(!(var0 instanceof Multisets.UnmodifiableMultiset) && !(var0 instanceof ImmutableMultiset) ? new Multisets.UnmodifiableMultiset((Multiset)Preconditions.checkNotNull(var0)) : var0);
   }

   /** @deprecated */
   @Deprecated
   public static Multiset unmodifiableMultiset(ImmutableMultiset var0) {
      return (Multiset)Preconditions.checkNotNull(var0);
   }

   @Beta
   public static SortedMultiset unmodifiableSortedMultiset(SortedMultiset var0) {
      return new UnmodifiableSortedMultiset((SortedMultiset)Preconditions.checkNotNull(var0));
   }

   public static Multiset.Entry immutableEntry(@Nullable Object var0, int var1) {
      return new Multisets.ImmutableEntry(var0, var1);
   }

   @Beta
   public static Multiset filter(Multiset var0, Predicate var1) {
      if (var0 instanceof Multisets.FilteredMultiset) {
         Multisets.FilteredMultiset var2 = (Multisets.FilteredMultiset)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Multisets.FilteredMultiset(var2.unfiltered, var3);
      } else {
         return new Multisets.FilteredMultiset(var0, var1);
      }
   }

   static int inferDistinctElements(Iterable var0) {
      return var0 instanceof Multiset ? ((Multiset)var0).elementSet().size() : 11;
   }

   @Beta
   public static Multiset union(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMultiset(var0, var1) {
         final Multiset val$multiset1;
         final Multiset val$multiset2;

         {
            this.val$multiset1 = var1;
            this.val$multiset2 = var2;
         }

         public boolean contains(@Nullable Object var1) {
            return this.val$multiset1.contains(var1) || this.val$multiset2.contains(var1);
         }

         public boolean isEmpty() {
            return this.val$multiset1.isEmpty() && this.val$multiset2.isEmpty();
         }

         public int count(Object var1) {
            return Math.max(this.val$multiset1.count(var1), this.val$multiset2.count(var1));
         }

         Set createElementSet() {
            return Sets.union(this.val$multiset1.elementSet(), this.val$multiset2.elementSet());
         }

         Iterator entryIterator() {
            Iterator var1 = this.val$multiset1.entrySet().iterator();
            Iterator var2 = this.val$multiset2.entrySet().iterator();
            return new AbstractIterator(this, var1, var2) {
               final Iterator val$iterator1;
               final Iterator val$iterator2;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$iterator1 = var2;
                  this.val$iterator2 = var3;
               }

               protected Multiset.Entry computeNext() {
                  Multiset.Entry var1;
                  Object var2;
                  if (this.val$iterator1.hasNext()) {
                     var1 = (Multiset.Entry)this.val$iterator1.next();
                     var2 = var1.getElement();
                     int var3 = Math.max(var1.getCount(), this.this$0.val$multiset2.count(var2));
                     return Multisets.immutableEntry(var2, var3);
                  } else {
                     do {
                        if (!this.val$iterator2.hasNext()) {
                           return (Multiset.Entry)this.endOfData();
                        }

                        var1 = (Multiset.Entry)this.val$iterator2.next();
                        var2 = var1.getElement();
                     } while(this.this$0.val$multiset1.contains(var2));

                     return Multisets.immutableEntry(var2, var1.getCount());
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }

         int distinctElements() {
            return this.elementSet().size();
         }
      };
   }

   public static Multiset intersection(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMultiset(var0, var1) {
         final Multiset val$multiset1;
         final Multiset val$multiset2;

         {
            this.val$multiset1 = var1;
            this.val$multiset2 = var2;
         }

         public int count(Object var1) {
            int var2 = this.val$multiset1.count(var1);
            return var2 == 0 ? 0 : Math.min(var2, this.val$multiset2.count(var1));
         }

         Set createElementSet() {
            return Sets.intersection(this.val$multiset1.elementSet(), this.val$multiset2.elementSet());
         }

         Iterator entryIterator() {
            Iterator var1 = this.val$multiset1.entrySet().iterator();
            return new AbstractIterator(this, var1) {
               final Iterator val$iterator1;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$iterator1 = var2;
               }

               protected Multiset.Entry computeNext() {
                  while(true) {
                     if (this.val$iterator1.hasNext()) {
                        Multiset.Entry var1 = (Multiset.Entry)this.val$iterator1.next();
                        Object var2 = var1.getElement();
                        int var3 = Math.min(var1.getCount(), this.this$0.val$multiset2.count(var2));
                        if (var3 <= 0) {
                           continue;
                        }

                        return Multisets.immutableEntry(var2, var3);
                     }

                     return (Multiset.Entry)this.endOfData();
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }

         int distinctElements() {
            return this.elementSet().size();
         }
      };
   }

   @Beta
   public static Multiset sum(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMultiset(var0, var1) {
         final Multiset val$multiset1;
         final Multiset val$multiset2;

         {
            this.val$multiset1 = var1;
            this.val$multiset2 = var2;
         }

         public boolean contains(@Nullable Object var1) {
            return this.val$multiset1.contains(var1) || this.val$multiset2.contains(var1);
         }

         public boolean isEmpty() {
            return this.val$multiset1.isEmpty() && this.val$multiset2.isEmpty();
         }

         public int size() {
            return this.val$multiset1.size() + this.val$multiset2.size();
         }

         public int count(Object var1) {
            return this.val$multiset1.count(var1) + this.val$multiset2.count(var1);
         }

         Set createElementSet() {
            return Sets.union(this.val$multiset1.elementSet(), this.val$multiset2.elementSet());
         }

         Iterator entryIterator() {
            Iterator var1 = this.val$multiset1.entrySet().iterator();
            Iterator var2 = this.val$multiset2.entrySet().iterator();
            return new AbstractIterator(this, var1, var2) {
               final Iterator val$iterator1;
               final Iterator val$iterator2;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$iterator1 = var2;
                  this.val$iterator2 = var3;
               }

               protected Multiset.Entry computeNext() {
                  Multiset.Entry var1;
                  Object var2;
                  if (this.val$iterator1.hasNext()) {
                     var1 = (Multiset.Entry)this.val$iterator1.next();
                     var2 = var1.getElement();
                     int var3 = var1.getCount() + this.this$0.val$multiset2.count(var2);
                     return Multisets.immutableEntry(var2, var3);
                  } else {
                     do {
                        if (!this.val$iterator2.hasNext()) {
                           return (Multiset.Entry)this.endOfData();
                        }

                        var1 = (Multiset.Entry)this.val$iterator2.next();
                        var2 = var1.getElement();
                     } while(this.this$0.val$multiset1.contains(var2));

                     return Multisets.immutableEntry(var2, var1.getCount());
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }

         int distinctElements() {
            return this.elementSet().size();
         }
      };
   }

   @Beta
   public static Multiset difference(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMultiset(var0, var1) {
         final Multiset val$multiset1;
         final Multiset val$multiset2;

         {
            this.val$multiset1 = var1;
            this.val$multiset2 = var2;
         }

         public int count(@Nullable Object var1) {
            int var2 = this.val$multiset1.count(var1);
            return var2 == 0 ? 0 : Math.max(0, var2 - this.val$multiset2.count(var1));
         }

         Iterator entryIterator() {
            Iterator var1 = this.val$multiset1.entrySet().iterator();
            return new AbstractIterator(this, var1) {
               final Iterator val$iterator1;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$iterator1 = var2;
               }

               protected Multiset.Entry computeNext() {
                  while(true) {
                     if (this.val$iterator1.hasNext()) {
                        Multiset.Entry var1 = (Multiset.Entry)this.val$iterator1.next();
                        Object var2 = var1.getElement();
                        int var3 = var1.getCount() - this.this$0.val$multiset2.count(var2);
                        if (var3 <= 0) {
                           continue;
                        }

                        return Multisets.immutableEntry(var2, var3);
                     }

                     return (Multiset.Entry)this.endOfData();
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }

         int distinctElements() {
            return Iterators.size(this.entryIterator());
         }
      };
   }

   public static boolean containsOccurrences(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Iterator var2 = var1.entrySet().iterator();

      Multiset.Entry var3;
      int var4;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = (Multiset.Entry)var2.next();
         var4 = var0.count(var3.getElement());
      } while(var4 >= var3.getCount());

      return false;
   }

   public static boolean retainOccurrences(Multiset var0, Multiset var1) {
      return retainOccurrencesImpl(var0, var1);
   }

   private static boolean retainOccurrencesImpl(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Iterator var2 = var0.entrySet().iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         Multiset.Entry var4 = (Multiset.Entry)var2.next();
         int var5 = var1.count(var4.getElement());
         if (var5 == 0) {
            var2.remove();
            var3 = true;
         } else if (var5 < var4.getCount()) {
            var0.setCount(var4.getElement(), var5);
            var3 = true;
         }
      }

      return var3;
   }

   public static boolean removeOccurrences(Multiset var0, Multiset var1) {
      return removeOccurrencesImpl(var0, var1);
   }

   private static boolean removeOccurrencesImpl(Multiset var0, Multiset var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      boolean var2 = false;
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Multiset.Entry var4 = (Multiset.Entry)var3.next();
         int var5 = var1.count(var4.getElement());
         if (var5 >= var4.getCount()) {
            var3.remove();
            var2 = true;
         } else if (var5 > 0) {
            var0.remove(var4.getElement(), var5);
            var2 = true;
         }
      }

      return var2;
   }

   static boolean equalsImpl(Multiset var0, @Nullable Object var1) {
      if (var1 == var0) {
         return true;
      } else if (var1 instanceof Multiset) {
         Multiset var2 = (Multiset)var1;
         if (var0.size() == var2.size() && var0.entrySet().size() == var2.entrySet().size()) {
            Iterator var3 = var2.entrySet().iterator();

            Multiset.Entry var4;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               var4 = (Multiset.Entry)var3.next();
            } while(var0.count(var4.getElement()) == var4.getCount());

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   static boolean addAllImpl(Multiset var0, Collection var1) {
      if (var1.isEmpty()) {
         return false;
      } else {
         if (var1 instanceof Multiset) {
            Multiset var2 = cast(var1);
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Multiset.Entry var4 = (Multiset.Entry)var3.next();
               var0.add(var4.getElement(), var4.getCount());
            }
         } else {
            Iterators.addAll(var0, var1.iterator());
         }

         return true;
      }
   }

   static boolean removeAllImpl(Multiset var0, Collection var1) {
      Object var2 = var1 instanceof Multiset ? ((Multiset)var1).elementSet() : var1;
      return var0.elementSet().removeAll((Collection)var2);
   }

   static boolean retainAllImpl(Multiset var0, Collection var1) {
      Preconditions.checkNotNull(var1);
      Object var2 = var1 instanceof Multiset ? ((Multiset)var1).elementSet() : var1;
      return var0.elementSet().retainAll((Collection)var2);
   }

   static int setCountImpl(Multiset var0, Object var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "count");
      int var3 = var0.count(var1);
      int var4 = var2 - var3;
      if (var4 > 0) {
         var0.add(var1, var4);
      } else if (var4 < 0) {
         var0.remove(var1, -var4);
      }

      return var3;
   }

   static boolean setCountImpl(Multiset var0, Object var1, int var2, int var3) {
      CollectPreconditions.checkNonnegative(var2, "oldCount");
      CollectPreconditions.checkNonnegative(var3, "newCount");
      if (var0.count(var1) == var2) {
         var0.setCount(var1, var3);
         return true;
      } else {
         return false;
      }
   }

   static Iterator iteratorImpl(Multiset var0) {
      return new Multisets.MultisetIteratorImpl(var0, var0.entrySet().iterator());
   }

   static int sizeImpl(Multiset var0) {
      long var1 = 0L;

      Multiset.Entry var4;
      for(Iterator var3 = var0.entrySet().iterator(); var3.hasNext(); var1 += (long)var4.getCount()) {
         var4 = (Multiset.Entry)var3.next();
      }

      return Ints.saturatedCast(var1);
   }

   static Multiset cast(Iterable var0) {
      return (Multiset)var0;
   }

   @Beta
   public static ImmutableMultiset copyHighestCountFirst(Multiset var0) {
      ImmutableList var1 = DECREASING_COUNT_ORDERING.immutableSortedCopy(var0.entrySet());
      return ImmutableMultiset.copyFromEntries(var1);
   }

   static final class MultisetIteratorImpl implements Iterator {
      private final Multiset multiset;
      private final Iterator entryIterator;
      private Multiset.Entry currentEntry;
      private int laterCount;
      private int totalCount;
      private boolean canRemove;

      MultisetIteratorImpl(Multiset var1, Iterator var2) {
         this.multiset = var1;
         this.entryIterator = var2;
      }

      public Object next() {
         if (this <= 0) {
            throw new NoSuchElementException();
         } else {
            if (this.laterCount == 0) {
               this.currentEntry = (Multiset.Entry)this.entryIterator.next();
               this.totalCount = this.laterCount = this.currentEntry.getCount();
            }

            --this.laterCount;
            this.canRemove = true;
            return this.currentEntry.getElement();
         }
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.canRemove);
         if (this.totalCount == 1) {
            this.entryIterator.remove();
         } else {
            this.multiset.remove(this.currentEntry.getElement());
         }

         --this.totalCount;
         this.canRemove = false;
      }
   }

   abstract static class EntrySet extends Sets.ImprovedAbstractSet {
      abstract Multiset multiset();

      public boolean contains(@Nullable Object var1) {
         if (var1 instanceof Multiset.Entry) {
            Multiset.Entry var2 = (Multiset.Entry)var1;
            if (var2.getCount() <= 0) {
               return false;
            } else {
               int var3 = this.multiset().count(var2.getElement());
               return var3 == var2.getCount();
            }
         } else {
            return false;
         }
      }

      public boolean remove(Object var1) {
         if (var1 instanceof Multiset.Entry) {
            Multiset.Entry var2 = (Multiset.Entry)var1;
            Object var3 = var2.getElement();
            int var4 = var2.getCount();
            if (var4 != 0) {
               Multiset var5 = this.multiset();
               return var5.setCount(var3, var4, 0);
            }
         }

         return false;
      }

      public void clear() {
         this.multiset().clear();
      }
   }

   abstract static class ElementSet extends Sets.ImprovedAbstractSet {
      abstract Multiset multiset();

      public void clear() {
         this.multiset().clear();
      }

      public boolean contains(Object var1) {
         return this.multiset().contains(var1);
      }

      public boolean containsAll(Collection var1) {
         return this.multiset().containsAll(var1);
      }

      public boolean isEmpty() {
         return this.multiset().isEmpty();
      }

      public Iterator iterator() {
         return new TransformedIterator(this, this.multiset().entrySet().iterator()) {
            final Multisets.ElementSet this$0;

            {
               this.this$0 = var1;
            }

            Object transform(Multiset.Entry var1) {
               return var1.getElement();
            }

            Object transform(Object var1) {
               return this.transform((Multiset.Entry)var1);
            }
         };
      }

      public boolean remove(Object var1) {
         int var2 = this.multiset().count(var1);
         if (var2 > 0) {
            this.multiset().remove(var1, var2);
            return true;
         } else {
            return false;
         }
      }

      public int size() {
         return this.multiset().entrySet().size();
      }
   }

   abstract static class AbstractEntry implements Multiset.Entry {
      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Multiset.Entry)) {
            return false;
         } else {
            Multiset.Entry var2 = (Multiset.Entry)var1;
            return this.getCount() == var2.getCount() && Objects.equal(this.getElement(), var2.getElement());
         }
      }

      public int hashCode() {
         Object var1 = this.getElement();
         return (var1 == null ? 0 : var1.hashCode()) ^ this.getCount();
      }

      public String toString() {
         String var1 = String.valueOf(this.getElement());
         int var2 = this.getCount();
         return var2 == 1 ? var1 : var1 + " x " + var2;
      }
   }

   private static final class FilteredMultiset extends AbstractMultiset {
      final Multiset unfiltered;
      final Predicate predicate;

      FilteredMultiset(Multiset var1, Predicate var2) {
         this.unfiltered = (Multiset)Preconditions.checkNotNull(var1);
         this.predicate = (Predicate)Preconditions.checkNotNull(var2);
      }

      public UnmodifiableIterator iterator() {
         return Iterators.filter(this.unfiltered.iterator(), this.predicate);
      }

      Set createElementSet() {
         return Sets.filter(this.unfiltered.elementSet(), this.predicate);
      }

      Set createEntrySet() {
         return Sets.filter(this.unfiltered.entrySet(), new Predicate(this) {
            final Multisets.FilteredMultiset this$0;

            {
               this.this$0 = var1;
            }

            public boolean apply(Multiset.Entry var1) {
               return this.this$0.predicate.apply(var1.getElement());
            }

            public boolean apply(Object var1) {
               return this.apply((Multiset.Entry)var1);
            }
         });
      }

      Iterator entryIterator() {
         throw new AssertionError("should never be called");
      }

      int distinctElements() {
         return this.elementSet().size();
      }

      public int count(@Nullable Object var1) {
         int var2 = this.unfiltered.count(var1);
         if (var2 > 0) {
            return this.predicate.apply(var1) ? var2 : 0;
         } else {
            return 0;
         }
      }

      public int add(@Nullable Object var1, int var2) {
         Preconditions.checkArgument(this.predicate.apply(var1), "Element %s does not match predicate %s", var1, this.predicate);
         return this.unfiltered.add(var1, var2);
      }

      public int remove(@Nullable Object var1, int var2) {
         CollectPreconditions.checkNonnegative(var2, "occurrences");
         if (var2 == 0) {
            return this.count(var1);
         } else {
            return this.contains(var1) ? this.unfiltered.remove(var1, var2) : 0;
         }
      }

      public void clear() {
         this.elementSet().clear();
      }

      public Iterator iterator() {
         return this.iterator();
      }
   }

   static final class ImmutableEntry extends Multisets.AbstractEntry implements Serializable {
      @Nullable
      final Object element;
      final int count;
      private static final long serialVersionUID = 0L;

      ImmutableEntry(@Nullable Object var1, int var2) {
         this.element = var1;
         this.count = var2;
         CollectPreconditions.checkNonnegative(var2, "count");
      }

      @Nullable
      public Object getElement() {
         return this.element;
      }

      public int getCount() {
         return this.count;
      }
   }

   static class UnmodifiableMultiset extends ForwardingMultiset implements Serializable {
      final Multiset delegate;
      transient Set elementSet;
      transient Set entrySet;
      private static final long serialVersionUID = 0L;

      UnmodifiableMultiset(Multiset var1) {
         this.delegate = var1;
      }

      protected Multiset delegate() {
         return this.delegate;
      }

      Set createElementSet() {
         return Collections.unmodifiableSet(this.delegate.elementSet());
      }

      public Set elementSet() {
         Set var1 = this.elementSet;
         return var1 == null ? (this.elementSet = this.createElementSet()) : var1;
      }

      public Set entrySet() {
         Set var1 = this.entrySet;
         return var1 == null ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : var1;
      }

      public Iterator iterator() {
         return Iterators.unmodifiableIterator(this.delegate.iterator());
      }

      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public int add(Object var1, int var2) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public int remove(Object var1, int var2) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public int setCount(Object var1, int var2) {
         throw new UnsupportedOperationException();
      }

      public boolean setCount(Object var1, int var2, int var3) {
         throw new UnsupportedOperationException();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
