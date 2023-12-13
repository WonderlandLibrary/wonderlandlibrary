package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class MethodDecl extends ASTList {
   public static final String initName = "<init>";

   public MethodDecl(ASTree var1, ASTList var2) {
      super(var1, var2);
   }

   public boolean isConstructor() {
      Symbol var1 = this.getReturn().getVariable();
      return var1 != null && "<init>".equals(var1.get());
   }

   public ASTList getModifiers() {
      return (ASTList)this.getLeft();
   }

   public Declarator getReturn() {
      return (Declarator)this.tail().head();
   }

   public ASTList getParams() {
      return (ASTList)this.sublist(2).head();
   }

   public ASTList getThrows() {
      return (ASTList)this.sublist(3).head();
   }

   public Stmnt getBody() {
      return (Stmnt)this.sublist(4).head();
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atMethodDecl(this);
   }
}
