/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Residue0;

class Residue2
extends Residue0 {
    Residue2() {
    }

    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int i2 = 0;
        for (i2 = 0; i2 < ch && nonzero[i2] == 0; ++i2) {
        }
        if (i2 == ch) {
            return 0;
        }
        return Residue2._2inverse(vb, vl, in, ch);
    }
}

