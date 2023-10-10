/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Visitor;

public class Symbol
extends ASTree {
    private static final long serialVersionUID = 1L;
    protected String identifier;

    public Symbol(String sym) {
        this.identifier = sym;
    }

    public String get() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atSymbol(this);
    }
}

