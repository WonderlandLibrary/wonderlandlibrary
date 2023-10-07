// 
// Decompiled by Procyon v0.6.0
// 

package javassist.compiler;

public class SyntaxError extends CompileError
{
    private static final long serialVersionUID = 1L;
    
    public SyntaxError(final Lex lexer) {
        super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
    }
}
