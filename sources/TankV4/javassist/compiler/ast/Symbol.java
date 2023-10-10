package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Symbol extends ASTree {
   protected String identifier;

   public Symbol(String var1) {
      this.identifier = var1;
   }

   public String get() {
      return this.identifier;
   }

   public String toString() {
      return this.identifier;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atSymbol(this);
   }
}
