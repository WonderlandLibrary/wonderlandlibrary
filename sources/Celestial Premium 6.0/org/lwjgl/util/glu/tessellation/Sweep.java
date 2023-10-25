/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.ActiveRegion;
import org.lwjgl.util.glu.tessellation.Dict;
import org.lwjgl.util.glu.tessellation.DictNode;
import org.lwjgl.util.glu.tessellation.GLUface;
import org.lwjgl.util.glu.tessellation.GLUhalfEdge;
import org.lwjgl.util.glu.tessellation.GLUmesh;
import org.lwjgl.util.glu.tessellation.GLUtessellatorImpl;
import org.lwjgl.util.glu.tessellation.GLUvertex;
import org.lwjgl.util.glu.tessellation.Geom;
import org.lwjgl.util.glu.tessellation.Mesh;
import org.lwjgl.util.glu.tessellation.PriorityQ;

class Sweep {
    private static final boolean TOLERANCE_NONZERO = false;
    private static final double SENTINEL_COORD = 4.0E150;

    private Sweep() {
    }

    private static void DebugEvent(GLUtessellatorImpl tess) {
    }

    private static void AddWinding(GLUhalfEdge eDst, GLUhalfEdge eSrc) {
        eDst.winding += eSrc.winding;
        eDst.Sym.winding += eSrc.Sym.winding;
    }

    private static ActiveRegion RegionBelow(ActiveRegion r) {
        return (ActiveRegion)Dict.dictKey(Dict.dictPred(r.nodeUp));
    }

    private static ActiveRegion RegionAbove(ActiveRegion r) {
        return (ActiveRegion)Dict.dictKey(Dict.dictSucc(r.nodeUp));
    }

    static boolean EdgeLeq(GLUtessellatorImpl tess, ActiveRegion reg1, ActiveRegion reg2) {
        double t2;
        GLUvertex event = tess.event;
        GLUhalfEdge e1 = reg1.eUp;
        GLUhalfEdge e2 = reg2.eUp;
        if (e1.Sym.Org == event) {
            if (e2.Sym.Org == event) {
                if (Geom.VertLeq(e1.Org, e2.Org)) {
                    return Geom.EdgeSign(e2.Sym.Org, e1.Org, e2.Org) <= 0.0;
                }
                return Geom.EdgeSign(e1.Sym.Org, e2.Org, e1.Org) >= 0.0;
            }
            return Geom.EdgeSign(e2.Sym.Org, event, e2.Org) <= 0.0;
        }
        if (e2.Sym.Org == event) {
            return Geom.EdgeSign(e1.Sym.Org, event, e1.Org) >= 0.0;
        }
        double t1 = Geom.EdgeEval(e1.Sym.Org, event, e1.Org);
        return t1 >= (t2 = Geom.EdgeEval(e2.Sym.Org, event, e2.Org));
    }

    static void DeleteRegion(GLUtessellatorImpl tess, ActiveRegion reg) {
        if (reg.fixUpperEdge && !$assertionsDisabled && reg.eUp.winding != 0) {
            throw new AssertionError();
        }
        reg.eUp.activeRegion = null;
        Dict.dictDelete(tess.dict, reg.nodeUp);
    }

    static boolean FixUpperEdge(ActiveRegion reg, GLUhalfEdge newEdge) {
        assert (reg.fixUpperEdge);
        if (!Mesh.__gl_meshDelete(reg.eUp)) {
            return false;
        }
        reg.fixUpperEdge = false;
        reg.eUp = newEdge;
        newEdge.activeRegion = reg;
        return true;
    }

    static ActiveRegion TopLeftRegion(ActiveRegion reg) {
        GLUvertex org = reg.eUp.Org;
        do {
            reg = Sweep.RegionAbove(reg);
        } while (reg.eUp.Org == org);
        if (reg.fixUpperEdge) {
            GLUhalfEdge e = Mesh.__gl_meshConnect(Sweep.RegionBelow((ActiveRegion)reg).eUp.Sym, reg.eUp.Lnext);
            if (e == null) {
                return null;
            }
            if (!Sweep.FixUpperEdge(reg, e)) {
                return null;
            }
            reg = Sweep.RegionAbove(reg);
        }
        return reg;
    }

    static ActiveRegion TopRightRegion(ActiveRegion reg) {
        GLUvertex dst = reg.eUp.Sym.Org;
        do {
            reg = Sweep.RegionAbove(reg);
        } while (reg.eUp.Sym.Org == dst);
        return reg;
    }

