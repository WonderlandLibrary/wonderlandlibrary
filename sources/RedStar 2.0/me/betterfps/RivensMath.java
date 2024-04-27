package me.betterfps;

public class RivensMath {
    private static final int BF_SIN_BITS;
    private static final int BF_SIN_MASK;
    private static final int BF_SIN_COUNT;
    private static final float BF_radFull;
    private static final float BF_radToIndex;
    private static final float BF_degFull;
    private static final float BF_degToIndex;
    private static final float[] BF_sin;
    private static final float[] BF_cos;

    public float sin(float rad) {
        return BF_sin[(int)(rad * BF_radToIndex) & BF_SIN_MASK];
    }

    public float cos(float rad) {
        return BF_cos[(int)(rad * BF_radToIndex) & BF_SIN_MASK];
    }

    static {
        int i;
        BF_SIN_BITS = 12;
        BF_SIN_MASK = ~(-1 << BF_SIN_BITS);
        BF_SIN_COUNT = BF_SIN_MASK + 1;
        BF_radFull = (float)Math.PI * 2;
        BF_degFull = 360.0f;
        BF_radToIndex = (float)BF_SIN_COUNT / BF_radFull;
        BF_degToIndex = (float)BF_SIN_COUNT / BF_degFull;
        BF_sin = new float[BF_SIN_COUNT];
        BF_cos = new float[BF_SIN_COUNT];
        for (i = 0; i < BF_SIN_COUNT; ++i) {
            RivensMath.BF_sin[i] = (float)Math.sin(((float)i + 0.5f) / (float)BF_SIN_COUNT * BF_radFull);
            RivensMath.BF_cos[i] = (float)Math.cos(((float)i + 0.5f) / (float)BF_SIN_COUNT * BF_radFull);
        }
        for (i = 0; i < 360; i += 90) {
            RivensMath.BF_sin[(int)((float)i * RivensMath.BF_degToIndex) & RivensMath.BF_SIN_MASK] = (float)Math.sin((double)i * Math.PI / 180.0);
            RivensMath.BF_cos[(int)((float)i * RivensMath.BF_degToIndex) & RivensMath.BF_SIN_MASK] = (float)Math.cos((double)i * Math.PI / 180.0);
        }
    }
}
