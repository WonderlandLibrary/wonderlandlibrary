/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Visitor;

public class StringL
extends ASTree {
    private static final long serialVersionUID = 1L;
    protected String text;

    public StringL(String t2) {
        this.text = t2;
    }

    public String get() {
        return this.text;
    }

    @Override
    public String toString() {
        return "\"" + this.text + "\"";
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atStringL(this);
    }
}

