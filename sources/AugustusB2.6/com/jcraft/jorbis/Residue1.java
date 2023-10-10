// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Residue1 extends Residue0
{
    int forward(final Block block, final Object o, final float[][] array, final int n) {
        System.err.println("Residue0.forward: not implemented");
        return 0;
    }
    
    int inverse(final Block block, final Object o, final float[][] array, final int[] array2, final int n) {
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (array2[i] != 0) {
                array[n2++] = array[i];
            }
        }
        if (n2 != 0) {
            return Residue0._01inverse(block, o, array, n2, 1);
        }
        return 0;
    }
}
