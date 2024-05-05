package net.optifine.shaders.uniform;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionFloatArray;
import net.optifine.shaders.uniform.ShaderUniform1f;
import net.optifine.shaders.uniform.ShaderUniform1i;
import net.optifine.shaders.uniform.ShaderUniform2f;
import net.optifine.shaders.uniform.ShaderUniform3f;
import net.optifine.shaders.uniform.ShaderUniform4f;
import net.optifine.shaders.uniform.ShaderUniformBase;
import net.optifine.shaders.uniform.UniformType;

public enum UniformType {
    BOOL,
    INT,
    FLOAT,
    VEC2,
    VEC3,
    VEC4;


    public ShaderUniformBase makeShaderUniform(String name) {
        switch (1.$SwitchMap$net$optifine$shaders$uniform$UniformType[this.ordinal()]) {
            case 1: {
                return new ShaderUniform1i(name);
            }
            case 2: {
                return new ShaderUniform1i(name);
            }
            case 3: {
                return new ShaderUniform1f(name);
            }
            case 4: {
                return new ShaderUniform2f(name);
            }
            case 5: {
                return new ShaderUniform3f(name);
            }
            case 6: {
                return new ShaderUniform4f(name);
            }
        }
        throw new RuntimeException("Unknown uniform type: " + (Object)((Object)this));
    }

    public void updateUniform(IExpression expression, ShaderUniformBase uniform) {
        switch (1.$SwitchMap$net$optifine$shaders$uniform$UniformType[this.ordinal()]) {
            case 1: {
                this.updateUniformBool((IExpressionBool)expression, (ShaderUniform1i)uniform);
                return;
            }
            case 2: {
                this.updateUniformInt((IExpressionFloat)expression, (ShaderUniform1i)uniform);
                return;
            }
            case 3: {
                this.updateUniformFloat((IExpressionFloat)expression, (ShaderUniform1f)uniform);
                return;
            }
            case 4: {
                this.updateUniformFloat2((IExpressionFloatArray)expression, (ShaderUniform2f)uniform);
                return;
            }
            case 5: {
                this.updateUniformFloat3((IExpressionFloatArray)expression, (ShaderUniform3f)uniform);
                return;
            }
            case 6: {
                this.updateUniformFloat4((IExpressionFloatArray)expression, (ShaderUniform4f)uniform);
                return;
            }
        }
        throw new RuntimeException("Unknown uniform type: " + (Object)((Object)this));
    }

    private void updateUniformBool(IExpressionBool expression, ShaderUniform1i uniform) {
        boolean flag = expression.eval();
        int i = flag ? 1 : 0;
        uniform.setValue(i);
    }

    private void updateUniformInt(IExpressionFloat expression, ShaderUniform1i uniform) {
        int i = (int)expression.eval();
        uniform.setValue(i);
    }

    private void updateUniformFloat(IExpressionFloat expression, ShaderUniform1f uniform) {
        float f = expression.eval();
        uniform.setValue(f);
    }

    private void updateUniformFloat2(IExpressionFloatArray expression, ShaderUniform2f uniform) {
        float[] afloat = expression.eval();
        if (afloat.length != 2) {
            throw new RuntimeException("Value length is not 2, length: " + afloat.length);
        }
        uniform.setValue(afloat[0], afloat[1]);
    }

    private void updateUniformFloat3(IExpressionFloatArray expression, ShaderUniform3f uniform) {
        float[] afloat = expression.eval();
        if (afloat.length != 3) {
            throw new RuntimeException("Value length is not 3, length: " + afloat.length);
        }
        uniform.setValue(afloat[0], afloat[1], afloat[2]);
    }

    private void updateUniformFloat4(IExpressionFloatArray expression, ShaderUniform4f uniform) {
        float[] afloat = expression.eval();
        if (afloat.length != 4) {
            throw new RuntimeException("Value length is not 4, length: " + afloat.length);
        }
        uniform.setValue(afloat[0], afloat[1], afloat[2], afloat[3]);
    }

    public boolean matchesExpressionType(ExpressionType expressionType) {
        switch (1.$SwitchMap$net$optifine$shaders$uniform$UniformType[this.ordinal()]) {
            case 1: {
                return expressionType == ExpressionType.BOOL;
            }
            case 2: {
                return expressionType == ExpressionType.FLOAT;
            }
            case 3: {
                return expressionType == ExpressionType.FLOAT;
            }
            case 4: 
            case 5: 
            case 6: {
                return expressionType == ExpressionType.FLOAT_ARRAY;
            }
        }
        throw new RuntimeException("Unknown uniform type: " + (Object)((Object)this));
    }

    public static UniformType parse(String type) {
        UniformType[] auniformtype = UniformType.values();
        for (int i = 0; i < auniformtype.length; ++i) {
            UniformType uniformtype = auniformtype[i];
            if (!uniformtype.name().toLowerCase().equals((Object)type)) continue;
            return uniformtype;
        }
        return null;
    }
}
