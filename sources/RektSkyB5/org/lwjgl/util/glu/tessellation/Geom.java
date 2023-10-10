/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUvertex;

class Geom {
    private Geom() {
    }

    static double EdgeEval(GLUvertex u2, GLUvertex v2, GLUvertex w2) {
        assert (Geom.VertLeq(u2, v2) && Geom.VertLeq(v2, w2));
        double gapL = v2.s - u2.s;
        double gapR = w2.s - v2.s;
        if (gapL + gapR > 0.0) {
            if (gapL < gapR) {
                return v2.t - u2.t + (u2.t - w2.t) * (gapL / (gapL + gapR));
            }
            return v2.t - w2.t + (w2.t - u2.t) * (gapR / (gapL + gapR));
        }
        return 0.0;
    }

    static double EdgeSign(GLUvertex u2, GLUvertex v2, GLUvertex w2) {
        assert (Geom.VertLeq(u2, v2) && Geom.VertLeq(v2, w2));
        double gapL = v2.s - u2.s;
        double gapR = w2.s - v2.s;
        if (gapL + gapR > 0.0) {
            return (v2.t - w2.t) * gapL + (v2.t - u2.t) * gapR;
        }
        return 0.0;
    }

    static double TransEval(GLUvertex u2, GLUvertex v2, GLUvertex w2) {
        assert (Geom.TransLeq(u2, v2) && Geom.TransLeq(v2, w2));
        double gapL = v2.t - u2.t;
        double gapR = w2.t - v2.t;
        if (gapL + gapR > 0.0) {
            if (gapL < gapR) {
                return v2.s - u2.s + (u2.s - w2.s) * (gapL / (gapL + gapR));
            }
            return v2.s - w2.s + (w2.s - u2.s) * (gapR / (gapL + gapR));
        }
        return 0.0;
    }

    static double TransSign(GLUvertex u2, GLUvertex v2, GLUvertex w2) {
        assert (Geom.TransLeq(u2, v2) && Geom.TransLeq(v2, w2));
        double gapL = v2.t - u2.t;
        double gapR = w2.t - v2.t;
        if (gapL + gapR > 0.0) {
            return (v2.s - w2.s) * gapL + (v2.s - u2.s) * gapR;
        }
        return 0.0;
    }

    static boolean VertCCW(GLUvertex u2, GLUvertex v2, GLUvertex w2) {
        return u2.s * (v2.t - w2.t) + v2.s * (w2.t - u2.t) + w2.s * (u2.t - v2.t) >= 0.0;
    }

    static double Interpolate(double a2, double x2, double b2, double y2) {
        a2 = a2 < 0.0 ? 0.0 : a2;
        double d2 = b2 = b2 < 0.0 ? 0.0 : b2;
        if (a2 <= b2) {
            if (b2 == 0.0) {
                return (x2 + y2) / 2.0;
            }
            return x2 + (y2 - x2) * (a2 / (a2 + b2));
        }
        return y2 + (x2 - y2) * (b2 / (a2 + b2));
    }

    static void EdgeIntersect(GLUvertex o1, GLUvertex d1, GLUvertex o2, GLUvertex d2, GLUvertex v2) {
        double z2;
        double z1;
        GLUvertex temp;
        if (!Geom.VertLeq(o1, d1)) {
            temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!Geom.VertLeq(o2, d2)) {
            temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!Geom.VertLeq(o1, o2)) {
            temp = o1;
            o1 = o2;
            o2 = temp;
            temp = d1;
            d1 = d2;
            d2 = temp;
        }
        if (!Geom.VertLeq(o2, d1)) {
            v2.s = (o2.s + d1.s) / 2.0;
        } else if (Geom.VertLeq(d1, d2)) {
            z1 = Geom.EdgeEval(o1, o2, d1);
            if (z1 + (z2 = Geom.EdgeEval(o2, d1, d2)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v2.s = Geom.Interpolate(z1, o2.s, z2, d1.s);
        } else {
            z1 = Geom.EdgeSign(o1, o2, d1);
            if (z1 + (z2 = -Geom.EdgeSign(o1, d2, d1)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v2.s = Geom.Interpolate(z1, o2.s, z2, d2.s);
        }
        if (!Geom.TransLeq(o1, d1)) {
            temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!Geom.TransLeq(o2, d2)) {
            temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!Geom.TransLeq(o1, o2)) {
            temp = o2;
            o2 = o1;
            o1 = temp;
            temp = d2;
            d2 = d1;
            d1 = temp;
        }
        if (!Geom.TransLeq(o2, d1)) {
            v2.t = (o2.t + d1.t) / 2.0;
        } else if (Geom.TransLeq(d1, d2)) {
            z1 = Geom.TransEval(o1, o2, d1);
            if (z1 + (z2 = Geom.TransEval(o2, d1, d2)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v2.t = Geom.Interpolate(z1, o2.t, z2, d1.t);
        } else {
            z1 = Geom.TransSign(o1, o2, d1);
            if (z1 + (z2 = -Geom.TransSign(o1, d2, d1)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v2.t = Geom.Interpolate(z1, o2.t, z2, d2.t);
        }
    }

    static boolean VertEq(GLUvertex u2, GLUvertex v2) {
        return u2.s == v2.s && u2.t == v2.t;
    }

    static boolean VertLeq(GLUvertex u2, GLUvertex v2) {
        return u2.s < v2.s || u2.s == v2.s && u2.t <= v2.t;
    }

    static boolean TransLeq(GLUvertex u2, GLUvertex v2) {
        return u2.t < v2.t || u2.t == v2.t && u2.s <= v2.s;
    }

    static boolean EdgeGoesLeft(GLUhalfEdge e2) {
        return Geom.VertLeq(e2.Sym.Org, e2.Org);
    }

    static boolean EdgeGoesRight(GLUhalfEdge e2) {
        return Geom.VertLeq(e2.Org, e2.Sym.Org);
    }

    static double VertL1dist(GLUvertex u2, GLUvertex v2) {
        return Math.abs(u2.s - v2.s) + Math.abs(u2.t - v2.t);
    }
}

