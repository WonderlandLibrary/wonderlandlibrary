package me.betterfps;

public class RivensHalfMath {
    private static final float BF_SIN_TO_COS;
    private static final int BF_SIN_BITS;
    private static final int BF_SIN_MASK;
    private static final int BF_SIN_MASK2;
    private static final int BF_SIN_COUNT;
    private static final int BF_SIN_COUNT2;
    private static final float BF_radFull;
    private static final float BF_radToIndex;
    private static final float[] BF_sinHalf;

    public float sin(float rad) {
        int index1 = (int)(rad * BF_radToIndex) & BF_SIN_MASK;
        int index2 = index1 & BF_SIN_MASK2;
        int mul = index1 == index2 ? 1 : -1;
        return BF_sinHalf[index2] * (float)mul;
    }

    public float cos(float rad) {
        return this.sin(rad + BF_SIN_TO_COS);
    }

    static {
        float[] hardcodedAngles;
        BF_SIN_TO_COS = 1.5707964f;
        BF_SIN_BITS = 12;
        BF_SIN_MASK = ~(-1 << BF_SIN_BITS);
        BF_SIN_MASK2 = BF_SIN_MASK >> 1;
        BF_SIN_COUNT = BF_SIN_MASK + 1;
        BF_SIN_COUNT2 = BF_SIN_MASK2 + 1;
        BF_radFull = (float)Math.PI * 2;
        BF_radToIndex = (float)BF_SIN_COUNT / BF_radFull;
        BF_sinHalf = new float[BF_SIN_COUNT2];
        for (int i = 0; i < BF_SIN_COUNT2; ++i) {
            RivensHalfMath.BF_sinHalf[i] = (float)Math.sin(((double)i + (double)Math.min(1, i % (BF_SIN_COUNT / 4)) * 0.5) / (double)BF_SIN_COUNT * (double)BF_radFull);
        }
        for (float angle : hardcodedAngles = new float[]{1.5707964f, 1.5707964f + BF_SIN_TO_COS}) {
            int index1 = (int)(angle * BF_radToIndex) & BF_SIN_MASK;
            int index2 = index1 & BF_SIN_MASK2;
            int mul = index1 == index2 ? 1 : -1;
            RivensHalfMath.BF_sinHalf[index2] = (float)(Math.sin(angle) / (double)mul);
        }
    }
}
