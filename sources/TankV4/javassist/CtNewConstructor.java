package javassist;

import javassist.bytecode.Bytecode;
import javassist.bytecode.ConstPool;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class CtNewConstructor {
   public static final int PASS_NONE = 0;
   public static final int PASS_ARRAY = 1;
   public static final int PASS_PARAMS = 2;

   public static CtConstructor make(String var0, CtClass var1) throws CannotCompileException {
      Javac var2 = new Javac(var1);

      try {
         CtMember var3 = var2.compile(var0);
         if (var3 instanceof CtConstructor) {
            return (CtConstructor)var3;
         }
      } catch (CompileError var4) {
         throw new CannotCompileException(var4);
      }

      throw new CannotCompileException("not a constructor");
   }

   public static CtConstructor make(CtClass[] var0, CtClass[] var1, String var2, CtClass var3) throws CannotCompileException {
      try {
         CtConstructor var4 = new CtConstructor(var0, var3);
         var4.setExceptionTypes(var1);
         var4.setBody(var2);
         return var4;
      } catch (NotFoundException var5) {
         throw new CannotCompileException(var5);
      }
   }

   public static CtConstructor copy(CtConstructor var0, CtClass var1, ClassMap var2) throws CannotCompileException {
      return new CtConstructor(var0, var1, var2);
   }

   public static CtConstructor defaultConstructor(CtClass var0) throws CannotCompileException {
      CtConstructor var1 = new CtConstructor((CtClass[])null, var0);
      ConstPool var2 = var0.getClassFile2().getConstPool();
      Bytecode var3 = new Bytecode(var2, 1, 1);
      var3.addAload(0);

      try {
         var3.addInvokespecial(var0.getSuperclass(), "<init>", "()V");
      } catch (NotFoundException var5) {
         throw new CannotCompileException(var5);
      }

      var3.add(177);
      var1.getMethodInfo2().setCodeAttribute(var3.toCodeAttribute());
      return var1;
   }

   public static CtConstructor skeleton(CtClass[] var0, CtClass[] var1, CtClass var2) throws CannotCompileException {
      return make(var0, var1, 0, (CtMethod)null, (CtMethod.ConstParameter)null, var2);
   }

   public static CtConstructor make(CtClass[] var0, CtClass[] var1, CtClass var2) throws CannotCompileException {
      return make(var0, var1, 2, (CtMethod)null, (CtMethod.ConstParameter)null, var2);
   }

   public static CtConstructor make(CtClass[] var0, CtClass[] var1, int var2, CtMethod var3, CtMethod.ConstParameter var4, CtClass var5) throws CannotCompileException {
      return CtNewWrappedConstructor.wrapped(var0, var1, var2, var3, var4, var5);
   }
}
