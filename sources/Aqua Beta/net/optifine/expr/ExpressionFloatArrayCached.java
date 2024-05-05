package net.optifine.expr;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionCached;
import net.optifine.expr.IExpressionFloatArray;

public class ExpressionFloatArrayCached
implements IExpressionFloatArray,
IExpressionCached {
    private IExpressionFloatArray expression;
    private boolean cached;
    private float[] value;

    public ExpressionFloatArrayCached(IExpressionFloatArray expression) {
        this.expression = expression;
    }

    public float[] eval() {
        if (!this.cached) {
            this.value = this.expression.eval();
            this.cached = true;
        }
        return this.value;
    }

    public void reset() {
        this.cached = false;
    }

    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }

    public String toString() {
        return "cached(" + this.expression + ")";
    }
}
