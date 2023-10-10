// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;
import com.viaversion.viaversion.libs.javassist.compiler.TokenId;

public class Stmnt extends ASTList implements TokenId
{
    private static final long serialVersionUID = 1L;
    protected int operatorId;
    
    public Stmnt(final int op, final ASTree _head, final ASTList _tail) {
        super(_head, _tail);
        this.operatorId = op;
    }
    
    public Stmnt(final int op, final ASTree _head) {
        super(_head);
        this.operatorId = op;
    }
    
    public Stmnt(final int op) {
        this(op, null);
    }
    
    public static Stmnt make(final int op, final ASTree oprand1, final ASTree oprand2) {
        return new Stmnt(op, oprand1, new ASTList(oprand2));
    }
    
    public static Stmnt make(final int op, final ASTree op1, final ASTree op2, final ASTree op3) {
        return new Stmnt(op, op1, new ASTList(op2, new ASTList(op3)));
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atStmnt(this);
    }
    
    public int getOperator() {
        return this.operatorId;
    }
    
    @Override
    protected String getTag() {
        if (this.operatorId < 128) {
            return "stmnt:" + (char)this.operatorId;
        }
        return "stmnt:" + this.operatorId;
    }
}
