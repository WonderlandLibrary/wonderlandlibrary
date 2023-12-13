package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtPrimitiveType;
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
import javassist.compiler.ProceedHandler;
import javassist.compiler.ast.ASTList;

public class FieldAccess extends Expr {
   int opcode;

   protected FieldAccess(int var1, CodeIterator var2, CtClass var3, MethodInfo var4, int var5) {
      super(var1, var2, var3, var4);
      this.opcode = var5;
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

   public boolean isStatic() {
      return isStatic(this.opcode);
   }

   static boolean isStatic(int var0) {
      return var0 == 178 || var0 == 179;
   }

   public boolean isReader() {
      return this.opcode == 180 || this.opcode == 178;
   }

   public boolean isWriter() {
      return this.opcode == 181 || this.opcode == 179;
   }

   private CtClass getCtClass() throws NotFoundException {
      return this.thisClass.getClassPool().get(this.getClassName());
   }

   public String getClassName() {
      int var1 = this.iterator.u16bitAt(this.currentPos + 1);
      return this.getConstPool().getFieldrefClassName(var1);
   }

   public String getFieldName() {
      int var1 = this.iterator.u16bitAt(this.currentPos + 1);
      return this.getConstPool().getFieldrefName(var1);
   }

   public CtField getField() throws NotFoundException {
      CtClass var1 = this.getCtClass();
      int var2 = this.iterator.u16bitAt(this.currentPos + 1);
      ConstPool var3 = this.getConstPool();
      return var1.getField(var3.getFieldrefName(var2), var3.getFieldrefType(var2));
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public String getSignature() {
      int var1 = this.iterator.u16bitAt(this.currentPos + 1);
      return this.getConstPool().getFieldrefType(var1);
   }

   public void replace(String var1) throws CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool var2 = this.getConstPool();
      int var3 = this.currentPos;
      int var4 = this.iterator.u16bitAt(var3 + 1);
      Javac var5 = new Javac(this.thisClass);
      CodeAttribute var6 = this.iterator.get();

      try {
         CtClass var9 = Descriptor.toCtClass(var2.getFieldrefType(var4), this.thisClass.getClassPool());
         boolean var10 = this.isReader();
         CtClass[] var7;
         CtClass var8;
         if (var10) {
            var7 = new CtClass[0];
            var8 = var9;
         } else {
            var7 = new CtClass[]{var9};
            var8 = CtClass.voidType;
         }

         int var11 = var6.getMaxLocals();
         var5.recordParams(var2.getFieldrefClassName(var4), var7, true, var11, this.withinStatic());
         boolean var12 = checkResultValue(var8, var1);
         if (var10) {
            var12 = true;
         }

         int var13 = var5.recordReturnType(var8, var12);
         if (var10) {
            var5.recordProceed(new FieldAccess.ProceedForRead(var8, this.opcode, var4, var11));
         } else {
            var5.recordType(var9);
            var5.recordProceed(new FieldAccess.ProceedForWrite(var7[0], this.opcode, var4, var11));
         }

         Bytecode var14 = var5.getBytecode();
         storeStack(var7, this.isStatic(), var11, var14);
         var5.recordLocalVariables(var6, var3);
         if (var12) {
            if (var8 == CtClass.voidType) {
               var14.addOpcode(1);
               var14.addAstore(var13);
            } else {
               var14.addConstZero(var8);
               var14.addStore(var13, var8);
            }
         }

         var5.compileStmnt(var1);
         if (var10) {
            var14.addLoad(var13, var8);
         }

         this.replace0(var3, var14, 3);
      } catch (CompileError var15) {
         throw new CannotCompileException(var15);
      } catch (NotFoundException var16) {
         throw new CannotCompileException(var16);
      } catch (BadBytecode var17) {
         throw new CannotCompileException("broken method");
      }
   }

   static class ProceedForWrite implements ProceedHandler {
      CtClass fieldType;
      int opcode;
      int targetVar;
      int index;

      ProceedForWrite(CtClass var1, int var2, int var3, int var4) {
         this.fieldType = var1;
         this.targetVar = var4;
         this.opcode = var2;
         this.index = var3;
      }

      public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
         if (var1.getMethodArgsLength(var3) != 1) {
            throw new CompileError("$proceed() cannot take more than one parameter for field writing");
         } else {
            byte var4;
            if (FieldAccess.isStatic(this.opcode)) {
               var4 = 0;
            } else {
               var4 = -1;
               var2.addAload(this.targetVar);
            }

            var1.atMethodArgs(var3, new int[1], new int[1], new String[1]);
            var1.doNumCast(this.fieldType);
            int var5;
            if (this.fieldType instanceof CtPrimitiveType) {
               var5 = var4 - ((CtPrimitiveType)this.fieldType).getDataSize();
            } else {
               var5 = var4 - 1;
            }

            var2.add(this.opcode);
            var2.addIndex(this.index);
            var2.growStack(var5);
            var1.setType(CtClass.voidType);
            var1.addNullIfVoid();
         }
      }

      public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
         var1.atMethodArgs(var2, new int[1], new int[1], new String[1]);
         var1.setType(CtClass.voidType);
         var1.addNullIfVoid();
      }
   }

   static class ProceedForRead implements ProceedHandler {
      CtClass fieldType;
      int opcode;
      int targetVar;
      int index;

      ProceedForRead(CtClass var1, int var2, int var3, int var4) {
         this.fieldType = var1;
         this.targetVar = var4;
         this.opcode = var2;
         this.index = var3;
      }

      public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
         if (var3 != null && !var1.isParamListName(var3)) {
            throw new CompileError("$proceed() cannot take a parameter for field reading");
         } else {
            byte var4;
            if (FieldAccess.isStatic(this.opcode)) {
               var4 = 0;
            } else {
               var4 = -1;
               var2.addAload(this.targetVar);
            }

            int var5;
            if (this.fieldType instanceof CtPrimitiveType) {
               var5 = var4 + ((CtPrimitiveType)this.fieldType).getDataSize();
            } else {
               var5 = var4 + 1;
            }

            var2.add(this.opcode);
            var2.addIndex(this.index);
            var2.growStack(var5);
            var1.setType(this.fieldType);
         }
      }

      public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
         var1.setType(this.fieldType);
      }
   }
}
