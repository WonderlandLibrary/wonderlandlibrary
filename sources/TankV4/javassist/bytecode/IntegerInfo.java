package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class IntegerInfo extends ConstInfo {
   static final int tag = 3;
   int value;

   public IntegerInfo(int var1, int var2) {
      super(var2);
      this.value = var1;
   }

   public IntegerInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.value = var1.readInt();
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(Object var1) {
      return var1 instanceof IntegerInfo && ((IntegerInfo)var1).value == this.value;
   }

   public int getTag() {
      return 3;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addIntegerInfo(this.value);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(3);
      var1.writeInt(this.value);
   }

   public void print(PrintWriter var1) {
      var1.print("Integer ");
      var1.println(this.value);
   }
}
