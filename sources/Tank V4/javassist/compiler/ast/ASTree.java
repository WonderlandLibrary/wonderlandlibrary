package javassist.compiler.ast;

import java.io.Serializable;
import javassist.compiler.CompileError;

public abstract class ASTree implements Serializable {
   public ASTree getLeft() {
      return null;
   }

   public ASTree getRight() {
      return null;
   }

   public void setLeft(ASTree var1) {
   }

   public void setRight(ASTree var1) {
   }

   public abstract void accept(Visitor var1) throws CompileError;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append('<');
      var1.append(this.getTag());
      var1.append('>');
      return var1.toString();
   }

   protected String getTag() {
      String var1 = this.getClass().getName();
      return var1.substring(var1.lastIndexOf(46) + 1);
   }
}
