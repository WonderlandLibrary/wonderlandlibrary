// 
// Decompiled by Procyon v0.6.0
// 

package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class FieldDecl extends ASTList
{
    private static final long serialVersionUID = 1L;
    
    public FieldDecl(final ASTree _head, final ASTList _tail) {
        super(_head, _tail);
    }
    
    public ASTList getModifiers() {
        return (ASTList)this.getLeft();
    }
    
    public Declarator getDeclarator() {
        return (Declarator)this.tail().head();
    }
    
    public ASTree getInit() {
        return this.sublist(2).head();
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atFieldDecl(this);
    }
}
