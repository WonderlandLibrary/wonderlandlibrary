// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import net.optifine.expr.ConstantFloat;
import net.minecraft.src.Config;
import net.optifine.expr.FunctionBool;
import net.optifine.expr.FunctionType;
import net.optifine.expr.IExpression;
import java.util.Map;
import net.optifine.expr.IExpressionResolver;

public class MacroExpressionResolver implements IExpressionResolver
{
    private Map<String, String> mapMacroValues;
    
    public MacroExpressionResolver(final Map<String, String> mapMacroValues) {
        this.mapMacroValues = null;
        this.mapMacroValues = mapMacroValues;
    }
    
    @Override
    public IExpression getExpression(String name) {
        final String s = "defined_";
        if (name.startsWith(s)) {
            final String s2 = name.substring(s.length());
            return this.mapMacroValues.containsKey(s2) ? new FunctionBool(FunctionType.TRUE, null) : new FunctionBool(FunctionType.FALSE, null);
        }
        while (this.mapMacroValues.containsKey(name)) {
            final String s3 = this.mapMacroValues.get(name);
            if (s3 == null) {
                break;
            }
            if (s3.equals(name)) {
                break;
            }
            name = s3;
        }
        final int i = Config.parseInt(name, Integer.MIN_VALUE);
        if (i == Integer.MIN_VALUE) {
            Config.warn("Unknown macro value: " + name);
            return new ConstantFloat(0.0f);
        }
        return new ConstantFloat((float)i);
    }
}
