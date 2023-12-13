package javassist.bytecode.analysis;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class Executor implements Opcode {
   private final ConstPool constPool;
   private final ClassPool classPool;
   private final Type STRING_TYPE;
   private final Type CLASS_TYPE;
   private final Type THROWABLE_TYPE;
   private int lastPos;

   public Executor(ClassPool var1, ConstPool var2) {
      this.constPool = var2;
      this.classPool = var1;

      try {
         this.STRING_TYPE = this.getType("java.lang.String");
         this.CLASS_TYPE = this.getType("java.lang.Class");
         this.THROWABLE_TYPE = this.getType("java.lang.Throwable");
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public void execute(MethodInfo var1, int var2, CodeIterator var3, Frame var4, Subroutine var5) throws BadBytecode {
      this.lastPos = var2;
      int var6 = var3.byteAt(var2);
      Type var7;
      int var13;
      int var14;
      switch(var6) {
      case 0:
      case 167:
      case 177:
      case 200:
      default:
         break;
      case 1:
         var4.push(Type.UNINIT);
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         var4.push(Type.INTEGER);
         break;
      case 9:
      case 10:
         var4.push(Type.LONG);
         var4.push(Type.TOP);
         break;
      case 11:
      case 12:
      case 13:
         var4.push(Type.FLOAT);
         break;
      case 14:
      case 15:
         var4.push(Type.DOUBLE);
         var4.push(Type.TOP);
         break;
      case 16:
      case 17:
         var4.push(Type.INTEGER);
         break;
      case 18:
         this.evalLDC(var3.byteAt(var2 + 1), var4);
         break;
      case 19:
      case 20:
         this.evalLDC(var3.u16bitAt(var2 + 1), var4);
         break;
      case 21:
         this.evalLoad(Type.INTEGER, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 22:
         this.evalLoad(Type.LONG, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 23:
         this.evalLoad(Type.FLOAT, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 24:
         this.evalLoad(Type.DOUBLE, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 25:
         this.evalLoad(Type.OBJECT, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 26:
      case 27:
      case 28:
      case 29:
         this.evalLoad(Type.INTEGER, var6 - 26, var4, var5);
         break;
      case 30:
      case 31:
      case 32:
      case 33:
         this.evalLoad(Type.LONG, var6 - 30, var4, var5);
         break;
      case 34:
      case 35:
      case 36:
      case 37:
         this.evalLoad(Type.FLOAT, var6 - 34, var4, var5);
         break;
      case 38:
      case 39:
      case 40:
      case 41:
         this.evalLoad(Type.DOUBLE, var6 - 38, var4, var5);
         break;
      case 42:
      case 43:
      case 44:
      case 45:
         this.evalLoad(Type.OBJECT, var6 - 42, var4, var5);
         break;
      case 46:
         this.evalArrayLoad(Type.INTEGER, var4);
         break;
      case 47:
         this.evalArrayLoad(Type.LONG, var4);
         break;
      case 48:
         this.evalArrayLoad(Type.FLOAT, var4);
         break;
      case 49:
         this.evalArrayLoad(Type.DOUBLE, var4);
         break;
      case 50:
         this.evalArrayLoad(Type.OBJECT, var4);
         break;
      case 51:
      case 52:
      case 53:
         this.evalArrayLoad(Type.INTEGER, var4);
         break;
      case 54:
         this.evalStore(Type.INTEGER, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 55:
         this.evalStore(Type.LONG, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 56:
         this.evalStore(Type.FLOAT, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 57:
         this.evalStore(Type.DOUBLE, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 58:
         this.evalStore(Type.OBJECT, var3.byteAt(var2 + 1), var4, var5);
         break;
      case 59:
      case 60:
      case 61:
      case 62:
         this.evalStore(Type.INTEGER, var6 - 59, var4, var5);
         break;
      case 63:
      case 64:
      case 65:
      case 66:
         this.evalStore(Type.LONG, var6 - 63, var4, var5);
         break;
      case 67:
      case 68:
      case 69:
      case 70:
         this.evalStore(Type.FLOAT, var6 - 67, var4, var5);
         break;
      case 71:
      case 72:
      case 73:
      case 74:
         this.evalStore(Type.DOUBLE, var6 - 71, var4, var5);
         break;
      case 75:
      case 76:
      case 77:
      case 78:
         this.evalStore(Type.OBJECT, var6 - 75, var4, var5);
         break;
      case 79:
         this.evalArrayStore(Type.INTEGER, var4);
         break;
      case 80:
         this.evalArrayStore(Type.LONG, var4);
         break;
      case 81:
         this.evalArrayStore(Type.FLOAT, var4);
         break;
      case 82:
         this.evalArrayStore(Type.DOUBLE, var4);
         break;
      case 83:
         this.evalArrayStore(Type.OBJECT, var4);
         break;
      case 84:
      case 85:
      case 86:
         this.evalArrayStore(Type.INTEGER, var4);
         break;
      case 87:
         if (var4.pop() == Type.TOP) {
            throw new BadBytecode("POP can not be used with a category 2 value, pos = " + var2);
         }
         break;
      case 88:
         var4.pop();
         var4.pop();
         break;
      case 89:
         var7 = var4.peek();
         if (var7 == Type.TOP) {
            throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + var2);
         }

         var4.push(var4.peek());
         break;
      case 90:
      case 91:
         var7 = var4.peek();
         if (var7 == Type.TOP) {
            throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + var2);
         }

         var14 = var4.getTopIndex();
         int var15 = var14 - (var6 - 90) - 1;
         var4.push(var7);

         while(var14 > var15) {
            var4.setStack(var14, var4.getStack(var14 - 1));
            --var14;
         }

         var4.setStack(var15, var7);
         break;
      case 92:
         var4.push(var4.getStack(var4.getTopIndex() - 1));
         var4.push(var4.getStack(var4.getTopIndex() - 1));
         break;
      case 93:
      case 94:
         var13 = var4.getTopIndex();
         var14 = var13 - (var6 - 93) - 1;
         Type var9 = var4.getStack(var4.getTopIndex() - 1);
         Type var10 = var4.peek();
         var4.push(var9);
         var4.push(var10);

         while(var13 > var14) {
            var4.setStack(var13, var4.getStack(var13 - 2));
            --var13;
         }

         var4.setStack(var14, var10);
         var4.setStack(var14 - 1, var9);
         break;
      case 95:
         var7 = var4.pop();
         Type var8 = var4.pop();
         if (var7.getSize() != 2 && var8.getSize() != 2) {
            var4.push(var7);
            var4.push(var8);
            break;
         }

         throw new BadBytecode("Swap can not be used with category 2 values, pos = " + var2);
      case 96:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 97:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 98:
         this.evalBinaryMath(Type.FLOAT, var4);
         break;
      case 99:
         this.evalBinaryMath(Type.DOUBLE, var4);
         break;
      case 100:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 101:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 102:
         this.evalBinaryMath(Type.FLOAT, var4);
         break;
      case 103:
         this.evalBinaryMath(Type.DOUBLE, var4);
         break;
      case 104:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 105:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 106:
         this.evalBinaryMath(Type.FLOAT, var4);
         break;
      case 107:
         this.evalBinaryMath(Type.DOUBLE, var4);
         break;
      case 108:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 109:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 110:
         this.evalBinaryMath(Type.FLOAT, var4);
         break;
      case 111:
         this.evalBinaryMath(Type.DOUBLE, var4);
         break;
      case 112:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 113:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 114:
         this.evalBinaryMath(Type.FLOAT, var4);
         break;
      case 115:
         this.evalBinaryMath(Type.DOUBLE, var4);
         break;
      case 116:
         this.verifyAssignable(Type.INTEGER, this.simplePeek(var4));
         break;
      case 117:
         this.verifyAssignable(Type.LONG, this.simplePeek(var4));
         break;
      case 118:
         this.verifyAssignable(Type.FLOAT, this.simplePeek(var4));
         break;
      case 119:
         this.verifyAssignable(Type.DOUBLE, this.simplePeek(var4));
         break;
      case 120:
         this.evalShift(Type.INTEGER, var4);
         break;
      case 121:
         this.evalShift(Type.LONG, var4);
         break;
      case 122:
         this.evalShift(Type.INTEGER, var4);
         break;
      case 123:
         this.evalShift(Type.LONG, var4);
         break;
      case 124:
         this.evalShift(Type.INTEGER, var4);
         break;
      case 125:
         this.evalShift(Type.LONG, var4);
         break;
      case 126:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 127:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 128:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 129:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 130:
         this.evalBinaryMath(Type.INTEGER, var4);
         break;
      case 131:
         this.evalBinaryMath(Type.LONG, var4);
         break;
      case 132:
         var13 = var3.byteAt(var2 + 1);
         this.verifyAssignable(Type.INTEGER, var4.getLocal(var13));
         this.access(var13, Type.INTEGER, var5);
         break;
      case 133:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         this.simplePush(Type.LONG, var4);
         break;
      case 134:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         this.simplePush(Type.FLOAT, var4);
         break;
      case 135:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         this.simplePush(Type.DOUBLE, var4);
         break;
      case 136:
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         this.simplePush(Type.INTEGER, var4);
         break;
      case 137:
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         this.simplePush(Type.FLOAT, var4);
         break;
      case 138:
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         this.simplePush(Type.DOUBLE, var4);
         break;
      case 139:
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         this.simplePush(Type.INTEGER, var4);
         break;
      case 140:
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         this.simplePush(Type.LONG, var4);
         break;
      case 141:
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         this.simplePush(Type.DOUBLE, var4);
         break;
      case 142:
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         this.simplePush(Type.INTEGER, var4);
         break;
      case 143:
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         this.simplePush(Type.LONG, var4);
         break;
      case 144:
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         this.simplePush(Type.FLOAT, var4);
         break;
      case 145:
      case 146:
      case 147:
         this.verifyAssignable(Type.INTEGER, var4.peek());
         break;
      case 148:
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         var4.push(Type.INTEGER);
         break;
      case 149:
      case 150:
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         var4.push(Type.INTEGER);
         break;
      case 151:
      case 152:
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         var4.push(Type.INTEGER);
         break;
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         break;
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         break;
      case 165:
      case 166:
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         break;
      case 168:
         var4.push(Type.RETURN_ADDRESS);
         break;
      case 169:
         this.verifyAssignable(Type.RETURN_ADDRESS, var4.getLocal(var3.byteAt(var2 + 1)));
         break;
      case 170:
      case 171:
      case 172:
         this.verifyAssignable(Type.INTEGER, this.simplePop(var4));
         break;
      case 173:
         this.verifyAssignable(Type.LONG, this.simplePop(var4));
         break;
      case 174:
         this.verifyAssignable(Type.FLOAT, this.simplePop(var4));
         break;
      case 175:
         this.verifyAssignable(Type.DOUBLE, this.simplePop(var4));
         break;
      case 176:
         try {
            CtClass var12 = Descriptor.getReturnType(var1.getDescriptor(), this.classPool);
            this.verifyAssignable(Type.get(var12), this.simplePop(var4));
            break;
         } catch (NotFoundException var11) {
            throw new RuntimeException(var11);
         }
      case 178:
         this.evalGetField(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 179:
         this.evalPutField(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 180:
         this.evalGetField(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 181:
         this.evalPutField(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 182:
      case 183:
      case 184:
         this.evalInvokeMethod(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 185:
         this.evalInvokeIntfMethod(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 186:
         this.evalInvokeDynamic(var6, var3.u16bitAt(var2 + 1), var4);
         break;
      case 187:
         var4.push(this.resolveClassInfo(this.constPool.getClassInfo(var3.u16bitAt(var2 + 1))));
         break;
      case 188:
         this.evalNewArray(var2, var3, var4);
         break;
      case 189:
         this.evalNewObjectArray(var2, var3, var4);
         break;
      case 190:
         var7 = this.simplePop(var4);
         if (!var7.isArray() && var7 != Type.UNINIT) {
            throw new BadBytecode("Array length passed a non-array [pos = " + var2 + "]: " + var7);
         }

         var4.push(Type.INTEGER);
         break;
      case 191:
         this.verifyAssignable(this.THROWABLE_TYPE, this.simplePop(var4));
         break;
      case 192:
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         var4.push(this.typeFromDesc(this.constPool.getClassInfoByDescriptor(var3.u16bitAt(var2 + 1))));
         break;
      case 193:
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         var4.push(Type.INTEGER);
         break;
      case 194:
      case 195:
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         break;
      case 196:
         this.evalWide(var2, var3, var4, var5);
         break;
      case 197:
         this.evalNewObjectArray(var2, var3, var4);
         break;
      case 198:
      case 199:
         this.verifyAssignable(Type.OBJECT, this.simplePop(var4));
         break;
      case 201:
         var4.push(Type.RETURN_ADDRESS);
      }

   }

   private Type zeroExtend(Type var1) {
      return var1 != Type.SHORT && var1 != Type.BYTE && var1 != Type.CHAR && var1 != Type.BOOLEAN ? var1 : Type.INTEGER;
   }

   private void evalArrayLoad(Type var1, Frame var2) throws BadBytecode {
      Type var3 = var2.pop();
      Type var4 = var2.pop();
      if (var4 == Type.UNINIT) {
         this.verifyAssignable(Type.INTEGER, var3);
         if (var1 == Type.OBJECT) {
            this.simplePush(Type.UNINIT, var2);
         } else {
            this.simplePush(var1, var2);
         }

      } else {
         Type var5 = var4.getComponent();
         if (var5 == null) {
            throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + var5);
         } else {
            var5 = this.zeroExtend(var5);
            this.verifyAssignable(var1, var5);
            this.verifyAssignable(Type.INTEGER, var3);
            this.simplePush(var5, var2);
         }
      }
   }

   private void evalArrayStore(Type var1, Frame var2) throws BadBytecode {
      Type var3 = this.simplePop(var2);
      Type var4 = var2.pop();
      Type var5 = var2.pop();
      if (var5 == Type.UNINIT) {
         this.verifyAssignable(Type.INTEGER, var4);
      } else {
         Type var6 = var5.getComponent();
         if (var6 == null) {
            throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + var6);
         } else {
            var6 = this.zeroExtend(var6);
            this.verifyAssignable(var1, var6);
            this.verifyAssignable(Type.INTEGER, var4);
            if (var1 == Type.OBJECT) {
               this.verifyAssignable(var1, var3);
            } else {
               this.verifyAssignable(var6, var3);
            }

         }
      }
   }

   private void evalBinaryMath(Type var1, Frame var2) throws BadBytecode {
      Type var3 = this.simplePop(var2);
      Type var4 = this.simplePop(var2);
      this.verifyAssignable(var1, var3);
      this.verifyAssignable(var1, var4);
      this.simplePush(var4, var2);
   }

   private void evalGetField(int var1, int var2, Frame var3) throws BadBytecode {
      String var4 = this.constPool.getFieldrefType(var2);
      Type var5 = this.zeroExtend(this.typeFromDesc(var4));
      if (var1 == 180) {
         Type var6 = this.resolveClassInfo(this.constPool.getFieldrefClassName(var2));
         this.verifyAssignable(var6, this.simplePop(var3));
      }

      this.simplePush(var5, var3);
   }

   private void evalInvokeIntfMethod(int var1, int var2, Frame var3) throws BadBytecode {
      String var4 = this.constPool.getInterfaceMethodrefType(var2);
      Type[] var5 = this.paramTypesFromDesc(var4);
      int var6 = var5.length;

      while(var6 > 0) {
         --var6;
         this.verifyAssignable(this.zeroExtend(var5[var6]), this.simplePop(var3));
      }

      String var7 = this.constPool.getInterfaceMethodrefClassName(var2);
      Type var8 = this.resolveClassInfo(var7);
      this.verifyAssignable(var8, this.simplePop(var3));
      Type var9 = this.returnTypeFromDesc(var4);
      if (var9 != Type.VOID) {
         this.simplePush(this.zeroExtend(var9), var3);
      }

   }

   private void evalInvokeMethod(int var1, int var2, Frame var3) throws BadBytecode {
      String var4 = this.constPool.getMethodrefType(var2);
      Type[] var5 = this.paramTypesFromDesc(var4);
      int var6 = var5.length;

      while(var6 > 0) {
         --var6;
         this.verifyAssignable(this.zeroExtend(var5[var6]), this.simplePop(var3));
      }

      Type var7;
      if (var1 != 184) {
         var7 = this.resolveClassInfo(this.constPool.getMethodrefClassName(var2));
         this.verifyAssignable(var7, this.simplePop(var3));
      }

      var7 = this.returnTypeFromDesc(var4);
      if (var7 != Type.VOID) {
         this.simplePush(this.zeroExtend(var7), var3);
      }

   }

   private void evalInvokeDynamic(int var1, int var2, Frame var3) throws BadBytecode {
      String var4 = this.constPool.getInvokeDynamicType(var2);
      Type[] var5 = this.paramTypesFromDesc(var4);
      int var6 = var5.length;

      while(var6 > 0) {
         --var6;
         this.verifyAssignable(this.zeroExtend(var5[var6]), this.simplePop(var3));
      }

      Type var7 = this.returnTypeFromDesc(var4);
      if (var7 != Type.VOID) {
         this.simplePush(this.zeroExtend(var7), var3);
      }

   }

   private void evalLDC(int var1, Frame var2) throws BadBytecode {
      int var3 = this.constPool.getTag(var1);
      Type var4;
      switch(var3) {
      case 3:
         var4 = Type.INTEGER;
         break;
      case 4:
         var4 = Type.FLOAT;
         break;
      case 5:
         var4 = Type.LONG;
         break;
      case 6:
         var4 = Type.DOUBLE;
         break;
      case 7:
         var4 = this.CLASS_TYPE;
         break;
      case 8:
         var4 = this.STRING_TYPE;
         break;
      default:
         throw new BadBytecode("bad LDC [pos = " + this.lastPos + "]: " + var3);
      }

      this.simplePush(var4, var2);
   }

   private void evalLoad(Type var1, int var2, Frame var3, Subroutine var4) throws BadBytecode {
      Type var5 = var3.getLocal(var2);
      this.verifyAssignable(var1, var5);
      this.simplePush(var5, var3);
      this.access(var2, var5, var4);
   }

   private void evalNewArray(int var1, CodeIterator var2, Frame var3) throws BadBytecode {
      this.verifyAssignable(Type.INTEGER, this.simplePop(var3));
      Type var4 = null;
      int var5 = var2.byteAt(var1 + 1);
      switch(var5) {
      case 4:
         var4 = this.getType("boolean[]");
         break;
      case 5:
         var4 = this.getType("char[]");
         break;
      case 6:
         var4 = this.getType("float[]");
         break;
      case 7:
         var4 = this.getType("double[]");
         break;
      case 8:
         var4 = this.getType("byte[]");
         break;
      case 9:
         var4 = this.getType("short[]");
         break;
      case 10:
         var4 = this.getType("int[]");
         break;
      case 11:
         var4 = this.getType("long[]");
         break;
      default:
         throw new BadBytecode("Invalid array type [pos = " + var1 + "]: " + var5);
      }

      var3.push(var4);
   }

   private void evalNewObjectArray(int var1, CodeIterator var2, Frame var3) throws BadBytecode {
      Type var4 = this.resolveClassInfo(this.constPool.getClassInfo(var2.u16bitAt(var1 + 1)));
      String var5 = var4.getCtClass().getName();
      int var6 = var2.byteAt(var1);
      int var7;
      if (var6 == 197) {
         var7 = var2.byteAt(var1 + 3);
      } else {
         var5 = var5 + "[]";
         var7 = 1;
      }

      while(var7-- > 0) {
         this.verifyAssignable(Type.INTEGER, this.simplePop(var3));
      }

      this.simplePush(this.getType(var5), var3);
   }

   private void evalPutField(int var1, int var2, Frame var3) throws BadBytecode {
      String var4 = this.constPool.getFieldrefType(var2);
      Type var5 = this.zeroExtend(this.typeFromDesc(var4));
      this.verifyAssignable(var5, this.simplePop(var3));
      if (var1 == 181) {
         Type var6 = this.resolveClassInfo(this.constPool.getFieldrefClassName(var2));
         this.verifyAssignable(var6, this.simplePop(var3));
      }

   }

   private void evalShift(Type var1, Frame var2) throws BadBytecode {
      Type var3 = this.simplePop(var2);
      Type var4 = this.simplePop(var2);
      this.verifyAssignable(Type.INTEGER, var3);
      this.verifyAssignable(var1, var4);
      this.simplePush(var4, var2);
   }

   private void evalStore(Type var1, int var2, Frame var3, Subroutine var4) throws BadBytecode {
      Type var5 = this.simplePop(var3);
      if (var1 != Type.OBJECT || var5 != Type.RETURN_ADDRESS) {
         this.verifyAssignable(var1, var5);
      }

      this.simpleSetLocal(var2, var5, var3);
      this.access(var2, var5, var4);
   }

   private void evalWide(int var1, CodeIterator var2, Frame var3, Subroutine var4) throws BadBytecode {
      int var5 = var2.byteAt(var1 + 1);
      int var6 = var2.u16bitAt(var1 + 2);
      switch(var5) {
      case 21:
         this.evalLoad(Type.INTEGER, var6, var3, var4);
         break;
      case 22:
         this.evalLoad(Type.LONG, var6, var3, var4);
         break;
      case 23:
         this.evalLoad(Type.FLOAT, var6, var3, var4);
         break;
      case 24:
         this.evalLoad(Type.DOUBLE, var6, var3, var4);
         break;
      case 25:
         this.evalLoad(Type.OBJECT, var6, var3, var4);
         break;
      case 54:
         this.evalStore(Type.INTEGER, var6, var3, var4);
         break;
      case 55:
         this.evalStore(Type.LONG, var6, var3, var4);
         break;
      case 56:
         this.evalStore(Type.FLOAT, var6, var3, var4);
         break;
      case 57:
         this.evalStore(Type.DOUBLE, var6, var3, var4);
         break;
      case 58:
         this.evalStore(Type.OBJECT, var6, var3, var4);
         break;
      case 132:
         this.verifyAssignable(Type.INTEGER, var3.getLocal(var6));
         break;
      case 169:
         this.verifyAssignable(Type.RETURN_ADDRESS, var3.getLocal(var6));
         break;
      default:
         throw new BadBytecode("Invalid WIDE operand [pos = " + var1 + "]: " + var5);
      }

   }

   private Type getType(String var1) throws BadBytecode {
      try {
         return Type.get(this.classPool.get(var1));
      } catch (NotFoundException var3) {
         throw new BadBytecode("Could not find class [pos = " + this.lastPos + "]: " + var1);
      }
   }

   private Type[] paramTypesFromDesc(String var1) throws BadBytecode {
      CtClass[] var2 = null;

      try {
         var2 = Descriptor.getParameterTypes(var1, this.classPool);
      } catch (NotFoundException var5) {
         throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + var5.getMessage());
      }

      if (var2 == null) {
         throw new BadBytecode("Could not obtain parameters for descriptor [pos = " + this.lastPos + "]: " + var1);
      } else {
         Type[] var3 = new Type[var2.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = Type.get(var2[var4]);
         }

         return var3;
      }
   }

   private Type returnTypeFromDesc(String var1) throws BadBytecode {
      CtClass var2 = null;

      try {
         var2 = Descriptor.getReturnType(var1, this.classPool);
      } catch (NotFoundException var4) {
         throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + var4.getMessage());
      }

      if (var2 == null) {
         throw new BadBytecode("Could not obtain return type for descriptor [pos = " + this.lastPos + "]: " + var1);
      } else {
         return Type.get(var2);
      }
   }

   private Type simplePeek(Frame var1) {
      Type var2 = var1.peek();
      return var2 == Type.TOP ? var1.getStack(var1.getTopIndex() - 1) : var2;
   }

   private Type simplePop(Frame var1) {
      Type var2 = var1.pop();
      return var2 == Type.TOP ? var1.pop() : var2;
   }

   private void simplePush(Type var1, Frame var2) {
      var2.push(var1);
      if (var1.getSize() == 2) {
         var2.push(Type.TOP);
      }

   }

   private void access(int var1, Type var2, Subroutine var3) {
      if (var3 != null) {
         var3.access(var1);
         if (var2.getSize() == 2) {
            var3.access(var1 + 1);
         }

      }
   }

   private void simpleSetLocal(int var1, Type var2, Frame var3) {
      var3.setLocal(var1, var2);
      if (var2.getSize() == 2) {
         var3.setLocal(var1 + 1, Type.TOP);
      }

   }

   private Type resolveClassInfo(String var1) throws BadBytecode {
      CtClass var2 = null;

      try {
         if (var1.charAt(0) == '[') {
            var2 = Descriptor.toCtClass(var1, this.classPool);
         } else {
            var2 = this.classPool.get(var1);
         }
      } catch (NotFoundException var4) {
         throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + var4.getMessage());
      }

      if (var2 == null) {
         throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + var1);
      } else {
         return Type.get(var2);
      }
   }

   private Type typeFromDesc(String var1) throws BadBytecode {
      CtClass var2 = null;

      try {
         var2 = Descriptor.toCtClass(var1, this.classPool);
      } catch (NotFoundException var4) {
         throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + var4.getMessage());
      }

      if (var2 == null) {
         throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + var1);
      } else {
         return Type.get(var2);
      }
   }

   private void verifyAssignable(Type var1, Type var2) throws BadBytecode {
      if (!var1.isAssignableFrom(var2)) {
         throw new BadBytecode("Expected type: " + var1 + " Got: " + var2 + " [pos = " + this.lastPos + "]");
      }
   }
}
