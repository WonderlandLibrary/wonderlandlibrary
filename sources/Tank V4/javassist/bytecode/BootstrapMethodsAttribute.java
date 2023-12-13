package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class BootstrapMethodsAttribute extends AttributeInfo {
   public static final String tag = "BootstrapMethods";

   BootstrapMethodsAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public BootstrapMethodsAttribute(ConstPool var1, BootstrapMethodsAttribute.BootstrapMethod[] var2) {
      super(var1, "BootstrapMethods");
      int var3 = 2;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3 += 4 + var2[var4].arguments.length * 2;
      }

      byte[] var9 = new byte[var3];
      ByteArray.write16bit(var2.length, var9, 0);
      int var5 = 2;

      for(int var6 = 0; var6 < var2.length; ++var6) {
         ByteArray.write16bit(var2[var6].methodRef, var9, var5);
         ByteArray.write16bit(var2[var6].arguments.length, var9, var5 + 2);
         int[] var7 = var2[var6].arguments;
         var5 += 4;

         for(int var8 = 0; var8 < var7.length; ++var8) {
            ByteArray.write16bit(var7[var8], var9, var5);
            var5 += 2;
         }
      }

      this.set(var9);
   }

   public BootstrapMethodsAttribute.BootstrapMethod[] getMethods() {
      byte[] var1 = this.get();
      int var2 = ByteArray.readU16bit(var1, 0);
      BootstrapMethodsAttribute.BootstrapMethod[] var3 = new BootstrapMethodsAttribute.BootstrapMethod[var2];
      int var4 = 2;

      for(int var5 = 0; var5 < var2; ++var5) {
         int var6 = ByteArray.readU16bit(var1, var4);
         int var7 = ByteArray.readU16bit(var1, var4 + 2);
         int[] var8 = new int[var7];
         var4 += 4;

         for(int var9 = 0; var9 < var7; ++var9) {
            var8[var9] = ByteArray.readU16bit(var1, var4);
            var4 += 2;
         }

         var3[var5] = new BootstrapMethodsAttribute.BootstrapMethod(var6, var8);
      }

      return var3;
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      BootstrapMethodsAttribute.BootstrapMethod[] var3 = this.getMethods();
      ConstPool var4 = this.getConstPool();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         BootstrapMethodsAttribute.BootstrapMethod var6 = var3[var5];
         var6.methodRef = var4.copy(var6.methodRef, var1, var2);

         for(int var7 = 0; var7 < var6.arguments.length; ++var7) {
            var6.arguments[var7] = var4.copy(var6.arguments[var7], var1, var2);
         }
      }

      return new BootstrapMethodsAttribute(var1, var3);
   }

   public static class BootstrapMethod {
      public int methodRef;
      public int[] arguments;

      public BootstrapMethod(int var1, int[] var2) {
         this.methodRef = var1;
         this.arguments = var2;
      }
   }
}
