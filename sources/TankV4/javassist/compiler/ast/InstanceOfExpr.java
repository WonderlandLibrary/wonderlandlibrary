package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class InstanceOfExpr extends CastExpr {
   public InstanceOfExpr(ASTList var1, int var2, ASTree var3) {
      super(var1, var2, var3);
   }

   public InstanceOfExpr(int var1, int var2, ASTree var3) {
      super(var1, var2, var3);
   }

   public String getTag() {
      return "instanceof:" + this.castType + ":" + this.arrayDim;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atInstanceOfExpr(this);
   }
}
