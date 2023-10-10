package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Expr extends ASTList implements TokenId {
   protected int operatorId;

   Expr(int var1, ASTree var2, ASTList var3) {
      super(var2, var3);
      this.operatorId = var1;
   }

   Expr(int var1, ASTree var2) {
      super(var2);
      this.operatorId = var1;
   }

   public static Expr make(int var0, ASTree var1, ASTree var2) {
      return new Expr(var0, var1, new ASTList(var2));
   }

   public static Expr make(int var0, ASTree var1) {
      return new Expr(var0, var1);
   }

   public int getOperator() {
      return this.operatorId;
   }

   public void setOperator(int var1) {
      this.operatorId = var1;
   }

   public ASTree oprand1() {
      return this.getLeft();
   }

   public void setOprand1(ASTree var1) {
      this.setLeft(var1);
   }

   public ASTree oprand2() {
      return this.getRight().getLeft();
   }

   public void setOprand2(ASTree var1) {
      this.getRight().setLeft(var1);
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atExpr(this);
   }

   public String getName() {
      int var1 = this.operatorId;
      if (var1 < 128) {
         return String.valueOf((char)var1);
      } else if (350 <= var1 && var1 <= 371) {
         return opNames[var1 - 350];
      } else {
         return var1 == 323 ? "instanceof" : String.valueOf(var1);
      }
   }

   protected String getTag() {
      return "op:" + this.getName();
   }
}
