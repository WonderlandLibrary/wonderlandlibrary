/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.EnumModelVariable;
import net.optifine.entity.model.anim.IExpression;

public class ModelVariable
implements IExpression {
    private String name;
    private ModelRenderer modelRenderer;
    private EnumModelVariable enumModelVariable;

    public ModelVariable(String name, ModelRenderer modelRenderer, EnumModelVariable enumModelVariable) {
        this.name = name;
        this.modelRenderer = modelRenderer;
        this.enumModelVariable = enumModelVariable;
    }

    @Override
    public float eval() {
        return this.getValue();
    }

    public float getValue() {
        return this.enumModelVariable.getFloat(this.modelRenderer);
    }

    public void setValue(float value) {
        this.enumModelVariable.setFloat(this.modelRenderer, value);
    }

    public String toString() {
        return this.name;
    }
}

