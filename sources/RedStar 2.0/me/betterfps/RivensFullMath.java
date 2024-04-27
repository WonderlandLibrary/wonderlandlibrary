package me.betterfps;

public class RivensFullMath {
    private static final float BF_SIN_TO_COS = 1.5707964f;
    private static final int BF_SIN_BITS = 12;
    private static final int BF_SIN_MASK = ~(-1 << BF_SIN_BITS);
    private static final int BF_SIN_COUNT = BF_SIN_MASK + 1;
    private static final float BF_radFull = (float)Math.PI * 2;
    private static final float BF_radToIndex = (float)BF_SIN_COUNT / BF_radFull;
    private static final float[] BF_sinFull = new float[BF_SIN_COUNT];

    public float sin(float rad) {
        return BF_sinFull[(int)(rad * BF_radToIndex) & BF_SIN_MASK];
    }

    public float cos(float rad) {
        return this.sin(rad + BF_SIN_TO_COS);
    }

    static {
        for (int i = 0; i < BF_SIN_COUNT; ++i) {
            RivensFullMath.BF_sinFull[i] = (float)Math.sin(((double)i + (double)Math.min(1, i % (BF_SIN_COUNT / 4)) * 0.5) / (double)BF_SIN_COUNT * (double)BF_radFull);
        }
    }
}
