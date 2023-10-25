/*
 * Decompiled with CFR 0.150.
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

    private static double Dot(double[] u, double[] v) {
        return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
    }

    static void Normalize(double[] v) {
        double len = v[0] * v[0] + v[1] * v[1] + v[2] * v[2];
        if (!$assertionsDisabled && !(len > 0.0)) {
            throw new AssertionError();
        }
        len = Math.sqrt(len);
        v[0] = v[0] / len;
        v[1] = v[1] / len;
        v[2] = v[2] / len;
    }

    static int LongAxis(double[] v) {
        int i = 0;
        if (Math.abs(v[1]) > Math.abs(v[0])) {
            i = 1;
        }
        if (Math.abs(v[2]) > Math.abs(v[i])) {
            i = 2;
        }
        return i;
    }

    static void ComputeNormal(GLUtessellatorImpl tess, double[] norm) {
        int i;
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
        GLUvertex v = vHead.next;
        while (v != vHead) {
            for (i = 0; i < 3; ++i) {
                double c = v.coords[i];
                if (c < minVal[i]) {
                    minVal[i] = c;
                    minVert[i] = v;
                }
                if (!(c > maxVal[i])) continue;
                maxVal[i] = c;
                maxVert[i] = v;
            }
            v = v.next;
        }
        i = 0;
        if (maxVal[1] - minVal[1] > maxVal[0] - minVal[0]) {
            i = 1;
        }
        if (maxVal[2] - minVal[2] > maxVal[i] - minVal[i]) {
            i = 2;
        }
        if (minVal[i] >= maxVal[i]) {
            norm[0] = 0.0;
            norm[1] = 0.0;
            norm[2] = 1.0;
            return;
        }
        double maxLen2 = 0.0;
        GLUvertex v1 = minVert[i];
        GLUvertex v2 = maxVert[i];
        d1[0] = v1.coords[0] - v2.coords[0];
        d1[1] = v1.coords[1] - v2.coords[1];
        d1[2] = v1.coords[2] - v2.coords[2];
        v = vHead.next;
        while (v != vHead) {
            d2[0] = v.coords[0] - v2.coords[0];
            d2[1] = v.coords[1] - v2.coords[1];
            d2[2] = v.coords[2] - v2.coords[2];
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
            v = v.next;
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
        GLUface f = fHead.next;
        while (f != fHead) {
            GLUhalfEdge e = f.anEdge;
            if (e.winding > 0) {
                do {
                    area += (e.Org.s - e.Sym.Org.s) * (e.Org.t + e.Sym.Org.t);
                } while ((e = e.Lnext) != f.anEdge);
            }
            f = f.next;
        }
        if (area < 0.0) {
            GLUvertex v = vHead.next;
            while (v != vHead) {
                v.t = -v.t;
                v = v.next;
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
        int i = Normal.LongAxis(norm);
        sUnit[i] = 0.0;
        sUnit[(i + 1) % 3] = S_UNIT_X;
        sUnit[(i + 2) % 3] = S_UNIT_Y;
        tUnit[i] = 0.0;
        tUnit[(i + 1) % 3] = norm[i] > 0.0 ? -S_UNIT_Y : S_UNIT_Y;
        tUnit[(i + 2) % 3] = norm[i] > 0.0 ? S_UNIT_X : -S_UNIT_X;
        GLUvertex v = vHead.next;
        while (v != vHead) {
            v.s = Normal.Dot(v.coords, sUnit);
            v.t = Normal.Dot(v.coords, tUnit);
            v = v.next;
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

