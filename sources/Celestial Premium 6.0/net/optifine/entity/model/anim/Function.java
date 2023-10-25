/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.EnumFunctionType;
import net.optifine.entity.model.anim.IExpression;

public class Function
implements IExpression {
    private EnumFunctionType enumFunction;
    private IExpression[] arguments;

    public Function(EnumFunctionType enumFunction, IExpression[] arguments) {
        this.enumFunction = enumFunction;
        this.arguments = arguments;
    }

    @Override
    public float eval() {
        return this.enumFunction.eval(this.arguments);
    }

    public String toString() {
        return "" + (Object)((Object)this.enumFunction) + "()";
    }
}

