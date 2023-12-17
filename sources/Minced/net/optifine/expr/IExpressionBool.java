// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

public interface IExpressionBool extends IExpression
{
    boolean eval();
    
    default ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
}
