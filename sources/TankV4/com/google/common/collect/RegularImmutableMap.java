package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class RegularImmutableMap extends ImmutableMap {
   private final transient ImmutableMapEntry[] entries;
   private final transient ImmutableMapEntry[] table;
   private final transient int mask;
   private static final double MAX_LOAD_FACTOR = 1.2D;
   private static final long serialVersionUID = 0L;

   RegularImmutableMap(ImmutableMapEntry.TerminalEntry... var1) {
      this(var1.length, var1);
   }

   RegularImmutableMap(int var1, ImmutableMapEntry.TerminalEntry[] var2) {
      this.entries = this.createEntryArray(var1);
      int var3 = Hashing.closedTableSize(var1, 1.2D);
      this.table = this.createEntryArray(var3);
      this.mask = var3 - 1;

      for(int var4 = 0; var4 < var1; ++var4) {
         ImmutableMapEntry.TerminalEntry var5 = var2[var4];
         Object var6 = var5.getKey();
         int var7 = Hashing.smear(var6.hashCode()) & this.mask;
         ImmutableMapEntry var8 = this.table[var7];
         Object var9 = var8 == null ? var5 : new RegularImmutableMap.NonTerminalMapEntry(var5, var8);
         this.table[var7] = (ImmutableMapEntry)var9;
         this.entries[var4] = (ImmutableMapEntry)var9;
         this.checkNoConflictInBucket(var6, (ImmutableMapEntry)var9, var8);
      }

   }

   RegularImmutableMap(Entry[] var1) {
      int var2 = var1.length;
      this.entries = this.createEntryArray(var2);
      int var3 = Hashing.closedTableSize(var2, 1.2D);
      this.table = this.createEntryArray(var3);
      this.mask = var3 - 1;

      for(int var4 = 0; var4 < var2; ++var4) {
         Entry var5 = var1[var4];
         Object var6 = var5.getKey();
         Object var7 = var5.getValue();
         CollectPreconditions.checkEntryNotNull(var6, var7);
         int var8 = Hashing.smear(var6.hashCode()) & this.mask;
         ImmutableMapEntry var9 = this.table[var8];
         Object var10 = var9 == null ? new ImmutableMapEntry.TerminalEntry(var6, var7) : new RegularImmutableMap.NonTerminalMapEntry(var6, var7, var9);
         this.table[var8] = (ImmutableMapEntry)var10;
         this.entries[var4] = (ImmutableMapEntry)var10;
         this.checkNoConflictInBucket(var6, (ImmutableMapEntry)var10, var9);
      }

   }

   private void checkNoConflictInBucket(Object var1, ImmutableMapEntry var2, ImmutableMapEntry var3) {
      while(var3 != null) {
         checkNoConflict(!var1.equals(var3.getKey()), "key", var2, var3);
         var3 = var3.getNextInKeyBucket();
      }

   }

   private ImmutableMapEntry[] createEntryArray(int var1) {
      return new ImmutableMapEntry[var1];
   }

   public Object get(@Nullable Object var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = Hashing.smear(var1.hashCode()) & this.mask;

         for(ImmutableMapEntry var3 = this.table[var2]; var3 != null; var3 = var3.getNextInKeyBucket()) {
            Object var4 = var3.getKey();
            if (var1.equals(var4)) {
               return var3.getValue();
            }
         }

         return null;
      }
   }

   public int size() {
      return this.entries.length;
   }

   boolean isPartialView() {
      return false;
   }

   ImmutableSet createEntrySet() {
      return new RegularImmutableMap.EntrySet(this);
   }

   static ImmutableMapEntry[] access$100(RegularImmutableMap var0) {
      return var0.entries;
   }

   private class EntrySet extends ImmutableMapEntrySet {
      final RegularImmutableMap this$0;

      private EntrySet(RegularImmutableMap var1) {
         this.this$0 = var1;
      }

      ImmutableMap map() {
         return this.this$0;
      }

      public UnmodifiableIterator iterator() {
         return this.asList().iterator();
      }

      ImmutableList createAsList() {
         return new RegularImmutableAsList(this, RegularImmutableMap.access$100(this.this$0));
      }

      public Iterator iterator() {
         return this.iterator();
      }

      EntrySet(RegularImmutableMap var1, Object var2) {
         this(var1);
      }
   }

   private static final class NonTerminalMapEntry extends ImmutableMapEntry {
      private final ImmutableMapEntry nextInKeyBucket;

      NonTerminalMapEntry(Object var1, Object var2, ImmutableMapEntry var3) {
         super(var1, var2);
         this.nextInKeyBucket = var3;
      }

      NonTerminalMapEntry(ImmutableMapEntry var1, ImmutableMapEntry var2) {
         super(var1);
         this.nextInKeyBucket = var2;
      }

      ImmutableMapEntry getNextInKeyBucket() {
         return this.nextInKeyBucket;
      }

      @Nullable
      ImmutableMapEntry getNextInValueBucket() {
         return null;
      }
   }
}
