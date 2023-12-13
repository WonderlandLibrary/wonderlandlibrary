package javassist.compiler;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
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
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.Visitor;

public class TypeChecker extends Visitor implements Opcode, TokenId {
   static final String javaLangObject = "java.lang.Object";
   static final String jvmJavaLangObject = "java/lang/Object";
   static final String jvmJavaLangString = "java/lang/String";
   static final String jvmJavaLangClass = "java/lang/Class";
   protected int exprType;
   protected int arrayDim;
   protected String className;
   protected MemberResolver resolver;
   protected CtClass thisClass;
   protected MethodInfo thisMethod;

   public TypeChecker(CtClass var1, ClassPool var2) {
      this.resolver = new MemberResolver(var2);
      this.thisClass = var1;
      this.thisMethod = null;
   }

   protected static String argTypesToString(int[] var0, int[] var1, String[] var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append('(');
      int var4 = var0.length;
      if (var4 > 0) {
         int var5 = 0;

         while(true) {
            typeToString(var3, var0[var5], var1[var5], var2[var5]);
            ++var5;
            if (var5 >= var4) {
               break;
            }

            var3.append(',');
         }
      }

      var3.append(')');
      return var3.toString();
   }

   protected static StringBuffer typeToString(StringBuffer var0, int var1, int var2, String var3) {
      String var4;
      if (var1 == 307) {
         var4 = MemberResolver.jvmToJavaName(var3);
      } else if (var1 == 412) {
         var4 = "Object";
      } else {
         try {
            var4 = MemberResolver.getTypeName(var1);
         } catch (CompileError var6) {
            var4 = "?";
         }
      }

      var0.append(var4);

      while(var2-- > 0) {
         var0.append("[]");
      }

      return var0;
   }

   public void setThisMethod(MethodInfo var1) {
      this.thisMethod = var1;
   }

   protected static void fatal() throws CompileError {
      throw new CompileError("fatal");
   }

   protected String getThisName() {
      return MemberResolver.javaToJvmName(this.thisClass.getName());
   }

   protected String getSuperName() throws CompileError {
      return MemberResolver.javaToJvmName(MemberResolver.getSuperclass(this.thisClass).getName());
   }

   protected String resolveClassName(ASTList var1) throws CompileError {
      return this.resolver.resolveClassName(var1);
   }

   protected String resolveClassName(String var1) throws CompileError {
      return this.resolver.resolveJvmClassName(var1);
   }

