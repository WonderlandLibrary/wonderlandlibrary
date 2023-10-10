package net.optifine.expr;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
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
    MIN(ExpressionType.FLOAT, "min", new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT)),
    MAX(ExpressionType.FLOAT, "max", new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT)),
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
    IF(ExpressionType.FLOAT, "if", new ParametersVariable().first(ExpressionType.BOOL, ExpressionType.FLOAT).repeat(ExpressionType.BOOL, ExpressionType.FLOAT).last(ExpressionType.FLOAT)),
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
    IN(ExpressionType.BOOL, "in", new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT).last(ExpressionType.FLOAT)),
    SMOOTH(ExpressionType.FLOAT, "smooth", new ParametersVariable().first(ExpressionType.FLOAT).repeat(ExpressionType.FLOAT).maxCount(4)),
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
        this(precedence, expressionType, name, new Parameters(parameterTypes));
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

    @SuppressWarnings("incomplete-switch")
	public float evalFloat(IExpression[] args) {
        switch (this) {
            case PLUS: {
                return FunctionType.evalFloat(args, 0) + FunctionType.evalFloat(args, 1);
            }
            case MINUS: {
                return FunctionType.evalFloat(args, 0) - FunctionType.evalFloat(args, 1);
            }
            case MUL: {
                return FunctionType.evalFloat(args, 0) * FunctionType.evalFloat(args, 1);
            }
            case DIV: {
                return FunctionType.evalFloat(args, 0) / FunctionType.evalFloat(args, 1);
            }
            case MOD: {
                float modX = FunctionType.evalFloat(args, 0);
                float modY = FunctionType.evalFloat(args, 1);
                return modX - modY * (float)((int)(modX / modY));
            }
            case NEG: {
                return -FunctionType.evalFloat(args, 0);
            }
            case PI: {
                return MathHelper.PI;
            }
            case SIN: {
                return MathHelper.sin((float)FunctionType.evalFloat(args, 0));
            }
            case COS: {
                return MathHelper.cos((float)FunctionType.evalFloat(args, 0));
            }
            case ASIN: {
                return MathUtils.asin(FunctionType.evalFloat(args, 0));
            }
            case ACOS: {
                return MathUtils.acos(FunctionType.evalFloat(args, 0));
            }
            case TAN: {
                return (float)Math.tan(FunctionType.evalFloat(args, 0));
            }
            case ATAN: {
                return (float)Math.atan(FunctionType.evalFloat(args, 0));
            }
            case ATAN2: {
                return (float)MathHelper.atan2((double)FunctionType.evalFloat(args, 0), (double)FunctionType.evalFloat(args, 1));
            }
            case TORAD: {
                return MathUtils.toRad(FunctionType.evalFloat(args, 0));
            }
            case TODEG: {
                return MathUtils.toDeg(FunctionType.evalFloat(args, 0));
            }
            case MIN: {
                return this.getMin(args);
            }
            case MAX: {
                return this.getMax(args);
            }
            case CLAMP: {
                return MathHelper.clamp_float((float)FunctionType.evalFloat(args, 0), (float)FunctionType.evalFloat(args, 1), (float)FunctionType.evalFloat(args, 2));
            }
            case ABS: {
                return MathHelper.abs((float)FunctionType.evalFloat(args, 0));
            }
            case EXP: {
                return (float)Math.exp(FunctionType.evalFloat(args, 0));
            }
            case FLOOR: {
                return MathHelper.floor_float((float)FunctionType.evalFloat(args, 0));
            }
            case CEIL: {
                return MathHelper.ceiling_float_int((float)FunctionType.evalFloat(args, 0));
            }
            case FRAC: {
                return (float)MathHelper.func_181162_h((double)FunctionType.evalFloat(args, 0));
            }
            case LOG: {
                return (float)Math.log(FunctionType.evalFloat(args, 0));
            }
            case POW: {
                return (float)Math.pow(FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1));
            }
            case RANDOM: {
                return (float)Math.random();
            }
            case ROUND: {
                return Math.round(FunctionType.evalFloat(args, 0));
            }
            case SIGNUM: {
                return Math.signum(FunctionType.evalFloat(args, 0));
            }
            case SQRT: {
                return MathHelper.sqrt_float((float)FunctionType.evalFloat(args, 0));
            }
            case FMOD: {
                float fmodX = FunctionType.evalFloat(args, 0);
                float fmodY = FunctionType.evalFloat(args, 1);
                return fmodX - fmodY * (float)MathHelper.floor_float((float)(fmodX / fmodY));
            }
            case TIME: {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.theWorld;
                if (world == null) {
                    return 0.0f;
                }
                return (float)(world.getTotalWorldTime() % 24000L) + Config.renderPartialTicks;
            }
            case IF: {
                int countChecks = (args.length - 1) / 2;
                for (int i = 0; i < countChecks; ++i) {
                    int index = i * 2;
                    if (!FunctionType.evalBool(args, index)) continue;
                    return FunctionType.evalFloat(args, index + 1);
                }
                return FunctionType.evalFloat(args, countChecks * 2);
            }
            case SMOOTH: {
                int id = (int)FunctionType.evalFloat(args, 0);
                float valRaw = FunctionType.evalFloat(args, 1);
                float valFadeUp = args.length > 2 ? FunctionType.evalFloat(args, 2) : 1.0f;
                float valFadeDown = args.length > 3 ? FunctionType.evalFloat(args, 3) : valFadeUp;
                float valSmooth = Smoother.getSmoothValue(id, valRaw, valFadeUp, valFadeDown);
                return valSmooth;
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return 0.0f;
    }

    private float getMin(IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.min(FunctionType.evalFloat(exprs, 0), FunctionType.evalFloat(exprs, 1));
        }
        float valMin = FunctionType.evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            float valExpr = FunctionType.evalFloat(exprs, i);
            if (!(valExpr < valMin)) continue;
            valMin = valExpr;
        }
        return valMin;
    }

    private float getMax(IExpression[] exprs) {
        if (exprs.length == 2) {
            return Math.max(FunctionType.evalFloat(exprs, 0), FunctionType.evalFloat(exprs, 1));
        }
        float valMax = FunctionType.evalFloat(exprs, 0);
        for (int i = 1; i < exprs.length; ++i) {
            float valExpr = FunctionType.evalFloat(exprs, i);
            if (!(valExpr > valMax)) continue;
            valMax = valExpr;
        }
        return valMax;
    }

    private static float evalFloat(IExpression[] exprs, int index) {
        IExpressionFloat ef = (IExpressionFloat)exprs[index];
        float val = ef.eval();
        return val;
    }

    @SuppressWarnings("incomplete-switch")
	public boolean evalBool(IExpression[] args) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
            case NOT: {
                return !FunctionType.evalBool(args, 0);
            }
            case AND: {
                return FunctionType.evalBool(args, 0) && FunctionType.evalBool(args, 1);
            }
            case OR: {
                return FunctionType.evalBool(args, 0) || FunctionType.evalBool(args, 1);
            }
            case GREATER: {
                return FunctionType.evalFloat(args, 0) > FunctionType.evalFloat(args, 1);
            }
            case GREATER_OR_EQUAL: {
                return FunctionType.evalFloat(args, 0) >= FunctionType.evalFloat(args, 1);
            }
            case SMALLER: {
                return FunctionType.evalFloat(args, 0) < FunctionType.evalFloat(args, 1);
            }
            case SMALLER_OR_EQUAL: {
                return FunctionType.evalFloat(args, 0) <= FunctionType.evalFloat(args, 1);
            }
            case EQUAL: {
                return FunctionType.evalFloat(args, 0) == FunctionType.evalFloat(args, 1);
            }
            case NOT_EQUAL: {
                return FunctionType.evalFloat(args, 0) != FunctionType.evalFloat(args, 1);
            }
            case BETWEEN: {
                float val = FunctionType.evalFloat(args, 0);
                return val >= FunctionType.evalFloat(args, 1) && val <= FunctionType.evalFloat(args, 2);
            }
            case EQUALS: {
                float diff = FunctionType.evalFloat(args, 0) - FunctionType.evalFloat(args, 1);
                float delta = FunctionType.evalFloat(args, 2);
                return Math.abs(diff) <= delta;
            }
            case IN: {
                float valIn = FunctionType.evalFloat(args, 0);
                for (int i = 1; i < args.length; ++i) {
                    float valCheck = FunctionType.evalFloat(args, i);
                    if (valIn != valCheck) continue;
                    return true;
                }
                return false;
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return false;
    }

    private static boolean evalBool(IExpression[] exprs, int index) {
        IExpressionBool eb = (IExpressionBool)exprs[index];
        boolean val = eb.eval();
        return val;
    }

    @SuppressWarnings("incomplete-switch")
	public float[] evalFloatArray(IExpression[] args) {
        switch (this) {
            case VEC2: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1)};
            }
            case VEC3: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1), FunctionType.evalFloat(args, 2)};
            }
            case VEC4: {
                return new float[]{FunctionType.evalFloat(args, 0), FunctionType.evalFloat(args, 1), FunctionType.evalFloat(args, 2), FunctionType.evalFloat(args, 3)};
            }
        }
        Config.warn((String)("Unknown function type: " + (Object)((Object)this)));
        return null;
    }

    public static FunctionType parse(String str) {
        for (int i = 0; i < VALUES.length; ++i) {
            FunctionType ef = VALUES[i];
            if (!ef.getName().equals(str)) continue;
            return ef;
        }
        return null;
    }

    static {
        VALUES = FunctionType.values();
        mapSmooth = new HashMap<Integer, Float>();
    }

}

