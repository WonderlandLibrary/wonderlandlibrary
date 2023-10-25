/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.GLUface;
import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUmesh;
import org.lwjgl.util.glu.tessellation.GLUvertex;

class Mesh {
    private Mesh() {
    }

    static GLUhalfEdge MakeEdge(GLUhalfEdge eNext) {
        GLUhalfEdge ePrev;
        GLUhalfEdge e = new GLUhalfEdge(true);
        GLUhalfEdge eSym = new GLUhalfEdge(false);
        if (!eNext.first) {
            eNext = eNext.Sym;
        }
        eSym.next = ePrev = eNext.Sym.next;
        ePrev.Sym.next = e;
        e.next = eNext;
        eNext.Sym.next = eSym;
        e.Sym = eSym;
        e.Onext = e;
        e.Lnext = eSym;
        e.Org = null;
        e.Lface = null;
        e.winding = 0;
        e.activeRegion = null;
        eSym.Sym = e;
        eSym.Onext = eSym;
        eSym.Lnext = e;
        eSym.Org = null;
        eSym.Lface = null;
        eSym.winding = 0;
        eSym.activeRegion = null;
        return e;
    }

    static void Splice(GLUhalfEdge a, GLUhalfEdge b) {
        GLUhalfEdge aOnext = a.Onext;
        GLUhalfEdge bOnext = b.Onext;
        aOnext.Sym.Lnext = b;
        bOnext.Sym.Lnext = a;
        a.Onext = bOnext;
        b.Onext = aOnext;
    }

    static void MakeVertex(GLUvertex newVertex, GLUhalfEdge eOrig, GLUvertex vNext) {
        GLUvertex vPrev;
        GLUvertex vNew = newVertex;
        assert (vNew != null);
        vNew.prev = vPrev = vNext.prev;
        vPrev.next = vNew;
        vNew.next = vNext;
        vNext.prev = vNew;
        vNew.anEdge = eOrig;
        vNew.data = null;
        GLUhalfEdge e = eOrig;
        do {
            e.Org = vNew;
        } while ((e = e.Onext) != eOrig);
    }

    static void MakeFace(GLUface newFace, GLUhalfEdge eOrig, GLUface fNext) {
        GLUface fPrev;
        GLUface fNew = newFace;
        assert (fNew != null);
        fNew.prev = fPrev = fNext.prev;
        fPrev.next = fNew;
        fNew.next = fNext;
        fNext.prev = fNew;
        fNew.anEdge = eOrig;
        fNew.data = null;
        fNew.trail = null;
        fNew.marked = false;
        fNew.inside = fNext.inside;
        GLUhalfEdge e = eOrig;
        do {
            e.Lface = fNew;
        } while ((e = e.Lnext) != eOrig);
    }

    static void KillEdge(GLUhalfEdge eDel) {
        GLUhalfEdge ePrev;
        if (!eDel.first) {
            eDel = eDel.Sym;
        }
        GLUhalfEdge eNext = eDel.next;
        eNext.Sym.next = ePrev = eDel.Sym.next;
        ePrev.Sym.next = eNext;
    }

    static void KillVertex(GLUvertex vDel, GLUvertex newOrg) {
        GLUhalfEdge eStart;
        GLUhalfEdge e = eStart = vDel.anEdge;
        do {
            e.Org = newOrg;
        } while ((e = e.Onext) != eStart);
        GLUvertex vPrev = vDel.prev;
        GLUvertex vNext = vDel.next;
        vNext.prev = vPrev;
        vPrev.next = vNext;
    }

    static void KillFace(GLUface fDel, GLUface newLface) {
        GLUhalfEdge eStart;
        GLUhalfEdge e = eStart = fDel.anEdge;
        do {
            e.Lface = newLface;
        } while ((e = e.Lnext) != eStart);
        GLUface fPrev = fDel.prev;
        GLUface fNext = fDel.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }

    public static GLUhalfEdge __gl_meshMakeEdge(GLUmesh mesh) {
        GLUvertex newVertex1 = new GLUvertex();
        GLUvertex newVertex2 = new GLUvertex();
        GLUface newFace = new GLUface();
        GLUhalfEdge e = Mesh.MakeEdge(mesh.eHead);
        if (e == null) {
            return null;
        }
        Mesh.MakeVertex(newVertex1, e, mesh.vHead);
        Mesh.MakeVertex(newVertex2, e.Sym, mesh.vHead);
        Mesh.MakeFace(newFace, e, mesh.fHead);
        return e;
    }

    public static boolean __gl_meshSplice(GLUhalfEdge eOrg, GLUhalfEdge eDst) {
        boolean joiningLoops = false;
        boolean joiningVertices = false;
        if (eOrg == eDst) {
            return true;
        }
        if (eDst.Org != eOrg.Org) {
            joiningVertices = true;
            Mesh.KillVertex(eDst.Org, eOrg.Org);
        }
        if (eDst.Lface != eOrg.Lface) {
            joiningLoops = true;
            Mesh.KillFace(eDst.Lface, eOrg.Lface);
        }
        Mesh.Splice(eDst, eOrg);
        if (!joiningVertices) {
            GLUvertex newVertex = new GLUvertex();
            Mesh.MakeVertex(newVertex, eDst, eOrg.Org);
            eOrg.Org.anEdge = eOrg;
        }
        if (!joiningLoops) {
            GLUface newFace = new GLUface();
            Mesh.MakeFace(newFace, eDst, eOrg.Lface);
            eOrg.Lface.anEdge = eOrg;
        }
        return true;
    }

