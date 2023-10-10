package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractMapBasedMultiset extends AbstractMultiset implements Serializable {
   private transient Map backingMap;
   private transient long size;
   @GwtIncompatible("not needed in emulated source.")
   private static final long serialVersionUID = -2250766705698539974L;

   protected AbstractMapBasedMultiset(Map var1) {
      this.backingMap = (Map)Preconditions.checkNotNull(var1);
      this.size = (long)super.size();
   }

   void setBackingMap(Map var1) {
      this.backingMap = var1;
   }

   public Set entrySet() {
      return super.entrySet();
   }

   Iterator entryIterator() {
      Iterator var1 = this.backingMap.entrySet().iterator();
      return new Iterator(this, var1) {
         java.util.Map.Entry toRemove;
         final Iterator val$backingEntries;
         final AbstractMapBasedMultiset this$0;

         {
            this.this$0 = var1;
            this.val$backingEntries = var2;
         }

         public boolean hasNext() {
            return this.val$backingEntries.hasNext();
         }

         public Multiset.Entry next() {
            java.util.Map.Entry var1 = (java.util.Map.Entry)this.val$backingEntries.next();
            this.toRemove = var1;
            return new Multisets.AbstractEntry(this, var1) {
               final java.util.Map.Entry val$mapEntry;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.val$mapEntry = var2;
               }

               public Object getElement() {
                  return this.val$mapEntry.getKey();
               }

               public int getCount() {
                  Count var1 = (Count)this.val$mapEntry.getValue();
                  if (var1 == null || var1.get() == 0) {
                     Count var2 = (Count)AbstractMapBasedMultiset.access$000(this.this$1.this$0).get(this.getElement());
                     if (var2 != null) {
                        return var2.get();
                     }
                  }

                  return var1 == null ? 0 : var1.get();
               }
            };
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            AbstractMapBasedMultiset.access$122(this.this$0, (long)((Count)this.toRemove.getValue()).getAndSet(0));
            this.val$backingEntries.remove();
            this.toRemove = null;
         }

         public Object next() {
            return this.next();
         }
      };
   }

   public void clear() {
      Iterator var1 = this.backingMap.values().iterator();

      while(var1.hasNext()) {
         Count var2 = (Count)var1.next();
         var2.set(0);
      }

      this.backingMap.clear();
      this.size = 0L;
   }

   int distinctElements() {
      return this.backingMap.size();
   }

   public int size() {
      return Ints.saturatedCast(this.size);
   }

   public Iterator iterator() {
      return new AbstractMapBasedMultiset.MapBasedMultisetIterator(this);
   }

   public int count(@Nullable Object var1) {
      Count var2 = (Count)Maps.safeGet(this.backingMap, var1);
      return var2 == null ? 0 : var2.get();
   }

   public int add(@Nullable Object var1, int var2) {
      if (var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "occurrences cannot be negative: %s", var2);
         Count var3 = (Count)this.backingMap.get(var1);
         int var4;
         if (var3 == null) {
            var4 = 0;
            this.backingMap.put(var1, new Count(var2));
         } else {
            var4 = var3.get();
            long var5 = (long)var4 + (long)var2;
            Preconditions.checkArgument(var5 <= 2147483647L, "too many occurrences: %s", var5);
            var3.getAndAdd(var2);
         }

         this.size += (long)var2;
         return var4;
      }
   }

   public int remove(@Nullable Object var1, int var2) {
      if (var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "occurrences cannot be negative: %s", var2);
         Count var3 = (Count)this.backingMap.get(var1);
         if (var3 == null) {
            return 0;
         } else {
            int var4 = var3.get();
            int var5;
            if (var4 > var2) {
               var5 = var2;
            } else {
               var5 = var4;
               this.backingMap.remove(var1);
            }

            var3.addAndGet(-var5);
            this.size -= (long)var5;
            return var4;
         }
      }
   }

   public int setCount(@Nullable Object var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "count");
      Count var3;
      int var4;
      if (var2 == 0) {
         var3 = (Count)this.backingMap.remove(var1);
         var4 = getAndSet(var3, var2);
      } else {
         var3 = (Count)this.backingMap.get(var1);
         var4 = getAndSet(var3, var2);
         if (var3 == null) {
            this.backingMap.put(var1, new Count(var2));
         }
      }

      this.size += (long)(var2 - var4);
      return var4;
   }

   private static int getAndSet(Count var0, int var1) {
      return var0 == null ? 0 : var0.getAndSet(var1);
   }

   @GwtIncompatible("java.io.ObjectStreamException")
   private void readObjectNoData() throws ObjectStreamException {
      throw new InvalidObjectException("Stream data required");
   }

   static Map access$000(AbstractMapBasedMultiset var0) {
      return var0.backingMap;
   }

   static long access$122(AbstractMapBasedMultiset var0, long var1) {
      return var0.size -= var1;
   }

   static long access$110(AbstractMapBasedMultiset var0) {
      return (long)(var0.size--);
   }

   private class MapBasedMultisetIterator implements Iterator {
      final Iterator entryIterator;
      java.util.Map.Entry currentEntry;
      int occurrencesLeft;
      boolean canRemove;
      final AbstractMapBasedMultiset this$0;

      MapBasedMultisetIterator(AbstractMapBasedMultiset var1) {
         this.this$0 = var1;
         this.entryIterator = AbstractMapBasedMultiset.access$000(var1).entrySet().iterator();
      }

      public boolean hasNext() {
         return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
      }

      public Object next() {
         if (this.occurrencesLeft == 0) {
            this.currentEntry = (java.util.Map.Entry)this.entryIterator.next();
            this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
         }

         --this.occurrencesLeft;
         this.canRemove = true;
         return this.currentEntry.getKey();
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.canRemove);
         int var1 = ((Count)this.currentEntry.getValue()).get();
         if (var1 <= 0) {
            throw new ConcurrentModificationException();
         } else {
            if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
               this.entryIterator.remove();
            }

            AbstractMapBasedMultiset.access$110(this.this$0);
            this.canRemove = false;
         }
      }
   }
}
