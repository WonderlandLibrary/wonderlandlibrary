package io.netty.handler.codec;

public class UnsupportedMessageTypeException extends CodecException {
   private static final long serialVersionUID = 2799598826487038726L;

   public UnsupportedMessageTypeException(Object var1, Class... var2) {
      super(message(var1 == null ? "null" : var1.getClass().getName(), var2));
   }

   public UnsupportedMessageTypeException() {
   }

   public UnsupportedMessageTypeException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public UnsupportedMessageTypeException(String var1) {
      super(var1);
   }

   public UnsupportedMessageTypeException(Throwable var1) {
      super(var1);
   }

   private static String message(String var0, Class... var1) {
      StringBuilder var2 = new StringBuilder(var0);
      if (var1 != null && var1.length > 0) {
         var2.append(" (expected: ").append(var1[0].getName());

         for(int var3 = 1; var3 < var1.length; ++var3) {
            Class var4 = var1[var3];
            if (var4 == null) {
               break;
            }

            var2.append(", ").append(var4.getName());
         }

         var2.append(')');
      }

      return var2.toString();
   }
}
