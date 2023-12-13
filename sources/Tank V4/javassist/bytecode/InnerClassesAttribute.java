package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class InnerClassesAttribute extends AttributeInfo {
   public static final String tag = "InnerClasses";

   InnerClassesAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   private InnerClassesAttribute(ConstPool var1, byte[] var2) {
      super(var1, "InnerClasses", var2);
   }

   public InnerClassesAttribute(ConstPool var1) {
      super(var1, "InnerClasses", new byte[2]);
      ByteArray.write16bit(0, this.get(), 0);
   }

   public int tableLength() {
      return ByteArray.readU16bit(this.get(), 0);
   }

   public int innerClassIndex(int var1) {
      return ByteArray.readU16bit(this.get(), var1 * 8 + 2);
   }

   public String innerClass(int var1) {
      int var2 = this.innerClassIndex(var1);
      return var2 == 0 ? null : this.constPool.getClassInfo(var2);
   }

   public void setInnerClassIndex(int var1, int var2) {
      ByteArray.write16bit(var2, this.get(), var1 * 8 + 2);
   }

   public int outerClassIndex(int var1) {
      return ByteArray.readU16bit(this.get(), var1 * 8 + 4);
   }

   public String outerClass(int var1) {
      int var2 = this.outerClassIndex(var1);
      return var2 == 0 ? null : this.constPool.getClassInfo(var2);
   }

   public void setOuterClassIndex(int var1, int var2) {
      ByteArray.write16bit(var2, this.get(), var1 * 8 + 4);
   }

   public int innerNameIndex(int var1) {
      return ByteArray.readU16bit(this.get(), var1 * 8 + 6);
   }

   public String innerName(int var1) {
      int var2 = this.innerNameIndex(var1);
      return var2 == 0 ? null : this.constPool.getUtf8Info(var2);
   }

   public void setInnerNameIndex(int var1, int var2) {
      ByteArray.write16bit(var2, this.get(), var1 * 8 + 6);
   }

   public int accessFlags(int var1) {
      return ByteArray.readU16bit(this.get(), var1 * 8 + 8);
   }

   public void setAccessFlags(int var1, int var2) {
      ByteArray.write16bit(var2, this.get(), var1 * 8 + 8);
   }

   public void append(String var1, String var2, String var3, int var4) {
      int var5 = this.constPool.addClassInfo(var1);
      int var6 = this.constPool.addClassInfo(var2);
      int var7 = this.constPool.addUtf8Info(var3);
      this.append(var5, var6, var7, var4);
   }

   public void append(int var1, int var2, int var3, int var4) {
      byte[] var5 = this.get();
      int var6 = var5.length;
      byte[] var7 = new byte[var6 + 8];

      int var8;
      for(var8 = 2; var8 < var6; ++var8) {
         var7[var8] = var5[var8];
      }

      var8 = ByteArray.readU16bit(var5, 0);
      ByteArray.write16bit(var8 + 1, var7, 0);
      ByteArray.write16bit(var1, var7, var6);
      ByteArray.write16bit(var2, var7, var6 + 2);
      ByteArray.write16bit(var3, var7, var6 + 4);
      ByteArray.write16bit(var4, var7, var6 + 6);
      this.set(var7);
   }

   public int remove(int var1) {
      byte[] var2 = this.get();
      int var3 = var2.length;
      if (var3 < 10) {
         return 0;
      } else {
         int var4 = ByteArray.readU16bit(var2, 0);
         int var5 = 2 + var1 * 8;
         if (var4 <= var1) {
            return var4;
         } else {
            byte[] var6 = new byte[var3 - 8];
            ByteArray.write16bit(var4 - 1, var6, 0);
            int var7 = 2;
            int var8 = 2;

            while(var7 < var3) {
               if (var7 == var5) {
                  var7 += 8;
               } else {
                  var6[var8++] = var2[var7++];
               }
            }

            this.set(var6);
            return var4 - 1;
         }
      }
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      byte[] var3 = this.get();
      byte[] var4 = new byte[var3.length];
      ConstPool var5 = this.getConstPool();
      InnerClassesAttribute var6 = new InnerClassesAttribute(var1, var4);
      int var7 = ByteArray.readU16bit(var3, 0);
      ByteArray.write16bit(var7, var4, 0);
      int var8 = 2;

      for(int var9 = 0; var9 < var7; ++var9) {
         int var10 = ByteArray.readU16bit(var3, var8);
         int var11 = ByteArray.readU16bit(var3, var8 + 2);
         int var12 = ByteArray.readU16bit(var3, var8 + 4);
         int var13 = ByteArray.readU16bit(var3, var8 + 6);
         if (var10 != 0) {
            var10 = var5.copy(var10, var1, var2);
         }

         ByteArray.write16bit(var10, var4, var8);
         if (var11 != 0) {
            var11 = var5.copy(var11, var1, var2);
         }

         ByteArray.write16bit(var11, var4, var8 + 2);
         if (var12 != 0) {
            var12 = var5.copy(var12, var1, var2);
         }

         ByteArray.write16bit(var12, var4, var8 + 4);
         ByteArray.write16bit(var13, var4, var8 + 6);
         var8 += 8;
      }

      return var6;
   }
}
