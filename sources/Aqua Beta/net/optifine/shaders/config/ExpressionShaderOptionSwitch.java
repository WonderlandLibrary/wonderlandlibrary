package net.optifine.shaders.config;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;
import net.optifine.shaders.config.ShaderOptionSwitch;

public class ExpressionShaderOptionSwitch
implements IExpressionBool {
    private ShaderOptionSwitch shaderOption;

    public ExpressionShaderOptionSwitch(ShaderOptionSwitch shaderOption) {
        this.shaderOption = shaderOption;
    }

    public boolean eval() {
        return ShaderOptionSwitch.isTrue((String)this.shaderOption.getValue());
    }

    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }

    public String toString() {
        return "" + this.shaderOption;
    }
}
