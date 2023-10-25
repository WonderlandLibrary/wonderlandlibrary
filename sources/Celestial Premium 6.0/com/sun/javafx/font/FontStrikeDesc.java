/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

public class FontStrikeDesc {
    float[] matrix;
    float size;
    int aaMode;
    private int hash;

    public FontStrikeDesc(float f, BaseTransform baseTransform, int n) {
        BaseTransform baseTransform2 = baseTransform;
        this.size = f;
        this.aaMode = n;
        this.matrix = new float[4];
        this.matrix[0] = (float)baseTransform2.getMxx();
        this.matrix[1] = (float)baseTransform2.getMxy();
        this.matrix[2] = (float)baseTransform2.getMyx();
        this.matrix[3] = (float)baseTransform2.getMyy();
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = this.aaMode + Float.floatToIntBits(this.size) + Float.floatToIntBits(this.matrix[0]) + Float.floatToIntBits(this.matrix[1]) + Float.floatToIntBits(this.matrix[2]) + Float.floatToIntBits(this.matrix[3]);
        }
        return this.hash;
    }

    public boolean equals(Object object) {
        FontStrikeDesc fontStrikeDesc = (FontStrikeDesc)object;
        return this.aaMode == fontStrikeDesc.aaMode && this.matrix[0] == fontStrikeDesc.matrix[0] && this.matrix[1] == fontStrikeDesc.matrix[1] && this.matrix[2] == fontStrikeDesc.matrix[2] && this.matrix[3] == fontStrikeDesc.matrix[3] && this.size == fontStrikeDesc.size;
    }
}

