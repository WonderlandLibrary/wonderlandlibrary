// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import net.optifine.expr.IExpression;
import java.util.HashMap;
import java.util.Map;
import net.optifine.expr.IExpressionResolver;

public class ShaderOptionResolver implements IExpressionResolver
{
    private Map<String, ExpressionShaderOptionSwitch> mapOptions;
    
    public ShaderOptionResolver(final ShaderOption[] options) {
        this.mapOptions = new HashMap<String, ExpressionShaderOptionSwitch>();
        for (int i = 0; i < options.length; ++i) {
            final ShaderOption shaderoption = options[i];
            if (shaderoption instanceof ShaderOptionSwitch) {
                final ShaderOptionSwitch shaderoptionswitch = (ShaderOptionSwitch)shaderoption;
                final ExpressionShaderOptionSwitch expressionshaderoptionswitch = new ExpressionShaderOptionSwitch(shaderoptionswitch);
                this.mapOptions.put(shaderoption.getName(), expressionshaderoptionswitch);
            }
        }
    }
    
    @Override
    public IExpression getExpression(final String name) {
        final ExpressionShaderOptionSwitch expressionshaderoptionswitch = this.mapOptions.get(name);
        return expressionshaderoptionswitch;
    }
}
