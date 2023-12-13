/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Visitor;

public class InstanceOfExpr
extends CastExpr {
    private static final long serialVersionUID = 1L;

    public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
        super(className, dim, expr);
    }

    public InstanceOfExpr(int type, int dim, ASTree expr) {
        super(type, dim, expr);
    }

    @Override
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atInstanceOfExpr(this);
    }
}

