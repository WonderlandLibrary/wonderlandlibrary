package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class RegularImmutableBiMap extends ImmutableBiMap {
   static final double MAX_LOAD_FACTOR = 1.2D;
   private final transient ImmutableMapEntry[] keyTable;
   private final transient ImmutableMapEntry[] valueTable;
   private final transient ImmutableMapEntry[] entries;
   private final transient int mask;
   private final transient int hashCode;
   private transient ImmutableBiMap inverse;

   RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry... var1) {
      this(var1.length, var1);
   }

   RegularImmutableBiMap(int var1, ImmutableMapEntry.TerminalEntry[] var2) {
      int var3 = Hashing.closedTableSize(var1, 1.2D);
      this.mask = var3 - 1;
      ImmutableMapEntry[] var4 = createEntryArray(var3);
      ImmutableMapEntry[] var5 = createEntryArray(var3);
      ImmutableMapEntry[] var6 = createEntryArray(var1);
      int var7 = 0;

      for(int var8 = 0; var8 < var1; ++var8) {
         ImmutableMapEntry.TerminalEntry var9 = var2[var8];
         Object var10 = var9.getKey();
         Object var11 = var9.getValue();
         int var12 = var10.hashCode();
         int var13 = var11.hashCode();
         int var14 = Hashing.smear(var12) & this.mask;
         int var15 = Hashing.smear(var13) & this.mask;
         ImmutableMapEntry var16 = var4[var14];

         ImmutableMapEntry var17;
         for(var17 = var16; var17 != null; var17 = var17.getNextInKeyBucket()) {
            checkNoConflict(!var10.equals(var17.getKey()), "key", var9, var17);
         }

         var17 = var5[var15];

         for(ImmutableMapEntry var18 = var17; var18 != null; var18 = var18.getNextInValueBucket()) {
            checkNoConflict(!var11.equals(var18.getValue()), "value", var9, var18);
         }

         Object var19 = var16 == null && var17 == null ? var9 : new RegularImmutableBiMap.NonTerminalBiMapEntry(var9, var16, var17);
         var4[var14] = (ImmutableMapEntry)var19;
         var5[var15] = (ImmutableMapEntry)var19;
         var6[var8] = (ImmutableMapEntry)var19;
         var7 += var12 ^ var13;
      }

      this.keyTable = var4;
      this.valueTable = var5;
      this.entries = var6;
      this.hashCode = var7;
   }

   RegularImmutableBiMap(Entry[] var1) {
      int var2 = var1.length;
      int var3 = Hashing.closedTableSize(var2, 1.2D);
      this.mask = var3 - 1;
      ImmutableMapEntry[] var4 = createEntryArray(var3);
      ImmutableMapEntry[] var5 = createEntryArray(var3);
      ImmutableMapEntry[] var6 = createEntryArray(var2);
      int var7 = 0;

      for(int var8 = 0; var8 < var2; ++var8) {
         Entry var9 = var1[var8];
         Object var10 = var9.getKey();
         Object var11 = var9.getValue();
         CollectPreconditions.checkEntryNotNull(var10, var11);
         int var12 = var10.hashCode();
         int var13 = var11.hashCode();
         int var14 = Hashing.smear(var12) & this.mask;
         int var15 = Hashing.smear(var13) & this.mask;
         ImmutableMapEntry var16 = var4[var14];

         ImmutableMapEntry var17;
         for(var17 = var16; var17 != null; var17 = var17.getNextInKeyBucket()) {
            checkNoConflict(!var10.equals(var17.getKey()), "key", var9, var17);
         }

         var17 = var5[var15];

         for(ImmutableMapEntry var18 = var17; var18 != null; var18 = var18.getNextInValueBucket()) {
            checkNoConflict(!var11.equals(var18.getValue()), "value", var9, var18);
         }

         Object var19 = var16 == null && var17 == null ? new ImmutableMapEntry.TerminalEntry(var10, var11) : new RegularImmutableBiMap.NonTerminalBiMapEntry(var10, var11, var16, var17);
         var4[var14] = (ImmutableMapEntry)var19;
         var5[var15] = (ImmutableMapEntry)var19;
         var6[var8] = (ImmutableMapEntry)var19;
         var7 += var12 ^ var13;
      }

      this.keyTable = var4;
      this.valueTable = var5;
      this.entries = var6;
      this.hashCode = var7;
   }

   private static ImmutableMapEntry[] createEntryArray(int var0) {
      return new ImmutableMapEntry[var0];
   }

   @Nullable
   public Object get(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = Hashing.smear(var1.hashCode()) & this.mask;

         for(ImmutableMapEntry var3 = this.keyTable[var2]; var3 != null; var3 = var3.getNextInKeyBucket()) {
            if (var1.equals(var3.getKey())) {
               return var3.getValue();
            }
         }

         return null;
      }
   }

   ImmutableSet createEntrySet() {
      return new ImmutableMapEntrySet(this) {
         final RegularImmutableBiMap this$0;

         {
            this.this$0 = var1;
         }

         ImmutableMap map() {
            return this.this$0;
         }

         public UnmodifiableIterator iterator() {
            return this.asList().iterator();
         }

         ImmutableList createAsList() {
            return new RegularImmutableAsList(this, RegularImmutableBiMap.access$000(this.this$0));
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.access$100(this.this$0);
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   boolean isPartialView() {
      return false;
   }

   public int size() {
      return this.entries.length;
   }

   public ImmutableBiMap inverse() {
      ImmutableBiMap var1 = this.inverse;
      return var1 == null ? (this.inverse = new RegularImmutableBiMap.Inverse(this)) : var1;
   }

   public BiMap inverse() {
      return this.inverse();
   }

   static ImmutableMapEntry[] access$000(RegularImmutableBiMap var0) {
      return var0.entries;
   }

   static int access$100(RegularImmutableBiMap var0) {
      return var0.hashCode;
   }

   static int access$300(RegularImmutableBiMap var0) {
      return var0.mask;
   }

   static ImmutableMapEntry[] access$400(RegularImmutableBiMap var0) {
      return var0.valueTable;
   }

   private static class InverseSerializedForm implements Serializable {
      private final ImmutableBiMap forward;
      private static final long serialVersionUID = 1L;

      InverseSerializedForm(ImmutableBiMap var1) {
         this.forward = var1;
      }

      Object readResolve() {
         return this.forward.inverse();
      }
   }

   private final class Inverse extends ImmutableBiMap {
      final RegularImmutableBiMap this$0;

      private Inverse(RegularImmutableBiMap var1) {
         this.this$0 = var1;
      }

      public int size() {
         return this.inverse().size();
      }

      public ImmutableBiMap inverse() {
         return this.this$0;
      }

      public Object get(@Nullable Object var1) {
         if (var1 == null) {
            return null;
         } else {
            int var2 = Hashing.smear(var1.hashCode()) & RegularImmutableBiMap.access$300(this.this$0);

            for(ImmutableMapEntry var3 = RegularImmutableBiMap.access$400(this.this$0)[var2]; var3 != null; var3 = var3.getNextInValueBucket()) {
               if (var1.equals(var3.getValue())) {
                  return var3.getKey();
               }
            }

            return null;
         }
      }

      ImmutableSet createEntrySet() {
         return new RegularImmutableBiMap.Inverse.InverseEntrySet(this);
      }

      boolean isPartialView() {
         return false;
      }

      Object writeReplace() {
         return new RegularImmutableBiMap.InverseSerializedForm(this.this$0);
      }

      public BiMap inverse() {
         return this.inverse();
      }

      Inverse(RegularImmutableBiMap var1, Object var2) {
         this(var1);
      }

      final class InverseEntrySet extends ImmutableMapEntrySet {
         final RegularImmutableBiMap.Inverse this$1;

         InverseEntrySet(RegularImmutableBiMap.Inverse var1) {
            this.this$1 = var1;
         }

         ImmutableMap map() {
            return this.this$1;
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.access$100(this.this$1.this$0);
         }

         public UnmodifiableIterator iterator() {
            return this.asList().iterator();
         }

         ImmutableList createAsList() {
            return new ImmutableAsList(this) {
               final RegularImmutableBiMap.Inverse.InverseEntrySet this$2;

               {
                  this.this$2 = var1;
               }

               public Entry get(int var1) {
                  ImmutableMapEntry var2 = RegularImmutableBiMap.access$000(this.this$2.this$1.this$0)[var1];
                  return Maps.immutableEntry(var2.getValue(), var2.getKey());
               }

               ImmutableCollection delegateCollection() {
                  return this.this$2;
               }

               public Object get(int var1) {
                  return this.get(var1);
               }
            };
         }

         public Iterator iterator() {
            return this.iterator();
         }
      }
   }

   private static final class NonTerminalBiMapEntry extends ImmutableMapEntry {
      @Nullable
      private final ImmutableMapEntry nextInKeyBucket;
      @Nullable
      private final ImmutableMapEntry nextInValueBucket;

      NonTerminalBiMapEntry(Object var1, Object var2, @Nullable ImmutableMapEntry var3, @Nullable ImmutableMapEntry var4) {
         super(var1, var2);
         this.nextInKeyBucket = var3;
         this.nextInValueBucket = var4;
      }

      NonTerminalBiMapEntry(ImmutableMapEntry var1, @Nullable ImmutableMapEntry var2, @Nullable ImmutableMapEntry var3) {
         super(var1);
         this.nextInKeyBucket = var2;
         this.nextInValueBucket = var3;
      }

      @Nullable
      ImmutableMapEntry getNextInKeyBucket() {
         return this.nextInKeyBucket;
      }

      @Nullable
      ImmutableMapEntry getNextInValueBucket() {
         return this.nextInValueBucket;
      }
   }
}
