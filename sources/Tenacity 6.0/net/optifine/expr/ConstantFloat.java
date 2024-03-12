// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

public class ConstantFloat implements IExpressionFloat
{
    private float value;
    
    public ConstantFloat(final float value) {
        this.value = value;
    }
    
    @Override
    public float eval() {
        return this.value;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }
    
    @Override
    public String toString() {
        return "" + this.value;
    }
}
