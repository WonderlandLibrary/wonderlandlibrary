package javassist.tools.rmi;

public class Sample {
   private ObjectImporter importer;
   private int objectId;

   public Object forward(Object[] var1, int var2) {
      return this.importer.call(this.objectId, var2, var1);
   }

   public static Object forwardStatic(Object[] var0, int var1) throws RemoteException {
      throw new RemoteException("cannot call a static method.");
   }
}
