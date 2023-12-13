package net.minecraft.util;

public class LongHashMap {
   private static final String __OBFID = "CL_00001492";
   private final float percentUseable = 0.75F;
   private int mask;
   private transient volatile int modCount;
   private transient LongHashMap.Entry[] hashArray = new LongHashMap.Entry[4096];
   private transient int numHashElements;
   private int capacity = 3072;

   public Object getValueByKey(long var1) {
      int var3 = getHashedKey(var1);

      for(LongHashMap.Entry var4 = this.hashArray[getHashIndex(var3, this.mask)]; var4 != null; var4 = var4.nextEntry) {
         if (var4.key == var1) {
            return var4.value;
         }
      }

      return null;
   }

   private static int getHashIndex(int var0, int var1) {
      return var0 & var1;
   }

   private static int hash(int var0) {
      var0 = var0 ^ var0 >>> 20 ^ var0 >>> 12;
      return var0 ^ var0 >>> 7 ^ var0 >>> 4;
   }

   private static int getHashedKey(long var0) {
      return (int)(var0 ^ var0 >>> 27);
   }

   public int getNumHashElements() {
      return this.numHashElements;
   }

   private void copyHashTableTo(LongHashMap.Entry[] var1) {
      LongHashMap.Entry[] var2 = this.hashArray;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         LongHashMap.Entry var5 = var2[var4];
         if (var5 != null) {
            var2[var4] = null;

            LongHashMap.Entry var6;
            do {
               var6 = var5.nextEntry;
               int var7 = getHashIndex(var5.hash, var3 - 1);
               var5.nextEntry = var1[var7];
               var1[var7] = var5;
               var5 = var6;
            } while(var6 != null);
         }
      }

   }

   public double getKeyDistribution() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.hashArray.length; ++var2) {
         if (this.hashArray[var2] != null) {
            ++var1;
         }
      }

      return 1.0D * (double)var1 / (double)this.numHashElements;
   }

   private void resizeTable(int var1) {
      LongHashMap.Entry[] var2 = this.hashArray;
      int var3 = var2.length;
      if (var3 == 1073741824) {
         this.capacity = Integer.MAX_VALUE;
      } else {
         LongHashMap.Entry[] var4 = new LongHashMap.Entry[var1];
         this.copyHashTableTo(var4);
         this.hashArray = var4;
         this.mask = this.hashArray.length - 1;
         float var5 = (float)var1;
         this.getClass();
         this.capacity = (int)(var5 * 0.75F);
      }

   }

   final LongHashMap.Entry getEntry(long var1) {
      int var3 = getHashedKey(var1);

      for(LongHashMap.Entry var4 = this.hashArray[getHashIndex(var3, this.mask)]; var4 != null; var4 = var4.nextEntry) {
         if (var4.key == var1) {
            return var4;
         }
      }

      return null;
   }

   public LongHashMap() {
      this.mask = this.hashArray.length - 1;
   }

   public void add(long var1, Object var3) {
      int var4 = getHashedKey(var1);
      int var5 = getHashIndex(var4, this.mask);

      for(LongHashMap.Entry var6 = this.hashArray[var5]; var6 != null; var6 = var6.nextEntry) {
         if (var6.key == var1) {
            var6.value = var3;
            return;
         }
      }

      ++this.modCount;
      this.createKey(var4, var1, var3, var5);
   }

   final LongHashMap.Entry removeKey(long var1) {
      int var3 = getHashedKey(var1);
      int var4 = getHashIndex(var3, this.mask);
      LongHashMap.Entry var5 = this.hashArray[var4];

      LongHashMap.Entry var6;
      LongHashMap.Entry var7;
      for(var6 = var5; var6 != null; var6 = var7) {
         var7 = var6.nextEntry;
         if (var6.key == var1) {
            ++this.modCount;
            --this.numHashElements;
            if (var5 == var6) {
               this.hashArray[var4] = var7;
            } else {
               var5.nextEntry = var7;
            }

            return var6;
         }

         var5 = var6;
      }

      return var6;
   }

   private void createKey(int var1, long var2, Object var4, int var5) {
      LongHashMap.Entry var6 = this.hashArray[var5];
      this.hashArray[var5] = new LongHashMap.Entry(var1, var2, var4, var6);
      if (this.numHashElements++ >= this.capacity) {
         this.resizeTable(2 * this.hashArray.length);
      }

   }

   static int access$0(long var0) {
      return getHashedKey(var0);
   }

   public Object remove(long var1) {
      LongHashMap.Entry var3 = this.removeKey(var1);
      return var3 == null ? null : var3.value;
   }

   public boolean containsItem(long var1) {
      return this.getEntry(var1) != null;
   }

   static class Entry {
      final long key;
      final int hash;
      Object value;
      private static final String __OBFID = "CL_00001493";
      LongHashMap.Entry nextEntry;

      Entry(int var1, long var2, Object var4, LongHashMap.Entry var5) {
         this.value = var4;
         this.nextEntry = var5;
         this.key = var2;
         this.hash = var1;
      }

      public final long getKey() {
         return this.key;
      }

      public final boolean equals(Object var1) {
         if (!(var1 instanceof LongHashMap.Entry)) {
            return false;
         } else {
            LongHashMap.Entry var2 = (LongHashMap.Entry)var1;
            Long var3 = this.getKey();
            Long var4 = var2.getKey();
            if (var3 == var4 || var3 != null && var3.equals(var4)) {
               Object var5 = this.getValue();
               Object var6 = var2.getValue();
               if (var5 == var6 || var5 != null && var5.equals(var6)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final Object getValue() {
         return this.value;
      }

      public final String toString() {
         return this.getKey() + "=" + this.getValue();
      }

      public final int hashCode() {
         return LongHashMap.access$0(this.key);
      }
   }
}
