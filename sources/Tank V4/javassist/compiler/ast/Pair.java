package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Pair extends ASTree {
   protected ASTree left;
   protected ASTree right;

   public Pair(ASTree var1, ASTree var2) {
      this.left = var1;
      this.right = var2;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atPair(this);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(<Pair> ");
      var1.append(this.left == null ? "<null>" : this.left.toString());
      var1.append(" . ");
      var1.append(this.right == null ? "<null>" : this.right.toString());
      var1.append(')');
      return var1.toString();
   }

   public ASTree getLeft() {
      return this.left;
   }

   public ASTree getRight() {
      return this.right;
   }

   public void setLeft(ASTree var1) {
      this.left = var1;
   }

   public void setRight(ASTree var1) {
      this.right = var1;
   }
}
