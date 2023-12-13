package javassist.compiler;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.Symbol;

public class Javac {
   JvstCodeGen gen;
   SymbolTable stable;
   private Bytecode bytecode;
   public static final String param0Name = "$0";
   public static final String resultVarName = "$_";
   public static final String proceedName = "$proceed";

   public Javac(CtClass var1) {
      this(new Bytecode(var1.getClassFile2().getConstPool(), 0, 0), var1);
   }

   public Javac(Bytecode var1, CtClass var2) {
      this.gen = new JvstCodeGen(var1, var2, var2.getClassPool());
      this.stable = new SymbolTable();
      this.bytecode = var1;
   }

   public Bytecode getBytecode() {
      return this.bytecode;
   }

   public CtMember compile(String var1) throws CompileError {
      Parser var2 = new Parser(new Lex(var1));
      ASTList var3 = var2.parseMember1(this.stable);

      try {
         if (var3 instanceof FieldDecl) {
            return this.compileField((FieldDecl)var3);
         } else {
            CtBehavior var4 = this.compileMethod(var2, (MethodDecl)var3);
            CtClass var5 = var4.getDeclaringClass();
            var4.getMethodInfo2().rebuildStackMapIf6(var5.getClassPool(), var5.getClassFile2());
            return var4;
         }
      } catch (BadBytecode var6) {
         throw new CompileError(var6.getMessage());
      } catch (CannotCompileException var7) {
         throw new CompileError(var7.getMessage());
      }
   }

   private CtField compileField(FieldDecl var1) throws CompileError, CannotCompileException {
      Declarator var3 = var1.getDeclarator();
      Javac.CtFieldWithInit var2 = new Javac.CtFieldWithInit(this.gen.resolver.lookupClass(var3), var3.getVariable().get(), this.gen.getThisClass());
      var2.setModifiers(MemberResolver.getModifiers(var1.getModifiers()));
      if (var1.getInit() != null) {
         var2.setInit(var1.getInit());
      }

      return var2;
   }

   private CtBehavior compileMethod(Parser var1, MethodDecl var2) throws CompileError {
      int var3 = MemberResolver.getModifiers(var2.getModifiers());
      CtClass[] var4 = this.gen.makeParamList(var2);
      CtClass[] var5 = this.gen.makeThrowsList(var2);
      this.recordParams(var4, Modifier.isStatic(var3));
      var2 = var1.parseMethod2(this.stable, var2);

      try {
         if (var2.isConstructor()) {
            CtConstructor var10 = new CtConstructor(var4, this.gen.getThisClass());
            var10.setModifiers(var3);
            var2.accept(this.gen);
            var10.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
            var10.setExceptionTypes(var5);
            return var10;
         } else {
            Declarator var6 = var2.getReturn();
            CtClass var7 = this.gen.resolver.lookupClass(var6);
            this.recordReturnType(var7, false);
            CtMethod var8 = new CtMethod(var7, var6.getVariable().get(), var4, this.gen.getThisClass());
            var8.setModifiers(var3);
            this.gen.setThisMethod(var8);
            var2.accept(this.gen);
            if (var2.getBody() != null) {
               var8.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
            } else {
               var8.setModifiers(var3 | 1024);
            }

            var8.setExceptionTypes(var5);
            return var8;
         }
      } catch (NotFoundException var9) {
         throw new CompileError(var9.toString());
      }
   }

