package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ArrayInit extends ASTList {
   public ArrayInit(ASTree var1) {
      super(var1);
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atArrayInit(this);
   }

   public String getTag() {
      return "array";
   }
}
