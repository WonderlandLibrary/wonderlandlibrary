package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class LongInfo extends ConstInfo {
   static final int tag = 5;
   long value;

   public LongInfo(long var1, int var3) {
      super(var3);
      this.value = var1;
   }

   public LongInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.value = var1.readLong();
   }

   public int hashCode() {
      return (int)(this.value ^ this.value >>> 32);
   }

   public boolean equals(Object var1) {
      return var1 instanceof LongInfo && ((LongInfo)var1).value == this.value;
   }

   public int getTag() {
      return 5;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addLongInfo(this.value);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(5);
      var1.writeLong(this.value);
   }

   public void print(PrintWriter var1) {
      var1.print("Long ");
      var1.println(this.value);
   }
}
