package javassist.tools.reflect;

public class CannotCreateException extends Exception {
   public CannotCreateException(String var1) {
      super(var1);
   }

   public CannotCreateException(Exception var1) {
      super("by " + var1.toString());
   }
}
