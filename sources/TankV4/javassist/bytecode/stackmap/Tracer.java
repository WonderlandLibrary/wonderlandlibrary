package javassist.bytecode.stackmap;

import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ByteArray;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;

public abstract class Tracer implements TypeTag {
   protected ClassPool classPool;
   protected ConstPool cpool;
   protected String returnType;
   protected int stackTop;
   protected TypeData[] stackTypes;
   protected TypeData[] localsTypes;

   public Tracer(ClassPool var1, ConstPool var2, int var3, int var4, String var5) {
      this.classPool = var1;
      this.cpool = var2;
      this.returnType = var5;
      this.stackTop = 0;
      this.stackTypes = TypeData.make(var3);
      this.localsTypes = TypeData.make(var4);
   }

   public Tracer(Tracer var1) {
      this.classPool = var1.classPool;
      this.cpool = var1.cpool;
      this.returnType = var1.returnType;
      this.stackTop = var1.stackTop;
      this.stackTypes = TypeData.make(var1.stackTypes.length);
      this.localsTypes = TypeData.make(var1.localsTypes.length);
   }

   protected int doOpcode(int var1, byte[] var2) throws BadBytecode {
      try {
         int var3 = var2[var1] & 255;
         if (var3 < 96) {
            return var3 < 54 ? this.doOpcode0_53(var1, var2, var3) : this.doOpcode54_95(var1, var2, var3);
         } else {
            return var3 < 148 ? this.doOpcode96_147(var1, var2, var3) : this.doOpcode148_201(var1, var2, var3);
         }
      } catch (ArrayIndexOutOfBoundsException var4) {
         throw new BadBytecode("inconsistent stack height " + var4.getMessage(), var4);
      }
   }

   protected void visitBranch(int var1, byte[] var2, int var3) throws BadBytecode {
   }

   protected void visitGoto(int var1, byte[] var2, int var3) throws BadBytecode {
   }

   protected void visitReturn(int var1, byte[] var2) throws BadBytecode {
   }

   protected void visitThrow(int var1, byte[] var2) throws BadBytecode {
   }

   protected void visitTableSwitch(int var1, byte[] var2, int var3, int var4, int var5) throws BadBytecode {
   }

   protected void visitLookupSwitch(int var1, byte[] var2, int var3, int var4, int var5) throws BadBytecode {
   }

   protected void visitJSR(int var1, byte[] var2) throws BadBytecode {
   }

   protected void visitRET(int var1, byte[] var2) throws BadBytecode {
   }

   private int doOpcode0_53(int var1, byte[] var2, int var3) throws BadBytecode {
      TypeData[] var5 = this.stackTypes;
      switch(var3) {
      case 0:
         break;
      case 1:
         var5[this.stackTop++] = new TypeData.NullType();
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         var5[this.stackTop++] = INTEGER;
         break;
      case 9:
      case 10:
         var5[this.stackTop++] = LONG;
         var5[this.stackTop++] = TOP;
         break;
      case 11:
      case 12:
      case 13:
         var5[this.stackTop++] = FLOAT;
         break;
      case 14:
      case 15:
         var5[this.stackTop++] = DOUBLE;
         var5[this.stackTop++] = TOP;
         break;
      case 16:
      case 17:
         var5[this.stackTop++] = INTEGER;
         return var3 == 17 ? 3 : 2;
      case 18:
         this.doLDC(var2[var1 + 1] & 255);
         return 2;
      case 19:
      case 20:
         this.doLDC(ByteArray.readU16bit(var2, var1 + 1));
         return 3;
      case 21:
         return this.doXLOAD(INTEGER, var2, var1);
      case 22:
         return this.doXLOAD(LONG, var2, var1);
      case 23:
         return this.doXLOAD(FLOAT, var2, var1);
      case 24:
         return this.doXLOAD(DOUBLE, var2, var1);
      case 25:
         return this.doALOAD(var2[var1 + 1] & 255);
      case 26:
      case 27:
      case 28:
      case 29:
         var5[this.stackTop++] = INTEGER;
         break;
      case 30:
      case 31:
      case 32:
      case 33:
         var5[this.stackTop++] = LONG;
         var5[this.stackTop++] = TOP;
         break;
      case 34:
      case 35:
      case 36:
      case 37:
         var5[this.stackTop++] = FLOAT;
         break;
      case 38:
      case 39:
      case 40:
      case 41:
         var5[this.stackTop++] = DOUBLE;
         var5[this.stackTop++] = TOP;
         break;
      case 42:
      case 43:
      case 44:
      case 45:
         int var4 = var3 - 42;
         var5[this.stackTop++] = this.localsTypes[var4];
         break;
      case 46:
         var5[--this.stackTop - 1] = INTEGER;
         break;
      case 47:
         var5[this.stackTop - 2] = LONG;
         var5[this.stackTop - 1] = TOP;
         break;
      case 48:
         var5[--this.stackTop - 1] = FLOAT;
         break;
      case 49:
         var5[this.stackTop - 2] = DOUBLE;
         var5[this.stackTop - 1] = TOP;
         break;
      case 50:
         int var6 = --this.stackTop - 1;
         TypeData var7 = var5[var6];
         var5[var6] = TypeData.ArrayElement.make(var7);
         break;
      case 51:
      case 52:
      case 53:
         var5[--this.stackTop - 1] = INTEGER;
         break;
      default:
         throw new RuntimeException("fatal");
      }

      return 1;
   }

