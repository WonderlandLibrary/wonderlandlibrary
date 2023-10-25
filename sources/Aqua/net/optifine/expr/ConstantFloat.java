package net.optifine.expr;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionFloat;

public class ConstantFloat
implements IExpressionFloat {
    private float value;

    public ConstantFloat(float value) {
        this.value = value;
    }

    public float eval() {
        return this.value;
    }

    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }

    public String toString() {
        return "" + this.value;
    }
}
