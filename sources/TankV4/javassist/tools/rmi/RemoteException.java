package javassist.tools.rmi;

public class RemoteException extends RuntimeException {
   public RemoteException(String var1) {
      super(var1);
   }

   public RemoteException(Exception var1) {
      super("by " + var1.toString());
   }
}
