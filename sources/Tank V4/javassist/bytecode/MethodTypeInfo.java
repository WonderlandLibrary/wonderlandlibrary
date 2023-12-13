package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class MethodTypeInfo extends ConstInfo {
   static final int tag = 16;
   int descriptor;

   public MethodTypeInfo(int var1, int var2) {
      super(var2);
      this.descriptor = var1;
   }

   public MethodTypeInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.descriptor = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.descriptor;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MethodTypeInfo) {
         return ((MethodTypeInfo)var1).descriptor == this.descriptor;
      } else {
         return false;
      }
   }

   public int getTag() {
      return 16;
   }

   public void renameClass(ConstPool var1, String var2, String var3, HashMap var4) {
      String var5 = var1.getUtf8Info(this.descriptor);
      String var6 = Descriptor.rename(var5, var2, var3);
      if (var5 != var6) {
         if (var4 == null) {
            this.descriptor = var1.addUtf8Info(var6);
         } else {
            var4.remove(this);
            this.descriptor = var1.addUtf8Info(var6);
            var4.put(this, this);
         }
      }

   }

   public void renameClass(ConstPool var1, Map var2, HashMap var3) {
      String var4 = var1.getUtf8Info(this.descriptor);
      String var5 = Descriptor.rename(var4, var2);
      if (var4 != var5) {
         if (var3 == null) {
            this.descriptor = var1.addUtf8Info(var5);
         } else {
            var3.remove(this);
            this.descriptor = var1.addUtf8Info(var5);
            var3.put(this, this);
         }
      }

   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      String var4 = var1.getUtf8Info(this.descriptor);
      var4 = Descriptor.rename(var4, var3);
      return var2.addMethodTypeInfo(var2.addUtf8Info(var4));
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(16);
      var1.writeShort(this.descriptor);
   }

   public void print(PrintWriter var1) {
      var1.print("MethodType #");
      var1.println(this.descriptor);
   }
}
