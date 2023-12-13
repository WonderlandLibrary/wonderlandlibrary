package io.netty.util;

public class IllegalReferenceCountException extends IllegalStateException {
   private static final long serialVersionUID = -2507492394288153468L;

   public IllegalReferenceCountException() {
   }

   public IllegalReferenceCountException(int var1) {
      this("refCnt: " + var1);
   }

   public IllegalReferenceCountException(int var1, int var2) {
      this("refCnt: " + var1 + ", " + (var2 > 0 ? "increment: " + var2 : "decrement: " + -var2));
   }

   public IllegalReferenceCountException(String var1) {
      super(var1);
   }

   public IllegalReferenceCountException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public IllegalReferenceCountException(Throwable var1) {
      super(var1);
   }
}