   public void atNewExpr(NewExpr var1) throws CompileError {
      if (var1.isArray()) {
         this.atNewArrayExpr(var1);
      } else {
         CtClass var2 = this.resolver.lookupClassByName(var1.getClassName());
         String var3 = var2.getName();
         ASTList var4 = var1.getArguments();
         this.atMethodCallCore(var2, "<init>", var4);
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
      if (var5 != null) {
         var5.accept(this);
      }

      if (var3.length() > 1) {
         this.atMultiNewArray(var2, var4, var3);
      } else {
         ASTree var6 = var3.head();
         if (var6 != null) {
            var6.accept(this);
         }

         this.exprType = var2;
         this.arrayDim = 1;
         if (var2 == 307) {
            this.className = this.resolveClassName(var4);
         } else {
            this.className = null;
         }
      }

   }

   public void atArrayInit(ArrayInit var1) throws CompileError {
      Object var2 = var1;

      while(var2 != null) {
         ASTree var3 = ((ASTList)var2).head();
         var2 = ((ASTList)var2).tail();
         if (var3 != null) {
            var3.accept(this);
         }
      }

   }

   protected void atMultiNewArray(int var1, ASTList var2, ASTList var3) throws CompileError {
      int var5 = var3.length();

      for(int var4 = 0; var3 != null; var3 = var3.tail()) {
         ASTree var6 = var3.head();
         if (var6 == null) {
            break;
         }

         ++var4;
         var6.accept(this);
      }

      this.exprType = var1;
      this.arrayDim = var5;
      if (var1 == 307) {
         this.className = this.resolveClassName(var2);
      } else {
         this.className = null;
      }

   }

   public void atAssignExpr(AssignExpr var1) throws CompileError {
      int var2 = var1.getOperator();
      ASTree var3 = var1.oprand1();
      ASTree var4 = var1.oprand2();
      if (var3 instanceof Variable) {
         this.atVariableAssign(var1, var2, (Variable)var3, ((Variable)var3).getDeclarator(), var4);
      } else {
         if (var3 instanceof Expr) {
            Expr var5 = (Expr)var3;
            if (var5.getOperator() == 65) {
               this.atArrayAssign(var1, var2, (Expr)var3, var4);
               return;
            }
         }

         this.atFieldAssign(var1, var2, var3, var4);
      }

   }

   private void atVariableAssign(Expr var1, int var2, Variable var3, Declarator var4, ASTree var5) throws CompileError {
      int var6 = var4.getType();
      int var7 = var4.getArrayDim();
      String var8 = var4.getClassName();
      if (var2 != 61) {
         this.atVariable(var3);
      }

      var5.accept(this);
      this.exprType = var6;
      this.arrayDim = var7;
      this.className = var8;
   }

   private void atArrayAssign(Expr var1, int var2, Expr var3, ASTree var4) throws CompileError {
      this.atArrayRead(var3.oprand1(), var3.oprand2());
      int var5 = this.exprType;
      int var6 = this.arrayDim;
      String var7 = this.className;
      var4.accept(this);
      this.exprType = var5;
      this.arrayDim = var6;
      this.className = var7;
   }

   protected void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4) throws CompileError {
      CtField var5 = this.fieldAccess(var3);
      this.atFieldRead(var5);
      int var6 = this.exprType;
      int var7 = this.arrayDim;
      String var8 = this.className;
      var4.accept(this);
      this.exprType = var6;
      this.arrayDim = var7;
      this.className = var8;
   }

   public void atCondExpr(CondExpr var1) throws CompileError {
      this.booleanExpr(var1.condExpr());
      var1.thenExpr().accept(this);
      int var2 = this.exprType;
      int var3 = this.arrayDim;
      String var4 = this.className;
      var1.elseExpr().accept(this);
      if (var3 == 0 && var3 == this.arrayDim) {
         if (CodeGen.rightIsStrong(var2, this.exprType)) {
            var1.setThen(new CastExpr(this.exprType, 0, var1.thenExpr()));
         } else if (CodeGen.rightIsStrong(this.exprType, var2)) {
            var1.setElse(new CastExpr(var2, 0, var1.elseExpr()));
            this.exprType = var2;
         }
      }

   }

   public void atBinExpr(BinExpr var1) throws CompileError {
      int var2 = var1.getOperator();
      int var3 = CodeGen.lookupBinOp(var2);
      if (var3 >= 0) {
         if (var2 == 43) {
            Expr var4 = this.atPlusExpr(var1);
            if (var4 != null) {
               CallExpr var7 = CallExpr.makeCall(Expr.make(46, var4, new Member("toString")), (ASTree)null);
               var1.setOprand1(var7);
               var1.setOprand2((ASTree)null);
               this.className = "java/lang/String";
            }
         } else {
            ASTree var8 = var1.oprand1();
            ASTree var5 = var1.oprand2();
            var8.accept(this);
            int var6 = this.exprType;
            var5.accept(this);
            if (var5 != false) {
               this.computeBinExprType(var1, var2, var6);
            }
         }
      } else {
         this.booleanExpr(var1);
      }

   }

