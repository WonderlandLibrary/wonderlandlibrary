package javassist.bytecode;

import java.util.ArrayList;

public class CodeIterator implements Opcode {
   protected CodeAttribute codeAttr;
   protected byte[] bytecode;
   protected int endPos;
   protected int currentPos;
   protected int mark;
   private static final int[] opcodeLength = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5};

   protected CodeIterator(CodeAttribute var1) {
      this.codeAttr = var1;
      this.bytecode = var1.getCode();
      this.begin();
   }

   public void begin() {
      this.currentPos = this.mark = 0;
      this.endPos = this.getCodeLength();
   }

   public void move(int var1) {
      this.currentPos = var1;
   }

   public void setMark(int var1) {
      this.mark = var1;
   }

   public int getMark() {
      return this.mark;
   }

   public CodeAttribute get() {
      return this.codeAttr;
   }

   public int getCodeLength() {
      return this.bytecode.length;
   }

   public int byteAt(int var1) {
      return this.bytecode[var1] & 255;
   }

   public int signedByteAt(int var1) {
      return this.bytecode[var1];
   }

   public void writeByte(int var1, int var2) {
      this.bytecode[var2] = (byte)var1;
   }

   public int u16bitAt(int var1) {
      return ByteArray.readU16bit(this.bytecode, var1);
   }

   public int s16bitAt(int var1) {
      return ByteArray.readS16bit(this.bytecode, var1);
   }

   public void write16bit(int var1, int var2) {
      ByteArray.write16bit(var1, this.bytecode, var2);
   }

   public int s32bitAt(int var1) {
      return ByteArray.read32bit(this.bytecode, var1);
   }

   public void write32bit(int var1, int var2) {
      ByteArray.write32bit(var1, this.bytecode, var2);
   }

   public void write(byte[] var1, int var2) {
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         this.bytecode[var2++] = var1[var4];
      }

   }

   public int next() throws BadBytecode {
      int var1 = this.currentPos;
      this.currentPos = nextOpcode(this.bytecode, var1);
      return var1;
   }

   public int lookAhead() {
      return this.currentPos;
   }

   public int skipConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(-1);
   }

   public int skipSuperConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(0);
   }

   public int skipThisConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(1);
   }

   private int skipSuperConstructor0(int param1) throws BadBytecode {
      // $FF: Couldn't be decompiled
   }

   public int insert(byte[] var1) throws BadBytecode {
      return this.insert0(this.currentPos, var1, false);
   }

   public void insert(int var1, byte[] var2) throws BadBytecode {
      this.insert0(var1, var2, false);
   }

   public int insertAt(int var1, byte[] var2) throws BadBytecode {
      return this.insert0(var1, var2, false);
   }

   public int insertEx(byte[] var1) throws BadBytecode {
      return this.insert0(this.currentPos, var1, true);
   }

   public void insertEx(int var1, byte[] var2) throws BadBytecode {
      this.insert0(var1, var2, true);
   }

   public int insertExAt(int var1, byte[] var2) throws BadBytecode {
      return this.insert0(var1, var2, true);
   }

   private int insert0(int var1, byte[] var2, boolean var3) throws BadBytecode {
      int var4 = var2.length;
      if (var4 <= 0) {
         return var1;
      } else {
         var1 = this.insertGapAt(var1, var4, var3).position;
         int var5 = var1;

         for(int var6 = 0; var6 < var4; ++var6) {
            this.bytecode[var5++] = var2[var6];
         }

         return var1;
      }
   }

   public int insertGap(int var1) throws BadBytecode {
      return this.insertGapAt(this.currentPos, var1, false).position;
   }

   public int insertGap(int var1, int var2) throws BadBytecode {
      return this.insertGapAt(var1, var2, false).length;
   }

   public int insertExGap(int var1) throws BadBytecode {
      return this.insertGapAt(this.currentPos, var1, true).position;
   }

   public int insertExGap(int var1, int var2) throws BadBytecode {
      return this.insertGapAt(var1, var2, true).length;
   }

   public CodeIterator.Gap insertGapAt(int var1, int var2, boolean var3) throws BadBytecode {
      CodeIterator.Gap var4 = new CodeIterator.Gap();
      if (var2 <= 0) {
         var4.position = var1;
         var4.length = 0;
         return var4;
      } else {
         byte[] var5;
         int var6;
         if (this.bytecode.length + var2 > 32767) {
            var5 = this.insertGapCore0w(this.bytecode, var1, var2, var3, this.get().getExceptionTable(), this.codeAttr, var4);
            var1 = var4.position;
            var6 = var2;
         } else {
            int var7 = this.currentPos;
            var5 = insertGapCore0(this.bytecode, var1, var2, var3, this.get().getExceptionTable(), this.codeAttr);
            var6 = var5.length - this.bytecode.length;
            var4.position = var1;
            var4.length = var6;
            if (var7 >= var1) {
               this.currentPos = var7 + var6;
            }

            if (this.mark > var1 || this.mark == var1 && var3) {
               this.mark += var6;
            }
         }

         this.codeAttr.setCode(var5);
         this.bytecode = var5;
         this.endPos = this.getCodeLength();
         this.updateCursors(var1, var6);
         return var4;
      }
   }

   protected void updateCursors(int var1, int var2) {
   }

   public void insert(ExceptionTable var1, int var2) {
      this.codeAttr.getExceptionTable().add(0, var1, var2);
   }

   public int append(byte[] var1) {
      int var2 = this.getCodeLength();
      int var3 = var1.length;
      if (var3 <= 0) {
         return var2;
      } else {
         this.appendGap(var3);
         byte[] var4 = this.bytecode;

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5 + var2] = var1[var5];
         }

         return var2;
      }
   }

   public void appendGap(int var1) {
      byte[] var2 = this.bytecode;
      int var3 = var2.length;
      byte[] var4 = new byte[var3 + var1];

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var2[var5];
      }

      for(var5 = var3; var5 < var3 + var1; ++var5) {
         var4[var5] = 0;
      }

      this.codeAttr.setCode(var4);
      this.bytecode = var4;
      this.endPos = this.getCodeLength();
   }

   public void append(ExceptionTable var1, int var2) {
      ExceptionTable var3 = this.codeAttr.getExceptionTable();
      var3.add(var3.size(), var1, var2);
   }

   static int nextOpcode(byte[] var0, int var1) throws BadBytecode {
      int var2;
      try {
         var2 = var0[var1] & 255;
      } catch (IndexOutOfBoundsException var8) {
         throw new BadBytecode("invalid opcode address");
      }

      try {
         int var3 = opcodeLength[var2];
         if (var3 > 0) {
            return var1 + var3;
         }

         if (var2 == 196) {
            if (var0[var1 + 1] == -124) {
               return var1 + 6;
            }

            return var1 + 4;
         }

         int var4 = (var1 & -4) + 8;
         int var5;
         if (var2 == 171) {
            var5 = ByteArray.read32bit(var0, var4);
            return var4 + var5 * 8 + 4;
         }

         if (var2 == 170) {
            var5 = ByteArray.read32bit(var0, var4);
            int var6 = ByteArray.read32bit(var0, var4 + 4);
            return var4 + (var6 - var5 + 1) * 4 + 8;
         }
      } catch (IndexOutOfBoundsException var7) {
      }

      throw new BadBytecode(var2);
   }

   static byte[] insertGapCore0(byte[] var0, int var1, int var2, boolean var3, ExceptionTable var4, CodeAttribute var5) throws BadBytecode {
      if (var2 <= 0) {
         return var0;
      } else {
         try {
            return insertGapCore1(var0, var1, var2, var3, var4, var5);
         } catch (CodeIterator.AlignmentException var9) {
            try {
               return insertGapCore1(var0, var1, var2 + 3 & -4, var3, var4, var5);
            } catch (CodeIterator.AlignmentException var8) {
               throw new RuntimeException("fatal error?");
            }
         }
      }
   }

   private static byte[] insertGapCore1(byte[] var0, int var1, int var2, boolean var3, ExceptionTable var4, CodeAttribute var5) throws BadBytecode, CodeIterator.AlignmentException {
      int var6 = var0.length;
      byte[] var7 = new byte[var6 + var2];
      insertGap2(var0, var1, var2, var6, var7, var3);
      var4.shiftPc(var1, var2, var3);
      LineNumberAttribute var8 = (LineNumberAttribute)var5.getAttribute("LineNumberTable");
      if (var8 != null) {
         var8.shiftPc(var1, var2, var3);
      }

      LocalVariableAttribute var9 = (LocalVariableAttribute)var5.getAttribute("LocalVariableTable");
      if (var9 != null) {
         var9.shiftPc(var1, var2, var3);
      }

      LocalVariableAttribute var10 = (LocalVariableAttribute)var5.getAttribute("LocalVariableTypeTable");
      if (var10 != null) {
         var10.shiftPc(var1, var2, var3);
      }

      StackMapTable var11 = (StackMapTable)var5.getAttribute("StackMapTable");
      if (var11 != null) {
         var11.shiftPc(var1, var2, var3);
      }

      StackMap var12 = (StackMap)var5.getAttribute("StackMap");
      if (var12 != null) {
         var12.shiftPc(var1, var2, var3);
      }

      return var7;
   }

   private static void insertGap2(byte[] var0, int var1, int var2, int var3, byte[] var4, boolean var5) throws BadBytecode, CodeIterator.AlignmentException {
      int var7 = 0;

      int var6;
      for(int var8 = 0; var7 < var3; var7 = var6) {
         int var9;
         if (var7 == var1) {
            for(var9 = var8 + var2; var8 < var9; var4[var8++] = 0) {
            }
         }

         var6 = nextOpcode(var0, var7);
         var9 = var0[var7] & 255;
         int var10;
         if ((153 > var9 || var9 > 168) && var9 != 198 && var9 != 199) {
            if (var9 != 200 && var9 != 201) {
               int var11;
               int var12;
               int var13;
               int var14;
               if (var9 == 170) {
                  if (var7 != var8 && (var2 & 3) != 0) {
                     throw new CodeIterator.AlignmentException();
                  }

                  var10 = (var7 & -4) + 4;
                  var8 = copyGapBytes(var4, var8, var0, var7, var10);
                  var11 = newOffset(var7, ByteArray.read32bit(var0, var10), var1, var2, var5);
                  ByteArray.write32bit(var11, var4, var8);
                  var12 = ByteArray.read32bit(var0, var10 + 4);
                  ByteArray.write32bit(var12, var4, var8 + 4);
                  var13 = ByteArray.read32bit(var0, var10 + 8);
                  ByteArray.write32bit(var13, var4, var8 + 8);
                  var8 += 12;
                  var14 = var10 + 12;

                  for(var10 = var14 + (var13 - var12 + 1) * 4; var14 < var10; var14 += 4) {
                     int var15 = newOffset(var7, ByteArray.read32bit(var0, var14), var1, var2, var5);
                     ByteArray.write32bit(var15, var4, var8);
                     var8 += 4;
                  }
               } else if (var9 == 171) {
                  if (var7 != var8 && (var2 & 3) != 0) {
                     throw new CodeIterator.AlignmentException();
                  }

                  var10 = (var7 & -4) + 4;
                  var8 = copyGapBytes(var4, var8, var0, var7, var10);
                  var11 = newOffset(var7, ByteArray.read32bit(var0, var10), var1, var2, var5);
                  ByteArray.write32bit(var11, var4, var8);
                  var12 = ByteArray.read32bit(var0, var10 + 4);
                  ByteArray.write32bit(var12, var4, var8 + 4);
                  var8 += 8;
                  var13 = var10 + 8;

                  for(var10 = var13 + var12 * 8; var13 < var10; var13 += 8) {
                     ByteArray.copy32bit(var0, var13, var4, var8);
                     var14 = newOffset(var7, ByteArray.read32bit(var0, var13 + 4), var1, var2, var5);
                     ByteArray.write32bit(var14, var4, var8 + 4);
                     var8 += 8;
                  }
               } else {
                  while(var7 < var6) {
                     var4[var8++] = var0[var7++];
                  }
               }
            } else {
               var10 = ByteArray.read32bit(var0, var7 + 1);
               var10 = newOffset(var7, var10, var1, var2, var5);
               var4[var8++] = var0[var7];
               ByteArray.write32bit(var10, var4, var8);
               var8 += 4;
            }
         } else {
            var10 = var0[var7 + 1] << 8 | var0[var7 + 2] & 255;
            var10 = newOffset(var7, var10, var1, var2, var5);
            var4[var8] = var0[var7];
            ByteArray.write16bit(var10, var4, var8 + 1);
            var8 += 3;
         }
      }

   }

   private static int copyGapBytes(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      switch(var4 - var3) {
      case 4:
         var0[var1++] = var2[var3++];
      case 3:
         var0[var1++] = var2[var3++];
      case 2:
         var0[var1++] = var2[var3++];
      case 1:
         var0[var1++] = var2[var3++];
      default:
         return var1;
      }
   }

   private static int newOffset(int var0, int var1, int var2, int var3, boolean var4) {
      int var5 = var0 + var1;
      if (var0 < var2) {
         if (var2 < var5 || var4 && var2 == var5) {
            var1 += var3;
         }
      } else if (var0 == var2) {
         if (var5 < var2) {
            var1 -= var3;
         }
      } else if (var5 < var2 || !var4 && var2 == var5) {
         var1 -= var3;
      }

      return var1;
   }

   static byte[] changeLdcToLdcW(byte[] var0, ExceptionTable var1, CodeAttribute var2, CodeAttribute.LdcEntry var3) throws BadBytecode {
      CodeIterator.Pointers var4 = new CodeIterator.Pointers(0, 0, 0, var1, var2);

      ArrayList var5;
      for(var5 = makeJumpList(var0, var0.length, var4); var3 != null; var3 = var3.next) {
         addLdcW(var3, var5);
      }

      byte[] var6 = insertGap2w(var0, 0, 0, false, var5, var4);
      return var6;
   }

   private static void addLdcW(CodeAttribute.LdcEntry var0, ArrayList var1) {
      int var2 = var0.where;
      CodeIterator.LdcW var3 = new CodeIterator.LdcW(var2, var0.index);
      int var4 = var1.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var2 < ((CodeIterator.Branch)var1.get(var5)).orgPos) {
            var1.add(var5, var3);
            return;
         }
      }

      var1.add(var3);
   }

   private byte[] insertGapCore0w(byte[] var1, int var2, int var3, boolean var4, ExceptionTable var5, CodeAttribute var6, CodeIterator.Gap var7) throws BadBytecode {
      if (var3 <= 0) {
         return var1;
      } else {
         CodeIterator.Pointers var8 = new CodeIterator.Pointers(this.currentPos, this.mark, var2, var5, var6);
         ArrayList var9 = makeJumpList(var1, var1.length, var8);
         byte[] var10 = insertGap2w(var1, var2, var3, var4, var9, var8);
         this.currentPos = var8.cursor;
         this.mark = var8.mark;
         int var11 = var8.mark0;
         if (var11 == this.currentPos && !var4) {
            this.currentPos += var3;
         }

         if (var4) {
            var11 -= var3;
         }

         var7.position = var11;
         var7.length = var3;
         return var10;
      }
   }

   private static byte[] insertGap2w(byte[] var0, int var1, int var2, boolean var3, ArrayList var4, CodeIterator.Pointers var5) throws BadBytecode {
      int var6 = var4.size();
      if (var2 > 0) {
         var5.shiftPc(var1, var2, var3);

         for(int var7 = 0; var7 < var6; ++var7) {
            ((CodeIterator.Branch)var4.get(var7)).shift(var1, var2, var3);
         }
      }

      boolean var13 = true;

      while(true) {
         int var8;
         CodeIterator.Branch var9;
         int var10;
         int var11;
         int var12;
         while(!var13) {
            for(var8 = 0; var8 < var6; ++var8) {
               var9 = (CodeIterator.Branch)var4.get(var8);
               var10 = var9.gapChanged();
               if (var10 > 0) {
                  var13 = true;
                  var11 = var9.pos;
                  var5.shiftPc(var11, var10, false);

                  for(var12 = 0; var12 < var6; ++var12) {
                     ((CodeIterator.Branch)var4.get(var12)).shift(var11, var10, false);
                  }
               }
            }

            if (!var13) {
               return makeExapndedCode(var0, var4, var1, var2);
            }
         }

         var13 = false;

         for(var8 = 0; var8 < var6; ++var8) {
            var9 = (CodeIterator.Branch)var4.get(var8);
            if (var9.expanded()) {
               var13 = true;
               var10 = var9.pos;
               var11 = var9.deltaSize();
               var5.shiftPc(var10, var11, false);

               for(var12 = 0; var12 < var6; ++var12) {
                  ((CodeIterator.Branch)var4.get(var12)).shift(var10, var11, false);
               }
            }
         }
      }
   }

   private static ArrayList makeJumpList(byte[] var0, int var1, CodeIterator.Pointers var2) throws BadBytecode {
      ArrayList var3 = new ArrayList();

      int var4;
      for(int var5 = 0; var5 < var1; var5 = var4) {
         var4 = nextOpcode(var0, var5);
         int var6 = var0[var5] & 255;
         int var7;
         if ((153 > var6 || var6 > 168) && var6 != 198 && var6 != 199) {
            if (var6 != 200 && var6 != 201) {
               int var9;
               int var10;
               int var15;
               if (var6 == 170) {
                  var7 = (var5 & -4) + 4;
                  var15 = ByteArray.read32bit(var0, var7);
                  var9 = ByteArray.read32bit(var0, var7 + 4);
                  var10 = ByteArray.read32bit(var0, var7 + 8);
                  int var16 = var7 + 12;
                  int var17 = var10 - var9 + 1;
                  int[] var18 = new int[var17];

                  for(int var14 = 0; var14 < var17; ++var14) {
                     var18[var14] = ByteArray.read32bit(var0, var16);
                     var16 += 4;
                  }

                  var3.add(new CodeIterator.Table(var5, var15, var9, var10, var18, var2));
               } else if (var6 == 171) {
                  var7 = (var5 & -4) + 4;
                  var15 = ByteArray.read32bit(var0, var7);
                  var9 = ByteArray.read32bit(var0, var7 + 4);
                  var10 = var7 + 8;
                  int[] var11 = new int[var9];
                  int[] var12 = new int[var9];

                  for(int var13 = 0; var13 < var9; ++var13) {
                     var11[var13] = ByteArray.read32bit(var0, var10);
                     var12[var13] = ByteArray.read32bit(var0, var10 + 4);
                     var10 += 8;
                  }

                  var3.add(new CodeIterator.Lookup(var5, var15, var11, var12, var2));
               }
            } else {
               var7 = ByteArray.read32bit(var0, var5 + 1);
               var3.add(new CodeIterator.Jump32(var5, var7));
            }
         } else {
            var7 = var0[var5 + 1] << 8 | var0[var5 + 2] & 255;
            Object var8;
            if (var6 != 167 && var6 != 168) {
               var8 = new CodeIterator.If16(var5, var7);
            } else {
               var8 = new CodeIterator.Jump16(var5, var7);
            }

            var3.add(var8);
         }
      }

      return var3;
   }

   private static byte[] makeExapndedCode(byte[] var0, ArrayList var1, int var2, int var3) throws BadBytecode {
      int var4 = var1.size();
      int var5 = var0.length + var3;

      for(int var6 = 0; var6 < var4; ++var6) {
         CodeIterator.Branch var7 = (CodeIterator.Branch)var1.get(var6);
         var5 += var7.deltaSize();
      }

      byte[] var14 = new byte[var5];
      int var15 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = var0.length;
      CodeIterator.Branch var11;
      int var12;
      if (0 < var4) {
         var11 = (CodeIterator.Branch)var1.get(0);
         var12 = var11.orgPos;
      } else {
         var11 = null;
         var12 = var10;
      }

      while(var15 < var10) {
         int var13;
         if (var15 == var2) {
            for(var13 = var8 + var3; var8 < var13; var14[var8++] = 0) {
            }
         }

         if (var15 != var12) {
            var14[var8++] = var0[var15++];
         } else {
            var13 = var11.write(var15, var0, var8, var14);
            var15 += var13;
            var8 += var13 + var11.deltaSize();
            ++var9;
            if (var9 < var4) {
               var11 = (CodeIterator.Branch)var1.get(var9);
               var12 = var11.orgPos;
            } else {
               var11 = null;
               var12 = var10;
            }
         }
      }

      return var14;
   }

   static class Lookup extends CodeIterator.Switcher {
      int[] matches;

      Lookup(int var1, int var2, int[] var3, int[] var4, CodeIterator.Pointers var5) {
         super(var1, var2, var4, var5);
         this.matches = var3;
      }

      int write2(int var1, byte[] var2) {
         int var3 = this.matches.length;
         ByteArray.write32bit(var3, var2, var1);
         var1 += 4;

         for(int var4 = 0; var4 < var3; ++var4) {
            ByteArray.write32bit(this.matches[var4], var2, var1);
            ByteArray.write32bit(this.offsets[var4], var2, var1 + 4);
            var1 += 8;
         }

         return 4 + 8 * var3;
      }

      int tableSize() {
         return 4 + 8 * this.matches.length;
      }
   }

   static class Table extends CodeIterator.Switcher {
      int low;
      int high;

      Table(int var1, int var2, int var3, int var4, int[] var5, CodeIterator.Pointers var6) {
         super(var1, var2, var5, var6);
         this.low = var3;
         this.high = var4;
      }

      int write2(int var1, byte[] var2) {
         ByteArray.write32bit(this.low, var2, var1);
         ByteArray.write32bit(this.high, var2, var1 + 4);
         int var3 = this.offsets.length;
         var1 += 8;

         for(int var4 = 0; var4 < var3; ++var4) {
            ByteArray.write32bit(this.offsets[var4], var2, var1);
            var1 += 4;
         }

         return 8 + 4 * var3;
      }

      int tableSize() {
         return 8 + 4 * this.offsets.length;
      }
   }

   abstract static class Switcher extends CodeIterator.Branch {
      int gap;
      int defaultByte;
      int[] offsets;
      CodeIterator.Pointers pointers;

      Switcher(int var1, int var2, int[] var3, CodeIterator.Pointers var4) {
         super(var1);
         this.gap = 3 - (var1 & 3);
         this.defaultByte = var2;
         this.offsets = var3;
         this.pointers = var4;
      }

      void shift(int var1, int var2, boolean var3) {
         int var4 = this.pos;
         this.defaultByte = shiftOffset(var4, this.defaultByte, var1, var2, var3);
         int var5 = this.offsets.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            this.offsets[var6] = shiftOffset(var4, this.offsets[var6], var1, var2, var3);
         }

         super.shift(var1, var2, var3);
      }

      int gapChanged() {
         int var1 = 3 - (this.pos & 3);
         if (var1 > this.gap) {
            int var2 = var1 - this.gap;
            this.gap = var1;
            return var2;
         } else {
            return 0;
         }
      }

      int deltaSize() {
         return this.gap - (3 - (this.orgPos & 3));
      }

      int write(int var1, byte[] var2, int var3, byte[] var4) throws BadBytecode {
         int var5 = 3 - (this.pos & 3);
         int var6 = this.gap - var5;
         int var7 = 5 + (3 - (this.orgPos & 3)) + this.tableSize();
         if (var6 > 0) {
            this.adjustOffsets(var7, var6);
         }

         for(var4[var3++] = var2[var1]; var5-- > 0; var4[var3++] = 0) {
         }

         ByteArray.write32bit(this.defaultByte, var4, var3);
         int var8 = this.write2(var3 + 4, var4);

         for(var3 += var8 + 4; var6-- > 0; var4[var3++] = 0) {
         }

         return 5 + (3 - (this.orgPos & 3)) + var8;
      }

      abstract int write2(int var1, byte[] var2);

      abstract int tableSize();

      void adjustOffsets(int var1, int var2) throws BadBytecode {
         this.pointers.shiftForSwitch(this.pos + var1, var2);
         if (this.defaultByte == var1) {
            this.defaultByte -= var2;
         }

         for(int var3 = 0; var3 < this.offsets.length; ++var3) {
            if (this.offsets[var3] == var1) {
               int[] var10000 = this.offsets;
               var10000[var3] -= var2;
            }
         }

      }
   }

   static class Jump32 extends CodeIterator.Branch {
      int offset;

      Jump32(int var1, int var2) {
         super(var1);
         this.offset = var2;
      }

      void shift(int var1, int var2, boolean var3) {
         this.offset = shiftOffset(this.pos, this.offset, var1, var2, var3);
         super.shift(var1, var2, var3);
      }

      int write(int var1, byte[] var2, int var3, byte[] var4) {
         var4[var3] = var2[var1];
         ByteArray.write32bit(this.offset, var4, var3 + 1);
         return 5;
      }
   }

   static class If16 extends CodeIterator.Branch16 {
      If16(int var1, int var2) {
         super(var1, var2);
      }

      int deltaSize() {
         return this.state == 2 ? 5 : 0;
      }

      void write32(int var1, byte[] var2, int var3, byte[] var4) {
         var4[var3] = (byte)this.opcode(var2[var1] & 255);
         var4[var3 + 1] = 0;
         var4[var3 + 2] = 8;
         var4[var3 + 3] = -56;
         ByteArray.write32bit(this.offset - 3, var4, var3 + 4);
      }

      int opcode(int var1) {
         if (var1 == 198) {
            return 199;
         } else if (var1 == 199) {
            return 198;
         } else {
            return (var1 - 153 & 1) == 0 ? var1 + 1 : var1 - 1;
         }
      }
   }

   static class Jump16 extends CodeIterator.Branch16 {
      Jump16(int var1, int var2) {
         super(var1, var2);
      }

      int deltaSize() {
         return this.state == 2 ? 2 : 0;
      }

      void write32(int var1, byte[] var2, int var3, byte[] var4) {
         var4[var3] = (byte)((var2[var1] & 255) == 167 ? 200 : 201);
         ByteArray.write32bit(this.offset, var4, var3 + 1);
      }
   }

   abstract static class Branch16 extends CodeIterator.Branch {
      int offset;
      int state;
      static final int BIT16 = 0;
      static final int EXPAND = 1;
      static final int BIT32 = 2;

      Branch16(int var1, int var2) {
         super(var1);
         this.offset = var2;
         this.state = 0;
      }

      void shift(int var1, int var2, boolean var3) {
         this.offset = shiftOffset(this.pos, this.offset, var1, var2, var3);
         super.shift(var1, var2, var3);
         if (this.state == 0 && (this.offset < -32768 || 32767 < this.offset)) {
            this.state = 1;
         }

      }

      boolean expanded() {
         if (this.state == 1) {
            this.state = 2;
            return true;
         } else {
            return false;
         }
      }

      abstract int deltaSize();

      abstract void write32(int var1, byte[] var2, int var3, byte[] var4);

      int write(int var1, byte[] var2, int var3, byte[] var4) {
         if (this.state == 2) {
            this.write32(var1, var2, var3, var4);
         } else {
            var4[var3] = var2[var1];
            ByteArray.write16bit(this.offset, var4, var3 + 1);
         }

         return 3;
      }
   }

   static class LdcW extends CodeIterator.Branch {
      int index;
      boolean state;

      LdcW(int var1, int var2) {
         super(var1);
         this.index = var2;
         this.state = true;
      }

      boolean expanded() {
         if (this.state) {
            this.state = false;
            return true;
         } else {
            return false;
         }
      }

      int deltaSize() {
         return 1;
      }

      int write(int var1, byte[] var2, int var3, byte[] var4) {
         var4[var3] = 19;
         ByteArray.write16bit(this.index, var4, var3 + 1);
         return 2;
      }
   }

   abstract static class Branch {
      int pos;
      int orgPos;

      Branch(int var1) {
         this.pos = this.orgPos = var1;
      }

      void shift(int var1, int var2, boolean var3) {
         if (var1 < this.pos || var1 == this.pos && var3) {
            this.pos += var2;
         }

      }

      static int shiftOffset(int var0, int var1, int var2, int var3, boolean var4) {
         int var5 = var0 + var1;
         if (var0 < var2) {
            if (var2 < var5 || var4 && var2 == var5) {
               var1 += var3;
            }
         } else if (var0 == var2) {
            if (var5 < var2 && var4) {
               var1 -= var3;
            } else if (var2 < var5 && !var4) {
               var1 += var3;
            }
         } else if (var5 < var2 || !var4 && var2 == var5) {
            var1 -= var3;
         }

         return var1;
      }

      boolean expanded() {
         return false;
      }

      int gapChanged() {
         return 0;
      }

      int deltaSize() {
         return 0;
      }

      abstract int write(int var1, byte[] var2, int var3, byte[] var4) throws BadBytecode;
   }

   static class Pointers {
      int cursor;
      int mark0;
      int mark;
      ExceptionTable etable;
      LineNumberAttribute line;
      LocalVariableAttribute vars;
      LocalVariableAttribute types;
      StackMapTable stack;
      StackMap stack2;

      Pointers(int var1, int var2, int var3, ExceptionTable var4, CodeAttribute var5) {
         this.cursor = var1;
         this.mark = var2;
         this.mark0 = var3;
         this.etable = var4;
         this.line = (LineNumberAttribute)var5.getAttribute("LineNumberTable");
         this.vars = (LocalVariableAttribute)var5.getAttribute("LocalVariableTable");
         this.types = (LocalVariableAttribute)var5.getAttribute("LocalVariableTypeTable");
         this.stack = (StackMapTable)var5.getAttribute("StackMapTable");
         this.stack2 = (StackMap)var5.getAttribute("StackMap");
      }

      void shiftPc(int var1, int var2, boolean var3) throws BadBytecode {
         if (var1 < this.cursor || var1 == this.cursor && var3) {
            this.cursor += var2;
         }

         if (var1 < this.mark || var1 == this.mark && var3) {
            this.mark += var2;
         }

         if (var1 < this.mark0 || var1 == this.mark0 && var3) {
            this.mark0 += var2;
         }

         this.etable.shiftPc(var1, var2, var3);
         if (this.line != null) {
            this.line.shiftPc(var1, var2, var3);
         }

         if (this.vars != null) {
            this.vars.shiftPc(var1, var2, var3);
         }

         if (this.types != null) {
            this.types.shiftPc(var1, var2, var3);
         }

         if (this.stack != null) {
            this.stack.shiftPc(var1, var2, var3);
         }

         if (this.stack2 != null) {
            this.stack2.shiftPc(var1, var2, var3);
         }

      }

      void shiftForSwitch(int var1, int var2) throws BadBytecode {
         if (this.stack != null) {
            this.stack.shiftForSwitch(var1, var2);
         }

         if (this.stack2 != null) {
            this.stack2.shiftForSwitch(var1, var2);
         }

      }
   }

   static class AlignmentException extends Exception {
   }

   public static class Gap {
      public int position;
      public int length;
   }
}
