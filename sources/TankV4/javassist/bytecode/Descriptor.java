package javassist.bytecode;

import java.util.Map;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

public class Descriptor {
   public static String toJvmName(String var0) {
      return var0.replace('.', '/');
   }

   public static String toJavaName(String var0) {
      return var0.replace('/', '.');
   }

   public static String toJvmName(CtClass var0) {
      return var0.isArray() ? of(var0) : toJvmName(var0.getName());
   }

   public static String toClassName(String var0) {
      int var1 = 0;
      int var2 = 0;

      char var3;
      for(var3 = var0.charAt(0); var3 == '['; var3 = var0.charAt(var2)) {
         ++var1;
         ++var2;
      }

      String var4;
      if (var3 == 'L') {
         int var5 = var0.indexOf(59, var2++);
         var4 = var0.substring(var2, var5).replace('/', '.');
         var2 = var5;
      } else if (var3 == 'V') {
         var4 = "void";
      } else if (var3 == 'I') {
         var4 = "int";
      } else if (var3 == 'B') {
         var4 = "byte";
      } else if (var3 == 'J') {
         var4 = "long";
      } else if (var3 == 'D') {
         var4 = "double";
      } else if (var3 == 'F') {
         var4 = "float";
      } else if (var3 == 'C') {
         var4 = "char";
      } else if (var3 == 'S') {
         var4 = "short";
      } else {
         if (var3 != 'Z') {
            throw new RuntimeException("bad descriptor: " + var0);
         }

         var4 = "boolean";
      }

      if (var2 + 1 != var0.length()) {
         throw new RuntimeException("multiple descriptors?: " + var0);
      } else if (var1 == 0) {
         return var4;
      } else {
         StringBuffer var6 = new StringBuffer(var4);

         do {
            var6.append("[]");
            --var1;
         } while(var1 > 0);

         return var6.toString();
      }
   }

   public static String of(String var0) {
      if (var0.equals("void")) {
         return "V";
      } else if (var0.equals("int")) {
         return "I";
      } else if (var0.equals("byte")) {
         return "B";
      } else if (var0.equals("long")) {
         return "J";
      } else if (var0.equals("double")) {
         return "D";
      } else if (var0.equals("float")) {
         return "F";
      } else if (var0.equals("char")) {
         return "C";
      } else if (var0.equals("short")) {
         return "S";
      } else {
         return var0.equals("boolean") ? "Z" : "L" + toJvmName(var0) + ";";
      }
   }

   public static String rename(String var0, String var1, String var2) {
      if (var0.indexOf(var1) < 0) {
         return var0;
      } else {
         StringBuffer var3 = new StringBuffer();
         int var4 = 0;
         int var5 = 0;

         int var6;
         while(true) {
            var6 = var0.indexOf(76, var5);
            if (var6 < 0) {
               break;
            }

            if (var0.startsWith(var1, var6 + 1) && var0.charAt(var6 + var1.length() + 1) == ';') {
               var3.append(var0.substring(var4, var6));
               var3.append('L');
               var3.append(var2);
               var3.append(';');
               var4 = var5 = var6 + var1.length() + 2;
            } else {
               var5 = var0.indexOf(59, var6) + 1;
               if (var5 < 1) {
                  break;
               }
            }
         }

         if (var4 == 0) {
            return var0;
         } else {
            var6 = var0.length();
            if (var4 < var6) {
               var3.append(var0.substring(var4, var6));
            }

            return var3.toString();
         }
      }
   }

