package javassist.compiler;

import java.util.ArrayList;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.Symbol;

public class MemberCodeGen extends CodeGen {
   protected MemberResolver resolver;
   protected CtClass thisClass;
   protected MethodInfo thisMethod;
   protected boolean resultStatic;

   public MemberCodeGen(Bytecode var1, CtClass var2, ClassPool var3) {
      super(var1);
      this.resolver = new MemberResolver(var3);
      this.thisClass = var2;
      this.thisMethod = null;
   }

   public int getMajorVersion() {
      ClassFile var1 = this.thisClass.getClassFile2();
      return var1 == null ? ClassFile.MAJOR_VERSION : var1.getMajorVersion();
   }

   public void setThisMethod(CtMethod var1) {
      this.thisMethod = var1.getMethodInfo2();
      if (this.typeChecker != null) {
         this.typeChecker.setThisMethod(this.thisMethod);
      }

   }

   public CtClass getThisClass() {
      return this.thisClass;
   }

   protected String getThisName() {
      return MemberResolver.javaToJvmName(this.thisClass.getName());
   }

   protected String getSuperName() throws CompileError {
      return MemberResolver.javaToJvmName(MemberResolver.getSuperclass(this.thisClass).getName());
   }

   protected void insertDefaultSuperCall() throws CompileError {
      this.bytecode.addAload(0);
      this.bytecode.addInvokespecial(MemberResolver.getSuperclass(this.thisClass), "<init>", "()V");
   }

   protected void atTryStmnt(Stmnt var1) throws CompileError {
      Bytecode var2 = this.bytecode;
      Stmnt var3 = (Stmnt)var1.getLeft();
      if (var3 != null) {
         ASTList var4 = (ASTList)var1.getRight().getLeft();
         Stmnt var5 = (Stmnt)var1.getRight().getRight().getLeft();
         ArrayList var6 = new ArrayList();
         MemberCodeGen.JsrHook var7 = null;
         if (var5 != null) {
            var7 = new MemberCodeGen.JsrHook(this);
         }

         int var8 = var2.currentPc();
         var3.accept(this);
         int var9 = var2.currentPc();
         if (var8 == var9) {
            throw new CompileError("empty try block");
         } else {
            boolean var10 = !this.hasReturned;
            if (var10) {
               var2.addOpcode(167);
               var6.add(new Integer(var2.currentPc()));
               var2.addIndex(0);
            }

            int var11 = this.getMaxLocals();
            this.incMaxLocals(1);

            while(var4 != null) {
               Pair var12 = (Pair)var4.head();
               var4 = var4.tail();
               Declarator var13 = (Declarator)var12.getLeft();
               Stmnt var14 = (Stmnt)var12.getRight();
               var13.setLocalVar(var11);
               CtClass var15 = this.resolver.lookupClassByJvmName(var13.getClassName());
               var13.setClassName(MemberResolver.javaToJvmName(var15.getName()));
               var2.addExceptionHandler(var8, var9, var2.currentPc(), var15);
               var2.growStack(1);
               var2.addAstore(var11);
               this.hasReturned = false;
               if (var14 != null) {
                  var14.accept(this);
               }

               if (!this.hasReturned) {
                  var2.addOpcode(167);
                  var6.add(new Integer(var2.currentPc()));
                  var2.addIndex(0);
                  var10 = true;
               }
            }

            int var16;
            if (var5 != null) {
               var7.remove(this);
               var16 = var2.currentPc();
               var2.addExceptionHandler(var8, var16, var16, 0);
               var2.growStack(1);
               var2.addAstore(var11);
               this.hasReturned = false;
               var5.accept(this);
               if (!this.hasReturned) {
                  var2.addAload(var11);
                  var2.addOpcode(191);
               }

               this.addFinally(var7.jsrList, var5);
            }

            var16 = var2.currentPc();
            this.patchGoto(var6, var16);
            this.hasReturned = !var10;
            if (var5 != null && var10) {
               var5.accept(this);
            }

         }
      }
   }

