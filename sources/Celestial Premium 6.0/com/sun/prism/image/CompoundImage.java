/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.Coords;

public abstract class CompoundImage {
    public static final int BORDER_SIZE_DEFAULT = 1;
    protected final int[] uSubdivision;
    protected final int[] u0;
    protected final int[] u1;
    protected final int[] vSubdivision;
    protected final int[] v0;
    protected final int[] v1;
    protected final int uSections;
    protected final int vSections;
    protected final int uBorderSize;
    protected final int vBorderSize;
    protected Image[] tiles;

    public CompoundImage(Image image, int n) {
        this(image, n, 1);
    }

    public CompoundImage(Image image, int n, int n2) {
        int n3;
        if (4 * n2 >= n) {
            n2 = n / 4;
        }
        int n4 = image.getWidth();
        int n5 = image.getHeight();
        this.uBorderSize = n4 <= n ? 0 : n2;
        this.vBorderSize = n5 <= n ? 0 : n2;
        this.uSubdivision = CompoundImage.subdivideUVs(n4, n, this.uBorderSize);
        this.vSubdivision = CompoundImage.subdivideUVs(n5, n, this.vBorderSize);
        this.uSections = this.uSubdivision.length - 1;
        this.vSections = this.vSubdivision.length - 1;
        this.u0 = new int[this.uSections];
        this.u1 = new int[this.uSections];
        this.v0 = new int[this.vSections];
        this.v1 = new int[this.vSections];
        this.tiles = new Image[this.uSections * this.vSections];
        for (n3 = 0; n3 != this.vSections; ++n3) {
            this.v0[n3] = this.vSubdivision[n3] - this.uBorder(n3);
            this.v1[n3] = this.vSubdivision[n3 + 1] + this.dBorder(n3);
        }
        for (n3 = 0; n3 != this.uSections; ++n3) {
            this.u0[n3] = this.uSubdivision[n3] - this.lBorder(n3);
            this.u1[n3] = this.uSubdivision[n3 + 1] + this.rBorder(n3);
        }
        for (n3 = 0; n3 != this.vSections; ++n3) {
            for (int i = 0; i != this.uSections; ++i) {
                this.tiles[n3 * this.uSections + i] = image.createSubImage(this.u0[i], this.v0[n3], this.u1[i] - this.u0[i], this.v1[n3] - this.v0[n3]);
            }
        }
    }

    private int lBorder(int n) {
        return n > 0 ? this.uBorderSize : 0;
    }

    private int rBorder(int n) {
        return n < this.uSections - 1 ? this.uBorderSize : 0;
    }

    private int uBorder(int n) {
        return n > 0 ? this.vBorderSize : 0;
    }

    private int dBorder(int n) {
        return n < this.vSections - 1 ? this.vBorderSize : 0;
    }

    private static int[] subdivideUVs(int n, int n2, int n3) {
        int n4 = n2 - n3 * 2;
        int n5 = (n - n3 * 2 + n4 - 1) / n4;
        int[] arrn = new int[n5 + 1];
        arrn[0] = 0;
        arrn[n5] = n;
        for (int i = 1; i < n5; ++i) {
            arrn[i] = n3 + n4 * i;
        }
        return arrn;
    }

    protected abstract Texture getTile(int var1, int var2, ResourceFactory var3);

    public void drawLazy(Graphics graphics, Coords coords, float f, float f2) {
        new CompoundCoords(this, coords).draw(graphics, this, f, f2);
    }
}

