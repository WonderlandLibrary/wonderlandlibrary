package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class HashBiMap extends AbstractMap implements BiMap, Serializable {
   private static final double LOAD_FACTOR = 1.0D;
   private transient HashBiMap.BiEntry[] hashTableKToV;
   private transient HashBiMap.BiEntry[] hashTableVToK;
   private transient int size;
   private transient int mask;
   private transient int modCount;
   private transient BiMap inverse;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static HashBiMap create() {
      return create(16);
   }

   public static HashBiMap create(int var0) {
      return new HashBiMap(var0);
   }

   public static HashBiMap create(Map var0) {
      HashBiMap var1 = create(var0.size());
      var1.putAll(var0);
      return var1;
   }

   private HashBiMap(int var1) {
      this.init(var1);
   }

   private void init(int var1) {
      CollectPreconditions.checkNonnegative(var1, "expectedSize");
      int var2 = Hashing.closedTableSize(var1, 1.0D);
      this.hashTableKToV = this.createTable(var2);
      this.hashTableVToK = this.createTable(var2);
      this.mask = var2 - 1;
      this.modCount = 0;
      this.size = 0;
   }

   private void delete(HashBiMap.BiEntry var1) {
      int var2 = var1.keyHash & this.mask;
      HashBiMap.BiEntry var3 = null;

      for(HashBiMap.BiEntry var4 = this.hashTableKToV[var2]; var4 != var1; var4 = var4.nextInKToVBucket) {
         var3 = var4;
      }

      if (var3 == null) {
         this.hashTableKToV[var2] = var1.nextInKToVBucket;
      } else {
         var3.nextInKToVBucket = var1.nextInKToVBucket;
      }

      int var6 = var1.valueHash & this.mask;
      var3 = null;

      for(HashBiMap.BiEntry var5 = this.hashTableVToK[var6]; var5 != var1; var5 = var5.nextInVToKBucket) {
         var3 = var5;
      }

      if (var3 == null) {
         this.hashTableVToK[var6] = var1.nextInVToKBucket;
      } else {
         var3.nextInVToKBucket = var1.nextInVToKBucket;
      }

      --this.size;
      ++this.modCount;
   }

   private void insert(HashBiMap.BiEntry var1) {
      int var2 = var1.keyHash & this.mask;
      var1.nextInKToVBucket = this.hashTableKToV[var2];
      this.hashTableKToV[var2] = var1;
      int var3 = var1.valueHash & this.mask;
      var1.nextInVToKBucket = this.hashTableVToK[var3];
      this.hashTableVToK[var3] = var1;
      ++this.size;
      ++this.modCount;
   }

   private static int hash(@Nullable Object var0) {
      return Hashing.smear(var0 == null ? 0 : var0.hashCode());
   }

   private HashBiMap.BiEntry seekByKey(@Nullable Object var1, int var2) {
      for(HashBiMap.BiEntry var3 = this.hashTableKToV[var2 & this.mask]; var3 != null; var3 = var3.nextInKToVBucket) {
         if (var2 == var3.keyHash && Objects.equal(var1, var3.key)) {
            return var3;
         }
      }

      return null;
   }

   private HashBiMap.BiEntry seekByValue(@Nullable Object var1, int var2) {
      for(HashBiMap.BiEntry var3 = this.hashTableVToK[var2 & this.mask]; var3 != null; var3 = var3.nextInVToKBucket) {
         if (var2 == var3.valueHash && Objects.equal(var1, var3.value)) {
            return var3;
         }
      }

      return null;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.seekByKey(var1, hash(var1)) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.seekByValue(var1, hash(var1)) != null;
   }

   @Nullable
   public Object get(@Nullable Object var1) {
      HashBiMap.BiEntry var2 = this.seekByKey(var1, hash(var1));
      return var2 == null ? null : var2.value;
   }

   public Object put(@Nullable Object var1, @Nullable Object var2) {
      return this.put(var1, var2, false);
   }

   public Object forcePut(@Nullable Object var1, @Nullable Object var2) {
      return this.put(var1, var2, true);
   }

   private Object put(@Nullable Object var1, @Nullable Object var2, boolean var3) {
      int var4 = hash(var1);
      int var5 = hash(var2);
      HashBiMap.BiEntry var6 = this.seekByKey(var1, var4);
      if (var6 != null && var5 == var6.valueHash && Objects.equal(var2, var6.value)) {
         return var2;
      } else {
         HashBiMap.BiEntry var7 = this.seekByValue(var2, var5);
         if (var7 != null) {
            if (!var3) {
               throw new IllegalArgumentException("value already present: " + var2);
            }

            this.delete(var7);
         }

         if (var6 != null) {
            this.delete(var6);
         }

         HashBiMap.BiEntry var8 = new HashBiMap.BiEntry(var1, var4, var2, var5);
         this.insert(var8);
         this.rehashIfNecessary();
         return var6 == null ? null : var6.value;
      }
   }

   @Nullable
   private Object putInverse(@Nullable Object var1, @Nullable Object var2, boolean var3) {
      int var4 = hash(var1);
      int var5 = hash(var2);
      HashBiMap.BiEntry var6 = this.seekByValue(var1, var4);
      if (var6 != null && var5 == var6.keyHash && Objects.equal(var2, var6.key)) {
         return var2;
      } else {
         HashBiMap.BiEntry var7 = this.seekByKey(var2, var5);
         if (var7 != null) {
            if (!var3) {
               throw new IllegalArgumentException("value already present: " + var2);
            }

            this.delete(var7);
         }

         if (var6 != null) {
            this.delete(var6);
         }

         HashBiMap.BiEntry var8 = new HashBiMap.BiEntry(var2, var5, var1, var4);
         this.insert(var8);
         this.rehashIfNecessary();
         return var6 == null ? null : var6.key;
      }
   }

   private void rehashIfNecessary() {
      HashBiMap.BiEntry[] var1 = this.hashTableKToV;
      if (Hashing.needsResizing(this.size, var1.length, 1.0D)) {
         int var2 = var1.length * 2;
         this.hashTableKToV = this.createTable(var2);
         this.hashTableVToK = this.createTable(var2);
         this.mask = var2 - 1;
         this.size = 0;

         HashBiMap.BiEntry var5;
         for(int var3 = 0; var3 < var1.length; ++var3) {
            for(HashBiMap.BiEntry var4 = var1[var3]; var4 != null; var4 = var5) {
               var5 = var4.nextInKToVBucket;
               this.insert(var4);
            }
         }

         ++this.modCount;
      }

   }

   private HashBiMap.BiEntry[] createTable(int var1) {
      return new HashBiMap.BiEntry[var1];
   }

   public Object remove(@Nullable Object var1) {
      HashBiMap.BiEntry var2 = this.seekByKey(var1, hash(var1));
      if (var2 == null) {
         return null;
      } else {
         this.delete(var2);
         return var2.value;
      }
   }

   public void clear() {
      this.size = 0;
      Arrays.fill(this.hashTableKToV, (Object)null);
      Arrays.fill(this.hashTableVToK, (Object)null);
      ++this.modCount;
   }

   public int size() {
      return this.size;
   }

   public Set keySet() {
      return new HashBiMap.KeySet(this);
   }

   public Set values() {
      return this.inverse().keySet();
   }

   public Set entrySet() {
      return new HashBiMap.EntrySet(this);
   }

   public BiMap inverse() {
      return this.inverse == null ? (this.inverse = new HashBiMap.Inverse(this)) : this.inverse;
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Serialization.writeMap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = Serialization.readCount(var1);
      this.init(var2);
      Serialization.populateMap(this, var1, var2);
   }

   public Collection values() {
      return this.values();
   }

   static int access$000(HashBiMap var0) {
      return var0.modCount;
   }

   static HashBiMap.BiEntry[] access$100(HashBiMap var0) {
      return var0.hashTableKToV;
   }

   static void access$200(HashBiMap var0, HashBiMap.BiEntry var1) {
      var0.delete(var1);
   }

   static int access$300(Object var0) {
      return hash(var0);
   }

   static HashBiMap.BiEntry access$400(HashBiMap var0, Object var1, int var2) {
      return var0.seekByKey(var1, var2);
   }

   static HashBiMap.BiEntry access$600(HashBiMap var0, Object var1, int var2) {
      return var0.seekByValue(var1, var2);
   }

   static void access$700(HashBiMap var0, HashBiMap.BiEntry var1) {
      var0.insert(var1);
   }

   static int access$900(HashBiMap var0) {
      return var0.size;
   }

   static Object access$1000(HashBiMap var0, Object var1, Object var2, boolean var3) {
      return var0.putInverse(var1, var2, var3);
   }

   private static final class InverseSerializedForm implements Serializable {
      private final HashBiMap bimap;

      InverseSerializedForm(HashBiMap var1) {
         this.bimap = var1;
      }

      Object readResolve() {
         return this.bimap.inverse();
      }
   }

   private final class Inverse extends AbstractMap implements BiMap, Serializable {
      final HashBiMap this$0;

      private Inverse(HashBiMap var1) {
         this.this$0 = var1;
      }

      BiMap forward() {
         return this.this$0;
      }

      public int size() {
         return HashBiMap.access$900(this.this$0);
      }

      public void clear() {
         this.forward().clear();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.forward().containsValue(var1);
      }

      public Object get(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.access$600(this.this$0, var1, HashBiMap.access$300(var1));
         return var2 == null ? null : var2.key;
      }

      public Object put(@Nullable Object var1, @Nullable Object var2) {
         return HashBiMap.access$1000(this.this$0, var1, var2, false);
      }

      public Object forcePut(@Nullable Object var1, @Nullable Object var2) {
         return HashBiMap.access$1000(this.this$0, var1, var2, true);
      }

      public Object remove(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.access$600(this.this$0, var1, HashBiMap.access$300(var1));
         if (var2 == null) {
            return null;
         } else {
            HashBiMap.access$200(this.this$0, var2);
            return var2.key;
         }
      }

      public BiMap inverse() {
         return this.forward();
      }

      public Set keySet() {
         return new HashBiMap.Inverse.InverseKeySet(this);
      }

      public Set values() {
         return this.forward().keySet();
      }

      public Set entrySet() {
         return new Maps.EntrySet(this) {
            final HashBiMap.Inverse this$1;

            {
               this.this$1 = var1;
            }

            Map map() {
               return this.this$1;
            }

            public Iterator iterator() {
               return new HashBiMap.Itr(this) {
                  final <undefinedtype> this$2;

                  {
                     this.this$2 = var1;
                  }

                  Entry output(HashBiMap.BiEntry var1) {
                     return new null.InverseEntry(this, var1);
                  }

                  Object output(HashBiMap.BiEntry var1) {
                     return this.output(var1);
                  }

                  class InverseEntry extends AbstractMapEntry {
                     HashBiMap.BiEntry delegate;
                     final <undefinedtype> this$3;

                     InverseEntry(Object var1, HashBiMap.BiEntry var2) {
                        this.this$3 = var1;
                        this.delegate = var2;
                     }

                     public Object getKey() {
                        return this.delegate.value;
                     }

                     public Object getValue() {
                        return this.delegate.key;
                     }

                     public Object setValue(Object var1) {
                        Object var2 = this.delegate.key;
                        int var3 = HashBiMap.access$300(var1);
                        if (var3 == this.delegate.keyHash && Objects.equal(var1, var2)) {
                           return var1;
                        } else {
                           Preconditions.checkArgument(HashBiMap.access$400(this.this$3.this$2.this$1.this$0, var1, var3) == null, "value already present: %s", var1);
                           HashBiMap.access$200(this.this$3.this$2.this$1.this$0, this.delegate);
                           HashBiMap.BiEntry var4 = new HashBiMap.BiEntry(var1, var3, this.delegate.value, this.delegate.valueHash);
                           HashBiMap.access$700(this.this$3.this$2.this$1.this$0, var4);
                           this.this$3.expectedModCount = HashBiMap.access$000(this.this$3.this$2.this$1.this$0);
                           return var2;
                        }
                     }
                  }
               };
            }
         };
      }

      Object writeReplace() {
         return new HashBiMap.InverseSerializedForm(this.this$0);
      }

      public Collection values() {
         return this.values();
      }

      Inverse(HashBiMap var1, Object var2) {
         this(var1);
      }

      private final class InverseKeySet extends Maps.KeySet {
         final HashBiMap.Inverse this$1;

         InverseKeySet(HashBiMap.Inverse var1) {
            super(var1);
            this.this$1 = var1;
         }

         public boolean remove(@Nullable Object var1) {
            HashBiMap.BiEntry var2 = HashBiMap.access$600(this.this$1.this$0, var1, HashBiMap.access$300(var1));
            if (var2 == null) {
               return false;
            } else {
               HashBiMap.access$200(this.this$1.this$0, var2);
               return true;
            }
         }

         public Iterator iterator() {
            return new HashBiMap.Itr(this) {
               final HashBiMap.Inverse.InverseKeySet this$2;

               {
                  this.this$2 = var1;
               }

               Object output(HashBiMap.BiEntry var1) {
                  return var1.value;
               }
            };
         }
      }
   }

   private final class EntrySet extends Maps.EntrySet {
      final HashBiMap this$0;

      private EntrySet(HashBiMap var1) {
         this.this$0 = var1;
      }

      Map map() {
         return this.this$0;
      }

      public Iterator iterator() {
         return new HashBiMap.Itr(this) {
            final HashBiMap.EntrySet this$1;

            {
               this.this$1 = var1;
            }

            Entry output(HashBiMap.BiEntry var1) {
               return new null.MapEntry(this, var1);
            }

            Object output(HashBiMap.BiEntry var1) {
               return this.output(var1);
            }

            class MapEntry extends AbstractMapEntry {
               HashBiMap.BiEntry delegate;
               final <undefinedtype> this$2;

               MapEntry(Object var1, HashBiMap.BiEntry var2) {
                  this.this$2 = var1;
                  this.delegate = var2;
               }

               public Object getKey() {
                  return this.delegate.key;
               }

               public Object getValue() {
                  return this.delegate.value;
               }

               public Object setValue(Object var1) {
                  Object var2 = this.delegate.value;
                  int var3 = HashBiMap.access$300(var1);
                  if (var3 == this.delegate.valueHash && Objects.equal(var1, var2)) {
                     return var1;
                  } else {
                     Preconditions.checkArgument(HashBiMap.access$600(this.this$2.this$1.this$0, var1, var3) == null, "value already present: %s", var1);
                     HashBiMap.access$200(this.this$2.this$1.this$0, this.delegate);
                     HashBiMap.BiEntry var4 = new HashBiMap.BiEntry(this.delegate.key, this.delegate.keyHash, var1, var3);
                     HashBiMap.access$700(this.this$2.this$1.this$0, var4);
                     this.this$2.expectedModCount = HashBiMap.access$000(this.this$2.this$1.this$0);
                     if (this.this$2.toRemove == this.delegate) {
                        this.this$2.toRemove = var4;
                     }

                     this.delegate = var4;
                     return var2;
                  }
               }
            }
         };
      }

      EntrySet(HashBiMap var1, Object var2) {
         this(var1);
      }
   }

   private final class KeySet extends Maps.KeySet {
      final HashBiMap this$0;

      KeySet(HashBiMap var1) {
         super(var1);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new HashBiMap.Itr(this) {
            final HashBiMap.KeySet this$1;

            {
               this.this$1 = var1;
            }

            Object output(HashBiMap.BiEntry var1) {
               return var1.key;
            }
         };
      }

      public boolean remove(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.access$400(this.this$0, var1, HashBiMap.access$300(var1));
         if (var2 == null) {
            return false;
         } else {
            HashBiMap.access$200(this.this$0, var2);
            return true;
         }
      }
   }

   abstract class Itr implements Iterator {
      int nextBucket;
      HashBiMap.BiEntry next;
      HashBiMap.BiEntry toRemove;
      int expectedModCount;
      final HashBiMap this$0;

      Itr(HashBiMap var1) {
         this.this$0 = var1;
         this.nextBucket = 0;
         this.next = null;
         this.toRemove = null;
         this.expectedModCount = HashBiMap.access$000(this.this$0);
      }

      private void checkForConcurrentModification() {
         if (HashBiMap.access$000(this.this$0) != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      public Object next() {
         this.checkForConcurrentModification();
         if (this != null) {
            throw new NoSuchElementException();
         } else {
            HashBiMap.BiEntry var1 = this.next;
            this.next = var1.nextInKToVBucket;
            this.toRemove = var1;
            return this.output(var1);
         }
      }

      public void remove() {
         this.checkForConcurrentModification();
         CollectPreconditions.checkRemove(this.toRemove != null);
         HashBiMap.access$200(this.this$0, this.toRemove);
         this.expectedModCount = HashBiMap.access$000(this.this$0);
         this.toRemove = null;
      }

      abstract Object output(HashBiMap.BiEntry var1);
   }

   private static final class BiEntry extends ImmutableEntry {
      final int keyHash;
      final int valueHash;
      @Nullable
      HashBiMap.BiEntry nextInKToVBucket;
      @Nullable
      HashBiMap.BiEntry nextInVToKBucket;

      BiEntry(Object var1, int var2, Object var3, int var4) {
         super(var1, var3);
         this.keyHash = var2;
         this.valueHash = var4;
      }
   }
}
