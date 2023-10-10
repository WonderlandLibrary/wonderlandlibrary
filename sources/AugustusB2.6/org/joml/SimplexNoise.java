// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class SimplexNoise
{
    private static final Vector3b[] grad3;
    private static final Vector4b[] grad4;
    private static final byte[] p;
    private static final byte[] perm;
    private static final byte[] permMod12;
    private static final float F2 = 0.36602542f;
    private static final float G2 = 0.21132487f;
    private static final float F3 = 0.33333334f;
    private static final float G3 = 0.16666667f;
    private static final float F4 = 0.309017f;
    private static final float G4 = 0.1381966f;
    
    private static int fastfloor(final float x) {
        final int xi = (int)x;
        return (x < xi) ? (xi - 1) : xi;
    }
    
    private static float dot(final Vector3b g, final float x, final float y) {
        return g.x * x + g.y * y;
    }
    
    private static float dot(final Vector3b g, final float x, final float y, final float z) {
        return g.x * x + g.y * y + g.z * z;
    }
    
    private static float dot(final Vector4b g, final float x, final float y, final float z, final float w) {
        return g.x * x + g.y * y + g.z * z + g.w * w;
    }
    
    public static float noise(final float x, final float y) {
        final float s = (x + y) * 0.36602542f;
        final int i = fastfloor(x + s);
        final int j = fastfloor(y + s);
        final float t = (i + j) * 0.21132487f;
        final float X0 = i - t;
        final float Y0 = j - t;
        final float x2 = x - X0;
        final float y2 = y - Y0;
        int i2;
        int j2;
        if (x2 > y2) {
            i2 = 1;
            j2 = 0;
        }
        else {
            i2 = 0;
            j2 = 1;
        }
        final float x3 = x2 - i2 + 0.21132487f;
        final float y3 = y2 - j2 + 0.21132487f;
        final float x4 = x2 - 1.0f + 0.42264974f;
        final float y4 = y2 - 1.0f + 0.42264974f;
        final int ii = i & 0xFF;
        final int jj = j & 0xFF;
        final int gi0 = SimplexNoise.permMod12[ii + SimplexNoise.perm[jj] & 0xFF] & 0xFF;
        final int gi2 = SimplexNoise.permMod12[ii + i2 + SimplexNoise.perm[jj + j2] & 0xFF] & 0xFF;
        final int gi3 = SimplexNoise.permMod12[ii + 1 + SimplexNoise.perm[jj + 1] & 0xFF] & 0xFF;
        float t2 = 0.5f - x2 * x2 - y2 * y2;
        float n0;
        if (t2 < 0.0f) {
            n0 = 0.0f;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad3[gi0], x2, y2);
        }
        float t3 = 0.5f - x3 * x3 - y3 * y3;
        float n2;
        if (t3 < 0.0f) {
            n2 = 0.0f;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad3[gi2], x3, y3);
        }
        float t4 = 0.5f - x4 * x4 - y4 * y4;
        float n3;
        if (t4 < 0.0f) {
            n3 = 0.0f;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad3[gi3], x4, y4);
        }
        return 70.0f * (n0 + n2 + n3);
    }
    
    public static float noise(final float x, final float y, final float z) {
        final float s = (x + y + z) * 0.33333334f;
        final int i = fastfloor(x + s);
        final int j = fastfloor(y + s);
        final int k = fastfloor(z + s);
        final float t = (i + j + k) * 0.16666667f;
        final float X0 = i - t;
        final float Y0 = j - t;
        final float Z0 = k - t;
        final float x2 = x - X0;
        final float y2 = y - Y0;
        final float z2 = z - Z0;
        int i2;
        int j2;
        int k2;
        int i3;
        int j3;
        int k3;
        if (x2 >= y2) {
            if (y2 >= z2) {
                i2 = 1;
                j2 = 0;
                k2 = 0;
                i3 = 1;
                j3 = 1;
                k3 = 0;
            }
            else if (x2 >= z2) {
                i2 = 1;
                j2 = 0;
                k2 = 0;
                i3 = 1;
                j3 = 0;
                k3 = 1;
            }
            else {
                i2 = 0;
                j2 = 0;
                k2 = 1;
                i3 = 1;
                j3 = 0;
                k3 = 1;
            }
        }
        else if (y2 < z2) {
            i2 = 0;
            j2 = 0;
            k2 = 1;
            i3 = 0;
            j3 = 1;
            k3 = 1;
        }
        else if (x2 < z2) {
            i2 = 0;
            j2 = 1;
            k2 = 0;
            i3 = 0;
            j3 = 1;
            k3 = 1;
        }
        else {
            i2 = 0;
            j2 = 1;
            k2 = 0;
            i3 = 1;
            j3 = 1;
            k3 = 0;
        }
        final float x3 = x2 - i2 + 0.16666667f;
        final float y3 = y2 - j2 + 0.16666667f;
        final float z3 = z2 - k2 + 0.16666667f;
        final float x4 = x2 - i3 + 0.33333334f;
        final float y4 = y2 - j3 + 0.33333334f;
        final float z4 = z2 - k3 + 0.33333334f;
        final float x5 = x2 - 1.0f + 0.5f;
        final float y5 = y2 - 1.0f + 0.5f;
        final float z5 = z2 - 1.0f + 0.5f;
        final int ii = i & 0xFF;
        final int jj = j & 0xFF;
        final int kk = k & 0xFF;
        final int gi0 = SimplexNoise.permMod12[ii + SimplexNoise.perm[jj + SimplexNoise.perm[kk] & 0xFF] & 0xFF] & 0xFF;
        final int gi2 = SimplexNoise.permMod12[ii + i2 + SimplexNoise.perm[jj + j2 + SimplexNoise.perm[kk + k2] & 0xFF] & 0xFF] & 0xFF;
        final int gi3 = SimplexNoise.permMod12[ii + i3 + SimplexNoise.perm[jj + j3 + SimplexNoise.perm[kk + k3] & 0xFF] & 0xFF] & 0xFF;
        final int gi4 = SimplexNoise.permMod12[ii + 1 + SimplexNoise.perm[jj + 1 + SimplexNoise.perm[kk + 1] & 0xFF] & 0xFF] & 0xFF;
        float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
        float n0;
        if (t2 < 0.0f) {
            n0 = 0.0f;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad3[gi0], x2, y2, z2);
        }
        float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
        float n2;
        if (t3 < 0.0f) {
            n2 = 0.0f;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad3[gi2], x3, y3, z3);
        }
        float t4 = 0.6f - x4 * x4 - y4 * y4 - z4 * z4;
        float n3;
        if (t4 < 0.0f) {
            n3 = 0.0f;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad3[gi3], x4, y4, z4);
        }
        float t5 = 0.6f - x5 * x5 - y5 * y5 - z5 * z5;
        float n4;
        if (t5 < 0.0f) {
            n4 = 0.0f;
        }
        else {
            t5 *= t5;
            n4 = t5 * t5 * dot(SimplexNoise.grad3[gi4], x5, y5, z5);
        }
        return 32.0f * (n0 + n2 + n3 + n4);
    }
    
    public static float noise(final float x, final float y, final float z, final float w) {
        final float s = (x + y + z + w) * 0.309017f;
        final int i = fastfloor(x + s);
        final int j = fastfloor(y + s);
        final int k = fastfloor(z + s);
        final int l = fastfloor(w + s);
        final float t = (i + j + k + l) * 0.1381966f;
        final float X0 = i - t;
        final float Y0 = j - t;
        final float Z0 = k - t;
        final float W0 = l - t;
        final float x2 = x - X0;
        final float y2 = y - Y0;
        final float z2 = z - Z0;
        final float w2 = w - W0;
        int rankx = 0;
        int ranky = 0;
        int rankz = 0;
        int rankw = 0;
        if (x2 > y2) {
            ++rankx;
        }
        else {
            ++ranky;
        }
        if (x2 > z2) {
            ++rankx;
        }
        else {
            ++rankz;
        }
        if (x2 > w2) {
            ++rankx;
        }
        else {
            ++rankw;
        }
        if (y2 > z2) {
            ++ranky;
        }
        else {
            ++rankz;
        }
        if (y2 > w2) {
            ++ranky;
        }
        else {
            ++rankw;
        }
        if (z2 > w2) {
            ++rankz;
        }
        else {
            ++rankw;
        }
        final int i2 = (rankx >= 3) ? 1 : 0;
        final int j2 = (ranky >= 3) ? 1 : 0;
        final int k2 = (rankz >= 3) ? 1 : 0;
        final int l2 = (rankw >= 3) ? 1 : 0;
        final int i3 = (rankx >= 2) ? 1 : 0;
        final int j3 = (ranky >= 2) ? 1 : 0;
        final int k3 = (rankz >= 2) ? 1 : 0;
        final int l3 = (rankw >= 2) ? 1 : 0;
        final int i4 = (rankx >= 1) ? 1 : 0;
        final int j4 = (ranky >= 1) ? 1 : 0;
        final int k4 = (rankz >= 1) ? 1 : 0;
        final int l4 = (rankw >= 1) ? 1 : 0;
        final float x3 = x2 - i2 + 0.1381966f;
        final float y3 = y2 - j2 + 0.1381966f;
        final float z3 = z2 - k2 + 0.1381966f;
        final float w3 = w2 - l2 + 0.1381966f;
        final float x4 = x2 - i3 + 0.2763932f;
        final float y4 = y2 - j3 + 0.2763932f;
        final float z4 = z2 - k3 + 0.2763932f;
        final float w4 = w2 - l3 + 0.2763932f;
        final float x5 = x2 - i4 + 0.41458982f;
        final float y5 = y2 - j4 + 0.41458982f;
        final float z5 = z2 - k4 + 0.41458982f;
        final float w5 = w2 - l4 + 0.41458982f;
        final float x6 = x2 - 1.0f + 0.5527864f;
        final float y6 = y2 - 1.0f + 0.5527864f;
        final float z6 = z2 - 1.0f + 0.5527864f;
        final float w6 = w2 - 1.0f + 0.5527864f;
        final int ii = i & 0xFF;
        final int jj = j & 0xFF;
        final int kk = k & 0xFF;
        final int ll = l & 0xFF;
        final int gi0 = (SimplexNoise.perm[ii + SimplexNoise.perm[jj + SimplexNoise.perm[kk + SimplexNoise.perm[ll] & 0xFF] & 0xFF] & 0xFF] & 0xFF) % 32;
        final int gi2 = (SimplexNoise.perm[ii + i2 + SimplexNoise.perm[jj + j2 + SimplexNoise.perm[kk + k2 + SimplexNoise.perm[ll + l2] & 0xFF] & 0xFF] & 0xFF] & 0xFF) % 32;
        final int gi3 = (SimplexNoise.perm[ii + i3 + SimplexNoise.perm[jj + j3 + SimplexNoise.perm[kk + k3 + SimplexNoise.perm[ll + l3] & 0xFF] & 0xFF] & 0xFF] & 0xFF) % 32;
        final int gi4 = (SimplexNoise.perm[ii + i4 + SimplexNoise.perm[jj + j4 + SimplexNoise.perm[kk + k4 + SimplexNoise.perm[ll + l4] & 0xFF] & 0xFF] & 0xFF] & 0xFF) % 32;
        final int gi5 = (SimplexNoise.perm[ii + 1 + SimplexNoise.perm[jj + 1 + SimplexNoise.perm[kk + 1 + SimplexNoise.perm[ll + 1] & 0xFF] & 0xFF] & 0xFF] & 0xFF) % 32;
        float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        float n0;
        if (t2 < 0.0f) {
            n0 = 0.0f;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad4[gi0], x2, y2, z2, w2);
        }
        float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        float n2;
        if (t3 < 0.0f) {
            n2 = 0.0f;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad4[gi2], x3, y3, z3, w3);
        }
        float t4 = 0.6f - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        float n3;
        if (t4 < 0.0f) {
            n3 = 0.0f;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad4[gi3], x4, y4, z4, w4);
        }
        float t5 = 0.6f - x5 * x5 - y5 * y5 - z5 * z5 - w5 * w5;
        float n4;
        if (t5 < 0.0f) {
            n4 = 0.0f;
        }
        else {
            t5 *= t5;
            n4 = t5 * t5 * dot(SimplexNoise.grad4[gi4], x5, y5, z5, w5);
        }
        float t6 = 0.6f - x6 * x6 - y6 * y6 - z6 * z6 - w6 * w6;
        float n5;
        if (t6 < 0.0f) {
            n5 = 0.0f;
        }
        else {
            t6 *= t6;
            n5 = t6 * t6 * dot(SimplexNoise.grad4[gi5], x6, y6, z6, w6);
        }
        return 27.0f * (n0 + n2 + n3 + n4 + n5);
    }
    
    static {
        grad3 = new Vector3b[] { new Vector3b(1, 1, 0), new Vector3b(-1, 1, 0), new Vector3b(1, -1, 0), new Vector3b(-1, -1, 0), new Vector3b(1, 0, 1), new Vector3b(-1, 0, 1), new Vector3b(1, 0, -1), new Vector3b(-1, 0, -1), new Vector3b(0, 1, 1), new Vector3b(0, -1, 1), new Vector3b(0, 1, -1), new Vector3b(0, -1, -1) };
        grad4 = new Vector4b[] { new Vector4b(0, 1, 1, 1), new Vector4b(0, 1, 1, -1), new Vector4b(0, 1, -1, 1), new Vector4b(0, 1, -1, -1), new Vector4b(0, -1, 1, 1), new Vector4b(0, -1, 1, -1), new Vector4b(0, -1, -1, 1), new Vector4b(0, -1, -1, -1), new Vector4b(1, 0, 1, 1), new Vector4b(1, 0, 1, -1), new Vector4b(1, 0, -1, 1), new Vector4b(1, 0, -1, -1), new Vector4b(-1, 0, 1, 1), new Vector4b(-1, 0, 1, -1), new Vector4b(-1, 0, -1, 1), new Vector4b(-1, 0, -1, -1), new Vector4b(1, 1, 0, 1), new Vector4b(1, 1, 0, -1), new Vector4b(1, -1, 0, 1), new Vector4b(1, -1, 0, -1), new Vector4b(-1, 1, 0, 1), new Vector4b(-1, 1, 0, -1), new Vector4b(-1, -1, 0, 1), new Vector4b(-1, -1, 0, -1), new Vector4b(1, 1, 1, 0), new Vector4b(1, 1, -1, 0), new Vector4b(1, -1, 1, 0), new Vector4b(1, -1, -1, 0), new Vector4b(-1, 1, 1, 0), new Vector4b(-1, 1, -1, 0), new Vector4b(-1, -1, 1, 0), new Vector4b(-1, -1, -1, 0) };
        p = new byte[] { -105, -96, -119, 91, 90, 15, -125, 13, -55, 95, 96, 53, -62, -23, 7, -31, -116, 36, 103, 30, 69, -114, 8, 99, 37, -16, 21, 10, 23, -66, 6, -108, -9, 120, -22, 75, 0, 26, -59, 62, 94, -4, -37, -53, 117, 35, 11, 32, 57, -79, 33, 88, -19, -107, 56, 87, -82, 20, 125, -120, -85, -88, 68, -81, 74, -91, 71, -122, -117, 48, 27, -90, 77, -110, -98, -25, 83, 111, -27, 122, 60, -45, -123, -26, -36, 105, 92, 41, 55, 46, -11, 40, -12, 102, -113, 54, 65, 25, 63, -95, 1, -40, 80, 73, -47, 76, -124, -69, -48, 89, 18, -87, -56, -60, -121, -126, 116, -68, -97, 86, -92, 100, 109, -58, -83, -70, 3, 64, 52, -39, -30, -6, 124, 123, 5, -54, 38, -109, 118, 126, -1, 82, 85, -44, -49, -50, 59, -29, 47, 16, 58, 17, -74, -67, 28, 42, -33, -73, -86, -43, 119, -8, -104, 2, 44, -102, -93, 70, -35, -103, 101, -101, -89, 43, -84, 9, -127, 22, 39, -3, 19, 98, 108, 110, 79, 113, -32, -24, -78, -71, 112, 104, -38, -10, 97, -28, -5, 34, -14, -63, -18, -46, -112, 12, -65, -77, -94, -15, 81, 51, -111, -21, -7, 14, -17, 107, 49, -64, -42, 31, -75, -57, 106, -99, -72, 84, -52, -80, 115, 121, 50, 45, 127, 4, -106, -2, -118, -20, -51, 93, -34, 114, 67, 29, 24, 72, -13, -115, -128, -61, 78, 66, -41, 61, -100, -76 };
        perm = new byte[512];
        permMod12 = new byte[512];
        for (int i = 0; i < 512; ++i) {
            SimplexNoise.perm[i] = SimplexNoise.p[i & 0xFF];
            SimplexNoise.permMod12[i] = (byte)((SimplexNoise.perm[i] & 0xFF) % 12);
        }
    }
    
    private static class Vector3b
    {
        byte x;
        byte y;
        byte z;
        
        Vector3b(final int x, final int y, final int z) {
            this.x = (byte)x;
            this.y = (byte)y;
            this.z = (byte)z;
        }
    }
    
    private static class Vector4b
    {
        byte x;
        byte y;
        byte z;
        byte w;
        
        Vector4b(final int x, final int y, final int z, final int w) {
            this.x = (byte)x;
            this.y = (byte)y;
            this.z = (byte)z;
            this.w = (byte)w;
        }
    }
}
