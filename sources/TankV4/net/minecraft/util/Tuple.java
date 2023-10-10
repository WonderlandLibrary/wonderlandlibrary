package net.minecraft.util;

public class Tuple {
   private Object a;
   private Object b;

   public Tuple(Object var1, Object var2) {
      this.a = var1;
      this.b = var2;
   }

   public Object getFirst() {
      return this.a;
   }

   public Object getSecond() {
      return this.b;
   }
}
