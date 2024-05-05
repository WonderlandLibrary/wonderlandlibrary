package net.optifine.shaders.uniform;

import java.util.ArrayList;
import java.util.Map;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionCached;
import net.optifine.shaders.uniform.CustomUniform;

public class CustomUniforms {
    private CustomUniform[] uniforms;
    private IExpressionCached[] expressionsCached;

    public CustomUniforms(CustomUniform[] uniforms, Map<String, IExpression> mapExpressions) {
        this.uniforms = uniforms;
        ArrayList list = new ArrayList();
        for (String s : mapExpressions.keySet()) {
            IExpression iexpression = (IExpression)mapExpressions.get((Object)s);
            if (!(iexpression instanceof IExpressionCached)) continue;
            IExpressionCached iexpressioncached = (IExpressionCached)iexpression;
            list.add((Object)iexpressioncached);
        }
        this.expressionsCached = (IExpressionCached[])list.toArray((Object[])new IExpressionCached[list.size()]);
    }

    public void setProgram(int program) {
        for (int i = 0; i < this.uniforms.length; ++i) {
            CustomUniform customuniform = this.uniforms[i];
            customuniform.setProgram(program);
        }
    }

    public void update() {
        this.resetCache();
        for (int i = 0; i < this.uniforms.length; ++i) {
            CustomUniform customuniform = this.uniforms[i];
            customuniform.update();
        }
    }

    private void resetCache() {
        for (int i = 0; i < this.expressionsCached.length; ++i) {
            IExpressionCached iexpressioncached = this.expressionsCached[i];
            iexpressioncached.reset();
        }
    }

    public void reset() {
        for (int i = 0; i < this.uniforms.length; ++i) {
            CustomUniform customuniform = this.uniforms[i];
            customuniform.reset();
        }
    }
}
