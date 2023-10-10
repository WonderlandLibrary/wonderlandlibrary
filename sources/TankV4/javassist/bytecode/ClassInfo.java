package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class ClassInfo extends ConstInfo {
   static final int tag = 7;
   int name;

   public ClassInfo(int var1, int var2) {
      super(var2);
      this.name = var1;
   }

   public ClassInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.name = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.name;
   }

   public boolean equals(Object var1) {
      return var1 instanceof ClassInfo && ((ClassInfo)var1).name == this.name;
   }

   public int getTag() {
      return 7;
   }

   public String getClassName(ConstPool var1) {
      return var1.getUtf8Info(this.name);
   }

   public void renameClass(ConstPool var1, String var2, String var3, HashMap var4) {
      String var5 = var1.getUtf8Info(this.name);
      String var6 = null;
      if (var5.equals(var2)) {
         var6 = var3;
      } else if (var5.charAt(0) == '[') {
         String var7 = Descriptor.rename(var5, var2, var3);
         if (var5 != var7) {
            var6 = var7;
         }
      }

      if (var6 != null) {
         if (var4 == null) {
            this.name = var1.addUtf8Info(var6);
         } else {
            var4.remove(this);
            this.name = var1.addUtf8Info(var6);
            var4.put(this, this);
         }
      }

   }

   public void renameClass(ConstPool var1, Map var2, HashMap var3) {
      String var4 = var1.getUtf8Info(this.name);
      String var5 = null;
      String var6;
      if (var4.charAt(0) == '[') {
         var6 = Descriptor.rename(var4, var2);
         if (var4 != var6) {
            var5 = var6;
         }
      } else {
         var6 = (String)var2.get(var4);
         if (var6 != null && !var6.equals(var4)) {
            var5 = var6;
         }
      }

      if (var5 != null) {
         if (var3 == null) {
            this.name = var1.addUtf8Info(var5);
         } else {
            var3.remove(this);
            this.name = var1.addUtf8Info(var5);
            var3.put(this, this);
         }
      }

   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      String var4 = var1.getUtf8Info(this.name);
      if (var3 != null) {
         String var5 = (String)var3.get(var4);
         if (var5 != null) {
            var4 = var5;
         }
      }

      return var2.addClassInfo(var4);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(7);
      var1.writeShort(this.name);
   }

   public void print(PrintWriter var1) {
      var1.print("Class #");
      var1.println(this.name);
   }
}
