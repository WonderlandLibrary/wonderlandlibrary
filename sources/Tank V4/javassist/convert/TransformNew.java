package javassist.convert;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;

public final class TransformNew extends Transformer {
   private int nested;
   private String classname;
   private String trapClass;
   private String trapMethod;

   public TransformNew(Transformer var1, String var2, String var3, String var4) {
      super(var1);
      this.classname = var2;
      this.trapClass = var3;
      this.trapMethod = var4;
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
      this.nested = 0;
   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws CannotCompileException {
      int var6 = var3.byteAt(var2);
      int var5;
      if (var6 == 187) {
         var5 = var3.u16bitAt(var2 + 1);
         if (var4.getClassInfo(var5).equals(this.classname)) {
            if (var3.byteAt(var2 + 3) != 89) {
               throw new CannotCompileException("NEW followed by no DUP was found");
            }

            var3.writeByte(0, var2);
            var3.writeByte(0, var2 + 1);
            var3.writeByte(0, var2 + 2);
            var3.writeByte(0, var2 + 3);
            ++this.nested;
            StackMapTable var7 = (StackMapTable)var3.get().getAttribute("StackMapTable");
            if (var7 != null) {
               var7.removeNew(var2);
            }

            StackMap var8 = (StackMap)var3.get().getAttribute("StackMap");
            if (var8 != null) {
               var8.removeNew(var2);
            }
         }
      } else if (var6 == 183) {
         var5 = var3.u16bitAt(var2 + 1);
         int var9 = var4.isConstructor(this.classname, var5);
         if (var9 != 0 && this.nested > 0) {
            int var10 = this.computeMethodref(var9, var4);
            var3.writeByte(184, var2);
            var3.write16bit(var10, var2 + 1);
            --this.nested;
         }
      }

      return var2;
   }

   private int computeMethodref(int var1, ConstPool var2) {
      int var3 = var2.addClassInfo(this.trapClass);
      int var4 = var2.addUtf8Info(this.trapMethod);
      var1 = var2.addUtf8Info(Descriptor.changeReturnType(this.classname, var2.getUtf8Info(var1)));
      return var2.addMethodrefInfo(var3, var2.addNameAndTypeInfo(var4, var1));
   }
}
