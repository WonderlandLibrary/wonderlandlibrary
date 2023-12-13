package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public final class CtConstructor extends CtBehavior {
   protected CtConstructor(MethodInfo var1, CtClass var2) {
      super(var2, var1);
   }

   public CtConstructor(CtClass[] var1, CtClass var2) {
      this((MethodInfo)null, var2);
      ConstPool var3 = var2.getClassFile2().getConstPool();
      String var4 = Descriptor.ofConstructor(var1);
      this.methodInfo = new MethodInfo(var3, "<init>", var4);
      this.setModifiers(1);
   }

   public CtConstructor(CtConstructor var1, CtClass var2, ClassMap var3) throws CannotCompileException {
      this((MethodInfo)null, var2);
      this.copy(var1, true, var3);
   }

   public boolean isConstructor() {
      return this.methodInfo.isConstructor();
   }

   public boolean isClassInitializer() {
      return this.methodInfo.isStaticInitializer();
   }

   public String getLongName() {
      return this.getDeclaringClass().getName() + (this.isConstructor() ? Descriptor.toString(this.getSignature()) : ".<clinit>()");
   }

   public String getName() {
      return this.methodInfo.isStaticInitializer() ? "<clinit>" : this.declaringClass.getSimpleName();
   }

   public boolean isEmpty() {
      CodeAttribute var1 = this.getMethodInfo2().getCodeAttribute();
      if (var1 == null) {
         return false;
      } else {
         ConstPool var2 = var1.getConstPool();
         CodeIterator var3 = var1.iterator();

         try {
            int var6 = var3.byteAt(var3.next());
            int var4;
            int var5;
            return var6 == 177 || var6 == 42 && var3.byteAt(var4 = var3.next()) == 183 && (var5 = var2.isConstructor(this.getSuperclassName(), var3.u16bitAt(var4 + 1))) != 0 && "()V".equals(var2.getUtf8Info(var5)) && var3.byteAt(var3.next()) == 177 && !var3.hasNext();
         } catch (BadBytecode var7) {
            return false;
         }
      }
   }

   private String getSuperclassName() {
      ClassFile var1 = this.declaringClass.getClassFile2();
      return var1.getSuperclass();
   }

   public boolean callsSuper() throws CannotCompileException {
      CodeAttribute var1 = this.methodInfo.getCodeAttribute();
      if (var1 != null) {
         CodeIterator var2 = var1.iterator();

         try {
            int var3 = var2.skipSuperConstructor();
            return var3 >= 0;
         } catch (BadBytecode var4) {
            throw new CannotCompileException(var4);
         }
      } else {
         return false;
      }
   }

   public void setBody(String var1) throws CannotCompileException {
      if (var1 == null) {
         if (this.isClassInitializer()) {
            var1 = ";";
         } else {
            var1 = "super();";
         }
      }

      super.setBody(var1);
   }

   public void setBody(CtConstructor var1, ClassMap var2) throws CannotCompileException {
      setBody0(var1.declaringClass, var1.methodInfo, this.declaringClass, this.methodInfo, var2);
   }

   public void insertBeforeBody(String var1) throws CannotCompileException {
      CtClass var2 = this.declaringClass;
      var2.checkModify();
      if (this.isClassInitializer()) {
         throw new CannotCompileException("class initializer");
      } else {
         CodeAttribute var3 = this.methodInfo.getCodeAttribute();
         CodeIterator var4 = var3.iterator();
         Bytecode var5 = new Bytecode(this.methodInfo.getConstPool(), var3.getMaxStack(), var3.getMaxLocals());
         var5.setStackDepth(var3.getMaxStack());
         Javac var6 = new Javac(var5, var2);

         try {
            var6.recordParams(this.getParameterTypes(), false);
            var6.compileStmnt(var1);
            var3.setMaxStack(var5.getMaxStack());
            var3.setMaxLocals(var5.getMaxLocals());
            var4.skipConstructor();
            int var7 = var4.insertEx(var5.get());
            var4.insert(var5.getExceptionTable(), var7);
            this.methodInfo.rebuildStackMapIf6(var2.getClassPool(), var2.getClassFile2());
         } catch (NotFoundException var8) {
            throw new CannotCompileException(var8);
         } catch (CompileError var9) {
            throw new CannotCompileException(var9);
         } catch (BadBytecode var10) {
            throw new CannotCompileException(var10);
         }
      }
   }

   int getStartPosOfBody(CodeAttribute var1) throws CannotCompileException {
      CodeIterator var2 = var1.iterator();

      try {
         var2.skipConstructor();
         return var2.next();
      } catch (BadBytecode var4) {
         throw new CannotCompileException(var4);
      }
   }

   public CtMethod toMethod(String var1, CtClass var2) throws CannotCompileException {
      return this.toMethod(var1, var2, (ClassMap)null);
   }

   public CtMethod toMethod(String var1, CtClass var2, ClassMap var3) throws CannotCompileException {
      CtMethod var4 = new CtMethod((MethodInfo)null, var2);
      var4.copy(this, false, var3);
      if (this.isConstructor()) {
         MethodInfo var5 = var4.getMethodInfo2();
         CodeAttribute var6 = var5.getCodeAttribute();
         if (var6 != null) {
            removeConsCall(var6);

            try {
               this.methodInfo.rebuildStackMapIf6(var2.getClassPool(), var2.getClassFile2());
            } catch (BadBytecode var8) {
               throw new CannotCompileException(var8);
            }
         }
      }

      var4.setName(var1);
      return var4;
   }

   private static void removeConsCall(CodeAttribute var0) throws CannotCompileException {
      CodeIterator var1 = var0.iterator();

      try {
         int var2 = var1.skipConstructor();
         if (var2 >= 0) {
            int var3 = var1.u16bitAt(var2 + 1);
            String var4 = var0.getConstPool().getMethodrefType(var3);
            int var5 = Descriptor.numOfParameters(var4) + 1;
            if (var5 > 3) {
               var2 = var1.insertGapAt(var2, var5 - 3, false).position;
            }

            var1.writeByte(87, var2++);
            var1.writeByte(0, var2);
            var1.writeByte(0, var2 + 1);
            Descriptor.Iterator var6 = new Descriptor.Iterator(var4);

            while(true) {
               var6.next();
               if (!var6.isParameter()) {
                  break;
               }

               var1.writeByte(var6.is2byte() ? 88 : 87, var2++);
            }
         }

      } catch (BadBytecode var7) {
         throw new CannotCompileException(var7);
      }
   }
}
