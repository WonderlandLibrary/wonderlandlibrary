/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUvertex;

class Geom {
    private Geom() {
    }

    static double EdgeEval(GLUvertex u, GLUvertex v, GLUvertex w) {
        assert (Geom.VertLeq(u, v) && Geom.VertLeq(v, w));
        double gapL = v.s - u.s;
        double gapR = w.s - v.s;
        if (gapL + gapR > 0.0) {
            if (gapL < gapR) {
                return v.t - u.t + (u.t - w.t) * (gapL / (gapL + gapR));
            }
            return v.t - w.t + (w.t - u.t) * (gapR / (gapL + gapR));
        }
        return 0.0;
    }

    static double EdgeSign(GLUvertex u, GLUvertex v, GLUvertex w) {
        assert (Geom.VertLeq(u, v) && Geom.VertLeq(v, w));
        double gapL = v.s - u.s;
        double gapR = w.s - v.s;
        if (gapL + gapR > 0.0) {
            return (v.t - w.t) * gapL + (v.t - u.t) * gapR;
        }
        return 0.0;
    }

    static double TransEval(GLUvertex u, GLUvertex v, GLUvertex w) {
        assert (Geom.TransLeq(u, v) && Geom.TransLeq(v, w));
        double gapL = v.t - u.t;
        double gapR = w.t - v.t;
        if (gapL + gapR > 0.0) {
            if (gapL < gapR) {
                return v.s - u.s + (u.s - w.s) * (gapL / (gapL + gapR));
            }
            return v.s - w.s + (w.s - u.s) * (gapR / (gapL + gapR));
        }
        return 0.0;
    }

    static double TransSign(GLUvertex u, GLUvertex v, GLUvertex w) {
        assert (Geom.TransLeq(u, v) && Geom.TransLeq(v, w));
        double gapL = v.t - u.t;
        double gapR = w.t - v.t;
        if (gapL + gapR > 0.0) {
            return (v.s - w.s) * gapL + (v.s - u.s) * gapR;
        }
        return 0.0;
    }

    static boolean VertCCW(GLUvertex u, GLUvertex v, GLUvertex w) {
        return u.s * (v.t - w.t) + v.s * (w.t - u.t) + w.s * (u.t - v.t) >= 0.0;
    }

    static double Interpolate(double a, double x, double b, double y) {
        a = a < 0.0 ? 0.0 : a;
        double d = b = b < 0.0 ? 0.0 : b;
        if (a <= b) {
            if (b == 0.0) {
                return (x + y) / 2.0;
            }
            return x + (y - x) * (a / (a + b));
        }
        return y + (x - y) * (b / (a + b));
    }

    static void EdgeIntersect(GLUvertex o1, GLUvertex d1, GLUvertex o2, GLUvertex d2, GLUvertex v) {
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
            v.s = (o2.s + d1.s) / 2.0;
        } else if (Geom.VertLeq(d1, d2)) {
            z1 = Geom.EdgeEval(o1, o2, d1);
            if (z1 + (z2 = Geom.EdgeEval(o2, d1, d2)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Geom.Interpolate(z1, o2.s, z2, d1.s);
        } else {
            z1 = Geom.EdgeSign(o1, o2, d1);
            if (z1 + (z2 = -Geom.EdgeSign(o1, d2, d1)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Geom.Interpolate(z1, o2.s, z2, d2.s);
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
            v.t = (o2.t + d1.t) / 2.0;
        } else if (Geom.TransLeq(d1, d2)) {
            z1 = Geom.TransEval(o1, o2, d1);
            if (z1 + (z2 = Geom.TransEval(o2, d1, d2)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Geom.Interpolate(z1, o2.t, z2, d1.t);
        } else {
            z1 = Geom.TransSign(o1, o2, d1);
            if (z1 + (z2 = -Geom.TransSign(o1, d2, d1)) < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Geom.Interpolate(z1, o2.t, z2, d2.t);
        }
    }

    static boolean VertEq(GLUvertex u, GLUvertex v) {
        return u.s == v.s && u.t == v.t;
    }

    static boolean VertLeq(GLUvertex u, GLUvertex v) {
        return u.s < v.s || u.s == v.s && u.t <= v.t;
    }

    static boolean TransLeq(GLUvertex u, GLUvertex v) {
        return u.t < v.t || u.t == v.t && u.s <= v.s;
    }

    static boolean EdgeGoesLeft(GLUhalfEdge e) {
        return Geom.VertLeq(e.Sym.Org, e.Org);
    }

    static boolean EdgeGoesRight(GLUhalfEdge e) {
        return Geom.VertLeq(e.Org, e.Sym.Org);
    }

    static double VertL1dist(GLUvertex u, GLUvertex v) {
        return Math.abs(u.s - v.s) + Math.abs(u.t - v.t);
    }
}