   private Expr atPlusExpr(BinExpr var1) throws CompileError {
      ASTree var2 = var1.oprand1();
      ASTree var3 = var1.oprand2();
      if (var3 == null) {
         var2.accept(this);
         return null;
      } else {
         if (var2 != false) {
            Expr var4 = this.atPlusExpr((BinExpr)var2);
            if (var4 != null) {
               var3.accept(this);
               this.exprType = 307;
               this.arrayDim = 0;
               this.className = "java/lang/StringBuffer";
               return makeAppendCall(var4, var3);
            }
         } else {
            var2.accept(this);
         }

         int var9 = this.exprType;
         int var5 = this.arrayDim;
         String var6 = this.className;
         var3.accept(this);
         boolean var10002 = true;
         if (var3 != false) {
            return null;
         } else if ((var9 != 307 || var5 != 0 || !"java/lang/String".equals(var6)) && (this.exprType != 307 || this.arrayDim != 0 || !"java/lang/String".equals(this.className))) {
            this.computeBinExprType(var1, 43, var9);
            return null;
         } else {
            ASTList var7 = ASTList.make(new Symbol("java"), new Symbol("lang"), new Symbol("StringBuffer"));
            NewExpr var8 = new NewExpr(var7, (ASTList)null);
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/StringBuffer";
            return makeAppendCall(makeAppendCall(var8, var2), var3);
         }
      }
   }

   static ASTree stripPlusExpr(ASTree var0) {
      if (var0 instanceof BinExpr) {
         BinExpr var1 = (BinExpr)var0;
         if (var1.getOperator() == 43 && var1.oprand2() == null) {
            return var1.getLeft();
         }
      } else if (var0 instanceof Expr) {
         Expr var4 = (Expr)var0;
         int var2 = var4.getOperator();
         if (var2 == 35) {
            ASTree var3 = getConstantFieldValue((Member)var4.oprand2());
            if (var3 != null) {
               return var3;
            }
         } else if (var2 == 43 && var4.getRight() == null) {
            return var4.getLeft();
         }
      } else if (var0 instanceof Member) {
         ASTree var5 = getConstantFieldValue((Member)var0);
         if (var5 != null) {
            return var5;
         }
      }

      return var0;
   }

   private static ASTree getConstantFieldValue(Member var0) {
      return getConstantFieldValue(var0.getField());
   }

   public static ASTree getConstantFieldValue(CtField var0) {
      if (var0 == null) {
         return null;
      } else {
         Object var1 = var0.getConstantValue();
         if (var1 == null) {
            return null;
         } else if (var1 instanceof String) {
            return new StringL((String)var1);
         } else {
            int var2;
            if (!(var1 instanceof Double) && !(var1 instanceof Float)) {
               if (var1 instanceof Number) {
                  var2 = var1 instanceof Long ? 403 : 402;
                  return new IntConst(((Number)var1).longValue(), var2);
               } else {
                  return var1 instanceof Boolean ? new Keyword((Boolean)var1 ? 410 : 411) : null;
               }
            } else {
               var2 = var1 instanceof Double ? 405 : 404;
               return new DoubleConst(((Number)var1).doubleValue(), var2);
            }
         }
      }
   }

   private static Expr makeAppendCall(ASTree var0, ASTree var1) {
      return CallExpr.makeCall(Expr.make(46, var0, new Member("append")), new ASTList(var1));
   }

   private void computeBinExprType(BinExpr var1, int var2, int var3) throws CompileError {
      int var4 = this.exprType;
      if (var2 != 364 && var2 != 366 && var2 != 370) {
         this.insertCast(var1, var3, var4);
      } else {
         this.exprType = var3;
      }

      if (CodeGen.isP_INT(this.exprType) && this.exprType != 301) {
         this.exprType = 324;
      }

   }

   private void booleanExpr(ASTree var1) throws CompileError {
      int var2 = CodeGen.getCompOperator(var1);
      BinExpr var3;
      if (var2 == 358) {
         var3 = (BinExpr)var1;
         var3.oprand1().accept(this);
         int var4 = this.exprType;
         int var5 = this.arrayDim;
         var3.oprand2().accept(this);
         if (var5 == 0 && this.arrayDim == 0) {
            this.insertCast(var3, var4, this.exprType);
         }
      } else if (var2 == 33) {
         ((Expr)var1).oprand1().accept(this);
      } else if (var2 != 369 && var2 != 368) {
         var1.accept(this);
      } else {
         var3 = (BinExpr)var1;
         var3.oprand1().accept(this);
         var3.oprand2().accept(this);
      }

      this.exprType = 301;
      this.arrayDim = 0;
   }

