/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.util.vector.Matrix4f
 */
package net.minecraft.client.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUniform {
    private final ShaderManager shaderManager;
    private final int uniformType;
    private int uniformLocation;
    private static final Logger logger = LogManager.getLogger();
    private final int uniformCount;
    private boolean dirty;
    private final FloatBuffer uniformFloatBuffer;
    private final String shaderName;
    private final IntBuffer uniformIntBuffer;

    public String getShaderName() {
        return this.shaderName;
    }

    public void setUniformLocation(int n) {
        this.uniformLocation = n;
    }

    public ShaderUniform(String string, int n, int n2, ShaderManager shaderManager) {
        this.shaderName = string;
        this.uniformCount = n2;
        this.uniformType = n;
        this.shaderManager = shaderManager;
        if (n <= 3) {
            this.uniformIntBuffer = BufferUtils.createIntBuffer((int)n2);
            this.uniformFloatBuffer = null;
        } else {
            this.uniformIntBuffer = null;
            this.uniformFloatBuffer = BufferUtils.createFloatBuffer((int)n2);
        }
        this.uniformLocation = -1;
        this.markDirty();
    }

    public void set(float f) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, f);
        this.markDirty();
    }

    private void markDirty() {
        this.dirty = true;
        if (this.shaderManager != null) {
            this.shaderManager.markDirty();
        }
    }

    public static int parseType(String string) {
        int n = -1;
        if (string.equals("int")) {
            n = 0;
        } else if (string.equals("float")) {
            n = 4;
        } else if (string.startsWith("matrix")) {
            if (string.endsWith("2x2")) {
                n = 8;
            } else if (string.endsWith("3x3")) {
                n = 9;
            } else if (string.endsWith("4x4")) {
                n = 10;
            }
        }
        return n;
    }

    public void set(float[] fArray) {
        if (fArray.length < this.uniformCount) {
            logger.warn("Uniform.set called with a too-small value array (expected " + this.uniformCount + ", got " + fArray.length + "). Ignoring.");
        } else {
            this.uniformFloatBuffer.position(0);
            this.uniformFloatBuffer.put(fArray);
            this.uniformFloatBuffer.position(0);
            this.markDirty();
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
            }
        }
    }

    public void set(float f, float f2, float f3) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, f);
        this.uniformFloatBuffer.put(1, f2);
        this.uniformFloatBuffer.put(2, f3);
        this.markDirty();
    }

    public void set(float f, float f2, float f3, float f4) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(f);
        this.uniformFloatBuffer.put(f2);
        this.uniformFloatBuffer.put(f3);
        this.uniformFloatBuffer.put(f4);
        this.uniformFloatBuffer.flip();
        this.markDirty();
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
                logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + "not in the range of 1 to 4. Ignoring.");
            }
        }
    }

    public void func_148092_b(float f, float f2, float f3, float f4) {
        this.uniformFloatBuffer.position(0);
        if (this.uniformType >= 4) {
            this.uniformFloatBuffer.put(0, f);
        }
        if (this.uniformType >= 5) {
            this.uniformFloatBuffer.put(1, f2);
        }
        if (this.uniformType >= 6) {
            this.uniformFloatBuffer.put(2, f3);
        }
        if (this.uniformType >= 7) {
            this.uniformFloatBuffer.put(3, f4);
        }
        this.markDirty();
    }

    public void upload() {
        if (!this.dirty) {
            // empty if block
        }
        this.dirty = false;
        if (this.uniformType <= 3) {
            this.uploadInt();
        } else if (this.uniformType <= 7) {
            this.uploadFloat();
        } else {
            if (this.uniformType > 10) {
                logger.warn("Uniform.upload called, but type value (" + this.uniformType + ") is not " + "a valid type. Ignoring.");
                return;
            }
            this.uploadFloatMatrix();
        }
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, f);
        this.uniformFloatBuffer.put(1, f2);
        this.uniformFloatBuffer.put(2, f3);
        this.uniformFloatBuffer.put(3, f4);
        this.uniformFloatBuffer.put(4, f5);
        this.uniformFloatBuffer.put(5, f6);
        this.uniformFloatBuffer.put(6, f7);
        this.uniformFloatBuffer.put(7, f8);
        this.uniformFloatBuffer.put(8, f9);
        this.uniformFloatBuffer.put(9, f10);
        this.uniformFloatBuffer.put(10, f11);
        this.uniformFloatBuffer.put(11, f12);
        this.uniformFloatBuffer.put(12, f13);
        this.uniformFloatBuffer.put(13, f14);
        this.uniformFloatBuffer.put(14, f15);
        this.uniformFloatBuffer.put(15, f16);
        this.markDirty();
    }

    public void set(Matrix4f matrix4f) {
        this.set(matrix4f.m00, matrix4f.m01, matrix4f.m02, matrix4f.m03, matrix4f.m10, matrix4f.m11, matrix4f.m12, matrix4f.m13, matrix4f.m20, matrix4f.m21, matrix4f.m22, matrix4f.m23, matrix4f.m30, matrix4f.m31, matrix4f.m32, matrix4f.m33);
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
                logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + " not in the range of 1 to 4. Ignoring.");
            }
        }
    }

    public void set(float f, float f2) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, f);
        this.uniformFloatBuffer.put(1, f2);
        this.markDirty();
    }

    public void set(int n, int n2, int n3, int n4) {
        this.uniformIntBuffer.position(0);
        if (this.uniformType >= 0) {
            this.uniformIntBuffer.put(0, n);
        }
        if (this.uniformType >= 1) {
            this.uniformIntBuffer.put(1, n2);
        }
        if (this.uniformType >= 2) {
            this.uniformIntBuffer.put(2, n3);
        }
        if (this.uniformType >= 3) {
            this.uniformIntBuffer.put(3, n4);
        }
        this.markDirty();
    }
}

