// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.uniform;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniform4f extends ShaderUniformBase
{
    private float[][] programValues;
    private static final float VALUE_UNKNOWN = -3.4028235E38f;
    
    public ShaderUniform4f(final String name) {
        super(name);
        this.resetValue();
    }
    
    public void setValue(final float v0, final float v1, final float v2, final float v3) {
        final int i = this.getProgram();
        final float[] afloat = this.programValues[i];
        if (afloat[0] != v0 || afloat[1] != v1 || afloat[2] != v2 || afloat[3] != v3) {
            afloat[0] = v0;
            afloat[1] = v1;
            afloat[2] = v2;
            afloat[3] = v3;
            final int j = this.getLocation();
            if (j >= 0) {
                ARBShaderObjects.glUniform4fARB(j, v0, v1, v2, v3);
                this.checkGLError();
            }
        }
    }
    
    public float[] getValue() {
        final int i = this.getProgram();
        final float[] afloat = this.programValues[i];
        return afloat;
    }
    
    @Override
    protected void onProgramSet(final int program) {
        if (program >= this.programValues.length) {
            final float[][] afloat = this.programValues;
            final float[][] afloat2 = new float[program + 10][];
            System.arraycopy(afloat, 0, afloat2, 0, afloat.length);
            this.programValues = afloat2;
        }
        if (this.programValues[program] == null) {
            this.programValues[program] = new float[] { -3.4028235E38f, -3.4028235E38f, -3.4028235E38f, -3.4028235E38f };
        }
    }
    
    @Override
    protected void resetValue() {
        this.programValues = new float[][] { { -3.4028235E38f, -3.4028235E38f, -3.4028235E38f, -3.4028235E38f } };
    }
}
