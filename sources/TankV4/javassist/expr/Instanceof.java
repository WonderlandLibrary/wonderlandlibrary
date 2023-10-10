package javassist.expr;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.compiler.JvstCodeGen;
import javassist.compiler.JvstTypeChecker;
import javassist.compiler.ProceedHandler;
import javassist.compiler.ast.ASTList;

public class Instanceof extends Expr {
   protected Instanceof(int var1, CodeIterator var2, CtClass var3, MethodInfo var4) {
      super(var1, var2, var3, var4);
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

   public CtClass getType() throws NotFoundException {
      ConstPool var1 = this.getConstPool();
      int var2 = this.currentPos;
      int var3 = this.iterator.u16bitAt(var2 + 1);
      String var4 = var1.getClassInfo(var3);
      return this.thisClass.getClassPool().getCtClass(var4);
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public void replace(String var1) throws CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool var2 = this.getConstPool();
      int var3 = this.currentPos;
      int var4 = this.iterator.u16bitAt(var3 + 1);
      Javac var5 = new Javac(this.thisClass);
      ClassPool var6 = this.thisClass.getClassPool();
      CodeAttribute var7 = this.iterator.get();

      try {
         CtClass[] var8 = new CtClass[]{var6.get("java.lang.Object")};
         CtClass var9 = CtClass.booleanType;
         int var10 = var7.getMaxLocals();
         var5.recordParams("java.lang.Object", var8, true, var10, this.withinStatic());
         int var11 = var5.recordReturnType(var9, true);
         var5.recordProceed(new Instanceof.ProceedForInstanceof(var4));
         var5.recordType(this.getType());
         checkResultValue(var9, var1);
         Bytecode var12 = var5.getBytecode();
         storeStack(var8, true, var10, var12);
         var5.recordLocalVariables(var7, var3);
         var12.addConstZero(var9);
         var12.addStore(var11, var9);
         var5.compileStmnt(var1);
         var12.addLoad(var11, var9);
         this.replace0(var3, var12, 3);
      } catch (CompileError var13) {
         throw new CannotCompileException(var13);
      } catch (NotFoundException var14) {
         throw new CannotCompileException(var14);
      } catch (BadBytecode var15) {
         throw new CannotCompileException("broken method");
      }
   }

   static class ProceedForInstanceof implements ProceedHandler {
      int index;

      ProceedForInstanceof(int var1) {
         this.index = var1;
      }

      public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
         if (var1.getMethodArgsLength(var3) != 1) {
            throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
         } else {
            var1.atMethodArgs(var3, new int[1], new int[1], new String[1]);
            var2.addOpcode(193);
            var2.addIndex(this.index);
            var1.setType(CtClass.booleanType);
         }
      }

      public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
         var1.atMethodArgs(var2, new int[1], new int[1], new String[1]);
         var1.setType(CtClass.booleanType);
      }
   }
}