   private void insertCast(BinExpr var1, int var2, int var3) throws CompileError {
      if (CodeGen.rightIsStrong(var2, var3)) {
         var1.setLeft(new CastExpr(var3, 0, var1.oprand1()));
      } else {
         this.exprType = var2;
      }

   }

   public void atCastExpr(CastExpr var1) throws CompileError {
      String var2 = this.resolveClassName(var1.getClassName());
      var1.getOprand().accept(this);
      this.exprType = var1.getType();
      this.arrayDim = var1.getArrayDim();
      this.className = var2;
   }

   public void atInstanceOfExpr(InstanceOfExpr var1) throws CompileError {
      var1.getOprand().accept(this);
      this.exprType = 301;
      this.arrayDim = 0;
   }

   public void atExpr(Expr var1) throws CompileError {
      int var2 = var1.getOperator();
      ASTree var3 = var1.oprand1();
      String var4;
      if (var2 == 46) {
         var4 = ((Symbol)var1.oprand2()).get();
         if (var4.equals("length")) {
            try {
               this.atArrayLength(var1);
            } catch (NoFieldException var6) {
               this.atFieldRead((ASTree)var1);
            }
         } else if (var4.equals("class")) {
            this.atClassObject(var1);
         } else {
            this.atFieldRead((ASTree)var1);
         }
      } else if (var2 == 35) {
         var4 = ((Symbol)var1.oprand2()).get();
         if (var4.equals("class")) {
            this.atClassObject(var1);
         } else {
            this.atFieldRead((ASTree)var1);
         }
      } else if (var2 == 65) {
         this.atArrayRead(var3, var1.oprand2());
      } else if (var2 != 362 && var2 != 363) {
         if (var2 == 33) {
            this.booleanExpr(var1);
         } else if (var2 == 67) {
            fatal();
         } else {
            var3.accept(this);
            if (var3 != false && (var2 == 45 || var2 == 126) && CodeGen.isP_INT(this.exprType)) {
               this.exprType = 324;
            }
         }
      } else {
         this.atPlusPlus(var2, var3, var1);
      }

   }

   public void atCallExpr(CallExpr var1) throws CompileError {
      String var2 = null;
      CtClass var3 = null;
      ASTree var4 = var1.oprand1();
      ASTList var5 = (ASTList)var1.oprand2();
      if (var4 instanceof Member) {
         var2 = ((Member)var4).get();
         var3 = this.thisClass;
      } else if (var4 instanceof Keyword) {
         var2 = "<init>";
         if (((Keyword)var4).get() == 336) {
            var3 = MemberResolver.getSuperclass(this.thisClass);
         } else {
            var3 = this.thisClass;
         }
      } else if (!(var4 instanceof Expr)) {
         fatal();
      } else {
         Expr var6 = (Expr)var4;
         var2 = ((Symbol)var6.oprand2()).get();
         int var7 = var6.getOperator();
         if (var7 == 35) {
            var3 = this.resolver.lookupClass(((Symbol)var6.oprand1()).get(), false);
         } else if (var7 != 46) {
            badMethod();
         } else {
            ASTree var8 = var6.oprand1();
            String var9 = isDotSuper(var8);
            if (var9 != null) {
               var3 = MemberResolver.getSuperInterface(this.thisClass, var9);
            } else {
               try {
                  var8.accept(this);
               } catch (NoFieldException var11) {
                  if (var11.getExpr() != var8) {
                     throw var11;
                  }

                  this.exprType = 307;
                  this.arrayDim = 0;
                  this.className = var11.getField();
                  var6.setOperator(35);
                  var6.setOprand1(new Symbol(MemberResolver.jvmToJavaName(this.className)));
               }

               if (this.arrayDim > 0) {
                  var3 = this.resolver.lookupClass("java.lang.Object", true);
               } else if (this.exprType == 307) {
                  var3 = this.resolver.lookupClassByJvmName(this.className);
               } else {
                  badMethod();
               }
            }
         }
      }

      MemberResolver.Method var12 = this.atMethodCallCore(var3, var2, var5);
      var1.setMethod(var12);
   }