    static boolean __gl_meshDelete(GLUhalfEdge eDel) {
        GLUhalfEdge eDelSym = eDel.Sym;
        boolean joiningLoops = false;
        if (eDel.Lface != eDel.Sym.Lface) {
            joiningLoops = true;
            Mesh.KillFace(eDel.Lface, eDel.Sym.Lface);
        }
        if (eDel.Onext == eDel) {
            Mesh.KillVertex(eDel.Org, null);
        } else {
            eDel.Sym.Lface.anEdge = eDel.Sym.Lnext;
            eDel.Org.anEdge = eDel.Onext;
            Mesh.Splice(eDel, eDel.Sym.Lnext);
            if (!joiningLoops) {
                GLUface newFace = new GLUface();
                Mesh.MakeFace(newFace, eDel, eDel.Lface);
            }
        }
        if (eDelSym.Onext == eDelSym) {
            Mesh.KillVertex(eDelSym.Org, null);
            Mesh.KillFace(eDelSym.Lface, null);
        } else {
            eDel.Lface.anEdge = eDelSym.Sym.Lnext;
            eDelSym.Org.anEdge = eDelSym.Onext;
            Mesh.Splice(eDelSym, eDelSym.Sym.Lnext);
        }
        Mesh.KillEdge(eDel);
        return true;
    }

    static GLUhalfEdge __gl_meshAddEdgeVertex(GLUhalfEdge eOrg) {
        GLUhalfEdge eNew = Mesh.MakeEdge(eOrg);
        GLUhalfEdge eNewSym = eNew.Sym;
        Mesh.Splice(eNew, eOrg.Lnext);
        eNew.Org = eOrg.Sym.Org;
        GLUvertex newVertex = new GLUvertex();
        Mesh.MakeVertex(newVertex, eNewSym, eNew.Org);
        eNew.Lface = eNewSym.Lface = eOrg.Lface;
        return eNew;
    }

    public static GLUhalfEdge __gl_meshSplitEdge(GLUhalfEdge eOrg) {
        GLUhalfEdge tempHalfEdge = Mesh.__gl_meshAddEdgeVertex(eOrg);
        GLUhalfEdge eNew = tempHalfEdge.Sym;
        Mesh.Splice(eOrg.Sym, eOrg.Sym.Sym.Lnext);
        Mesh.Splice(eOrg.Sym, eNew);
        eOrg.Sym.Org = eNew.Org;
        eNew.Sym.Org.anEdge = eNew.Sym;
        eNew.Sym.Lface = eOrg.Sym.Lface;
        eNew.winding = eOrg.winding;
        eNew.Sym.winding = eOrg.Sym.winding;
        return eNew;
    }

    static GLUhalfEdge __gl_meshConnect(GLUhalfEdge eOrg, GLUhalfEdge eDst) {
        boolean joiningLoops = false;
        GLUhalfEdge eNew = Mesh.MakeEdge(eOrg);
        GLUhalfEdge eNewSym = eNew.Sym;
        if (eDst.Lface != eOrg.Lface) {
            joiningLoops = true;
            Mesh.KillFace(eDst.Lface, eOrg.Lface);
        }
        Mesh.Splice(eNew, eOrg.Lnext);
        Mesh.Splice(eNewSym, eDst);
        eNew.Org = eOrg.Sym.Org;
        eNewSym.Org = eDst.Org;
        eNew.Lface = eNewSym.Lface = eOrg.Lface;
        eOrg.Lface.anEdge = eNewSym;
        if (!joiningLoops) {
            GLUface newFace = new GLUface();
            Mesh.MakeFace(newFace, eNew, eOrg.Lface);
        }
        return eNew;
    }

    static void __gl_meshZapFace(GLUface fZap) {
        GLUhalfEdge e;
        GLUhalfEdge eStart = fZap.anEdge;
        GLUhalfEdge eNext = eStart.Lnext;
        do {
            e = eNext;
            eNext = e.Lnext;
            e.Lface = null;
            if (e.Sym.Lface != null) continue;
            if (e.Onext == e) {
                Mesh.KillVertex(e.Org, null);
            } else {
                e.Org.anEdge = e.Onext;
                Mesh.Splice(e, e.Sym.Lnext);
            }
            GLUhalfEdge eSym = e.Sym;
            if (eSym.Onext == eSym) {
                Mesh.KillVertex(eSym.Org, null);
            } else {
                eSym.Org.anEdge = eSym.Onext;
                Mesh.Splice(eSym, eSym.Sym.Lnext);
            }
            Mesh.KillEdge(e);
        } while (e != eStart);
        GLUface fPrev = fZap.prev;
        GLUface fNext = fZap.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }

