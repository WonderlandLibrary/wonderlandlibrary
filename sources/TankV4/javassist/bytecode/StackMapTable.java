package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import javassist.CannotCompileException;

public class StackMapTable extends AttributeInfo {
   public static final String tag = "StackMapTable";
   public static final int TOP = 0;
   public static final int INTEGER = 1;
   public static final int FLOAT = 2;
   public static final int DOUBLE = 3;
   public static final int LONG = 4;
   public static final int NULL = 5;
   public static final int THIS = 6;
   public static final int OBJECT = 7;
   public static final int UNINIT = 8;

   StackMapTable(ConstPool var1, byte[] var2) {
      super(var1, "StackMapTable", var2);
   }

   StackMapTable(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) throws StackMapTable.RuntimeCopyException {
      try {
         return new StackMapTable(var1, (new StackMapTable.Copier(this.constPool, this.info, var1, var2)).doit());
      } catch (BadBytecode var4) {
         throw new StackMapTable.RuntimeCopyException("bad bytecode. fatal?");
      }
   }

   void write(DataOutputStream var1) throws IOException {
      super.write(var1);
   }

   public void insertLocal(int var1, int var2, int var3) throws BadBytecode {
      byte[] var4 = (new StackMapTable.InsertLocal(this.get(), var1, var2, var3)).doit();
      this.set(var4);
   }

   public static int typeTagOf(char var0) {
      switch(var0) {
      case 'D':
         return 3;
      case 'F':
         return 2;
      case 'J':
         return 4;
      case 'L':
      case '[':
         return 7;
      default:
         return 1;
      }
   }

   public void println(PrintWriter var1) {
      StackMapTable.Printer.print(this, var1);
   }

   public void println(PrintStream var1) {
      StackMapTable.Printer.print(this, new PrintWriter(var1, true));
   }

   void shiftPc(int var1, int var2, boolean var3) throws BadBytecode {
      (new StackMapTable.OffsetShifter(this, var1, var2)).parse();
      (new StackMapTable.Shifter(this, var1, var2, var3)).doit();
   }

   void shiftForSwitch(int var1, int var2) throws BadBytecode {
      (new StackMapTable.SwitchShifter(this, var1, var2)).doit();
   }

   public void removeNew(int var1) throws CannotCompileException {
      try {
         byte[] var2 = (new StackMapTable.NewRemover(this.get(), var1)).doit();
         this.set(var2);
      } catch (BadBytecode var3) {
         throw new CannotCompileException("bad stack map table", var3);
      }
   }

   static class NewRemover extends StackMapTable.SimpleCopy {
      int posOfNew;

      public NewRemover(byte[] var1, int var2) {
         super(var1);
         this.posOfNew = var2;
      }

      public void sameLocals(int var1, int var2, int var3, int var4) {
         if (var3 == 8 && var4 == this.posOfNew) {
            super.sameFrame(var1, var2);
         } else {
            super.sameLocals(var1, var2, var3, var4);
         }

      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) {
         int var7 = var5.length - 1;

         for(int var8 = 0; var8 < var7; ++var8) {
            if (var5[var8] == 8 && var6[var8] == this.posOfNew && var5[var8 + 1] == 8 && var6[var8 + 1] == this.posOfNew) {
               ++var7;
               int[] var9 = new int[var7 - 2];
               int[] var10 = new int[var7 - 2];
               int var11 = 0;

               for(int var12 = 0; var12 < var7; ++var12) {
                  if (var12 == var8) {
                     ++var12;
                  } else {
                     var9[var11] = var5[var12];
                     var10[var11++] = var6[var12];
                  }
               }

               var5 = var9;
               var6 = var10;
               break;
            }
         }

         super.fullFrame(var1, var2, var3, var4, var5, var6);
      }
   }

   static class SwitchShifter extends StackMapTable.Shifter {
      SwitchShifter(StackMapTable var1, int var2, int var3) {
         super(var1, var2, var3, false);
      }

