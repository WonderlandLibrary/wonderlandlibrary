// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.optifine.expr.ExpressionType;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.expr.IExpressionFloat;

public class ModelVariableFloat implements IExpressionFloat
{
    private String name;
    private ModelRenderer modelRenderer;
    private ModelVariableType enumModelVariable;
    
    public ModelVariableFloat(final String name, final ModelRenderer modelRenderer, final ModelVariableType enumModelVariable) {
        this.name = name;
        this.modelRenderer = modelRenderer;
        this.enumModelVariable = enumModelVariable;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }
    
    @Override
    public float eval() {
        return this.getValue();
    }
    
    public float getValue() {
        return this.enumModelVariable.getFloat(this.modelRenderer);
    }
    
    public void setValue(final float value) {
        this.enumModelVariable.setFloat(this.modelRenderer, value);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
