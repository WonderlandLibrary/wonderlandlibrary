package joptsimple;

public class ValueConversionException extends RuntimeException {
   private static final long serialVersionUID = -1L;

   public ValueConversionException(String var1) {
      this(var1, (Throwable)null);
   }

   public ValueConversionException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