   public Bytecode compileBody(CtBehavior var1, String var2) throws CompileError {
      try {
         int var3 = var1.getModifiers();
         this.recordParams(var1.getParameterTypes(), Modifier.isStatic(var3));
         CtClass var4;
         if (var1 instanceof CtMethod) {
            this.gen.setThisMethod((CtMethod)var1);
            var4 = ((CtMethod)var1).getReturnType();
         } else {
            var4 = CtClass.voidType;
         }

         this.recordReturnType(var4, false);
         boolean var5 = var4 == CtClass.voidType;
         if (var2 == null) {
            makeDefaultBody(this.bytecode, var4);
         } else {
            Parser var6 = new Parser(new Lex(var2));
            SymbolTable var7 = new SymbolTable(this.stable);
            Stmnt var8 = var6.parseStatement(var7);
            if (var6.hasMore()) {
               throw new CompileError("the method/constructor body must be surrounded by {}");
            }

            boolean var9 = false;
            if (var1 instanceof CtConstructor) {
               var9 = !((CtConstructor)var1).isClassInitializer();
            }

            this.gen.atMethodBody(var8, var9, var5);
         }

         return this.bytecode;
      } catch (NotFoundException var10) {
         throw new CompileError(var10.toString());
      }
   }

   private static void makeDefaultBody(Bytecode var0, CtClass var1) {
      int var2;
      byte var3;
      if (var1 instanceof CtPrimitiveType) {
         CtPrimitiveType var4 = (CtPrimitiveType)var1;
         var2 = var4.getReturnOp();
         if (var2 == 175) {
            var3 = 14;
         } else if (var2 == 174) {
            var3 = 11;
         } else if (var2 == 173) {
            var3 = 9;
         } else if (var2 == 177) {
            var3 = 0;
         } else {
            var3 = 3;
         }
      } else {
         var2 = 176;
         var3 = 1;
      }

      if (var3 != 0) {
         var0.addOpcode(var3);
      }

      var0.addOpcode(var2);
   }

