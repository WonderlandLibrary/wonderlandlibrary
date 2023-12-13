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
final class ImmutableMapValues extends ImmutableCollection {
   private final ImmutableMap map;

   ImmutableMapValues(ImmutableMap var1) {
      this.map = var1;
   }

   public int size() {
      return this.map.size();
   }

   public UnmodifiableIterator iterator() {
      return Maps.valueIterator(this.map.entrySet().iterator());
   }

   public boolean contains(@Nullable Object var1) {
      return var1 != null && Iterators.contains(this.iterator(), var1);
   }

   boolean isPartialView() {
      return true;
   }

   ImmutableList createAsList() {
      ImmutableList var1 = this.map.entrySet().asList();
      return new ImmutableAsList(this, var1) {
         final ImmutableList val$entryList;
         final ImmutableMapValues this$0;

         {
            this.this$0 = var1;
            this.val$entryList = var2;
         }

         public Object get(int var1) {
            return ((Entry)this.val$entryList.get(var1)).getValue();
         }

         ImmutableCollection delegateCollection() {
            return this.this$0;
         }
      };
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new ImmutableMapValues.SerializedForm(this.map);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   @GwtIncompatible("serialization")
   private static class SerializedForm implements Serializable {
      final ImmutableMap map;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap var1) {
         this.map = var1;
      }

      Object readResolve() {
         return this.map.values();
      }
   }
}
