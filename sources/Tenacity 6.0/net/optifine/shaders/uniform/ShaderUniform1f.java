// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.uniform;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniform1f extends ShaderUniformBase
{
    private float[] programValues;
    private static final float VALUE_UNKNOWN = -3.4028235E38f;
    
    public ShaderUniform1f(final String name) {
        super(name);
        this.resetValue();
    }
    
    public void setValue(final float valueNew) {
        final int i = this.getProgram();
        final float f = this.programValues[i];
        if (valueNew != f) {
            this.programValues[i] = valueNew;
            final int j = this.getLocation();
            if (j >= 0) {
                ARBShaderObjects.glUniform1fARB(j, valueNew);
                this.checkGLError();
            }
        }
    }
    
    public float getValue() {
        final int i = this.getProgram();
        final float f = this.programValues[i];
        return f;
    }
    
    @Override
    protected void onProgramSet(final int program) {
        if (program >= this.programValues.length) {
            final float[] afloat = this.programValues;
            final float[] afloat2 = new float[program + 10];
            System.arraycopy(afloat, 0, afloat2, 0, afloat.length);
            for (int i = afloat.length; i < afloat2.length; ++i) {
                afloat2[i] = -3.4028235E38f;
            }
            this.programValues = afloat2;
        }
    }
    
    @Override
    protected void resetValue() {
        this.programValues = new float[] { -3.4028235E38f };
    }
}
