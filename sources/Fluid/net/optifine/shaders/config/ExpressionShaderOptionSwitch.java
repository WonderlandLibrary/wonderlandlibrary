// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders.config;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;

public class ExpressionShaderOptionSwitch implements IExpressionBool
{
    private ShaderOptionSwitch shaderOption;
    
    public ExpressionShaderOptionSwitch(final ShaderOptionSwitch shaderOption) {
        this.shaderOption = shaderOption;
    }
    
    @Override
    public boolean eval() {
        return ShaderOptionSwitch.isTrue(this.shaderOption.getValue());
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(this.shaderOption).toString();
    }
}
