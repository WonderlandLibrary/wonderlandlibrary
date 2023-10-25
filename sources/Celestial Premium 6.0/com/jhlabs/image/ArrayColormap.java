/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;

public class ArrayColormap
implements Colormap,
Cloneable {
    protected int[] map;

    public ArrayColormap() {
        this.map = new int[256];
    }

    public ArrayColormap(int[] map) {
        this.map = map;
    }

    public Object clone() {
        try {
            ArrayColormap g = (ArrayColormap)super.clone();
            g.map = (int[])this.map.clone();
            return g;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public void setMap(int[] map) {
        this.map = map;
    }

    public int[] getMap() {
        return this.map;
    }

    @Override
    public int getColor(float v) {
        int n = (int)(v * 255.0f);
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        return this.map[n];
    }

    public void setColorInterpolated(int index, int firstIndex, int lastIndex, int color) {
        int i;
        int firstColor = this.map[firstIndex];
        int lastColor = this.map[lastIndex];
        for (i = firstIndex; i <= index; ++i) {
            this.map[i] = ImageMath.mixColors((float)(i - firstIndex) / (float)(index - firstIndex), firstColor, color);
        }
        for (i = index; i < lastIndex; ++i) {
            this.map[i] = ImageMath.mixColors((float)(i - index) / (float)(lastIndex - index), color, lastColor);
        }
    }

    public void setColorRange(int firstIndex, int lastIndex, int color1, int color2) {
        for (int i = firstIndex; i <= lastIndex; ++i) {
            this.map[i] = ImageMath.mixColors((float)(i - firstIndex) / (float)(lastIndex - firstIndex), color1, color2);
        }
    }

    public void setColorRange(int firstIndex, int lastIndex, int color) {
        for (int i = firstIndex; i <= lastIndex; ++i) {
            this.map[i] = color;
        }
    }

    public void setColor(int index, int color) {
        this.map[index] = color;
    }
}

