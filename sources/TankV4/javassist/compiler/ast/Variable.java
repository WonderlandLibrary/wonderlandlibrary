package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Variable extends Symbol {
   protected Declarator declarator;

   public Variable(String var1, Declarator var2) {
      super(var1);
      this.declarator = var2;
   }

   public Declarator getDeclarator() {
      return this.declarator;
   }

   public String toString() {
      return this.identifier + ":" + this.declarator.getType();
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atVariable(this);
   }
}
