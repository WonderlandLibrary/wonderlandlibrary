package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ASTList extends ASTree {
   private ASTree left;
   private ASTList right;

   public ASTList(ASTree var1, ASTList var2) {
      this.left = var1;
      this.right = var2;
   }

   public ASTList(ASTree var1) {
      this.left = var1;
      this.right = null;
   }

   public static ASTList make(ASTree var0, ASTree var1, ASTree var2) {
      return new ASTList(var0, new ASTList(var1, new ASTList(var2)));
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
      this.right = (ASTList)var1;
   }

   public ASTree head() {
      return this.left;
   }

   public void setHead(ASTree var1) {
      this.left = var1;
   }

   public ASTList tail() {
      return this.right;
   }

   public void setTail(ASTList var1) {
      this.right = var1;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atASTList(this);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(<");
      var1.append(this.getTag());
      var1.append('>');

      for(ASTList var2 = this; var2 != null; var2 = var2.right) {
         var1.append(' ');
         ASTree var3 = var2.left;
         var1.append(var3 == null ? "<null>" : var3.toString());
      }

      var1.append(')');
      return var1.toString();
   }

   public int length() {
      return length(this);
   }

   public static int length(ASTList var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1;
         for(var1 = 0; var0 != null; ++var1) {
            var0 = var0.right;
         }

         return var1;
      }
   }

   public ASTList sublist(int var1) {
      ASTList var2;
      for(var2 = this; var1-- > 0; var2 = var2.right) {
      }

      return var2;
   }

   public boolean subst(ASTree var1, ASTree var2) {
      for(ASTList var3 = this; var3 != null; var3 = var3.right) {
         if (var3.left == var2) {
            var3.left = var1;
            return true;
         }
      }

      return false;
   }

   public static ASTList append(ASTList var0, ASTree var1) {
      return concat(var0, new ASTList(var1));
   }

   public static ASTList concat(ASTList var0, ASTList var1) {
      if (var0 == null) {
         return var1;
      } else {
         ASTList var2;
         for(var2 = var0; var2.right != null; var2 = var2.right) {
         }

         var2.right = var1;
         return var0;
      }
   }
}
