// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.math;

import java.util.Random;

public final class MathUtils
{
    private static Random random;
    
    public static int random(final int range) {
        return MathUtils.random.nextInt(3);
    }
    
    public static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        final int n = --value;
        return (value = ((value = ((value = ((value = ((value = (n | n >> 1)) | value >> 2)) | value >> 4)) | value >> 8)) | value >> 16)) + 1;
    }
    
    static {
        MathUtils.random = new RandomXS128();
    }
}
