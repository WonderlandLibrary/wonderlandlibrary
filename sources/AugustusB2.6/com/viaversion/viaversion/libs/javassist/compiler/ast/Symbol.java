// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;

public class Symbol extends ASTree
{
    private static final long serialVersionUID = 1L;
    protected String identifier;
    
    public Symbol(final String sym) {
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
    public void accept(final Visitor v) throws CompileError {
        v.atSymbol(this);
    }
}
