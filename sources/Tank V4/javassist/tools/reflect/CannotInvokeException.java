package javassist.tools.reflect;

import java.lang.reflect.InvocationTargetException;

public class CannotInvokeException extends RuntimeException {
   private Throwable err = null;

   public Throwable getReason() {
      return this.err;
   }

   public CannotInvokeException(String var1) {
      super(var1);
   }

   public CannotInvokeException(InvocationTargetException var1) {
      super("by " + var1.getTargetException().toString());
      this.err = var1.getTargetException();
   }

   public CannotInvokeException(IllegalAccessException var1) {
      super("by " + var1.toString());
      this.err = var1;
   }

   public CannotInvokeException(ClassNotFoundException var1) {
      super("by " + var1.toString());
      this.err = var1;
   }
}
