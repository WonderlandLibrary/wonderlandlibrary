package org.lwjgl.opengl;

import java.util.Iterator;

final class FastIntMap implements Iterable {
   private FastIntMap.Entry[] table;
   private int size;
   private int mask;
   private int capacity;
   private int threshold;

   FastIntMap() {
      this(16, 0.75F);
   }

   FastIntMap(int var1) {
      this(var1, 0.75F);
   }

   FastIntMap(int var1, float var2) {
      if (var1 > 1073741824) {
         throw new IllegalArgumentException("initialCapacity is too large.");
      } else if (var1 < 0) {
         throw new IllegalArgumentException("initialCapacity must be greater than zero.");
      } else if (var2 <= 0.0F) {
         throw new IllegalArgumentException("initialCapacity must be greater than zero.");
      } else {
         for(this.capacity = 1; this.capacity < var1; this.capacity <<= 1) {
         }

         this.threshold = (int)((float)this.capacity * var2);
         this.table = new FastIntMap.Entry[this.capacity];
         this.mask = this.capacity - 1;
      }
   }

   private int index(int var1) {
      return index(var1, this.mask);
   }

   private static int index(int var0, int var1) {
      return var0 & var1;
   }

   public Object put(int var1, Object var2) {
      FastIntMap.Entry[] var3 = this.table;
      int var4 = this.index(var1);

      for(FastIntMap.Entry var5 = var3[var4]; var5 != null; var5 = var5.next) {
         if (var5.key == var1) {
            Object var6 = var5.value;
            var5.value = var2;
            return var6;
         }
      }

      var3[var4] = new FastIntMap.Entry(var1, var2, var3[var4]);
      if (this.size++ >= this.threshold) {
         this.rehash(var3);
      }

      return null;
   }

   private void rehash(FastIntMap.Entry[] var1) {
      int var2 = 2 * this.capacity;
      int var3 = var2 - 1;
      FastIntMap.Entry[] var4 = new FastIntMap.Entry[var2];

      for(int var5 = 0; var5 < var1.length; ++var5) {
         FastIntMap.Entry var7 = var1[var5];
         FastIntMap.Entry var8;
         if (var7 != null) {
            do {
               var8 = var7.next;
               int var6 = index(var7.key, var3);
               var7.next = var4[var6];
               var4[var6] = var7;
               var7 = var8;
            } while(var8 != null);
         }
      }

      this.table = var4;
      this.capacity = var2;
      this.mask = var3;
      this.threshold *= 2;
   }

   public Object get(int var1) {
      int var2 = this.index(var1);

      for(FastIntMap.Entry var3 = this.table[var2]; var3 != null; var3 = var3.next) {
         if (var3.key == var1) {
            return var3.value;
         }
      }

      return null;
   }

   public boolean containsValue(Object var1) {
      FastIntMap.Entry[] var2 = this.table;

      for(int var3 = var2.length - 1; var3 >= 0; --var3) {
         for(FastIntMap.Entry var4 = var2[var3]; var4 != null; var4 = var4.next) {
            if (var4.value.equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean containsKey(int var1) {
      int var2 = this.index(var1);

      for(FastIntMap.Entry var3 = this.table[var2]; var3 != null; var3 = var3.next) {
         if (var3.key == var1) {
            return true;
         }
      }

      return false;
   }

   public Object remove(int var1) {
      int var2 = this.index(var1);
      FastIntMap.Entry var3 = this.table[var2];

      FastIntMap.Entry var5;
      for(FastIntMap.Entry var4 = var3; var4 != null; var4 = var5) {
         var5 = var4.next;
         if (var4.key == var1) {
            --this.size;
            if (var3 == var4) {
               this.table[var2] = var5;
            } else {
               var3.next = var5;
            }

            return var4.value;
         }

         var3 = var4;
      }

      return null;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      FastIntMap.Entry[] var1 = this.table;

      for(int var2 = var1.length - 1; var2 >= 0; --var2) {
         var1[var2] = null;
      }

      this.size = 0;
   }

   public FastIntMap.EntryIterator iterator() {
      return new FastIntMap.EntryIterator(this);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   static FastIntMap.Entry[] access$000(FastIntMap var0) {
      return var0.table;
   }

   static final class Entry {
      final int key;
      Object value;
      FastIntMap.Entry next;

      Entry(int var1, Object var2, FastIntMap.Entry var3) {
         this.key = var1;
         this.value = var2;
         this.next = var3;
      }

      public int getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }
   }

   public class EntryIterator implements Iterator {
      private int nextIndex;
      private FastIntMap.Entry current;
      final FastIntMap this$0;

      EntryIterator(FastIntMap var1) {
         this.this$0 = var1;
         this.reset();
      }

      public void reset() {
         this.current = null;
         FastIntMap.Entry[] var1 = FastIntMap.access$000(this.this$0);

         int var2;
         for(var2 = var1.length - 1; var2 >= 0 && var1[var2] == null; --var2) {
         }

         this.nextIndex = var2;
      }

      public boolean hasNext() {
         if (this.nextIndex >= 0) {
            return true;
         } else {
            FastIntMap.Entry var1 = this.current;
            return var1 != null && var1.next != null;
         }
      }

      public FastIntMap.Entry next() {
         FastIntMap.Entry var1 = this.current;
         if (var1 != null) {
            var1 = var1.next;
            if (var1 != null) {
               this.current = var1;
               return var1;
            }
         }

         FastIntMap.Entry[] var2 = FastIntMap.access$000(this.this$0);
         int var3 = this.nextIndex;
         var1 = this.current = var2[var3];

         do {
            --var3;
         } while(var3 >= 0 && var2[var3] == null);

         this.nextIndex = var3;
         return var1;
      }

      public void remove() {
         this.this$0.remove(this.current.key);
      }

      public Object next() {
         return this.next();
      }
   }
}
