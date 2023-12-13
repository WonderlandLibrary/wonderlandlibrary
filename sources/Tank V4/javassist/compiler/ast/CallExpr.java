package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.MemberResolver;

public class CallExpr extends Expr {
   private MemberResolver.Method method = null;

   private CallExpr(ASTree var1, ASTList var2) {
      super(67, var1, var2);
   }

   public void setMethod(MemberResolver.Method var1) {
      this.method = var1;
   }

   public MemberResolver.Method getMethod() {
      return this.method;
   }

   public static CallExpr makeCall(ASTree var0, ASTree var1) {
      return new CallExpr(var0, new ASTList(var1));
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atCallExpr(this);
   }
}
