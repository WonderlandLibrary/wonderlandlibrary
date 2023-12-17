// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.apache.logging.log4j.Logger;

public class ShaderUniform
{
    private static final Logger LOGGER;
    private int uniformLocation;
    private final int uniformCount;
    private final int uniformType;
    private final IntBuffer uniformIntBuffer;
    private final FloatBuffer uniformFloatBuffer;
    private final String shaderName;
    private boolean dirty;
    private final ShaderManager shaderManager;
    
    public ShaderUniform(final String name, final int type, final int count, final ShaderManager manager) {
        this.shaderName = name;
        this.uniformCount = count;
        this.uniformType = type;
        this.shaderManager = manager;
        if (type <= 3) {
            this.uniformIntBuffer = BufferUtils.createIntBuffer(count);
            this.uniformFloatBuffer = null;
        }
        else {
            this.uniformIntBuffer = null;
            this.uniformFloatBuffer = BufferUtils.createFloatBuffer(count);
        }
        this.uniformLocation = -1;
        this.markDirty();
    }
    
    private void markDirty() {
        this.dirty = true;
        if (this.shaderManager != null) {
            this.shaderManager.markDirty();
        }
    }
    
    public static int parseType(final String typeName) {
        int i = -1;
        if ("int".equals(typeName)) {
            i = 0;
        }
        else if ("float".equals(typeName)) {
            i = 4;
        }
        else if (typeName.startsWith("matrix")) {
            if (typeName.endsWith("2x2")) {
                i = 8;
            }
            else if (typeName.endsWith("3x3")) {
                i = 9;
            }
            else if (typeName.endsWith("4x4")) {
                i = 10;
            }
        }
        return i;
    }
    
    public void setUniformLocation(final int uniformLocationIn) {
        this.uniformLocation = uniformLocationIn;
    }
    
    public String getShaderName() {
        return this.shaderName;
    }
    
    public void set(final float p_148090_1_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148090_1_);
        this.markDirty();
    }
    
    public void set(final float p_148087_1_, final float p_148087_2_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148087_1_);
        this.uniformFloatBuffer.put(1, p_148087_2_);
        this.markDirty();
    }
    
    public void set(final float p_148095_1_, final float p_148095_2_, final float p_148095_3_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148095_1_);
        this.uniformFloatBuffer.put(1, p_148095_2_);
        this.uniformFloatBuffer.put(2, p_148095_3_);
        this.markDirty();
    }
    
    public void set(final float p_148081_1_, final float p_148081_2_, final float p_148081_3_, final float p_148081_4_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(p_148081_1_);
        this.uniformFloatBuffer.put(p_148081_2_);
        this.uniformFloatBuffer.put(p_148081_3_);
        this.uniformFloatBuffer.put(p_148081_4_);
        this.uniformFloatBuffer.flip();
        this.markDirty();
    }
    
    public void setSafe(final float p_148092_1_, final float p_148092_2_, final float p_148092_3_, final float p_148092_4_) {
        this.uniformFloatBuffer.position(0);
        if (this.uniformType >= 4) {
            this.uniformFloatBuffer.put(0, p_148092_1_);
        }
        if (this.uniformType >= 5) {
            this.uniformFloatBuffer.put(1, p_148092_2_);
        }
        if (this.uniformType >= 6) {
            this.uniformFloatBuffer.put(2, p_148092_3_);
        }
        if (this.uniformType >= 7) {
            this.uniformFloatBuffer.put(3, p_148092_4_);
        }
        this.markDirty();
    }
    
    public void set(final int p_148083_1_, final int p_148083_2_, final int p_148083_3_, final int p_148083_4_) {
        this.uniformIntBuffer.position(0);
        if (this.uniformType >= 0) {
            this.uniformIntBuffer.put(0, p_148083_1_);
        }
        if (this.uniformType >= 1) {
            this.uniformIntBuffer.put(1, p_148083_2_);
        }
        if (this.uniformType >= 2) {
            this.uniformIntBuffer.put(2, p_148083_3_);
        }
        if (this.uniformType >= 3) {
            this.uniformIntBuffer.put(3, p_148083_4_);
        }
        this.markDirty();
    }
    
    public void set(final float[] p_148097_1_) {
        if (p_148097_1_.length < this.uniformCount) {
            ShaderUniform.LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", (Object)this.uniformCount, (Object)p_148097_1_.length);
        }
        else {
            this.uniformFloatBuffer.position(0);
            this.uniformFloatBuffer.put(p_148097_1_);
            this.uniformFloatBuffer.position(0);
            this.markDirty();
        }
    }
    
    public void set(final float m00, final float m01, final float m02, final float m03, final float m10, final float m11, final float m12, final float m13, final float m20, final float m21, final float m22, final float m23, final float m30, final float m31, final float m32, final float m33) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, m00);
        this.uniformFloatBuffer.put(1, m01);
        this.uniformFloatBuffer.put(2, m02);
        this.uniformFloatBuffer.put(3, m03);
        this.uniformFloatBuffer.put(4, m10);
        this.uniformFloatBuffer.put(5, m11);
        this.uniformFloatBuffer.put(6, m12);
        this.uniformFloatBuffer.put(7, m13);
        this.uniformFloatBuffer.put(8, m20);
        this.uniformFloatBuffer.put(9, m21);
        this.uniformFloatBuffer.put(10, m22);
        this.uniformFloatBuffer.put(11, m23);
        this.uniformFloatBuffer.put(12, m30);
        this.uniformFloatBuffer.put(13, m31);
        this.uniformFloatBuffer.put(14, m32);
        this.uniformFloatBuffer.put(15, m33);
        this.markDirty();
    }
    
    public void set(final Matrix4f matrix) {
        this.set(matrix.m00, matrix.m01, matrix.m02, matrix.m03, matrix.m10, matrix.m11, matrix.m12, matrix.m13, matrix.m20, matrix.m21, matrix.m22, matrix.m23, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }
    
    public void upload() {
        if (!this.dirty) {}
        this.dirty = false;
        if (this.uniformType <= 3) {
            this.uploadInt();
        }
        else if (this.uniformType <= 7) {
            this.uploadFloat();
        }
        else {
            if (this.uniformType > 10) {
                ShaderUniform.LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", (Object)this.uniformType);
                return;
            }
            this.uploadFloatMatrix();
        }
    }
    
    private void uploadInt() {
        switch (this.uniformType) {
            case 0: {
                OpenGlHelper.glUniform1(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 1: {
                OpenGlHelper.glUniform2(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 2: {
                OpenGlHelper.glUniform3(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 3: {
                OpenGlHelper.glUniform4(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            default: {
                ShaderUniform.LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", (Object)this.uniformCount);
                break;
            }
        }
    }
    
    private void uploadFloat() {
        switch (this.uniformType) {
            case 4: {
                OpenGlHelper.glUniform1(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 5: {
                OpenGlHelper.glUniform2(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 6: {
                OpenGlHelper.glUniform3(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 7: {
                OpenGlHelper.glUniform4(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            default: {
                ShaderUniform.LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", (Object)this.uniformCount);
                break;
            }
        }
    }
    
    private void uploadFloatMatrix() {
        switch (this.uniformType) {
            case 8: {
                OpenGlHelper.glUniformMatrix2(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
            case 9: {
                OpenGlHelper.glUniformMatrix3(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
            case 10: {
                OpenGlHelper.glUniformMatrix4(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
