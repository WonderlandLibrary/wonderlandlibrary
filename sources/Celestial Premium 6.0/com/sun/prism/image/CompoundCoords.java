/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundImage;
import com.sun.prism.image.Coords;

public class CompoundCoords {
    private int xImg0;
    private int xImg1;
    private int yImg0;
    private int yImg1;
    private Coords[] tileCoords;

    public CompoundCoords(CompoundImage compoundImage, Coords coords) {
        int n;
        int n2 = CompoundCoords.find1(CompoundCoords.fastFloor(coords.u0), compoundImage.uSubdivision);
        int n3 = CompoundCoords.find2(CompoundCoords.fastCeil(coords.u1), compoundImage.uSubdivision);
        int n4 = CompoundCoords.find1(CompoundCoords.fastFloor(coords.v0), compoundImage.vSubdivision);
        int n5 = CompoundCoords.find2(CompoundCoords.fastCeil(coords.v1), compoundImage.vSubdivision);
        if (n2 < 0 || n3 < 0 || n4 < 0 || n5 < 0) {
            return;
        }
        this.xImg0 = n2;
        this.xImg1 = n3;
        this.yImg0 = n4;
        this.yImg1 = n5;
        this.tileCoords = new Coords[(n3 - n2 + 1) * (n5 - n4 + 1)];
        float[] arrf = new float[n3 - n2];
        float[] arrf2 = new float[n5 - n4];
        for (n = n2; n < n3; ++n) {
            arrf[n - n2] = coords.getX(compoundImage.uSubdivision[n + 1]);
        }
        for (n = n4; n < n5; ++n) {
            arrf2[n - n4] = coords.getY(compoundImage.vSubdivision[n + 1]);
        }
        n = 0;
        for (int i = n4; i <= n5; ++i) {
            float f = (i == n4 ? coords.v0 : (float)compoundImage.vSubdivision[i]) - (float)compoundImage.v0[i];
            float f2 = (i == n5 ? coords.v1 : (float)compoundImage.vSubdivision[i + 1]) - (float)compoundImage.v0[i];
            float f3 = i == n4 ? coords.y0 : arrf2[i - n4 - 1];
            float f4 = i == n5 ? coords.y1 : arrf2[i - n4];
            for (int j = n2; j <= n3; ++j) {
                Coords coords2 = new Coords();
                coords2.v0 = f;
                coords2.v1 = f2;
                coords2.y0 = f3;
                coords2.y1 = f4;
                coords2.u0 = (j == n2 ? coords.u0 : (float)compoundImage.uSubdivision[j]) - (float)compoundImage.u0[j];
                coords2.u1 = (j == n3 ? coords.u1 : (float)compoundImage.uSubdivision[j + 1]) - (float)compoundImage.u0[j];
                coords2.x0 = j == n2 ? coords.x0 : arrf[j - n2 - 1];
                coords2.x1 = j == n3 ? coords.x1 : arrf[j - n2];
                this.tileCoords[n++] = coords2;
            }
        }
    }

    public void draw(Graphics graphics, CompoundImage compoundImage, float f, float f2) {
        if (this.tileCoords == null) {
            return;
        }
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        int n = 0;
        for (int i = this.yImg0; i <= this.yImg1; ++i) {
            for (int j = this.xImg0; j <= this.xImg1; ++j) {
                Texture texture = compoundImage.getTile(j, i, resourceFactory);
                this.tileCoords[n++].draw(texture, graphics, f, f2);
                texture.unlock();
            }
        }
    }

    private static int find1(int n, int[] arrn) {
        for (int i = 0; i < arrn.length - 1; ++i) {
            if (arrn[i] > n || n >= arrn[i + 1]) continue;
            return i;
        }
        return -1;
    }

    private static int find2(int n, int[] arrn) {
        for (int i = 0; i < arrn.length - 1; ++i) {
            if (arrn[i] >= n || n > arrn[i + 1]) continue;
            return i;
        }
        return -1;
    }

    private static int fastFloor(float f) {
        int n = (int)f;
        return (float)n <= f ? n : n - 1;
    }

    private static int fastCeil(float f) {
        int n = (int)f;
        return (float)n >= f ? n : n + 1;
    }
}

