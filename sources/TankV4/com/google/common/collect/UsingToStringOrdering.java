package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(
   serializable = true
)
final class UsingToStringOrdering extends Ordering implements Serializable {
   static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
   private static final long serialVersionUID = 0L;

   public int compare(Object var1, Object var2) {
      return var1.toString().compareTo(var2.toString());
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public String toString() {
      return "Ordering.usingToString()";
   }

   private UsingToStringOrdering() {
   }
}
