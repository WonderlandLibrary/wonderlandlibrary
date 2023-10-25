/*
 * Decompiled with CFR 0.150.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Residue0;

class Residue2
extends Residue0 {
    Residue2() {
    }

    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int i = 0;
        for (i = 0; i < ch && nonzero[i] == 0; ++i) {
        }
        if (i == ch) {
            return 0;
        }
        return Residue2._2inverse(vb, vl, in, ch);
    }
}

