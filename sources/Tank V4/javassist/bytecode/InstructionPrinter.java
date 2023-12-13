package javassist.bytecode;

import java.io.PrintStream;
import javassist.CtMethod;

public class InstructionPrinter implements Opcode {
   private static final String[] opcodes;
   private final PrintStream stream;

   public InstructionPrinter(PrintStream var1) {
      this.stream = var1;
   }

   public static void print(CtMethod var0, PrintStream var1) {
      (new InstructionPrinter(var1)).print(var0);
   }

   public void print(CtMethod var1) {
      MethodInfo var2 = var1.getMethodInfo2();
      ConstPool var3 = var2.getConstPool();
      CodeAttribute var4 = var2.getCodeAttribute();
      if (var4 != null) {
         int var6;
         for(CodeIterator var5 = var4.iterator(); var5.hasNext(); this.stream.println(var6 + ": " + instructionString(var5, var6, var3))) {
            try {
               var6 = var5.next();
            } catch (BadBytecode var8) {
               throw new RuntimeException(var8);
            }
         }

      }
   }

   public static String instructionString(CodeIterator var0, int var1, ConstPool var2) {
      int var3 = var0.byteAt(var1);
      if (var3 <= opcodes.length && var3 >= 0) {
         String var4 = opcodes[var3];
         switch(var3) {
         case 16:
            return var4 + " " + var0.byteAt(var1 + 1);
         case 17:
            return var4 + " " + var0.s16bitAt(var1 + 1);
         case 18:
            return var4 + " " + ldc(var2, var0.byteAt(var1 + 1));
         case 19:
         case 20:
            return var4 + " " + ldc(var2, var0.u16bitAt(var1 + 1));
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
            return var4 + " " + var0.byteAt(var1 + 1);
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 119:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 190:
         case 191:
         case 193:
         case 194:
         case 195:
         default:
            return var4;
         case 132:
            return var4 + " " + var0.byteAt(var1 + 1) + ", " + var0.signedByteAt(var1 + 2);
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 198:
         case 199:
            return var4 + " " + (var0.s16bitAt(var1 + 1) + var1);
         case 167:
         case 168:
            return var4 + " " + (var0.s16bitAt(var1 + 1) + var1);
         case 169:
            return var4 + " " + var0.byteAt(var1 + 1);
         case 170:
            return tableSwitch(var0, var1);
         case 171:
            return lookupSwitch(var0, var1);
         case 178:
         case 179:
         case 180:
         case 181:
            return var4 + " " + fieldInfo(var2, var0.u16bitAt(var1 + 1));
         case 182:
         case 183:
         case 184:
            return var4 + " " + methodInfo(var2, var0.u16bitAt(var1 + 1));
         case 185:
            return var4 + " " + interfaceMethodInfo(var2, var0.u16bitAt(var1 + 1));
         case 186:
            return var4 + " " + var0.u16bitAt(var1 + 1);
         case 187:
            return var4 + " " + classInfo(var2, var0.u16bitAt(var1 + 1));
         case 188:
            return var4 + " " + arrayInfo(var0.byteAt(var1 + 1));
         case 189:
         case 192:
            return var4 + " " + classInfo(var2, var0.u16bitAt(var1 + 1));
         case 196:
            return wide(var0, var1);
         case 197:
            return var4 + " " + classInfo(var2, var0.u16bitAt(var1 + 1));
         case 200:
         case 201:
            return var4 + " " + (var0.s32bitAt(var1 + 1) + var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid opcode, opcode: " + var3 + " pos: " + var1);
      }
   }

   private static String wide(CodeIterator var0, int var1) {
      int var2 = var0.byteAt(var1 + 1);
      int var3 = var0.u16bitAt(var1 + 2);
      switch(var2) {
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 132:
      case 169:
         return opcodes[var2] + " " + var3;
      default:
         throw new RuntimeException("Invalid WIDE operand");
      }
   }

   private static String arrayInfo(int var0) {
      switch(var0) {
      case 4:
         return "boolean";
      case 5:
         return "char";
      case 6:
         return "float";
      case 7:
         return "double";
      case 8:
         return "byte";
      case 9:
         return "short";
      case 10:
         return "int";
      case 11:
         return "long";
      default:
         throw new RuntimeException("Invalid array type");
      }
   }

   private static String classInfo(ConstPool var0, int var1) {
      return "#" + var1 + " = Class " + var0.getClassInfo(var1);
   }

   private static String interfaceMethodInfo(ConstPool var0, int var1) {
      return "#" + var1 + " = Method " + var0.getInterfaceMethodrefClassName(var1) + "." + var0.getInterfaceMethodrefName(var1) + "(" + var0.getInterfaceMethodrefType(var1) + ")";
   }

   private static String methodInfo(ConstPool var0, int var1) {
      return "#" + var1 + " = Method " + var0.getMethodrefClassName(var1) + "." + var0.getMethodrefName(var1) + "(" + var0.getMethodrefType(var1) + ")";
   }

   private static String fieldInfo(ConstPool var0, int var1) {
      return "#" + var1 + " = Field " + var0.getFieldrefClassName(var1) + "." + var0.getFieldrefName(var1) + "(" + var0.getFieldrefType(var1) + ")";
   }

   private static String lookupSwitch(CodeIterator var0, int var1) {
      StringBuffer var2 = new StringBuffer("lookupswitch {\n");
      int var3 = (var1 & -4) + 4;
      var2.append("\t\tdefault: ").append(var1 + var0.s32bitAt(var3)).append("\n");
      var3 += 4;
      int var4 = var0.s32bitAt(var3);
      int var10000 = var4 * 8;
      var3 += 4;

      for(int var5 = var10000 + var3; var3 < var5; var3 += 8) {
         int var6 = var0.s32bitAt(var3);
         int var7 = var0.s32bitAt(var3 + 4) + var1;
         var2.append("\t\t").append(var6).append(": ").append(var7).append("\n");
      }

      var2.setCharAt(var2.length() - 1, '}');
      return var2.toString();
   }

   private static String tableSwitch(CodeIterator var0, int var1) {
      StringBuffer var2 = new StringBuffer("tableswitch {\n");
      int var3 = (var1 & -4) + 4;
      var2.append("\t\tdefault: ").append(var1 + var0.s32bitAt(var3)).append("\n");
      var3 += 4;
      int var4 = var0.s32bitAt(var3);
      var3 += 4;
      int var5 = var0.s32bitAt(var3);
      int var10000 = (var5 - var4 + 1) * 4;
      var3 += 4;
      int var6 = var10000 + var3;

      for(int var7 = var4; var3 < var6; ++var7) {
         int var8 = var0.s32bitAt(var3) + var1;
         var2.append("\t\t").append(var7).append(": ").append(var8).append("\n");
         var3 += 4;
      }

      var2.setCharAt(var2.length() - 1, '}');
      return var2.toString();
   }

   private static String ldc(ConstPool var0, int var1) {
      int var2 = var0.getTag(var1);
      switch(var2) {
      case 3:
         return "#" + var1 + " = int " + var0.getIntegerInfo(var1);
      case 4:
         return "#" + var1 + " = float " + var0.getFloatInfo(var1);
      case 5:
         return "#" + var1 + " = long " + var0.getLongInfo(var1);
      case 6:
         return "#" + var1 + " = int " + var0.getDoubleInfo(var1);
      case 7:
         return classInfo(var0, var1);
      case 8:
         return "#" + var1 + " = \"" + var0.getStringInfo(var1) + "\"";
      default:
         throw new RuntimeException("bad LDC: " + var2);
      }
   }

   static {
      opcodes = Mnemonic.OPCODE;
   }
}
