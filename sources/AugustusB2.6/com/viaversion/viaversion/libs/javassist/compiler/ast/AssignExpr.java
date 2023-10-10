// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;

public class AssignExpr extends Expr
{
    private static final long serialVersionUID = 1L;
    
    private AssignExpr(final int op, final ASTree _head, final ASTList _tail) {
        super(op, _head, _tail);
    }
    
    public static AssignExpr makeAssign(final int op, final ASTree oprand1, final ASTree oprand2) {
        return new AssignExpr(op, oprand1, new ASTList(oprand2));
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atAssignExpr(this);
    }
}
