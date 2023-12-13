/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Visitor;

public class CondExpr
extends ASTList {
    private static final long serialVersionUID = 1L;

    public CondExpr(ASTree cond, ASTree thenp, ASTree elsep) {
        super(cond, new ASTList(thenp, new ASTList(elsep)));
    }

    public ASTree condExpr() {
        return this.head();
    }

    public void setCond(ASTree t2) {
        this.setHead(t2);
    }

    public ASTree thenExpr() {
        return this.tail().head();
    }

    public void setThen(ASTree t2) {
        this.tail().setHead(t2);
    }

    public ASTree elseExpr() {
        return this.tail().tail().head();
    }

    public void setElse(ASTree t2) {
        this.tail().tail().setHead(t2);
    }

    @Override
    public String getTag() {
        return "?:";
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atCondExpr(this);
    }
}

