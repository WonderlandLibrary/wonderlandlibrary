/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Visitor;

public class AssignExpr
extends Expr {
    private static final long serialVersionUID = 1L;

    private AssignExpr(int op, ASTree _head, ASTList _tail) {
        super(op, _head, _tail);
    }

    public static AssignExpr makeAssign(int op, ASTree oprand1, ASTree oprand2) {
        return new AssignExpr(op, oprand1, new ASTList(oprand2));
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atAssignExpr(this);
    }
}