   private static void badMethod() throws CompileError {
      throw new CompileError("bad method");
   }

   static String isDotSuper(ASTree var0) {
      if (var0 instanceof Expr) {
         Expr var1 = (Expr)var0;
         if (var1.getOperator() == 46) {
            ASTree var2 = var1.oprand2();
            if (var2 instanceof Keyword && ((Keyword)var2).get() == 336) {
               return ((Symbol)var1.oprand1()).get();
            }
         }
      }

      return null;
   }

   public MemberResolver.Method atMethodCallCore(CtClass var1, String var2, ASTList var3) throws CompileError {
      int var4 = this.getMethodArgsLength(var3);
      int[] var5 = new int[var4];
      int[] var6 = new int[var4];
      String[] var7 = new String[var4];
      this.atMethodArgs(var3, var5, var6, var7);
      MemberResolver.Method var8 = this.resolver.lookupMethod(var1, this.thisClass, this.thisMethod, var2, var5, var6, var7);
      String var9;
      if (var8 == null) {
         var9 = var1.getName();
         String var10 = argTypesToString(var5, var6, var7);
         String var11;
         if (var2.equals("<init>")) {
            var11 = "cannot find constructor " + var9 + var10;
         } else {
            var11 = var2 + var10 + " not found in " + var9;
         }

         throw new CompileError(var11);
      } else {
         var9 = var8.info.getDescriptor();
         this.setReturnType(var9);
         return var8;
      }
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

   void setReturnType(String var1) throws CompileError {
      int var2 = var1.indexOf(41);
      if (var2 < 0) {
         badMethod();
      }

      ++var2;
      char var3 = var1.charAt(var2);

      int var4;
      for(var4 = 0; var3 == '['; var3 = var1.charAt(var2)) {
         ++var4;
         ++var2;
      }

      this.arrayDim = var4;
      if (var3 == 'L') {
         int var5 = var1.indexOf(59, var2 + 1);
         if (var5 < 0) {
            badMethod();
         }

         this.exprType = 307;
         this.className = var1.substring(var2 + 1, var5);
      } else {
         this.exprType = MemberResolver.descToType(var3);
         this.className = null;
      }

   }

   private void atFieldRead(ASTree var1) throws CompileError {
      this.atFieldRead(this.fieldAccess(var1));
   }

   private void atFieldRead(CtField var1) throws CompileError {
      FieldInfo var2 = var1.getFieldInfo2();
      String var3 = var2.getDescriptor();
      int var4 = 0;
      int var5 = 0;

      char var6;
      for(var6 = var3.charAt(var4); var6 == '['; var6 = var3.charAt(var4)) {
         ++var5;
         ++var4;
      }

      this.arrayDim = var5;
      this.exprType = MemberResolver.descToType(var6);
      if (var6 == 'L') {
         this.className = var3.substring(var4 + 1, var3.indexOf(59, var4 + 1));
      } else {
         this.className = null;
      }

   }

   protected CtField fieldAccess(ASTree var1) throws CompileError {
      if (var1 instanceof Member) {
         Member var9 = (Member)var1;
         String var10 = var9.get();

         try {
            CtField var12 = this.thisClass.getField(var10);
            if (Modifier.isStatic(var12.getModifiers())) {
               var9.setField(var12);
            }

            return var12;
         } catch (NotFoundException var6) {
            throw new NoFieldException(var10, var1);
         }
      } else {
         if (var1 instanceof Expr) {
            Expr var2 = (Expr)var1;
            int var3 = var2.getOperator();
            if (var3 == 35) {
               Member var11 = (Member)var2.oprand2();
               CtField var13 = this.resolver.lookupField(((Symbol)var2.oprand1()).get(), var11);
               var11.setField(var13);
               return var13;
            }

            if (var3 == 46) {
               try {
                  var2.oprand1().accept(this);
               } catch (NoFieldException var8) {
                  if (var8.getExpr() != var2.oprand1()) {
                     throw var8;
                  }

                  return this.fieldAccess2(var2, var8.getField());
               }

               CompileError var4 = null;

               try {
                  if (this.exprType == 307 && this.arrayDim == 0) {
                     return this.resolver.lookupFieldByJvmName(this.className, (Symbol)var2.oprand2());
                  }
               } catch (CompileError var7) {
                  var4 = var7;
               }

               ASTree var5 = var2.oprand1();
               if (var5 instanceof Symbol) {
                  return this.fieldAccess2(var2, ((Symbol)var5).get());
               }

               if (var4 != null) {
                  throw var4;
               }
            }
         }

         throw new CompileError("bad filed access");
      }
   }

   private CtField fieldAccess2(Expr var1, String var2) throws CompileError {
      Member var3 = (Member)var1.oprand2();
      CtField var4 = this.resolver.lookupFieldByJvmName2(var2, var3, var1);
      var1.setOperator(35);
      var1.setOprand1(new Symbol(MemberResolver.jvmToJavaName(var2)));
      var3.setField(var4);
      return var4;
   }

   public void atClassObject(Expr var1) throws CompileError {
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/Class";
   }

   public void atArrayLength(Expr var1) throws CompileError {
      var1.oprand1().accept(this);
      if (this.arrayDim == 0) {
         throw new NoFieldException("length", var1);
      } else {
         this.exprType = 324;
         this.arrayDim = 0;
      }
   }

   public void atArrayRead(ASTree var1, ASTree var2) throws CompileError {
      var1.accept(this);
      int var3 = this.exprType;
      int var4 = this.arrayDim;
      String var5 = this.className;
      var2.accept(this);
      this.exprType = var3;
      this.arrayDim = var4 - 1;
      this.className = var5;
   }

   private void atPlusPlus(int var1, ASTree var2, Expr var3) throws CompileError {
      boolean var4 = var2 == null;
      if (var4) {
         var2 = var3.oprand2();
      }

      if (var2 instanceof Variable) {
         Declarator var5 = ((Variable)var2).getDeclarator();
         this.exprType = var5.getType();
         this.arrayDim = var5.getArrayDim();
      } else {
         if (var2 instanceof Expr) {
            Expr var7 = (Expr)var2;
            if (var7.getOperator() == 65) {
               this.atArrayRead(var7.oprand1(), var7.oprand2());
               int var6 = this.exprType;
               if (var6 == 324 || var6 == 303 || var6 == 306 || var6 == 334) {
                  this.exprType = 324;
               }

               return;
            }
         }

         this.atFieldPlusPlus(var2);
      }

   }

   protected void atFieldPlusPlus(ASTree var1) throws CompileError {
      CtField var2 = this.fieldAccess(var1);
      this.atFieldRead(var2);
      int var3 = this.exprType;
      if (var3 == 324 || var3 == 303 || var3 == 306 || var3 == 334) {
         this.exprType = 324;
      }

   }

   public void atMember(Member var1) throws CompileError {
      this.atFieldRead((ASTree)var1);
   }

   public void atVariable(Variable var1) throws CompileError {
      Declarator var2 = var1.getDeclarator();
      this.exprType = var2.getType();
      this.arrayDim = var2.getArrayDim();
      this.className = var2.getClassName();
   }

   public void atKeyword(Keyword var1) throws CompileError {
      this.arrayDim = 0;
      int var2 = var1.get();
      switch(var2) {
      case 336:
      case 339:
         this.exprType = 307;
         if (var2 == 339) {
            this.className = this.getThisName();
         } else {
            this.className = this.getSuperName();
         }
         break;
      case 410:
      case 411:
         this.exprType = 301;
         break;
      case 412:
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
   }

   public void atIntConst(IntConst var1) throws CompileError {
      this.arrayDim = 0;
      int var2 = var1.getType();
      if (var2 != 402 && var2 != 401) {
         this.exprType = 326;
      } else {
         this.exprType = var2 == 402 ? 324 : 306;
      }

   }

   public void atDoubleConst(DoubleConst var1) throws CompileError {
      this.arrayDim = 0;
      if (var1.getType() == 405) {
         this.exprType = 312;
      } else {
         this.exprType = 317;
      }

   }
}
