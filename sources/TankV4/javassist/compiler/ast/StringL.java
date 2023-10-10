package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class StringL extends ASTree {
   protected String text;

   public StringL(String var1) {
      this.text = var1;
   }

   public String get() {
      return this.text;
   }

   public String toString() {
      return "\"" + this.text + "\"";
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atStringL(this);
   }
}
