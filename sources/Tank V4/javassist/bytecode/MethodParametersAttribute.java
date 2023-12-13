package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class MethodParametersAttribute extends AttributeInfo {
   public static final String tag = "MethodParameters";

   MethodParametersAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public MethodParametersAttribute(ConstPool var1, String[] var2, int[] var3) {
      super(var1, "MethodParameters");
      byte[] var4 = new byte[var2.length * 4 + 1];
      var4[0] = (byte)var2.length;

      for(int var5 = 0; var5 < var2.length; ++var5) {
         ByteArray.write16bit(var1.addUtf8Info(var2[var5]), var4, var5 * 4 + 1);
         ByteArray.write16bit(var3[var5], var4, var5 * 4 + 3);
      }

      this.set(var4);
   }

   public int size() {
      return this.info[0] & 255;
   }

   public int name(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 4 + 1);
   }

   public int accessFlags(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 4 + 3);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      int var3 = this.size();
      ConstPool var4 = this.getConstPool();
      String[] var5 = new String[var3];
      int[] var6 = new int[var3];

      for(int var7 = 0; var7 < var3; ++var7) {
         var5[var7] = var4.getUtf8Info(this.name(var7));
         var6[var7] = this.accessFlags(var7);
      }

      return new MethodParametersAttribute(var1, var5, var6);
   }
}
