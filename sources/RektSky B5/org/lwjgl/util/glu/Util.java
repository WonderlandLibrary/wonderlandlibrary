/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Util {
    private static IntBuffer scratch = BufferUtils.createIntBuffer(16);

    protected static int ceil(int a2, int b2) {
        return a2 % b2 == 0 ? a2 / b2 : a2 / b2 + 1;
    }

    protected static float[] normalize(float[] v2) {
        float r2 = (float)Math.sqrt(v2[0] * v2[0] + v2[1] * v2[1] + v2[2] * v2[2]);
        if ((double)r2 == 0.0) {
            return v2;
        }
        r2 = 1.0f / r2;
        v2[0] = v2[0] * r2;
        v2[1] = v2[1] * r2;
        v2[2] = v2[2] * r2;
        return v2;
    }

    protected static void cross(float[] v1, float[] v2, float[] result) {
        result[0] = v1[1] * v2[2] - v1[2] * v2[1];
        result[1] = v1[2] * v2[0] - v1[0] * v2[2];
        result[2] = v1[0] * v2[1] - v1[1] * v2[0];
    }

    protected static int compPerPix(int format) {
        switch (format) {
            case 6400: 
            case 6401: 
            case 6402: 
            case 6403: 
            case 6404: 
            case 6405: 
            case 6406: 
            case 6409: {
                return 1;
            }
            case 6410: {
                return 2;
            }
            case 6407: 
            case 32992: {
                return 3;
            }
            case 6408: 
            case 32993: {
                return 4;
            }
        }
        return -1;
    }

    protected static int nearestPower(int value) {
        int i2 = 1;
        if (value == 0) {
            return -1;
        }
        while (value != 1) {
            if (value == 3) {
                return i2 << 2;
            }
            value >>= 1;
            i2 <<= 1;
        }
        return i2;
    }

    protected static int bytesPerPixel(int format, int type) {
        int m2;
        int n2;
        switch (format) {
            case 6400: 
            case 6401: 
            case 6402: 
            case 6403: 
            case 6404: 
            case 6405: 
            case 6406: 
            case 6409: {
                n2 = 1;
                break;
            }
            case 6410: {
                n2 = 2;
                break;
            }
            case 6407: 
            case 32992: {
                n2 = 3;
                break;
            }
            case 6408: 
            case 32993: {
                n2 = 4;
                break;
            }
            default: {
                n2 = 0;
            }
        }
        switch (type) {
            case 5121: {
                m2 = 1;
                break;
            }
            case 5120: {
                m2 = 1;
                break;
            }
            case 6656: {
                m2 = 1;
                break;
            }
            case 5123: {
                m2 = 2;
                break;
            }
            case 5122: {
                m2 = 2;
                break;
            }
            case 5125: {
                m2 = 4;
                break;
            }
            case 5124: {
                m2 = 4;
                break;
            }
            case 5126: {
                m2 = 4;
                break;
            }
            default: {
                m2 = 0;
            }
        }
        return n2 * m2;
    }

    protected static int glGetIntegerv(int what) {
        scratch.rewind();
        GL11.glGetInteger(what, scratch);
        return scratch.get();
    }
}

