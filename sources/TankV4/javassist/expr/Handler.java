package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class Handler extends Expr {
   private static String EXCEPTION_NAME = "$1";
   private ExceptionTable etable;
   private int index;

   protected Handler(ExceptionTable var1, int var2, CodeIterator var3, CtClass var4, MethodInfo var5) {
      super(var1.handlerPc(var2), var3, var4, var5);
      this.etable = var1;
      this.index = var2;
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

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public CtClass getType() throws NotFoundException {
      int var1 = this.etable.catchType(this.index);
      if (var1 == 0) {
         return null;
      } else {
         ConstPool var2 = this.getConstPool();
         String var3 = var2.getClassInfo(var1);
         return this.thisClass.getClassPool().getCtClass(var3);
      }
   }

   public boolean isFinally() {
      return this.etable.catchType(this.index) == 0;
   }

   public void replace(String var1) throws CannotCompileException {
      throw new RuntimeException("not implemented yet");
   }

   public void insertBefore(String var1) throws CannotCompileException {
      this.edited = true;
      ConstPool var2 = this.getConstPool();
      CodeAttribute var3 = this.iterator.get();
      Javac var4 = new Javac(this.thisClass);
      Bytecode var5 = var4.getBytecode();
      var5.setStackDepth(1);
      var5.setMaxLocals(var3.getMaxLocals());

      try {
         CtClass var6 = this.getType();
         int var7 = var4.recordVariable(var6, EXCEPTION_NAME);
         var4.recordReturnType(var6, false);
         var5.addAstore(var7);
         var4.compileStmnt(var1);
         var5.addAload(var7);
         int var8 = this.etable.handlerPc(this.index);
         var5.addOpcode(167);
         var5.addIndex(var8 - this.iterator.getCodeLength() - var5.currentPc() + 1);
         this.maxStack = var5.getMaxStack();
         this.maxLocals = var5.getMaxLocals();
         int var9 = this.iterator.append(var5.get());
         this.iterator.append(var5.getExceptionTable(), var9);
         this.etable.setHandlerPc(this.index, var9);
      } catch (NotFoundException var10) {
         throw new CannotCompileException(var10);
      } catch (CompileError var11) {
         throw new CannotCompileException(var11);
      }
   }
}