    public static GLUmesh __gl_meshNewMesh() {
        GLUmesh mesh = new GLUmesh();
        GLUvertex v = mesh.vHead;
        GLUface f = mesh.fHead;
        GLUhalfEdge e = mesh.eHead;
        GLUhalfEdge eSym = mesh.eHeadSym;
        v.next = v.prev = v;
        v.anEdge = null;
        v.data = null;
        f.next = f.prev = f;
        f.anEdge = null;
        f.data = null;
        f.trail = null;
        f.marked = false;
        f.inside = false;
        e.next = e;
        e.Sym = eSym;
        e.Onext = null;
        e.Lnext = null;
        e.Org = null;
        e.Lface = null;
        e.winding = 0;
        e.activeRegion = null;
        eSym.next = eSym;
        eSym.Sym = e;
        eSym.Onext = null;
        eSym.Lnext = null;
        eSym.Org = null;
        eSym.Lface = null;
        eSym.winding = 0;
        eSym.activeRegion = null;
        return mesh;
    }

    static GLUmesh __gl_meshUnion(GLUmesh mesh1, GLUmesh mesh2) {
        GLUface f1 = mesh1.fHead;
        GLUvertex v1 = mesh1.vHead;
        GLUhalfEdge e1 = mesh1.eHead;
        GLUface f2 = mesh2.fHead;
        GLUvertex v2 = mesh2.vHead;
        GLUhalfEdge e2 = mesh2.eHead;
        if (f2.next != f2) {
            f1.prev.next = f2.next;
            f2.next.prev = f1.prev;
            f2.prev.next = f1;
            f1.prev = f2.prev;
        }
        if (v2.next != v2) {
            v1.prev.next = v2.next;
            v2.next.prev = v1.prev;
            v2.prev.next = v1;
            v1.prev = v2.prev;
        }
        if (e2.next != e2) {
            e1.Sym.next.Sym.next = e2.next;
            e2.next.Sym.next = e1.Sym.next;
            e2.Sym.next.Sym.next = e1;
            e1.Sym.next = e2.Sym.next;
        }
        return mesh1;
    }

    static void __gl_meshDeleteMeshZap(GLUmesh mesh) {
        GLUface fHead = mesh.fHead;
        while (fHead.next != fHead) {
            Mesh.__gl_meshZapFace(fHead.next);
        }
        assert (mesh.vHead.next == mesh.vHead);
    }

    public static void __gl_meshDeleteMesh(GLUmesh mesh) {
        GLUface f = mesh.fHead.next;
        while (f != mesh.fHead) {
            GLUface fNext;
            f = fNext = f.next;
        }
        GLUvertex v = mesh.vHead.next;
        while (v != mesh.vHead) {
            GLUvertex vNext;
            v = vNext = v.next;
        }
        GLUhalfEdge e = mesh.eHead.next;
        while (e != mesh.eHead) {
            GLUhalfEdge eNext;
            e = eNext = e.next;
        }
    }

    public static void __gl_meshCheckMesh(GLUmesh mesh) {
        GLUvertex v;
        GLUhalfEdge e;
        GLUface f;
        GLUface fHead = mesh.fHead;
        GLUvertex vHead = mesh.vHead;
        GLUhalfEdge eHead = mesh.eHead;
        GLUface fPrev = fHead;
        fPrev = fHead;
        while ((f = fPrev.next) != fHead) {
            assert (f.prev == fPrev);
            e = f.anEdge;
            do {
                assert (e.Sym != e);
                assert (e.Sym.Sym == e);
                assert (e.Lnext.Onext.Sym == e);
                assert (e.Onext.Sym.Lnext == e);
                assert (e.Lface == f);
            } while ((e = e.Lnext) != f.anEdge);
            fPrev = f;
        }
        assert (f.prev == fPrev && f.anEdge == null && f.data == null);
        GLUvertex vPrev = vHead;
        vPrev = vHead;
        while ((v = vPrev.next) != vHead) {
            assert (v.prev == vPrev);
            e = v.anEdge;
            do {
                assert (e.Sym != e);
                assert (e.Sym.Sym == e);
                assert (e.Lnext.Onext.Sym == e);
                assert (e.Onext.Sym.Lnext == e);
                assert (e.Org == v);
            } while ((e = e.Onext) != v.anEdge);
            vPrev = v;
        }
        assert (v.prev == vPrev && v.anEdge == null && v.data == null);
        GLUhalfEdge ePrev = eHead;
        ePrev = eHead;
        while ((e = ePrev.next) != eHead) {
            assert (e.Sym.next == ePrev.Sym);
            assert (e.Sym != e);
            assert (e.Sym.Sym == e);
            assert (e.Org != null);
            assert (e.Sym.Org != null);
            assert (e.Lnext.Onext.Sym == e);
            assert (e.Onext.Sym.Lnext == e);
            ePrev = e;
        }
        assert (e.Sym.next == ePrev.Sym && e.Sym == mesh.eHeadSym && e.Sym.Sym == e && e.Org == null && e.Sym.Org == null && e.Lface == null && e.Sym.Lface == null);
    }
}

