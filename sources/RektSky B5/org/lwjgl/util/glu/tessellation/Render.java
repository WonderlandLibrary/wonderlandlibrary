/*
 * Decompiled with CFR 0.152.
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
        GLUface f2 = mesh.fHead.next;
        while (f2 != mesh.fHead) {
            f2.marked = false;
            f2 = f2.next;
        }
        f2 = mesh.fHead.next;
        while (f2 != mesh.fHead) {
            if (f2.inside && !f2.marked) {
                Render.RenderMaximumFaceGroup(tess, f2);
                assert (f2.marked);
            }
            f2 = f2.next;
        }
        if (tess.lonelyTriList != null) {
            Render.RenderLonelyTriangles(tess, tess.lonelyTriList);
            tess.lonelyTriList = null;
        }
    }

    static void RenderMaximumFaceGroup(GLUtessellatorImpl tess, GLUface fOrig) {
        GLUhalfEdge e2 = fOrig.anEdge;
        FaceCount max = new FaceCount();
        max.size = 1L;
        max.eStart = e2;
        max.render = renderTriangle;
        if (!tess.flagBoundary) {
            FaceCount newFace = Render.MaximumFan(e2);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumFan(e2.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumFan(e2.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e2);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e2.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = Render.MaximumStrip(e2.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
        }
        max.render.render(tess, max.eStart, max.size);
    }

    private static boolean Marked(GLUface f2) {
        return !f2.inside || f2.marked;
    }

    private static GLUface AddToTrail(GLUface f2, GLUface t2) {
        f2.trail = t2;
        f2.marked = true;
        return f2;
    }

    private static void FreeTrail(GLUface t2) {
        while (t2 != null) {
            t2.marked = false;
            t2 = t2.trail;
        }
    }

    static FaceCount MaximumFan(GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0L, null, renderFan);
        GLUface trail = null;
        GLUhalfEdge e2 = eOrig;
        while (!Render.Marked(e2.Lface)) {
            trail = Render.AddToTrail(e2.Lface, trail);
            ++newFace.size;
            e2 = e2.Onext;
        }
        e2 = eOrig;
        while (!Render.Marked(e2.Sym.Lface)) {
            trail = Render.AddToTrail(e2.Sym.Lface, trail);
            ++newFace.size;
            e2 = e2.Sym.Lnext;
        }
        newFace.eStart = e2;
        Render.FreeTrail(trail);
        return newFace;
    }

    private static boolean IsEven(long n2) {
        return (n2 & 1L) == 0L;
    }

    static FaceCount MaximumStrip(GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0L, null, renderStrip);
        long headSize = 0L;
        long tailSize = 0L;
        GLUface trail = null;
        GLUhalfEdge e2 = eOrig;
        while (!Render.Marked(e2.Lface)) {
            trail = Render.AddToTrail(e2.Lface, trail);
            ++tailSize;
            e2 = e2.Lnext.Sym;
            if (Render.Marked(e2.Lface)) break;
            trail = Render.AddToTrail(e2.Lface, trail);
            ++tailSize;
            e2 = e2.Onext;
        }
        GLUhalfEdge eTail = e2;
        e2 = eOrig;
        while (!Render.Marked(e2.Sym.Lface)) {
            trail = Render.AddToTrail(e2.Sym.Lface, trail);
            ++headSize;
            e2 = e2.Sym.Lnext;
            if (Render.Marked(e2.Sym.Lface)) break;
            trail = Render.AddToTrail(e2.Sym.Lface, trail);
            ++headSize;
            e2 = e2.Sym.Onext.Sym;
        }
        GLUhalfEdge eHead = e2;
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

    static void RenderLonelyTriangles(GLUtessellatorImpl tess, GLUface f2) {
        int edgeState = -1;
        tess.callBeginOrBeginData(4);
        while (f2 != null) {
            GLUhalfEdge e2 = f2.anEdge;
            do {
                if (tess.flagBoundary) {
                    int newState;
                    int n2 = newState = !e2.Sym.Lface.inside ? 1 : 0;
                    if (edgeState != newState) {
                        edgeState = newState;
                        tess.callEdgeFlagOrEdgeFlagData(edgeState != 0);
                    }
                }
                tess.callVertexOrVertexData(e2.Org.data);
            } while ((e2 = e2.Lnext) != f2.anEdge);
            f2 = f2.trail;
        }
        tess.callEndOrEndData();
    }

    public static void __gl_renderBoundary(GLUtessellatorImpl tess, GLUmesh mesh) {
        GLUface f2 = mesh.fHead.next;
        while (f2 != mesh.fHead) {
            if (f2.inside) {
                tess.callBeginOrBeginData(2);
                GLUhalfEdge e2 = f2.anEdge;
                do {
                    tess.callVertexOrVertexData(e2.Org.data);
                } while ((e2 = e2.Lnext) != f2.anEdge);
                tess.callEndOrEndData();
            }
            f2 = f2.next;
        }
    }

    static int ComputeNormal(GLUtessellatorImpl tess, double[] norm, boolean check) {
        CachedVertex[] v2 = tess.cache;
        int vn = tess.cacheCount;
        double[] n2 = new double[3];
        int sign = 0;
        if (!check) {
            norm[2] = 0.0;
            norm[1] = 0.0;
            norm[0] = 0.0;
        }
        int vc = 1;
        double xc = v2[vc].coords[0] - v2[0].coords[0];
        double yc = v2[vc].coords[1] - v2[0].coords[1];
        double zc = v2[vc].coords[2] - v2[0].coords[2];
        while (++vc < vn) {
            double xp = xc;
            double yp = yc;
            double zp = zc;
            xc = v2[vc].coords[0] - v2[0].coords[0];
            yc = v2[vc].coords[1] - v2[0].coords[1];
            zc = v2[vc].coords[2] - v2[0].coords[2];
            n2[0] = yp * zc - zp * yc;
            n2[1] = zp * xc - xp * zc;
            n2[2] = xp * yc - yp * xc;
            double dot = n2[0] * norm[0] + n2[1] * norm[1] + n2[2] * norm[2];
            if (!check) {
                if (dot >= 0.0) {
                    norm[0] = norm[0] + n2[0];
                    norm[1] = norm[1] + n2[1];
                    norm[2] = norm[2] + n2[2];
                    continue;
                }
                norm[0] = norm[0] - n2[0];
                norm[1] = norm[1] - n2[1];
                norm[2] = norm[2] - n2[2];
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
        CachedVertex[] v2 = tess.cache;
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

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e2, long size) {
            tess.callBeginOrBeginData(5);
            tess.callVertexOrVertexData(e2.Org.data);
            tess.callVertexOrVertexData(e2.Sym.Org.data);
            while (!Render.Marked(e2.Lface)) {
                e2.Lface.marked = true;
                --size;
                e2 = e2.Lnext.Sym;
                tess.callVertexOrVertexData(e2.Org.data);
                if (Render.Marked(e2.Lface)) break;
                e2.Lface.marked = true;
                --size;
                e2 = e2.Onext;
                tess.callVertexOrVertexData(e2.Sym.Org.data);
            }
            assert (size == 0L);
            tess.callEndOrEndData();
        }
    }

    private static class RenderFan
    implements renderCallBack {
        private RenderFan() {
        }

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e2, long size) {
            tess.callBeginOrBeginData(6);
            tess.callVertexOrVertexData(e2.Org.data);
            tess.callVertexOrVertexData(e2.Sym.Org.data);
            while (!Render.Marked(e2.Lface)) {
                e2.Lface.marked = true;
                --size;
                e2 = e2.Onext;
                tess.callVertexOrVertexData(e2.Sym.Org.data);
            }
            assert (size == 0L);
            tess.callEndOrEndData();
        }
    }

    private static class RenderTriangle
    implements renderCallBack {
        private RenderTriangle() {
        }

        public void render(GLUtessellatorImpl tess, GLUhalfEdge e2, long size) {
            assert (size == 1L);
            tess.lonelyTriList = Render.AddToTrail(e2.Lface, tess.lonelyTriList);
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

