package javassist;

import javassist.bytecode.ClassFile;
import javassist.bytecode.InnerClassesAttribute;

class CtNewNestedClass extends CtNewClass {
   CtNewNestedClass(String var1, ClassPool var2, boolean var3, CtClass var4) {
      super(var1, var2, var3, var4);
   }

   public void setModifiers(int var1) {
      var1 &= -9;
      super.setModifiers(var1);
      updateInnerEntry(var1, this.getName(), this, true);
   }

   private static void updateInnerEntry(int var0, String var1, CtClass var2, boolean var3) {
      ClassFile var4 = var2.getClassFile2();
      InnerClassesAttribute var5 = (InnerClassesAttribute)var4.getAttribute("InnerClasses");
      if (var5 != null) {
         int var6 = var5.tableLength();

         for(int var7 = 0; var7 < var6; ++var7) {
            if (var1.equals(var5.innerClass(var7))) {
               int var8 = var5.accessFlags(var7) & 8;
               var5.setAccessFlags(var7, var0 | var8);
               String var9 = var5.outerClass(var7);
               if (var9 != null && var3) {
                  try {
                     CtClass var10 = var2.getClassPool().get(var9);
                     updateInnerEntry(var0, var1, var10, false);
                  } catch (NotFoundException var11) {
                     throw new RuntimeException("cannot find the declaring class: " + var9);
                  }
               }
               break;
            }
         }

      }
   }
}
