package javassist.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.Visitor;

public abstract class CodeGen extends Visitor implements Opcode, TokenId {
   static final String javaLangObject = "java.lang.Object";
   static final String jvmJavaLangObject = "java/lang/Object";
   static final String javaLangString = "java.lang.String";
   static final String jvmJavaLangString = "java/lang/String";
   protected Bytecode bytecode;
   private int tempVar;
   TypeChecker typeChecker;
   protected boolean hasReturned;
   public boolean inStaticMethod;
   protected ArrayList breakList;
   protected ArrayList continueList;
   protected CodeGen.ReturnHook returnHooks;
   protected int exprType;
   protected int arrayDim;
   protected String className;
   static final int[] binOp = new int[]{43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124};
   private static final int[] ifOp = new int[]{358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164};
   private static final int[] ifOp2 = new int[]{358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158};
   private static final int P_DOUBLE = 0;
   private static final int P_FLOAT = 1;
   private static final int P_LONG = 2;
   private static final int P_INT = 3;
   private static final int P_OTHER = -1;
   private static final int[] castOp = new int[]{0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0};

   public CodeGen(Bytecode var1) {
      this.bytecode = var1;
      this.tempVar = -1;
      this.typeChecker = null;
      this.hasReturned = false;
      this.inStaticMethod = false;
      this.breakList = null;
      this.continueList = null;
      this.returnHooks = null;
   }

   public void setTypeChecker(TypeChecker var1) {
      this.typeChecker = var1;
   }

   protected static void fatal() throws CompileError {
      throw new CompileError("fatal");
   }

   public int getMaxLocals() {
      return this.bytecode.getMaxLocals();
   }

   public void setMaxLocals(int var1) {
      this.bytecode.setMaxLocals(var1);
   }

   protected void incMaxLocals(int var1) {
      this.bytecode.incMaxLocals(var1);
   }

   protected int getTempVar() {
      if (this.tempVar < 0) {
         this.tempVar = this.getMaxLocals();
         this.incMaxLocals(2);
      }

      return this.tempVar;
   }

   protected int getLocalVar(Declarator var1) {
      int var2 = var1.getLocalVar();
      if (var2 < 0) {
         var2 = this.getMaxLocals();
         var1.setLocalVar(var2);
         this.incMaxLocals(1);
      }

      return var2;
   }

   protected abstract String getThisName();

   protected abstract String getSuperName() throws CompileError;

   protected abstract String resolveClassName(ASTList var1) throws CompileError;

   protected abstract String resolveClassName(String var1) throws CompileError;

