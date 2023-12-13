package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class AssignExpr extends Expr {
   private AssignExpr(int var1, ASTree var2, ASTList var3) {
      super(var1, var2, var3);
   }

   public static AssignExpr makeAssign(int var0, ASTree var1, ASTree var2) {
      return new AssignExpr(var0, var1, new ASTList(var2));
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atAssignExpr(this);
   }
}
