package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javassist.CannotCompileException;

public class StackMap extends AttributeInfo {
   public static final String tag = "StackMap";
   public static final int TOP = 0;
   public static final int INTEGER = 1;
   public static final int FLOAT = 2;
   public static final int DOUBLE = 3;
   public static final int LONG = 4;
   public static final int NULL = 5;
   public static final int THIS = 6;
   public static final int OBJECT = 7;
   public static final int UNINIT = 8;

   StackMap(ConstPool var1, byte[] var2) {
      super(var1, "StackMap", var2);
   }

   StackMap(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public int numOfEntries() {
      return ByteArray.readU16bit(this.info, 0);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      StackMap.Copier var3 = new StackMap.Copier(this, var1, var2);
      var3.visit();
      return var3.getStackMap();
   }

   public void insertLocal(int var1, int var2, int var3) throws BadBytecode {
      byte[] var4 = (new StackMap.InsertLocal(this, var1, var2, var3)).doit();
      this.set(var4);
   }

   void shiftPc(int var1, int var2, boolean var3) throws BadBytecode {
      (new StackMap.Shifter(this, var1, var2, var3)).visit();
   }

   void shiftForSwitch(int var1, int var2) throws BadBytecode {
      (new StackMap.SwitchShifter(this, var1, var2)).visit();
   }

   public void removeNew(int var1) throws CannotCompileException {
      byte[] var2 = (new StackMap.NewRemover(this, var1)).doit();
      this.set(var2);
   }

   public void print(PrintWriter var1) {
      (new StackMap.Printer(this, var1)).print();
   }

   public static class Writer {
      private ByteArrayOutputStream output = new ByteArrayOutputStream();

      public byte[] toByteArray() {
         return this.output.toByteArray();
      }

      public StackMap toStackMap(ConstPool var1) {
         return new StackMap(var1, this.output.toByteArray());
      }

      public void writeVerifyTypeInfo(int var1, int var2) {
         this.output.write(var1);
         if (var1 == 7 || var1 == 8) {
            this.write16bit(var2);
         }

      }

      public void write16bit(int var1) {
         this.output.write(var1 >>> 8 & 255);
         this.output.write(var1 & 255);
      }
   }

   static class Printer extends StackMap.Walker {
      private PrintWriter writer;

      public Printer(StackMap var1, PrintWriter var2) {
         super(var1);
         this.writer = var2;
      }

      public void print() {
         int var1 = ByteArray.readU16bit(this.info, 0);
         this.writer.println(var1 + " entries");
         this.visit();
      }

      public int locals(int var1, int var2, int var3) {
         this.writer.println("  * offset " + var2);
         return super.locals(var1, var2, var3);
      }
   }

   static class NewRemover extends StackMap.SimpleCopy {
      int posOfNew;

      NewRemover(StackMap var1, int var2) {
         super(var1);
         this.posOfNew = var2;
      }

      public int stack(int var1, int var2, int var3) {
         return this.stackTypeInfoArray(var1, var2, var3);
      }

      private int stackTypeInfoArray(int var1, int var2, int var3) {
         int var4 = var1;
         int var5 = 0;

         int var6;
         byte var7;
         int var8;
         for(var6 = 0; var6 < var3; ++var6) {
            var7 = this.info[var4];
            if (var7 == 7) {
               var4 += 3;
            } else if (var7 == 8) {
               var8 = ByteArray.readU16bit(this.info, var4 + 1);
               if (var8 == this.posOfNew) {
                  ++var5;
               }

               var4 += 3;
            } else {
               ++var4;
            }
         }

         this.writer.write16bit(var3 - var5);

         for(var6 = 0; var6 < var3; ++var6) {
            var7 = this.info[var1];
            if (var7 == 7) {
               var8 = ByteArray.readU16bit(this.info, var1 + 1);
               this.objectVariable(var1, var8);
               var1 += 3;
            } else if (var7 == 8) {
               var8 = ByteArray.readU16bit(this.info, var1 + 1);
               if (var8 != this.posOfNew) {
                  this.uninitialized(var1, var8);
               }

               var1 += 3;
            } else {
               this.typeInfo(var1, var7);
               ++var1;
            }
         }

         return var1;
      }
   }

   static class SwitchShifter extends StackMap.Walker {
      private int where;
      private int gap;

      public SwitchShifter(StackMap var1, int var2, int var3) {
         super(var1);
         this.where = var2;
         this.gap = var3;
      }

      public int locals(int var1, int var2, int var3) {
         if (this.where == var1 + var2) {
            ByteArray.write16bit(var2 - this.gap, this.info, var1 - 4);
         } else if (this.where == var1) {
            ByteArray.write16bit(var2 + this.gap, this.info, var1 - 4);
         }

         return super.locals(var1, var2, var3);
      }
   }

   static class Shifter extends StackMap.Walker {
      private int where;
      private int gap;
      private boolean exclusive;

      public Shifter(StackMap var1, int var2, int var3, boolean var4) {
         super(var1);
         this.where = var2;
         this.gap = var3;
         this.exclusive = var4;
      }

      public int locals(int var1, int var2, int var3) {
         if (this.exclusive) {
            if (this.where > var2) {
               return super.locals(var1, var2, var3);
            }
         } else if (this.where >= var2) {
            return super.locals(var1, var2, var3);
         }

         ByteArray.write16bit(var2 + this.gap, this.info, var1 - 4);
         return super.locals(var1, var2, var3);
      }

      public void uninitialized(int var1, int var2) {
         if (this.where <= var2) {
            ByteArray.write16bit(var2 + this.gap, this.info, var1 + 1);
         }

      }
   }

   static class InsertLocal extends StackMap.SimpleCopy {
      private int varIndex;
      private int varTag;
      private int varData;

      InsertLocal(StackMap var1, int var2, int var3, int var4) {
         super(var1);
         this.varIndex = var2;
         this.varTag = var3;
         this.varData = var4;
      }

      public int typeInfoArray(int var1, int var2, int var3, boolean var4) {
         if (var4 && var3 >= this.varIndex) {
            this.writer.write16bit(var3 + 1);

            for(int var5 = 0; var5 < var3; ++var5) {
               if (var5 == this.varIndex) {
                  this.writeVarTypeInfo();
               }

               var1 = this.typeInfoArray2(var5, var1);
            }

            if (var3 == this.varIndex) {
               this.writeVarTypeInfo();
            }

            return var1;
         } else {
            return super.typeInfoArray(var1, var2, var3, var4);
         }
      }

      private void writeVarTypeInfo() {
         if (this.varTag == 7) {
            this.writer.writeVerifyTypeInfo(7, this.varData);
         } else if (this.varTag == 8) {
            this.writer.writeVerifyTypeInfo(8, this.varData);
         } else {
            this.writer.writeVerifyTypeInfo(this.varTag, 0);
         }

      }
   }

   static class SimpleCopy extends StackMap.Walker {
      StackMap.Writer writer = new StackMap.Writer();

      SimpleCopy(StackMap var1) {
         super(var1);
      }

      byte[] doit() {
         this.visit();
         return this.writer.toByteArray();
      }

      public void visit() {
         int var1 = ByteArray.readU16bit(this.info, 0);
         this.writer.write16bit(var1);
         super.visit();
      }

      public int locals(int var1, int var2, int var3) {
         this.writer.write16bit(var2);
         return super.locals(var1, var2, var3);
      }

      public int typeInfoArray(int var1, int var2, int var3, boolean var4) {
         this.writer.write16bit(var3);
         return super.typeInfoArray(var1, var2, var3, var4);
      }

      public void typeInfo(int var1, byte var2) {
         this.writer.writeVerifyTypeInfo(var2, 0);
      }

      public void objectVariable(int var1, int var2) {
         this.writer.writeVerifyTypeInfo(7, var2);
      }

      public void uninitialized(int var1, int var2) {
         this.writer.writeVerifyTypeInfo(8, var2);
      }
   }

   static class Copier extends StackMap.Walker {
      byte[] dest;
      ConstPool srcCp;
      ConstPool destCp;
      Map classnames;

      Copier(StackMap var1, ConstPool var2, Map var3) {
         super(var1);
         this.srcCp = var1.getConstPool();
         this.dest = new byte[this.info.length];
         this.destCp = var2;
         this.classnames = var3;
      }

      public void visit() {
         int var1 = ByteArray.readU16bit(this.info, 0);
         ByteArray.write16bit(var1, this.dest, 0);
         super.visit();
      }

      public int locals(int var1, int var2, int var3) {
         ByteArray.write16bit(var2, this.dest, var1 - 4);
         return super.locals(var1, var2, var3);
      }

      public int typeInfoArray(int var1, int var2, int var3, boolean var4) {
         ByteArray.write16bit(var3, this.dest, var1 - 2);
         return super.typeInfoArray(var1, var2, var3, var4);
      }

      public void typeInfo(int var1, byte var2) {
         this.dest[var1] = var2;
      }

      public void objectVariable(int var1, int var2) {
         this.dest[var1] = 7;
         int var3 = this.srcCp.copy(var2, this.destCp, this.classnames);
         ByteArray.write16bit(var3, this.dest, var1 + 1);
      }

      public void uninitialized(int var1, int var2) {
         this.dest[var1] = 8;
         ByteArray.write16bit(var2, this.dest, var1 + 1);
      }

      public StackMap getStackMap() {
         return new StackMap(this.destCp, this.dest);
      }
   }

   public static class Walker {
      byte[] info;

      public Walker(StackMap var1) {
         this.info = var1.get();
      }

      public void visit() {
         int var1 = ByteArray.readU16bit(this.info, 0);
         int var2 = 2;

         for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = ByteArray.readU16bit(this.info, var2);
            int var5 = ByteArray.readU16bit(this.info, var2 + 2);
            var2 = this.locals(var2 + 4, var4, var5);
            int var6 = ByteArray.readU16bit(this.info, var2);
            var2 = this.stack(var2 + 2, var4, var6);
         }

      }

      public int locals(int var1, int var2, int var3) {
         return this.typeInfoArray(var1, var2, var3, true);
      }

      public int stack(int var1, int var2, int var3) {
         return this.typeInfoArray(var1, var2, var3, false);
      }

      public int typeInfoArray(int var1, int var2, int var3, boolean var4) {
         for(int var5 = 0; var5 < var3; ++var5) {
            var1 = this.typeInfoArray2(var5, var1);
         }

         return var1;
      }

      int typeInfoArray2(int var1, int var2) {
         byte var3 = this.info[var2];
         int var4;
         if (var3 == 7) {
            var4 = ByteArray.readU16bit(this.info, var2 + 1);
            this.objectVariable(var2, var4);
            var2 += 3;
         } else if (var3 == 8) {
            var4 = ByteArray.readU16bit(this.info, var2 + 1);
            this.uninitialized(var2, var4);
            var2 += 3;
         } else {
            this.typeInfo(var2, var3);
            ++var2;
         }

         return var2;
      }

      public void typeInfo(int var1, byte var2) {
      }

      public void objectVariable(int var1, int var2) {
      }

      public void uninitialized(int var1, int var2) {
      }
   }
}
