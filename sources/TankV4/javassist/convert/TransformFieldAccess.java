package javassist.convert;

import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public final class TransformFieldAccess extends Transformer {
   private String newClassname;
   private String newFieldname;
   private String fieldname;
   private CtClass fieldClass;
   private boolean isPrivate;
   private int newIndex;
   private ConstPool constPool;

   public TransformFieldAccess(Transformer var1, CtField var2, String var3, String var4) {
      super(var1);
      this.fieldClass = var2.getDeclaringClass();
      this.fieldname = var2.getName();
      this.isPrivate = Modifier.isPrivate(var2.getModifiers());
      this.newClassname = var3;
      this.newFieldname = var4;
      this.constPool = null;
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
      if (this.constPool != var1) {
         this.newIndex = 0;
      }

   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) {
      int var5 = var3.byteAt(var2);
      if (var5 == 180 || var5 == 178 || var5 == 181 || var5 == 179) {
         int var6 = var3.u16bitAt(var2 + 1);
         String var7 = TransformReadField.isField(var1.getClassPool(), var4, this.fieldClass, this.fieldname, this.isPrivate, var6);
         if (var7 != null) {
            if (this.newIndex == 0) {
               int var8 = var4.addNameAndTypeInfo(this.newFieldname, var7);
               this.newIndex = var4.addFieldrefInfo(var4.addClassInfo(this.newClassname), var8);
               this.constPool = var4;
            }

            var3.write16bit(this.newIndex, var2 + 1);
         }
      }

      return var2;
   }
}
