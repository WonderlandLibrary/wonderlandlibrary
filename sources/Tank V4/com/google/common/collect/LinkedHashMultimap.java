package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class LinkedHashMultimap extends AbstractSetMultimap {
   private static final int DEFAULT_KEY_CAPACITY = 16;
   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
   @VisibleForTesting
   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
   @VisibleForTesting
   transient int valueSetCapacity = 2;
   private transient LinkedHashMultimap.ValueEntry multimapHeaderEntry;
   @GwtIncompatible("java serialization not supported")
   private static final long serialVersionUID = 1L;

   public static LinkedHashMultimap create() {
      return new LinkedHashMultimap(16, 2);
   }

   public static LinkedHashMultimap create(int var0, int var1) {
      return new LinkedHashMultimap(Maps.capacity(var0), Maps.capacity(var1));
   }

   public static LinkedHashMultimap create(Multimap var0) {
      LinkedHashMultimap var1 = create(var0.keySet().size(), 2);
      var1.putAll(var0);
      return var1;
   }

   private static void succeedsInValueSet(LinkedHashMultimap.ValueSetLink var0, LinkedHashMultimap.ValueSetLink var1) {
      var0.setSuccessorInValueSet(var1);
      var1.setPredecessorInValueSet(var0);
   }

   private static void succeedsInMultimap(LinkedHashMultimap.ValueEntry var0, LinkedHashMultimap.ValueEntry var1) {
      var0.setSuccessorInMultimap(var1);
      var1.setPredecessorInMultimap(var0);
   }

   private static void deleteFromValueSet(LinkedHashMultimap.ValueSetLink var0) {
      succeedsInValueSet(var0.getPredecessorInValueSet(), var0.getSuccessorInValueSet());
   }

   private static void deleteFromMultimap(LinkedHashMultimap.ValueEntry var0) {
      succeedsInMultimap(var0.getPredecessorInMultimap(), var0.getSuccessorInMultimap());
   }

   private LinkedHashMultimap(int var1, int var2) {
      super(new LinkedHashMap(var1));
      CollectPreconditions.checkNonnegative(var2, "expectedValuesPerKey");
      this.valueSetCapacity = var2;
      this.multimapHeaderEntry = new LinkedHashMultimap.ValueEntry((Object)null, (Object)null, 0, (LinkedHashMultimap.ValueEntry)null);
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
   }

   Set createCollection() {
      return new LinkedHashSet(this.valueSetCapacity);
   }

   Collection createCollection(Object var1) {
      return new LinkedHashMultimap.ValueSet(this, var1, this.valueSetCapacity);
   }

   public Set replaceValues(@Nullable Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   public Set entries() {
      return super.entries();
   }

   public Collection values() {
      return super.values();
   }

   Iterator entryIterator() {
      return new Iterator(this) {
         LinkedHashMultimap.ValueEntry nextEntry;
         LinkedHashMultimap.ValueEntry toRemove;
         final LinkedHashMultimap this$0;

         {
            this.this$0 = var1;
            this.nextEntry = LinkedHashMultimap.access$300(this.this$0).successorInMultimap;
         }

         public Entry next() {
            // $FF: Couldn't be decompiled
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            this.this$0.remove(this.toRemove.getKey(), this.toRemove.getValue());
            this.toRemove = null;
         }

         public Object next() {
            return this.next();
         }
      };
   }

   Iterator valueIterator() {
      return Maps.valueIterator(this.entryIterator());
   }

   public void clear() {
      super.clear();
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.valueSetCapacity);
      var1.writeInt(this.keySet().size());
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.writeObject(var3);
      }

      var1.writeInt(this.size());
      var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var4 = (Entry)var2.next();
         var1.writeObject(var4.getKey());
         var1.writeObject(var4.getValue());
      }

   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.multimapHeaderEntry = new LinkedHashMultimap.ValueEntry((Object)null, (Object)null, 0, (LinkedHashMultimap.ValueEntry)null);
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
      this.valueSetCapacity = var1.readInt();
      int var2 = var1.readInt();
      LinkedHashMap var3 = new LinkedHashMap(Maps.capacity(var2));

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         Object var5 = var1.readObject();
         var3.put(var5, this.createCollection(var5));
      }

      var4 = var1.readInt();

      for(int var8 = 0; var8 < var4; ++var8) {
         Object var6 = var1.readObject();
         Object var7 = var1.readObject();
         ((Collection)var3.get(var6)).add(var7);
      }

      this.setMap(var3);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   public Map asMap() {
      return super.asMap();
   }

   public Set removeAll(Object var1) {
      return super.removeAll(var1);
   }

   public Set get(Object var1) {
      return super.get(var1);
   }

   public Collection entries() {
      return this.entries();
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   public int size() {
      return super.size();
   }

   Collection createCollection() {
      return this.createCollection();
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public Multiset keys() {
      return super.keys();
   }

   public Set keySet() {
      return super.keySet();
   }

   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   static void access$200(LinkedHashMultimap.ValueSetLink var0, LinkedHashMultimap.ValueSetLink var1) {
      succeedsInValueSet(var0, var1);
   }

   static LinkedHashMultimap.ValueEntry access$300(LinkedHashMultimap var0) {
      return var0.multimapHeaderEntry;
   }

   static void access$400(LinkedHashMultimap.ValueEntry var0, LinkedHashMultimap.ValueEntry var1) {
      succeedsInMultimap(var0, var1);
   }

   static void access$500(LinkedHashMultimap.ValueSetLink var0) {
      deleteFromValueSet(var0);
   }

   static void access$600(LinkedHashMultimap.ValueEntry var0) {
      deleteFromMultimap(var0);
   }

   @VisibleForTesting
   final class ValueSet extends Sets.ImprovedAbstractSet implements LinkedHashMultimap.ValueSetLink {
      private final Object key;
      @VisibleForTesting
      LinkedHashMultimap.ValueEntry[] hashTable;
      private int size;
      private int modCount;
      private LinkedHashMultimap.ValueSetLink firstEntry;
      private LinkedHashMultimap.ValueSetLink lastEntry;
      final LinkedHashMultimap this$0;

      ValueSet(LinkedHashMultimap var1, Object var2, int var3) {
         this.this$0 = var1;
         this.size = 0;
         this.modCount = 0;
         this.key = var2;
         this.firstEntry = this;
         this.lastEntry = this;
         int var4 = Hashing.closedTableSize(var3, 1.0D);
         LinkedHashMultimap.ValueEntry[] var5 = new LinkedHashMultimap.ValueEntry[var4];
         this.hashTable = var5;
      }

      private int mask() {
         return this.hashTable.length - 1;
      }

      public LinkedHashMultimap.ValueSetLink getPredecessorInValueSet() {
         return this.lastEntry;
      }

      public LinkedHashMultimap.ValueSetLink getSuccessorInValueSet() {
         return this.firstEntry;
      }

      public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink var1) {
         this.lastEntry = var1;
      }

      public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink var1) {
         this.firstEntry = var1;
      }

      public Iterator iterator() {
         return new Iterator(this) {
            LinkedHashMultimap.ValueSetLink nextEntry;
            LinkedHashMultimap.ValueEntry toRemove;
            int expectedModCount;
            final LinkedHashMultimap.ValueSet this$1;

            {
               this.this$1 = var1;
               this.nextEntry = LinkedHashMultimap.ValueSet.access$000(this.this$1);
               this.expectedModCount = LinkedHashMultimap.ValueSet.access$100(this.this$1);
            }

            private void checkForComodification() {
               if (LinkedHashMultimap.ValueSet.access$100(this.this$1) != this.expectedModCount) {
                  throw new ConcurrentModificationException();
               }
            }

            public Object next() {
               // $FF: Couldn't be decompiled
            }

            public void remove() {
               this.checkForComodification();
               CollectPreconditions.checkRemove(this.toRemove != null);
               this.this$1.remove(this.toRemove.getValue());
               this.expectedModCount = LinkedHashMultimap.ValueSet.access$100(this.this$1);
               this.toRemove = null;
            }
         };
      }

      public int size() {
         return this.size;
      }

      public boolean contains(@Nullable Object var1) {
         int var2 = Hashing.smearedHash(var1);

         for(LinkedHashMultimap.ValueEntry var3 = this.hashTable[var2 & this.mask()]; var3 != null; var3 = var3.nextInValueBucket) {
            if (var3.matchesValue(var1, var2)) {
               return true;
            }
         }

         return false;
      }

      public boolean add(@Nullable Object var1) {
         int var2 = Hashing.smearedHash(var1);
         int var3 = var2 & this.mask();
         LinkedHashMultimap.ValueEntry var4 = this.hashTable[var3];

         LinkedHashMultimap.ValueEntry var5;
         for(var5 = var4; var5 != null; var5 = var5.nextInValueBucket) {
            if (var5.matchesValue(var1, var2)) {
               return false;
            }
         }

         var5 = new LinkedHashMultimap.ValueEntry(this.key, var1, var2, var4);
         LinkedHashMultimap.access$200(this.lastEntry, var5);
         LinkedHashMultimap.access$200(var5, this);
         LinkedHashMultimap.access$400(LinkedHashMultimap.access$300(this.this$0).getPredecessorInMultimap(), var5);
         LinkedHashMultimap.access$400(var5, LinkedHashMultimap.access$300(this.this$0));
         this.hashTable[var3] = var5;
         ++this.size;
         ++this.modCount;
         this.rehashIfNecessary();
         return true;
      }

      private void rehashIfNecessary() {
         if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
            LinkedHashMultimap.ValueEntry[] var1 = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
            this.hashTable = var1;
            int var2 = var1.length - 1;

            for(LinkedHashMultimap.ValueSetLink var3 = this.firstEntry; var3 != this; var3 = var3.getSuccessorInValueSet()) {
               LinkedHashMultimap.ValueEntry var4 = (LinkedHashMultimap.ValueEntry)var3;
               int var5 = var4.smearedValueHash & var2;
               var4.nextInValueBucket = var1[var5];
               var1[var5] = var4;
            }
         }

      }

      public boolean remove(@Nullable Object var1) {
         int var2 = Hashing.smearedHash(var1);
         int var3 = var2 & this.mask();
         LinkedHashMultimap.ValueEntry var4 = null;

         for(LinkedHashMultimap.ValueEntry var5 = this.hashTable[var3]; var5 != null; var5 = var5.nextInValueBucket) {
            if (var5.matchesValue(var1, var2)) {
               if (var4 == null) {
                  this.hashTable[var3] = var5.nextInValueBucket;
               } else {
                  var4.nextInValueBucket = var5.nextInValueBucket;
               }

               LinkedHashMultimap.access$500(var5);
               LinkedHashMultimap.access$600(var5);
               --this.size;
               ++this.modCount;
               return true;
            }

            var4 = var5;
         }

         return false;
      }

      public void clear() {
         Arrays.fill(this.hashTable, (Object)null);
         this.size = 0;

         for(LinkedHashMultimap.ValueSetLink var1 = this.firstEntry; var1 != this; var1 = var1.getSuccessorInValueSet()) {
            LinkedHashMultimap.ValueEntry var2 = (LinkedHashMultimap.ValueEntry)var1;
            LinkedHashMultimap.access$600(var2);
         }

         LinkedHashMultimap.access$200(this, this);
         ++this.modCount;
      }

      static LinkedHashMultimap.ValueSetLink access$000(LinkedHashMultimap.ValueSet var0) {
         return var0.firstEntry;
      }

      static int access$100(LinkedHashMultimap.ValueSet var0) {
         return var0.modCount;
      }
   }

   @VisibleForTesting
   static final class ValueEntry extends ImmutableEntry implements LinkedHashMultimap.ValueSetLink {
      final int smearedValueHash;
      @Nullable
      LinkedHashMultimap.ValueEntry nextInValueBucket;
      LinkedHashMultimap.ValueSetLink predecessorInValueSet;
      LinkedHashMultimap.ValueSetLink successorInValueSet;
      LinkedHashMultimap.ValueEntry predecessorInMultimap;
      LinkedHashMultimap.ValueEntry successorInMultimap;

      ValueEntry(@Nullable Object var1, @Nullable Object var2, int var3, @Nullable LinkedHashMultimap.ValueEntry var4) {
         super(var1, var2);
         this.smearedValueHash = var3;
         this.nextInValueBucket = var4;
      }

      boolean matchesValue(@Nullable Object var1, int var2) {
         return this.smearedValueHash == var2 && Objects.equal(this.getValue(), var1);
      }

      public LinkedHashMultimap.ValueSetLink getPredecessorInValueSet() {
         return this.predecessorInValueSet;
      }

      public LinkedHashMultimap.ValueSetLink getSuccessorInValueSet() {
         return this.successorInValueSet;
      }

      public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink var1) {
         this.predecessorInValueSet = var1;
      }

      public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink var1) {
         this.successorInValueSet = var1;
      }

      public LinkedHashMultimap.ValueEntry getPredecessorInMultimap() {
         return this.predecessorInMultimap;
      }

      public LinkedHashMultimap.ValueEntry getSuccessorInMultimap() {
         return this.successorInMultimap;
      }

      public void setSuccessorInMultimap(LinkedHashMultimap.ValueEntry var1) {
         this.successorInMultimap = var1;
      }

      public void setPredecessorInMultimap(LinkedHashMultimap.ValueEntry var1) {
         this.predecessorInMultimap = var1;
      }
   }

   private interface ValueSetLink {
      LinkedHashMultimap.ValueSetLink getPredecessorInValueSet();

      LinkedHashMultimap.ValueSetLink getSuccessorInValueSet();

      void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink var1);

      void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink var1);
   }
}
