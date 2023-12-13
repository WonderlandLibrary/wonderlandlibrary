package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class NameAndTypeInfo extends ConstInfo {
   static final int tag = 12;
   int memberName;
   int typeDescriptor;

   public NameAndTypeInfo(int var1, int var2, int var3) {
      super(var3);
      this.memberName = var1;
      this.typeDescriptor = var2;
   }

   public NameAndTypeInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.memberName = var1.readUnsignedShort();
      this.typeDescriptor = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.memberName << 16 ^ this.typeDescriptor;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof NameAndTypeInfo)) {
         return false;
      } else {
         NameAndTypeInfo var2 = (NameAndTypeInfo)var1;
         return var2.memberName == this.memberName && var2.typeDescriptor == this.typeDescriptor;
      }
   }

   public int getTag() {
      return 12;
   }

   public void renameClass(ConstPool var1, String var2, String var3, HashMap var4) {
      String var5 = var1.getUtf8Info(this.typeDescriptor);
      String var6 = Descriptor.rename(var5, var2, var3);
      if (var5 != var6) {
         if (var4 == null) {
            this.typeDescriptor = var1.addUtf8Info(var6);
         } else {
            var4.remove(this);
            this.typeDescriptor = var1.addUtf8Info(var6);
            var4.put(this, this);
         }
      }

   }

   public void renameClass(ConstPool var1, Map var2, HashMap var3) {
      String var4 = var1.getUtf8Info(this.typeDescriptor);
      String var5 = Descriptor.rename(var4, var2);
      if (var4 != var5) {
         if (var3 == null) {
            this.typeDescriptor = var1.addUtf8Info(var5);
         } else {
            var3.remove(this);
            this.typeDescriptor = var1.addUtf8Info(var5);
            var3.put(this, this);
         }
      }

   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      String var4 = var1.getUtf8Info(this.memberName);
      String var5 = var1.getUtf8Info(this.typeDescriptor);
      var5 = Descriptor.rename(var5, var3);
      return var2.addNameAndTypeInfo(var2.addUtf8Info(var4), var2.addUtf8Info(var5));
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(12);
      var1.writeShort(this.memberName);
      var1.writeShort(this.typeDescriptor);
   }

   public void print(PrintWriter var1) {
      var1.print("NameAndType #");
      var1.print(this.memberName);
      var1.print(", type #");
      var1.println(this.typeDescriptor);
   }
}
