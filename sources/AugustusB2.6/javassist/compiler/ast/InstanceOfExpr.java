// 
// Decompiled by Procyon v0.5.36
// 

package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class InstanceOfExpr extends CastExpr
{
    public InstanceOfExpr(final ASTList className, final int dim, final ASTree expr) {
        super(className, dim, expr);
    }
    
    public InstanceOfExpr(final int type, final int dim, final ASTree expr) {
        super(type, dim, expr);
    }
    
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atInstanceOfExpr(this);
    }
}
