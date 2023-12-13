package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
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

public class NewArray extends Expr {
   int opcode;

   protected NewArray(int var1, CodeIterator var2, CtClass var3, MethodInfo var4, int var5) {
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

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public CtClass getComponentType() throws NotFoundException {
      int var1;
      if (this.opcode == 188) {
         var1 = this.iterator.byteAt(this.currentPos + 1);
         return this.getPrimitiveType(var1);
      } else if (this.opcode != 189 && this.opcode != 197) {
         throw new RuntimeException("bad opcode: " + this.opcode);
      } else {
         var1 = this.iterator.u16bitAt(this.currentPos + 1);
         String var2 = this.getConstPool().getClassInfo(var1);
         int var3 = Descriptor.arrayDimension(var2);
         var2 = Descriptor.toArrayComponent(var2, var3);
         return Descriptor.toCtClass(var2, this.thisClass.getClassPool());
      }
   }

   CtClass getPrimitiveType(int var1) {
      switch(var1) {
      case 4:
         return CtClass.booleanType;
      case 5:
         return CtClass.charType;
      case 6:
         return CtClass.floatType;
      case 7:
         return CtClass.doubleType;
      case 8:
         return CtClass.byteType;
      case 9:
         return CtClass.shortType;
      case 10:
         return CtClass.intType;
      case 11:
         return CtClass.longType;
      default:
         throw new RuntimeException("bad atype: " + var1);
      }
   }

   public int getDimension() {
      if (this.opcode == 188) {
         return 1;
      } else if (this.opcode != 189 && this.opcode != 197) {
         throw new RuntimeException("bad opcode: " + this.opcode);
      } else {
         int var1 = this.iterator.u16bitAt(this.currentPos + 1);
         String var2 = this.getConstPool().getClassInfo(var1);
         return Descriptor.arrayDimension(var2) + (this.opcode == 189 ? 1 : 0);
      }
   }

   public int getCreatedDimensions() {
      return this.opcode == 197 ? this.iterator.byteAt(this.currentPos + 3) : 1;
   }

   public void replace(String var1) throws CannotCompileException {
      try {
         this.replace2(var1);
      } catch (CompileError var3) {
         throw new CannotCompileException(var3);
      } catch (NotFoundException var4) {
         throw new CannotCompileException(var4);
      } catch (BadBytecode var5) {
         throw new CannotCompileException("broken method");
      }
   }

   private void replace2(String var1) throws CompileError, NotFoundException, BadBytecode, CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool var2 = this.getConstPool();
      int var3 = this.currentPos;
      boolean var6 = false;
      int var7 = 1;
      byte var5;
      String var8;
      int var15;
      if (this.opcode == 188) {
         var15 = this.iterator.byteAt(this.currentPos + 1);
         CtPrimitiveType var9 = (CtPrimitiveType)this.getPrimitiveType(var15);
         var8 = "[" + var9.getDescriptor();
         var5 = 2;
      } else if (this.opcode == 189) {
         var15 = this.iterator.u16bitAt(var3 + 1);
         var8 = var2.getClassInfo(var15);
         if (var8.startsWith("[")) {
            var8 = "[" + var8;
         } else {
            var8 = "[L" + var8 + ";";
         }

         var5 = 3;
      } else {
         if (this.opcode != 197) {
            throw new RuntimeException("bad opcode: " + this.opcode);
         }

         var15 = this.iterator.u16bitAt(this.currentPos + 1);
         var8 = var2.getClassInfo(var15);
         var7 = this.iterator.byteAt(this.currentPos + 3);
         var5 = 4;
      }

      CtClass var4 = Descriptor.toCtClass(var8, this.thisClass.getClassPool());
      Javac var16 = new Javac(this.thisClass);
      CodeAttribute var10 = this.iterator.get();
      CtClass[] var11 = new CtClass[var7];

      int var12;
      for(var12 = 0; var12 < var7; ++var12) {
         var11[var12] = CtClass.intType;
      }

      var12 = var10.getMaxLocals();
      var16.recordParams("java.lang.Object", var11, true, var12, this.withinStatic());
      checkResultValue(var4, var1);
      int var13 = var16.recordReturnType(var4, true);
      var16.recordProceed(new NewArray.ProceedForArray(var4, this.opcode, var15, var7));
      Bytecode var14 = var16.getBytecode();
      storeStack(var11, true, var12, var14);
      var16.recordLocalVariables(var10, var3);
      var14.addOpcode(1);
      var14.addAstore(var13);
      var16.compileStmnt(var1);
      var14.addAload(var13);
      this.replace0(var3, var14, var5);
   }

   static class ProceedForArray implements ProceedHandler {
      CtClass arrayType;
      int opcode;
      int index;
      int dimension;

      ProceedForArray(CtClass var1, int var2, int var3, int var4) {
         this.arrayType = var1;
         this.opcode = var2;
         this.index = var3;
         this.dimension = var4;
      }

      public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
         int var4 = var1.getMethodArgsLength(var3);
         if (var4 != this.dimension) {
            throw new CompileError("$proceed() with a wrong number of parameters");
         } else {
            var1.atMethodArgs(var3, new int[var4], new int[var4], new String[var4]);
            var2.addOpcode(this.opcode);
            if (this.opcode == 189) {
               var2.addIndex(this.index);
            } else if (this.opcode == 188) {
               var2.add(this.index);
            } else {
               var2.addIndex(this.index);
               var2.add(this.dimension);
               var2.growStack(1 - this.dimension);
            }

            var1.setType(this.arrayType);
         }
      }

      public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
         var1.setType(this.arrayType);
      }
   }
}
