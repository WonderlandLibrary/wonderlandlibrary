package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class ImmutableMapKeySet extends ImmutableSet {
   private final ImmutableMap map;

   ImmutableMapKeySet(ImmutableMap var1) {
      this.map = var1;
   }

   public int size() {
      return this.map.size();
   }

   public UnmodifiableIterator iterator() {
      return this.asList().iterator();
   }

   public boolean contains(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   ImmutableList createAsList() {
      ImmutableList var1 = this.map.entrySet().asList();
      return new ImmutableAsList(this, var1) {
         final ImmutableList val$entryList;
         final ImmutableMapKeySet this$0;

         {
            this.this$0 = var1;
            this.val$entryList = var2;
         }

         public Object get(int var1) {
            return ((Entry)this.val$entryList.get(var1)).getKey();
         }

         ImmutableCollection delegateCollection() {
            return this.this$0;
         }
      };
   }

   boolean isPartialView() {
      return true;
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new ImmutableMapKeySet.KeySetSerializedForm(this.map);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   @GwtIncompatible("serialization")
   private static class KeySetSerializedForm implements Serializable {
      final ImmutableMap map;
      private static final long serialVersionUID = 0L;

      KeySetSerializedForm(ImmutableMap var1) {
         this.map = var1;
      }

      Object readResolve() {
         return this.map.keySet();
      }
   }
}
