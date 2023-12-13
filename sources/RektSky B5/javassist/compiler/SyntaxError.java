/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler;

import javassist.compiler.CompileError;
import javassist.compiler.Lex;

public class SyntaxError
extends CompileError {
    private static final long serialVersionUID = 1L;

    public SyntaxError(Lex lexer) {
        super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
    }
}

