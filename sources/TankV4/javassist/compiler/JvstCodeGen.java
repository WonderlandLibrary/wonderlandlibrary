package javassist.compiler;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Descriptor;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.Symbol;

public class JvstCodeGen extends MemberCodeGen {
   String paramArrayName = null;
   String paramListName = null;
   CtClass[] paramTypeList = null;
   private int paramVarBase = 0;
   private boolean useParam0 = false;
   private String param0Type = null;
   public static final String sigName = "$sig";
   public static final String dollarTypeName = "$type";
   public static final String clazzName = "$class";
   private CtClass dollarType = null;
   CtClass returnType = null;
   String returnCastName = null;
   private String returnVarName = null;
   public static final String wrapperCastName = "$w";
   String proceedName = null;
   public static final String cflowName = "$cflow";
   ProceedHandler procHandler = null;

   public JvstCodeGen(Bytecode var1, CtClass var2, ClassPool var3) {
      super(var1, var2, var3);
      this.setTypeChecker(new JvstTypeChecker(var2, var3, this));
   }

   private int indexOfParam1() {
      return this.paramVarBase + (this.useParam0 ? 1 : 0);
   }

   public void setProceedHandler(ProceedHandler var1, String var2) {
      this.proceedName = var2;
      this.procHandler = var1;
   }

   public void addNullIfVoid() {
      if (this.exprType == 344) {
         this.bytecode.addOpcode(1);
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Object";
      }

   }

   public void atMember(Member var1) throws CompileError {
      String var2 = var1.get();
      if (var2.equals(this.paramArrayName)) {
         compileParameterList(this.bytecode, this.paramTypeList, this.indexOfParam1());
         this.exprType = 307;
         this.arrayDim = 1;
         this.className = "java/lang/Object";
      } else if (var2.equals("$sig")) {
         this.bytecode.addLdc(Descriptor.ofMethod(this.returnType, this.paramTypeList));
         this.bytecode.addInvokestatic("javassist/runtime/Desc", "getParams", "(Ljava/lang/String;)[Ljava/lang/Class;");
         this.exprType = 307;
         this.arrayDim = 1;
         this.className = "java/lang/Class";
      } else if (var2.equals("$type")) {
         if (this.dollarType == null) {
            throw new CompileError("$type is not available");
         }

         this.bytecode.addLdc(Descriptor.of(this.dollarType));
         this.callGetType("getType");
      } else if (var2.equals("$class")) {
         if (this.param0Type == null) {
            throw new CompileError("$class is not available");
         }

         this.bytecode.addLdc(this.param0Type);
         this.callGetType("getClazz");
      } else {
         super.atMember(var1);
      }

   }

   private void callGetType(String var1) {
      this.bytecode.addInvokestatic("javassist/runtime/Desc", var1, "(Ljava/lang/String;)Ljava/lang/Class;");
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/Class";
   }

