package javassist.compiler;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class CompileError extends Exception {
   private Lex lex;
   private String reason;

   public CompileError(String var1, Lex var2) {
      this.reason = var1;
      this.lex = var2;
   }

   public CompileError(String var1) {
      this.reason = var1;
      this.lex = null;
   }

   public CompileError(CannotCompileException var1) {
      this(var1.getReason());
   }

   public CompileError(NotFoundException var1) {
      this("cannot find " + var1.getMessage());
   }

   public Lex getLex() {
      return this.lex;
   }

   public String getMessage() {
      return this.reason;
   }

   public String toString() {
      return "compile error: " + this.reason;
   }
}
