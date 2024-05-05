package net.optifine.shaders.uniform;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.expr.ConstantFloat;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionResolver;
import net.optifine.shaders.SMCLog;
import net.optifine.shaders.uniform.ShaderParameterBool;
import net.optifine.shaders.uniform.ShaderParameterFloat;
import net.optifine.shaders.uniform.ShaderParameterIndexed;

public class ShaderExpressionResolver
implements IExpressionResolver {
    private Map<String, IExpression> mapExpressions = new HashMap();

    public ShaderExpressionResolver(Map<String, IExpression> map) {
        this.registerExpressions();
        for (String s : map.keySet()) {
            IExpression iexpression = (IExpression)map.get((Object)s);
            this.registerExpression(s, iexpression);
        }
    }

    private void registerExpressions() {
        ShaderParameterFloat[] ashaderparameterfloat = ShaderParameterFloat.values();
        for (int i = 0; i < ashaderparameterfloat.length; ++i) {
            ShaderParameterFloat shaderparameterfloat = ashaderparameterfloat[i];
            this.addParameterFloat(this.mapExpressions, shaderparameterfloat);
        }
        ShaderParameterBool[] ashaderparameterbool = ShaderParameterBool.values();
        for (int k = 0; k < ashaderparameterbool.length; ++k) {
            ShaderParameterBool shaderparameterbool = ashaderparameterbool[k];
            this.mapExpressions.put((Object)shaderparameterbool.getName(), (Object)shaderparameterbool);
        }
        for (BiomeGenBase biomegenbase : BiomeGenBase.BIOME_ID_MAP.values()) {
            String s = biomegenbase.biomeName.trim();
            s = "BIOME_" + s.toUpperCase().replace(' ', '_');
            int j = biomegenbase.biomeID;
            ConstantFloat iexpression = new ConstantFloat((float)j);
            this.registerExpression(s, (IExpression)iexpression);
        }
    }

    private void addParameterFloat(Map<String, IExpression> map, ShaderParameterFloat spf) {
        String[] astring = spf.getIndexNames1();
        if (astring == null) {
            map.put((Object)spf.getName(), (Object)new ShaderParameterIndexed(spf));
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = astring[i];
                String[] astring1 = spf.getIndexNames2();
                if (astring1 == null) {
                    map.put((Object)(spf.getName() + "." + s), (Object)new ShaderParameterIndexed(spf, i));
                    continue;
                }
                for (int j = 0; j < astring1.length; ++j) {
                    String s1 = astring1[j];
                    map.put((Object)(spf.getName() + "." + s + "." + s1), (Object)new ShaderParameterIndexed(spf, i, j));
                }
            }
        }
    }

    public boolean registerExpression(String name, IExpression expr) {
        if (this.mapExpressions.containsKey((Object)name)) {
            SMCLog.warning((String)("Expression already defined: " + name));
            return false;
        }
        this.mapExpressions.put((Object)name, (Object)expr);
        return true;
    }

    public IExpression getExpression(String name) {
        return (IExpression)this.mapExpressions.get((Object)name);
    }

    public boolean hasExpression(String name) {
        return this.mapExpressions.containsKey((Object)name);
    }
}
