package net.optifine.shaders.config;

import java.util.Map;
import net.minecraft.src.Config;
import net.optifine.expr.ConstantFloat;
import net.optifine.expr.FunctionBool;
import net.optifine.expr.FunctionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionResolver;

public class MacroExpressionResolver
implements IExpressionResolver {
    private Map<String, String> mapMacroValues = null;

    public MacroExpressionResolver(Map<String, String> mapMacroValues) {
        this.mapMacroValues = mapMacroValues;
    }

    public IExpression getExpression(String name) {
        String s1;
        String s = "defined_";
        if (name.startsWith(s)) {
            String s2 = name.substring(s.length());
            return this.mapMacroValues.containsKey((Object)s2) ? new FunctionBool(FunctionType.TRUE, (IExpression[])null) : new FunctionBool(FunctionType.FALSE, (IExpression[])null);
        }
        while (this.mapMacroValues.containsKey((Object)name) && (s1 = (String)this.mapMacroValues.get((Object)name)) != null && !s1.equals((Object)name)) {
            name = s1;
        }
        int i = Config.parseInt((String)name, (int)Integer.MIN_VALUE);
        if (i == Integer.MIN_VALUE) {
            Config.warn((String)("Unknown macro value: " + name));
            return new ConstantFloat(0.0f);
        }
        return new ConstantFloat((float)i);
    }
}
