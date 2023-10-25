/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Matrix4f
 */
package net.minecraft.util;

public class Matrix4f
extends org.lwjgl.util.vector.Matrix4f {
    public Matrix4f() {
        this.m33 = 0.0f;
        this.m32 = 0.0f;
        this.m31 = 0.0f;
        this.m30 = 0.0f;
        this.m23 = 0.0f;
        this.m22 = 0.0f;
        this.m21 = 0.0f;
        this.m20 = 0.0f;
        this.m13 = 0.0f;
        this.m12 = 0.0f;
        this.m11 = 0.0f;
        this.m10 = 0.0f;
        this.m03 = 0.0f;
        this.m02 = 0.0f;
        this.m01 = 0.0f;
        this.m00 = 0.0f;
    }

    public Matrix4f(float[] fArray) {
        this.m00 = fArray[0];
        this.m01 = fArray[1];
        this.m02 = fArray[2];
        this.m03 = fArray[3];
        this.m10 = fArray[4];
        this.m11 = fArray[5];
        this.m12 = fArray[6];
        this.m13 = fArray[7];
        this.m20 = fArray[8];
        this.m21 = fArray[9];
        this.m22 = fArray[10];
        this.m23 = fArray[11];
        this.m30 = fArray[12];
        this.m31 = fArray[13];
        this.m32 = fArray[14];
        this.m33 = fArray[15];
    }
}

