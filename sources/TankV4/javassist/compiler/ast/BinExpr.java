package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class BinExpr extends Expr {
   private BinExpr(int var1, ASTree var2, ASTList var3) {
      super(var1, var2, var3);
   }

   public static BinExpr makeBin(int var0, ASTree var1, ASTree var2) {
      return new BinExpr(var0, var1, new ASTList(var2));
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atBinExpr(this);
   }
}
