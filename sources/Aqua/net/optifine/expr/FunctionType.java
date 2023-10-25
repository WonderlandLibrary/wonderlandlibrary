package net.optifine.expr;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.FunctionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IParameters;
import net.optifine.expr.Parameters;
import net.optifine.expr.ParametersVariable;
import net.optifine.shaders.uniform.Smoother;
import net.optifine.util.MathUtils;

public enum FunctionType {
    PLUS(10, ExpressionType.FLOAT, "+", ExpressionType.FLOAT, ExpressionType.FLOAT),
    MINUS(10, ExpressionType.FLOAT, "-", ExpressionType.FLOAT, ExpressionType.FLOAT),
    MUL(11, ExpressionType.FLOAT, "*", ExpressionType.FLOAT, ExpressionType.FLOAT),
    DIV(11, ExpressionType.FLOAT, "/", ExpressionType.FLOAT, ExpressionType.FLOAT),
    MOD(11, ExpressionType.FLOAT, "%", ExpressionType.FLOAT, ExpressionType.FLOAT),
    NEG(12, ExpressionType.FLOAT, "neg", ExpressionType.FLOAT),
    PI(ExpressionType.FLOAT, "pi", new ExpressionType[0]),
    SIN(ExpressionType.FLOAT, "sin", ExpressionType.FLOAT),
    COS(ExpressionType.FLOAT, "cos", ExpressionType.FLOAT),
    ASIN(ExpressionType.FLOAT, "asin", ExpressionType.FLOAT),
    ACOS(ExpressionType.FLOAT, "acos", ExpressionType.FLOAT),
    TAN(ExpressionType.FLOAT, "tan", ExpressionType.FLOAT),
    ATAN(ExpressionType.FLOAT, "atan", ExpressionType.FLOAT),
    ATAN2(ExpressionType.FLOAT, "atan2", ExpressionType.FLOAT, ExpressionType.FLOAT),
    TORAD(ExpressionType.FLOAT, "torad", ExpressionType.FLOAT),
    TODEG(ExpressionType.FLOAT, "todeg", ExpressionType.FLOAT),
    MIN(ExpressionType.FLOAT, "min", (IParameters)new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
    MAX(ExpressionType.FLOAT, "max", (IParameters)new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
    CLAMP(ExpressionType.FLOAT, "clamp", ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT),
    ABS(ExpressionType.FLOAT, "abs", ExpressionType.FLOAT),
    FLOOR(ExpressionType.FLOAT, "floor", ExpressionType.FLOAT),
    CEIL(ExpressionType.FLOAT, "ceil", ExpressionType.FLOAT),
    EXP(ExpressionType.FLOAT, "exp", ExpressionType.FLOAT),
    FRAC(ExpressionType.FLOAT, "frac", ExpressionType.FLOAT),
    LOG(ExpressionType.FLOAT, "log", ExpressionType.FLOAT),
    POW(ExpressionType.FLOAT, "pow", ExpressionType.FLOAT, ExpressionType.FLOAT),
    RANDOM(ExpressionType.FLOAT, "random", new ExpressionType[0]),
    ROUND(ExpressionType.FLOAT, "round", ExpressionType.FLOAT),
    SIGNUM(ExpressionType.FLOAT, "signum", ExpressionType.FLOAT),
    SQRT(ExpressionType.FLOAT, "sqrt", ExpressionType.FLOAT),
    FMOD(ExpressionType.FLOAT, "fmod", ExpressionType.FLOAT, ExpressionType.FLOAT),
    TIME(ExpressionType.FLOAT, "time", new ExpressionType[0]),
    IF(ExpressionType.FLOAT, "if", (IParameters)new ParametersVariable().first(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT}).last(new ExpressionType[]{ExpressionType.FLOAT})),
    NOT(12, ExpressionType.BOOL, "!", ExpressionType.BOOL),
    AND(3, ExpressionType.BOOL, "&&", ExpressionType.BOOL, ExpressionType.BOOL),
    OR(2, ExpressionType.BOOL, "||", ExpressionType.BOOL, ExpressionType.BOOL),
    GREATER(8, ExpressionType.BOOL, ">", ExpressionType.FLOAT, ExpressionType.FLOAT),
    GREATER_OR_EQUAL(8, ExpressionType.BOOL, ">=", ExpressionType.FLOAT, ExpressionType.FLOAT),
    SMALLER(8, ExpressionType.BOOL, "<", ExpressionType.FLOAT, ExpressionType.FLOAT),
    SMALLER_OR_EQUAL(8, ExpressionType.BOOL, "<=", ExpressionType.FLOAT, ExpressionType.FLOAT),
    EQUAL(7, ExpressionType.BOOL, "==", ExpressionType.FLOAT, ExpressionType.FLOAT),
    NOT_EQUAL(7, ExpressionType.BOOL, "!=", ExpressionType.FLOAT, ExpressionType.FLOAT),
    BETWEEN(7, ExpressionType.BOOL, "between", ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT),
    EQUALS(7, ExpressionType.BOOL, "equals", ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT),
    IN(ExpressionType.BOOL, "in", (IParameters)new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT}).last(new ExpressionType[]{ExpressionType.FLOAT})),
    SMOOTH(ExpressionType.FLOAT, "smooth", (IParameters)new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT}).maxCount(4)),
    TRUE(ExpressionType.BOOL, "true", new ExpressionType[0]),
    FALSE(ExpressionType.BOOL, "false", new ExpressionType[0]),
    VEC2(ExpressionType.FLOAT_ARRAY, "vec2", ExpressionType.FLOAT, ExpressionType.FLOAT),
    VEC3(ExpressionType.FLOAT_ARRAY, "vec3", ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT),
    VEC4(ExpressionType.FLOAT_ARRAY, "vec4", ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT);

    private int precedence;
    private ExpressionType expressionType;
    private String name;
    private IParameters parameters;
    public static FunctionType[] VALUES;
    private static final Map<Integer, Float> mapSmooth;

    private FunctionType(ExpressionType expressionType, String name, ExpressionType ... parameterTypes) {
        this(0, expressionType, name, parameterTypes);
    }

    private FunctionType(int precedence, ExpressionType expressionType, String name, ExpressionType ... parameterTypes) {
        this(precedence, expressionType, name, (IParameters)new Parameters(parameterTypes));
    }

    private FunctionType(ExpressionType expressionType, String name, IParameters parameters) {
        this(0, expressionType, name, parameters);
    }

    private FunctionType(int precedence, ExpressionType expressionType, String name, IParameters parameters) {
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

    public int getParameterCount(IExpression[] arguments) {
        return this.parameters.getParameterTypes(arguments).length;
    }

    public ExpressionType[] getParameterTypes(IExpression[] arguments) {
        return this.parameters.getParameterTypes(arguments);
    }

    public float evalFloat(IExpression[] args) {
        switch (1.$SwitchMap$net$optifine$expr$FunctionType[this.ordinal()]) {
            case 1: {
                return FunctionType.evalFloat(args, 0) + FunctionType.evalFloat(args, 1);
            }
            case 2: {
                return FunctionType.evalFloat(args, 0) - FunctionType.evalFloat(args, 1);
            }
            case 3: {
                return FunctionType.evalFloat(args, 0) * FunctionType.evalFloat(args, 1);
            }
            case 4: {
                return FunctionType.evalFloat(args, 0) / FunctionType.evalFloat(args, 1);
            }
            case 5: {
                float f = FunctionType.evalFloat(args, 0);
                float f1 = FunctionType.evalFloat(args, 1);
                return f - f1 * (float)((int)(f / f1));
            }
            case 6: {
                return -FunctionType.evalFloat(args, 0);
            }
            case 7: {
                return MathHelper.PI;
            }
            case 8: {
                return MathHelper.sin((float)FunctionType.evalFloat(args, 0));
            }
            case 9: {
                return MathHelper.cos((float)FunctionType.evalFloat(args, 0));
            }
            case 10: {
                return MathUtils.asin((float)FunctionType.evalFloat(args, 0));
            }
            case 11: {
                return MathUtils.acos((float)FunctionType.evalFloat(args, 0));
            }
            case 12: {
                return (float)Math.tan((double)FunctionType.evalFloat(args, 0));
            }
            case 13: {
                return (float)Math.atan((double)FunctionType.evalFloat(args, 0));
            }
            case 14: {
                return (float)MathHelper.atan2((double)FunctionType.evalFloat(args, 0), (double)FunctionType.evalFloat(args, 1));
            }
            case 15: {
                return MathUtils.toRad((float)FunctionType.evalFloat(args, 0));
            }
            case 16: {
                return MathUtils.toDeg((float)FunctionType.evalFloat(args, 0));
            }
            case 17: {
                return this.getMin(args);
            }
            case 18: {
                return this.getMax(args);
            }
            case 19: {
                return Float.valueOf((float)MathHelper.clamp_float((float)FunctionType.evalFloat(args, 0), (float)FunctionType.evalFloat(args, 1), (float)FunctionType.evalFloat(args, 2))).floatValue();
            }
            case 20: {
                return MathHelper.abs((float)FunctionType.evalFloat(args, 0));
            }
            case 21: {
                return (float)Math.exp((double)FunctionType.evalFloat(args, 0));
            }
            case 22: {
                return MathHelper.floor_float((float)FunctionType.evalFloat(args, 0));
            }
            case 23: {
                return MathHelper.ceiling_float_int((float)FunctionType.evalFloat(args, 0));
            }
            case 24: {
                return (float)MathHelper.func_181162_h((double)FunctionType.evalFloat(args, 0));
            }
            case 25: {
                return (float)Math.log((double)FunctionType.evalFloat(args, 0));
            }
            case 26: {
                return (float)Math.pow((double)FunctionType.evalFloat(args, 0), (double)FunctionType.evalFloat(args, 1));
            }
            case 27: {
                return (float)Math.random();
            }
            case 28: {
                return Math.round((float)FunctionType.evalFloat(args, 0));
            }
            case 29: {
                return Math.signum((float)FunctionType.evalFloat(args, 0));
            }
            case 30: {
                return MathHelper.sqrt_float((float)FunctionType.evalFloat(args, 0));
            }
            case 31: {
                float f2 = FunctionType.evalFloat(args, 0);
                float f3 = FunctionType.evalFloat(args, 1);
                return f2 - f3 * (float)MathHelper.floor_float((float)(f2 / f3));
            }
            case 32: {
                Minecraft minecraft = Minecraft.getMinecraft();
                WorldClient world = minecraft.theWorld;
                if (world == null) {
                    return 0.0f;
                }
                return (float)(world.getTotalWorldTime() % 24000L) + Config.renderPartialTicks;
            }
            case 33: {
                int i = (args.length - 1) / 2;
                for (int k = 0; k < i; ++k) {
                    int l = k * 2;
                    if (!FunctionType.evalBool(args, l)) continue;
                    return FunctionType.evalFloat(args, l + 1);
                }
                return FunctionType.evalFloat(args, i * 2);
            }
            case 34: {
                int j = (int)FunctionType.evalFloat(args, 0);
                float f4 = FunctionType.evalFloat(args, 1);
                float f5 = args.length > 2 ? FunctionType.evalFloat(args, 2) : 1.0f;
                float f6 = args.length > 3 ? FunctionType.evalFloat(args, 3) : f5;
                return Smoother.getSmoothValue((int)j, (float)f4, (float)f5, (float)f6);
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return 0.0f;
    }

    private float getMin(IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.min((float)FunctionType.evalFloat(exprs, 0), (float)FunctionType.evalFloat(exprs, 1));
        }
        float f = FunctionType.evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            float f1 = FunctionType.evalFloat(exprs, i);
            if (!(f1 < f)) continue;
            f = f1;
        }
        return f;
    }

    private float getMax(IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.max((float)FunctionType.evalFloat(exprs, 0), (float)FunctionType.evalFloat(exprs, 1));
        }
        float f = FunctionType.evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            float f1 = FunctionType.evalFloat(exprs, i);
            if (!(f1 > f)) continue;
            f = f1;
        }
        return f;
    }

    private static float evalFloat(IExpression[] exprs, int index) {
        IExpressionFloat iexpressionfloat = (IExpressionFloat)exprs[index];
        return iexpressionfloat.eval();
    }

    public boolean evalBool(IExpression[] args) {
        switch (1.$SwitchMap$net$optifine$expr$FunctionType[this.ordinal()]) {
            case 35: {
                return true;
            }
            case 36: {
                return false;
            }
            case 37: {
                return !FunctionType.evalBool(args, 0);
            }
            case 38: {
                return FunctionType.evalBool(args, 0) && FunctionType.evalBool(args, 1);
            }
            case 39: {
                return FunctionType.evalBool(args, 0) || FunctionType.evalBool(args, 1);
            }
            case 40: {
                return FunctionType.evalFloat(args, 0) > FunctionType.evalFloat(args, 1);
            }
            case 41: {
                return FunctionType.evalFloat(args, 0) >= FunctionType.evalFloat(args, 1);
            }
            case 42: {
                return FunctionType.evalFloat(args, 0) < FunctionType.evalFloat(args, 1);
            }
            case 43: {
                return FunctionType.evalFloat(args, 0) <= FunctionType.evalFloat(args, 1);
            }
            case 44: {
                return FunctionType.evalFloat(args, 0) == FunctionType.evalFloat(args, 1);
            }
            case 45: {
                return FunctionType.evalFloat(args, 0) != FunctionType.evalFloat(args, 1);
            }
            case 46: {
                float f = FunctionType.evalFloat(args, 0);
                return f >= FunctionType.evalFloat(args, 1) && f <= FunctionType.evalFloat(args, 2);
            }
            case 47: {
                float f1 = FunctionType.evalFloat(args, 0) - FunctionType.evalFloat(args, 1);
                float f2 = FunctionType.evalFloat(args, 2);
                return Math.abs((float)f1) <= f2;
            }
            case 48: {
                float f3 = FunctionType.evalFloat(args, 0);
                for (int i = 1; i < args.length; ++i) {
                    float f4 = FunctionType.evalFloat(args, i);
                    if (f3 != f4) continue;
                    return true;
                }
                return false;
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return false;
    }

    private static boolean evalBool(IExpression[] exprs, int index) {
        IExpressionBool iexpressionbool = (IExpressionBool)exprs[index];
        return iexpressionbool.eval();
    }

    public float[] evalFloatArray(IExpression[] args) {
        switch (1.$SwitchMap$net$optifine$expr$FunctionType[this.ordinal()]) {
            case 49: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1)};
            }
            case 50: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1), FunctionType.evalFloat(args, 2)};
            }
            case 51: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1), FunctionType.evalFloat(args, 2), FunctionType.evalFloat(args, 3)};
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return null;
    }

    public static FunctionType parse(String str) {
        for (int i = 0; i < VALUES.length; ++i) {
            FunctionType functiontype = VALUES[i];
            if (!functiontype.getName().equals((Object)str)) continue;
            return functiontype;
        }
        return null;
    }

    static {
        VALUES = FunctionType.values();
        mapSmooth = new HashMap();
    }
}
