package io.netty.util.internal.logging;

class FormattingTuple {
   static final FormattingTuple NULL = new FormattingTuple((String)null);
   private final String message;
   private final Throwable throwable;
   private final Object[] argArray;

   FormattingTuple(String var1) {
      this(var1, (Object[])null, (Throwable)null);
   }

   FormattingTuple(String var1, Object[] var2, Throwable var3) {
      this.message = var1;
      this.throwable = var3;
      if (var3 == null) {
         this.argArray = var2;
      } else {
         this.argArray = trimmedCopy(var2);
      }

   }

   static Object[] trimmedCopy(Object[] var0) {
      if (var0 != null && var0.length != 0) {
         int var1 = var0.length - 1;
         Object[] var2 = new Object[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
         return var2;
      } else {
         throw new IllegalStateException("non-sensical empty or null argument array");
      }
   }

   public String getMessage() {
      return this.message;
   }

   public Object[] getArgArray() {
      return this.argArray;
   }

   public Throwable getThrowable() {
      return this.throwable;
   }
}
