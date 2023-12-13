/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.GLUface;
import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUtessellatorImpl;
import org.lwjgl.util.glu.tessellation.GLUvertex;

class Normal {
    static boolean SLANTED_SWEEP;
    static double S_UNIT_X;
    static double S_UNIT_Y;
    private static final boolean TRUE_PROJECT = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    private Normal() {
    }

    private static double Dot(double[] u2, double[] v2) {
        return u2[0] * v2[0] + u2[1] * v2[1] + u2[2] * v2[2];
    }

    static void Normalize(double[] v2) {
        double len = v2[0] * v2[0] + v2[1] * v2[1] + v2[2] * v2[2];
        if (!$assertionsDisabled && !(len > 0.0)) {
            throw new AssertionError();
        }
        len = Math.sqrt(len);
        v2[0] = v2[0] / len;
        v2[1] = v2[1] / len;
        v2[2] = v2[2] / len;
    }

    static int LongAxis(double[] v2) {
        int i2 = 0;
        if (Math.abs(v2[1]) > Math.abs(v2[0])) {
            i2 = 1;
        }
        if (Math.abs(v2[2]) > Math.abs(v2[i2])) {
            i2 = 2;
        }
        return i2;
    }

    static void ComputeNormal(GLUtessellatorImpl tess, double[] norm) {
        int i2;
        GLUvertex vHead = tess.mesh.vHead;
        double[] maxVal = new double[3];
        double[] minVal = new double[3];
        GLUvertex[] minVert = new GLUvertex[3];
        GLUvertex[] maxVert = new GLUvertex[3];
        double[] d1 = new double[3];
        double[] d2 = new double[3];
        double[] tNorm = new double[3];
        maxVal[2] = -2.0E150;
        maxVal[1] = -2.0E150;
        maxVal[0] = -2.0E150;
        minVal[2] = 2.0E150;
        minVal[1] = 2.0E150;
        minVal[0] = 2.0E150;
        GLUvertex v2 = vHead.next;
        while (v2 != vHead) {
            for (i2 = 0; i2 < 3; ++i2) {
                double c2 = v2.coords[i2];
                if (c2 < minVal[i2]) {
                    minVal[i2] = c2;
                    minVert[i2] = v2;
                }
                if (!(c2 > maxVal[i2])) continue;
                maxVal[i2] = c2;
                maxVert[i2] = v2;
            }
            v2 = v2.next;
        }
        i2 = 0;
        if (maxVal[1] - minVal[1] > maxVal[0] - minVal[0]) {
            i2 = 1;
        }
        if (maxVal[2] - minVal[2] > maxVal[i2] - minVal[i2]) {
            i2 = 2;
        }
        if (minVal[i2] >= maxVal[i2]) {
            norm[0] = 0.0;
            norm[1] = 0.0;
            norm[2] = 1.0;
            return;
        }
        double maxLen2 = 0.0;
        GLUvertex v1 = minVert[i2];
        GLUvertex v22 = maxVert[i2];
        d1[0] = v1.coords[0] - v22.coords[0];
        d1[1] = v1.coords[1] - v22.coords[1];
        d1[2] = v1.coords[2] - v22.coords[2];
        v2 = vHead.next;
        while (v2 != vHead) {
            d2[0] = v2.coords[0] - v22.coords[0];
            d2[1] = v2.coords[1] - v22.coords[1];
            d2[2] = v2.coords[2] - v22.coords[2];
            tNorm[0] = d1[1] * d2[2] - d1[2] * d2[1];
            tNorm[1] = d1[2] * d2[0] - d1[0] * d2[2];
            tNorm[2] = d1[0] * d2[1] - d1[1] * d2[0];
            double tLen2 = tNorm[0] * tNorm[0] + tNorm[1] * tNorm[1] + tNorm[2] * tNorm[2];
            if (tLen2 > maxLen2) {
                maxLen2 = tLen2;
                norm[0] = tNorm[0];
                norm[1] = tNorm[1];
                norm[2] = tNorm[2];
            }
            v2 = v2.next;
        }
        if (maxLen2 <= 0.0) {
            norm[2] = 0.0;
            norm[1] = 0.0;
            norm[0] = 0.0;
            norm[Normal.LongAxis((double[])d1)] = 1.0;
        }
    }

    static void CheckOrientation(GLUtessellatorImpl tess) {
        GLUface fHead = tess.mesh.fHead;
        GLUvertex vHead = tess.mesh.vHead;
        double area = 0.0;
        GLUface f2 = fHead.next;
        while (f2 != fHead) {
            GLUhalfEdge e2 = f2.anEdge;
            if (e2.winding > 0) {
                do {
                    area += (e2.Org.s - e2.Sym.Org.s) * (e2.Org.t + e2.Sym.Org.t);
                } while ((e2 = e2.Lnext) != f2.anEdge);
            }
            f2 = f2.next;
        }
        if (area < 0.0) {
            GLUvertex v2 = vHead.next;
            while (v2 != vHead) {
                v2.t = -v2.t;
                v2 = v2.next;
            }
            tess.tUnit[0] = -tess.tUnit[0];
            tess.tUnit[1] = -tess.tUnit[1];
            tess.tUnit[2] = -tess.tUnit[2];
        }
    }

    public static void __gl_projectPolygon(GLUtessellatorImpl tess) {
        GLUvertex vHead = tess.mesh.vHead;
        double[] norm = new double[3];
        boolean computedNormal = false;
        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0.0 && norm[1] == 0.0 && norm[2] == 0.0) {
            Normal.ComputeNormal(tess, norm);
            computedNormal = true;
        }
        double[] sUnit = tess.sUnit;
        double[] tUnit = tess.tUnit;
        int i2 = Normal.LongAxis(norm);
        sUnit[i2] = 0.0;
        sUnit[(i2 + 1) % 3] = S_UNIT_X;
        sUnit[(i2 + 2) % 3] = S_UNIT_Y;
        tUnit[i2] = 0.0;
        tUnit[(i2 + 1) % 3] = norm[i2] > 0.0 ? -S_UNIT_Y : S_UNIT_Y;
        tUnit[(i2 + 2) % 3] = norm[i2] > 0.0 ? S_UNIT_X : -S_UNIT_X;
        GLUvertex v2 = vHead.next;
        while (v2 != vHead) {
            v2.s = Normal.Dot(v2.coords, sUnit);
            v2.t = Normal.Dot(v2.coords, tUnit);
            v2 = v2.next;
        }
        if (computedNormal) {
            Normal.CheckOrientation(tess);
        }
    }

    static {
        boolean bl = $assertionsDisabled = !Normal.class.desiredAssertionStatus();
        if (SLANTED_SWEEP) {
            S_UNIT_X = 0.5094153956495538;
            S_UNIT_Y = 0.8605207462201063;
        } else {
            S_UNIT_X = 1.0;
            S_UNIT_Y = 0.0;
        }
    }
}

