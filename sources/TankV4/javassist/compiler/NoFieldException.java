package javassist.compiler;

import javassist.compiler.ast.ASTree;

public class NoFieldException extends CompileError {
   private String fieldName;
   private ASTree expr;

   public NoFieldException(String var1, ASTree var2) {
      super("no such field: " + var1);
      this.fieldName = var1;
      this.expr = var2;
   }

   public String getField() {
      return this.fieldName;
   }

   public ASTree getExpr() {
      return this.expr;
   }
}
