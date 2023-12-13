package javassist.convert;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public class TransformReadField extends Transformer {
   protected String fieldname;
   protected CtClass fieldClass;
   protected boolean isPrivate;
   protected String methodClassname;
   protected String methodName;

   public TransformReadField(Transformer var1, CtField var2, String var3, String var4) {
      super(var1);
      this.fieldClass = var2.getDeclaringClass();
      this.fieldname = var2.getName();
      this.methodClassname = var3;
      this.methodName = var4;
      this.isPrivate = Modifier.isPrivate(var2.getModifiers());
   }

   static String isField(ClassPool var0, ConstPool var1, CtClass var2, String var3, boolean var4, int var5) {
      if (!var1.getFieldrefName(var5).equals(var3)) {
         return null;
      } else {
         try {
            CtClass var6 = var0.get(var1.getFieldrefClassName(var5));
            if (var6 == var2 || !var4 && var3 == false) {
               return var1.getFieldrefType(var5);
            }
         } catch (NotFoundException var7) {
         }

         return null;
      }
   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws BadBytecode {
      int var5 = var3.byteAt(var2);
      if (var5 == 180 || var5 == 178) {
         int var6 = var3.u16bitAt(var2 + 1);
         String var7 = isField(var1.getClassPool(), var4, this.fieldClass, this.fieldname, this.isPrivate, var6);
         if (var7 != null) {
            if (var5 == 178) {
               var3.move(var2);
               var2 = var3.insertGap(1);
               var3.writeByte(1, var2);
               var2 = var3.next();
            }

            String var8 = "(Ljava/lang/Object;)" + var7;
            int var9 = var4.addClassInfo(this.methodClassname);
            int var10 = var4.addMethodrefInfo(var9, this.methodName, var8);
            var3.writeByte(184, var2);
            var3.write16bit(var10, var2 + 1);
            return var2;
         }
      }

      return var2;
   }
}
