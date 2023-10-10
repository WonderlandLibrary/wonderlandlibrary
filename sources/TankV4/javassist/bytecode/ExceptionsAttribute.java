package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ExceptionsAttribute extends AttributeInfo {
   public static final String tag = "Exceptions";

   ExceptionsAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   private ExceptionsAttribute(ConstPool var1, ExceptionsAttribute var2, Map var3) {
      super(var1, "Exceptions");
      this.copyFrom(var2, var3);
   }

   public ExceptionsAttribute(ConstPool var1) {
      super(var1, "Exceptions");
      byte[] var2 = new byte[2];
      var2[0] = var2[1] = 0;
      this.info = var2;
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return new ExceptionsAttribute(var1, this, var2);
   }

   private void copyFrom(ExceptionsAttribute var1, Map var2) {
      ConstPool var3 = var1.constPool;
      ConstPool var4 = this.constPool;
      byte[] var5 = var1.info;
      int var6 = var5.length;
      byte[] var7 = new byte[var6];
      var7[0] = var5[0];
      var7[1] = var5[1];

      for(int var8 = 2; var8 < var6; var8 += 2) {
         int var9 = ByteArray.readU16bit(var5, var8);
         ByteArray.write16bit(var3.copy(var9, var4, var2), var7, var8);
      }

      this.info = var7;
   }

   public int[] getExceptionIndexes() {
      byte[] var1 = this.info;
      int var2 = var1.length;
      if (var2 <= 2) {
         return null;
      } else {
         int[] var3 = new int[var2 / 2 - 1];
         int var4 = 0;

         for(int var5 = 2; var5 < var2; var5 += 2) {
            var3[var4++] = (var1[var5] & 255) << 8 | var1[var5 + 1] & 255;
         }

         return var3;
      }
   }

   public String[] getExceptions() {
      byte[] var1 = this.info;
      int var2 = var1.length;
      if (var2 <= 2) {
         return null;
      } else {
         String[] var3 = new String[var2 / 2 - 1];
         int var4 = 0;

         for(int var5 = 2; var5 < var2; var5 += 2) {
            int var6 = (var1[var5] & 255) << 8 | var1[var5 + 1] & 255;
            var3[var4++] = this.constPool.getClassInfo(var6);
         }

         return var3;
      }
   }

   public void setExceptionIndexes(int[] var1) {
      int var2 = var1.length;
      byte[] var3 = new byte[var2 * 2 + 2];
      ByteArray.write16bit(var2, var3, 0);

      for(int var4 = 0; var4 < var2; ++var4) {
         ByteArray.write16bit(var1[var4], var3, var4 * 2 + 2);
      }

      this.info = var3;
   }

   public void setExceptions(String[] var1) {
      int var2 = var1.length;
      byte[] var3 = new byte[var2 * 2 + 2];
      ByteArray.write16bit(var2, var3, 0);

      for(int var4 = 0; var4 < var2; ++var4) {
         ByteArray.write16bit(this.constPool.addClassInfo(var1[var4]), var3, var4 * 2 + 2);
      }

      this.info = var3;
   }

   public int tableLength() {
      return this.info.length / 2 - 1;
   }

   public int getException(int var1) {
      int var2 = var1 * 2 + 2;
      return (this.info[var2] & 255) << 8 | this.info[var2 + 1] & 255;
   }
}
