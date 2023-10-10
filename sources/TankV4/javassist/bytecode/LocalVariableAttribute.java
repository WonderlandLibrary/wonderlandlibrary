package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class LocalVariableAttribute extends AttributeInfo {
   public static final String tag = "LocalVariableTable";
   public static final String typeTag = "LocalVariableTypeTable";

   public LocalVariableAttribute(ConstPool var1) {
      super(var1, "LocalVariableTable", new byte[2]);
      ByteArray.write16bit(0, this.info, 0);
   }

   /** @deprecated */
   public LocalVariableAttribute(ConstPool var1, String var2) {
      super(var1, var2, new byte[2]);
      ByteArray.write16bit(0, this.info, 0);
   }

   LocalVariableAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   LocalVariableAttribute(ConstPool var1, String var2, byte[] var3) {
      super(var1, var2, var3);
   }

   public void addEntry(int var1, int var2, int var3, int var4, int var5) {
      int var6 = this.info.length;
      byte[] var7 = new byte[var6 + 10];
      ByteArray.write16bit(this.tableLength() + 1, var7, 0);

      for(int var8 = 2; var8 < var6; ++var8) {
         var7[var8] = this.info[var8];
      }

      ByteArray.write16bit(var1, var7, var6);
      ByteArray.write16bit(var2, var7, var6 + 2);
      ByteArray.write16bit(var3, var7, var6 + 4);
      ByteArray.write16bit(var4, var7, var6 + 6);
      ByteArray.write16bit(var5, var7, var6 + 8);
      this.info = var7;
   }

   void renameClass(String var1, String var2) {
      ConstPool var3 = this.getConstPool();
      int var4 = this.tableLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var5 * 10 + 2;
         int var7 = ByteArray.readU16bit(this.info, var6 + 6);
         if (var7 != 0) {
            String var8 = var3.getUtf8Info(var7);
            var8 = this.renameEntry(var8, var1, var2);
            ByteArray.write16bit(var3.addUtf8Info(var8), this.info, var6 + 6);
         }
      }

   }

   String renameEntry(String var1, String var2, String var3) {
      return Descriptor.rename(var1, var2, var3);
   }

   void renameClass(Map var1) {
      ConstPool var2 = this.getConstPool();
      int var3 = this.tableLength();

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var4 * 10 + 2;
         int var6 = ByteArray.readU16bit(this.info, var5 + 6);
         if (var6 != 0) {
            String var7 = var2.getUtf8Info(var6);
            var7 = this.renameEntry(var7, var1);
            ByteArray.write16bit(var2.addUtf8Info(var7), this.info, var5 + 6);
         }
      }

   }

   String renameEntry(String var1, Map var2) {
      return Descriptor.rename(var1, var2);
   }

   public void shiftIndex(int var1, int var2) {
      int var3 = this.info.length;

      for(int var4 = 2; var4 < var3; var4 += 10) {
         int var5 = ByteArray.readU16bit(this.info, var4 + 8);
         if (var5 >= var1) {
            ByteArray.write16bit(var5 + var2, this.info, var4 + 8);
         }
      }

   }

   public int tableLength() {
      return ByteArray.readU16bit(this.info, 0);
   }

   public int startPc(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 10 + 2);
   }

   public int codeLength(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 10 + 4);
   }

   void shiftPc(int var1, int var2, boolean var3) {
      int var4 = this.tableLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var5 * 10 + 2;
         int var7 = ByteArray.readU16bit(this.info, var6);
         int var8 = ByteArray.readU16bit(this.info, var6 + 2);
         if (var7 <= var1 && (!var3 || var7 != var1 || var7 == 0)) {
            if (var7 + var8 > var1 || var3 && var7 + var8 == var1) {
               ByteArray.write16bit(var8 + var2, this.info, var6 + 2);
            }
         } else {
            ByteArray.write16bit(var7 + var2, this.info, var6);
         }
      }

   }

   public int nameIndex(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 10 + 6);
   }

   public String variableName(int var1) {
      return this.getConstPool().getUtf8Info(this.nameIndex(var1));
   }

   public int descriptorIndex(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 10 + 8);
   }

   public int signatureIndex(int var1) {
      return this.descriptorIndex(var1);
   }

   public String descriptor(int var1) {
      return this.getConstPool().getUtf8Info(this.descriptorIndex(var1));
   }

   public String signature(int var1) {
      return this.descriptor(var1);
   }

   public int index(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 10 + 10);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      byte[] var3 = this.get();
      byte[] var4 = new byte[var3.length];
      ConstPool var5 = this.getConstPool();
      LocalVariableAttribute var6 = this.makeThisAttr(var1, var4);
      int var7 = ByteArray.readU16bit(var3, 0);
      ByteArray.write16bit(var7, var4, 0);
      int var8 = 2;

      for(int var9 = 0; var9 < var7; ++var9) {
         int var10 = ByteArray.readU16bit(var3, var8);
         int var11 = ByteArray.readU16bit(var3, var8 + 2);
         int var12 = ByteArray.readU16bit(var3, var8 + 4);
         int var13 = ByteArray.readU16bit(var3, var8 + 6);
         int var14 = ByteArray.readU16bit(var3, var8 + 8);
         ByteArray.write16bit(var10, var4, var8);
         ByteArray.write16bit(var11, var4, var8 + 2);
         if (var12 != 0) {
            var12 = var5.copy(var12, var1, (Map)null);
         }

         ByteArray.write16bit(var12, var4, var8 + 4);
         if (var13 != 0) {
            String var15 = var5.getUtf8Info(var13);
            var15 = Descriptor.rename(var15, var2);
            var13 = var1.addUtf8Info(var15);
         }

         ByteArray.write16bit(var13, var4, var8 + 6);
         ByteArray.write16bit(var14, var4, var8 + 8);
         var8 += 10;
      }

      return var6;
   }

   LocalVariableAttribute makeThisAttr(ConstPool var1, byte[] var2) {
      return new LocalVariableAttribute(var1, "LocalVariableTable", var2);
   }
}