    static ActiveRegion AddRegionBelow(GLUtessellatorImpl tess, ActiveRegion regAbove, GLUhalfEdge eNewUp) {
        ActiveRegion regNew = new ActiveRegion();
        if (regNew == null) {
            throw new RuntimeException();
        }
        regNew.eUp = eNewUp;
        regNew.nodeUp = Dict.dictInsertBefore(tess.dict, regAbove.nodeUp, regNew);
        if (regNew.nodeUp == null) {
            throw new RuntimeException();
        }
        regNew.fixUpperEdge = false;
        regNew.sentinel = false;
        regNew.dirty = false;
        eNewUp.activeRegion = regNew;
        return regNew;
    }

    static boolean IsWindingInside(GLUtessellatorImpl tess, int n) {
        switch (tess.windingRule) {
            case 100130: {
                return (n & 1) != 0;
            }
            case 100131: {
                return n != 0;
            }
            case 100132: {
                return n > 0;
            }
            case 100133: {
                return n < 0;
            }
            case 100134: {
                return n >= 2 || n <= -2;
            }
        }
        throw new InternalError();
    }

    static void ComputeWinding(GLUtessellatorImpl tess, ActiveRegion reg) {
        reg.windingNumber = Sweep.RegionAbove((ActiveRegion)reg).windingNumber + reg.eUp.winding;
        reg.inside = Sweep.IsWindingInside(tess, reg.windingNumber);
    }

    static void FinishRegion(GLUtessellatorImpl tess, ActiveRegion reg) {
        GLUhalfEdge e = reg.eUp;
        GLUface f = e.Lface;
        f.inside = reg.inside;
        f.anEdge = e;
        Sweep.DeleteRegion(tess, reg);
    }

