// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.culling;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.FloatBuffer;

public class ClippingHelperImpl extends ClippingHelper
{
    private static ClippingHelperImpl instance;
    private FloatBuffer projectionMatrixBuffer;
    private FloatBuffer modelviewMatrixBuffer;
    private FloatBuffer field_78564_h;
    private static final String __OBFID = "CL_00000975";
    
    public ClippingHelperImpl() {
        this.projectionMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.modelviewMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.field_78564_h = GLAllocation.createDirectFloatBuffer(16);
    }
    
    public static ClippingHelper getInstance() {
        ClippingHelperImpl.instance.init();
        return ClippingHelperImpl.instance;
    }
    
    private void func_180547_a(final float[] p_180547_1_) {
        final float var2 = MathHelper.sqrt_float(p_180547_1_[0] * p_180547_1_[0] + p_180547_1_[1] * p_180547_1_[1] + p_180547_1_[2] * p_180547_1_[2]);
        final int n = 0;
        p_180547_1_[n] /= var2;
        final int n2 = 1;
        p_180547_1_[n2] /= var2;
        final int n3 = 2;
        p_180547_1_[n3] /= var2;
        final int n4 = 3;
        p_180547_1_[n4] /= var2;
    }
    
    public void init() {
        this.projectionMatrixBuffer.clear();
        this.modelviewMatrixBuffer.clear();
        this.field_78564_h.clear();
        GlStateManager.getFloat(2983, this.projectionMatrixBuffer);
        GlStateManager.getFloat(2982, this.modelviewMatrixBuffer);
        final float[] var1 = this.field_178625_b;
        final float[] var2 = this.field_178626_c;
        this.projectionMatrixBuffer.flip().limit(16);
        this.projectionMatrixBuffer.get(var1);
        this.modelviewMatrixBuffer.flip().limit(16);
        this.modelviewMatrixBuffer.get(var2);
        this.clippingMatrix[0] = var2[0] * var1[0] + var2[1] * var1[4] + var2[2] * var1[8] + var2[3] * var1[12];
        this.clippingMatrix[1] = var2[0] * var1[1] + var2[1] * var1[5] + var2[2] * var1[9] + var2[3] * var1[13];
        this.clippingMatrix[2] = var2[0] * var1[2] + var2[1] * var1[6] + var2[2] * var1[10] + var2[3] * var1[14];
        this.clippingMatrix[3] = var2[0] * var1[3] + var2[1] * var1[7] + var2[2] * var1[11] + var2[3] * var1[15];
        this.clippingMatrix[4] = var2[4] * var1[0] + var2[5] * var1[4] + var2[6] * var1[8] + var2[7] * var1[12];
        this.clippingMatrix[5] = var2[4] * var1[1] + var2[5] * var1[5] + var2[6] * var1[9] + var2[7] * var1[13];
        this.clippingMatrix[6] = var2[4] * var1[2] + var2[5] * var1[6] + var2[6] * var1[10] + var2[7] * var1[14];
        this.clippingMatrix[7] = var2[4] * var1[3] + var2[5] * var1[7] + var2[6] * var1[11] + var2[7] * var1[15];
        this.clippingMatrix[8] = var2[8] * var1[0] + var2[9] * var1[4] + var2[10] * var1[8] + var2[11] * var1[12];
        this.clippingMatrix[9] = var2[8] * var1[1] + var2[9] * var1[5] + var2[10] * var1[9] + var2[11] * var1[13];
        this.clippingMatrix[10] = var2[8] * var1[2] + var2[9] * var1[6] + var2[10] * var1[10] + var2[11] * var1[14];
        this.clippingMatrix[11] = var2[8] * var1[3] + var2[9] * var1[7] + var2[10] * var1[11] + var2[11] * var1[15];
        this.clippingMatrix[12] = var2[12] * var1[0] + var2[13] * var1[4] + var2[14] * var1[8] + var2[15] * var1[12];
        this.clippingMatrix[13] = var2[12] * var1[1] + var2[13] * var1[5] + var2[14] * var1[9] + var2[15] * var1[13];
        this.clippingMatrix[14] = var2[12] * var1[2] + var2[13] * var1[6] + var2[14] * var1[10] + var2[15] * var1[14];
        this.clippingMatrix[15] = var2[12] * var1[3] + var2[13] * var1[7] + var2[14] * var1[11] + var2[15] * var1[15];
        final float[] var3 = this.frustum[0];
        var3[0] = this.clippingMatrix[3] - this.clippingMatrix[0];
        var3[1] = this.clippingMatrix[7] - this.clippingMatrix[4];
        var3[2] = this.clippingMatrix[11] - this.clippingMatrix[8];
        var3[3] = this.clippingMatrix[15] - this.clippingMatrix[12];
        this.func_180547_a(var3);
        final float[] var4 = this.frustum[1];
        var4[0] = this.clippingMatrix[3] + this.clippingMatrix[0];
        var4[1] = this.clippingMatrix[7] + this.clippingMatrix[4];
        var4[2] = this.clippingMatrix[11] + this.clippingMatrix[8];
        var4[3] = this.clippingMatrix[15] + this.clippingMatrix[12];
        this.func_180547_a(var4);
        final float[] var5 = this.frustum[2];
        var5[0] = this.clippingMatrix[3] + this.clippingMatrix[1];
        var5[1] = this.clippingMatrix[7] + this.clippingMatrix[5];
        var5[2] = this.clippingMatrix[11] + this.clippingMatrix[9];
        var5[3] = this.clippingMatrix[15] + this.clippingMatrix[13];
        this.func_180547_a(var5);
        final float[] var6 = this.frustum[3];
        var6[0] = this.clippingMatrix[3] - this.clippingMatrix[1];
        var6[1] = this.clippingMatrix[7] - this.clippingMatrix[5];
        var6[2] = this.clippingMatrix[11] - this.clippingMatrix[9];
        var6[3] = this.clippingMatrix[15] - this.clippingMatrix[13];
        this.func_180547_a(var6);
        final float[] var7 = this.frustum[4];
        var7[0] = this.clippingMatrix[3] - this.clippingMatrix[2];
        var7[1] = this.clippingMatrix[7] - this.clippingMatrix[6];
        var7[2] = this.clippingMatrix[11] - this.clippingMatrix[10];
        var7[3] = this.clippingMatrix[15] - this.clippingMatrix[14];
        this.func_180547_a(var7);
        final float[] var8 = this.frustum[5];
        var8[0] = this.clippingMatrix[3] + this.clippingMatrix[2];
        var8[1] = this.clippingMatrix[7] + this.clippingMatrix[6];
        var8[2] = this.clippingMatrix[11] + this.clippingMatrix[10];
        var8[3] = this.clippingMatrix[15] + this.clippingMatrix[14];
        this.func_180547_a(var8);
    }
    
    static {
        ClippingHelperImpl.instance = new ClippingHelperImpl();
    }
}
