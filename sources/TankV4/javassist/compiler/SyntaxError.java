package javassist.compiler;

public class SyntaxError extends CompileError {
   public SyntaxError(Lex var1) {
      super("syntax error near \"" + var1.getTextAround() + "\"", var1);
   }
}
