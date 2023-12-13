package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
class ImmutableEntry extends AbstractMapEntry implements Serializable {
   final Object key;
   final Object value;
   private static final long serialVersionUID = 0L;

   ImmutableEntry(@Nullable Object var1, @Nullable Object var2) {
      this.key = var1;
      this.value = var2;
   }

   @Nullable
   public final Object getKey() {
      return this.key;
   }

   @Nullable
   public final Object getValue() {
      return this.value;
   }

   public final Object setValue(Object var1) {
      throw new UnsupportedOperationException();
   }
}
