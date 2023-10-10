/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Visitor;

public class Variable
extends Symbol {
    private static final long serialVersionUID = 1L;
    protected Declarator declarator;

    public Variable(String sym, Declarator d2) {
        super(sym);
        this.declarator = d2;
    }

    public Declarator getDeclarator() {
        return this.declarator;
    }

    @Override
    public String toString() {
        return this.identifier + ":" + this.declarator.getType();
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atVariable(this);
    }
}

