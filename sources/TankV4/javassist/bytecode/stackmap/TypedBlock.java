package javassist.bytecode.stackmap;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;

public class TypedBlock extends BasicBlock {
   public int stackTop;
   public int numLocals;
   public TypeData[] localsTypes = null;
   public TypeData[] stackTypes;

   public static TypedBlock[] makeBlocks(MethodInfo var0, CodeAttribute var1, boolean var2) throws BadBytecode {
      TypedBlock[] var3 = (TypedBlock[])((TypedBlock[])(new TypedBlock.Maker()).make(var0));
      if (!var2 || var3.length >= 2 || var3.length != 0 && var3[0].incoming != 0) {
         ConstPool var4 = var0.getConstPool();
         boolean var5 = (var0.getAccessFlags() & 8) != 0;
         var3[0].initFirstBlock(var1.getMaxStack(), var1.getMaxLocals(), var4.getClassName(), var0.getDescriptor(), var5, var0.isConstructor());
         return var3;
      } else {
         return null;
      }
   }

   protected TypedBlock(int var1) {
      super(var1);
   }

   protected void toString2(StringBuffer var1) {
      super.toString2(var1);
      var1.append(",\n stack={");
      this.printTypes(var1, this.stackTop, this.stackTypes);
      var1.append("}, locals={");
      this.printTypes(var1, this.numLocals, this.localsTypes);
      var1.append('}');
   }

   private void printTypes(StringBuffer var1, int var2, TypeData[] var3) {
      if (var3 != null) {
         for(int var4 = 0; var4 < var2; ++var4) {
            if (var4 > 0) {
               var1.append(", ");
            }

            TypeData var5 = var3[var4];
            var1.append(var5 == null ? "<>" : var5.toString());
         }

      }
   }

   public boolean alreadySet() {
      return this.localsTypes != null;
   }

   public void setStackMap(int var1, TypeData[] var2, int var3, TypeData[] var4) throws BadBytecode {
      this.stackTop = var1;
      this.stackTypes = var2;
      this.numLocals = var3;
      this.localsTypes = var4;
   }

   public void resetNumLocals() {
      if (this.localsTypes != null) {
         int var1;
         for(var1 = this.localsTypes.length; var1 > 0 && this.localsTypes[var1 - 1].isBasicType() == TypeTag.TOP && (var1 <= 1 || !this.localsTypes[var1 - 2].is2WordType()); --var1) {
         }

         this.numLocals = var1;
      }

   }

   void initFirstBlock(int var1, int var2, String var3, String var4, boolean var5, boolean var6) throws BadBytecode {
      if (var4.charAt(0) != '(') {
         throw new BadBytecode("no method descriptor: " + var4);
      } else {
         this.stackTop = 0;
         this.stackTypes = TypeData.make(var1);
         TypeData[] var7 = TypeData.make(var2);
         if (var6) {
            var7[0] = new TypeData.UninitThis(var3);
         } else if (!var5) {
            var7[0] = new TypeData.ClassName(var3);
         }

         int var8 = var5 ? -1 : 0;
         int var9 = 1;

         try {
            while(true) {
               ++var8;
               if ((var9 = descToTag(var4, var9, var8, var7)) <= 0) {
                  break;
               }

               if (var7[var8].is2WordType()) {
                  ++var8;
                  var7[var8] = TypeTag.TOP;
               }
            }
         } catch (StringIndexOutOfBoundsException var11) {
            throw new BadBytecode("bad method descriptor: " + var4);
         }

         this.numLocals = var8;
         this.localsTypes = var7;
      }
   }

   private static int descToTag(String var0, int var1, int var2, TypeData[] var3) throws BadBytecode {
      int var4 = var1;
      int var5 = 0;
      char var6 = var0.charAt(var1);
      if (var6 == ')') {
         return 0;
      } else {
         while(var6 == '[') {
            ++var5;
            ++var1;
            var6 = var0.charAt(var1);
         }

         if (var6 == 'L') {
            ++var1;
            int var8 = var0.indexOf(59, var1);
            if (var5 > 0) {
               ++var8;
               var3[var2] = new TypeData.ClassName(var0.substring(var4, var8));
            } else {
               int var10005 = var4 + 1;
               ++var8;
               var3[var2] = new TypeData.ClassName(var0.substring(var10005, var8 - 1).replace('/', '.'));
            }

            return var8;
         } else if (var5 > 0) {
            var3[var2] = new TypeData.ClassName(var0.substring(var1++, var1));
            return var1;
         } else {
            TypeData var7 = toPrimitiveTag(var6);
            if (var7 == null) {
               throw new BadBytecode("bad method descriptor: " + var0);
            } else {
               var3[var2] = var7;
               return var1 + 1;
            }
         }
      }
   }

   private static TypeData toPrimitiveTag(char var0) {
      switch(var0) {
      case 'B':
      case 'C':
      case 'I':
      case 'S':
      case 'Z':
         return TypeTag.INTEGER;
      case 'D':
         return TypeTag.DOUBLE;
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
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      default:
         return null;
      case 'F':
         return TypeTag.FLOAT;
      case 'J':
         return TypeTag.LONG;
      }
   }

   public static String getRetType(String var0) {
      int var1 = var0.indexOf(41);
      if (var1 < 0) {
         return "java.lang.Object";
      } else {
         char var2 = var0.charAt(var1 + 1);
         if (var2 == '[') {
            return var0.substring(var1 + 1);
         } else {
            return var2 == 'L' ? var0.substring(var1 + 2, var0.length() - 1).replace('/', '.') : "java.lang.Object";
         }
      }
   }

   public static class Maker extends BasicBlock.Maker {
      protected BasicBlock makeBlock(int var1) {
         return new TypedBlock(var1);
      }

      protected BasicBlock[] makeArray(int var1) {
         return new TypedBlock[var1];
      }
   }
}
