/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;

public class LinearColormap
implements Colormap {
    private int color1;
    private int color2;

    public LinearColormap() {
        this(-16777216, -1);
    }

    public LinearColormap(int color1, int color2) {
        this.color1 = color1;
        this.color2 = color2;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor1() {
        return this.color1;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor2() {
        return this.color2;
    }

    @Override
    public int getColor(float v) {
        return ImageMath.mixColors(ImageMath.clamp(v, 0.0f, 1.0f), this.color1, this.color2);
    }
}

