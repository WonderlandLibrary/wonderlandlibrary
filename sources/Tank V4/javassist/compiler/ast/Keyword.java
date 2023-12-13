package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Keyword extends ASTree {
   protected int tokenId;

   public Keyword(int var1) {
      this.tokenId = var1;
   }

   public int get() {
      return this.tokenId;
   }

   public String toString() {
      return "id:" + this.tokenId;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atKeyword(this);
   }
}
