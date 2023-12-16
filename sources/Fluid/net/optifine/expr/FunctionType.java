// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.expr;

import net.minecraft.world.World;
import net.optifine.shaders.uniform.Smoother;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.optifine.util.MathUtils;
import net.minecraft.util.MathHelper;
import java.util.HashMap;
import java.util.Map;

public enum FunctionType
{
    PLUS("PLUS", 0, 10, ExpressionType.FLOAT, "+", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    MINUS("MINUS", 1, 10, ExpressionType.FLOAT, "-", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    MUL("MUL", 2, 11, ExpressionType.FLOAT, "*", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    DIV("DIV", 3, 11, ExpressionType.FLOAT, "/", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    MOD("MOD", 4, 11, ExpressionType.FLOAT, "%", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    NEG("NEG", 5, 12, ExpressionType.FLOAT, "neg", new ExpressionType[] { ExpressionType.FLOAT }), 
    PI("PI", 6, ExpressionType.FLOAT, "pi", new ExpressionType[0]), 
    SIN("SIN", 7, ExpressionType.FLOAT, "sin", new ExpressionType[] { ExpressionType.FLOAT }), 
    COS("COS", 8, ExpressionType.FLOAT, "cos", new ExpressionType[] { ExpressionType.FLOAT }), 
    ASIN("ASIN", 9, ExpressionType.FLOAT, "asin", new ExpressionType[] { ExpressionType.FLOAT }), 
    ACOS("ACOS", 10, ExpressionType.FLOAT, "acos", new ExpressionType[] { ExpressionType.FLOAT }), 
    TAN("TAN", 11, ExpressionType.FLOAT, "tan", new ExpressionType[] { ExpressionType.FLOAT }), 
    ATAN("ATAN", 12, ExpressionType.FLOAT, "atan", new ExpressionType[] { ExpressionType.FLOAT }), 
    ATAN2("ATAN2", 13, ExpressionType.FLOAT, "atan2", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    TORAD("TORAD", 14, ExpressionType.FLOAT, "torad", new ExpressionType[] { ExpressionType.FLOAT }), 
    TODEG("TODEG", 15, ExpressionType.FLOAT, "todeg", new ExpressionType[] { ExpressionType.FLOAT }), 
    MIN("MIN", 16, ExpressionType.FLOAT, "min", (IParameters)new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT)), 
    MAX("MAX", 17, ExpressionType.FLOAT, "max", (IParameters)new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT)), 
    CLAMP("CLAMP", 18, ExpressionType.FLOAT, "clamp", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    ABS("ABS", 19, ExpressionType.FLOAT, "abs", new ExpressionType[] { ExpressionType.FLOAT }), 
    FLOOR("FLOOR", 20, ExpressionType.FLOAT, "floor", new ExpressionType[] { ExpressionType.FLOAT }), 
    CEIL("CEIL", 21, ExpressionType.FLOAT, "ceil", new ExpressionType[] { ExpressionType.FLOAT }), 
    EXP("EXP", 22, ExpressionType.FLOAT, "exp", new ExpressionType[] { ExpressionType.FLOAT }), 
    FRAC("FRAC", 23, ExpressionType.FLOAT, "frac", new ExpressionType[] { ExpressionType.FLOAT }), 
    LOG("LOG", 24, ExpressionType.FLOAT, "log", new ExpressionType[] { ExpressionType.FLOAT }), 
    POW("POW", 25, ExpressionType.FLOAT, "pow", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    RANDOM("RANDOM", 26, ExpressionType.FLOAT, "random", new ExpressionType[0]), 
    ROUND("ROUND", 27, ExpressionType.FLOAT, "round", new ExpressionType[] { ExpressionType.FLOAT }), 
    SIGNUM("SIGNUM", 28, ExpressionType.FLOAT, "signum", new ExpressionType[] { ExpressionType.FLOAT }), 
    SQRT("SQRT", 29, ExpressionType.FLOAT, "sqrt", new ExpressionType[] { ExpressionType.FLOAT }), 
    FMOD("FMOD", 30, ExpressionType.FLOAT, "fmod", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    TIME("TIME", 31, ExpressionType.FLOAT, "time", new ExpressionType[0]), 
    IF("IF", 32, ExpressionType.FLOAT, "if", (IParameters)new ParametersVariable().first(ExpressionType.BOOL, ExpressionType.FLOAT).repeat(ExpressionType.BOOL, ExpressionType.FLOAT).last(ExpressionType.FLOAT)), 
    NOT("NOT", 33, 12, ExpressionType.BOOL, "!", new ExpressionType[] { ExpressionType.BOOL }), 
    AND("AND", 34, 3, ExpressionType.BOOL, "&&", new ExpressionType[] { ExpressionType.BOOL, ExpressionType.BOOL }), 
    OR("OR", 35, 2, ExpressionType.BOOL, "||", new ExpressionType[] { ExpressionType.BOOL, ExpressionType.BOOL }), 
    GREATER("GREATER", 36, 8, ExpressionType.BOOL, ">", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    GREATER_OR_EQUAL("GREATER_OR_EQUAL", 37, 8, ExpressionType.BOOL, ">=", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    SMALLER("SMALLER", 38, 8, ExpressionType.BOOL, "<", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    SMALLER_OR_EQUAL("SMALLER_OR_EQUAL", 39, 8, ExpressionType.BOOL, "<=", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    EQUAL("EQUAL", 40, 7, ExpressionType.BOOL, "==", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    NOT_EQUAL("NOT_EQUAL", 41, 7, ExpressionType.BOOL, "!=", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    BETWEEN("BETWEEN", 42, 7, ExpressionType.BOOL, "between", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    EQUALS("EQUALS", 43, 7, ExpressionType.BOOL, "equals", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    IN("IN", 44, ExpressionType.BOOL, "in", (IParameters)new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT).last(ExpressionType.FLOAT)), 
    SMOOTH("SMOOTH", 45, ExpressionType.FLOAT, "smooth", (IParameters)new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT).maxCount(4)), 
    TRUE("TRUE", 46, ExpressionType.BOOL, "true", new ExpressionType[0]), 
    FALSE("FALSE", 47, ExpressionType.BOOL, "false", new ExpressionType[0]), 
    VEC2("VEC2", 48, ExpressionType.FLOAT_ARRAY, "vec2", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    VEC3("VEC3", 49, ExpressionType.FLOAT_ARRAY, "vec3", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT }), 
    VEC4("VEC4", 50, ExpressionType.FLOAT_ARRAY, "vec4", new ExpressionType[] { ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT });
    
    private int precedence;
    private ExpressionType expressionType;
    private String name;
    private IParameters parameters;
    public static FunctionType[] VALUES;
    private static final Map<Integer, Float> mapSmooth;
    
    static {
        FunctionType.VALUES = values();
        mapSmooth = new HashMap<Integer, Float>();
    }
    
    private FunctionType(final String s, final int n, final ExpressionType expressionType, final String name, final ExpressionType[] parameterTypes) {
        this(s, n, 0, expressionType, name, parameterTypes);
    }
    
    private FunctionType(final String s, final int n, final int precedence, final ExpressionType expressionType, final String name, final ExpressionType[] parameterTypes) {
        this(s, n, precedence, expressionType, name, new Parameters(parameterTypes));
    }
    
    private FunctionType(final String s, final int n, final ExpressionType expressionType, final String name, final IParameters parameters) {
        this(s, n, 0, expressionType, name, parameters);
    }
    
    private FunctionType(final String s, final int n, final int precedence, final ExpressionType expressionType, final String name, final IParameters parameters) {
        this.precedence = precedence;
        this.expressionType = expressionType;
        this.name = name;
        this.parameters = parameters;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getPrecedence() {
        return this.precedence;
    }
    
    public ExpressionType getExpressionType() {
        return this.expressionType;
    }
    
    public IParameters getParameters() {
        return this.parameters;
    }
    
    public int getParameterCount(final IExpression[] arguments) {
        return this.parameters.getParameterTypes(arguments).length;
    }
    
    public ExpressionType[] getParameterTypes(final IExpression[] arguments) {
        return this.parameters.getParameterTypes(arguments);
    }
    
    public float evalFloat(final IExpression[] args) {
        switch (this) {
            case PLUS: {
                return evalFloat(args, 0) + evalFloat(args, 1);
            }
            case MINUS: {
                return evalFloat(args, 0) - evalFloat(args, 1);
            }
            case MUL: {
                return evalFloat(args, 0) * evalFloat(args, 1);
            }
            case DIV: {
                return evalFloat(args, 0) / evalFloat(args, 1);
            }
            case MOD: {
                final float f = evalFloat(args, 0);
                final float f2 = evalFloat(args, 1);
                return f - f2 * (int)(f / f2);
            }
            case NEG: {
                return -evalFloat(args, 0);
            }
            case PI: {
                return MathHelper.PI;
            }
            case SIN: {
                return MathHelper.sin(evalFloat(args, 0));
            }
            case COS: {
                return MathHelper.cos(evalFloat(args, 0));
            }
            case ASIN: {
                return MathUtils.asin(evalFloat(args, 0));
            }
            case ACOS: {
                return MathUtils.acos(evalFloat(args, 0));
            }
            case TAN: {
                return (float)Math.tan(evalFloat(args, 0));
            }
            case ATAN: {
                return (float)Math.atan(evalFloat(args, 0));
            }
            case ATAN2: {
                return (float)MathHelper.atan2(evalFloat(args, 0), evalFloat(args, 1));
            }
            case TORAD: {
                return MathUtils.toRad(evalFloat(args, 0));
            }
            case TODEG: {
                return MathUtils.toDeg(evalFloat(args, 0));
            }
            case MIN: {
                return this.getMin(args);
            }
            case MAX: {
                return this.getMax(args);
            }
            case CLAMP: {
                return MathHelper.clamp_float(evalFloat(args, 0), evalFloat(args, 1), evalFloat(args, 2));
            }
            case ABS: {
                return MathHelper.abs(evalFloat(args, 0));
            }
            case EXP: {
                return (float)Math.exp(evalFloat(args, 0));
            }
            case FLOOR: {
                return (float)MathHelper.floor_float(evalFloat(args, 0));
            }
            case CEIL: {
                return (float)MathHelper.ceiling_float_int(evalFloat(args, 0));
            }
            case FRAC: {
                return (float)MathHelper.func_181162_h(evalFloat(args, 0));
            }
            case LOG: {
                return (float)Math.log(evalFloat(args, 0));
            }
            case POW: {
                return (float)Math.pow(evalFloat(args, 0), evalFloat(args, 1));
            }
            case RANDOM: {
                return (float)Math.random();
            }
            case ROUND: {
                return (float)Math.round(evalFloat(args, 0));
            }
            case SIGNUM: {
                return Math.signum(evalFloat(args, 0));
            }
            case SQRT: {
                return MathHelper.sqrt_float(evalFloat(args, 0));
            }
            case FMOD: {
                final float f3 = evalFloat(args, 0);
                final float f4 = evalFloat(args, 1);
                return f3 - f4 * MathHelper.floor_float(f3 / f4);
            }
            case TIME: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                final World world = minecraft.theWorld;
                if (world == null) {
                    return 0.0f;
                }
                return world.getTotalWorldTime() % 24000L + Config.renderPartialTicks;
            }
            case IF: {
                final int i = (args.length - 1) / 2;
                for (int k = 0; k < i; ++k) {
                    final int l = k * 2;
                    if (evalBool(args, l)) {
                        return evalFloat(args, l + 1);
                    }
                }
                return evalFloat(args, i * 2);
            }
            case SMOOTH: {
                final int j = (int)evalFloat(args, 0);
                final float f5 = evalFloat(args, 1);
                final float f6 = (args.length > 2) ? evalFloat(args, 2) : 1.0f;
                final float f7 = (args.length > 3) ? evalFloat(args, 3) : f6;
                final float f8 = Smoother.getSmoothValue(j, f5, f6, f7);
                return f8;
            }
            default: {
                Config.warn("Unknown function type: " + this);
                return 0.0f;
            }
        }
    }
    
    private float getMin(final IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.min(evalFloat(exprs, 0), evalFloat(exprs, 1));
        }
        float f = evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            final float f2 = evalFloat(exprs, i);
            if (f2 < f) {
                f = f2;
            }
        }
        return f;
    }
    
    private float getMax(final IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.max(evalFloat(exprs, 0), evalFloat(exprs, 1));
        }
        float f = evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            final float f2 = evalFloat(exprs, i);
            if (f2 > f) {
                f = f2;
            }
        }
        return f;
    }
    
    private static float evalFloat(final IExpression[] exprs, final int index) {
        final IExpressionFloat iexpressionfloat = (IExpressionFloat)exprs[index];
        final float f = iexpressionfloat.eval();
        return f;
    }
    
    public boolean evalBool(final IExpression[] args) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
            case NOT: {
                return !evalBool(args, 0);
            }
            case AND: {
                return evalBool(args, 0) && evalBool(args, 1);
            }
            case OR: {
                return evalBool(args, 0) || evalBool(args, 1);
            }
            case GREATER: {
                return evalFloat(args, 0) > evalFloat(args, 1);
            }
            case GREATER_OR_EQUAL: {
                return evalFloat(args, 0) >= evalFloat(args, 1);
            }
            case SMALLER: {
                return evalFloat(args, 0) < evalFloat(args, 1);
            }
            case SMALLER_OR_EQUAL: {
                return evalFloat(args, 0) <= evalFloat(args, 1);
            }
            case EQUAL: {
                return evalFloat(args, 0) == evalFloat(args, 1);
            }
            case NOT_EQUAL: {
                return evalFloat(args, 0) != evalFloat(args, 1);
            }
            case BETWEEN: {
                final float f = evalFloat(args, 0);
                return f >= evalFloat(args, 1) && f <= evalFloat(args, 2);
            }
            case EQUALS: {
                final float f2 = evalFloat(args, 0) - evalFloat(args, 1);
                final float f3 = evalFloat(args, 2);
                return Math.abs(f2) <= f3;
            }
            case IN: {
                final float f4 = evalFloat(args, 0);
                for (int i = 1; i < args.length; ++i) {
                    final float f5 = evalFloat(args, i);
                    if (f4 == f5) {
                        return true;
                    }
                }
                return false;
            }
            default: {
                Config.warn("Unknown function type: " + this);
                return false;
            }
        }
    }
    
    private static boolean evalBool(final IExpression[] exprs, final int index) {
        final IExpressionBool iexpressionbool = (IExpressionBool)exprs[index];
        final boolean flag = iexpressionbool.eval();
        return flag;
    }
    
    public float[] evalFloatArray(final IExpression[] args) {
        switch (this) {
            case VEC2: {
                return new float[] { evalFloat(args, 0), evalFloat(args, 1) };
            }
            case VEC3: {
                return new float[] { evalFloat(args, 0), evalFloat(args, 1), evalFloat(args, 2) };
            }
            case VEC4: {
                return new float[] { evalFloat(args, 0), evalFloat(args, 1), evalFloat(args, 2), evalFloat(args, 3) };
            }
            default: {
                Config.warn("Unknown function type: " + this);
                return null;
            }
        }
    }
    
    public static FunctionType parse(final String str) {
        for (int i = 0; i < FunctionType.VALUES.length; ++i) {
            final FunctionType functiontype = FunctionType.VALUES[i];
            if (functiontype.getName().equals(str)) {
                return functiontype;
            }
        }
        return null;
    }
}
