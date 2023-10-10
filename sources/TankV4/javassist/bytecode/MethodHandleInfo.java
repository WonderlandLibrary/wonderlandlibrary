package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class MethodHandleInfo extends ConstInfo {
   static final int tag = 15;
   int refKind;
   int refIndex;

   public MethodHandleInfo(int var1, int var2, int var3) {
      super(var3);
      this.refKind = var1;
      this.refIndex = var2;
   }

   public MethodHandleInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.refKind = var1.readUnsignedByte();
      this.refIndex = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.refKind << 16 ^ this.refIndex;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MethodHandleInfo)) {
         return false;
      } else {
         MethodHandleInfo var2 = (MethodHandleInfo)var1;
         return var2.refKind == this.refKind && var2.refIndex == this.refIndex;
      }
   }

   public int getTag() {
      return 15;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addMethodHandleInfo(this.refKind, var1.getItem(this.refIndex).copy(var1, var2, var3));
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(15);
      var1.writeByte(this.refKind);
      var1.writeShort(this.refIndex);
   }

   public void print(PrintWriter var1) {
      var1.print("MethodHandle #");
      var1.print(this.refKind);
      var1.print(", index #");
      var1.println(this.refIndex);
   }
}
