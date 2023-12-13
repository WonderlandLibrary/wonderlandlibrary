package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class NewExpr extends ASTList implements TokenId {
   protected boolean newArray;
   protected int arrayType;

   public NewExpr(ASTList var1, ASTList var2) {
      super(var1, new ASTList(var2));
      this.newArray = false;
      this.arrayType = 307;
   }

   public NewExpr(int var1, ASTList var2, ArrayInit var3) {
      super((ASTree)null, new ASTList(var2));
      this.newArray = true;
      this.arrayType = var1;
      if (var3 != null) {
         append(this, var3);
      }

   }

   public static NewExpr makeObjectArray(ASTList var0, ASTList var1, ArrayInit var2) {
      NewExpr var3 = new NewExpr(var0, var1);
      var3.newArray = true;
      if (var2 != null) {
         append(var3, var2);
      }

      return var3;
   }

   public boolean isArray() {
      return this.newArray;
   }

   public int getArrayType() {
      return this.arrayType;
   }

   public ASTList getClassName() {
      return (ASTList)this.getLeft();
   }

   public ASTList getArguments() {
      return (ASTList)this.getRight().getLeft();
   }

   public ASTList getArraySize() {
      return this.getArguments();
   }

   public ArrayInit getInitializer() {
      ASTree var1 = this.getRight().getRight();
      return var1 == null ? null : (ArrayInit)var1.getLeft();
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atNewExpr(this);
   }

   protected String getTag() {
      return this.newArray ? "new[]" : "new";
   }
}
