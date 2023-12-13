package javassist.compiler;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.Symbol;

public class JvstTypeChecker extends TypeChecker {
   private JvstCodeGen codeGen;

   public JvstTypeChecker(CtClass var1, ClassPool var2, JvstCodeGen var3) {
      super(var1, var2);
      this.codeGen = var3;
   }

   public void addNullIfVoid() {
      if (this.exprType == 344) {
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Object";
      }

   }

   public void atMember(Member var1) throws CompileError {
      String var2 = var1.get();
      if (var2.equals(this.codeGen.paramArrayName)) {
         this.exprType = 307;
         this.arrayDim = 1;
         this.className = "java/lang/Object";
      } else if (var2.equals("$sig")) {
         this.exprType = 307;
         this.arrayDim = 1;
         this.className = "java/lang/Class";
      } else if (!var2.equals("$type") && !var2.equals("$class")) {
         super.atMember(var1);
      } else {
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Class";
      }

   }

   protected void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4) throws CompileError {
      if (var3 instanceof Member && ((Member)var3).get().equals(this.codeGen.paramArrayName)) {
         var4.accept(this);
         CtClass[] var5 = this.codeGen.paramTypeList;
         if (var5 == null) {
            return;
         }

         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            this.compileUnwrapValue(var5[var7]);
         }
      } else {
         super.atFieldAssign(var1, var2, var3, var4);
      }

   }

   public void atCastExpr(CastExpr var1) throws CompileError {
      ASTList var2 = var1.getClassName();
      if (var2 != null && var1.getArrayDim() == 0) {
         ASTree var3 = var2.head();
         if (var3 instanceof Symbol && var2.tail() == null) {
            String var4 = ((Symbol)var3).get();
            if (var4.equals(this.codeGen.returnCastName)) {
               this.atCastToRtype(var1);
               return;
            }

            if (var4.equals("$w")) {
               this.atCastToWrapper(var1);
               return;
            }
         }
      }

      super.atCastExpr(var1);
   }

   protected void atCastToRtype(CastExpr var1) throws CompileError {
      CtClass var2 = this.codeGen.returnType;
      var1.getOprand().accept(this);
      if (this.exprType != 344 && !CodeGen.isRefType(this.exprType) && this.arrayDim <= 0) {
         if (var2 instanceof CtPrimitiveType) {
            CtPrimitiveType var3 = (CtPrimitiveType)var2;
            int var4 = MemberResolver.descToType(var3.getDescriptor());
            this.exprType = var4;
            this.arrayDim = 0;
            this.className = null;
         }
      } else {
         this.compileUnwrapValue(var2);
      }

   }

   protected void atCastToWrapper(CastExpr var1) throws CompileError {
      var1.getOprand().accept(this);
      if (!CodeGen.isRefType(this.exprType) && this.arrayDim <= 0) {
         CtClass var2 = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
         if (var2 instanceof CtPrimitiveType) {
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Object";
         }

      }
   }

   public void atCallExpr(CallExpr var1) throws CompileError {
      ASTree var2 = var1.oprand1();
      if (var2 instanceof Member) {
         String var3 = ((Member)var2).get();
         if (this.codeGen.procHandler != null && var3.equals(this.codeGen.proceedName)) {
            this.codeGen.procHandler.setReturnType(this, (ASTList)var1.oprand2());
            return;
         }

         if (var3.equals("$cflow")) {
            this.atCflow((ASTList)var1.oprand2());
            return;
         }
      }

      super.atCallExpr(var1);
   }

   protected void atCflow(ASTList var1) throws CompileError {
      this.exprType = 324;
      this.arrayDim = 0;
      this.className = null;
   }

   public boolean isParamListName(ASTList var1) {
      if (this.codeGen.paramTypeList != null && var1 != null && var1.tail() == null) {
         ASTree var2 = var1.head();
         return var2 instanceof Member && ((Member)var2).get().equals(this.codeGen.paramListName);
      } else {
         return false;
      }
   }

   public int getMethodArgsLength(ASTList var1) {
      String var2 = this.codeGen.paramListName;

      int var3;
      for(var3 = 0; var1 != null; var1 = var1.tail()) {
         ASTree var4 = var1.head();
         if (var4 instanceof Member && ((Member)var4).get().equals(var2)) {
            if (this.codeGen.paramTypeList != null) {
               var3 += this.codeGen.paramTypeList.length;
            }
         } else {
            ++var3;
         }
      }

      return var3;
   }

   public void atMethodArgs(ASTList var1, int[] var2, int[] var3, String[] var4) throws CompileError {
      CtClass[] var5 = this.codeGen.paramTypeList;
      String var6 = this.codeGen.paramListName;

      for(int var7 = 0; var1 != null; var1 = var1.tail()) {
         ASTree var8 = var1.head();
         if (var8 instanceof Member && ((Member)var8).get().equals(var6)) {
            if (var5 != null) {
               int var9 = var5.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  CtClass var11 = var5[var10];
                  this.setType(var11);
                  var2[var7] = this.exprType;
                  var3[var7] = this.arrayDim;
                  var4[var7] = this.className;
                  ++var7;
               }
            }
         } else {
            var8.accept(this);
            var2[var7] = this.exprType;
            var3[var7] = this.arrayDim;
            var4[var7] = this.className;
            ++var7;
         }
      }

   }

   void compileInvokeSpecial(ASTree var1, String var2, String var3, String var4, ASTList var5) throws CompileError {
      var1.accept(this);
      int var6 = this.getMethodArgsLength(var5);
      this.atMethodArgs(var5, new int[var6], new int[var6], new String[var6]);
      this.setReturnType(var4);
      this.addNullIfVoid();
   }

   protected void compileUnwrapValue(CtClass var1) throws CompileError {
      if (var1 == CtClass.voidType) {
         this.addNullIfVoid();
      } else {
         this.setType(var1);
      }

   }

   public void setType(CtClass var1) throws CompileError {
      this.setType(var1, 0);
   }

   private void setType(CtClass var1, int var2) throws CompileError {
      if (var1.isPrimitive()) {
         CtPrimitiveType var3 = (CtPrimitiveType)var1;
         this.exprType = MemberResolver.descToType(var3.getDescriptor());
         this.arrayDim = var2;
         this.className = null;
      } else if (var1.isArray()) {
         try {
            this.setType(var1.getComponentType(), var2 + 1);
         } catch (NotFoundException var4) {
            throw new CompileError("undefined type: " + var1.getName());
         }
      } else {
         this.exprType = 307;
         this.arrayDim = var2;
         this.className = MemberResolver.javaToJvmName(var1.getName());
      }

   }
}