      void update(int var1, int var2, int var3, int var4) {
         int var5 = this.position;
         this.position = var5 + var2 + (var5 == 0 ? 0 : 1);
         int var6;
         if (this.where == this.position) {
            var6 = var2 - this.gap;
         } else {
            if (this.where != var5) {
               return;
            }

            var6 = var2 + this.gap;
         }

         byte[] var7;
         if (var2 < 64) {
            if (var6 < 64) {
               this.info[var1] = (byte)(var6 + var3);
            } else {
               var7 = insertGap(this.info, var1, 2);
               var7[var1] = (byte)var4;
               ByteArray.write16bit(var6, var7, var1 + 1);
               this.updatedInfo = var7;
            }
         } else if (var6 < 64) {
            var7 = deleteGap(this.info, var1, 2);
            var7[var1] = (byte)(var6 + var3);
            this.updatedInfo = var7;
         } else {
            ByteArray.write16bit(var6, this.info, var1 + 1);
         }

      }

      static byte[] deleteGap(byte[] var0, int var1, int var2) {
         var1 += var2;
         int var3 = var0.length;
         byte[] var4 = new byte[var3 - var2];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5 - (var5 < var1 ? 0 : var2)] = var0[var5];
         }

         return var4;
      }

      void update(int var1, int var2) {
         int var3 = this.position;
         this.position = var3 + var2 + (var3 == 0 ? 0 : 1);
         int var4;
         if (this.where == this.position) {
            var4 = var2 - this.gap;
         } else {
            if (this.where != var3) {
               return;
            }

            var4 = var2 + this.gap;
         }

         ByteArray.write16bit(var4, this.info, var1 + 1);
      }
   }

   static class Shifter extends StackMapTable.Walker {
      private StackMapTable stackMap;
      int where;
      int gap;
      int position;
      byte[] updatedInfo;
      boolean exclusive;

      public Shifter(StackMapTable var1, int var2, int var3, boolean var4) {
         super(var1);
         this.stackMap = var1;
         this.where = var2;
         this.gap = var3;
         this.position = 0;
         this.updatedInfo = null;
         this.exclusive = var4;
      }

      public void doit() throws BadBytecode {
         this.parse();
         if (this.updatedInfo != null) {
            this.stackMap.set(this.updatedInfo);
         }

      }

      public void sameFrame(int var1, int var2) {
         this.update(var1, var2, 0, 251);
      }

      public void sameLocals(int var1, int var2, int var3, int var4) {
         this.update(var1, var2, 64, 247);
      }

      void update(int var1, int var2, int var3, int var4) {
         int var5 = this.position;
         this.position = var5 + var2 + (var5 == 0 ? 0 : 1);
         boolean var6;
         if (this.exclusive) {
            var6 = var5 < this.where && this.where <= this.position;
         } else {
            var6 = var5 <= this.where && this.where < this.position;
         }

         if (var6) {
            int var7 = var2 + this.gap;
            this.position += this.gap;
            if (var7 < 64) {
               this.info[var1] = (byte)(var7 + var3);
            } else if (var2 < 64) {
               byte[] var8 = insertGap(this.info, var1, 2);
               var8[var1] = (byte)var4;
               ByteArray.write16bit(var7, var8, var1 + 1);
               this.updatedInfo = var8;
            } else {
               ByteArray.write16bit(var7, this.info, var1 + 1);
            }
         }

      }

      static byte[] insertGap(byte[] var0, int var1, int var2) {
         int var3 = var0.length;
         byte[] var4 = new byte[var3 + var2];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5 + (var5 < var1 ? 0 : var2)] = var0[var5];
         }

         return var4;
      }

      public void chopFrame(int var1, int var2, int var3) {
         this.update(var1, var2);
      }

      public void appendFrame(int var1, int var2, int[] var3, int[] var4) {
         this.update(var1, var2);
      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) {
         this.update(var1, var2);
      }

      void update(int var1, int var2) {
         int var3 = this.position;
         this.position = var3 + var2 + (var3 == 0 ? 0 : 1);
         boolean var4;
         if (this.exclusive) {
            var4 = var3 < this.where && this.where <= this.position;
         } else {
            var4 = var3 <= this.where && this.where < this.position;
         }

         if (var4) {
            int var5 = var2 + this.gap;
            ByteArray.write16bit(var5, this.info, var1 + 1);
            this.position += this.gap;
         }

      }
   }

   static class OffsetShifter extends StackMapTable.Walker {
      int where;
      int gap;

      public OffsetShifter(StackMapTable var1, int var2, int var3) {
         super(var1);
         this.where = var2;
         this.gap = var3;
      }

      public void objectOrUninitialized(int var1, int var2, int var3) {
         if (var1 == 8 && this.where <= var2) {
            ByteArray.write16bit(var2 + this.gap, this.info, var3);
         }

      }
   }

   static class Printer extends StackMapTable.Walker {
      private PrintWriter writer;
      private int offset;

      public static void print(StackMapTable var0, PrintWriter var1) {
         try {
            (new StackMapTable.Printer(var0.get(), var1)).parse();
         } catch (BadBytecode var3) {
            var1.println(var3.getMessage());
         }

      }

      Printer(byte[] var1, PrintWriter var2) {
         super(var1);
         this.writer = var2;
         this.offset = -1;
      }

      public void sameFrame(int var1, int var2) {
         this.offset += var2 + 1;
         this.writer.println(this.offset + " same frame: " + var2);
      }

      public void sameLocals(int var1, int var2, int var3, int var4) {
         this.offset += var2 + 1;
         this.writer.println(this.offset + " same locals: " + var2);
         this.printTypeInfo(var3, var4);
      }

      public void chopFrame(int var1, int var2, int var3) {
         this.offset += var2 + 1;
         this.writer.println(this.offset + " chop frame: " + var2 + ",    " + var3 + " last locals");
      }

      public void appendFrame(int var1, int var2, int[] var3, int[] var4) {
         this.offset += var2 + 1;
         this.writer.println(this.offset + " append frame: " + var2);

         for(int var5 = 0; var5 < var3.length; ++var5) {
            this.printTypeInfo(var3[var5], var4[var5]);
         }

      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) {
         this.offset += var2 + 1;
         this.writer.println(this.offset + " full frame: " + var2);
         this.writer.println("[locals]");

         int var7;
         for(var7 = 0; var7 < var3.length; ++var7) {
            this.printTypeInfo(var3[var7], var4[var7]);
         }

         this.writer.println("[stack]");

         for(var7 = 0; var7 < var5.length; ++var7) {
            this.printTypeInfo(var5[var7], var6[var7]);
         }

      }

      private void printTypeInfo(int var1, int var2) {
         String var3 = null;
         switch(var1) {
         case 0:
            var3 = "top";
            break;
         case 1:
            var3 = "integer";
            break;
         case 2:
            var3 = "float";
            break;
         case 3:
            var3 = "double";
            break;
         case 4:
            var3 = "long";
            break;
         case 5:
            var3 = "null";
            break;
         case 6:
            var3 = "this";
            break;
         case 7:
            var3 = "object (cpool_index " + var2 + ")";
            break;
         case 8:
            var3 = "uninitialized (offset " + var2 + ")";
         }

         this.writer.print("    ");
         this.writer.println(var3);
      }
   }

   public static class Writer {
      ByteArrayOutputStream output;
      int numOfEntries;

      public Writer(int var1) {
         this.output = new ByteArrayOutputStream(var1);
         this.numOfEntries = 0;
         this.output.write(0);
         this.output.write(0);
      }

      public byte[] toByteArray() {
         byte[] var1 = this.output.toByteArray();
         ByteArray.write16bit(this.numOfEntries, var1, 0);
         return var1;
      }

      public StackMapTable toStackMapTable(ConstPool var1) {
         return new StackMapTable(var1, this.toByteArray());
      }

      public void sameFrame(int var1) {
         ++this.numOfEntries;
         if (var1 < 64) {
            this.output.write(var1);
         } else {
            this.output.write(251);
            this.write16(var1);
         }

      }

      public void sameLocals(int var1, int var2, int var3) {
         ++this.numOfEntries;
         if (var1 < 64) {
            this.output.write(var1 + 64);
         } else {
            this.output.write(247);
            this.write16(var1);
         }

         this.writeTypeInfo(var2, var3);
      }

      public void chopFrame(int var1, int var2) {
         ++this.numOfEntries;
         this.output.write(251 - var2);
         this.write16(var1);
      }

      public void appendFrame(int var1, int[] var2, int[] var3) {
         ++this.numOfEntries;
         int var4 = var2.length;
         this.output.write(var4 + 251);
         this.write16(var1);

         for(int var5 = 0; var5 < var4; ++var5) {
            this.writeTypeInfo(var2[var5], var3[var5]);
         }

      }

      public void fullFrame(int var1, int[] var2, int[] var3, int[] var4, int[] var5) {
         ++this.numOfEntries;
         this.output.write(255);
         this.write16(var1);
         int var6 = var2.length;
         this.write16(var6);

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            this.writeTypeInfo(var2[var7], var3[var7]);
         }

         var6 = var4.length;
         this.write16(var6);

         for(var7 = 0; var7 < var6; ++var7) {
            this.writeTypeInfo(var4[var7], var5[var7]);
         }

      }

      private void writeTypeInfo(int var1, int var2) {
         this.output.write(var1);
         if (var1 == 7 || var1 == 8) {
            this.write16(var2);
         }

      }

      private void write16(int var1) {
         this.output.write(var1 >>> 8 & 255);
         this.output.write(var1 & 255);
      }
   }

   static class InsertLocal extends StackMapTable.SimpleCopy {
      private int varIndex;
      private int varTag;
      private int varData;

      public InsertLocal(byte[] var1, int var2, int var3, int var4) {
         super(var1);
         this.varIndex = var2;
         this.varTag = var3;
         this.varData = var4;
      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) {
         int var7 = var3.length;
         if (var7 < this.varIndex) {
            super.fullFrame(var1, var2, var3, var4, var5, var6);
         } else {
            int var8 = this.varTag != 4 && this.varTag != 3 ? 1 : 2;
            int[] var9 = new int[var7 + var8];
            int[] var10 = new int[var7 + var8];
            int var11 = this.varIndex;
            int var12 = 0;

            for(int var13 = 0; var13 < var7; ++var13) {
               if (var12 == var11) {
                  var12 += var8;
               }

               var9[var12] = var3[var13];
               var10[var12++] = var4[var13];
            }

            var9[var11] = this.varTag;
            var10[var11] = this.varData;
            if (var8 > 1) {
               var9[var11 + 1] = 0;
               var10[var11 + 1] = 0;
            }

            super.fullFrame(var1, var2, var9, var10, var5, var6);
         }
      }
   }

   static class Copier extends StackMapTable.SimpleCopy {
      private ConstPool srcPool;
      private ConstPool destPool;
      private Map classnames;

      public Copier(ConstPool var1, byte[] var2, ConstPool var3, Map var4) {
         super(var2);
         this.srcPool = var1;
         this.destPool = var3;
         this.classnames = var4;
      }

      protected int copyData(int var1, int var2) {
         return var1 == 7 ? this.srcPool.copy(var2, this.destPool, this.classnames) : var2;
      }

      protected int[] copyData(int[] var1, int[] var2) {
         int[] var3 = new int[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var1[var4] == 7) {
               var3[var4] = this.srcPool.copy(var2[var4], this.destPool, this.classnames);
            } else {
               var3[var4] = var2[var4];
            }
         }

         return var3;
      }
   }

   static class SimpleCopy extends StackMapTable.Walker {
      private StackMapTable.Writer writer;

      public SimpleCopy(byte[] var1) {
         super(var1);
         this.writer = new StackMapTable.Writer(var1.length);
      }

      public byte[] doit() throws BadBytecode {
         this.parse();
         return this.writer.toByteArray();
      }

      public void sameFrame(int var1, int var2) {
         this.writer.sameFrame(var2);
      }

      public void sameLocals(int var1, int var2, int var3, int var4) {
         this.writer.sameLocals(var2, var3, this.copyData(var3, var4));
      }

      public void chopFrame(int var1, int var2, int var3) {
         this.writer.chopFrame(var2, var3);
      }

      public void appendFrame(int var1, int var2, int[] var3, int[] var4) {
         this.writer.appendFrame(var2, var3, this.copyData(var3, var4));
      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) {
         this.writer.fullFrame(var2, var3, this.copyData(var3, var4), var5, this.copyData(var5, var6));
      }

      protected int copyData(int var1, int var2) {
         return var2;
      }

      protected int[] copyData(int[] var1, int[] var2) {
         return var2;
      }
   }

   public static class Walker {
      byte[] info;
      int numOfEntries;

      public Walker(StackMapTable var1) {
         this(var1.get());
      }

      public Walker(byte[] var1) {
         this.info = var1;
         this.numOfEntries = ByteArray.readU16bit(var1, 0);
      }

      public final int size() {
         return this.numOfEntries;
      }

      public void parse() throws BadBytecode {
         int var1 = this.numOfEntries;
         int var2 = 2;

         for(int var3 = 0; var3 < var1; ++var3) {
            var2 = this.stackMapFrames(var2, var3);
         }

      }

      int stackMapFrames(int var1, int var2) throws BadBytecode {
         int var3 = this.info[var1] & 255;
         if (var3 < 64) {
            this.sameFrame(var1, var3);
            ++var1;
         } else if (var3 < 128) {
            var1 = this.sameLocals(var1, var3);
         } else {
            if (var3 < 247) {
               throw new BadBytecode("bad frame_type in StackMapTable");
            }

            if (var3 == 247) {
               var1 = this.sameLocals(var1, var3);
            } else {
               int var4;
               if (var3 < 251) {
                  var4 = ByteArray.readU16bit(this.info, var1 + 1);
                  this.chopFrame(var1, var4, 251 - var3);
                  var1 += 3;
               } else if (var3 == 251) {
                  var4 = ByteArray.readU16bit(this.info, var1 + 1);
                  this.sameFrame(var1, var4);
                  var1 += 3;
               } else if (var3 < 255) {
                  var1 = this.appendFrame(var1, var3);
               } else {
                  var1 = this.fullFrame(var1);
               }
            }
         }

         return var1;
      }

      public void sameFrame(int var1, int var2) throws BadBytecode {
      }

      private int sameLocals(int var1, int var2) throws BadBytecode {
         int var3 = var1;
         int var4;
         if (var2 < 128) {
            var4 = var2 - 64;
         } else {
            var4 = ByteArray.readU16bit(this.info, var1 + 1);
            var1 += 2;
         }

         int var5 = this.info[var1 + 1] & 255;
         int var6 = 0;
         if (var5 == 7 || var5 == 8) {
            var6 = ByteArray.readU16bit(this.info, var1 + 2);
            this.objectOrUninitialized(var5, var6, var1 + 2);
            var1 += 2;
         }

         this.sameLocals(var3, var4, var5, var6);
         return var1 + 2;
      }

      public void sameLocals(int var1, int var2, int var3, int var4) throws BadBytecode {
      }

      public void chopFrame(int var1, int var2, int var3) throws BadBytecode {
      }

      private int appendFrame(int var1, int var2) throws BadBytecode {
         int var3 = var2 - 251;
         int var4 = ByteArray.readU16bit(this.info, var1 + 1);
         int[] var5 = new int[var3];
         int[] var6 = new int[var3];
         int var7 = var1 + 3;

         for(int var8 = 0; var8 < var3; ++var8) {
            int var9 = this.info[var7] & 255;
            var5[var8] = var9;
            if (var9 != 7 && var9 != 8) {
               var6[var8] = 0;
               ++var7;
            } else {
               var6[var8] = ByteArray.readU16bit(this.info, var7 + 1);
               this.objectOrUninitialized(var9, var6[var8], var7 + 1);
               var7 += 3;
            }
         }

         this.appendFrame(var1, var4, var5, var6);
         return var7;
      }

      public void appendFrame(int var1, int var2, int[] var3, int[] var4) throws BadBytecode {
      }

      private int fullFrame(int var1) throws BadBytecode {
         int var2 = ByteArray.readU16bit(this.info, var1 + 1);
         int var3 = ByteArray.readU16bit(this.info, var1 + 3);
         int[] var4 = new int[var3];
         int[] var5 = new int[var3];
         int var6 = this.verifyTypeInfo(var1 + 5, var3, var4, var5);
         int var7 = ByteArray.readU16bit(this.info, var6);
         int[] var8 = new int[var7];
         int[] var9 = new int[var7];
         var6 = this.verifyTypeInfo(var6 + 2, var7, var8, var9);
         this.fullFrame(var1, var2, var4, var5, var8, var9);
         return var6;
      }

      public void fullFrame(int var1, int var2, int[] var3, int[] var4, int[] var5, int[] var6) throws BadBytecode {
      }

      private int verifyTypeInfo(int var1, int var2, int[] var3, int[] var4) {
         for(int var5 = 0; var5 < var2; ++var5) {
            int var6 = this.info[var1++] & 255;
            var3[var5] = var6;
            if (var6 == 7 || var6 == 8) {
               var4[var5] = ByteArray.readU16bit(this.info, var1);
               this.objectOrUninitialized(var6, var4[var5], var1);
               var1 += 2;
            }
         }

         return var1;
      }

      public void objectOrUninitialized(int var1, int var2, int var3) {
      }
   }

   public static class RuntimeCopyException extends RuntimeException {
      public RuntimeCopyException(String var1) {
         super(var1);
      }
   }
}