   public static String rename(String var0, Map var1) {
      if (var1 == null) {
         return var0;
      } else {
         StringBuffer var2 = new StringBuffer();
         int var3 = 0;
         int var4 = 0;

         int var5;
         while(true) {
            var5 = var0.indexOf(76, var4);
            if (var5 < 0) {
               break;
            }

            int var6 = var0.indexOf(59, var5);
            if (var6 < 0) {
               break;
            }

            var4 = var6 + 1;
            String var7 = var0.substring(var5 + 1, var6);
            String var8 = (String)var1.get(var7);
            if (var8 != null) {
               var2.append(var0.substring(var3, var5));
               var2.append('L');
               var2.append(var8);
               var2.append(';');
               var3 = var4;
            }
         }

         if (var3 == 0) {
            return var0;
         } else {
            var5 = var0.length();
            if (var3 < var5) {
               var2.append(var0.substring(var3, var5));
            }

            return var2.toString();
         }
      }
   }

   public static String of(CtClass var0) {
      StringBuffer var1 = new StringBuffer();
      toDescriptor(var1, var0);
      return var1.toString();
   }

   private static void toDescriptor(StringBuffer var0, CtClass var1) {
      if (var1.isArray()) {
         var0.append('[');

         try {
            toDescriptor(var0, var1.getComponentType());
         } catch (NotFoundException var4) {
            var0.append('L');
            String var3 = var1.getName();
            var0.append(toJvmName(var3.substring(0, var3.length() - 2)));
            var0.append(';');
         }
      } else if (var1.isPrimitive()) {
         CtPrimitiveType var2 = (CtPrimitiveType)var1;
         var0.append(var2.getDescriptor());
      } else {
         var0.append('L');
         var0.append(var1.getName().replace('.', '/'));
         var0.append(';');
      }

   }

   public static String ofConstructor(CtClass[] var0) {
      return ofMethod(CtClass.voidType, var0);
   }

   public static String ofMethod(CtClass var0, CtClass[] var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append('(');
      if (var1 != null) {
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            toDescriptor(var2, var1[var4]);
         }
      }

      var2.append(')');
      if (var0 != null) {
         toDescriptor(var2, var0);
      }

