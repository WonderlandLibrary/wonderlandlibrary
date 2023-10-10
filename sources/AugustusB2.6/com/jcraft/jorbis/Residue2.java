// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Residue2 extends Residue0
{
    int forward(final Block block, final Object o, final float[][] array, final int n) {
        System.err.println("Residue0.forward: not implemented");
        return 0;
    }
    
    int inverse(final Block block, final Object o, final float[][] array, final int[] array2, final int n) {
        int n2;
        for (n2 = 0; n2 < n && array2[n2] == 0; ++n2) {}
        if (n2 == n) {
            return 0;
        }
        return Residue0._2inverse(block, o, array, n);
    }
}
