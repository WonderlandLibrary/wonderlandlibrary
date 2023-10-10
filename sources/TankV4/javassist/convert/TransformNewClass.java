package javassist.convert;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public final class TransformNewClass extends Transformer {
   private int nested;
   private String classname;
   private String newClassName;
   private int newClassIndex;
   private int newMethodNTIndex;
   private int newMethodIndex;

   public TransformNewClass(Transformer var1, String var2, String var3) {
      super(var1);
      this.classname = var2;
      this.newClassName = var3;
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
      this.nested = 0;
      this.newClassIndex = this.newMethodNTIndex = this.newMethodIndex = 0;
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

            if (this.newClassIndex == 0) {
               this.newClassIndex = var4.addClassInfo(this.newClassName);
            }

            var3.write16bit(this.newClassIndex, var2 + 1);
            ++this.nested;
         }
      } else if (var6 == 183) {
         var5 = var3.u16bitAt(var2 + 1);
         int var7 = var4.isConstructor(this.classname, var5);
         if (var7 != 0 && this.nested > 0) {
            int var8 = var4.getMethodrefNameAndType(var5);
            if (this.newMethodNTIndex != var8) {
               this.newMethodNTIndex = var8;
               this.newMethodIndex = var4.addMethodrefInfo(this.newClassIndex, var8);
            }

            var3.write16bit(this.newMethodIndex, var2 + 1);
            --this.nested;
         }
      }

      return var2;
   }
}
