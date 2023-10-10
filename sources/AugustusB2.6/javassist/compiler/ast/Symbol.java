// 
// Decompiled by Procyon v0.5.36
// 

package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Symbol extends ASTree
{
    protected String identifier;
    
    public Symbol(final String sym) {
        this.identifier = sym;
    }
    
    public String get() {
        return this.identifier;
    }
    
    public String toString() {
        return this.identifier;
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atSymbol(this);
    }
}
