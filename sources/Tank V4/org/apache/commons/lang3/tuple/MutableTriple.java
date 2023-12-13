package org.apache.commons.lang3.tuple;

public class MutableTriple extends Triple {
   private static final long serialVersionUID = 1L;
   public Object left;
   public Object middle;
   public Object right;

   public static MutableTriple of(Object var0, Object var1, Object var2) {
      return new MutableTriple(var0, var1, var2);
   }

   public MutableTriple() {
   }

   public MutableTriple(Object var1, Object var2, Object var3) {
      this.left = var1;
      this.middle = var2;
      this.right = var3;
   }

   public Object getLeft() {
      return this.left;
   }

   public void setLeft(Object var1) {
      this.left = var1;
   }

   public Object getMiddle() {
      return this.middle;
   }

   public void setMiddle(Object var1) {
      this.middle = var1;
   }

   public Object getRight() {
      return this.right;
   }

   public void setRight(Object var1) {
      this.right = var1;
   }
}
