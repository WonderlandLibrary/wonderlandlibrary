package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

abstract class MemberrefInfo extends ConstInfo {
   int classIndex;
   int nameAndTypeIndex;

   public MemberrefInfo(int var1, int var2, int var3) {
      super(var3);
      this.classIndex = var1;
      this.nameAndTypeIndex = var2;
   }

   public MemberrefInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.classIndex = var1.readUnsignedShort();
      this.nameAndTypeIndex = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.classIndex << 16 ^ this.nameAndTypeIndex;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MemberrefInfo)) {
         return false;
      } else {
         MemberrefInfo var2 = (MemberrefInfo)var1;
         return var2.classIndex == this.classIndex && var2.nameAndTypeIndex == this.nameAndTypeIndex && var2.getClass() == this.getClass();
      }
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      int var4 = var1.getItem(this.classIndex).copy(var1, var2, var3);
      int var5 = var1.getItem(this.nameAndTypeIndex).copy(var1, var2, var3);
      return this.copy2(var2, var4, var5);
   }

   protected abstract int copy2(ConstPool var1, int var2, int var3);

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(this.getTag());
      var1.writeShort(this.classIndex);
      var1.writeShort(this.nameAndTypeIndex);
   }

   public void print(PrintWriter var1) {
      var1.print(this.getTagName() + " #");
      var1.print(this.classIndex);
      var1.print(", name&type #");
      var1.println(this.nameAndTypeIndex);
   }

   public abstract String getTagName();
}
