/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.culling;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.util.MathHelper;

public class ClippingHelperImpl
extends ClippingHelper {
    private FloatBuffer modelviewMatrixBuffer;
    private FloatBuffer projectionMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
    private FloatBuffer field_78564_h;
    private static ClippingHelperImpl instance = new ClippingHelperImpl();

    public void init() {
        this.projectionMatrixBuffer.clear();
        this.modelviewMatrixBuffer.clear();
        this.field_78564_h.clear();
        GlStateManager.getFloat(2983, this.projectionMatrixBuffer);
        GlStateManager.getFloat(2982, this.modelviewMatrixBuffer);
        float[] fArray = this.projectionMatrix;
        float[] fArray2 = this.modelviewMatrix;
        this.projectionMatrixBuffer.flip().limit(16);
        this.projectionMatrixBuffer.get(fArray);
        this.modelviewMatrixBuffer.flip().limit(16);
        this.modelviewMatrixBuffer.get(fArray2);
        this.clippingMatrix[0] = fArray2[0] * fArray[0] + fArray2[1] * fArray[4] + fArray2[2] * fArray[8] + fArray2[3] * fArray[12];
        this.clippingMatrix[1] = fArray2[0] * fArray[1] + fArray2[1] * fArray[5] + fArray2[2] * fArray[9] + fArray2[3] * fArray[13];
        this.clippingMatrix[2] = fArray2[0] * fArray[2] + fArray2[1] * fArray[6] + fArray2[2] * fArray[10] + fArray2[3] * fArray[14];
        this.clippingMatrix[3] = fArray2[0] * fArray[3] + fArray2[1] * fArray[7] + fArray2[2] * fArray[11] + fArray2[3] * fArray[15];
        this.clippingMatrix[4] = fArray2[4] * fArray[0] + fArray2[5] * fArray[4] + fArray2[6] * fArray[8] + fArray2[7] * fArray[12];
        this.clippingMatrix[5] = fArray2[4] * fArray[1] + fArray2[5] * fArray[5] + fArray2[6] * fArray[9] + fArray2[7] * fArray[13];
        this.clippingMatrix[6] = fArray2[4] * fArray[2] + fArray2[5] * fArray[6] + fArray2[6] * fArray[10] + fArray2[7] * fArray[14];
        this.clippingMatrix[7] = fArray2[4] * fArray[3] + fArray2[5] * fArray[7] + fArray2[6] * fArray[11] + fArray2[7] * fArray[15];
        this.clippingMatrix[8] = fArray2[8] * fArray[0] + fArray2[9] * fArray[4] + fArray2[10] * fArray[8] + fArray2[11] * fArray[12];
        this.clippingMatrix[9] = fArray2[8] * fArray[1] + fArray2[9] * fArray[5] + fArray2[10] * fArray[9] + fArray2[11] * fArray[13];
        this.clippingMatrix[10] = fArray2[8] * fArray[2] + fArray2[9] * fArray[6] + fArray2[10] * fArray[10] + fArray2[11] * fArray[14];
        this.clippingMatrix[11] = fArray2[8] * fArray[3] + fArray2[9] * fArray[7] + fArray2[10] * fArray[11] + fArray2[11] * fArray[15];
        this.clippingMatrix[12] = fArray2[12] * fArray[0] + fArray2[13] * fArray[4] + fArray2[14] * fArray[8] + fArray2[15] * fArray[12];
        this.clippingMatrix[13] = fArray2[12] * fArray[1] + fArray2[13] * fArray[5] + fArray2[14] * fArray[9] + fArray2[15] * fArray[13];
        this.clippingMatrix[14] = fArray2[12] * fArray[2] + fArray2[13] * fArray[6] + fArray2[14] * fArray[10] + fArray2[15] * fArray[14];
        this.clippingMatrix[15] = fArray2[12] * fArray[3] + fArray2[13] * fArray[7] + fArray2[14] * fArray[11] + fArray2[15] * fArray[15];
        float[] fArray3 = this.frustum[0];
        fArray3[0] = this.clippingMatrix[3] - this.clippingMatrix[0];
        fArray3[1] = this.clippingMatrix[7] - this.clippingMatrix[4];
        fArray3[2] = this.clippingMatrix[11] - this.clippingMatrix[8];
        fArray3[3] = this.clippingMatrix[15] - this.clippingMatrix[12];
        this.normalize(fArray3);
        float[] fArray4 = this.frustum[1];
        fArray4[0] = this.clippingMatrix[3] + this.clippingMatrix[0];
        fArray4[1] = this.clippingMatrix[7] + this.clippingMatrix[4];
        fArray4[2] = this.clippingMatrix[11] + this.clippingMatrix[8];
        fArray4[3] = this.clippingMatrix[15] + this.clippingMatrix[12];
        this.normalize(fArray4);
        float[] fArray5 = this.frustum[2];
        fArray5[0] = this.clippingMatrix[3] + this.clippingMatrix[1];
        fArray5[1] = this.clippingMatrix[7] + this.clippingMatrix[5];
        fArray5[2] = this.clippingMatrix[11] + this.clippingMatrix[9];
        fArray5[3] = this.clippingMatrix[15] + this.clippingMatrix[13];
        this.normalize(fArray5);
        float[] fArray6 = this.frustum[3];
        fArray6[0] = this.clippingMatrix[3] - this.clippingMatrix[1];
        fArray6[1] = this.clippingMatrix[7] - this.clippingMatrix[5];
        fArray6[2] = this.clippingMatrix[11] - this.clippingMatrix[9];
        fArray6[3] = this.clippingMatrix[15] - this.clippingMatrix[13];
        this.normalize(fArray6);
        float[] fArray7 = this.frustum[4];
        fArray7[0] = this.clippingMatrix[3] - this.clippingMatrix[2];
        fArray7[1] = this.clippingMatrix[7] - this.clippingMatrix[6];
        fArray7[2] = this.clippingMatrix[11] - this.clippingMatrix[10];
        fArray7[3] = this.clippingMatrix[15] - this.clippingMatrix[14];
        this.normalize(fArray7);
        float[] fArray8 = this.frustum[5];
        fArray8[0] = this.clippingMatrix[3] + this.clippingMatrix[2];
        fArray8[1] = this.clippingMatrix[7] + this.clippingMatrix[6];
        fArray8[2] = this.clippingMatrix[11] + this.clippingMatrix[10];
        fArray8[3] = this.clippingMatrix[15] + this.clippingMatrix[14];
        this.normalize(fArray8);
    }

    public static ClippingHelper getInstance() {
        instance.init();
        return instance;
    }

    private void normalize(float[] fArray) {
        float f = MathHelper.sqrt_float(fArray[0] * fArray[0] + fArray[1] * fArray[1] + fArray[2] * fArray[2]);
        fArray[0] = fArray[0] / f;
        fArray[1] = fArray[1] / f;
        fArray[2] = fArray[2] / f;
        fArray[3] = fArray[3] / f;
    }

    public ClippingHelperImpl() {
        this.modelviewMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.field_78564_h = GLAllocation.createDirectFloatBuffer(16);
    }
}

