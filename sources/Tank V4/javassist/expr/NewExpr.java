package javassist.expr;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
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
import javassist.compiler.JvstCodeGen;
import javassist.compiler.JvstTypeChecker;
import javassist.compiler.MemberResolver;
import javassist.compiler.ProceedHandler;
import javassist.compiler.ast.ASTList;

public class NewExpr extends Expr {
   String newTypeName;
   int newPos;

   protected NewExpr(int var1, CodeIterator var2, CtClass var3, MethodInfo var4, String var5, int var6) {
      super(var1, var2, var3, var4);
      this.newTypeName = var5;
      this.newPos = var6;
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

   private CtClass getCtClass() throws NotFoundException {
      return this.thisClass.getClassPool().get(this.newTypeName);
   }

   public String getClassName() {
      return this.newTypeName;
   }

   public String getSignature() {
      ConstPool var1 = this.getConstPool();
      int var2 = this.iterator.u16bitAt(this.currentPos + 1);
      return var1.getMethodrefType(var2);
   }

   public CtConstructor getConstructor() throws NotFoundException {
      ConstPool var1 = this.getConstPool();
      int var2 = this.iterator.u16bitAt(this.currentPos + 1);
      String var3 = var1.getMethodrefType(var2);
      return this.getCtClass().getConstructor(var3);
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   private int canReplace() throws CannotCompileException {
      int var1 = this.iterator.byteAt(this.newPos + 3);
      if (var1 != 89) {
         return var1 == 90 && this.iterator.byteAt(this.newPos + 4) == 95 ? 5 : 3;
      } else {
         return this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator.byteAt(this.newPos + 5) == 88 ? 6 : 4;
      }
   }

   public void replace(String var1) throws CannotCompileException {
      this.thisClass.getClassFile();
      boolean var2 = true;
      int var3 = this.newPos;
      int var4 = this.iterator.u16bitAt(var3 + 1);
      int var5 = this.canReplace();
      int var6 = var3 + var5;

      for(int var7 = var3; var7 < var6; ++var7) {
         this.iterator.writeByte(0, var7);
      }

      ConstPool var21 = this.getConstPool();
      var3 = this.currentPos;
      int var8 = this.iterator.u16bitAt(var3 + 1);
      String var9 = var21.getMethodrefType(var8);
      Javac var10 = new Javac(this.thisClass);
      ClassPool var11 = this.thisClass.getClassPool();
      CodeAttribute var12 = this.iterator.get();

      try {
         CtClass[] var13 = Descriptor.getParameterTypes(var9, var11);
         CtClass var14 = var11.get(this.newTypeName);
         int var15 = var12.getMaxLocals();
         var10.recordParams(this.newTypeName, var13, true, var15, this.withinStatic());
         int var16 = var10.recordReturnType(var14, true);
         var10.recordProceed(new NewExpr.ProceedForNew(var14, var4, var8));
         checkResultValue(var14, var1);
         Bytecode var17 = var10.getBytecode();
         storeStack(var13, true, var15, var17);
         var10.recordLocalVariables(var12, var3);
         var17.addConstZero(var14);
         var17.addStore(var16, var14);
         var10.compileStmnt(var1);
         if (var5 > 3) {
            var17.addAload(var16);
         }

         this.replace0(var3, var17, 3);
      } catch (CompileError var18) {
         throw new CannotCompileException(var18);
      } catch (NotFoundException var19) {
         throw new CannotCompileException(var19);
      } catch (BadBytecode var20) {
         throw new CannotCompileException("broken method");
      }
   }

   static class ProceedForNew implements ProceedHandler {
      CtClass newType;
      int newIndex;
      int methodIndex;

      ProceedForNew(CtClass var1, int var2, int var3) {
         this.newType = var1;
         this.newIndex = var2;
         this.methodIndex = var3;
      }

      public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
         var2.addOpcode(187);
         var2.addIndex(this.newIndex);
         var2.addOpcode(89);
         var1.atMethodCallCore(this.newType, "<init>", var3, false, true, -1, (MemberResolver.Method)null);
         var1.setType(this.newType);
      }

      public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
         var1.atMethodCallCore(this.newType, "<init>", var2);
         var1.setType(this.newType);
      }
   }
}
