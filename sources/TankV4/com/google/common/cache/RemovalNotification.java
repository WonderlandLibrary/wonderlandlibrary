package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class RemovalNotification implements Entry {
   @Nullable
   private final Object key;
   @Nullable
   private final Object value;
   private final RemovalCause cause;
   private static final long serialVersionUID = 0L;

   RemovalNotification(@Nullable Object var1, @Nullable Object var2, RemovalCause var3) {
      this.key = var1;
      this.value = var2;
      this.cause = (RemovalCause)Preconditions.checkNotNull(var3);
   }

   public RemovalCause getCause() {
      return this.cause;
   }

   public boolean wasEvicted() {
      return this.cause.wasEvicted();
   }

   @Nullable
   public Object getKey() {
      return this.key;
   }

   @Nullable
   public Object getValue() {
      return this.value;
   }

   public final Object setValue(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         return Objects.equal(this.getKey(), var2.getKey()) && Objects.equal(this.getValue(), var2.getValue());
      }
   }

   public int hashCode() {
      Object var1 = this.getKey();
      Object var2 = this.getValue();
      return (var1 == null ? 0 : var1.hashCode()) ^ (var2 == null ? 0 : var2.hashCode());
   }

   public String toString() {
      return this.getKey() + "=" + this.getValue();
   }
}
