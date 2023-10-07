// 
// Decompiled by Procyon v0.6.0
// 

package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ArrayInit extends ASTList
{
    private static final long serialVersionUID = 1L;
    
    public ArrayInit(final ASTree firstElement) {
        super(firstElement);
    }
    
    public int size() {
        final int s = this.length();
        if (s == 1 && this.head() == null) {
            return 0;
        }
        return s;
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atArrayInit(this);
    }
    
    public String getTag() {
        return "array";
    }
}
