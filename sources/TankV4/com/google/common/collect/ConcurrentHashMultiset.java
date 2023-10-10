package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset extends AbstractMultiset implements Serializable {
   private final transient ConcurrentMap countMap;
   private transient ConcurrentHashMultiset.EntrySet entrySet;
   private static final long serialVersionUID = 1L;

   public static ConcurrentHashMultiset create() {
      return new ConcurrentHashMultiset(new ConcurrentHashMap());
   }

   public static ConcurrentHashMultiset create(Iterable var0) {
      ConcurrentHashMultiset var1 = create();
      Iterables.addAll(var1, var0);
      return var1;
   }

   @Beta
   public static ConcurrentHashMultiset create(MapMaker var0) {
      return new ConcurrentHashMultiset(var0.makeMap());
   }

   @VisibleForTesting
   ConcurrentHashMultiset(ConcurrentMap var1) {
      Preconditions.checkArgument(var1.isEmpty());
      this.countMap = var1;
   }

   public int count(@Nullable Object var1) {
      AtomicInteger var2 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
      return var2 == null ? 0 : var2.get();
   }

   public int size() {
      long var1 = 0L;

      AtomicInteger var4;
      for(Iterator var3 = this.countMap.values().iterator(); var3.hasNext(); var1 += (long)var4.get()) {
         var4 = (AtomicInteger)var3.next();
      }

      return Ints.saturatedCast(var1);
   }

   public Object[] toArray() {
      return this.snapshot().toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.snapshot().toArray(var1);
   }

   private List snapshot() {
      ArrayList var1 = Lists.newArrayListWithExpectedSize(this.size());
      Iterator var2 = this.entrySet().iterator();

      while(var2.hasNext()) {
         Multiset.Entry var3 = (Multiset.Entry)var2.next();
         Object var4 = var3.getElement();

         for(int var5 = var3.getCount(); var5 > 0; --var5) {
            var1.add(var4);
         }
      }

      return var1;
   }

   public int add(Object var1, int var2) {
      Preconditions.checkNotNull(var1);
      if (var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", var2);

         AtomicInteger var3;
         AtomicInteger var7;
         do {
            var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
            if (var3 == null) {
               var3 = (AtomicInteger)this.countMap.putIfAbsent(var1, new AtomicInteger(var2));
               if (var3 == null) {
                  return 0;
               }
            }

            while(true) {
               int var4 = var3.get();
               if (var4 == 0) {
                  var7 = new AtomicInteger(var2);
                  break;
               }

               try {
                  int var5 = IntMath.checkedAdd(var4, var2);
                  if (var3.compareAndSet(var4, var5)) {
                     return var4;
                  }
               } catch (ArithmeticException var6) {
                  throw new IllegalArgumentException("Overflow adding " + var2 + " occurrences to a count of " + var4);
               }
            }
         } while(this.countMap.putIfAbsent(var1, var7) != null && !this.countMap.replace(var1, var3, var7));

         return 0;
      }
   }

   public int remove(@Nullable Object var1, int var2) {
      if (var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", var2);
         AtomicInteger var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if (var3 == null) {
            return 0;
         } else {
            int var4;
            int var5;
            do {
               var4 = var3.get();
               if (var4 == 0) {
                  return 0;
               }

               var5 = Math.max(0, var4 - var2);
            } while(!var3.compareAndSet(var4, var5));

            if (var5 == 0) {
               this.countMap.remove(var1, var3);
            }

            return var4;
         }
      }
   }

   public boolean removeExactly(@Nullable Object var1, int var2) {
      if (var2 == 0) {
         return true;
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", var2);
         AtomicInteger var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if (var3 == null) {
            return false;
         } else {
            int var4;
            int var5;
            do {
               var4 = var3.get();
               if (var4 < var2) {
                  return false;
               }

               var5 = var4 - var2;
            } while(!var3.compareAndSet(var4, var5));

            if (var5 == 0) {
               this.countMap.remove(var1, var3);
            }

            return true;
         }
      }
   }

   public int setCount(Object var1, int var2) {
      Preconditions.checkNotNull(var1);
      CollectPreconditions.checkNonnegative(var2, "count");

      AtomicInteger var3;
      AtomicInteger var5;
      label40:
      do {
         var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if (var3 == null) {
            if (var2 == 0) {
               return 0;
            }

            var3 = (AtomicInteger)this.countMap.putIfAbsent(var1, new AtomicInteger(var2));
            if (var3 == null) {
               return 0;
            }
         }

         int var4;
         do {
            var4 = var3.get();
            if (var4 == 0) {
               if (var2 == 0) {
                  return 0;
               }

               var5 = new AtomicInteger(var2);
               continue label40;
            }
         } while(!var3.compareAndSet(var4, var2));

         if (var2 == 0) {
            this.countMap.remove(var1, var3);
         }

         return var4;
      } while(this.countMap.putIfAbsent(var1, var5) != null && !this.countMap.replace(var1, var3, var5));

      return 0;
   }

   public boolean setCount(Object var1, int var2, int var3) {
      Preconditions.checkNotNull(var1);
      CollectPreconditions.checkNonnegative(var2, "oldCount");
      CollectPreconditions.checkNonnegative(var3, "newCount");
      AtomicInteger var4 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
      if (var4 == null) {
         if (var2 != 0) {
            return false;
         } else if (var3 == 0) {
            return true;
         } else {
            return this.countMap.putIfAbsent(var1, new AtomicInteger(var3)) == null;
         }
      } else {
         int var5 = var4.get();
         if (var5 == var2) {
            if (var5 == 0) {
               if (var3 == 0) {
                  this.countMap.remove(var1, var4);
                  return true;
               }

               AtomicInteger var6 = new AtomicInteger(var3);
               return this.countMap.putIfAbsent(var1, var6) == null || this.countMap.replace(var1, var4, var6);
            }

            if (var4.compareAndSet(var5, var3)) {
               if (var3 == 0) {
                  this.countMap.remove(var1, var4);
               }

               return true;
            }
         }

         return false;
      }
   }

   Set createElementSet() {
      Set var1 = this.countMap.keySet();
      return new ForwardingSet(this, var1) {
         final Set val$delegate;
         final ConcurrentHashMultiset this$0;

         {
            this.this$0 = var1;
            this.val$delegate = var2;
         }

         protected Set delegate() {
            return this.val$delegate;
         }

         public boolean contains(@Nullable Object var1) {
            return var1 != null && Collections2.safeContains(this.val$delegate, var1);
         }

         public boolean containsAll(Collection var1) {
            return this.standardContainsAll(var1);
         }

         public boolean remove(Object var1) {
            return var1 != null && Collections2.safeRemove(this.val$delegate, var1);
         }

         public boolean removeAll(Collection var1) {
            return this.standardRemoveAll(var1);
         }

         protected Collection delegate() {
            return this.delegate();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   public Set entrySet() {
      ConcurrentHashMultiset.EntrySet var1 = this.entrySet;
      if (var1 == null) {
         this.entrySet = var1 = new ConcurrentHashMultiset.EntrySet(this);
      }

      return var1;
   }

   int distinctElements() {
      return this.countMap.size();
   }

   public boolean isEmpty() {
      return this.countMap.isEmpty();
   }

   Iterator entryIterator() {
      AbstractIterator var1 = new AbstractIterator(this) {
         private Iterator mapEntries;
         final ConcurrentHashMultiset this$0;

         {
            this.this$0 = var1;
            this.mapEntries = ConcurrentHashMultiset.access$100(this.this$0).entrySet().iterator();
         }

         protected Multiset.Entry computeNext() {
            java.util.Map.Entry var1;
            int var2;
            do {
               if (!this.mapEntries.hasNext()) {
                  return (Multiset.Entry)this.endOfData();
               }

               var1 = (java.util.Map.Entry)this.mapEntries.next();
               var2 = ((AtomicInteger)var1.getValue()).get();
            } while(var2 == 0);

            return Multisets.immutableEntry(var1.getKey(), var2);
         }

         protected Object computeNext() {
            return this.computeNext();
         }
      };
      return new ForwardingIterator(this, var1) {
         private Multiset.Entry last;
         final Iterator val$readOnlyIterator;
         final ConcurrentHashMultiset this$0;

         {
            this.this$0 = var1;
            this.val$readOnlyIterator = var2;
         }

         protected Iterator delegate() {
            return this.val$readOnlyIterator;
         }

         public Multiset.Entry next() {
            this.last = (Multiset.Entry)super.next();
            return this.last;
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.last != null);
            this.this$0.setCount(this.last.getElement(), 0);
            this.last = null;
         }

         public Object next() {
            return this.next();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   public void clear() {
      this.countMap.clear();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.countMap);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      ConcurrentMap var2 = (ConcurrentMap)var1.readObject();
      ConcurrentHashMultiset.FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, var2);
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public Set elementSet() {
      return super.elementSet();
   }

   public boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return super.removeAll(var1);
   }

   public boolean addAll(Collection var1) {
      return super.addAll(var1);
   }

   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   public boolean add(Object var1) {
      return super.add(var1);
   }

   public Iterator iterator() {
      return super.iterator();
   }

   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   static ConcurrentMap access$100(ConcurrentHashMultiset var0) {
      return var0.countMap;
   }

   private class EntrySet extends AbstractMultiset.EntrySet {
      final ConcurrentHashMultiset this$0;

      private EntrySet(ConcurrentHashMultiset var1) {
         super();
         this.this$0 = var1;
      }

      ConcurrentHashMultiset multiset() {
         return this.this$0;
      }

      public Object[] toArray() {
         return this.snapshot().toArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.snapshot().toArray(var1);
      }

      private List snapshot() {
         ArrayList var1 = Lists.newArrayListWithExpectedSize(this.size());
         Iterators.addAll(var1, this.iterator());
         return var1;
      }

      Multiset multiset() {
         return this.multiset();
      }

      EntrySet(ConcurrentHashMultiset var1, Object var2) {
         this(var1);
      }
   }

   private static class FieldSettersHolder {
      static final Serialization.FieldSetter COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
   }
}
