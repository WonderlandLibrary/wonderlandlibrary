/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.CachedVertex;
import org.lwjgl.util.glu.tessellation.GLUface;
import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUmesh;
import org.lwjgl.util.glu.tessellation.GLUtessellatorImpl;

class Render {
    private static final boolean USE_OPTIMIZED_CODE_PATH = false;
    private static final RenderFan renderFan = new RenderFan();
    private static final RenderStrip renderStrip = new RenderStrip();
    private static final RenderTriangle renderTriangle = new RenderTriangle();
    private static final int SIGN_INCONSISTENT = 2;

    private Render() {
    }

    public static void __gl_renderMesh(GLUtessellatorImpl tess, GLUmesh mesh) {
        tess.lonelyTriList = null;
        GLUface f = mesh.fHead.next;
        while (f != mesh.fHead) {
            f.marked = false;
            f = f.next;
        }
        f = mesh.fHead.next;
        while (f != mesh.fHead) {
            if (f.inside && !f.marked) {
                Render.RenderMaximumFaceGroup(tess, f);
                assert (f.marked);
            }
            f = f.next;
        }
        if (tess.lonelyTriList != null) {
            Render.RenderLonelyTriangles(tess, tess.lonelyTriList);
            tess.lonelyTriList = null;
        }
    }

