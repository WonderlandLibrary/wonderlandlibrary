package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class InvokeDynamicInfo extends ConstInfo {
   static final int tag = 18;
   int bootstrap;
   int nameAndType;

   public InvokeDynamicInfo(int var1, int var2, int var3) {
      super(var3);
      this.bootstrap = var1;
      this.nameAndType = var2;
   }

   public InvokeDynamicInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.bootstrap = var1.readUnsignedShort();
      this.nameAndType = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.bootstrap << 16 ^ this.nameAndType;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof InvokeDynamicInfo)) {
         return false;
      } else {
         InvokeDynamicInfo var2 = (InvokeDynamicInfo)var1;
         return var2.bootstrap == this.bootstrap && var2.nameAndType == this.nameAndType;
      }
   }

   public int getTag() {
      return 18;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addInvokeDynamicInfo(this.bootstrap, var1.getItem(this.nameAndType).copy(var1, var2, var3));
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(18);
      var1.writeShort(this.bootstrap);
      var1.writeShort(this.nameAndType);
   }

   public void print(PrintWriter var1) {
      var1.print("InvokeDynamic #");
      var1.print(this.bootstrap);
      var1.print(", name&type #");
      var1.println(this.nameAndType);
   }
}
