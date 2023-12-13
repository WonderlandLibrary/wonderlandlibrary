package javassist.tools.rmi;

import java.io.Serializable;

public class RemoteRef implements Serializable {
   public int oid;
   public String classname;

   public RemoteRef(int var1) {
      this.oid = var1;
      this.classname = null;
   }

   public RemoteRef(int var1, String var2) {
      this.oid = var1;
      this.classname = var2;
   }
}