    static void RenderMaximumFaceGroup(GLUtessellatorImpl tess, GLUface fOrig) {
        GLUhalfEdge e = fOrig.anEdge;
        FaceCount max = new FaceCount();
        max.size = 1L;
        max.eStart = e;
        max.render = renderTriangle;
        if (!tess.flagBoundary) {
            FaceCount newFace = Render.MaximumFan(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumFan(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumFan(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
        }
        max.render.render(tess, max.eStart, max.size);
    }

    private static boolean Marked(GLUface f) {
        return !f.inside || f.marked;
    }

    private static GLUface AddToTrail(GLUface f, GLUface t) {
        f.trail = t;
        f.marked = true;
        return f;
    }

    private static void FreeTrail(GLUface t) {
        while (t != null) {
            t.marked = false;
            t = t.trail;
        }
    }

    static FaceCount MaximumFan(GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0L, null, renderFan);
        GLUface trail = null;
        GLUhalfEdge e = eOrig;
        while (!Render.Marked(e.Lface)) {
            trail = Render.AddToTrail(e.Lface, trail);
            ++newFace.size;
            e = e.Onext;
        }
        e = eOrig;
        while (!Render.Marked(e.Sym.Lface)) {
            trail = Render.AddToTrail(e.Sym.Lface, trail);
            ++newFace.size;
            e = e.Sym.Lnext;
        }
        newFace.eStart = e;
        Render.FreeTrail(trail);
        return newFace;
    }

    private static boolean IsEven(long n) {
        return (n & 1L) == 0L;
    }

    static FaceCount MaximumStrip(GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0L, null, renderStrip);
        long headSize = 0L;
        long tailSize = 0L;
        GLUface trail = null;
        GLUhalfEdge e = eOrig;
        while (!Render.Marked(e.Lface)) {
            trail = Render.AddToTrail(e.Lface, trail);
            ++tailSize;
            e = e.Lnext.Sym;
            if (Render.Marked(e.Lface)) break;
            trail = Render.AddToTrail(e.Lface, trail);
            ++tailSize;
            e = e.Onext;
        }
        GLUhalfEdge eTail = e;
        e = eOrig;
        while (!Render.Marked(e.Sym.Lface)) {
            trail = Render.AddToTrail(e.Sym.Lface, trail);
            ++headSize;
            e = e.Sym.Lnext;
            if (Render.Marked(e.Sym.Lface)) break;
            trail = Render.AddToTrail(e.Sym.Lface, trail);
            ++headSize;
            e = e.Sym.Onext.Sym;
        }
        GLUhalfEdge eHead = e;
        newFace.size = tailSize + headSize;
        if (Render.IsEven(tailSize)) {
            newFace.eStart = eTail.Sym;
        } else if (Render.IsEven(headSize)) {
            newFace.eStart = eHead;
        } else {
            --newFace.size;
            newFace.eStart = eHead.Onext;
        }
        Render.FreeTrail(trail);
        return newFace;
    }

    static void RenderLonelyTriangles(GLUtessellatorImpl tess, GLUface f) {
        int edgeState = -1;
        tess.callBeginOrBeginData(4);
        while (f != null) {
            GLUhalfEdge e = f.anEdge;
            do {
                if (tess.flagBoundary) {
                    int newState;
                    int n = newState = !e.Sym.Lface.inside ? 1 : 0;
                    if (edgeState != newState) {
                        edgeState = newState;
                        tess.callEdgeFlagOrEdgeFlagData(edgeState != 0);
                    }
                }
                tess.callVertexOrVertexData(e.Org.data);
            } while ((e = e.Lnext) != f.anEdge);
            f = f.trail;
        }
        tess.callEndOrEndData();
    }

    public static void __gl_renderBoundary(GLUtessellatorImpl tess, GLUmesh mesh) {
        GLUface f = mesh.fHead.next;
        while (f != mesh.fHead) {
            if (f.inside) {
                tess.callBeginOrBeginData(2);
                GLUhalfEdge e = f.anEdge;
                do {
                    tess.callVertexOrVertexData(e.Org.data);
                } while ((e = e.Lnext) != f.anEdge);
                tess.callEndOrEndData();
            }
            f = f.next;
        }
    }

    static int ComputeNormal(GLUtessellatorImpl tess, double[] norm, boolean check) {
        CachedVertex[] v = tess.cache;
        int vn = tess.cacheCount;
        double[] n = new double[3];
        int sign = 0;
        if (!check) {
            norm[2] = 0.0;
            norm[1] = 0.0;
            norm[0] = 0.0;
        }
        int vc = 1;
        double xc = v[vc].coords[0] - v[0].coords[0];
        double yc = v[vc].coords[1] - v[0].coords[1];
        double zc = v[vc].coords[2] - v[0].coords[2];
        while (++vc < vn) {
            double xp = xc;
            double yp = yc;
            double zp = zc;
            xc = v[vc].coords[0] - v[0].coords[0];
            yc = v[vc].coords[1] - v[0].coords[1];
            zc = v[vc].coords[2] - v[0].coords[2];
            n[0] = yp * zc - zp * yc;
            n[1] = zp * xc - xp * zc;
            n[2] = xp * yc - yp * xc;
            double dot = n[0] * norm[0] + n[1] * norm[1] + n[2] * norm[2];
            if (!check) {
                if (dot >= 0.0) {
                    norm[0] = norm[0] + n[0];
                    norm[1] = norm[1] + n[1];
                    norm[2] = norm[2] + n[2];
                    continue;
                }
                norm[0] = norm[0] - n[0];
                norm[1] = norm[1] - n[1];
                norm[2] = norm[2] - n[2];
                continue;
            }
            if (dot == 0.0) continue;
            if (dot > 0.0) {
                if (sign < 0) {
                    return 2;
                }
                sign = 1;
                continue;
            }
            if (sign > 0) {
                return 2;
            }
            sign = -1;
        }
        return sign;
    }

    public static boolean __gl_renderCache(GLUtessellatorImpl tess) {
        int sign;
        CachedVertex[] v = tess.cache;
        int vn = tess.cacheCount;
        double[] norm = new double[3];
        if (tess.cacheCount < 3) {
            return true;
        }
        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0.0 && norm[1] == 0.0 && norm[2] == 0.0) {
            Render.ComputeNormal(tess, norm, false);
        }
        if ((sign = Render.ComputeNormal(tess, norm, true)) == 2) {
            return false;
        }
        return sign == 0;
    }

    private static class RenderStrip
    implements renderCallBack {
        private RenderStrip() {
        }

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(5);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Render.Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Lnext.Sym;
                tess.callVertexOrVertexData(e.Org.data);
                if (Render.Marked(e.Lface)) break;
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert (size == 0L);
            tess.callEndOrEndData();
        }
    }

    private static class RenderFan
    implements renderCallBack {
        private RenderFan() {
        }

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(6);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Render.Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert (size == 0L);
            tess.callEndOrEndData();
        }
    }

    private static class RenderTriangle
    implements renderCallBack {
        private RenderTriangle() {
        }

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e, long size) {
            assert (size == 1L);
            tess.lonelyTriList = Render.AddToTrail(e.Lface, tess.lonelyTriList);
        }
    }

    private static interface renderCallBack {
        public void render(GLUtessellatorImpl var1, GLUhalfEdge var2, long var3);
    }

    private static class FaceCount {
        long size;
        GLUhalfEdge eStart;
        renderCallBack render;

        private FaceCount() {
        }

        private FaceCount(long size, GLUhalfEdge eStart, renderCallBack render) {
            this.size = size;
            this.eStart = eStart;
            this.render = render;
        }
    }
}

