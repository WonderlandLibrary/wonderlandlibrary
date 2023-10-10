// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler.ast;

import com.viaversion.viaversion.libs.javassist.compiler.CompileError;

public class StringL extends ASTree
{
    private static final long serialVersionUID = 1L;
    protected String text;
    
    public StringL(final String t) {
        this.text = t;
    }
    
    public String get() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return "\"" + this.text + "\"";
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atStringL(this);
    }
}