    static GLUhalfEdge FinishLeftRegions(GLUtessellatorImpl tess, ActiveRegion regFirst, ActiveRegion regLast) {
        ActiveRegion regPrev = regFirst;
        GLUhalfEdge ePrev = regFirst.eUp;
        while (regPrev != regLast) {
            regPrev.fixUpperEdge = false;
            ActiveRegion reg = Sweep.RegionBelow(regPrev);
            GLUhalfEdge e = reg.eUp;
            if (e.Org != ePrev.Org) {
                if (!reg.fixUpperEdge) {
                    Sweep.FinishRegion(tess, regPrev);
                    break;
                }
                e = Mesh.__gl_meshConnect(ePrev.Onext.Sym, e.Sym);
                if (e == null) {
                    throw new RuntimeException();
                }
                if (!Sweep.FixUpperEdge(reg, e)) {
                    throw new RuntimeException();
                }
            }
            if (ePrev.Onext != e) {
                if (!Mesh.__gl_meshSplice(e.Sym.Lnext, e)) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(ePrev, e)) {
                    throw new RuntimeException();
                }
            }
            Sweep.FinishRegion(tess, regPrev);
            ePrev = reg.eUp;
            regPrev = reg;
        }
        return ePrev;
    }

    static void AddRightEdges(GLUtessellatorImpl tess, ActiveRegion regUp, GLUhalfEdge eFirst, GLUhalfEdge eLast, GLUhalfEdge eTopLeft, boolean cleanUp) {
        ActiveRegion reg;
        boolean firstTime = true;
        GLUhalfEdge e = eFirst;
        do {
            assert (Geom.VertLeq(e.Org, e.Sym.Org));
            Sweep.AddRegionBelow(tess, regUp, e.Sym);
        } while ((e = e.Onext) != eLast);
        if (eTopLeft == null) {
            eTopLeft = Sweep.RegionBelow((ActiveRegion)regUp).eUp.Sym.Onext;
        }
        ActiveRegion regPrev = regUp;
        GLUhalfEdge ePrev = eTopLeft;
        while (true) {
            reg = Sweep.RegionBelow(regPrev);
            e = reg.eUp.Sym;
            if (e.Org != ePrev.Org) break;
            if (e.Onext != ePrev) {
                if (!Mesh.__gl_meshSplice(e.Sym.Lnext, e)) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(ePrev.Sym.Lnext, e)) {
                    throw new RuntimeException();
                }
            }
            reg.windingNumber = regPrev.windingNumber - e.winding;
            reg.inside = Sweep.IsWindingInside(tess, reg.windingNumber);
            regPrev.dirty = true;
            if (!firstTime && Sweep.CheckForRightSplice(tess, regPrev)) {
                Sweep.AddWinding(e, ePrev);
                Sweep.DeleteRegion(tess, regPrev);
                if (!Mesh.__gl_meshDelete(ePrev)) {
                    throw new RuntimeException();
                }
            }
            firstTime = false;
            regPrev = reg;
            ePrev = e;
        }
        regPrev.dirty = true;
        assert (regPrev.windingNumber - e.winding == reg.windingNumber);
        if (cleanUp) {
            Sweep.WalkDirtyRegions(tess, regPrev);
        }
    }

    static void CallCombine(GLUtessellatorImpl tess, GLUvertex isect, Object[] data, float[] weights, boolean needed) {
        double[] coords = new double[]{isect.coords[0], isect.coords[1], isect.coords[2]};
        Object[] outData = new Object[1];
        tess.callCombineOrCombineData(coords, data, weights, outData);
        isect.data = outData[0];
        if (isect.data == null) {
            if (!needed) {
                isect.data = data[0];
            } else if (!tess.fatalError) {
                tess.callErrorOrErrorData(100156);
                tess.fatalError = true;
            }
        }
    }

    static void SpliceMergeVertices(GLUtessellatorImpl tess, GLUhalfEdge e1, GLUhalfEdge e2) {
        Object[] data = new Object[4];
        float[] weights = new float[]{0.5f, 0.5f, 0.0f, 0.0f};
        data[0] = e1.Org.data;
        data[1] = e2.Org.data;
        Sweep.CallCombine(tess, e1.Org, data, weights, false);
        if (!Mesh.__gl_meshSplice(e1, e2)) {
            throw new RuntimeException();
        }
    }

    static void VertexWeights(GLUvertex isect, GLUvertex org, GLUvertex dst, float[] weights) {
        double t1 = Geom.VertL1dist(org, isect);
        double t2 = Geom.VertL1dist(dst, isect);
        weights[0] = (float)(0.5 * t2 / (t1 + t2));
        weights[1] = (float)(0.5 * t1 / (t1 + t2));
        isect.coords[0] = isect.coords[0] + ((double)weights[0] * org.coords[0] + (double)weights[1] * dst.coords[0]);
        isect.coords[1] = isect.coords[1] + ((double)weights[0] * org.coords[1] + (double)weights[1] * dst.coords[1]);
        isect.coords[2] = isect.coords[2] + ((double)weights[0] * org.coords[2] + (double)weights[1] * dst.coords[2]);
    }

    static void GetIntersectData(GLUtessellatorImpl tess, GLUvertex isect, GLUvertex orgUp, GLUvertex dstUp, GLUvertex orgLo, GLUvertex dstLo) {
        Object[] data = new Object[4];
        float[] weights = new float[4];
        float[] weights1 = new float[2];
        float[] weights2 = new float[2];
        data[0] = orgUp.data;
        data[1] = dstUp.data;
        data[2] = orgLo.data;
        data[3] = dstLo.data;
        isect.coords[2] = 0.0;
        isect.coords[1] = 0.0;
        isect.coords[0] = 0.0;
        Sweep.VertexWeights(isect, orgUp, dstUp, weights1);
        Sweep.VertexWeights(isect, orgLo, dstLo, weights2);
        System.arraycopy(weights1, 0, weights, 0, 2);
        System.arraycopy(weights2, 0, weights, 2, 2);
        Sweep.CallCombine(tess, isect, data, weights, true);
    }

    static boolean CheckForRightSplice(GLUtessellatorImpl tess, ActiveRegion regUp) {
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        if (Geom.VertLeq(eUp.Org, eLo.Org)) {
            if (Geom.EdgeSign(eLo.Sym.Org, eUp.Org, eLo.Org) > 0.0) {
                return false;
            }
            if (!Geom.VertEq(eUp.Org, eLo.Org)) {
                if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eUp, eLo.Sym.Lnext)) {
                    throw new RuntimeException();
                }
                regLo.dirty = true;
                regUp.dirty = true;
            } else if (eUp.Org != eLo.Org) {
                tess.pq.pqDelete(eUp.Org.pqHandle);
                Sweep.SpliceMergeVertices(tess, eLo.Sym.Lnext, eUp);
            }
        } else {
            if (Geom.EdgeSign(eUp.Sym.Org, eLo.Org, eUp.Org) < 0.0) {
                return false;
            }
            regUp.dirty = true;
            Sweep.RegionAbove((ActiveRegion)regUp).dirty = true;
            if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eLo.Sym.Lnext, eUp)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    static boolean CheckForLeftSplice(GLUtessellatorImpl tess, ActiveRegion regUp) {
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        assert (!Geom.VertEq(eUp.Sym.Org, eLo.Sym.Org));
        if (Geom.VertLeq(eUp.Sym.Org, eLo.Sym.Org)) {
            if (Geom.EdgeSign(eUp.Sym.Org, eLo.Sym.Org, eUp.Org) < 0.0) {
                return false;
            }
            regUp.dirty = true;
            Sweep.RegionAbove((ActiveRegion)regUp).dirty = true;
            GLUhalfEdge e = Mesh.__gl_meshSplitEdge(eUp);
            if (e == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eLo.Sym, e)) {
                throw new RuntimeException();
            }
            e.Lface.inside = regUp.inside;
        } else {
            if (Geom.EdgeSign(eLo.Sym.Org, eUp.Sym.Org, eLo.Org) > 0.0) {
                return false;
            }
            regLo.dirty = true;
            regUp.dirty = true;
            GLUhalfEdge e = Mesh.__gl_meshSplitEdge(eLo);
            if (e == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eUp.Lnext, eLo.Sym)) {
                throw new RuntimeException();
            }
            e.Sym.Lface.inside = regUp.inside;
        }
        return true;
    }

    static boolean CheckForIntersect(GLUtessellatorImpl tess, ActiveRegion regUp) {
        GLUvertex orgMin;
        double tMaxLo;
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        GLUvertex orgUp = eUp.Org;
        GLUvertex orgLo = eLo.Org;
        GLUvertex dstUp = eUp.Sym.Org;
        GLUvertex dstLo = eLo.Sym.Org;
        GLUvertex isect = new GLUvertex();
        assert (!Geom.VertEq(dstLo, dstUp));
        assert (Geom.EdgeSign(dstUp, tess.event, orgUp) <= 0.0);
        assert (Geom.EdgeSign(dstLo, tess.event, orgLo) >= 0.0);
        assert (orgUp != tess.event && orgLo != tess.event);
        assert (!regUp.fixUpperEdge && !regLo.fixUpperEdge);
        if (orgUp == orgLo) {
            return false;
        }
        double tMinUp = Math.min(orgUp.t, dstUp.t);
        if (tMinUp > (tMaxLo = Math.max(orgLo.t, dstLo.t))) {
            return false;
        }
        if (Geom.VertLeq(orgUp, orgLo) ? Geom.EdgeSign(dstLo, orgUp, orgLo) > 0.0 : Geom.EdgeSign(dstUp, orgLo, orgUp) < 0.0) {
            return false;
        }
        Sweep.DebugEvent(tess);
        Geom.EdgeIntersect(dstUp, orgUp, dstLo, orgLo, isect);
        assert (Math.min(orgUp.t, dstUp.t) <= isect.t);
        assert (isect.t <= Math.max(orgLo.t, dstLo.t));
        assert (Math.min(dstLo.s, dstUp.s) <= isect.s);
        assert (isect.s <= Math.max(orgLo.s, orgUp.s));
        if (Geom.VertLeq(isect, tess.event)) {
            isect.s = tess.event.s;
            isect.t = tess.event.t;
        }
        GLUvertex gLUvertex = orgMin = Geom.VertLeq(orgUp, orgLo) ? orgUp : orgLo;
        if (Geom.VertLeq(orgMin, isect)) {
            isect.s = orgMin.s;
            isect.t = orgMin.t;
        }
        if (Geom.VertEq(isect, orgUp) || Geom.VertEq(isect, orgLo)) {
            Sweep.CheckForRightSplice(tess, regUp);
            return false;
        }
        if (!Geom.VertEq(dstUp, tess.event) && Geom.EdgeSign(dstUp, tess.event, isect) >= 0.0 || !Geom.VertEq(dstLo, tess.event) && Geom.EdgeSign(dstLo, tess.event, isect) <= 0.0) {
            if (dstLo == tess.event) {
                if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eLo.Sym, eUp)) {
                    throw new RuntimeException();
                }
                if ((regUp = Sweep.TopLeftRegion(regUp)) == null) {
                    throw new RuntimeException();
                }
                eUp = Sweep.RegionBelow((ActiveRegion)regUp).eUp;
                Sweep.FinishLeftRegions(tess, Sweep.RegionBelow(regUp), regLo);
                Sweep.AddRightEdges(tess, regUp, eUp.Sym.Lnext, eUp, eUp, true);
                return true;
            }
            if (dstUp == tess.event) {
                if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eUp.Lnext, eLo.Sym.Lnext)) {
                    throw new RuntimeException();
                }
                regLo = regUp;
                regUp = Sweep.TopRightRegion(regUp);
                GLUhalfEdge e = Sweep.RegionBelow((ActiveRegion)regUp).eUp.Sym.Onext;
                regLo.eUp = eLo.Sym.Lnext;
                eLo = Sweep.FinishLeftRegions(tess, regLo, null);
                Sweep.AddRightEdges(tess, regUp, eLo.Onext, eUp.Sym.Onext, e, true);
                return true;
            }
            if (Geom.EdgeSign(dstUp, tess.event, isect) >= 0.0) {
                regUp.dirty = true;
                Sweep.RegionAbove((ActiveRegion)regUp).dirty = true;
                if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                    throw new RuntimeException();
                }
                eUp.Org.s = tess.event.s;
                eUp.Org.t = tess.event.t;
            }
            if (Geom.EdgeSign(dstLo, tess.event, isect) <= 0.0) {
                regLo.dirty = true;
                regUp.dirty = true;
                if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                    throw new RuntimeException();
                }
                eLo.Org.s = tess.event.s;
                eLo.Org.t = tess.event.t;
            }
            return false;
        }
        if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
            throw new RuntimeException();
        }
        if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
            throw new RuntimeException();
        }
        if (!Mesh.__gl_meshSplice(eLo.Sym.Lnext, eUp)) {
            throw new RuntimeException();
        }
        eUp.Org.s = isect.s;
        eUp.Org.t = isect.t;
        eUp.Org.pqHandle = tess.pq.pqInsert(eUp.Org);
        if ((long)eUp.Org.pqHandle == Long.MAX_VALUE) {
            tess.pq.pqDeletePriorityQ();
            tess.pq = null;
            throw new RuntimeException();
        }
        Sweep.GetIntersectData(tess, eUp.Org, orgUp, dstUp, orgLo, dstLo);
        regLo.dirty = true;
        regUp.dirty = true;
        Sweep.RegionAbove((ActiveRegion)regUp).dirty = true;
        return false;
    }

    static void WalkDirtyRegions(GLUtessellatorImpl tess, ActiveRegion regUp) {
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        while (true) {
            if (regLo.dirty) {
                regUp = regLo;
                regLo = Sweep.RegionBelow(regLo);
                continue;
            }
            if (!regUp.dirty) {
                regLo = regUp;
                if ((regUp = Sweep.RegionAbove(regUp)) == null || !regUp.dirty) {
                    return;
                }
            }
            regUp.dirty = false;
            GLUhalfEdge eUp = regUp.eUp;
            GLUhalfEdge eLo = regLo.eUp;
            if (eUp.Sym.Org != eLo.Sym.Org && Sweep.CheckForLeftSplice(tess, regUp)) {
                if (regLo.fixUpperEdge) {
                    Sweep.DeleteRegion(tess, regLo);
                    if (!Mesh.__gl_meshDelete(eLo)) {
                        throw new RuntimeException();
                    }
                    regLo = Sweep.RegionBelow(regUp);
                    eLo = regLo.eUp;
                } else if (regUp.fixUpperEdge) {
                    Sweep.DeleteRegion(tess, regUp);
                    if (!Mesh.__gl_meshDelete(eUp)) {
                        throw new RuntimeException();
                    }
                    regUp = Sweep.RegionAbove(regLo);
                    eUp = regUp.eUp;
                }
            }
            if (eUp.Org != eLo.Org) {
                if (!(eUp.Sym.Org == eLo.Sym.Org || regUp.fixUpperEdge || regLo.fixUpperEdge || eUp.Sym.Org != tess.event && eLo.Sym.Org != tess.event)) {
                    if (Sweep.CheckForIntersect(tess, regUp)) {
                        return;
                    }
                } else {
                    Sweep.CheckForRightSplice(tess, regUp);
                }
            }
            if (eUp.Org != eLo.Org || eUp.Sym.Org != eLo.Sym.Org) continue;
            Sweep.AddWinding(eLo, eUp);
            Sweep.DeleteRegion(tess, regUp);
            if (!Mesh.__gl_meshDelete(eUp)) {
                throw new RuntimeException();
            }
            regUp = Sweep.RegionAbove(regLo);
        }
    }

    static void ConnectRightVertex(GLUtessellatorImpl tess, ActiveRegion regUp, GLUhalfEdge eBottomLeft) {
        GLUhalfEdge eTopLeft = eBottomLeft.Onext;
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        boolean degenerate = false;
        if (eUp.Sym.Org != eLo.Sym.Org) {
            Sweep.CheckForIntersect(tess, regUp);
        }
        if (Geom.VertEq(eUp.Org, tess.event)) {
            if (!Mesh.__gl_meshSplice(eTopLeft.Sym.Lnext, eUp)) {
                throw new RuntimeException();
            }
            if ((regUp = Sweep.TopLeftRegion(regUp)) == null) {
                throw new RuntimeException();
            }
            eTopLeft = Sweep.RegionBelow((ActiveRegion)regUp).eUp;
            Sweep.FinishLeftRegions(tess, Sweep.RegionBelow(regUp), regLo);
            degenerate = true;
        }
        if (Geom.VertEq(eLo.Org, tess.event)) {
            if (!Mesh.__gl_meshSplice(eBottomLeft, eLo.Sym.Lnext)) {
                throw new RuntimeException();
            }
            eBottomLeft = Sweep.FinishLeftRegions(tess, regLo, null);
            degenerate = true;
        }
        if (degenerate) {
            Sweep.AddRightEdges(tess, regUp, eBottomLeft.Onext, eTopLeft, eTopLeft, true);
            return;
        }
        GLUhalfEdge eNew = Geom.VertLeq(eLo.Org, eUp.Org) ? eLo.Sym.Lnext : eUp;
        if ((eNew = Mesh.__gl_meshConnect(eBottomLeft.Onext.Sym, eNew)) == null) {
            throw new RuntimeException();
        }
        Sweep.AddRightEdges(tess, regUp, eNew, eNew.Onext, eNew.Onext, false);
        eNew.Sym.activeRegion.fixUpperEdge = true;
        Sweep.WalkDirtyRegions(tess, regUp);
    }

    static void ConnectLeftDegenerate(GLUtessellatorImpl tess, ActiveRegion regUp, GLUvertex vEvent) {
        GLUhalfEdge eLast;
        GLUhalfEdge e = regUp.eUp;
        if (Geom.VertEq(e.Org, vEvent)) {
            assert (false);
            Sweep.SpliceMergeVertices(tess, e, vEvent.anEdge);
            return;
        }
        if (!Geom.VertEq(e.Sym.Org, vEvent)) {
            if (Mesh.__gl_meshSplitEdge(e.Sym) == null) {
                throw new RuntimeException();
            }
            if (regUp.fixUpperEdge) {
                if (!Mesh.__gl_meshDelete(e.Onext)) {
                    throw new RuntimeException();
                }
                regUp.fixUpperEdge = false;
            }
            if (!Mesh.__gl_meshSplice(vEvent.anEdge, e)) {
                throw new RuntimeException();
            }
            Sweep.SweepEvent(tess, vEvent);
            return;
        }
        assert (false);
        regUp = Sweep.TopRightRegion(regUp);
        ActiveRegion reg = Sweep.RegionBelow(regUp);
        GLUhalfEdge eTopRight = reg.eUp.Sym;
        GLUhalfEdge eTopLeft = eLast = eTopRight.Onext;
        if (reg.fixUpperEdge) {
            assert (eTopLeft != eTopRight);
            Sweep.DeleteRegion(tess, reg);
            if (!Mesh.__gl_meshDelete(eTopRight)) {
                throw new RuntimeException();
            }
            eTopRight = eTopLeft.Sym.Lnext;
        }
        if (!Mesh.__gl_meshSplice(vEvent.anEdge, eTopRight)) {
            throw new RuntimeException();
        }
        if (!Geom.EdgeGoesLeft(eTopLeft)) {
            eTopLeft = null;
        }
        Sweep.AddRightEdges(tess, regUp, eTopRight.Onext, eLast, eTopLeft, true);
    }

    static void ConnectLeftVertex(GLUtessellatorImpl tess, GLUvertex vEvent) {
        ActiveRegion reg;
        ActiveRegion tmp = new ActiveRegion();
        tmp.eUp = vEvent.anEdge.Sym;
        ActiveRegion regUp = (ActiveRegion)Dict.dictKey(Dict.dictSearch(tess.dict, tmp));
        ActiveRegion regLo = Sweep.RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        if (Geom.EdgeSign(eUp.Sym.Org, vEvent, eUp.Org) == 0.0) {
            Sweep.ConnectLeftDegenerate(tess, regUp, vEvent);
            return;
        }
        ActiveRegion activeRegion = reg = Geom.VertLeq(eLo.Sym.Org, eUp.Sym.Org) ? regUp : regLo;
        if (regUp.inside || reg.fixUpperEdge) {
            GLUhalfEdge eNew;
            if (reg == regUp) {
                eNew = Mesh.__gl_meshConnect(vEvent.anEdge.Sym, eUp.Lnext);
                if (eNew == null) {
                    throw new RuntimeException();
                }
            } else {
                GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(eLo.Sym.Onext.Sym, vEvent.anEdge);
                if (tempHalfEdge == null) {
                    throw new RuntimeException();
                }
                eNew = tempHalfEdge.Sym;
            }
            if (reg.fixUpperEdge) {
                if (!Sweep.FixUpperEdge(reg, eNew)) {
                    throw new RuntimeException();
                }
            } else {
                Sweep.ComputeWinding(tess, Sweep.AddRegionBelow(tess, regUp, eNew));
            }
            Sweep.SweepEvent(tess, vEvent);
        } else {
            Sweep.AddRightEdges(tess, regUp, vEvent.anEdge, vEvent.anEdge, null, true);
        }
    }

    static void SweepEvent(GLUtessellatorImpl tess, GLUvertex vEvent) {
        tess.event = vEvent;
        Sweep.DebugEvent(tess);
        GLUhalfEdge e = vEvent.anEdge;
        while (e.activeRegion == null) {
            e = e.Onext;
            if (e != vEvent.anEdge) continue;
            Sweep.ConnectLeftVertex(tess, vEvent);
            return;
        }
        ActiveRegion regUp = Sweep.TopLeftRegion(e.activeRegion);
        if (regUp == null) {
            throw new RuntimeException();
        }
        ActiveRegion reg = Sweep.RegionBelow(regUp);
        GLUhalfEdge eTopLeft = reg.eUp;
        GLUhalfEdge eBottomLeft = Sweep.FinishLeftRegions(tess, reg, null);
        if (eBottomLeft.Onext == eTopLeft) {
            Sweep.ConnectRightVertex(tess, regUp, eBottomLeft);
        } else {
            Sweep.AddRightEdges(tess, regUp, eBottomLeft.Onext, eTopLeft, eTopLeft, true);
        }
    }

    static void AddSentinel(GLUtessellatorImpl tess, double t) {
        ActiveRegion reg = new ActiveRegion();
        if (reg == null) {
            throw new RuntimeException();
        }
        GLUhalfEdge e = Mesh.__gl_meshMakeEdge(tess.mesh);
        if (e == null) {
            throw new RuntimeException();
        }
        e.Org.s = 4.0E150;
        e.Org.t = t;
        e.Sym.Org.s = -4.0E150;
        e.Sym.Org.t = t;
        tess.event = e.Sym.Org;
        reg.eUp = e;
        reg.windingNumber = 0;
        reg.inside = false;
        reg.fixUpperEdge = false;
        reg.sentinel = true;
        reg.dirty = false;
        reg.nodeUp = Dict.dictInsert(tess.dict, reg);
        if (reg.nodeUp == null) {
            throw new RuntimeException();
        }
    }

    static void InitEdgeDict(final GLUtessellatorImpl tess) {
        tess.dict = Dict.dictNewDict(tess, new Dict.DictLeq(){

            public boolean leq(Object frame, Object key1, Object key2) {
                return Sweep.EdgeLeq(tess, (ActiveRegion)key1, (ActiveRegion)key2);
            }
        });
        if (tess.dict == null) {
            throw new RuntimeException();
        }
        Sweep.AddSentinel(tess, -4.0E150);
        Sweep.AddSentinel(tess, 4.0E150);
    }

    static void DoneEdgeDict(GLUtessellatorImpl tess) {
        ActiveRegion reg;
        int fixedEdges = 0;
        while ((reg = (ActiveRegion)Dict.dictKey(Dict.dictMin(tess.dict))) != null) {
            if (!reg.sentinel) {
                assert (reg.fixUpperEdge);
                assert (++fixedEdges == 1);
            }
            assert (reg.windingNumber == 0);
            Sweep.DeleteRegion(tess, reg);
        }
        Dict.dictDeleteDict(tess.dict);
    }

    static void RemoveDegenerateEdges(GLUtessellatorImpl tess) {
        GLUhalfEdge eHead = tess.mesh.eHead;
        GLUhalfEdge e = eHead.next;
        while (e != eHead) {
            GLUhalfEdge eNext = e.next;
            GLUhalfEdge eLnext = e.Lnext;
            if (Geom.VertEq(e.Org, e.Sym.Org) && e.Lnext.Lnext != e) {
                Sweep.SpliceMergeVertices(tess, eLnext, e);
                if (!Mesh.__gl_meshDelete(e)) {
                    throw new RuntimeException();
                }
                e = eLnext;
                eLnext = e.Lnext;
            }
            if (eLnext.Lnext == e) {
                if (eLnext != e) {
                    if (eLnext == eNext || eLnext == eNext.Sym) {
                        eNext = eNext.next;
                    }
                    if (!Mesh.__gl_meshDelete(eLnext)) {
                        throw new RuntimeException();
                    }
                }
                if (e == eNext || e == eNext.Sym) {
                    eNext = eNext.next;
                }
                if (!Mesh.__gl_meshDelete(e)) {
                    throw new RuntimeException();
                }
            }
            e = eNext;
        }
    }

    static boolean InitPriorityQ(GLUtessellatorImpl tess) {
        tess.pq = PriorityQ.pqNewPriorityQ(new PriorityQ.Leq(){

            public boolean leq(Object key1, Object key2) {
                return Geom.VertLeq((GLUvertex)key1, (GLUvertex)key2);
            }
        });
        PriorityQ pq = tess.pq;
        if (pq == null) {
            return false;
        }
        GLUvertex vHead = tess.mesh.vHead;
        GLUvertex v = vHead.next;
        while (v != vHead) {
            v.pqHandle = pq.pqInsert(v);
            if ((long)v.pqHandle == Long.MAX_VALUE) break;
            v = v.next;
        }
        if (v != vHead || !pq.pqInit()) {
            tess.pq.pqDeletePriorityQ();
            tess.pq = null;
            return false;
        }
        return true;
    }

    static void DonePriorityQ(GLUtessellatorImpl tess) {
        tess.pq.pqDeletePriorityQ();
    }

    static boolean RemoveDegenerateFaces(GLUmesh mesh) {
        GLUface f = mesh.fHead.next;
        while (f != mesh.fHead) {
            GLUface fNext = f.next;
            GLUhalfEdge e = f.anEdge;
            assert (e.Lnext != e);
            if (e.Lnext.Lnext == e) {
                Sweep.AddWinding(e.Onext, e);
                if (!Mesh.__gl_meshDelete(e)) {
                    return false;
                }
            }
            f = fNext;
        }
        return true;
    }

    public static boolean __gl_computeInterior(GLUtessellatorImpl tess) {
        GLUvertex v;
        tess.fatalError = false;
        Sweep.RemoveDegenerateEdges(tess);
        if (!Sweep.InitPriorityQ(tess)) {
            return false;
        }
        Sweep.InitEdgeDict(tess);
        while ((v = (GLUvertex)tess.pq.pqExtractMin()) != null) {
            GLUvertex vNext;
            while ((vNext = (GLUvertex)tess.pq.pqMinimum()) != null && Geom.VertEq(vNext, v)) {
                vNext = (GLUvertex)tess.pq.pqExtractMin();
                Sweep.SpliceMergeVertices(tess, v.anEdge, vNext.anEdge);
            }
            Sweep.SweepEvent(tess, v);
        }
        tess.event = ((ActiveRegion)Dict.dictKey((DictNode)Dict.dictMin((Dict)tess.dict))).eUp.Org;
        Sweep.DebugEvent(tess);
        Sweep.DoneEdgeDict(tess);
        Sweep.DonePriorityQ(tess);
        if (!Sweep.RemoveDegenerateFaces(tess.mesh)) {
            return false;
        }
        Mesh.__gl_meshCheckMesh(tess.mesh);
        return true;
    }
}

