/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTree;

public class NoFieldException
extends CompileError {
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private ASTree expr;

    public NoFieldException(String name, ASTree e2) {
        super("no such field: " + name);
        this.fieldName = name;
        this.expr = e2;
    }

    public String getField() {
        return this.fieldName;
    }

    public ASTree getExpr() {
        return this.expr;
    }
}

