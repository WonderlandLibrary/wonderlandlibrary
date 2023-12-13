package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public final class CtMethod extends CtBehavior {
   protected String cachedStringRep;

   CtMethod(MethodInfo var1, CtClass var2) {
      super(var2, var1);
      this.cachedStringRep = null;
   }

   public CtMethod(CtClass var1, String var2, CtClass[] var3, CtClass var4) {
      this((MethodInfo)null, var4);
      ConstPool var5 = var4.getClassFile2().getConstPool();
      String var6 = Descriptor.ofMethod(var1, var3);
      this.methodInfo = new MethodInfo(var5, var2, var6);
      this.setModifiers(1025);
   }

   public CtMethod(CtMethod var1, CtClass var2, ClassMap var3) throws CannotCompileException {
      this((MethodInfo)null, var2);
      this.copy(var1, false, var3);
   }

   public static CtMethod make(String var0, CtClass var1) throws CannotCompileException {
      return CtNewMethod.make(var0, var1);
   }

   public static CtMethod make(MethodInfo var0, CtClass var1) throws CannotCompileException {
      if (var1.getClassFile2().getConstPool() != var0.getConstPool()) {
         throw new CannotCompileException("bad declaring class");
      } else {
         return new CtMethod(var0, var1);
      }
   }

   public int hashCode() {
      return this.getStringRep().hashCode();
   }

   void nameReplaced() {
      this.cachedStringRep = null;
   }

   final String getStringRep() {
      if (this.cachedStringRep == null) {
         this.cachedStringRep = this.methodInfo.getName() + Descriptor.getParamDescriptor(this.methodInfo.getDescriptor());
      }

      return this.cachedStringRep;
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof CtMethod && ((CtMethod)var1).getStringRep().equals(this.getStringRep());
   }

   public String getLongName() {
      return this.getDeclaringClass().getName() + "." + this.getName() + Descriptor.toString(this.getSignature());
   }

   public String getName() {
      return this.methodInfo.getName();
   }

   public void setName(String var1) {
      this.declaringClass.checkModify();
      this.methodInfo.setName(var1);
   }

   public CtClass getReturnType() throws NotFoundException {
      return this.getReturnType0();
   }

   public boolean isEmpty() {
      CodeAttribute var1 = this.getMethodInfo2().getCodeAttribute();
      if (var1 == null) {
         return (this.getModifiers() & 1024) != 0;
      } else {
         CodeIterator var2 = var1.iterator();

         try {
            return var2.hasNext() && var2.byteAt(var2.next()) == 177 && !var2.hasNext();
         } catch (BadBytecode var4) {
            return false;
         }
      }
   }

   public void setBody(CtMethod var1, ClassMap var2) throws CannotCompileException {
      setBody0(var1.declaringClass, var1.methodInfo, this.declaringClass, this.methodInfo, var2);
   }

   public void setWrappedBody(CtMethod var1, CtMethod.ConstParameter var2) throws CannotCompileException {
      this.declaringClass.checkModify();
      CtClass var3 = this.getDeclaringClass();

      CtClass[] var4;
      CtClass var5;
      try {
         var4 = this.getParameterTypes();
         var5 = this.getReturnType();
      } catch (NotFoundException var8) {
         throw new CannotCompileException(var8);
      }

      Bytecode var6 = CtNewWrappedMethod.makeBody(var3, var3.getClassFile2(), var1, var4, var5, var2);
      CodeAttribute var7 = var6.toCodeAttribute();
      this.methodInfo.setCodeAttribute(var7);
      this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & -1025);
   }

   static class StringConstParameter extends CtMethod.ConstParameter {
      String param;

      StringConstParameter(String var1) {
         this.param = var1;
      }

      int compile(Bytecode var1) throws CannotCompileException {
         var1.addLdc(this.param);
         return 1;
      }

      String descriptor() {
         return "([Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;";
      }

      String constDescriptor() {
         return "([Ljava/lang/Object;Ljava/lang/String;)V";
      }
   }

   static class LongConstParameter extends CtMethod.ConstParameter {
      long param;

      LongConstParameter(long var1) {
         this.param = var1;
      }

      int compile(Bytecode var1) throws CannotCompileException {
         var1.addLconst(this.param);
         return 2;
      }

      String descriptor() {
         return "([Ljava/lang/Object;J)Ljava/lang/Object;";
      }

      String constDescriptor() {
         return "([Ljava/lang/Object;J)V";
      }
   }

   static class IntConstParameter extends CtMethod.ConstParameter {
      int param;

      IntConstParameter(int var1) {
         this.param = var1;
      }

      int compile(Bytecode var1) throws CannotCompileException {
         var1.addIconst(this.param);
         return 1;
      }

      String descriptor() {
         return "([Ljava/lang/Object;I)Ljava/lang/Object;";
      }

      String constDescriptor() {
         return "([Ljava/lang/Object;I)V";
      }
   }

   public static class ConstParameter {
      public static CtMethod.ConstParameter integer(int var0) {
         return new CtMethod.IntConstParameter(var0);
      }

      public static CtMethod.ConstParameter integer(long var0) {
         return new CtMethod.LongConstParameter(var0);
      }

      public static CtMethod.ConstParameter string(String var0) {
         return new CtMethod.StringConstParameter(var0);
      }

      ConstParameter() {
      }

      int compile(Bytecode var1) throws CannotCompileException {
         return 0;
      }

      String descriptor() {
         return defaultDescriptor();
      }

      static String defaultDescriptor() {
         return "([Ljava/lang/Object;)Ljava/lang/Object;";
      }

      String constDescriptor() {
         return defaultConstDescriptor();
      }

      static String defaultConstDescriptor() {
         return "([Ljava/lang/Object;)V";
      }
   }
}