      return var2.toString();
   }

   public static String ofParameters(CtClass[] var0) {
      return ofMethod((CtClass)null, var0);
   }

   public static String appendParameter(String var0, String var1) {
      int var2 = var1.indexOf(41);
      if (var2 < 0) {
         return var1;
      } else {
         StringBuffer var3 = new StringBuffer();
         var3.append(var1.substring(0, var2));
         var3.append('L');
         var3.append(var0.replace('.', '/'));
         var3.append(';');
         var3.append(var1.substring(var2));
         return var3.toString();
      }
   }

   public static String insertParameter(String var0, String var1) {
      return var1.charAt(0) != '(' ? var1 : "(L" + var0.replace('.', '/') + ';' + var1.substring(1);
   }

   public static String appendParameter(CtClass var0, String var1) {
      int var2 = var1.indexOf(41);
      if (var2 < 0) {
         return var1;
      } else {
         StringBuffer var3 = new StringBuffer();
         var3.append(var1.substring(0, var2));
         toDescriptor(var3, var0);
         var3.append(var1.substring(var2));
         return var3.toString();
      }
   }

   public static String insertParameter(CtClass var0, String var1) {
      return var1.charAt(0) != '(' ? var1 : "(" + of(var0) + var1.substring(1);
   }

   public static String changeReturnType(String var0, String var1) {
      int var2 = var1.indexOf(41);
      if (var2 < 0) {
         return var1;
      } else {
         StringBuffer var3 = new StringBuffer();
         var3.append(var1.substring(0, var2 + 1));
         var3.append('L');
         var3.append(var0.replace('.', '/'));
         var3.append(';');
         return var3.toString();
      }
   }

   public static CtClass[] getParameterTypes(String var0, ClassPool var1) throws NotFoundException {
      if (var0.charAt(0) != '(') {
         return null;
      } else {
         int var2 = numOfParameters(var0);
         CtClass[] var3 = new CtClass[var2];
         int var4 = 0;
         int var5 = 1;

         do {
            var5 = toCtClass(var1, var0, var5, var3, var4++);
         } while(var5 > 0);

         return var3;
      }
   }

   public static boolean eqParamTypes(String var0, String var1) {
      if (var0.charAt(0) != '(') {
         return false;
      } else {
         int var2 = 0;

         while(true) {
            char var3 = var0.charAt(var2);
            if (var3 != var1.charAt(var2)) {
               return false;
            }

            if (var3 == ')') {
               return true;
            }

            ++var2;
         }
      }
   }

   public static String getParamDescriptor(String var0) {
      return var0.substring(0, var0.indexOf(41) + 1);
   }

   public static CtClass getReturnType(String var0, ClassPool var1) throws NotFoundException {
      int var2 = var0.indexOf(41);
      if (var2 < 0) {
         return null;
      } else {
         CtClass[] var3 = new CtClass[1];
         toCtClass(var1, var0, var2 + 1, var3, 0);
         return var3[0];
      }
   }

   public static int numOfParameters(String var0) {
      int var1 = 0;
      int var2 = 1;

      while(true) {
         char var3 = var0.charAt(var2);
         if (var3 == ')') {
            return var1;
         }

         while(var3 == '[') {
            ++var2;
            var3 = var0.charAt(var2);
         }

         if (var3 == 'L') {
            var2 = var0.indexOf(59, var2) + 1;
            if (var2 <= 0) {
               throw new IndexOutOfBoundsException("bad descriptor");
            }
         } else {
            ++var2;
         }

         ++var1;
      }
   }

   public static CtClass toCtClass(String var0, ClassPool var1) throws NotFoundException {
      CtClass[] var2 = new CtClass[1];
      int var3 = toCtClass(var1, var0, 0, var2, 0);
      return var3 >= 0 ? var2[0] : var1.get(var0.replace('/', '.'));
   }

   private static int toCtClass(ClassPool var0, String var1, int var2, CtClass[] var3, int var4) throws NotFoundException {
      int var7 = 0;

      char var8;
      for(var8 = var1.charAt(var2); var8 == '['; var8 = var1.charAt(var2)) {
         ++var7;
         ++var2;
      }

      int var5;
      String var6;
      if (var8 == 'L') {
         ++var2;
         var5 = var1.indexOf(59, var2);
         var6 = var1.substring(var2, var5++).replace('/', '.');
      } else {
         CtClass var9 = toPrimitiveClass(var8);
         if (var9 == null) {
            return -1;
         }

         var5 = var2 + 1;
         if (var7 == 0) {
            var3[var4] = var9;
            return var5;
         }

         var6 = var9.getName();
      }

      if (var7 > 0) {
         StringBuffer var10 = new StringBuffer(var6);

         while(var7-- > 0) {
            var10.append("[]");
         }

         var6 = var10.toString();
      }

      var3[var4] = var0.get(var6);
      return var5;
   }

   static CtClass toPrimitiveClass(char var0) {
      CtClass var1 = null;
      switch(var0) {
      case 'B':
         var1 = CtClass.byteType;
         break;
      case 'C':
         var1 = CtClass.charType;
         break;
      case 'D':
         var1 = CtClass.doubleType;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'W':
      case 'X':
      case 'Y':
      default:
         break;
      case 'F':
         var1 = CtClass.floatType;
         break;
      case 'I':
         var1 = CtClass.intType;
         break;
      case 'J':
         var1 = CtClass.longType;
         break;
      case 'S':
         var1 = CtClass.shortType;
         break;
      case 'V':
         var1 = CtClass.voidType;
         break;
      case 'Z':
         var1 = CtClass.booleanType;
      }

      return var1;
   }

   public static int arrayDimension(String var0) {
      int var1;
      for(var1 = 0; var0.charAt(var1) == '['; ++var1) {
      }

      return var1;
   }

   public static String toArrayComponent(String var0, int var1) {
      return var0.substring(var1);
   }

   public static int dataSize(String var0) {
      return dataSize(var0, true);
   }

   public static int paramSize(String var0) {
      return -dataSize(var0, false);
   }

   private static int dataSize(String var0, boolean var1) {
      int var2 = 0;
      char var3 = var0.charAt(0);
      if (var3 == '(') {
         int var4 = 1;

         label55:
         while(true) {
            while(true) {
               var3 = var0.charAt(var4);
               if (var3 == ')') {
                  var3 = var0.charAt(var4 + 1);
                  break label55;
               }

               boolean var5;
               for(var5 = false; var3 == '['; var3 = var0.charAt(var4)) {
                  var5 = true;
                  ++var4;
               }

               if (var3 == 'L') {
                  var4 = var0.indexOf(59, var4) + 1;
                  if (var4 <= 0) {
                     throw new IndexOutOfBoundsException("bad descriptor");
                  }
               } else {
                  ++var4;
               }

               if (!var5 && (var3 == 'J' || var3 == 'D')) {
                  var2 -= 2;
               } else {
                  --var2;
               }
            }
         }
      }

      if (var1) {
         if (var3 != 'J' && var3 != 'D') {
            if (var3 != 'V') {
               ++var2;
            }
         } else {
            var2 += 2;
         }
      }

      return var2;
   }

   public static String toString(String var0) {
      return Descriptor.PrettyPrinter.toString(var0);
   }

   public static class Iterator {
      private String desc;
      private int index;
      private int curPos;
      private boolean param;

      public Iterator(String var1) {
         this.desc = var1;
         this.index = this.curPos = 0;
         this.param = false;
      }

      public boolean hasNext() {
         return this.index < this.desc.length();
      }

      public boolean isParameter() {
         return this.param;
      }

      public char currentChar() {
         return this.desc.charAt(this.curPos);
      }

      public boolean is2byte() {
         char var1 = this.currentChar();
         return var1 == 'D' || var1 == 'J';
      }

      public int next() {
         int var1 = this.index;
         char var2 = this.desc.charAt(var1);
         if (var2 == '(') {
            ++this.index;
            ++var1;
            var2 = this.desc.charAt(var1);
            this.param = true;
         }

         if (var2 == ')') {
            ++this.index;
            ++var1;
            var2 = this.desc.charAt(var1);
            this.param = false;
         }

         while(var2 == '[') {
            ++var1;
            var2 = this.desc.charAt(var1);
         }

         if (var2 == 'L') {
            var1 = this.desc.indexOf(59, var1) + 1;
            if (var1 <= 0) {
               throw new IndexOutOfBoundsException("bad descriptor");
            }
         } else {
            ++var1;
         }

         this.curPos = this.index;
         this.index = var1;
         return this.curPos;
      }
   }

   static class PrettyPrinter {
      static String toString(String var0) {
         StringBuffer var1 = new StringBuffer();
         if (var0.charAt(0) == '(') {
            int var2 = 1;
            var1.append('(');

            for(; var0.charAt(var2) != ')'; var2 = readType(var1, var2, var0)) {
               if (var2 > 1) {
                  var1.append(',');
               }
            }

            var1.append(')');
         } else {
            readType(var1, 0, var0);
         }

         return var1.toString();
      }

      static int readType(StringBuffer var0, int var1, String var2) {
         char var3 = var2.charAt(var1);

         int var4;
         for(var4 = 0; var3 == '['; var3 = var2.charAt(var1)) {
            ++var4;
            ++var1;
         }

         if (var3 == 'L') {
            while(true) {
               ++var1;
               var3 = var2.charAt(var1);
               if (var3 == ';') {
                  break;
               }

               if (var3 == '/') {
                  var3 = '.';
               }

               var0.append(var3);
            }
         } else {
            CtClass var5 = Descriptor.toPrimitiveClass(var3);
            var0.append(var5.getName());
         }

         while(var4-- > 0) {
            var0.append("[]");
         }

         return var1 + 1;
      }
   }
}
