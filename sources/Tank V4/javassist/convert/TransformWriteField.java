package javassist.convert;

import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public final class TransformWriteField extends TransformReadField {
   public TransformWriteField(Transformer var1, CtField var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws BadBytecode {
      int var5 = var3.byteAt(var2);
      if (var5 == 181 || var5 == 179) {
         int var6 = var3.u16bitAt(var2 + 1);
         String var7 = isField(var1.getClassPool(), var4, this.fieldClass, this.fieldname, this.isPrivate, var6);
         if (var7 != null) {
            if (var5 == 179) {
               CodeAttribute var8 = var3.get();
               var3.move(var2);
               char var9 = var7.charAt(0);
               if (var9 != 'J' && var9 != 'D') {
                  var2 = var3.insertGap(2);
                  var3.writeByte(1, var2);
                  var3.writeByte(95, var2 + 1);
                  var8.setMaxStack(var8.getMaxStack() + 1);
               } else {
                  var2 = var3.insertGap(3);
                  var3.writeByte(1, var2);
                  var3.writeByte(91, var2 + 1);
                  var3.writeByte(87, var2 + 2);
                  var8.setMaxStack(var8.getMaxStack() + 2);
               }

               var2 = var3.next();
            }

            int var11 = var4.addClassInfo(this.methodClassname);
            String var12 = "(Ljava/lang/Object;" + var7 + ")V";
            int var10 = var4.addMethodrefInfo(var11, this.methodName, var12);
            var3.writeByte(184, var2);
            var3.write16bit(var10, var2 + 1);
         }
      }

      return var2;
   }
}
