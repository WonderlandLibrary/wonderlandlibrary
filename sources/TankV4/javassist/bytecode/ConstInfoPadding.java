package javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class ConstInfoPadding extends ConstInfo {
   public ConstInfoPadding(int var1) {
      super(var1);
   }

   public int getTag() {
      return 0;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addConstInfoPadding();
   }

   public void write(DataOutputStream var1) throws IOException {
   }

   public void print(PrintWriter var1) {
      var1.println("padding");
   }
}