   protected static String toJvmArrayName(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var1 == 0) {
         return var0;
      } else {
         StringBuffer var2 = new StringBuffer();
         int var3 = var1;

         while(var3-- > 0) {
            var2.append('[');
         }

         var2.append('L');
         var2.append(var0);
         var2.append(';');
         return var2.toString();
      }
   }

   protected static String toJvmTypeName(int var0, int var1) {
      char var2 = 'I';
      switch(var0) {
      case 301:
         var2 = 'Z';
         break;
      case 303:
         var2 = 'B';
         break;
      case 306:
         var2 = 'C';
         break;
      case 312:
         var2 = 'D';
         break;
      case 317:
         var2 = 'F';
         break;
      case 324:
         var2 = 'I';
         break;
      case 326:
         var2 = 'J';
         break;
      case 334:
         var2 = 'S';
         break;
      case 344:
         var2 = 'V';
      }

      StringBuffer var3 = new StringBuffer();

      while(var1-- > 0) {
         var3.append('[');
      }

      var3.append(var2);
      return var3.toString();
   }

   public void compileExpr(ASTree var1) throws CompileError {
      this.doTypeCheck(var1);
      var1.accept(this);
   }

   public boolean compileBooleanExpr(boolean var1, ASTree var2) throws CompileError {
      this.doTypeCheck(var2);
      return this.booleanExpr(var1, var2);
   }

   public void doTypeCheck(ASTree var1) throws CompileError {
      if (this.typeChecker != null) {
         var1.accept(this.typeChecker);
      }

   }

   public void atASTList(ASTList var1) throws CompileError {
      fatal();
   }

   public void atPair(Pair var1) throws CompileError {
      fatal();
   }

   public void atSymbol(Symbol var1) throws CompileError {
      fatal();
   }

   public void atFieldDecl(FieldDecl var1) throws CompileError {
      var1.getInit().accept(this);
   }

   public void atMethodDecl(MethodDecl var1) throws CompileError {
      ASTList var2 = var1.getModifiers();
      this.setMaxLocals(1);

      while(var2 != null) {
         Keyword var3 = (Keyword)var2.head();
         var2 = var2.tail();
         if (var3.get() == 335) {
            this.setMaxLocals(0);
            this.inStaticMethod = true;
         }
      }

      for(ASTList var5 = var1.getParams(); var5 != null; var5 = var5.tail()) {
         this.atDeclarator((Declarator)var5.head());
      }

      Stmnt var4 = var1.getBody();
      this.atMethodBody(var4, var1.isConstructor(), var1.getReturn().getType() == 344);
   }

   public void atMethodBody(Stmnt var1, boolean var2, boolean var3) throws CompileError {
      if (var1 != null) {
         if (var2 && this == var1) {
            this.insertDefaultSuperCall();
         }

         this.hasReturned = false;
         var1.accept(this);
         if (!this.hasReturned) {
            if (!var3) {
               throw new CompileError("no return statement");
            }

            this.bytecode.addOpcode(177);
            this.hasReturned = true;
         }

      }
   }

   protected abstract void insertDefaultSuperCall() throws CompileError;

   public void atStmnt(Stmnt var1) throws CompileError {
      if (var1 != null) {
         int var2 = var1.getOperator();
         if (var2 == 69) {
            ASTree var3 = var1.getLeft();
            this.doTypeCheck(var3);
            if (var3 instanceof AssignExpr) {
               this.atAssignExpr((AssignExpr)var3, false);
            } else if (var3 != false) {
               Expr var4 = (Expr)var3;
               this.atPlusPlus(var4.getOperator(), var4.oprand1(), var4, false);
            } else {
               var3.accept(this);
               int var10000 = this.exprType;
               if (this.arrayDim == 0) {
                  this.bytecode.addOpcode(88);
               } else if (this.exprType != 344) {
                  this.bytecode.addOpcode(87);
               }
            }
         } else if (var2 != 68 && var2 != 66) {
            if (var2 == 320) {
               this.atIfStmnt(var1);
            } else if (var2 != 346 && var2 != 311) {
               if (var2 == 318) {
                  this.atForStmnt(var1);
               } else if (var2 != 302 && var2 != 309) {
                  if (var2 == 333) {
                     this.atReturnStmnt(var1);
                  } else if (var2 == 340) {
                     this.atThrowStmnt(var1);
                  } else if (var2 == 343) {
                     this.atTryStmnt(var1);
                  } else if (var2 == 337) {
                     this.atSwitchStmnt(var1);
                  } else {
                     if (var2 != 338) {
                        this.hasReturned = false;
                        throw new CompileError("sorry, not supported statement: TokenId " + var2);
                     }

                     this.atSyncStmnt(var1);
                  }
               } else {
                  this.atBreakStmnt(var1, var2 == 302);
               }
            } else {
               this.atWhileStmnt(var1, var2 == 346);
            }
         } else {
            Object var5 = var1;

            while(var5 != null) {
               ASTree var6 = ((ASTList)var5).head();
               var5 = ((ASTList)var5).tail();
               if (var6 != null) {
                  var6.accept(this);
               }
            }
         }

      }
   }

   private void atIfStmnt(Stmnt var1) throws CompileError {
      ASTree var2 = var1.head();
      Stmnt var3 = (Stmnt)var1.tail().head();
      Stmnt var4 = (Stmnt)var1.tail().tail().head();
      if (this.compileBooleanExpr(false, var2)) {
         this.hasReturned = false;
         if (var4 != null) {
            var4.accept(this);
         }

      } else {
         int var5 = this.bytecode.currentPc();
         int var6 = 0;
         this.bytecode.addIndex(0);
         this.hasReturned = false;
         if (var3 != null) {
            var3.accept(this);
         }

         boolean var7 = this.hasReturned;
         this.hasReturned = false;
         if (var4 != null && !var7) {
            this.bytecode.addOpcode(167);
            var6 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
         }

         this.bytecode.write16bit(var5, this.bytecode.currentPc() - var5 + 1);
         if (var4 != null) {
            var4.accept(this);
            if (!var7) {
               this.bytecode.write16bit(var6, this.bytecode.currentPc() - var6 + 1);
            }

            this.hasReturned = var7 && this.hasReturned;
         }

      }
   }

   private void atWhileStmnt(Stmnt var1, boolean var2) throws CompileError {
      ArrayList var3 = this.breakList;
      ArrayList var4 = this.continueList;
      this.breakList = new ArrayList();
      this.continueList = new ArrayList();
      ASTree var5 = var1.head();
      Stmnt var6 = (Stmnt)var1.tail();
      int var7 = 0;
      if (var2) {
         this.bytecode.addOpcode(167);
         var7 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
      }

      int var8 = this.bytecode.currentPc();
      if (var6 != null) {
         var6.accept(this);
      }

      int var9 = this.bytecode.currentPc();
      if (var2) {
         this.bytecode.write16bit(var7, var9 - var7 + 1);
      }

      boolean var10 = this.compileBooleanExpr(true, var5);
      if (var10) {
         this.bytecode.addOpcode(167);
         var10 = this.breakList.size() == 0;
      }

      this.bytecode.addIndex(var8 - this.bytecode.currentPc() + 1);
      this.patchGoto(this.breakList, this.bytecode.currentPc());
      this.patchGoto(this.continueList, var9);
      this.continueList = var4;
      this.breakList = var3;
      this.hasReturned = var10;
   }

   protected void patchGoto(ArrayList var1, int var2) {
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = (Integer)var1.get(var4);
         this.bytecode.write16bit(var5, var2 - var5 + 1);
      }

   }

   private void atForStmnt(Stmnt var1) throws CompileError {
      ArrayList var2 = this.breakList;
      ArrayList var3 = this.continueList;
      this.breakList = new ArrayList();
      this.continueList = new ArrayList();
      Stmnt var4 = (Stmnt)var1.head();
      ASTList var5 = var1.tail();
      ASTree var6 = var5.head();
      var5 = var5.tail();
      Stmnt var7 = (Stmnt)var5.head();
      Stmnt var8 = (Stmnt)var5.tail();
      if (var4 != null) {
         var4.accept(this);
      }

      int var9 = this.bytecode.currentPc();
      int var10 = 0;
      if (var6 != null) {
         if (this.compileBooleanExpr(false, var6)) {
            this.continueList = var3;
            this.breakList = var2;
            this.hasReturned = false;
            return;
         }

         var10 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
      }

      if (var8 != null) {
         var8.accept(this);
      }

      int var11 = this.bytecode.currentPc();
      if (var7 != null) {
         var7.accept(this);
      }

      this.bytecode.addOpcode(167);
      this.bytecode.addIndex(var9 - this.bytecode.currentPc() + 1);
      int var12 = this.bytecode.currentPc();
      if (var6 != null) {
         this.bytecode.write16bit(var10, var12 - var10 + 1);
      }

      this.patchGoto(this.breakList, var12);
      this.patchGoto(this.continueList, var11);
      this.continueList = var3;
      this.breakList = var2;
      this.hasReturned = false;
   }

   private void atSwitchStmnt(Stmnt var1) throws CompileError {
      this.compileExpr(var1.head());
      ArrayList var2 = this.breakList;
      this.breakList = new ArrayList();
      int var3 = this.bytecode.currentPc();
      this.bytecode.addOpcode(171);
      int var4 = 3 - (var3 & 3);

      while(var4-- > 0) {
         this.bytecode.add(0);
      }

      Stmnt var5 = (Stmnt)var1.tail();
      int var6 = 0;

      for(Object var7 = var5; var7 != null; var7 = ((ASTList)var7).tail()) {
         if (((Stmnt)((ASTList)var7).head()).getOperator() == 304) {
            ++var6;
         }
      }

      int var14 = this.bytecode.currentPc();
      this.bytecode.addGap(4);
      this.bytecode.add32bit(var6);
      this.bytecode.addGap(var6 * 8);
      long[] var8 = new long[var6];
      int var9 = 0;
      int var10 = -1;

      for(Object var11 = var5; var11 != null; var11 = ((ASTList)var11).tail()) {
         Stmnt var12 = (Stmnt)((ASTList)var11).head();
         int var13 = var12.getOperator();
         if (var13 == 310) {
            var10 = this.bytecode.currentPc();
         } else if (var13 != 304) {
            fatal();
         } else {
            var8[var9++] = ((long)this.computeLabel(var12.head()) << 32) + ((long)(this.bytecode.currentPc() - var3) & -1L);
         }

         this.hasReturned = false;
         ((Stmnt)var12.tail()).accept(this);
      }

      Arrays.sort(var8);
      int var15 = var14 + 8;

      int var16;
      for(var16 = 0; var16 < var6; ++var16) {
         this.bytecode.write32bit(var15, (int)(var8[var16] >>> 32));
         this.bytecode.write32bit(var15 + 4, (int)var8[var16]);
         var15 += 8;
      }

      if (var10 < 0 || this.breakList.size() > 0) {
         this.hasReturned = false;
      }

      var16 = this.bytecode.currentPc();
      if (var10 < 0) {
         var10 = var16;
      }

      this.bytecode.write32bit(var14, var10 - var3);
      this.patchGoto(this.breakList, var16);
      this.breakList = var2;
   }

   private int computeLabel(ASTree var1) throws CompileError {
      this.doTypeCheck(var1);
      var1 = TypeChecker.stripPlusExpr(var1);
      if (var1 instanceof IntConst) {
         return (int)((IntConst)var1).get();
      } else {
         throw new CompileError("bad case label");
      }
   }

   private void atBreakStmnt(Stmnt var1, boolean var2) throws CompileError {
      if (var1.head() != null) {
         throw new CompileError("sorry, not support labeled break or continue");
      } else {
         this.bytecode.addOpcode(167);
         Integer var3 = new Integer(this.bytecode.currentPc());
         this.bytecode.addIndex(0);
         if (var2) {
            this.breakList.add(var3);
         } else {
            this.continueList.add(var3);
         }

      }
   }

   protected void atReturnStmnt(Stmnt var1) throws CompileError {
      this.atReturnStmnt2(var1.getLeft());
   }

   protected final void atReturnStmnt2(ASTree param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private void atThrowStmnt(Stmnt var1) throws CompileError {
      ASTree var2 = var1.getLeft();
      this.compileExpr(var2);
      if (this.exprType == 307 && this.arrayDim <= 0) {
         this.bytecode.addOpcode(191);
         this.hasReturned = true;
      } else {
         throw new CompileError("bad throw statement");
      }
   }

   protected void atTryStmnt(Stmnt var1) throws CompileError {
      this.hasReturned = false;
   }

   private void atSyncStmnt(Stmnt var1) throws CompileError {
      int var2 = getListSize(this.breakList);
      int var3 = getListSize(this.continueList);
      this.compileExpr(var1.head());
      if (this.exprType != 307 && this.arrayDim == 0) {
         throw new CompileError("bad type expr for synchronized block");
      } else {
         Bytecode var4 = this.bytecode;
         int var5 = var4.getMaxLocals();
         var4.incMaxLocals(1);
         var4.addOpcode(89);
         var4.addAstore(var5);
         var4.addOpcode(194);
         CodeGen.ReturnHook var6 = new CodeGen.ReturnHook(this, this, var5) {
            final int val$var;
            final CodeGen this$0;

            {
               this.this$0 = var1;
               this.val$var = var3;
            }

            protected boolean doit(Bytecode var1, int var2) {
               var1.addAload(this.val$var);
               var1.addOpcode(195);
               return false;
            }
         };
         int var7 = var4.currentPc();
         Stmnt var8 = (Stmnt)var1.tail();
         if (var8 != null) {
            var8.accept(this);
         }

         int var9 = var4.currentPc();
         int var10 = 0;
         if (!this.hasReturned) {
            var6.doit(var4, 0);
            var4.addOpcode(167);
            var10 = var4.currentPc();
            var4.addIndex(0);
         }

         if (var7 < var9) {
            int var11 = var4.currentPc();
            var6.doit(var4, 0);
            var4.addOpcode(191);
            var4.addExceptionHandler(var7, var9, var11, 0);
         }

         if (!this.hasReturned) {
            var4.write16bit(var10, var4.currentPc() - var10 + 1);
         }

         var6.remove(this);
         if (getListSize(this.breakList) != var2 || getListSize(this.continueList) != var3) {
            throw new CompileError("sorry, cannot break/continue in synchronized block");
         }
      }
   }

   private static int getListSize(ArrayList var0) {
      return var0 == null ? 0 : var0.size();
   }

   public void atDeclarator(Declarator var1) throws CompileError {
      var1.setLocalVar(this.getMaxLocals());
      var1.setClassName(this.resolveClassName(var1.getClassName()));
      var1.getType();
      byte var2;
      if (var1.getArrayDim() == 0) {
         var2 = 2;
      } else {
         var2 = 1;
      }

      this.incMaxLocals(var2);
      ASTree var3 = var1.getInitializer();
      if (var3 != null) {
         this.doTypeCheck(var3);
         this.atVariableAssign((Expr)null, 61, (Variable)null, var1, var3, false);
      }

   }

   public abstract void atNewExpr(NewExpr var1) throws CompileError;

   public abstract void atArrayInit(ArrayInit var1) throws CompileError;

   public void atAssignExpr(AssignExpr var1) throws CompileError {
      this.atAssignExpr(var1, true);
   }

   protected void atAssignExpr(AssignExpr var1, boolean var2) throws CompileError {
      int var3 = var1.getOperator();
      ASTree var4 = var1.oprand1();
      ASTree var5 = var1.oprand2();
      if (var4 instanceof Variable) {
         this.atVariableAssign(var1, var3, (Variable)var4, ((Variable)var4).getDeclarator(), var5, var2);
      } else {
         if (var4 instanceof Expr) {
            Expr var6 = (Expr)var4;
            if (var6.getOperator() == 65) {
               this.atArrayAssign(var1, var3, (Expr)var4, var5, var2);
               return;
            }
         }

         this.atFieldAssign(var1, var3, var4, var5, var2);
      }

   }

   protected static void badAssign(Expr var0) throws CompileError {
      String var1;
      if (var0 == null) {
         var1 = "incompatible type for assignment";
      } else {
         var1 = "incompatible type for " + var0.getName();
      }

      throw new CompileError(var1);
   }

   private void atVariableAssign(Expr param1, int param2, Variable param3, Declarator param4, ASTree param5, boolean param6) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   protected abstract void atArrayVariableAssign(ArrayInit var1, int var2, int var3, String var4) throws CompileError;

   private void atArrayAssign(Expr var1, int var2, Expr var3, ASTree var4, boolean var5) throws CompileError {
      this.arrayAccess(var3.oprand1(), var3.oprand2());
      if (var2 != 61) {
         this.bytecode.addOpcode(92);
         this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
      }

      int var6 = this.exprType;
      int var7 = this.arrayDim;
      String var8 = this.className;
      this.atAssignCore(var1, var2, var4, var6, var7, var8);
      if (var5) {
         if (var7 == 0) {
            this.bytecode.addOpcode(94);
         } else {
            this.bytecode.addOpcode(91);
         }
      }

      this.bytecode.addOpcode(getArrayWriteOp(var6, var7));
      this.exprType = var6;
      this.arrayDim = var7;
      this.className = var8;
   }

   protected abstract void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4, boolean var5) throws CompileError;

   protected void atAssignCore(Expr param1, int param2, ASTree param3, int param4, int param5, String param6) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private void atStringPlusEq(Expr var1, int var2, int var3, String var4, ASTree var5) throws CompileError {
      if (!"java/lang/String".equals(var4)) {
         badAssign(var1);
      }

      this.convToString(var2, var3);
      var5.accept(this);
      this.convToString(this.exprType, this.arrayDim);
      this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
   }

   public void atCondExpr(CondExpr var1) throws CompileError {
      if (false == var1.condExpr()) {
         var1.elseExpr().accept(this);
      } else {
         int var2 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
         var1.thenExpr().accept(this);
         int var3 = this.arrayDim;
         this.bytecode.addOpcode(167);
         int var4 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
         this.bytecode.write16bit(var2, this.bytecode.currentPc() - var2 + 1);
         var1.elseExpr().accept(this);
         if (var3 != this.arrayDim) {
            throw new CompileError("type mismatch in ?:");
         }

         this.bytecode.write16bit(var4, this.bytecode.currentPc() - var4 + 1);
      }

   }

   static int lookupBinOp(int var0) {
      int[] var1 = binOp;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; var3 += 5) {
         if (var1[var3] == var0) {
            return var3;
         }
      }

      return -1;
   }

   public void atBinExpr(BinExpr var1) throws CompileError {
      int var2 = var1.getOperator();
      int var3 = lookupBinOp(var2);
      if (var3 >= 0) {
         var1.oprand1().accept(this);
         ASTree var4 = var1.oprand2();
         if (var4 == null) {
            return;
         }

         int var5 = this.exprType;
         int var6 = this.arrayDim;
         String var7 = this.className;
         var4.accept(this);
         if (var6 != this.arrayDim) {
            throw new CompileError("incompatible array types");
         }

         if (var2 != 43 || var6 != 0 || var5 != 307 && this.exprType != 307) {
            this.atArithBinExpr(var1, var2, var3, var5);
         } else {
            this.atStringConcatExpr(var1, var5, var6, var7);
         }
      } else {
         if (true == var1) {
            this.bytecode.addIndex(7);
            this.bytecode.addIconst(0);
            this.bytecode.addOpcode(167);
            this.bytecode.addIndex(4);
         }

         this.bytecode.addIconst(1);
      }

   }

   private void atArithBinExpr(Expr var1, int var2, int var3, int var4) throws CompileError {
      if (this.arrayDim != 0) {
         badTypes(var1);
      }

      int var5 = this.exprType;
      if (var2 != 364 && var2 != 366 && var2 != 370) {
         this.convertOprandTypes(var4, var5, var1);
      } else if (var5 != 324 && var5 != 334 && var5 != 306 && var5 != 303) {
         badTypes(var1);
      } else {
         this.exprType = var4;
      }

      int var6 = typePrecedence(this.exprType);
      if (var6 >= 0) {
         int var7 = binOp[var3 + var6 + 1];
         if (var7 != 0) {
            if (var6 == 3 && this.exprType != 301) {
               this.exprType = 324;
            }

            this.bytecode.addOpcode(var7);
            return;
         }
      }

      badTypes(var1);
   }

   private void atStringConcatExpr(Expr var1, int var2, int var3, String var4) throws CompileError {
      int var5 = this.exprType;
      int var6 = this.arrayDim;
      boolean var7 = is2word(var5, var6);
      boolean var8 = var5 == 307 && "java/lang/String".equals(this.className);
      if (var7) {
         this.convToString(var5, var6);
      }

      if (var3 == 0) {
         this.bytecode.addOpcode(91);
         this.bytecode.addOpcode(87);
      } else {
         this.bytecode.addOpcode(95);
      }

      this.convToString(var2, var3);
      this.bytecode.addOpcode(95);
      if (!var7 && !var8) {
         this.convToString(var5, var6);
      }

      this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
   }

   private void convToString(int param1, int param2) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   static int getCompOperator(ASTree var0) throws CompileError {
      if (var0 instanceof Expr) {
         Expr var1 = (Expr)var0;
         int var2 = var1.getOperator();
         if (var2 == 33) {
            return 33;
         } else {
            return var1 instanceof BinExpr && var2 != 368 && var2 != 369 && var2 != 38 && var2 != 124 ? 358 : var2;
         }
      } else {
         return 32;
      }
   }

   private int compileOprands(BinExpr var1) throws CompileError {
      var1.oprand1().accept(this);
      int var2 = this.exprType;
      int var3 = this.arrayDim;
      var1.oprand2().accept(this);
      if (var3 != this.arrayDim) {
         if (var2 != 412 && this.exprType != 412) {
            throw new CompileError("incompatible array types");
         }

         if (this.exprType == 412) {
            this.arrayDim = var3;
         }
      }

      return var2 == 412 ? this.exprType : var2;
   }

   private void compareExpr(boolean var1, int var2, int var3, BinExpr var4) throws CompileError {
      if (this.arrayDim == 0) {
         this.convertOprandTypes(var3, this.exprType, var4);
      }

      int var5 = typePrecedence(this.exprType);
      if (var5 != -1 && this.arrayDim <= 0) {
         int[] var6;
         int var7;
         if (var5 == 3) {
            var6 = ifOp;

            for(var7 = 0; var7 < var6.length; var7 += 3) {
               if (var6[var7] == var2) {
                  this.bytecode.addOpcode(var6[var7 + (var1 ? 1 : 2)]);
                  return;
               }
            }

            badTypes(var4);
         } else {
            if (var5 == 0) {
               if (var2 != 60 && var2 != 357) {
                  this.bytecode.addOpcode(151);
               } else {
                  this.bytecode.addOpcode(152);
               }
            } else if (var5 == 1) {
               if (var2 != 60 && var2 != 357) {
                  this.bytecode.addOpcode(149);
               } else {
                  this.bytecode.addOpcode(150);
               }
            } else if (var5 == 2) {
               this.bytecode.addOpcode(148);
            } else {
               fatal();
            }

            var6 = ifOp2;

            for(var7 = 0; var7 < var6.length; var7 += 3) {
               if (var6[var7] == var2) {
                  this.bytecode.addOpcode(var6[var7 + (var1 ? 1 : 2)]);
                  return;
               }
            }

            badTypes(var4);
         }
      } else if (var2 == 358) {
         this.bytecode.addOpcode(var1 ? 165 : 166);
      } else if (var2 == 350) {
         this.bytecode.addOpcode(var1 ? 166 : 165);
      } else {
         badTypes(var4);
      }

   }

   protected static void badTypes(Expr var0) throws CompileError {
      throw new CompileError("invalid types for " + var0.getName());
   }

   private static int typePrecedence(int param0) {
      // $FF: Couldn't be decompiled
   }

   static boolean isP_INT(int var0) {
      return typePrecedence(var0) == 3;
   }

   static boolean rightIsStrong(int var0, int var1) {
      int var2 = typePrecedence(var0);
      int var3 = typePrecedence(var1);
      return var2 >= 0 && var3 >= 0 && var2 > var3;
   }

   private void convertOprandTypes(int var1, int var2, Expr var3) throws CompileError {
      int var5 = typePrecedence(var1);
      int var6 = typePrecedence(var2);
      if (var6 >= 0 || var5 >= 0) {
         if (var6 < 0 || var5 < 0) {
            badTypes(var3);
         }

         boolean var4;
         int var7;
         int var8;
         if (var5 <= var6) {
            var4 = false;
            this.exprType = var1;
            var7 = castOp[var6 * 4 + var5];
            var8 = var5;
         } else {
            var4 = true;
            var7 = castOp[var5 * 4 + var6];
            var8 = var6;
         }

         if (var4) {
            if (var8 != 0 && var8 != 2) {
               if (var8 == 1) {
                  if (var5 == 2) {
                     this.bytecode.addOpcode(91);
                     this.bytecode.addOpcode(87);
                  } else {
                     this.bytecode.addOpcode(95);
                  }

                  this.bytecode.addOpcode(var7);
                  this.bytecode.addOpcode(95);
               } else {
                  fatal();
               }
            } else {
               if (var5 != 0 && var5 != 2) {
                  this.bytecode.addOpcode(93);
               } else {
                  this.bytecode.addOpcode(94);
               }

               this.bytecode.addOpcode(88);
               this.bytecode.addOpcode(var7);
               this.bytecode.addOpcode(94);
               this.bytecode.addOpcode(88);
            }
         } else if (var7 != 0) {
            this.bytecode.addOpcode(var7);
         }

      }
   }

   public void atCastExpr(CastExpr var1) throws CompileError {
      String var2 = this.resolveClassName(var1.getClassName());
      String var3 = this.checkCastExpr(var1, var2);
      int var4 = this.exprType;
      this.exprType = var1.getType();
      this.arrayDim = var1.getArrayDim();
      this.className = var2;
      if (var3 == null) {
         this.atNumCastExpr(var4, this.exprType);
      } else {
         this.bytecode.addCheckcast(var3);
      }

   }

   public void atInstanceOfExpr(InstanceOfExpr var1) throws CompileError {
      String var2 = this.resolveClassName(var1.getClassName());
      String var3 = this.checkCastExpr(var1, var2);
      this.bytecode.addInstanceof(var3);
      this.exprType = 301;
      this.arrayDim = 0;
   }

   private String checkCastExpr(CastExpr var1, String var2) throws CompileError {
      String var3 = "invalid cast";
      ASTree var4 = var1.getOprand();
      int var5 = var1.getArrayDim();
      int var6 = var1.getType();
      var4.accept(this);
      int var7 = this.exprType;
      int var8 = this.arrayDim;
      int var10002 = this.arrayDim;
      String var10003 = this.className;
      if (var2 != true && var7 != 344 && var6 != 344) {
         if (var6 == 307) {
            if (var5 != var7 && var8 == 0) {
               throw new CompileError("invalid cast");
            } else {
               return toJvmArrayName(var2, var5);
            }
         } else {
            return var5 > 0 ? toJvmTypeName(var6, var5) : null;
         }
      } else {
         throw new CompileError("invalid cast");
      }
   }

   void atNumCastExpr(int var1, int var2) throws CompileError {
      if (var1 != var2) {
         int var5 = typePrecedence(var1);
         int var6 = typePrecedence(var2);
         int var3;
         if (0 <= var5 && var5 < 3) {
            var3 = castOp[var5 * 4 + var6];
         } else {
            var3 = 0;
         }

         short var4;
         if (var2 == 312) {
            var4 = 135;
         } else if (var2 == 317) {
            var4 = 134;
         } else if (var2 == 326) {
            var4 = 133;
         } else if (var2 == 334) {
            var4 = 147;
         } else if (var2 == 306) {
            var4 = 146;
         } else if (var2 == 303) {
            var4 = 145;
         } else {
            var4 = 0;
         }

         if (var3 != 0) {
            this.bytecode.addOpcode(var3);
         }

         if ((var3 == 0 || var3 == 136 || var3 == 139 || var3 == 142) && var4 != 0) {
            this.bytecode.addOpcode(var4);
         }

      }
   }

   public void atExpr(Expr var1) throws CompileError {
      int var2 = var1.getOperator();
      ASTree var3 = var1.oprand1();
      if (var2 == 46) {
         String var4 = ((Symbol)var1.oprand2()).get();
         if (var4.equals("class")) {
            this.atClassObject(var1);
         } else {
            this.atFieldRead(var1);
         }
      } else if (var2 == 35) {
         this.atFieldRead(var1);
      } else if (var2 == 65) {
         this.atArrayRead(var3, var1.oprand2());
      } else if (var2 != 362 && var2 != 363) {
         if (var2 == 33) {
            if (false == var1) {
               this.bytecode.addIndex(7);
               this.bytecode.addIconst(1);
               this.bytecode.addOpcode(167);
               this.bytecode.addIndex(4);
            }

            this.bytecode.addIconst(0);
         } else if (var2 == 67) {
            fatal();
         } else {
            var1.oprand1().accept(this);
            int var5 = typePrecedence(this.exprType);
            if (this.arrayDim > 0) {
               badType(var1);
            }

            if (var2 == 45) {
               if (var5 == 0) {
                  this.bytecode.addOpcode(119);
               } else if (var5 == 1) {
                  this.bytecode.addOpcode(118);
               } else if (var5 == 2) {
                  this.bytecode.addOpcode(117);
               } else if (var5 == 3) {
                  this.bytecode.addOpcode(116);
                  this.exprType = 324;
               } else {
                  badType(var1);
               }
            } else if (var2 == 126) {
               if (var5 == 3) {
                  this.bytecode.addIconst(-1);
                  this.bytecode.addOpcode(130);
                  this.exprType = 324;
               } else if (var5 == 2) {
                  this.bytecode.addLconst(-1L);
                  this.bytecode.addOpcode(131);
               } else {
                  badType(var1);
               }
            } else if (var2 == 43) {
               if (var5 == -1) {
                  badType(var1);
               }
            } else {
               fatal();
            }
         }
      } else {
         this.atPlusPlus(var2, var3, var1, true);
      }

   }

   protected static void badType(Expr var0) throws CompileError {
      throw new CompileError("invalid type for " + var0.getName());
   }

   public abstract void atCallExpr(CallExpr var1) throws CompileError;

   protected abstract void atFieldRead(ASTree var1) throws CompileError;

   public void atClassObject(Expr var1) throws CompileError {
      ASTree var2 = var1.oprand1();
      if (!(var2 instanceof Symbol)) {
         throw new CompileError("fatal error: badly parsed .class expr");
      } else {
         String var3 = ((Symbol)var2).get();
         if (var3.startsWith("[")) {
            int var4 = var3.indexOf("[L");
            if (var4 >= 0) {
               String var5 = var3.substring(var4 + 2, var3.length() - 1);
               String var6 = this.resolveClassName(var5);
               if (!var5.equals(var6)) {
                  var6 = MemberResolver.jvmToJavaName(var6);
                  StringBuffer var7 = new StringBuffer();

                  while(var4-- >= 0) {
                     var7.append('[');
                  }

                  var7.append('L').append(var6).append(';');
                  var3 = var7.toString();
               }
            }
         } else {
            var3 = this.resolveClassName(MemberResolver.javaToJvmName(var3));
            var3 = MemberResolver.jvmToJavaName(var3);
         }

         this.atClassObject2(var3);
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Class";
      }
   }

   protected void atClassObject2(String var1) throws CompileError {
      int var2 = this.bytecode.currentPc();
      this.bytecode.addLdc(var1);
      this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
      int var3 = this.bytecode.currentPc();
      this.bytecode.addOpcode(167);
      int var4 = this.bytecode.currentPc();
      this.bytecode.addIndex(0);
      this.bytecode.addExceptionHandler(var2, var3, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
      this.bytecode.growStack(1);
      this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
      this.bytecode.addOpcode(191);
      this.bytecode.write16bit(var4, this.bytecode.currentPc() - var4 + 1);
   }

   public void atArrayRead(ASTree var1, ASTree var2) throws CompileError {
      this.arrayAccess(var1, var2);
      this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
   }

   protected void arrayAccess(ASTree var1, ASTree var2) throws CompileError {
      var1.accept(this);
      int var3 = this.exprType;
      int var4 = this.arrayDim;
      if (var4 == 0) {
         throw new CompileError("bad array access");
      } else {
         String var5 = this.className;
         var2.accept(this);
         if (typePrecedence(this.exprType) == 3 && this.arrayDim <= 0) {
            this.exprType = var3;
            this.arrayDim = var4 - 1;
            this.className = var5;
         } else {
            throw new CompileError("bad array index");
         }
      }
   }

   protected static int getArrayReadOp(int var0, int var1) {
      if (var1 > 0) {
         return 50;
      } else {
         switch(var0) {
         case 301:
         case 303:
            return 51;
         case 306:
            return 52;
         case 312:
            return 49;
         case 317:
            return 48;
         case 324:
            return 46;
         case 326:
            return 47;
         case 334:
            return 53;
         default:
            return 50;
         }
      }
   }

   protected static int getArrayWriteOp(int var0, int var1) {
      if (var1 > 0) {
         return 83;
      } else {
         switch(var0) {
         case 301:
         case 303:
            return 84;
         case 306:
            return 85;
         case 312:
            return 82;
         case 317:
            return 81;
         case 324:
            return 79;
         case 326:
            return 80;
         case 334:
            return 86;
         default:
            return 83;
         }
      }
   }

   private void atPlusPlus(int var1, ASTree var2, Expr var3, boolean var4) throws CompileError {
      boolean var5 = var2 == null;
      if (var5) {
         var2 = var3.oprand2();
      }

      if (var2 instanceof Variable) {
         Declarator var6 = ((Variable)var2).getDeclarator();
         int var7 = this.exprType = var6.getType();
         this.arrayDim = var6.getArrayDim();
         int var8 = this.getLocalVar(var6);
         if (this.arrayDim > 0) {
            badType(var3);
         }

         if (var7 == 312) {
            this.bytecode.addDload(var8);
            if (var4 && var5) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addDconst(1.0D);
            this.bytecode.addOpcode(var1 == 362 ? 99 : 103);
            if (var4 && !var5) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addDstore(var8);
         } else if (var7 == 326) {
            this.bytecode.addLload(var8);
            if (var4 && var5) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addLconst(1L);
            this.bytecode.addOpcode(var1 == 362 ? 97 : 101);
            if (var4 && !var5) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addLstore(var8);
         } else if (var7 == 317) {
            this.bytecode.addFload(var8);
            if (var4 && var5) {
               this.bytecode.addOpcode(89);
            }

            this.bytecode.addFconst(1.0F);
            this.bytecode.addOpcode(var1 == 362 ? 98 : 102);
            if (var4 && !var5) {
               this.bytecode.addOpcode(89);
            }

            this.bytecode.addFstore(var8);
         } else if (var7 != 303 && var7 != 306 && var7 != 334 && var7 != 324) {
            badType(var3);
         } else {
            if (var4 && var5) {
               this.bytecode.addIload(var8);
            }

            int var9 = var1 == 362 ? 1 : -1;
            if (var8 > 255) {
               this.bytecode.addOpcode(196);
               this.bytecode.addOpcode(132);
               this.bytecode.addIndex(var8);
               this.bytecode.addIndex(var9);
            } else {
               this.bytecode.addOpcode(132);
               this.bytecode.add(var8);
               this.bytecode.add(var9);
            }

            if (var4 && !var5) {
               this.bytecode.addIload(var8);
            }
         }
      } else {
         if (var2 instanceof Expr) {
            Expr var10 = (Expr)var2;
            if (var10.getOperator() == 65) {
               this.atArrayPlusPlus(var1, var5, var10, var4);
               return;
            }
         }

         this.atFieldPlusPlus(var1, var5, var2, var3, var4);
      }

   }

   public void atArrayPlusPlus(int var1, boolean var2, Expr var3, boolean var4) throws CompileError {
      this.arrayAccess(var3.oprand1(), var3.oprand2());
      int var5 = this.exprType;
      int var6 = this.arrayDim;
      if (var6 > 0) {
         badType(var3);
      }

      this.bytecode.addOpcode(92);
      this.bytecode.addOpcode(getArrayReadOp(var5, this.arrayDim));
      int var7 = var6 == 0 ? 94 : 91;
      this.atPlusPlusCore(var7, var4, var1, var2, var3);
      this.bytecode.addOpcode(getArrayWriteOp(var5, var6));
   }

   protected void atPlusPlusCore(int var1, boolean var2, int var3, boolean var4, Expr var5) throws CompileError {
      int var6 = this.exprType;
      if (var2 && var4) {
         this.bytecode.addOpcode(var1);
      }

      if (var6 != 324 && var6 != 303 && var6 != 306 && var6 != 334) {
         if (var6 == 326) {
            this.bytecode.addLconst(1L);
            this.bytecode.addOpcode(var3 == 362 ? 97 : 101);
         } else if (var6 == 317) {
            this.bytecode.addFconst(1.0F);
            this.bytecode.addOpcode(var3 == 362 ? 98 : 102);
         } else if (var6 == 312) {
            this.bytecode.addDconst(1.0D);
            this.bytecode.addOpcode(var3 == 362 ? 99 : 103);
         } else {
            badType(var5);
         }
      } else {
         this.bytecode.addIconst(1);
         this.bytecode.addOpcode(var3 == 362 ? 96 : 100);
         this.exprType = 324;
      }

      if (var2 && !var4) {
         this.bytecode.addOpcode(var1);
      }

   }

   protected abstract void atFieldPlusPlus(int var1, boolean var2, ASTree var3, Expr var4, boolean var5) throws CompileError;

   public abstract void atMember(Member var1) throws CompileError;

   public void atVariable(Variable var1) throws CompileError {
      Declarator var2 = var1.getDeclarator();
      this.exprType = var2.getType();
      this.arrayDim = var2.getArrayDim();
      this.className = var2.getClassName();
      int var3 = this.getLocalVar(var2);
      if (this.arrayDim > 0) {
         this.bytecode.addAload(var3);
      } else {
         switch(this.exprType) {
         case 307:
            this.bytecode.addAload(var3);
            break;
         case 312:
            this.bytecode.addDload(var3);
            break;
         case 317:
            this.bytecode.addFload(var3);
            break;
         case 326:
            this.bytecode.addLload(var3);
            break;
         default:
            this.bytecode.addIload(var3);
         }
      }

   }

   public void atKeyword(Keyword var1) throws CompileError {
      this.arrayDim = 0;
      int var2 = var1.get();
      switch(var2) {
      case 336:
      case 339:
         if (this.inStaticMethod) {
            throw new CompileError("not-available: " + (var2 == 339 ? "this" : "super"));
         }

         this.bytecode.addAload(0);
         this.exprType = 307;
         if (var2 == 339) {
            this.className = this.getThisName();
         } else {
            this.className = this.getSuperName();
         }
         break;
      case 410:
         this.bytecode.addIconst(1);
         this.exprType = 301;
         break;
      case 411:
         this.bytecode.addIconst(0);
         this.exprType = 301;
         break;
      case 412:
         this.bytecode.addOpcode(1);
         this.exprType = 412;
         break;
      default:
         fatal();
      }

   }

   public void atStringL(StringL var1) throws CompileError {
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
      this.bytecode.addLdc(var1.get());
   }

   public void atIntConst(IntConst var1) throws CompileError {
      this.arrayDim = 0;
      long var2 = var1.get();
      int var4 = var1.getType();
      if (var4 != 402 && var4 != 401) {
         this.exprType = 326;
         this.bytecode.addLconst(var2);
      } else {
         this.exprType = var4 == 402 ? 324 : 306;
         this.bytecode.addIconst((int)var2);
      }

   }

   public void atDoubleConst(DoubleConst var1) throws CompileError {
      this.arrayDim = 0;
      if (var1.getType() == 405) {
         this.exprType = 312;
         this.bytecode.addDconst(var1.get());
      } else {
         this.exprType = 317;
         this.bytecode.addFconst((float)var1.get());
      }

   }

   protected abstract static class ReturnHook {
      CodeGen.ReturnHook next;

      protected abstract boolean doit(Bytecode var1, int var2);

      protected ReturnHook(CodeGen var1) {
         this.next = var1.returnHooks;
         var1.returnHooks = this;
      }

      protected void remove(CodeGen var1) {
         var1.returnHooks = this.next;
      }
   }
}
