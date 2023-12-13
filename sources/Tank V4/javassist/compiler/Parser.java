package javassist.compiler;

import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.Symbol;

public final class Parser implements TokenId {
   private Lex lex;
   private static final int[] binaryOpPrecedence = new int[]{0, 0, 0, 0, 1, 6, 0, 0, 0, 1, 2, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0};

   public Parser(Lex var1) {
      this.lex = var1;
   }

   public boolean hasMore() {
      return this.lex.lookAhead() >= 0;
   }

   public ASTList parseMember(SymbolTable var1) throws CompileError {
      ASTList var2 = this.parseMember1(var1);
      return (ASTList)(var2 instanceof MethodDecl ? this.parseMethod2(var1, (MethodDecl)var2) : var2);
   }

   public ASTList parseMember1(SymbolTable var1) throws CompileError {
      ASTList var2 = this.parseMemberMods();
      boolean var4 = false;
      Declarator var3;
      if (this.lex.lookAhead() == 400 && this.lex.lookAhead(1) == 40) {
         var3 = new Declarator(344, 0);
         var4 = true;
      } else {
         var3 = this.parseFormalType(var1);
      }

      if (this.lex.get() != 400) {
         throw new SyntaxError(this.lex);
      } else {
         String var5;
         if (var4) {
            var5 = "<init>";
         } else {
            var5 = this.lex.getString();
         }

         var3.setVariable(new Symbol(var5));
         return (ASTList)(!var4 && this.lex.lookAhead() != 40 ? this.parseField(var1, var2, var3) : this.parseMethod1(var1, var4, var2, var3));
      }
   }

   private FieldDecl parseField(SymbolTable var1, ASTList var2, Declarator var3) throws CompileError {
      ASTree var4 = null;
      if (this.lex.lookAhead() == 61) {
         this.lex.get();
         var4 = this.parseExpression(var1);
      }

      int var5 = this.lex.get();
      if (var5 == 59) {
         return new FieldDecl(var2, new ASTList(var3, new ASTList(var4)));
      } else if (var5 == 44) {
         throw new CompileError("only one field can be declared in one declaration", this.lex);
      } else {
         throw new SyntaxError(this.lex);
      }
   }

   private MethodDecl parseMethod1(SymbolTable var1, boolean var2, ASTList var3, Declarator var4) throws CompileError {
      if (this.lex.get() != 40) {
         throw new SyntaxError(this.lex);
      } else {
         ASTList var5 = null;
         if (this.lex.lookAhead() != 41) {
            label39:
            while(true) {
               while(true) {
                  var5 = ASTList.append(var5, this.parseFormalParam(var1));
                  int var6 = this.lex.lookAhead();
                  if (var6 == 44) {
                     this.lex.get();
                  } else if (var6 == 41) {
                     break label39;
                  }
               }
            }
         }

         this.lex.get();
         var4.addArrayDim(this.parseArrayDimension());
         if (var2 && var4.getArrayDim() > 0) {
            throw new SyntaxError(this.lex);
         } else {
            ASTList var7 = null;
            if (this.lex.lookAhead() == 341) {
               this.lex.get();

               while(true) {
                  var7 = ASTList.append(var7, this.parseClassType(var1));
                  if (this.lex.lookAhead() != 44) {
                     break;
                  }

                  this.lex.get();
               }
            }

            return new MethodDecl(var3, new ASTList(var4, ASTList.make(var5, var7, (ASTree)null)));
         }
      }
   }

   public MethodDecl parseMethod2(SymbolTable var1, MethodDecl var2) throws CompileError {
      Stmnt var3 = null;
      if (this.lex.lookAhead() == 59) {
         this.lex.get();
      } else {
         var3 = this.parseBlock(var1);
         if (var3 == null) {
            var3 = new Stmnt(66);
         }
      }

      var2.sublist(4).setHead(var3);
      return var2;
   }

