// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.optifine.expr.IExpression;

public class RenderResolverEntity implements IRenderResolver
{
    @Override
    public IExpression getParameter(final String name) {
        final RenderEntityParameterBool renderentityparameterbool = RenderEntityParameterBool.parse(name);
        if (renderentityparameterbool != null) {
            return renderentityparameterbool;
        }
        final RenderEntityParameterFloat renderentityparameterfloat = RenderEntityParameterFloat.parse(name);
        return (renderentityparameterfloat != null) ? renderentityparameterfloat : null;
    }
}
