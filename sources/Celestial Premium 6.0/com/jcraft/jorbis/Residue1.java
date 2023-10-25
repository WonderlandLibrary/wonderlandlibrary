/*
 * Decompiled with CFR 0.150.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Residue0;

class Residue1
extends Residue0 {
    Residue1() {
    }

    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int used = 0;
        for (int i = 0; i < ch; ++i) {
            if (nonzero[i] == 0) continue;
            in[used++] = in[i];
        }
        if (used != 0) {
            return Residue1._01inverse(vb, vl, in, used, 1);
        }
        return 0;
    }
}