   public boolean recordLocalVariables(CodeAttribute var1, int var2) throws CompileError {
      LocalVariableAttribute var3 = (LocalVariableAttribute)var1.getAttribute("LocalVariableTable");
      if (var3 == null) {
         return false;
      } else {
         int var4 = var3.tableLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var3.startPc(var5);
            int var7 = var3.codeLength(var5);
            if (var6 <= var2 && var2 < var6 + var7) {
               this.gen.recordVariable(var3.descriptor(var5), var3.variableName(var5), var3.index(var5), this.stable);
            }
         }

         return true;
      }
   }

   public boolean recordParamNames(CodeAttribute var1, int var2) throws CompileError {
      LocalVariableAttribute var3 = (LocalVariableAttribute)var1.getAttribute("LocalVariableTable");
      if (var3 == null) {
         return false;
      } else {
         int var4 = var3.tableLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var3.index(var5);
            if (var6 < var2) {
               this.gen.recordVariable(var3.descriptor(var5), var3.variableName(var5), var6, this.stable);
            }
         }

         return true;
      }
   }

   public int recordParams(CtClass[] var1, boolean var2) throws CompileError {
      return this.gen.recordParams(var1, var2, "$", "$args", "$$", this.stable);
   }

   public int recordParams(String var1, CtClass[] var2, boolean var3, int var4, boolean var5) throws CompileError {
      return this.gen.recordParams(var2, var5, "$", "$args", "$$", var3, var4, var1, this.stable);
   }

   public void setMaxLocals(int var1) {
      this.gen.setMaxLocals(var1);
   }

   public int recordReturnType(CtClass var1, boolean var2) throws CompileError {
      this.gen.recordType(var1);
      return this.gen.recordReturnType(var1, "$r", var2 ? "$_" : null, this.stable);
   }

   public void recordType(CtClass var1) {
      this.gen.recordType(var1);
   }

   public int recordVariable(CtClass var1, String var2) throws CompileError {
      return this.gen.recordVariable(var1, var2, this.stable);
   }

   public void recordProceed(String var1, String var2) throws CompileError {
      Parser var3 = new Parser(new Lex(var1));
      ASTree var4 = var3.parseExpression(this.stable);
      ProceedHandler var6 = new ProceedHandler(this, var2, var4) {
         final String val$m;
         final ASTree val$texpr;
         final Javac this$0;

         {
            this.this$0 = var1;
            this.val$m = var2;
            this.val$texpr = var3;
         }

         public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
            Object var4 = new Member(this.val$m);
            if (this.val$texpr != null) {
               var4 = Expr.make(46, this.val$texpr, (ASTree)var4);
            }

            CallExpr var5 = CallExpr.makeCall((ASTree)var4, var3);
            var1.compileExpr(var5);
            var1.addNullIfVoid();
         }

         public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
            Object var3 = new Member(this.val$m);
            if (this.val$texpr != null) {
               var3 = Expr.make(46, this.val$texpr, (ASTree)var3);
            }

            CallExpr var4 = CallExpr.makeCall((ASTree)var3, var2);
            var4.accept(var1);
            var1.addNullIfVoid();
         }
      };
      this.gen.setProceedHandler(var6, "$proceed");
   }

   public void recordStaticProceed(String var1, String var2) throws CompileError {
      ProceedHandler var5 = new ProceedHandler(this, var1, var2) {
         final String val$c;
         final String val$m;
         final Javac this$0;

         {
            this.this$0 = var1;
            this.val$c = var2;
            this.val$m = var3;
         }

         public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
            Expr var4 = Expr.make(35, new Symbol(this.val$c), new Member(this.val$m));
            CallExpr var5 = CallExpr.makeCall(var4, var3);
            var1.compileExpr(var5);
            var1.addNullIfVoid();
         }

         public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
            Expr var3 = Expr.make(35, new Symbol(this.val$c), new Member(this.val$m));
            CallExpr var4 = CallExpr.makeCall(var3, var2);
            var4.accept(var1);
            var1.addNullIfVoid();
         }
      };
      this.gen.setProceedHandler(var5, "$proceed");
   }

   public void recordSpecialProceed(String var1, String var2, String var3, String var4) throws CompileError {
      Parser var5 = new Parser(new Lex(var1));
      ASTree var6 = var5.parseExpression(this.stable);
      ProceedHandler var10 = new ProceedHandler(this, var6, var2, var3, var4) {
         final ASTree val$texpr;
         final String val$cname;
         final String val$method;
         final String val$desc;
         final Javac this$0;

         {
            this.this$0 = var1;
            this.val$texpr = var2;
            this.val$cname = var3;
            this.val$method = var4;
            this.val$desc = var5;
         }

         public void doit(JvstCodeGen var1, Bytecode var2, ASTList var3) throws CompileError {
            var1.compileInvokeSpecial(this.val$texpr, this.val$cname, this.val$method, this.val$desc, var3);
         }

         public void setReturnType(JvstTypeChecker var1, ASTList var2) throws CompileError {
            var1.compileInvokeSpecial(this.val$texpr, this.val$cname, this.val$method, this.val$desc, var2);
         }
      };
      this.gen.setProceedHandler(var10, "$proceed");
   }

   public void recordProceed(ProceedHandler var1) {
      this.gen.setProceedHandler(var1, "$proceed");
   }

   public void compileStmnt(String var1) throws CompileError {
      Parser var2 = new Parser(new Lex(var1));
      SymbolTable var3 = new SymbolTable(this.stable);

      while(var2.hasMore()) {
         Stmnt var4 = var2.parseStatement(var3);
         if (var4 != null) {
            var4.accept(this.gen);
         }
      }

   }

   public void compileExpr(String var1) throws CompileError {
      ASTree var2 = parseExpr(var1, this.stable);
      this.compileExpr(var2);
   }

   public static ASTree parseExpr(String var0, SymbolTable var1) throws CompileError {
      Parser var2 = new Parser(new Lex(var0));
      return var2.parseExpression(var1);
   }

   public void compileExpr(ASTree var1) throws CompileError {
      if (var1 != null) {
         this.gen.compileExpr(var1);
      }

   }

   public static class CtFieldWithInit extends CtField {
      private ASTree init = null;

      CtFieldWithInit(CtClass var1, String var2, CtClass var3) throws CannotCompileException {
         super(var1, var2, var3);
      }

      protected void setInit(ASTree var1) {
         this.init = var1;
      }

      protected ASTree getInitAST() {
         return this.init;
      }
   }
}
