package org.apache.commons.lang3.tuple;

public final class ImmutableTriple extends Triple {
   private static final long serialVersionUID = 1L;
   public final Object left;
   public final Object middle;
   public final Object right;

   public static ImmutableTriple of(Object var0, Object var1, Object var2) {
      return new ImmutableTriple(var0, var1, var2);
   }

   public ImmutableTriple(Object var1, Object var2, Object var3) {
      this.left = var1;
      this.middle = var2;
      this.right = var3;
   }

   public Object getLeft() {
      return this.left;
   }

   public Object getMiddle() {
      return this.middle;
   }

   public Object getRight() {
      return this.right;
   }
}
