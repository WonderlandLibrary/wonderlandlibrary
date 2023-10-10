package javassist;

import java.io.DataOutputStream;
import java.io.IOException;
import javassist.bytecode.ClassFile;

class CtNewClass extends CtClassType {
   protected boolean hasConstructor;

   CtNewClass(String var1, ClassPool var2, boolean var3, CtClass var4) {
      super(var1, var2);
      this.wasChanged = true;
      String var5;
      if (!var3 && var4 != null) {
         var5 = var4.getName();
      } else {
         var5 = null;
      }

      this.classfile = new ClassFile(var3, var1, var5);
      if (var3 && var4 != null) {
         this.classfile.setInterfaces(new String[]{var4.getName()});
      }

      this.setModifiers(Modifier.setPublic(this.getModifiers()));
      this.hasConstructor = var3;
   }

   protected void extendToString(StringBuffer var1) {
      if (this.hasConstructor) {
         var1.append("hasConstructor ");
      }

      super.extendToString(var1);
   }

   public void addConstructor(CtConstructor var1) throws CannotCompileException {
      this.hasConstructor = true;
      super.addConstructor(var1);
   }

   public void toBytecode(DataOutputStream var1) throws CannotCompileException, IOException {
      if (!this.hasConstructor) {
         try {
            this.inheritAllConstructors();
            this.hasConstructor = true;
         } catch (NotFoundException var3) {
            throw new CannotCompileException(var3);
         }
      }

      super.toBytecode(var1);
   }

   public void inheritAllConstructors() throws CannotCompileException, NotFoundException {
      CtClass var1 = this.getSuperclass();
      CtConstructor[] var2 = var1.getDeclaredConstructors();
      int var3 = 0;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         CtConstructor var5 = var2[var4];
         int var6 = var5.getModifiers();
         if (var1 != false) {
            CtConstructor var7 = CtNewConstructor.make(var5.getParameterTypes(), var5.getExceptionTypes(), this);
            var7.setModifiers(var6 & 7);
            this.addConstructor(var7);
            ++var3;
         }
      }

      if (var3 < 1) {
         throw new CannotCompileException("no inheritable constructor in " + var1.getName());
      }
   }
}
