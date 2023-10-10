package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class LineNumberAttribute extends AttributeInfo {
   public static final String tag = "LineNumberTable";

   LineNumberAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   private LineNumberAttribute(ConstPool var1, byte[] var2) {
      super(var1, "LineNumberTable", var2);
   }

   public int tableLength() {
      return ByteArray.readU16bit(this.info, 0);
   }

   public int startPc(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 4 + 2);
   }

   public int lineNumber(int var1) {
      return ByteArray.readU16bit(this.info, var1 * 4 + 4);
   }

   public int toLineNumber(int var1) {
      int var2 = this.tableLength();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         if (var1 < this.startPc(var3)) {
            if (var3 == 0) {
               return this.lineNumber(0);
            }
            break;
         }
      }

      return this.lineNumber(var3 - 1);
   }

   public int toStartPc(int var1) {
      int var2 = this.tableLength();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1 == this.lineNumber(var3)) {
            return this.startPc(var3);
         }
      }

      return -1;
   }

   public LineNumberAttribute.Pc toNearPc(int var1) {
      int var2 = this.tableLength();
      int var3 = 0;
      int var4 = 0;
      if (var2 > 0) {
         var4 = this.lineNumber(0) - var1;
         var3 = this.startPc(0);
      }

      for(int var5 = 1; var5 < var2; ++var5) {
         int var6 = this.lineNumber(var5) - var1;
         if (var6 < 0 && var6 > var4 || var6 >= 0 && (var6 < var4 || var4 < 0)) {
            var4 = var6;
            var3 = this.startPc(var5);
         }
      }

      LineNumberAttribute.Pc var7 = new LineNumberAttribute.Pc();
      var7.index = var3;
      var7.line = var1 + var4;
      return var7;
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      byte[] var3 = this.info;
      int var4 = var3.length;
      byte[] var5 = new byte[var4];

      for(int var6 = 0; var6 < var4; ++var6) {
         var5[var6] = var3[var6];
      }

      LineNumberAttribute var7 = new LineNumberAttribute(var1, var5);
      return var7;
   }

   void shiftPc(int var1, int var2, boolean var3) {
      int var4 = this.tableLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var5 * 4 + 2;
         int var7 = ByteArray.readU16bit(this.info, var6);
         if (var7 > var1 || var3 && var7 == var1) {
            ByteArray.write16bit(var7 + var2, this.info, var6);
         }
      }

   }

   public static class Pc {
      public int index;
      public int line;
   }
}
