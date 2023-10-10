package javassist.tools.rmi;

public class ObjectNotFoundException extends Exception {
   public ObjectNotFoundException(String var1) {
      super(var1 + " is not exported");
   }

   public ObjectNotFoundException(String var1, Exception var2) {
      super(var1 + " because of " + var2.toString());
   }
}