   protected void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4, boolean var5) throws CompileError {
      if (var3 instanceof Member && ((Member)var3).get().equals(this.paramArrayName)) {
         if (var2 != 61) {
            throw new CompileError("bad operator for " + this.paramArrayName);
         }

         var4.accept(this);
         if (this.arrayDim != 1 || this.exprType != 307) {
            throw new CompileError("invalid type for " + this.paramArrayName);
         }

         this.atAssignParamList(this.paramTypeList, this.bytecode);
         if (!var5) {
            this.bytecode.addOpcode(87);
         }
      } else {
         super.atFieldAssign(var1, var2, var3, var4, var5);
      }

   }

   protected void atAssignParamList(CtClass[] var1, Bytecode var2) throws CompileError {
      if (var1 != null) {
         int var3 = this.indexOfParam1();
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var2.addOpcode(89);
            var2.addIconst(var5);
            var2.addOpcode(50);
            this.compileUnwrapValue(var1[var5], var2);
            var2.addStore(var3, var1[var5]);
            var3 += is2word(this.exprType, this.arrayDim) ? 2 : 1;
         }

      }
   }

   public void atCastExpr(CastExpr var1) throws CompileError {
      ASTList var2 = var1.getClassName();
      if (var2 != null && var1.getArrayDim() == 0) {
         ASTree var3 = var2.head();
         if (var3 instanceof Symbol && var2.tail() == null) {
            String var4 = ((Symbol)var3).get();
            if (var4.equals(this.returnCastName)) {
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
      var1.getOprand().accept(this);
      if (this.exprType != 344 && !isRefType(this.exprType) && this.arrayDim <= 0) {
         if (!(this.returnType instanceof CtPrimitiveType)) {
            throw new CompileError("invalid cast");
         }

         CtPrimitiveType var2 = (CtPrimitiveType)this.returnType;
         int var3 = MemberResolver.descToType(var2.getDescriptor());
         this.atNumCastExpr(this.exprType, var3);
         this.exprType = var3;
         this.arrayDim = 0;
         this.className = null;
      } else {
         this.compileUnwrapValue(this.returnType, this.bytecode);
      }

   }

   protected void atCastToWrapper(CastExpr var1) throws CompileError {
      var1.getOprand().accept(this);
      if (!isRefType(this.exprType) && this.arrayDim <= 0) {
         CtClass var2 = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
         if (var2 instanceof CtPrimitiveType) {
            CtPrimitiveType var3 = (CtPrimitiveType)var2;
            String var4 = var3.getWrapperName();
            this.bytecode.addNew(var4);
            this.bytecode.addOpcode(89);
            if (var3.getDataSize() > 1) {
               this.bytecode.addOpcode(94);
            } else {
               this.bytecode.addOpcode(93);
            }

            this.bytecode.addOpcode(88);
            this.bytecode.addInvokespecial(var4, "<init>", "(" + var3.getDescriptor() + ")V");
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
         if (this.procHandler != null && var3.equals(this.proceedName)) {
            this.procHandler.doit(this, this.bytecode, (ASTList)var1.oprand2());
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
      StringBuffer var2 = new StringBuffer();
      if (var1 != null && var1.tail() == null) {
         makeCflowName(var2, var1.head());
         String var3 = var2.toString();
         Object[] var4 = this.resolver.getClassPool().lookupCflow(var3);
         if (var4 == null) {
            throw new CompileError("no such $cflow: " + var3);
         } else {
            this.bytecode.addGetstatic((String)var4[0], (String)var4[1], "Ljavassist/runtime/Cflow;");
            this.bytecode.addInvokevirtual("javassist.runtime.Cflow", "value", "()I");
            this.exprType = 324;
            this.arrayDim = 0;
            this.className = null;
         }
      } else {
         throw new CompileError("bad $cflow");
      }
   }

   private static void makeCflowName(StringBuffer var0, ASTree var1) throws CompileError {
      if (var1 instanceof Symbol) {
         var0.append(((Symbol)var1).get());
      } else {
         if (var1 instanceof Expr) {
            Expr var2 = (Expr)var1;
            if (var2.getOperator() == 46) {
               makeCflowName(var0, var2.oprand1());
               var0.append('.');
               makeCflowName(var0, var2.oprand2());
               return;
            }
         }

         throw new CompileError("bad $cflow");
      }
   }

   public boolean isParamListName(ASTList var1) {
      if (this.paramTypeList != null && var1 != null && var1.tail() == null) {
         ASTree var2 = var1.head();
         return var2 instanceof Member && ((Member)var2).get().equals(this.paramListName);
      } else {
         return false;
      }
   }

   public int getMethodArgsLength(ASTList var1) {
      String var2 = this.paramListName;

      int var3;
      for(var3 = 0; var1 != null; var1 = var1.tail()) {
         ASTree var4 = var1.head();
         if (var4 instanceof Member && ((Member)var4).get().equals(var2)) {
            if (this.paramTypeList != null) {
               var3 += this.paramTypeList.length;
            }
         } else {
            ++var3;
         }
      }

      return var3;
   }

   public void atMethodArgs(ASTList var1, int[] var2, int[] var3, String[] var4) throws CompileError {
      CtClass[] var5 = this.paramTypeList;
      String var6 = this.paramListName;

      for(int var7 = 0; var1 != null; var1 = var1.tail()) {
         ASTree var8 = var1.head();
         if (var8 instanceof Member && ((Member)var8).get().equals(var6)) {
            if (var5 != null) {
               int var9 = var5.length;
               int var10 = this.indexOfParam1();

               for(int var11 = 0; var11 < var9; ++var11) {
                  CtClass var12 = var5[var11];
                  var10 += this.bytecode.addLoad(var10, var12);
                  this.setType(var12);
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
      this.bytecode.addInvokespecial(var2, var3, var4);
      this.setReturnType(var4, false, false);
      this.addNullIfVoid();
   }

   protected void atReturnStmnt(Stmnt var1) throws CompileError {
      ASTree var2 = var1.getLeft();
      if (var2 != null && this.returnType == CtClass.voidType) {
         this.compileExpr(var2);
         if (is2word(this.exprType, this.arrayDim)) {
            this.bytecode.addOpcode(88);
         } else if (this.exprType != 344) {
            this.bytecode.addOpcode(87);
         }

         var2 = null;
      }

      this.atReturnStmnt2(var2);
   }

   public int recordReturnType(CtClass var1, String var2, String var3, SymbolTable var4) throws CompileError {
      this.returnType = var1;
      this.returnCastName = var2;
      this.returnVarName = var3;
      if (var3 == null) {
         return -1;
      } else {
         int var5 = this.getMaxLocals();
         int var6 = var5 + this.recordVar(var1, var3, var5, var4);
         this.setMaxLocals(var6);
         return var5;
      }
   }

   public void recordType(CtClass var1) {
      this.dollarType = var1;
   }

   public int recordParams(CtClass[] var1, boolean var2, String var3, String var4, String var5, SymbolTable var6) throws CompileError {
      return this.recordParams(var1, var2, var3, var4, var5, !var2, 0, this.getThisName(), var6);
   }

   public int recordParams(CtClass[] var1, boolean var2, String var3, String var4, String var5, boolean var6, int var7, String var8, SymbolTable var9) throws CompileError {
      this.paramTypeList = var1;
      this.paramArrayName = var4;
      this.paramListName = var5;
      this.paramVarBase = var7;
      this.useParam0 = var6;
      if (var8 != null) {
         this.param0Type = MemberResolver.jvmToJavaName(var8);
      }

      this.inStaticMethod = var2;
      int var10 = var7;
      if (var6) {
         String var11 = var3 + "0";
         String var10003 = MemberResolver.javaToJvmName(var8);
         var10 = var7 + 1;
         Declarator var12 = new Declarator(307, var10003, 0, var7, new Symbol(var11));
         var9.append(var11, var12);
      }

      for(int var13 = 0; var13 < var1.length; ++var13) {
         var10 += this.recordVar(var1[var13], var3 + (var13 + 1), var10, var9);
      }

      if (this.getMaxLocals() < var10) {
         this.setMaxLocals(var10);
      }

      return var10;
   }

   public int recordVariable(CtClass var1, String var2, SymbolTable var3) throws CompileError {
      if (var2 == null) {
         return -1;
      } else {
         int var4 = this.getMaxLocals();
         int var5 = var4 + this.recordVar(var1, var2, var4, var3);
         this.setMaxLocals(var5);
         return var4;
      }
   }

   private int recordVar(CtClass var1, String var2, int var3, SymbolTable var4) throws CompileError {
      if (var1 == CtClass.voidType) {
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Object";
      } else {
         this.setType(var1);
      }

      Declarator var5 = new Declarator(this.exprType, this.className, this.arrayDim, var3, new Symbol(var2));
      var4.append(var2, var5);
      return is2word(this.exprType, this.arrayDim) ? 2 : 1;
   }

   public void recordVariable(String var1, String var2, int var3, SymbolTable var4) throws CompileError {
      char var5;
      int var6;
      for(var6 = 0; (var5 = var1.charAt(var6)) == '['; ++var6) {
      }

      int var7 = MemberResolver.descToType(var5);
      String var8 = null;
      if (var7 == 307) {
         if (var6 == 0) {
            var8 = var1.substring(1, var1.length() - 1);
         } else {
            var8 = var1.substring(var6 + 1, var1.length() - 1);
         }
      }

      Declarator var9 = new Declarator(var7, var8, var6, var3, new Symbol(var2));
      var4.append(var2, var9);
   }

   public static int compileParameterList(Bytecode var0, CtClass[] var1, int var2) {
      if (var1 == null) {
         var0.addIconst(0);
         var0.addAnewarray("java.lang.Object");
         return 1;
      } else {
         CtClass[] var3 = new CtClass[1];
         int var4 = var1.length;
         var0.addIconst(var4);
         var0.addAnewarray("java.lang.Object");

         for(int var5 = 0; var5 < var4; ++var5) {
            var0.addOpcode(89);
            var0.addIconst(var5);
            if (var1[var5].isPrimitive()) {
               CtPrimitiveType var6 = (CtPrimitiveType)var1[var5];
               String var7 = var6.getWrapperName();
               var0.addNew(var7);
               var0.addOpcode(89);
               int var8 = var0.addLoad(var2, var6);
               var2 += var8;
               var3[0] = var6;
               var0.addInvokespecial(var7, "<init>", Descriptor.ofMethod(CtClass.voidType, var3));
            } else {
               var0.addAload(var2);
               ++var2;
            }

            var0.addOpcode(83);
         }

         return 8;
      }
   }

   protected void compileUnwrapValue(CtClass var1, Bytecode var2) throws CompileError {
      if (var1 == CtClass.voidType) {
         this.addNullIfVoid();
      } else if (this.exprType == 344) {
         throw new CompileError("invalid type for " + this.returnCastName);
      } else {
         if (var1 instanceof CtPrimitiveType) {
            CtPrimitiveType var3 = (CtPrimitiveType)var1;
            String var4 = var3.getWrapperName();
            var2.addCheckcast(var4);
            var2.addInvokevirtual(var4, var3.getGetMethodName(), var3.getGetMethodDescriptor());
            this.setType(var1);
         } else {
            var2.addCheckcast(var1);
            this.setType(var1);
         }

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

   public void doNumCast(CtClass var1) throws CompileError {
      if (this.arrayDim == 0 && !isRefType(this.exprType)) {
         if (!(var1 instanceof CtPrimitiveType)) {
            throw new CompileError("type mismatch");
         }

         CtPrimitiveType var2 = (CtPrimitiveType)var1;
         this.atNumCastExpr(this.exprType, MemberResolver.descToType(var2.getDescriptor()));
      }

   }
}
