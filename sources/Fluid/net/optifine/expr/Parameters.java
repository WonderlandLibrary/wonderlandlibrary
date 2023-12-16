// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.expr;

public class Parameters implements IParameters
{
    private ExpressionType[] parameterTypes;
    
    public Parameters(final ExpressionType[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    
    @Override
    public ExpressionType[] getParameterTypes(final IExpression[] params) {
        return this.parameterTypes;
    }
}