   private void doLDC(int var1) {
      TypeData[] var2 = this.stackTypes;
      int var3 = this.cpool.getTag(var1);
      if (var3 == 8) {
         var2[this.stackTop++] = new TypeData.ClassName("java.lang.String");
      } else if (var3 == 3) {
         var2[this.stackTop++] = INTEGER;
      } else if (var3 == 4) {
         var2[this.stackTop++] = FLOAT;
      } else if (var3 == 5) {
         var2[this.stackTop++] = LONG;
         var2[this.stackTop++] = TOP;
      } else if (var3 == 6) {
         var2[this.stackTop++] = DOUBLE;
         var2[this.stackTop++] = TOP;
      } else {
         if (var3 != 7) {
            throw new RuntimeException("bad LDC: " + var3);
         }

         var2[this.stackTop++] = new TypeData.ClassName("java.lang.Class");
      }

   }

   private int doXLOAD(TypeData var1, byte[] var2, int var3) {
      int var4 = var2[var3 + 1] & 255;
      return this.doXLOAD(var4, var1);
   }

   private int doXLOAD(int var1, TypeData var2) {
      this.stackTypes[this.stackTop++] = var2;
      if (var2.is2WordType()) {
         this.stackTypes[this.stackTop++] = TOP;
      }

      return 2;
   }

   private int doALOAD(int var1) {
      this.stackTypes[this.stackTop++] = this.localsTypes[var1];
      return 2;
   }

   private int doOpcode54_95(int var1, byte[] var2, int var3) throws BadBytecode {
      int var4;
      int var6;
      switch(var3) {
      case 54:
         return this.doXSTORE(var1, var2, INTEGER);
      case 55:
         return this.doXSTORE(var1, var2, LONG);
      case 56:
         return this.doXSTORE(var1, var2, FLOAT);
      case 57:
         return this.doXSTORE(var1, var2, DOUBLE);
      case 58:
         return this.doASTORE(var2[var1 + 1] & 255);
      case 59:
      case 60:
      case 61:
      case 62:
         var4 = var3 - 59;
         this.localsTypes[var4] = INTEGER;
         --this.stackTop;
         break;
      case 63:
      case 64:
      case 65:
      case 66:
         var4 = var3 - 63;
         this.localsTypes[var4] = LONG;
         this.localsTypes[var4 + 1] = TOP;
         this.stackTop -= 2;
         break;
      case 67:
      case 68:
      case 69:
      case 70:
         var4 = var3 - 67;
         this.localsTypes[var4] = FLOAT;
         --this.stackTop;
         break;
      case 71:
      case 72:
      case 73:
      case 74:
         var4 = var3 - 71;
         this.localsTypes[var4] = DOUBLE;
         this.localsTypes[var4 + 1] = TOP;
         this.stackTop -= 2;
         break;
      case 75:
      case 76:
      case 77:
      case 78:
         var4 = var3 - 75;
         this.doASTORE(var4);
         break;
      case 79:
      case 80:
      case 81:
      case 82:
         this.stackTop -= var3 != 80 && var3 != 82 ? 3 : 4;
         break;
      case 83:
         TypeData.ArrayElement.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool);
         this.stackTop -= 3;
         break;
      case 84:
      case 85:
      case 86:
         this.stackTop -= 3;
         break;
      case 87:
         --this.stackTop;
         break;
      case 88:
         this.stackTop -= 2;
         break;
      case 89:
         var4 = this.stackTop;
         this.stackTypes[var4] = this.stackTypes[var4 - 1];
         this.stackTop = var4 + 1;
         break;
      case 90:
      case 91:
         var4 = var3 - 90 + 2;
         this.doDUP_XX(1, var4);
         var6 = this.stackTop;
         this.stackTypes[var6 - var4] = this.stackTypes[var6];
         this.stackTop = var6 + 1;
         break;
      case 92:
         this.doDUP_XX(2, 2);
         this.stackTop += 2;
         break;
      case 93:
      case 94:
         var4 = var3 - 93 + 3;
         this.doDUP_XX(2, var4);
         var6 = this.stackTop;
         this.stackTypes[var6 - var4] = this.stackTypes[var6];
         this.stackTypes[var6 - var4 + 1] = this.stackTypes[var6 + 1];
         this.stackTop = var6 + 2;
         break;
      case 95:
         var4 = this.stackTop - 1;
         TypeData var5 = this.stackTypes[var4];
         this.stackTypes[var4] = this.stackTypes[var4 - 1];
         this.stackTypes[var4 - 1] = var5;
         break;
      default:
         throw new RuntimeException("fatal");
      }

