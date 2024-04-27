package me.betterfps;

public class LibGDXMath {
    public static final float BF_PI = (float)Math.PI;
    private static final int BF_SIN_BITS = 14;
    private static final int BF_SIN_MASK = 16383;
    private static final int BF_SIN_COUNT = 16384;
    private static final float BF_radFull = (float)Math.PI * 2;
    private static final float BF_degFull = 360.0f;
    private static final float BF_radToIndex = 2607.5945f;
    private static final float BF_degToIndex = 45.511112f;
    public static final float BF_degreesToRadians = (float)Math.PI / 180;
    private static final float[] BF_table;

    public float sin(float radians) {
        return BF_table[(int)(radians * 2607.5945f) & 0x3FFF];
    }

    public float cos(float radians) {
        return BF_table[(int)((radians + 1.5707964f) * 2607.5945f) & 0x3FFF];
    }

    static {
        int i;
        BF_table = new float[16384];
        for (i = 0; i < 16384; ++i) {
            LibGDXMath.BF_table[i] = (float)Math.sin(((float)i + 0.5f) / 16384.0f * ((float)Math.PI * 2));
        }
        for (i = 0; i < 360; i += 90) {
            LibGDXMath.BF_table[(int)((float)i * 45.511112f) & 0x3FFF] = (float)Math.sin((float)i * ((float)Math.PI / 180));
        }
    }
}
