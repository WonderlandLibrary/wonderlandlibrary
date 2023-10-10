package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Stmnt extends ASTList implements TokenId {
   protected int operatorId;

   public Stmnt(int var1, ASTree var2, ASTList var3) {
      super(var2, var3);
      this.operatorId = var1;
   }

   public Stmnt(int var1, ASTree var2) {
      super(var2);
      this.operatorId = var1;
   }

   public Stmnt(int var1) {
      this(var1, (ASTree)null);
   }

   public static Stmnt make(int var0, ASTree var1, ASTree var2) {
      return new Stmnt(var0, var1, new ASTList(var2));
   }

   public static Stmnt make(int var0, ASTree var1, ASTree var2, ASTree var3) {
      return new Stmnt(var0, var1, new ASTList(var2, new ASTList(var3)));
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atStmnt(this);
   }

   public int getOperator() {
      return this.operatorId;
   }

   protected String getTag() {
      return this.operatorId < 128 ? "stmnt:" + (char)this.operatorId : "stmnt:" + this.operatorId;
   }
}
