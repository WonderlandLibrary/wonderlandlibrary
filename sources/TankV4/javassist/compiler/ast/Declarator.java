package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Declarator extends ASTList implements TokenId {
   protected int varType;
   protected int arrayDim;
   protected int localVar;
   protected String qualifiedClass;

   public Declarator(int var1, int var2) {
      super((ASTree)null);
      this.varType = var1;
      this.arrayDim = var2;
      this.localVar = -1;
      this.qualifiedClass = null;
   }

   public Declarator(ASTList var1, int var2) {
      super((ASTree)null);
      this.varType = 307;
      this.arrayDim = var2;
      this.localVar = -1;
      this.qualifiedClass = astToClassName(var1, '/');
   }

   public Declarator(int var1, String var2, int var3, int var4, Symbol var5) {
      super((ASTree)null);
      this.varType = var1;
      this.arrayDim = var3;
      this.localVar = var4;
      this.qualifiedClass = var2;
      this.setLeft(var5);
      append(this, (ASTree)null);
   }

   public Declarator make(Symbol var1, int var2, ASTree var3) {
      Declarator var4 = new Declarator(this.varType, this.arrayDim + var2);
      var4.qualifiedClass = this.qualifiedClass;
      var4.setLeft(var1);
      append(var4, var3);
      return var4;
   }

   public int getType() {
      return this.varType;
   }

   public int getArrayDim() {
      return this.arrayDim;
   }

   public void addArrayDim(int var1) {
      this.arrayDim += var1;
   }

   public String getClassName() {
      return this.qualifiedClass;
   }

   public void setClassName(String var1) {
      this.qualifiedClass = var1;
   }

   public Symbol getVariable() {
      return (Symbol)this.getLeft();
   }

   public void setVariable(Symbol var1) {
      this.setLeft(var1);
   }

   public ASTree getInitializer() {
      ASTList var1 = this.tail();
      return var1 != null ? var1.head() : null;
   }

   public void setLocalVar(int var1) {
      this.localVar = var1;
   }

   public int getLocalVar() {
      return this.localVar;
   }

   public String getTag() {
      return "decl";
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atDeclarator(this);
   }

   public static String astToClassName(ASTList var0, char var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();
         astToClassName(var2, var0, var1);
         return var2.toString();
      }
   }

   private static void astToClassName(StringBuffer var0, ASTList var1, char var2) {
      while(true) {
         ASTree var3 = var1.head();
         if (var3 instanceof Symbol) {
            var0.append(((Symbol)var3).get());
         } else if (var3 instanceof ASTList) {
            astToClassName(var0, (ASTList)var3, var2);
         }

         var1 = var1.tail();
         if (var1 == null) {
            return;
         }

         var0.append(var2);
      }
   }
}