      return 1;
   }

   private int doXSTORE(int var1, byte[] var2, TypeData var3) {
      int var4 = var2[var1 + 1] & 255;
      return this.doXSTORE(var4, var3);
   }

   private int doXSTORE(int var1, TypeData var2) {
      --this.stackTop;
      this.localsTypes[var1] = var2;
      if (var2.is2WordType()) {
         --this.stackTop;
         this.localsTypes[var1 + 1] = TOP;
      }

      return 2;
   }

   private int doASTORE(int var1) {
      --this.stackTop;
      this.localsTypes[var1] = this.stackTypes[this.stackTop];
      return 2;
   }

   private void doDUP_XX(int var1, int var2) {
      TypeData[] var3 = this.stackTypes;
      int var4 = this.stackTop - 1;

      for(int var5 = var4 - var2; var4 > var5; --var4) {
         var3[var4 + var1] = var3[var4];
      }

   }

   private int doOpcode96_147(int var1, byte[] var2, int var3) {
      if (var3 <= 131) {
         this.stackTop += Opcode.STACK_GROW[var3];
         return 1;
      } else {
         switch(var3) {
         case 132:
            return 3;
         case 133:
            this.stackTypes[this.stackTop - 1] = LONG;
            this.stackTypes[this.stackTop] = TOP;
            ++this.stackTop;
            break;
         case 134:
            this.stackTypes[this.stackTop - 1] = FLOAT;
            break;
         case 135:
            this.stackTypes[this.stackTop - 1] = DOUBLE;
            this.stackTypes[this.stackTop] = TOP;
            ++this.stackTop;
            break;
         case 136:
            this.stackTypes[--this.stackTop - 1] = INTEGER;
            break;
         case 137:
            this.stackTypes[--this.stackTop - 1] = FLOAT;
            break;
         case 138:
            this.stackTypes[this.stackTop - 2] = DOUBLE;
            break;
         case 139:
            this.stackTypes[this.stackTop - 1] = INTEGER;
            break;
         case 140:
            this.stackTypes[this.stackTop - 1] = LONG;
            this.stackTypes[this.stackTop] = TOP;
            ++this.stackTop;
            break;
         case 141:
            this.stackTypes[this.stackTop - 1] = DOUBLE;
            this.stackTypes[this.stackTop] = TOP;
            ++this.stackTop;
            break;
         case 142:
            this.stackTypes[--this.stackTop - 1] = INTEGER;
            break;
         case 143:
            this.stackTypes[this.stackTop - 2] = LONG;
            break;
         case 144:
            this.stackTypes[--this.stackTop - 1] = FLOAT;
         case 145:
         case 146:
         case 147:
            break;
         default:
            throw new RuntimeException("fatal");
         }

         return 1;
      }
   }

   private int doOpcode148_201(int var1, byte[] var2, int var3) throws BadBytecode {
      int var4;
      String var5;
      int var8;
      switch(var3) {
      case 148:
         this.stackTypes[this.stackTop - 4] = INTEGER;
         this.stackTop -= 3;
         break;
      case 149:
      case 150:
         this.stackTypes[--this.stackTop - 1] = INTEGER;
         break;
      case 151:
      case 152:
         this.stackTypes[this.stackTop - 4] = INTEGER;
         this.stackTop -= 3;
         break;
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
         --this.stackTop;
         this.visitBranch(var1, var2, ByteArray.readS16bit(var2, var1 + 1));
         return 3;
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
      case 165:
      case 166:
         this.stackTop -= 2;
         this.visitBranch(var1, var2, ByteArray.readS16bit(var2, var1 + 1));
         return 3;
      case 167:
         this.visitGoto(var1, var2, ByteArray.readS16bit(var2, var1 + 1));
         return 3;
      case 168:
         this.visitJSR(var1, var2);
         return 3;
      case 169:
         this.visitRET(var1, var2);
         return 2;
      case 170:
         --this.stackTop;
         var4 = (var1 & -4) + 8;
         var8 = ByteArray.read32bit(var2, var4);
         int var6 = ByteArray.read32bit(var2, var4 + 4);
         int var7 = var6 - var8 + 1;
         this.visitTableSwitch(var1, var2, var7, var4 + 8, ByteArray.read32bit(var2, var4 - 4));
         return var7 * 4 + 16 - (var1 & 3);
      case 171:
         --this.stackTop;
         var4 = (var1 & -4) + 8;
         var8 = ByteArray.read32bit(var2, var4);
         this.visitLookupSwitch(var1, var2, var8, var4 + 4, ByteArray.read32bit(var2, var4 - 4));
         return var8 * 8 + 12 - (var1 & 3);
      case 172:
         --this.stackTop;
         this.visitReturn(var1, var2);
         break;
      case 173:
         this.stackTop -= 2;
         this.visitReturn(var1, var2);
         break;
      case 174:
         --this.stackTop;
         this.visitReturn(var1, var2);
         break;
      case 175:
         this.stackTop -= 2;
         this.visitReturn(var1, var2);
         break;
      case 176:
         this.stackTypes[--this.stackTop].setType(this.returnType, this.classPool);
         this.visitReturn(var1, var2);
         break;
      case 177:
         this.visitReturn(var1, var2);
         break;
      case 178:
         return this.doGetField(var1, var2, false);
      case 179:
         return this.doPutField(var1, var2, false);
      case 180:
         return this.doGetField(var1, var2, true);
      case 181:
         return this.doPutField(var1, var2, true);
      case 182:
      case 183:
         return this.doInvokeMethod(var1, var2, true);
      case 184:
         return this.doInvokeMethod(var1, var2, false);
      case 185:
         return this.doInvokeIntfMethod(var1, var2);
      case 186:
         return this.doInvokeDynamic(var1, var2);
      case 187:
         var4 = ByteArray.readU16bit(var2, var1 + 1);
         this.stackTypes[this.stackTop++] = new TypeData.UninitData(var1, this.cpool.getClassInfo(var4));
         return 3;
      case 188:
         return this.doNEWARRAY(var1, var2);
      case 189:
         var4 = ByteArray.readU16bit(var2, var1 + 1);
         var5 = this.cpool.getClassInfo(var4).replace('.', '/');
         if (var5.charAt(0) == '[') {
            var5 = "[" + var5;
         } else {
            var5 = "[L" + var5 + ";";
         }

         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(var5);
         return 3;
      case 190:
         this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
         this.stackTypes[this.stackTop - 1] = INTEGER;
         break;
      case 191:
         this.stackTypes[--this.stackTop].setType("java.lang.Throwable", this.classPool);
         this.visitThrow(var1, var2);
         break;
      case 192:
         var4 = ByteArray.readU16bit(var2, var1 + 1);
         var5 = this.cpool.getClassInfo(var4);
         if (var5.charAt(0) == '[') {
            var5 = var5.replace('.', '/');
         }

         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(var5);
         return 3;
      case 193:
         this.stackTypes[this.stackTop - 1] = INTEGER;
         return 3;
      case 194:
      case 195:
         --this.stackTop;
         break;
      case 196:
         return this.doWIDE(var1, var2);
      case 197:
         return this.doMultiANewArray(var1, var2);
      case 198:
      case 199:
         --this.stackTop;
         this.visitBranch(var1, var2, ByteArray.readS16bit(var2, var1 + 1));
         return 3;
      case 200:
         this.visitGoto(var1, var2, ByteArray.read32bit(var2, var1 + 1));
         return 5;
      case 201:
         this.visitJSR(var1, var2);
         return 5;
      }

      return 1;
   }

   private int doWIDE(int var1, byte[] var2) throws BadBytecode {
      int var3 = var2[var1 + 1] & 255;
      int var4;
      switch(var3) {
      case 21:
         this.doWIDE_XLOAD(var1, var2, INTEGER);
         break;
      case 22:
         this.doWIDE_XLOAD(var1, var2, LONG);
         break;
      case 23:
         this.doWIDE_XLOAD(var1, var2, FLOAT);
         break;
      case 24:
         this.doWIDE_XLOAD(var1, var2, DOUBLE);
         break;
      case 25:
         var4 = ByteArray.readU16bit(var2, var1 + 2);
         this.doALOAD(var4);
         break;
      case 54:
         this.doWIDE_STORE(var1, var2, INTEGER);
         break;
      case 55:
         this.doWIDE_STORE(var1, var2, LONG);
         break;
      case 56:
         this.doWIDE_STORE(var1, var2, FLOAT);
         break;
      case 57:
         this.doWIDE_STORE(var1, var2, DOUBLE);
         break;
      case 58:
         var4 = ByteArray.readU16bit(var2, var1 + 2);
         this.doASTORE(var4);
         break;
      case 132:
         return 6;
      case 169:
         this.visitRET(var1, var2);
         break;
      default:
         throw new RuntimeException("bad WIDE instruction: " + var3);
      }

      return 4;
   }

   private void doWIDE_XLOAD(int var1, byte[] var2, TypeData var3) {
      int var4 = ByteArray.readU16bit(var2, var1 + 2);
      this.doXLOAD(var4, var3);
   }

   private void doWIDE_STORE(int var1, byte[] var2, TypeData var3) {
      int var4 = ByteArray.readU16bit(var2, var1 + 2);
      this.doXSTORE(var4, var3);
   }

   private int doPutField(int var1, byte[] var2, boolean var3) throws BadBytecode {
      int var4 = ByteArray.readU16bit(var2, var1 + 1);
      String var5 = this.cpool.getFieldrefType(var4);
      this.stackTop -= Descriptor.dataSize(var5);
      char var6 = var5.charAt(0);
      if (var6 == 'L') {
         this.stackTypes[this.stackTop].setType(getFieldClassName(var5, 0), this.classPool);
      } else if (var6 == '[') {
         this.stackTypes[this.stackTop].setType(var5, this.classPool);
      }

      this.setFieldTarget(var3, var4);
      return 3;
   }

   private int doGetField(int var1, byte[] var2, boolean var3) throws BadBytecode {
      int var4 = ByteArray.readU16bit(var2, var1 + 1);
      this.setFieldTarget(var3, var4);
      String var5 = this.cpool.getFieldrefType(var4);
      this.pushMemberType(var5);
      return 3;
   }

   private void setFieldTarget(boolean var1, int var2) throws BadBytecode {
      if (var1) {
         String var3 = this.cpool.getFieldrefClassName(var2);
         this.stackTypes[--this.stackTop].setType(var3, this.classPool);
      }

   }

   private int doNEWARRAY(int var1, byte[] var2) {
      int var3 = this.stackTop - 1;
      String var4;
      switch(var2[var1 + 1] & 255) {
      case 4:
         var4 = "[Z";
         break;
      case 5:
         var4 = "[C";
         break;
      case 6:
         var4 = "[F";
         break;
      case 7:
         var4 = "[D";
         break;
      case 8:
         var4 = "[B";
         break;
      case 9:
         var4 = "[S";
         break;
      case 10:
         var4 = "[I";
         break;
      case 11:
         var4 = "[J";
         break;
      default:
         throw new RuntimeException("bad newarray");
      }

      this.stackTypes[var3] = new TypeData.ClassName(var4);
      return 2;
   }

   private int doMultiANewArray(int var1, byte[] var2) {
      int var3 = ByteArray.readU16bit(var2, var1 + 1);
      int var4 = var2[var1 + 3] & 255;
      this.stackTop -= var4 - 1;
      String var5 = this.cpool.getClassInfo(var3).replace('.', '/');
      this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(var5);
      return 4;
   }

   private int doInvokeMethod(int var1, byte[] var2, boolean var3) throws BadBytecode {
      int var4 = ByteArray.readU16bit(var2, var1 + 1);
      String var5 = this.cpool.getMethodrefType(var4);
      this.checkParamTypes(var5, 1);
      if (var3) {
         String var6 = this.cpool.getMethodrefClassName(var4);
         TypeData var7 = this.stackTypes[--this.stackTop];
         if (var7 instanceof TypeData.UninitTypeVar && var7.isUninit()) {
            this.constructorCalled(var7, ((TypeData.UninitTypeVar)var7).offset());
         } else if (var7 instanceof TypeData.UninitData) {
            this.constructorCalled(var7, ((TypeData.UninitData)var7).offset());
         }

         var7.setType(var6, this.classPool);
      }

      this.pushMemberType(var5);
      return 3;
   }

   private void constructorCalled(TypeData var1, int var2) {
      var1.constructorCalled(var2);

      int var3;
      for(var3 = 0; var3 < this.stackTop; ++var3) {
         this.stackTypes[var3].constructorCalled(var2);
      }

      for(var3 = 0; var3 < this.localsTypes.length; ++var3) {
         this.localsTypes[var3].constructorCalled(var2);
      }

   }

   private int doInvokeIntfMethod(int var1, byte[] var2) throws BadBytecode {
      int var3 = ByteArray.readU16bit(var2, var1 + 1);
      String var4 = this.cpool.getInterfaceMethodrefType(var3);
      this.checkParamTypes(var4, 1);
      String var5 = this.cpool.getInterfaceMethodrefClassName(var3);
      this.stackTypes[--this.stackTop].setType(var5, this.classPool);
      this.pushMemberType(var4);
      return 5;
   }

   private int doInvokeDynamic(int var1, byte[] var2) throws BadBytecode {
      int var3 = ByteArray.readU16bit(var2, var1 + 1);
      String var4 = this.cpool.getInvokeDynamicType(var3);
      this.checkParamTypes(var4, 1);
      this.pushMemberType(var4);
      return 5;
   }

   private void pushMemberType(String var1) {
      int var2 = 0;
      if (var1.charAt(0) == '(') {
         var2 = var1.indexOf(41) + 1;
         if (var2 < 1) {
            throw new IndexOutOfBoundsException("bad descriptor: " + var1);
         }
      }

      TypeData[] var3 = this.stackTypes;
      int var4 = this.stackTop;
      switch(var1.charAt(var2)) {
      case 'D':
         var3[var4] = DOUBLE;
         var3[var4 + 1] = TOP;
         this.stackTop += 2;
         return;
      case 'F':
         var3[var4] = FLOAT;
         break;
      case 'J':
         var3[var4] = LONG;
         var3[var4 + 1] = TOP;
         this.stackTop += 2;
         return;
      case 'L':
         var3[var4] = new TypeData.ClassName(getFieldClassName(var1, var2));
         break;
      case 'V':
         return;
      case '[':
         var3[var4] = new TypeData.ClassName(var1.substring(var2));
         break;
      default:
         var3[var4] = INTEGER;
      }

      ++this.stackTop;
   }

   private static String getFieldClassName(String var0, int var1) {
      return var0.substring(var1 + 1, var0.length() - 1).replace('/', '.');
   }

   private void checkParamTypes(String var1, int var2) throws BadBytecode {
      char var3 = var1.charAt(var2);
      if (var3 != ')') {
         int var4 = var2;

         boolean var5;
         for(var5 = false; var3 == '['; var3 = var1.charAt(var4)) {
            var5 = true;
            ++var4;
         }

         if (var3 == 'L') {
            var4 = var1.indexOf(59, var4) + 1;
            if (var4 <= 0) {
               throw new IndexOutOfBoundsException("bad descriptor");
            }
         } else {
            ++var4;
         }

         this.checkParamTypes(var1, var4);
         if (var5 || var3 != 'J' && var3 != 'D') {
            --this.stackTop;
         } else {
            this.stackTop -= 2;
         }

         if (var5) {
            this.stackTypes[this.stackTop].setType(var1.substring(var2, var4), this.classPool);
         } else if (var3 == 'L') {
            this.stackTypes[this.stackTop].setType(var1.substring(var2 + 1, var4 - 1).replace('/', '.'), this.classPool);
         }

      }
   }
}
