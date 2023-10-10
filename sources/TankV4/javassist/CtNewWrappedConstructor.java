package javassist;

import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

class CtNewWrappedConstructor extends CtNewWrappedMethod {
   private static final int PASS_NONE = 0;
   private static final int PASS_PARAMS = 2;

   public static CtConstructor wrapped(CtClass[] var0, CtClass[] var1, int var2, CtMethod var3, CtMethod.ConstParameter var4, CtClass var5) throws CannotCompileException {
      try {
         CtConstructor var6 = new CtConstructor(var0, var5);
         var6.setExceptionTypes(var1);
         Bytecode var7 = makeBody(var5, var5.getClassFile2(), var2, var3, var0, var4);
         var6.getMethodInfo2().setCodeAttribute(var7.toCodeAttribute());
         return var6;
      } catch (NotFoundException var8) {
         throw new CannotCompileException(var8);
      }
   }

   protected static Bytecode makeBody(CtClass var0, ClassFile var1, int var2, CtMethod var3, CtClass[] var4, CtMethod.ConstParameter var5) throws CannotCompileException {
      int var8 = var1.getSuperclassId();
      Bytecode var9 = new Bytecode(var1.getConstPool(), 0, 0);
      var9.setMaxLocals(false, var4, 0);
      var9.addAload(0);
      int var6;
      int var7;
      if (var2 == 0) {
         var6 = 1;
         var9.addInvokespecial(var8, "<init>", "()V");
      } else if (var2 == 2) {
         var6 = var9.addLoadParameters(var4, 1) + 1;
         var9.addInvokespecial(var8, "<init>", Descriptor.ofConstructor(var4));
      } else {
         var6 = compileParameterList(var9, var4, 1);
         String var10;
         if (var5 == null) {
            var7 = 2;
            var10 = CtMethod.ConstParameter.defaultConstDescriptor();
         } else {
            var7 = var5.compile(var9) + 2;
            var10 = var5.constDescriptor();
         }

         if (var6 < var7) {
            var6 = var7;
         }

         var9.addInvokespecial(var8, "<init>", var10);
      }

      if (var3 == null) {
         var9.add(177);
      } else {
         var7 = makeBody0(var0, var1, var3, false, var4, CtClass.voidType, var5, var9);
         if (var6 < var7) {
            var6 = var7;
         }
      }

      var9.setMaxStack(var6);
      return var9;
   }
}