   private ASTList parseMemberMods() {
      ASTList var2 = null;

      while(true) {
         int var1 = this.lex.lookAhead();
         if (var1 != 300 && var1 != 315 && var1 != 332 && var1 != 331 && var1 != 330 && var1 != 338 && var1 != 335 && var1 != 345 && var1 != 342 && var1 != 347) {
            return var2;
         }

         var2 = new ASTList(new Keyword(this.lex.get()), var2);
      }
   }

   private Declarator parseFormalType(SymbolTable param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private Declarator parseFormalParam(SymbolTable var1) throws CompileError {
      Declarator var2 = this.parseFormalType(var1);
      if (this.lex.get() != 400) {
         throw new SyntaxError(this.lex);
      } else {
         String var3 = this.lex.getString();
         var2.setVariable(new Symbol(var3));
         var2.addArrayDim(this.parseArrayDimension());
         var1.append(var3, var2);
         return var2;
      }
   }

   public Stmnt parseStatement(SymbolTable var1) throws CompileError {
      int var2 = this.lex.lookAhead();
      if (var2 == 123) {
         return this.parseBlock(var1);
      } else if (var2 == 59) {
         this.lex.get();
         return new Stmnt(66);
      } else if (var2 == 400 && this.lex.lookAhead(1) == 58) {
         this.lex.get();
         String var3 = this.lex.getString();
         this.lex.get();
         return Stmnt.make(76, new Symbol(var3), this.parseStatement(var1));
      } else if (var2 == 320) {
         return this.parseIf(var1);
      } else if (var2 == 346) {
         return this.parseWhile(var1);
      } else if (var2 == 311) {
         return this.parseDo(var1);
      } else if (var2 == 318) {
         return this.parseFor(var1);
      } else if (var2 == 343) {
         return this.parseTry(var1);
      } else if (var2 == 337) {
         return this.parseSwitch(var1);
      } else if (var2 == 338) {
         return this.parseSynchronized(var1);
      } else if (var2 == 333) {
         return this.parseReturn(var1);
      } else if (var2 == 340) {
         return this.parseThrow(var1);
      } else if (var2 == 302) {
         return this.parseBreak(var1);
      } else {
         return var2 == 309 ? this.parseContinue(var1) : this.parseDeclarationOrExpression(var1, false);
      }
   }

   private Stmnt parseBlock(SymbolTable var1) throws CompileError {
      if (this.lex.get() != 123) {
         throw new SyntaxError(this.lex);
      } else {
         Stmnt var2 = null;
         SymbolTable var3 = new SymbolTable(var1);

         while(this.lex.lookAhead() != 125) {
            Stmnt var4 = this.parseStatement(var3);
            if (var4 != null) {
               var2 = (Stmnt)ASTList.concat(var2, new Stmnt(66, var4));
            }
         }

         this.lex.get();
         if (var2 == null) {
            return new Stmnt(66);
         } else {
            return var2;
         }
      }
   }

   private Stmnt parseIf(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      ASTree var3 = this.parseParExpression(var1);
      Stmnt var4 = this.parseStatement(var1);
      Stmnt var5;
      if (this.lex.lookAhead() == 313) {
         this.lex.get();
         var5 = this.parseStatement(var1);
      } else {
         var5 = null;
      }

      return new Stmnt(var2, var3, new ASTList(var4, new ASTList(var5)));
   }

   private Stmnt parseWhile(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      ASTree var3 = this.parseParExpression(var1);
      Stmnt var4 = this.parseStatement(var1);
      return new Stmnt(var2, var3, var4);
   }

   private Stmnt parseDo(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      Stmnt var3 = this.parseStatement(var1);
      if (this.lex.get() == 346 && this.lex.get() == 40) {
         ASTree var4 = this.parseExpression(var1);
         if (this.lex.get() == 41 && this.lex.get() == 59) {
            return new Stmnt(var2, var4, var3);
         } else {
            throw new SyntaxError(this.lex);
         }
      } else {
         throw new SyntaxError(this.lex);
      }
   }

   private Stmnt parseFor(SymbolTable var1) throws CompileError {
      int var5 = this.lex.get();
      SymbolTable var6 = new SymbolTable(var1);
      if (this.lex.get() != 40) {
         throw new SyntaxError(this.lex);
      } else {
         Stmnt var2;
         if (this.lex.lookAhead() == 59) {
            this.lex.get();
            var2 = null;
         } else {
            var2 = this.parseDeclarationOrExpression(var6, true);
         }

         ASTree var4;
         if (this.lex.lookAhead() == 59) {
            var4 = null;
         } else {
            var4 = this.parseExpression(var6);
         }

         if (this.lex.get() != 59) {
            throw new CompileError("; is missing", this.lex);
         } else {
            Stmnt var3;
            if (this.lex.lookAhead() == 41) {
               var3 = null;
            } else {
               var3 = this.parseExprList(var6);
            }

            if (this.lex.get() != 41) {
               throw new CompileError(") is missing", this.lex);
            } else {
               Stmnt var7 = this.parseStatement(var6);
               return new Stmnt(var5, var2, new ASTList(var4, new ASTList(var3, var7)));
            }
         }
      }
   }

   private Stmnt parseSwitch(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      ASTree var3 = this.parseParExpression(var1);
      Stmnt var4 = this.parseSwitchBlock(var1);
      return new Stmnt(var2, var3, var4);
   }

   private Stmnt parseSwitchBlock(SymbolTable var1) throws CompileError {
      if (this.lex.get() != 123) {
         throw new SyntaxError(this.lex);
      } else {
         SymbolTable var2 = new SymbolTable(var1);
         Stmnt var3 = this.parseStmntOrCase(var2);
         if (var3 == null) {
            throw new CompileError("empty switch block", this.lex);
         } else {
            int var4 = var3.getOperator();
            if (var4 != 304 && var4 != 310) {
               throw new CompileError("no case or default in a switch block", this.lex);
            } else {
               Stmnt var5 = new Stmnt(66, var3);

               while(true) {
                  while(true) {
                     Stmnt var6;
                     do {
                        if (this.lex.lookAhead() == 125) {
                           this.lex.get();
                           return var5;
                        }

                        var6 = this.parseStmntOrCase(var2);
                     } while(var6 == null);

                     int var7 = var6.getOperator();
                     if (var7 != 304 && var7 != 310) {
                        var3 = (Stmnt)ASTList.concat(var3, new Stmnt(66, var6));
                     } else {
                        var5 = (Stmnt)ASTList.concat(var5, new Stmnt(66, var6));
                        var3 = var6;
                     }
                  }
               }
            }
         }
      }
   }

   private Stmnt parseStmntOrCase(SymbolTable var1) throws CompileError {
      int var2 = this.lex.lookAhead();
      if (var2 != 304 && var2 != 310) {
         return this.parseStatement(var1);
      } else {
         this.lex.get();
         Stmnt var3;
         if (var2 == 304) {
            var3 = new Stmnt(var2, this.parseExpression(var1));
         } else {
            var3 = new Stmnt(310);
         }

         if (this.lex.get() != 58) {
            throw new CompileError(": is missing", this.lex);
         } else {
            return var3;
         }
      }
   }

   private Stmnt parseSynchronized(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      if (this.lex.get() != 40) {
         throw new SyntaxError(this.lex);
      } else {
         ASTree var3 = this.parseExpression(var1);
         if (this.lex.get() != 41) {
            throw new SyntaxError(this.lex);
         } else {
            Stmnt var4 = this.parseBlock(var1);
            return new Stmnt(var2, var3, var4);
         }
      }
   }

   private Stmnt parseTry(SymbolTable var1) throws CompileError {
      this.lex.get();
      Stmnt var2 = this.parseBlock(var1);

      ASTList var3;
      Declarator var5;
      Stmnt var6;
      for(var3 = null; this.lex.lookAhead() == 305; var3 = ASTList.append(var3, new Pair(var5, var6))) {
         this.lex.get();
         if (this.lex.get() != 40) {
            throw new SyntaxError(this.lex);
         }

         SymbolTable var4 = new SymbolTable(var1);
         var5 = this.parseFormalParam(var4);
         if (var5.getArrayDim() > 0 || var5.getType() != 307) {
            throw new SyntaxError(this.lex);
         }

         if (this.lex.get() != 41) {
            throw new SyntaxError(this.lex);
         }

         var6 = this.parseBlock(var4);
      }

      Stmnt var7 = null;
      if (this.lex.lookAhead() == 316) {
         this.lex.get();
         var7 = this.parseBlock(var1);
      }

      return Stmnt.make(343, var2, var3, var7);
   }

   private Stmnt parseReturn(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      Stmnt var3 = new Stmnt(var2);
      if (this.lex.lookAhead() != 59) {
         var3.setLeft(this.parseExpression(var1));
      }

      if (this.lex.get() != 59) {
         throw new CompileError("; is missing", this.lex);
      } else {
         return var3;
      }
   }

   private Stmnt parseThrow(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      ASTree var3 = this.parseExpression(var1);
      if (this.lex.get() != 59) {
         throw new CompileError("; is missing", this.lex);
      } else {
         return new Stmnt(var2, var3);
      }
   }

   private Stmnt parseBreak(SymbolTable var1) throws CompileError {
      return this.parseContinue(var1);
   }

   private Stmnt parseContinue(SymbolTable var1) throws CompileError {
      int var2 = this.lex.get();
      Stmnt var3 = new Stmnt(var2);
      int var4 = this.lex.get();
      if (var4 == 400) {
         var3.setLeft(new Symbol(this.lex.getString()));
         var4 = this.lex.get();
      }

      if (var4 != 59) {
         throw new CompileError("; is missing", this.lex);
      } else {
         return var3;
      }
   }

   private Stmnt parseDeclarationOrExpression(SymbolTable param1, boolean param2) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private Stmnt parseExprList(SymbolTable var1) throws CompileError {
      Stmnt var2 = null;

      while(true) {
         Stmnt var3 = new Stmnt(69, this.parseExpression(var1));
         var2 = (Stmnt)ASTList.concat(var2, new Stmnt(66, var3));
         if (this.lex.lookAhead() != 44) {
            return var2;
         }

         this.lex.get();
      }
   }

   private Stmnt parseDeclarators(SymbolTable var1, Declarator var2) throws CompileError {
      Stmnt var3 = null;

      int var4;
      do {
         var3 = (Stmnt)ASTList.concat(var3, new Stmnt(68, this.parseDeclarator(var1, var2)));
         var4 = this.lex.get();
         if (var4 == 59) {
            return var3;
         }
      } while(var4 == 44);

      throw new CompileError("; is missing", this.lex);
   }

   private Declarator parseDeclarator(SymbolTable var1, Declarator var2) throws CompileError {
      if (this.lex.get() == 400 && var2.getType() != 344) {
         String var3 = this.lex.getString();
         Symbol var4 = new Symbol(var3);
         int var5 = this.parseArrayDimension();
         ASTree var6 = null;
         if (this.lex.lookAhead() == 61) {
            this.lex.get();
            var6 = this.parseInitializer(var1);
         }

         Declarator var7 = var2.make(var4, var5, var6);
         var1.append(var3, var7);
         return var7;
      } else {
         throw new SyntaxError(this.lex);
      }
   }

   private ASTree parseInitializer(SymbolTable var1) throws CompileError {
      return (ASTree)(this.lex.lookAhead() == 123 ? this.parseArrayInitializer(var1) : this.parseExpression(var1));
   }

   private ArrayInit parseArrayInitializer(SymbolTable var1) throws CompileError {
      this.lex.get();
      ASTree var2 = this.parseExpression(var1);
      ArrayInit var3 = new ArrayInit(var2);

      while(this.lex.lookAhead() == 44) {
         this.lex.get();
         var2 = this.parseExpression(var1);
         ASTList.append(var3, var2);
      }

      if (this.lex.get() != 125) {
         throw new SyntaxError(this.lex);
      } else {
         return var3;
      }
   }

   private ASTree parseParExpression(SymbolTable var1) throws CompileError {
      if (this.lex.get() != 40) {
         throw new SyntaxError(this.lex);
      } else {
         ASTree var2 = this.parseExpression(var1);
         if (this.lex.get() != 41) {
            throw new SyntaxError(this.lex);
         } else {
            return var2;
         }
      }
   }

   public ASTree parseExpression(SymbolTable param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private ASTree parseConditionalExpr(SymbolTable var1) throws CompileError {
      ASTree var2 = this.parseBinaryExpr(var1);
      if (this.lex.lookAhead() == 63) {
         this.lex.get();
         ASTree var3 = this.parseExpression(var1);
         if (this.lex.get() != 58) {
            throw new CompileError(": is missing", this.lex);
         } else {
            ASTree var4 = this.parseExpression(var1);
            return new CondExpr(var2, var3, var4);
         }
      } else {
         return var2;
      }
   }

   private ASTree parseBinaryExpr(SymbolTable var1) throws CompileError {
      ASTree var2 = this.parseUnaryExpr(var1);

      while(true) {
         int var3 = this.lex.lookAhead();
         int var4 = this.getOpPrecedence(var3);
         if (var4 == 0) {
            return var2;
         }

         var2 = this.binaryExpr2(var1, var2, var4);
      }
   }

   private ASTree parseInstanceOf(SymbolTable param1, ASTree param2) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private ASTree binaryExpr2(SymbolTable var1, ASTree var2, int var3) throws CompileError {
      int var4 = this.lex.get();
      if (var4 == 323) {
         return this.parseInstanceOf(var1, var2);
      } else {
         ASTree var5 = this.parseUnaryExpr(var1);

         while(true) {
            int var6 = this.lex.lookAhead();
            int var7 = this.getOpPrecedence(var6);
            if (var7 == 0 || var3 <= var7) {
               return BinExpr.makeBin(var4, var2, var5);
            }

            var5 = this.binaryExpr2(var1, var5, var7);
         }
      }
   }

   private int getOpPrecedence(int var1) {
      if (33 <= var1 && var1 <= 63) {
         return binaryOpPrecedence[var1 - 33];
      } else if (var1 == 94) {
         return 7;
      } else if (var1 == 124) {
         return 8;
      } else if (var1 == 369) {
         return 9;
      } else if (var1 == 368) {
         return 10;
      } else if (var1 != 358 && var1 != 350) {
         if (var1 != 357 && var1 != 359 && var1 != 323) {
            return var1 != 364 && var1 != 366 && var1 != 370 ? 0 : 3;
         } else {
            return 4;
         }
      } else {
         return 5;
      }
   }

   private ASTree parseUnaryExpr(SymbolTable var1) throws CompileError {
      switch(this.lex.lookAhead()) {
      case 33:
      case 43:
      case 45:
      case 126:
      case 362:
      case 363:
         int var2 = this.lex.get();
         if (var2 == 45) {
            int var3 = this.lex.lookAhead();
            switch(var3) {
            case 401:
            case 402:
            case 403:
               this.lex.get();
               return new IntConst(-this.lex.getLong(), var3);
            case 404:
            case 405:
               this.lex.get();
               return new DoubleConst(-this.lex.getDouble(), var3);
            }
         }

         return Expr.make(var2, this.parseUnaryExpr(var1));
      case 40:
         return this.parseCast(var1);
      default:
         return this.parsePostfix(var1);
      }
   }

   private ASTree parseCast(SymbolTable param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private int nextIsClassType(int var1) {
      while(true) {
         ++var1;
         if (this.lex.lookAhead(var1) == 46) {
            ++var1;
            if (this.lex.lookAhead(var1) == 400) {
               continue;
            }

            return -1;
         }

         do {
            if (this.lex.lookAhead(var1++) != 91) {
               return var1 - 1;
            }
         } while(this.lex.lookAhead(var1++) == 93);

         return -1;
      }
   }

   private int parseArrayDimension() throws CompileError {
      int var1 = 0;

      do {
         if (this.lex.lookAhead() != 91) {
            return var1;
         }

         ++var1;
         this.lex.get();
      } while(this.lex.get() == 93);

      throw new CompileError("] is missing", this.lex);
   }

   private ASTList parseClassType(SymbolTable var1) throws CompileError {
      ASTList var2 = null;

      while(this.lex.get() == 400) {
         var2 = ASTList.append(var2, new Symbol(this.lex.getString()));
         if (this.lex.lookAhead() != 46) {
            return var2;
         }

         this.lex.get();
      }

      throw new SyntaxError(this.lex);
   }

   private ASTree parsePostfix(SymbolTable var1) throws CompileError {
      int var2 = this.lex.lookAhead();
      switch(var2) {
      case 401:
      case 402:
      case 403:
         this.lex.get();
         return new IntConst(this.lex.getLong(), var2);
      case 404:
      case 405:
         this.lex.get();
         return new DoubleConst(this.lex.getDouble(), var2);
      default:
         Object var5 = this.parsePrimaryExpr(var1);

         while(true) {
            String var3;
            int var6;
            switch(this.lex.lookAhead()) {
            case 35:
               this.lex.get();
               var6 = this.lex.get();
               if (var6 != 400) {
                  throw new CompileError("missing static member name", this.lex);
               }

               var3 = this.lex.getString();
               var5 = Expr.make(35, new Symbol(this.toClassName((ASTree)var5)), new Member(var3));
               break;
            case 40:
               var5 = this.parseMethodCall(var1, (ASTree)var5);
               break;
            case 46:
               this.lex.get();
               var6 = this.lex.get();
               if (var6 == 307) {
                  var5 = this.parseDotClass((ASTree)var5, 0);
               } else if (var6 == 336) {
                  var5 = Expr.make(46, new Symbol(this.toClassName((ASTree)var5)), new Keyword(var6));
               } else {
                  if (var6 != 400) {
                     throw new CompileError("missing member name", this.lex);
                  }

                  var3 = this.lex.getString();
                  var5 = Expr.make(46, (ASTree)var5, new Member(var3));
               }
               break;
            case 91:
               if (this.lex.lookAhead(1) == 93) {
                  int var7 = this.parseArrayDimension();
                  if (this.lex.get() == 46 && this.lex.get() == 307) {
                     var5 = this.parseDotClass((ASTree)var5, var7);
                     break;
                  }

                  throw new SyntaxError(this.lex);
               }

               ASTree var4 = this.parseArrayIndex(var1);
               if (var4 == null) {
                  throw new SyntaxError(this.lex);
               }

               var5 = Expr.make(65, (ASTree)var5, var4);
               break;
            case 362:
            case 363:
               var6 = this.lex.get();
               var5 = Expr.make(var6, (ASTree)null, (ASTree)var5);
               break;
            default:
               return (ASTree)var5;
            }
         }
      }
   }

   private ASTree parseDotClass(ASTree var1, int var2) throws CompileError {
      String var3 = this.toClassName(var1);
      if (var2 > 0) {
         StringBuffer var4 = new StringBuffer();

         while(var2-- > 0) {
            var4.append('[');
         }

         var4.append('L').append(var3.replace('.', '/')).append(';');
         var3 = var4.toString();
      }

      return Expr.make(46, new Symbol(var3), new Member("class"));
   }

   private ASTree parseDotClass(int var1, int var2) throws CompileError {
      String var3;
      if (var2 > 0) {
         var3 = CodeGen.toJvmTypeName(var1, var2);
         return Expr.make(46, new Symbol(var3), new Member("class"));
      } else {
         switch(var1) {
         case 301:
            var3 = "java.lang.Boolean";
            break;
         case 303:
            var3 = "java.lang.Byte";
            break;
         case 306:
            var3 = "java.lang.Character";
            break;
         case 312:
            var3 = "java.lang.Double";
            break;
         case 317:
            var3 = "java.lang.Float";
            break;
         case 324:
            var3 = "java.lang.Integer";
            break;
         case 326:
            var3 = "java.lang.Long";
            break;
         case 334:
            var3 = "java.lang.Short";
            break;
         case 344:
            var3 = "java.lang.Void";
            break;
         default:
            throw new CompileError("invalid builtin type: " + var1);
         }

         return Expr.make(35, new Symbol(var3), new Member("TYPE"));
      }
   }

   private ASTree parseMethodCall(SymbolTable var1, ASTree var2) throws CompileError {
      int var3;
      if (var2 instanceof Keyword) {
         var3 = ((Keyword)var2).get();
         if (var3 != 339 && var3 != 336) {
            throw new SyntaxError(this.lex);
         }
      } else if (!(var2 instanceof Symbol) && var2 instanceof Expr) {
         var3 = ((Expr)var2).getOperator();
         if (var3 != 46 && var3 != 35) {
            throw new SyntaxError(this.lex);
         }
      }

      return CallExpr.makeCall(var2, this.parseArgumentList(var1));
   }

   private String toClassName(ASTree var1) throws CompileError {
      StringBuffer var2 = new StringBuffer();
      this.toClassName(var1, var2);
      return var2.toString();
   }

   private void toClassName(ASTree var1, StringBuffer var2) throws CompileError {
      if (var1 instanceof Symbol) {
         var2.append(((Symbol)var1).get());
      } else {
         if (var1 instanceof Expr) {
            Expr var3 = (Expr)var1;
            if (var3.getOperator() == 46) {
               this.toClassName(var3.oprand1(), var2);
               var2.append('.');
               this.toClassName(var3.oprand2(), var2);
               return;
            }
         }

         throw new CompileError("bad static member access", this.lex);
      }
   }

   private ASTree parsePrimaryExpr(SymbolTable param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private NewExpr parseNew(SymbolTable param1) throws CompileError {
      // $FF: Couldn't be decompiled
   }

   private ASTList parseArraySize(SymbolTable var1) throws CompileError {
      ASTList var2;
      for(var2 = null; this.lex.lookAhead() == 91; var2 = ASTList.append(var2, this.parseArrayIndex(var1))) {
      }

      return var2;
   }

   private ASTree parseArrayIndex(SymbolTable var1) throws CompileError {
      this.lex.get();
      if (this.lex.lookAhead() == 93) {
         this.lex.get();
         return null;
      } else {
         ASTree var2 = this.parseExpression(var1);
         if (this.lex.get() != 93) {
            throw new CompileError("] is missing", this.lex);
         } else {
            return var2;
         }
      }
   }

   private ASTList parseArgumentList(SymbolTable var1) throws CompileError {
      if (this.lex.get() != 40) {
         throw new CompileError("( is missing", this.lex);
      } else {
         ASTList var2 = null;
         if (this.lex.lookAhead() != 41) {
            while(true) {
               var2 = ASTList.append(var2, this.parseExpression(var1));
               if (this.lex.lookAhead() != 44) {
                  break;
               }

               this.lex.get();
            }
         }

         if (this.lex.get() != 41) {
            throw new CompileError(") is missing", this.lex);
         } else {
            return var2;
         }
      }
   }
}
