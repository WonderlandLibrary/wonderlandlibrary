/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.culling;

public class ClippingHelper {
    public float[] projectionMatrix;
    public float[] modelviewMatrix;
    public float[] clippingMatrix;
    public float[][] frustum = new float[6][4];

    public boolean isBoxInFrustum(double d, double d2, double d3, double d4, double d5, double d6) {
        int n = 0;
        while (n < 6) {
            float[] fArray = this.frustum[n];
            if (this.dot(fArray, d, d2, d3) <= 0.0 && this.dot(fArray, d4, d2, d3) <= 0.0 && this.dot(fArray, d, d5, d3) <= 0.0 && this.dot(fArray, d4, d5, d3) <= 0.0 && this.dot(fArray, d, d2, d6) <= 0.0 && this.dot(fArray, d4, d2, d6) <= 0.0 && this.dot(fArray, d, d5, d6) <= 0.0 && this.dot(fArray, d4, d5, d6) <= 0.0) {
                return false;
            }
            ++n;
        }
        return true;
    }

    private double dot(float[] fArray, double d, double d2, double d3) {
        return (double)fArray[0] * d + (double)fArray[1] * d2 + (double)fArray[2] * d3 + (double)fArray[3];
    }

    public ClippingHelper() {
        this.projectionMatrix = new float[16];
        this.modelviewMatrix = new float[16];
        this.clippingMatrix = new float[16];
    }
}

