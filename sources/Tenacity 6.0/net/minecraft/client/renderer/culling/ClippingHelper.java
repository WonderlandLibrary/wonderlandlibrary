// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.culling;

public class ClippingHelper
{
    public float[][] frustum;
    public float[] projectionMatrix;
    public float[] modelviewMatrix;
    public float[] clippingMatrix;
    public boolean disabled;
    
    public ClippingHelper() {
        this.frustum = new float[6][4];
        this.projectionMatrix = new float[16];
        this.modelviewMatrix = new float[16];
        this.clippingMatrix = new float[16];
        this.disabled = false;
    }
    
    private float dot(final float[] p_dot_1_, final float p_dot_2_, final float p_dot_3_, final float p_dot_4_) {
        return p_dot_1_[0] * p_dot_2_ + p_dot_1_[1] * p_dot_3_ + p_dot_1_[2] * p_dot_4_ + p_dot_1_[3];
    }
    
    public boolean isBoxInFrustum(final double p_78553_1_, final double p_78553_3_, final double p_78553_5_, final double p_78553_7_, final double p_78553_9_, final double p_78553_11_) {
        if (this.disabled) {
            return true;
        }
        final float f = (float)p_78553_1_;
        final float f2 = (float)p_78553_3_;
        final float f3 = (float)p_78553_5_;
        final float f4 = (float)p_78553_7_;
        final float f5 = (float)p_78553_9_;
        final float f6 = (float)p_78553_11_;
        for (int i = 0; i < 6; ++i) {
            final float[] afloat = this.frustum[i];
            final float f7 = afloat[0];
            final float f8 = afloat[1];
            final float f9 = afloat[2];
            final float f10 = afloat[3];
            if (f7 * f + f8 * f2 + f9 * f3 + f10 <= 0.0f && f7 * f4 + f8 * f2 + f9 * f3 + f10 <= 0.0f && f7 * f + f8 * f5 + f9 * f3 + f10 <= 0.0f && f7 * f4 + f8 * f5 + f9 * f3 + f10 <= 0.0f && f7 * f + f8 * f2 + f9 * f6 + f10 <= 0.0f && f7 * f4 + f8 * f2 + f9 * f6 + f10 <= 0.0f && f7 * f + f8 * f5 + f9 * f6 + f10 <= 0.0f && f7 * f4 + f8 * f5 + f9 * f6 + f10 <= 0.0f) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isBoxInFrustumFully(final double p_isBoxInFrustumFully_1_, final double p_isBoxInFrustumFully_3_, final double p_isBoxInFrustumFully_5_, final double p_isBoxInFrustumFully_7_, final double p_isBoxInFrustumFully_9_, final double p_isBoxInFrustumFully_11_) {
        if (this.disabled) {
            return true;
        }
        final float f = (float)p_isBoxInFrustumFully_1_;
        final float f2 = (float)p_isBoxInFrustumFully_3_;
        final float f3 = (float)p_isBoxInFrustumFully_5_;
        final float f4 = (float)p_isBoxInFrustumFully_7_;
        final float f5 = (float)p_isBoxInFrustumFully_9_;
        final float f6 = (float)p_isBoxInFrustumFully_11_;
        for (int i = 0; i < 6; ++i) {
            final float[] afloat = this.frustum[i];
            final float f7 = afloat[0];
            final float f8 = afloat[1];
            final float f9 = afloat[2];
            final float f10 = afloat[3];
            if (i < 4) {
                if (f7 * f + f8 * f2 + f9 * f3 + f10 <= 0.0f || f7 * f4 + f8 * f2 + f9 * f3 + f10 <= 0.0f || f7 * f + f8 * f5 + f9 * f3 + f10 <= 0.0f || f7 * f4 + f8 * f5 + f9 * f3 + f10 <= 0.0f || f7 * f + f8 * f2 + f9 * f6 + f10 <= 0.0f || f7 * f4 + f8 * f2 + f9 * f6 + f10 <= 0.0f || f7 * f + f8 * f5 + f9 * f6 + f10 <= 0.0f || f7 * f4 + f8 * f5 + f9 * f6 + f10 <= 0.0f) {
                    return false;
                }
            }
            else if (f7 * f + f8 * f2 + f9 * f3 + f10 <= 0.0f && f7 * f4 + f8 * f2 + f9 * f3 + f10 <= 0.0f && f7 * f + f8 * f5 + f9 * f3 + f10 <= 0.0f && f7 * f4 + f8 * f5 + f9 * f3 + f10 <= 0.0f && f7 * f + f8 * f2 + f9 * f6 + f10 <= 0.0f && f7 * f4 + f8 * f2 + f9 * f6 + f10 <= 0.0f && f7 * f + f8 * f5 + f9 * f6 + f10 <= 0.0f && f7 * f4 + f8 * f5 + f9 * f6 + f10 <= 0.0f) {
                return false;
            }
        }
        return true;
    }
}
