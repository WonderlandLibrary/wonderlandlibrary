package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class ImmutableMapEntrySet extends ImmutableSet {
   abstract ImmutableMap map();

   public int size() {
      return this.map().size();
   }

   public boolean contains(@Nullable Object var1) {
      if (!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         Object var3 = this.map().get(var2.getKey());
         return var3 != null && var3.equals(var2.getValue());
      }
   }

   boolean isPartialView() {
      return this.map().isPartialView();
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new ImmutableMapEntrySet.EntrySetSerializedForm(this.map());
   }

   @GwtIncompatible("serialization")
   private static class EntrySetSerializedForm implements Serializable {
      final ImmutableMap map;
      private static final long serialVersionUID = 0L;

      EntrySetSerializedForm(ImmutableMap var1) {
         this.map = var1;
      }

      Object readResolve() {
         return this.map.entrySet();
      }
   }
}
