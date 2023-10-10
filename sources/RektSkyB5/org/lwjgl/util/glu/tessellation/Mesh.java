/*
 * Decompiled with CFR 0.152.
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
        GLUhalfEdge e2 = new GLUhalfEdge(true);
        GLUhalfEdge eSym = new GLUhalfEdge(false);
        if (!eNext.first) {
            eNext = eNext.Sym;
        }
        eSym.next = ePrev = eNext.Sym.next;
        ePrev.Sym.next = e2;
        e2.next = eNext;
        eNext.Sym.next = eSym;
        e2.Sym = eSym;
        e2.Onext = e2;
        e2.Lnext = eSym;
        e2.Org = null;
        e2.Lface = null;
        e2.winding = 0;
        e2.activeRegion = null;
        eSym.Sym = e2;
        eSym.Onext = eSym;
        eSym.Lnext = e2;
        eSym.Org = null;
        eSym.Lface = null;
        eSym.winding = 0;
        eSym.activeRegion = null;
        return e2;
    }

    static void Splice(GLUhalfEdge a2, GLUhalfEdge b2) {
        GLUhalfEdge aOnext = a2.Onext;
        GLUhalfEdge bOnext = b2.Onext;
        aOnext.Sym.Lnext = b2;
        bOnext.Sym.Lnext = a2;
        a2.Onext = bOnext;
        b2.Onext = aOnext;
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
        GLUhalfEdge e2 = eOrig;
        do {
            e2.Org = vNew;
        } while ((e2 = e2.Onext) != eOrig);
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
        GLUhalfEdge e2 = eOrig;
        do {
            e2.Lface = fNew;
        } while ((e2 = e2.Lnext) != eOrig);
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
        GLUhalfEdge e2 = eStart = vDel.anEdge;
        do {
            e2.Org = newOrg;
        } while ((e2 = e2.Onext) != eStart);
        GLUvertex vPrev = vDel.prev;
        GLUvertex vNext = vDel.next;
        vNext.prev = vPrev;
        vPrev.next = vNext;
    }

    static void KillFace(GLUface fDel, GLUface newLface) {
        GLUhalfEdge eStart;
        GLUhalfEdge e2 = eStart = fDel.anEdge;
        do {
            e2.Lface = newLface;
        } while ((e2 = e2.Lnext) != eStart);
        GLUface fPrev = fDel.prev;
        GLUface fNext = fDel.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }

    public static GLUhalfEdge __gl_meshMakeEdge(GLUmesh mesh) {
        GLUvertex newVertex1 = new GLUvertex();
        GLUvertex newVertex2 = new GLUvertex();
        GLUface newFace = new GLUface();
        GLUhalfEdge e2 = Mesh.MakeEdge(mesh.eHead);
        if (e2 == null) {
            return null;
        }
        Mesh.MakeVertex(newVertex1, e2, mesh.vHead);
        Mesh.MakeVertex(newVertex2, e2.Sym, mesh.vHead);
        Mesh.MakeFace(newFace, e2, mesh.fHead);
        return e2;
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
        GLUhalfEdge e2;
        GLUhalfEdge eStart = fZap.anEdge;
        GLUhalfEdge eNext = eStart.Lnext;
        do {
            e2 = eNext;
            eNext = e2.Lnext;
            e2.Lface = null;
            if (e2.Sym.Lface != null) continue;
            if (e2.Onext == e2) {
                Mesh.KillVertex(e2.Org, null);
            } else {
                e2.Org.anEdge = e2.Onext;
                Mesh.Splice(e2, e2.Sym.Lnext);
            }
            GLUhalfEdge eSym = e2.Sym;
            if (eSym.Onext == eSym) {
                Mesh.KillVertex(eSym.Org, null);
            } else {
                eSym.Org.anEdge = eSym.Onext;
                Mesh.Splice(eSym, eSym.Sym.Lnext);
            }
            Mesh.KillEdge(e2);
        } while (e2 != eStart);
        GLUface fPrev = fZap.prev;
        GLUface fNext = fZap.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }

    public static GLUmesh __gl_meshNewMesh() {
        GLUmesh mesh = new GLUmesh();
        GLUvertex v2 = mesh.vHead;
        GLUface f2 = mesh.fHead;
        GLUhalfEdge e2 = mesh.eHead;
        GLUhalfEdge eSym = mesh.eHeadSym;
        v2.next = v2.prev = v2;
        v2.anEdge = null;
        v2.data = null;
        f2.next = f2.prev = f2;
        f2.anEdge = null;
        f2.data = null;
        f2.trail = null;
        f2.marked = false;
        f2.inside = false;
        e2.next = e2;
        e2.Sym = eSym;
        e2.Onext = null;
        e2.Lnext = null;
        e2.Org = null;
        e2.Lface = null;
        e2.winding = 0;
        e2.activeRegion = null;
        eSym.next = eSym;
        eSym.Sym = e2;
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
        GLUface f2 = mesh.fHead.next;
        while (f2 != mesh.fHead) {
            GLUface fNext;
            f2 = fNext = f2.next;
        }
        GLUvertex v2 = mesh.vHead.next;
        while (v2 != mesh.vHead) {
            GLUvertex vNext;
            v2 = vNext = v2.next;
        }
        GLUhalfEdge e2 = mesh.eHead.next;
        while (e2 != mesh.eHead) {
            GLUhalfEdge eNext;
            e2 = eNext = e2.next;
        }
    }

    public static void __gl_meshCheckMesh(GLUmesh mesh) {
        GLUvertex v2;
        GLUhalfEdge e2;
        GLUface f2;
        GLUface fHead = mesh.fHead;
        GLUvertex vHead = mesh.vHead;
        GLUhalfEdge eHead = mesh.eHead;
        GLUface fPrev = fHead;
        fPrev = fHead;
        while ((f2 = fPrev.next) != fHead) {
            assert (f2.prev == fPrev);
            e2 = f2.anEdge;
            do {
                assert (e2.Sym != e2);
                assert (e2.Sym.Sym == e2);
                assert (e2.Lnext.Onext.Sym == e2);
                assert (e2.Onext.Sym.Lnext == e2);
                assert (e2.Lface == f2);
            } while ((e2 = e2.Lnext) != f2.anEdge);
            fPrev = f2;
        }
        assert (f2.prev == fPrev && f2.anEdge == null && f2.data == null);
        GLUvertex vPrev = vHead;
        vPrev = vHead;
        while ((v2 = vPrev.next) != vHead) {
            assert (v2.prev == vPrev);
            e2 = v2.anEdge;
            do {
                assert (e2.Sym != e2);
                assert (e2.Sym.Sym == e2);
                assert (e2.Lnext.Onext.Sym == e2);
                assert (e2.Onext.Sym.Lnext == e2);
                assert (e2.Org == v2);
            } while ((e2 = e2.Onext) != v2.anEdge);
            vPrev = v2;
        }
        assert (v2.prev == vPrev && v2.anEdge == null && v2.data == null);
        GLUhalfEdge ePrev = eHead;
        ePrev = eHead;
        while ((e2 = ePrev.next) != eHead) {
            assert (e2.Sym.next == ePrev.Sym);
            assert (e2.Sym != e2);
            assert (e2.Sym.Sym == e2);
            assert (e2.Org != null);
            assert (e2.Sym.Org != null);
            assert (e2.Lnext.Onext.Sym == e2);
            assert (e2.Onext.Sym.Lnext == e2);
            ePrev = e2;
        }
        assert (e2.Sym.next == ePrev.Sym && e2.Sym == mesh.eHeadSym && e2.Sym.Sym == e2 && e2.Org == null && e2.Sym.Org == null && e2.Lface == null && e2.Sym.Lface == null);
    }
}

