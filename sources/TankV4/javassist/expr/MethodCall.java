package javassist.expr;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class MethodCall extends Expr {
   protected MethodCall(int var1, CodeIterator var2, CtClass var3, MethodInfo var4) {
      super(var1, var2, var3, var4);
   }

   private int getNameAndType(ConstPool var1) {
      int var2 = this.currentPos;
      int var3 = this.iterator.byteAt(var2);
      int var4 = this.iterator.u16bitAt(var2 + 1);
      return var3 == 185 ? var1.getInterfaceMethodrefNameAndType(var4) : var1.getMethodrefNameAndType(var4);
   }

   public CtBehavior where() {
      return super.where();
   }

   public int getLineNumber() {
      return super.getLineNumber();
   }

   public String getFileName() {
      return super.getFileName();
   }

   protected CtClass getCtClass() throws NotFoundException {
      return this.thisClass.getClassPool().get(this.getClassName());
   }

   public String getClassName() {
      ConstPool var2 = this.getConstPool();
      int var3 = this.currentPos;
      int var4 = this.iterator.byteAt(var3);
      int var5 = this.iterator.u16bitAt(var3 + 1);
      String var1;
      if (var4 == 185) {
         var1 = var2.getInterfaceMethodrefClassName(var5);
      } else {
         var1 = var2.getMethodrefClassName(var5);
      }

      if (var1.charAt(0) == '[') {
         var1 = Descriptor.toClassName(var1);
      }

      return var1;
   }

   public String getMethodName() {
      ConstPool var1 = this.getConstPool();
      int var2 = this.getNameAndType(var1);
      return var1.getUtf8Info(var1.getNameAndTypeName(var2));
   }

   public CtMethod getMethod() throws NotFoundException {
      return this.getCtClass().getMethod(this.getMethodName(), this.getSignature());
   }

   public String getSignature() {
      ConstPool var1 = this.getConstPool();
      int var2 = this.getNameAndType(var1);
      return var1.getUtf8Info(var1.getNameAndTypeDescriptor(var2));
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public boolean isSuper() {
      return this.iterator.byteAt(this.currentPos) == 183 && !this.where().getDeclaringClass().getName().equals(this.getClassName());
   }

   public void replace(String var1) throws CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool var2 = this.getConstPool();
      int var3 = this.currentPos;
      int var4 = this.iterator.u16bitAt(var3 + 1);
      int var9 = this.iterator.byteAt(var3);
      String var5;
      String var6;
      String var7;
      byte var8;
      if (var9 == 185) {
         var8 = 5;
         var5 = var2.getInterfaceMethodrefClassName(var4);
         var6 = var2.getInterfaceMethodrefName(var4);
         var7 = var2.getInterfaceMethodrefType(var4);
      } else {
         if (var9 != 184 && var9 != 183 && var9 != 182) {
            throw new CannotCompileException("not method invocation");
         }

         var8 = 3;
         var5 = var2.getMethodrefClassName(var4);
         var6 = var2.getMethodrefName(var4);
         var7 = var2.getMethodrefType(var4);
      }

      Javac var10 = new Javac(this.thisClass);
      ClassPool var11 = this.thisClass.getClassPool();
      CodeAttribute var12 = this.iterator.get();

      try {
         CtClass[] var13 = Descriptor.getParameterTypes(var7, var11);
         CtClass var14 = Descriptor.getReturnType(var7, var11);
         int var15 = var12.getMaxLocals();
         var10.recordParams(var5, var13, true, var15, this.withinStatic());
         int var16 = var10.recordReturnType(var14, true);
         if (var9 == 184) {
            var10.recordStaticProceed(var5, var6);
         } else if (var9 == 183) {
            var10.recordSpecialProceed("$0", var5, var6, var7);
         } else {
            var10.recordProceed("$0", var6);
         }

         checkResultValue(var14, var1);
         Bytecode var17 = var10.getBytecode();
         storeStack(var13, var9 == 184, var15, var17);
         var10.recordLocalVariables(var12, var3);
         if (var14 != CtClass.voidType) {
            var17.addConstZero(var14);
            var17.addStore(var16, var14);
         }

         var10.compileStmnt(var1);
         if (var14 != CtClass.voidType) {
            var17.addLoad(var16, var14);
         }

         this.replace0(var3, var17, var8);
      } catch (CompileError var18) {
         throw new CannotCompileException(var18);
      } catch (NotFoundException var19) {
         throw new CannotCompileException(var19);
      } catch (BadBytecode var20) {
         throw new CannotCompileException("broken method");
      }
   }
}
