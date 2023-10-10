package javassist;

public class NotFoundException extends Exception {
   public NotFoundException(String var1) {
      super(var1);
   }

   public NotFoundException(String var1, Exception var2) {
      super(var1 + " because of " + var2.toString());
   }
}
