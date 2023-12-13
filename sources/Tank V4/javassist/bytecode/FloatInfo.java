package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class FloatInfo extends ConstInfo {
   static final int tag = 4;
   float value;

   public FloatInfo(float var1, int var2) {
      super(var2);
      this.value = var1;
   }

   public FloatInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.value = var1.readFloat();
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public boolean equals(Object var1) {
      return var1 instanceof FloatInfo && ((FloatInfo)var1).value == this.value;
   }

   public int getTag() {
      return 4;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addFloatInfo(this.value);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(4);
      var1.writeFloat(this.value);
   }

   public void print(PrintWriter var1) {
      var1.print("Float ");
      var1.println(this.value);
   }
}
