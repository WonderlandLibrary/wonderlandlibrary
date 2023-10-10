package javassist;

import java.util.Map;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class CtNewMethod {
   public static CtMethod make(String var0, CtClass var1) throws CannotCompileException {
      return make(var0, var1, (String)null, (String)null);
   }

   public static CtMethod make(String var0, CtClass var1, String var2, String var3) throws CannotCompileException {
      Javac var4 = new Javac(var1);

      try {
         if (var3 != null) {
            var4.recordProceed(var2, var3);
         }

         CtMember var5 = var4.compile(var0);
         if (var5 instanceof CtMethod) {
            return (CtMethod)var5;
         }
      } catch (CompileError var6) {
         throw new CannotCompileException(var6);
      }

      throw new CannotCompileException("not a method");
   }

   public static CtMethod make(CtClass var0, String var1, CtClass[] var2, CtClass[] var3, String var4, CtClass var5) throws CannotCompileException {
      return make(1, var0, var1, var2, var3, var4, var5);
   }

   public static CtMethod make(int var0, CtClass var1, String var2, CtClass[] var3, CtClass[] var4, String var5, CtClass var6) throws CannotCompileException {
      try {
         CtMethod var7 = new CtMethod(var1, var2, var3, var6);
         var7.setModifiers(var0);
         var7.setExceptionTypes(var4);
         var7.setBody(var5);
         return var7;
      } catch (NotFoundException var8) {
         throw new CannotCompileException(var8);
      }
   }

   public static CtMethod copy(CtMethod var0, CtClass var1, ClassMap var2) throws CannotCompileException {
      return new CtMethod(var0, var1, var2);
   }

   public static CtMethod copy(CtMethod var0, String var1, CtClass var2, ClassMap var3) throws CannotCompileException {
      CtMethod var4 = new CtMethod(var0, var2, var3);
      var4.setName(var1);
      return var4;
   }

   public static CtMethod abstractMethod(CtClass var0, String var1, CtClass[] var2, CtClass[] var3, CtClass var4) throws NotFoundException {
      CtMethod var5 = new CtMethod(var0, var1, var2, var4);
      var5.setExceptionTypes(var3);
      return var5;
   }

   public static CtMethod getter(String var0, CtField var1) throws CannotCompileException {
      FieldInfo var2 = var1.getFieldInfo2();
      String var3 = var2.getDescriptor();
      String var4 = "()" + var3;
      ConstPool var5 = var2.getConstPool();
      MethodInfo var6 = new MethodInfo(var5, var0, var4);
      var6.setAccessFlags(1);
      Bytecode var7 = new Bytecode(var5, 2, 1);

      try {
         String var8 = var2.getName();
         if ((var2.getAccessFlags() & 8) == 0) {
            var7.addAload(0);
            var7.addGetfield(Bytecode.THIS, var8, var3);
         } else {
            var7.addGetstatic(Bytecode.THIS, var8, var3);
         }

         var7.addReturn(var1.getType());
      } catch (NotFoundException var9) {
         throw new CannotCompileException(var9);
      }

      var6.setCodeAttribute(var7.toCodeAttribute());
      CtClass var10 = var1.getDeclaringClass();
      return new CtMethod(var6, var10);
   }

   public static CtMethod setter(String var0, CtField var1) throws CannotCompileException {
      FieldInfo var2 = var1.getFieldInfo2();
      String var3 = var2.getDescriptor();
      String var4 = "(" + var3 + ")V";
      ConstPool var5 = var2.getConstPool();
      MethodInfo var6 = new MethodInfo(var5, var0, var4);
      var6.setAccessFlags(1);
      Bytecode var7 = new Bytecode(var5, 3, 3);

      try {
         String var8 = var2.getName();
         if ((var2.getAccessFlags() & 8) == 0) {
            var7.addAload(0);
            var7.addLoad(1, var1.getType());
            var7.addPutfield(Bytecode.THIS, var8, var3);
         } else {
            var7.addLoad(1, var1.getType());
            var7.addPutstatic(Bytecode.THIS, var8, var3);
         }

         var7.addReturn((CtClass)null);
      } catch (NotFoundException var9) {
         throw new CannotCompileException(var9);
      }

      var6.setCodeAttribute(var7.toCodeAttribute());
      CtClass var10 = var1.getDeclaringClass();
      return new CtMethod(var6, var10);
   }

   public static CtMethod delegator(CtMethod var0, CtClass var1) throws CannotCompileException {
      try {
         return delegator0(var0, var1);
      } catch (NotFoundException var3) {
         throw new CannotCompileException(var3);
      }
   }

   private static CtMethod delegator0(CtMethod var0, CtClass var1) throws CannotCompileException, NotFoundException {
      MethodInfo var2 = var0.getMethodInfo2();
      String var3 = var2.getName();
      String var4 = var2.getDescriptor();
      ConstPool var5 = var1.getClassFile2().getConstPool();
      MethodInfo var6 = new MethodInfo(var5, var3, var4);
      var6.setAccessFlags(var2.getAccessFlags());
      ExceptionsAttribute var7 = var2.getExceptionsAttribute();
      if (var7 != null) {
         var6.setExceptionsAttribute((ExceptionsAttribute)var7.copy(var5, (Map)null));
      }

      Bytecode var8 = new Bytecode(var5, 0, 0);
      boolean var9 = Modifier.isStatic(var0.getModifiers());
      CtClass var10 = var0.getDeclaringClass();
      CtClass[] var11 = var0.getParameterTypes();
      int var12;
      if (var9) {
         var12 = var8.addLoadParameters(var11, 0);
         var8.addInvokestatic(var10, var3, var4);
      } else {
         var8.addLoad(0, var10);
         var12 = var8.addLoadParameters(var11, 1);
         var8.addInvokespecial(var10, var3, var4);
      }

      var8.addReturn(var0.getReturnType());
      ++var12;
      var8.setMaxLocals(var12);
      var8.setMaxStack(var12 < 2 ? 2 : var12);
      var6.setCodeAttribute(var8.toCodeAttribute());
      return new CtMethod(var6, var1);
   }

   public static CtMethod wrapped(CtClass var0, String var1, CtClass[] var2, CtClass[] var3, CtMethod var4, CtMethod.ConstParameter var5, CtClass var6) throws CannotCompileException {
      return CtNewWrappedMethod.wrapped(var0, var1, var2, var3, var4, var5, var6);
   }
}
