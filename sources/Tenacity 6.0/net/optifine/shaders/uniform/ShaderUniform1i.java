// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.uniform;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniform1i extends ShaderUniformBase
{
    private int[] programValues;
    private static final int VALUE_UNKNOWN = Integer.MIN_VALUE;
    
    public ShaderUniform1i(final String name) {
        super(name);
        this.resetValue();
    }
    
    public void setValue(final int valueNew) {
        final int i = this.getProgram();
        final int j = this.programValues[i];
        if (valueNew != j) {
            this.programValues[i] = valueNew;
            final int k = this.getLocation();
            if (k >= 0) {
                ARBShaderObjects.glUniform1iARB(k, valueNew);
                this.checkGLError();
            }
        }
    }
    
    public int getValue() {
        final int i = this.getProgram();
        final int j = this.programValues[i];
        return j;
    }
    
    @Override
    protected void onProgramSet(final int program) {
        if (program >= this.programValues.length) {
            final int[] aint = this.programValues;
            final int[] aint2 = new int[program + 10];
            System.arraycopy(aint, 0, aint2, 0, aint.length);
            for (int i = aint.length; i < aint2.length; ++i) {
                aint2[i] = Integer.MIN_VALUE;
            }
            this.programValues = aint2;
        }
    }
    
    @Override
    protected void resetValue() {
        this.programValues = new int[] { Integer.MIN_VALUE };
    }
}
