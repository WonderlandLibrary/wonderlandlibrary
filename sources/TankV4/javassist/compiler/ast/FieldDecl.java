package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class FieldDecl extends ASTList {
   public FieldDecl(ASTree var1, ASTList var2) {
      super(var1, var2);
   }

   public ASTList getModifiers() {
      return (ASTList)this.getLeft();
   }

   public Declarator getDeclarator() {
      return (Declarator)this.tail().head();
   }

   public ASTree getInit() {
      return this.sublist(2).head();
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atFieldDecl(this);
   }
}
