package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

abstract class ConstInfo {
   int index;

   public ConstInfo(int var1) {
      this.index = var1;
   }

   public abstract int getTag();

   public String getClassName(ConstPool var1) {
      return null;
   }

   public void renameClass(ConstPool var1, String var2, String var3, HashMap var4) {
   }

   public void renameClass(ConstPool var1, Map var2, HashMap var3) {
   }

   public abstract int copy(ConstPool var1, ConstPool var2, Map var3);

   public abstract void write(DataOutputStream var1) throws IOException;

   public abstract void print(PrintWriter var1);

   public String toString() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      PrintWriter var2 = new PrintWriter(var1);
      this.print(var2);
      return var1.toString();
   }
}
