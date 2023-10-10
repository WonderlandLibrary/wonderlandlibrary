/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler.ast;

import javassist.CtField;
import javassist.compiler.CompileError;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Visitor;

public class Member
extends Symbol {
    private static final long serialVersionUID = 1L;
    private CtField field = null;

    public Member(String name) {
        super(name);
    }

    public void setField(CtField f2) {
        this.field = f2;
    }

    public CtField getField() {
        return this.field;
    }

    @Override
    public void accept(Visitor v2) throws CompileError {
        v2.atMember(this);
    }
}

