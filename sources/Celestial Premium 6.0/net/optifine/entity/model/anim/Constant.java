/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.IExpression;

public class Constant
implements IExpression {
    private float value;

    public Constant(float value) {
        this.value = value;
    }

    @Override
    public float eval() {
        return this.value;
    }

    public String toString() {
        return "" + this.value;
    }
}

