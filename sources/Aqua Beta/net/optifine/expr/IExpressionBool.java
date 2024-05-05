package net.optifine.expr;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;

public interface IExpressionBool
extends IExpression {
    public boolean eval();

    default public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
}
