package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class DoubleInfo extends ConstInfo {
   static final int tag = 6;
   double value;

   public DoubleInfo(double var1, int var3) {
      super(var3);
      this.value = var1;
   }

   public DoubleInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.value = var1.readDouble();
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.value);
      return (int)(var1 ^ var1 >>> 32);
   }

   public boolean equals(Object var1) {
      return var1 instanceof DoubleInfo && ((DoubleInfo)var1).value == this.value;
   }

   public int getTag() {
      return 6;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addDoubleInfo(this.value);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(6);
      var1.writeDouble(this.value);
   }

   public void print(PrintWriter var1) {
      var1.print("Double ");
      var1.println(this.value);
   }
}
