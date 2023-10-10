package javassist.compiler.ast;

import javassist.CtField;
import javassist.compiler.CompileError;

public class Member extends Symbol {
   private CtField field = null;

   public Member(String var1) {
      super(var1);
   }

   public void setField(CtField var1) {
      this.field = var1;
   }

   public CtField getField() {
      return this.field;
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atMember(this);
   }
}
