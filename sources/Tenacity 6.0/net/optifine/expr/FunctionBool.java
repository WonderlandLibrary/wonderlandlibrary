// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

public class FunctionBool implements IExpressionBool
{
    private FunctionType type;
    private IExpression[] arguments;
    
    public FunctionBool(final FunctionType type, final IExpression[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }
    
    @Override
    public boolean eval() {
        return this.type.evalBool(this.arguments);
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
    
    @Override
    public String toString() {
        return "" + this.type + "()";
    }
}
