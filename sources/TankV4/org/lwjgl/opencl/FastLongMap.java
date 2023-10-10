package org.lwjgl.opencl;

import java.util.Iterator;

final class FastLongMap implements Iterable {
   private FastLongMap.Entry[] table;
   private int size;
   private int mask;
   private int capacity;
   private int threshold;

   FastLongMap() {
      this(16, 0.75F);
   }

   FastLongMap(int var1) {
      this(var1, 0.75F);
   }

   FastLongMap(int var1, float var2) {
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
         this.table = new FastLongMap.Entry[this.capacity];
         this.mask = this.capacity - 1;
      }
   }

   private int index(long var1) {
      return index(var1, this.mask);
   }

   private static int index(long var0, int var2) {
      int var3 = (int)(var0 ^ var0 >>> 32);
      return var3 & var2;
   }

   public Object put(long var1, Object var3) {
      FastLongMap.Entry[] var4 = this.table;
      int var5 = this.index(var1);

      for(FastLongMap.Entry var6 = var4[var5]; var6 != null; var6 = var6.next) {
         if (var6.key == var1) {
            Object var7 = var6.value;
            var6.value = var3;
            return var7;
         }
      }

      var4[var5] = new FastLongMap.Entry(var1, var3, var4[var5]);
      if (this.size++ >= this.threshold) {
         this.rehash(var4);
      }

      return null;
   }

   private void rehash(FastLongMap.Entry[] var1) {
      int var2 = 2 * this.capacity;
      int var3 = var2 - 1;
      FastLongMap.Entry[] var4 = new FastLongMap.Entry[var2];

      for(int var5 = 0; var5 < var1.length; ++var5) {
         FastLongMap.Entry var7 = var1[var5];
         FastLongMap.Entry var8;
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

   public Object get(long var1) {
      int var3 = this.index(var1);

      for(FastLongMap.Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.key == var1) {
            return var4.value;
         }
      }

      return null;
   }

   public boolean containsValue(Object var1) {
      FastLongMap.Entry[] var2 = this.table;

      for(int var3 = var2.length - 1; var3 >= 0; --var3) {
         for(FastLongMap.Entry var4 = var2[var3]; var4 != null; var4 = var4.next) {
            if (var4.value.equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean containsKey(long var1) {
      int var3 = this.index(var1);

      for(FastLongMap.Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.key == var1) {
            return true;
         }
      }

      return false;
   }

   public Object remove(long var1) {
      int var3 = this.index(var1);
      FastLongMap.Entry var4 = this.table[var3];

      FastLongMap.Entry var6;
      for(FastLongMap.Entry var5 = var4; var5 != null; var5 = var6) {
         var6 = var5.next;
         if (var5.key == var1) {
            --this.size;
            if (var4 == var5) {
               this.table[var3] = var6;
            } else {
               var4.next = var6;
            }

            return var5.value;
         }

         var4 = var5;
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
      FastLongMap.Entry[] var1 = this.table;

      for(int var2 = var1.length - 1; var2 >= 0; --var2) {
         var1[var2] = null;
      }

      this.size = 0;
   }

   public FastLongMap.EntryIterator iterator() {
      return new FastLongMap.EntryIterator(this);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   static FastLongMap.Entry[] access$000(FastLongMap var0) {
      return var0.table;
   }

   static final class Entry {
      final long key;
      Object value;
      FastLongMap.Entry next;

      Entry(long var1, Object var3, FastLongMap.Entry var4) {
         this.key = var1;
         this.value = var3;
         this.next = var4;
      }

      public long getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }
   }

   public class EntryIterator implements Iterator {
      private int nextIndex;
      private FastLongMap.Entry current;
      final FastLongMap this$0;

      EntryIterator(FastLongMap var1) {
         this.this$0 = var1;
         this.reset();
      }

      public void reset() {
         this.current = null;
         FastLongMap.Entry[] var1 = FastLongMap.access$000(this.this$0);

         int var2;
         for(var2 = var1.length - 1; var2 >= 0 && var1[var2] == null; --var2) {
         }

         this.nextIndex = var2;
      }

      public boolean hasNext() {
         if (this.nextIndex >= 0) {
            return true;
         } else {
            FastLongMap.Entry var1 = this.current;
            return var1 != null && var1.next != null;
         }
      }

      public FastLongMap.Entry next() {
         FastLongMap.Entry var1 = this.current;
         if (var1 != null) {
            var1 = var1.next;
            if (var1 != null) {
               this.current = var1;
               return var1;
            }
         }

         FastLongMap.Entry[] var2 = FastLongMap.access$000(this.this$0);
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
