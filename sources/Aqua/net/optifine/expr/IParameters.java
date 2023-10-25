package net.optifine.expr;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;

public interface IParameters {
    public ExpressionType[] getParameterTypes(IExpression[] var1);
}
