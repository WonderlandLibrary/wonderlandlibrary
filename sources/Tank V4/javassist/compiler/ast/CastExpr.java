package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class CastExpr extends ASTList implements TokenId {
   protected int castType;
   protected int arrayDim;

   public CastExpr(ASTList var1, int var2, ASTree var3) {
      super(var1, new ASTList(var3));
      this.castType = 307;
      this.arrayDim = var2;
   }

   public CastExpr(int var1, int var2, ASTree var3) {
      super((ASTree)null, new ASTList(var3));
      this.castType = var1;
      this.arrayDim = var2;
   }

   public int getType() {
      return this.castType;
   }

   public int getArrayDim() {
      return this.arrayDim;
   }

   public ASTList getClassName() {
      return (ASTList)this.getLeft();
   }

   public ASTree getOprand() {
      return this.getRight().getLeft();
   }

   public void setOprand(ASTree var1) {
      this.getRight().setLeft(var1);
   }

   public String getTag() {
      return "cast:" + this.castType + ":" + this.arrayDim;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atCastExpr(this);
   }
}
