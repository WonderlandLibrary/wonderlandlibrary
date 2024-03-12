// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

import net.optifine.shaders.uniform.Smoother;

public class FunctionFloat implements IExpressionFloat
{
    private FunctionType type;
    private IExpression[] arguments;
    private int smoothId;
    
    public FunctionFloat(final FunctionType type, final IExpression[] arguments) {
        this.smoothId = -1;
        this.type = type;
        this.arguments = arguments;
    }
    
    @Override
    public float eval() {
        final IExpression[] aiexpression = this.arguments;
        switch (this.type) {
            case SMOOTH: {
                final IExpression iexpression = aiexpression[0];
                if (!(iexpression instanceof ConstantFloat)) {
                    final float f = evalFloat(aiexpression, 0);
                    final float f2 = (aiexpression.length > 1) ? evalFloat(aiexpression, 1) : 1.0f;
                    final float f3 = (aiexpression.length > 2) ? evalFloat(aiexpression, 2) : f2;
                    if (this.smoothId < 0) {
                        this.smoothId = Smoother.getNextId();
                    }
                    final float f4 = Smoother.getSmoothValue(this.smoothId, f, f2, f3);
                    return f4;
                }
                break;
            }
        }
        return this.type.evalFloat(this.arguments);
    }
    
    private static float evalFloat(final IExpression[] exprs, final int index) {
        final IExpressionFloat iexpressionfloat = (IExpressionFloat)exprs[index];
        final float f = iexpressionfloat.eval();
        return f;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }
    
    @Override
    public String toString() {
        return "" + this.type + "()";
    }
}
