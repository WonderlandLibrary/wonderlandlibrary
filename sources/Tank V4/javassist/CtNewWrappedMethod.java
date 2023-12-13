package javassist;

import java.util.Hashtable;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.SyntheticAttribute;
import javassist.compiler.JvstCodeGen;

class CtNewWrappedMethod {
   private static final String addedWrappedMethod = "_added_m$";

   public static CtMethod wrapped(CtClass var0, String var1, CtClass[] var2, CtClass[] var3, CtMethod var4, CtMethod.ConstParameter var5, CtClass var6) throws CannotCompileException {
      CtMethod var7 = new CtMethod(var0, var1, var2, var6);
      var7.setModifiers(var4.getModifiers());

      try {
         var7.setExceptionTypes(var3);
      } catch (NotFoundException var10) {
         throw new CannotCompileException(var10);
      }

      Bytecode var8 = makeBody(var6, var6.getClassFile2(), var4, var2, var0, var5);
      MethodInfo var9 = var7.getMethodInfo2();
      var9.setCodeAttribute(var8.toCodeAttribute());
      return var7;
   }

   static Bytecode makeBody(CtClass var0, ClassFile var1, CtMethod var2, CtClass[] var3, CtClass var4, CtMethod.ConstParameter var5) throws CannotCompileException {
      boolean var6 = Modifier.isStatic(var2.getModifiers());
      Bytecode var7 = new Bytecode(var1.getConstPool(), 0, 0);
      int var8 = makeBody0(var0, var1, var2, var6, var3, var4, var5, var7);
      var7.setMaxStack(var8);
      var7.setMaxLocals(var6, var3, 0);
      return var7;
   }

   protected static int makeBody0(CtClass var0, ClassFile var1, CtMethod var2, boolean var3, CtClass[] var4, CtClass var5, CtMethod.ConstParameter var6, Bytecode var7) throws CannotCompileException {
      if (!(var0 instanceof CtClassType)) {
         throw new CannotCompileException("bad declaring class" + var0.getName());
      } else {
         if (!var3) {
            var7.addAload(0);
         }

         int var8 = compileParameterList(var7, var4, var3 ? 0 : 1);
         int var9;
         String var10;
         if (var6 == null) {
            var9 = 0;
            var10 = CtMethod.ConstParameter.defaultDescriptor();
         } else {
            var9 = var6.compile(var7);
            var10 = var6.descriptor();
         }

         checkSignature(var2, var10);

         String var11;
         try {
            var11 = addBodyMethod((CtClassType)var0, var1, var2);
         } catch (BadBytecode var13) {
            throw new CannotCompileException(var13);
         }

         if (var3) {
            var7.addInvokestatic(Bytecode.THIS, var11, var10);
         } else {
            var7.addInvokespecial(Bytecode.THIS, var11, var10);
         }

         compileReturn(var7, var5);
         if (var8 < var9 + 2) {
            var8 = var9 + 2;
         }

         return var8;
      }
   }

   private static void checkSignature(CtMethod var0, String var1) throws CannotCompileException {
      if (!var1.equals(var0.getMethodInfo2().getDescriptor())) {
         throw new CannotCompileException("wrapped method with a bad signature: " + var0.getDeclaringClass().getName() + '.' + var0.getName());
      }
   }

   private static String addBodyMethod(CtClassType var0, ClassFile var1, CtMethod var2) throws BadBytecode, CannotCompileException {
      Hashtable var3 = var0.getHiddenMethods();
      String var4 = (String)var3.get(var2);
      if (var4 == null) {
         do {
            var4 = "_added_m$" + var0.getUniqueNumber();
         } while(var1.getMethod(var4) != null);

         ClassMap var5 = new ClassMap();
         var5.put(var2.getDeclaringClass().getName(), var0.getName());
         MethodInfo var6 = new MethodInfo(var1.getConstPool(), var4, var2.getMethodInfo2(), var5);
         int var7 = var6.getAccessFlags();
         var6.setAccessFlags(AccessFlag.setPrivate(var7));
         var6.addAttribute(new SyntheticAttribute(var1.getConstPool()));
         var1.addMethod(var6);
         var3.put(var2, var4);
         CtMember.Cache var8 = var0.hasMemberCache();
         if (var8 != null) {
            var8.addMethod(new CtMethod(var6, var0));
         }
      }

      return var4;
   }

   static int compileParameterList(Bytecode var0, CtClass[] var1, int var2) {
      return JvstCodeGen.compileParameterList(var0, var1, var2);
   }

   private static void compileReturn(Bytecode var0, CtClass var1) {
      if (var1.isPrimitive()) {
         CtPrimitiveType var2 = (CtPrimitiveType)var1;
         if (var2 != CtClass.voidType) {
            String var3 = var2.getWrapperName();
            var0.addCheckcast(var3);
            var0.addInvokevirtual(var3, var2.getGetMethodName(), var2.getGetMethodDescriptor());
         }

         var0.addOpcode(var2.getReturnOp());
      } else {
         var0.addCheckcast(var1);
         var0.addOpcode(176);
      }

   }
}
