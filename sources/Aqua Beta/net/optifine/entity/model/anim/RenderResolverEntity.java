package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.IRenderResolver;
import net.optifine.entity.model.anim.RenderEntityParameterBool;
import net.optifine.entity.model.anim.RenderEntityParameterFloat;
import net.optifine.expr.IExpression;

public class RenderResolverEntity
implements IRenderResolver {
    public IExpression getParameter(String name) {
        RenderEntityParameterBool renderentityparameterbool = RenderEntityParameterBool.parse((String)name);
        if (renderentityparameterbool != null) {
            return renderentityparameterbool;
        }
        RenderEntityParameterFloat renderentityparameterfloat = RenderEntityParameterFloat.parse((String)name);
        return renderentityparameterfloat != null ? renderentityparameterfloat : null;
    }
}