   private void addFinally(ArrayList var1, Stmnt var2) throws CompileError {
      Bytecode var3 = this.bytecode;
      int var4 = var1.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         int[] var6 = (int[])((int[])var1.get(var5));
         int var7 = var6[0];
         var3.write16bit(var7, var3.currentPc() - var7 + 1);
         MemberCodeGen.JsrHook2 var8 = new MemberCodeGen.JsrHook2(this, var6);
         var2.accept(this);
         var8.remove(this);
         if (!this.hasReturned) {
            var3.addOpcode(167);
            var3.addIndex(var7 + 3 - var3.currentPc());
         }
      }

   }

   public void atNewExpr(NewExpr var1) throws CompileError {
      if (var1.isArray()) {
         this.atNewArrayExpr(var1);
      } else {
         CtClass var2 = this.resolver.lookupClassByName(var1.getClassName());
         String var3 = var2.getName();
         ASTList var4 = var1.getArguments();
         this.bytecode.addNew(var3);
         this.bytecode.addOpcode(89);
         this.atMethodCallCore(var2, "<init>", var4, false, true, -1, (MemberResolver.Method)null);
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = MemberResolver.javaToJvmName(var3);
      }

   }

   public void atNewArrayExpr(NewExpr var1) throws CompileError {
      int var2 = var1.getArrayType();
      ASTList var3 = var1.getArraySize();
      ASTList var4 = var1.getClassName();
      ArrayInit var5 = var1.getInitializer();
      if (var3.length() > 1) {
         if (var5 != null) {
            throw new CompileError("sorry, multi-dimensional array initializer for new is not supported");
         } else {
            this.atMultiNewArray(var2, var4, var3);
         }
      } else {
         ASTree var6 = var3.head();
         this.atNewArrayExpr2(var2, var6, Declarator.astToClassName(var4, '/'), var5);
      }
   }

   private void atNewArrayExpr2(int var1, ASTree var2, String var3, ArrayInit var4) throws CompileError {
      if (var4 == null) {
         if (var2 == null) {
            throw new CompileError("no array size");
         }

         var2.accept(this);
      } else {
         if (var2 != null) {
            throw new CompileError("unnecessary array size specified for new");
         }

         int var5 = var4.length();
         this.bytecode.addIconst(var5);
      }

      String var9;
      if (var1 == 307) {
         var9 = this.resolveClassName(var3);
         this.bytecode.addAnewarray(MemberResolver.jvmToJavaName(var9));
      } else {
         var9 = null;
         byte var6 = 0;
         switch(var1) {
         case 301:
            var6 = 4;
            break;
         case 303:
            var6 = 8;
            break;
         case 306:
            var6 = 5;
            break;
         case 312:
            var6 = 7;
            break;
         case 317:
            var6 = 6;
            break;
         case 324:
            var6 = 10;
            break;
         case 326:
            var6 = 11;
            break;
         case 334:
            var6 = 9;
            break;
         default:
            badNewExpr();
         }

         this.bytecode.addOpcode(188);
         this.bytecode.add(var6);
      }

      if (var4 != null) {
         int var10 = var4.length();
         Object var7 = var4;

         for(int var8 = 0; var8 < var10; ++var8) {
            this.bytecode.addOpcode(89);
            this.bytecode.addIconst(var8);
            ((ASTList)var7).head().accept(this);
            if (!isRefType(var1)) {
               this.atNumCastExpr(this.exprType, var1);
            }

            this.bytecode.addOpcode(getArrayWriteOp(var1, 0));
            var7 = ((ASTList)var7).tail();
         }
      }

      this.exprType = var1;
      this.arrayDim = 1;
      this.className = var9;
   }

   private static void badNewExpr() throws CompileError {
      throw new CompileError("bad new expression");
   }

   protected void atArrayVariableAssign(ArrayInit var1, int var2, int var3, String var4) throws CompileError {
      this.atNewArrayExpr2(var2, (ASTree)null, var4, var1);
   }

   public void atArrayInit(ArrayInit var1) throws CompileError {
      throw new CompileError("array initializer is not supported");
   }

   protected void atMultiNewArray(int var1, ASTList var2, ASTList var3) throws CompileError {
      int var5 = var3.length();

      int var4;
      for(var4 = 0; var3 != null; var3 = var3.tail()) {
         ASTree var6 = var3.head();
         if (var6 == null) {
            break;
         }

         ++var4;
         var6.accept(this);
         if (this.exprType != 324) {
            throw new CompileError("bad type for array size");
         }
      }

      this.exprType = var1;
      this.arrayDim = var5;
      String var7;
      if (var1 == 307) {
         this.className = this.resolveClassName(var2);
         var7 = toJvmArrayName(this.className, var5);
      } else {
         var7 = toJvmTypeName(var1, var5);
      }

      this.bytecode.addMultiNewarray(var7, var4);
   }

   public void atCallExpr(CallExpr var1) throws CompileError {
      String var2 = null;
      CtClass var3 = null;
      ASTree var4 = var1.oprand1();
      ASTList var5 = (ASTList)var1.oprand2();
      boolean var6 = false;
      boolean var7 = false;
      int var8 = -1;
      MemberResolver.Method var9 = var1.getMethod();
      if (var4 instanceof Member) {
         var2 = ((Member)var4).get();
         var3 = this.thisClass;
         if (!this.inStaticMethod && (var9 == null || !var9.isStatic())) {
            var8 = this.bytecode.currentPc();
            this.bytecode.addAload(0);
         } else {
            var6 = true;
         }
      } else if (var4 instanceof Keyword) {
         var7 = true;
         var2 = "<init>";
         var3 = this.thisClass;
         if (this.inStaticMethod) {
            throw new CompileError("a constructor cannot be static");
         }

         this.bytecode.addAload(0);
         if (((Keyword)var4).get() == 336) {
            var3 = MemberResolver.getSuperclass(var3);
         }
      } else if (var4 instanceof Expr) {
         Expr var10 = (Expr)var4;
         var2 = ((Symbol)var10.oprand2()).get();
         int var11 = var10.getOperator();
         if (var11 == 35) {
            var3 = this.resolver.lookupClass(((Symbol)var10.oprand1()).get(), false);
            var6 = true;
         } else if (var11 == 46) {
            ASTree var12 = var10.oprand1();
            String var13 = TypeChecker.isDotSuper(var12);
            if (var13 != null) {
               var7 = true;
               var3 = MemberResolver.getSuperInterface(this.thisClass, var13);
               if (!this.inStaticMethod && (var9 == null || !var9.isStatic())) {
                  var8 = this.bytecode.currentPc();
                  this.bytecode.addAload(0);
               } else {
                  var6 = true;
               }
            } else {
               if (var12 instanceof Keyword && ((Keyword)var12).get() == 336) {
                  var7 = true;
               }

               try {
                  var12.accept(this);
               } catch (NoFieldException var15) {
                  if (var15.getExpr() != var12) {
                     throw var15;
                  }

                  this.exprType = 307;
                  this.arrayDim = 0;
                  this.className = var15.getField();
                  var6 = true;
               }

               if (this.arrayDim > 0) {
                  var3 = this.resolver.lookupClass("java.lang.Object", true);
               } else if (this.exprType == 307) {
                  var3 = this.resolver.lookupClassByJvmName(this.className);
               } else {
                  badMethod();
               }
            }
         } else {
            badMethod();
         }
      } else {
         fatal();
      }

      this.atMethodCallCore(var3, var2, var5, var6, var7, var8, var9);
   }

   private static void badMethod() throws CompileError {
      throw new CompileError("bad method");
   }

   public void atMethodCallCore(CtClass var1, String var2, ASTList var3, boolean var4, boolean var5, int var6, MemberResolver.Method var7) throws CompileError {
      int var8 = this.getMethodArgsLength(var3);
      int[] var9 = new int[var8];
      int[] var10 = new int[var8];
      String[] var11 = new String[var8];
      if (!var4 && var7 != null && var7.isStatic()) {
         this.bytecode.addOpcode(87);
         var4 = true;
      }

      int var12 = this.bytecode.getStackDepth();
      this.atMethodArgs(var3, var9, var10, var11);
      if (var7 == null) {
         var7 = this.resolver.lookupMethod(var1, this.thisClass, this.thisMethod, var2, var9, var10, var11);
      }

      if (var7 == null) {
         String var13;
         if (var2.equals("<init>")) {
            var13 = "constructor not found";
         } else {
            var13 = "Method " + var2 + " not found in " + var1.getName();
         }

         throw new CompileError(var13);
      } else {
         this.atMethodCallCore2(var1, var2, var4, var5, var6, var7);
      }
   }

   private void atMethodCallCore2(CtClass var1, String var2, boolean var3, boolean var4, int var5, MemberResolver.Method var6) throws CompileError {
      CtClass var7 = var6.declaring;
      MethodInfo var8 = var6.info;
      String var9 = var8.getDescriptor();
      int var10 = var8.getAccessFlags();
      if (var2.equals("<init>")) {
         var4 = true;
         if (var7 != var1) {
            throw new CompileError("no such constructor: " + var1.getName());
         }

         if (var7 != this.thisClass && AccessFlag.isPrivate(var10)) {
            var9 = this.getAccessibleConstructor(var9, var7, var8);
            this.bytecode.addOpcode(1);
         }
      } else if (AccessFlag.isPrivate(var10)) {
         if (var7 == this.thisClass) {
            var4 = true;
         } else {
            var4 = false;
            var3 = true;
            if ((var10 & 8) == 0) {
               var9 = Descriptor.insertParameter(var7.getName(), var9);
            }

            var10 = AccessFlag.setPackage(var10) | 8;
            var2 = this.getAccessiblePrivate(var2, var9, var9, var8, var7);
         }
      }

      boolean var11 = false;
      if ((var10 & 8) != 0) {
         if (!var3) {
            var3 = true;
            if (var5 >= 0) {
               this.bytecode.write(var5, 0);
            } else {
               var11 = true;
            }
         }

         this.bytecode.addInvokestatic(var7, var2, var9);
      } else if (var4) {
         this.bytecode.addInvokespecial(var1, var2, var9);
      } else {
         if (!Modifier.isPublic(var7.getModifiers()) || var7.isInterface() != var1.isInterface()) {
            var7 = var1;
         }

         if (var7.isInterface()) {
            int var12 = Descriptor.paramSize(var9) + 1;
            this.bytecode.addInvokeinterface(var7, var2, var9, var12);
         } else {
            if (var3) {
               throw new CompileError(var2 + " is not static");
            }

            this.bytecode.addInvokevirtual(var7, var2, var9);
         }
      }

      this.setReturnType(var9, var3, var11);
   }

   protected String getAccessiblePrivate(String var1, String var2, String var3, MethodInfo var4, CtClass var5) throws CompileError {
      if (this.thisClass != null) {
         AccessorMaker var6 = var5.getAccessorMaker();
         if (var6 != null) {
            return var6.getMethodAccessor(var1, var2, var3, var4);
         }
      }

      throw new CompileError("Method " + var1 + " is private");
   }

   protected String getAccessibleConstructor(String var1, CtClass var2, MethodInfo var3) throws CompileError {
      if (this.thisClass != null) {
         AccessorMaker var4 = var2.getAccessorMaker();
         if (var4 != null) {
            return var4.getConstructor(var2, var1, var3);
         }
      }

      throw new CompileError("the called constructor is private in " + var2.getName());
   }

   public int getMethodArgsLength(ASTList var1) {
      return ASTList.length(var1);
   }

   public void atMethodArgs(ASTList var1, int[] var2, int[] var3, String[] var4) throws CompileError {
      for(int var5 = 0; var1 != null; var1 = var1.tail()) {
         ASTree var6 = var1.head();
         var6.accept(this);
         var2[var5] = this.exprType;
         var3[var5] = this.arrayDim;
         var4[var5] = this.className;
         ++var5;
      }

   }

   void setReturnType(String var1, boolean var2, boolean var3) throws CompileError {
      int var4 = var1.indexOf(41);
      if (var4 < 0) {
         badMethod();
      }

      ++var4;
      char var5 = var1.charAt(var4);

      int var6;
      for(var6 = 0; var5 == '['; var5 = var1.charAt(var4)) {
         ++var6;
         ++var4;
      }

      this.arrayDim = var6;
      int var7;
      if (var5 == 'L') {
         var7 = var1.indexOf(59, var4 + 1);
         if (var7 < 0) {
            badMethod();
         }

         this.exprType = 307;
         this.className = var1.substring(var4 + 1, var7);
      } else {
         this.exprType = MemberResolver.descToType(var5);
         this.className = null;
      }

      var7 = this.exprType;
      if (var2 && var3) {
         if (is2word(var7, var6)) {
            this.bytecode.addOpcode(93);
            this.bytecode.addOpcode(88);
            this.bytecode.addOpcode(87);
         } else if (var7 == 344) {
            this.bytecode.addOpcode(87);
         } else {
            this.bytecode.addOpcode(95);
            this.bytecode.addOpcode(87);
         }
      }

   }

   protected void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4, boolean var5) throws CompileError {
      CtField var6 = this.fieldAccess(var3, false);
      boolean var7 = this.resultStatic;
      if (var2 != 61 && !var7) {
         this.bytecode.addOpcode(89);
      }

      int var8;
      if (var2 == 61) {
         FieldInfo var9 = var6.getFieldInfo2();
         this.setFieldType(var9);
         AccessorMaker var10 = this.isAccessibleField(var6, var9);
         if (var10 == null) {
            var8 = this.addFieldrefInfo(var6, var9);
         } else {
            var8 = 0;
         }
      } else {
         var8 = this.atFieldRead(var6, var7);
      }

      int var14 = this.exprType;
      int var15 = this.arrayDim;
      String var11 = this.className;
      this.atAssignCore(var1, var2, var4, var14, var15, var11);
      boolean var12 = is2word(var14, var15);
      if (var5) {
         int var13;
         if (var7) {
            var13 = var12 ? 92 : 89;
         } else {
            var13 = var12 ? 93 : 90;
         }

         this.bytecode.addOpcode(var13);
      }

      this.atFieldAssignCore(var6, var7, var8, var12);
      this.exprType = var14;
      this.arrayDim = var15;
      this.className = var11;
   }

   private void atFieldAssignCore(CtField var1, boolean var2, int var3, boolean var4) throws CompileError {
      if (var3 != 0) {
         if (var2) {
            this.bytecode.add(179);
            this.bytecode.growStack(var4 ? -2 : -1);
         } else {
            this.bytecode.add(181);
            this.bytecode.growStack(var4 ? -3 : -2);
         }

         this.bytecode.addIndex(var3);
      } else {
         CtClass var5 = var1.getDeclaringClass();
         AccessorMaker var6 = var5.getAccessorMaker();
         FieldInfo var7 = var1.getFieldInfo2();
         MethodInfo var8 = var6.getFieldSetter(var7, var2);
         this.bytecode.addInvokestatic(var5, var8.getName(), var8.getDescriptor());
      }

   }

   public void atMember(Member var1) throws CompileError {
      this.atFieldRead(var1);
   }

   protected void atFieldRead(ASTree var1) throws CompileError {
      CtField var2 = this.fieldAccess(var1, true);
      if (var2 == null) {
         this.atArrayLength(var1);
      } else {
         boolean var3 = this.resultStatic;
         ASTree var4 = TypeChecker.getConstantFieldValue(var2);
         if (var4 == null) {
            this.atFieldRead(var2, var3);
         } else {
            var4.accept(this);
            this.setFieldType(var2.getFieldInfo2());
         }

      }
   }

   private void atArrayLength(ASTree var1) throws CompileError {
      if (this.arrayDim == 0) {
         throw new CompileError(".length applied to a non array");
      } else {
         this.bytecode.addOpcode(190);
         this.exprType = 324;
         this.arrayDim = 0;
      }
   }

   private int atFieldRead(CtField var1, boolean var2) throws CompileError {
      FieldInfo var3 = var1.getFieldInfo2();
      boolean var4 = this.setFieldType(var3);
      AccessorMaker var5 = this.isAccessibleField(var1, var3);
      if (var5 != null) {
         MethodInfo var7 = var5.getFieldGetter(var3, var2);
         this.bytecode.addInvokestatic(var1.getDeclaringClass(), var7.getName(), var7.getDescriptor());
         return 0;
      } else {
         int var6 = this.addFieldrefInfo(var1, var3);
         if (var2) {
            this.bytecode.add(178);
            this.bytecode.growStack(var4 ? 2 : 1);
         } else {
            this.bytecode.add(180);
            this.bytecode.growStack(var4 ? 1 : 0);
         }

         this.bytecode.addIndex(var6);
         return var6;
      }
   }

   private AccessorMaker isAccessibleField(CtField var1, FieldInfo var2) throws CompileError {
      if (AccessFlag.isPrivate(var2.getAccessFlags()) && var1.getDeclaringClass() != this.thisClass) {
         CtClass var3 = var1.getDeclaringClass();
         if (this.thisClass != null) {
            AccessorMaker var4 = var3.getAccessorMaker();
            if (var4 != null) {
               return var4;
            } else {
               throw new CompileError("fatal error.  bug?");
            }
         } else {
            throw new CompileError("Field " + var1.getName() + " in " + var3.getName() + " is private.");
         }
      } else {
         return null;
      }
   }

   private boolean setFieldType(FieldInfo var1) throws CompileError {
      String var2 = var1.getDescriptor();
      int var3 = 0;
      int var4 = 0;

      char var5;
      for(var5 = var2.charAt(var3); var5 == '['; var5 = var2.charAt(var3)) {
         ++var4;
         ++var3;
      }

      this.arrayDim = var4;
      this.exprType = MemberResolver.descToType(var5);
      if (var5 == 'L') {
         this.className = var2.substring(var3 + 1, var2.indexOf(59, var3 + 1));
      } else {
         this.className = null;
      }

      boolean var6 = var4 == 0 && (var5 == 'J' || var5 == 'D');
      return var6;
   }

   private int addFieldrefInfo(CtField var1, FieldInfo var2) {
      ConstPool var3 = this.bytecode.getConstPool();
      String var4 = var1.getDeclaringClass().getName();
      int var5 = var3.addClassInfo(var4);
      String var6 = var2.getName();
      String var7 = var2.getDescriptor();
      return var3.addFieldrefInfo(var5, var6, var7);
   }

   protected void atClassObject2(String var1) throws CompileError {
      if (this.getMajorVersion() < 49) {
         super.atClassObject2(var1);
      } else {
         this.bytecode.addLdc(this.bytecode.getConstPool().addClassInfo(var1));
      }

   }

   protected void atFieldPlusPlus(int var1, boolean var2, ASTree var3, Expr var4, boolean var5) throws CompileError {
      CtField var6 = this.fieldAccess(var3, false);
      boolean var7 = this.resultStatic;
      if (!var7) {
         this.bytecode.addOpcode(89);
      }

      int var8 = this.atFieldRead(var6, var7);
      int var9 = this.exprType;
      boolean var10 = is2word(var9, this.arrayDim);
      int var11;
      if (var7) {
         var11 = var10 ? 92 : 89;
      } else {
         var11 = var10 ? 93 : 90;
      }

      this.atPlusPlusCore(var11, var5, var1, var2, var4);
      this.atFieldAssignCore(var6, var7, var8, var10);
   }

   protected CtField fieldAccess(ASTree var1, boolean var2) throws CompileError {
      if (var1 instanceof Member) {
         String var11 = ((Member)var1).get();
         CtField var12 = null;

         try {
            var12 = this.thisClass.getField(var11);
         } catch (NotFoundException var9) {
            throw new NoFieldException(var11, var1);
         }

         boolean var13 = Modifier.isStatic(var12.getModifiers());
         if (!var13) {
            if (this.inStaticMethod) {
               throw new CompileError("not available in a static method: " + var11);
            }

            this.bytecode.addAload(0);
         }

         this.resultStatic = var13;
         return var12;
      } else {
         if (var1 instanceof Expr) {
            Expr var3 = (Expr)var1;
            int var4 = var3.getOperator();
            CtField var5;
            if (var4 == 35) {
               var5 = this.resolver.lookupField(((Symbol)var3.oprand1()).get(), (Symbol)var3.oprand2());
               this.resultStatic = true;
               return var5;
            }

            if (var4 == 46) {
               var5 = null;

               try {
                  var3.oprand1().accept(this);
                  if (this.exprType == 307 && this.arrayDim == 0) {
                     var5 = this.resolver.lookupFieldByJvmName(this.className, (Symbol)var3.oprand2());
                  } else {
                     if (var2 && this.arrayDim > 0 && ((Symbol)var3.oprand2()).get().equals("length")) {
                        return null;
                     }

                     badLvalue();
                  }

                  boolean var6 = Modifier.isStatic(var5.getModifiers());
                  if (var6) {
                     this.bytecode.addOpcode(87);
                  }

                  this.resultStatic = var6;
                  return var5;
               } catch (NoFieldException var10) {
                  if (var10.getExpr() != var3.oprand1()) {
                     throw var10;
                  }

                  Symbol var7 = (Symbol)var3.oprand2();
                  String var8 = var10.getField();
                  var5 = this.resolver.lookupFieldByJvmName2(var8, var7, var1);
                  this.resultStatic = true;
                  return var5;
               }
            }

            badLvalue();
         } else {
            badLvalue();
         }

         this.resultStatic = false;
         return null;
      }
   }

   private static void badLvalue() throws CompileError {
      throw new CompileError("bad l-value");
   }

   public CtClass[] makeParamList(MethodDecl var1) throws CompileError {
      ASTList var3 = var1.getParams();
      CtClass[] var2;
      if (var3 == null) {
         var2 = new CtClass[0];
      } else {
         int var4 = 0;

         for(var2 = new CtClass[var3.length()]; var3 != null; var3 = var3.tail()) {
            var2[var4++] = this.resolver.lookupClass((Declarator)var3.head());
         }
      }

      return var2;
   }

   public CtClass[] makeThrowsList(MethodDecl var1) throws CompileError {
      ASTList var3 = var1.getThrows();
      if (var3 == null) {
         return null;
      } else {
         int var4 = 0;

         CtClass[] var2;
         for(var2 = new CtClass[var3.length()]; var3 != null; var3 = var3.tail()) {
            var2[var4++] = this.resolver.lookupClassByName((ASTList)var3.head());
         }

         return var2;
      }
   }

   protected String resolveClassName(ASTList var1) throws CompileError {
      return this.resolver.resolveClassName(var1);
   }

   protected String resolveClassName(String var1) throws CompileError {
      return this.resolver.resolveJvmClassName(var1);
   }

   static class JsrHook2 extends CodeGen.ReturnHook {
      int var;
      int target;

      JsrHook2(CodeGen var1, int[] var2) {
         super(var1);
         this.target = var2[0];
         this.var = var2[1];
      }

      protected boolean doit(Bytecode var1, int var2) {
         switch(var2) {
         case 172:
            var1.addIstore(this.var);
            break;
         case 173:
            var1.addLstore(this.var);
            break;
         case 174:
            var1.addFstore(this.var);
            break;
         case 175:
            var1.addDstore(this.var);
            break;
         case 176:
            var1.addAstore(this.var);
         case 177:
            break;
         default:
            throw new RuntimeException("fatal");
         }

         var1.addOpcode(167);
         var1.addIndex(this.target - var1.currentPc() + 3);
         return true;
      }
   }

   static class JsrHook extends CodeGen.ReturnHook {
      ArrayList jsrList = new ArrayList();
      CodeGen cgen;
      int var;

      JsrHook(CodeGen var1) {
         super(var1);
         this.cgen = var1;
         this.var = -1;
      }

      private int getVar(int var1) {
         if (this.var < 0) {
            this.var = this.cgen.getMaxLocals();
            this.cgen.incMaxLocals(var1);
         }

         return this.var;
      }

      private void jsrJmp(Bytecode var1) {
         var1.addOpcode(167);
         this.jsrList.add(new int[]{var1.currentPc(), this.var});
         var1.addIndex(0);
      }

      protected boolean doit(Bytecode var1, int var2) {
         switch(var2) {
         case 172:
            var1.addIstore(this.getVar(1));
            this.jsrJmp(var1);
            var1.addIload(this.var);
            break;
         case 173:
            var1.addLstore(this.getVar(2));
            this.jsrJmp(var1);
            var1.addLload(this.var);
            break;
         case 174:
            var1.addFstore(this.getVar(1));
            this.jsrJmp(var1);
            var1.addFload(this.var);
            break;
         case 175:
            var1.addDstore(this.getVar(2));
            this.jsrJmp(var1);
            var1.addDload(this.var);
            break;
         case 176:
            var1.addAstore(this.getVar(1));
            this.jsrJmp(var1);
            var1.addAload(this.var);
            break;
         case 177:
            this.jsrJmp(var1);
            break;
         default:
            throw new RuntimeException("fatal");
         }

         return false;
      }
   }
}
