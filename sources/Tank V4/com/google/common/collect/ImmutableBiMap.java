package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableBiMap extends ImmutableMap implements BiMap {
   private static final Entry[] EMPTY_ENTRY_ARRAY = new Entry[0];

   public static ImmutableBiMap of() {
      return EmptyImmutableBiMap.INSTANCE;
   }

   public static ImmutableBiMap of(Object var0, Object var1) {
      return new SingletonImmutableBiMap(var0, var1);
   }

   public static ImmutableBiMap of(Object var0, Object var1, Object var2, Object var3) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3)});
   }

   public static ImmutableBiMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5)});
   }

   public static ImmutableBiMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7)});
   }

   public static ImmutableBiMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9)});
   }

   public static ImmutableBiMap.Builder builder() {
      return new ImmutableBiMap.Builder();
   }

   public static ImmutableBiMap copyOf(Map var0) {
      if (var0 instanceof ImmutableBiMap) {
         ImmutableBiMap var1 = (ImmutableBiMap)var0;
         if (!var1.isPartialView()) {
            return var1;
         }
      }

      Entry[] var3 = (Entry[])var0.entrySet().toArray(EMPTY_ENTRY_ARRAY);
      switch(var3.length) {
      case 0:
         return of();
      case 1:
         Entry var2 = var3[0];
         return of(var2.getKey(), var2.getValue());
      default:
         return new RegularImmutableBiMap(var3);
      }
   }

   ImmutableBiMap() {
   }

   public abstract ImmutableBiMap inverse();

   public ImmutableSet values() {
      return this.inverse().keySet();
   }

   /** @deprecated */
   @Deprecated
   public Object forcePut(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   Object writeReplace() {
      return new ImmutableBiMap.SerializedForm(this);
   }

   public ImmutableCollection values() {
      return this.values();
   }

   public Collection values() {
      return this.values();
   }

   public BiMap inverse() {
      return this.inverse();
   }

   public Set values() {
      return this.values();
   }

   private static class SerializedForm extends ImmutableMap.SerializedForm {
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableBiMap var1) {
         super(var1);
      }

      Object readResolve() {
         ImmutableBiMap.Builder var1 = new ImmutableBiMap.Builder();
         return this.createMap(var1);
      }
   }

   public static final class Builder extends ImmutableMap.Builder {
      public ImmutableBiMap.Builder put(Object var1, Object var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableBiMap.Builder putAll(Map var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableBiMap build() {
         switch(this.size) {
         case 0:
            return ImmutableBiMap.of();
         case 1:
            return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
         default:
            return new RegularImmutableBiMap(this.size, this.entries);
         }
      }

      public ImmutableMap build() {
         return this.build();
      }

      public ImmutableMap.Builder putAll(Map var1) {
         return this.putAll(var1);
      }

      public ImmutableMap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
