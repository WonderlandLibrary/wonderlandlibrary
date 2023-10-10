package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Absent extends Optional {
   static final Absent INSTANCE = new Absent();
   private static final long serialVersionUID = 0L;

   static Optional withType() {
      return INSTANCE;
   }

   private Absent() {
   }

   public boolean isPresent() {
      return false;
   }

   public Object get() {
      throw new IllegalStateException("Optional.get() cannot be called on an absent value");
   }

   public Object or(Object var1) {
      return Preconditions.checkNotNull(var1, "use Optional.orNull() instead of Optional.or(null)");
   }

   public Optional or(Optional var1) {
      return (Optional)Preconditions.checkNotNull(var1);
   }

   public Object or(Supplier var1) {
      return Preconditions.checkNotNull(var1.get(), "use Optional.orNull() instead of a Supplier that returns null");
   }

   @Nullable
   public Object orNull() {
      return null;
   }

   public Set asSet() {
      return Collections.emptySet();
   }

   public Optional transform(Function var1) {
      Preconditions.checkNotNull(var1);
      return Optional.absent();
   }

   public boolean equals(@Nullable Object var1) {
      return var1 == this;
   }

   public int hashCode() {
      return 1502476572;
   }

   public String toString() {
      return "Optional.absent()";
   }

   private Object readResolve() {
      return INSTANCE;
   }
}
