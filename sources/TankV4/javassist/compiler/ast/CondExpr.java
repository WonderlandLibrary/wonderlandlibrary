package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class CondExpr extends ASTList {
   public CondExpr(ASTree var1, ASTree var2, ASTree var3) {
      super(var1, new ASTList(var2, new ASTList(var3)));
   }

   public ASTree condExpr() {
      return this.head();
   }

   public void setCond(ASTree var1) {
      this.setHead(var1);
   }

   public ASTree thenExpr() {
      return this.tail().head();
   }

   public void setThen(ASTree var1) {
      this.tail().setHead(var1);
   }

   public ASTree elseExpr() {
      return this.tail().tail().head();
   }

   public void setElse(ASTree var1) {
      this.tail().tail().setHead(var1);
   }

   public String getTag() {
      return "?:";
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atCondExpr(this);
   }
}
