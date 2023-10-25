/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.ChainEnd;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.CurveLink;
import com.sun.javafx.geom.Edge;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

public abstract class AreaOp {
    public static final int CTAG_LEFT = 0;
    public static final int CTAG_RIGHT = 1;
    public static final int ETAG_IGNORE = 0;
    public static final int ETAG_ENTER = 1;
    public static final int ETAG_EXIT = -1;
    public static final int RSTAG_INSIDE = 1;
    public static final int RSTAG_OUTSIDE = -1;
    private static Comparator YXTopComparator = (object, object2) -> {
        double d;
        Curve curve = ((Edge)object).getCurve();
        Curve curve2 = ((Edge)object2).getCurve();
        double d2 = curve.getYTop();
        if (d2 == (d = curve2.getYTop()) && (d2 = curve.getXTop()) == (d = curve2.getXTop())) {
            return 0;
        }
        if (d2 < d) {
            return -1;
        }
        return 1;
    };
    private static final CurveLink[] EmptyLinkList = new CurveLink[2];
    private static final ChainEnd[] EmptyChainList = new ChainEnd[2];

    private AreaOp() {
    }

    public abstract void newRow();

    public abstract int classify(Edge var1);

    public abstract int getState();

    public Vector calculate(Vector vector, Vector vector2) {
        Vector vector3 = new Vector();
        AreaOp.addEdges(vector3, vector, 0);
        AreaOp.addEdges(vector3, vector2, 1);
        vector3 = this.pruneEdges(vector3);
        return vector3;
    }

    private static void addEdges(Vector vector, Vector vector2, int n) {
        Enumeration enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            Curve curve = (Curve)enumeration.nextElement();
            if (curve.getOrder() <= 0) continue;
            vector.add(new Edge(curve, n));
        }
    }

    private Vector pruneEdges(Vector vector) {
        int n = vector.size();
        if (n < 2) {
            return vector;
        }
        Edge[] arredge = vector.toArray(new Edge[n]);
        Arrays.sort(arredge, YXTopComparator);
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        double[] arrd = new double[2];
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        Vector<CurveLink> vector4 = new Vector<CurveLink>();
        while (n2 < n) {
            int n6;
            Edge edge;
            double d = arrd[0];
            for (n4 = n5 = n3 - 1; n4 >= n2; --n4) {
                edge = arredge[n4];
                if (!(edge.getCurve().getYBot() > d)) continue;
                if (n5 > n4) {
                    arredge[n5] = edge;
                }
                --n5;
            }
            n2 = n5 + 1;
            if (n2 >= n3) {
                if (n3 >= n) break;
                d = arredge[n3].getCurve().getYTop();
                if (d > arrd[0]) {
                    AreaOp.finalizeSubCurves(vector2, vector3);
                }
                arrd[0] = d;
            }
            while (n3 < n && !((edge = arredge[n3]).getCurve().getYTop() > d)) {
                ++n3;
            }
            arrd[1] = arredge[n2].getCurve().getYBot();
            if (n3 < n && arrd[1] > (d = arredge[n3].getCurve().getYTop())) {
                arrd[1] = d;
            }
            int n7 = 1;
            for (n4 = n2; n4 < n3; ++n4) {
                edge = arredge[n4];
                edge.setEquivalence(0);
                for (n5 = n4; n5 > n2; --n5) {
                    Edge edge2 = arredge[n5 - 1];
                    int n8 = edge.compareTo(edge2, arrd);
                    if (arrd[1] <= arrd[0]) {
                        throw new InternalError("backstepping to " + arrd[1] + " from " + arrd[0]);
                    }
                    if (n8 >= 0) {
                        if (n8 != 0) break;
                        int n9 = edge2.getEquivalence();
                        if (n9 == 0) {
                            n9 = n7++;
                            edge2.setEquivalence(n9);
                        }
                        edge.setEquivalence(n9);
                        break;
                    }
                    arredge[n5] = edge2;
                }
                arredge[n5] = edge;
            }
            this.newRow();
            double d2 = arrd[0];
            double d3 = arrd[1];
            for (n4 = n2; n4 < n3; ++n4) {
                edge = arredge[n4];
                int n10 = edge.getEquivalence();
                if (n10 != 0) {
                    int n11 = this.getState();
                    n6 = n11 == 1 ? -1 : 1;
                    Edge edge3 = null;
                    Edge edge4 = edge;
                    double d4 = d3;
                    do {
                        this.classify(edge);
                        if (edge3 == null && edge.isActiveFor(d2, n6)) {
                            edge3 = edge;
                        }
                        if (!((d = edge.getCurve().getYBot()) > d4)) continue;
                        edge4 = edge;
                        d4 = d;
                    } while (++n4 < n3 && (edge = arredge[n4]).getEquivalence() == n10);
                    --n4;
                    if (this.getState() == n11) {
                        n6 = 0;
                    } else {
                        edge = edge3 != null ? edge3 : edge4;
                    }
                } else {
                    n6 = this.classify(edge);
                }
                if (n6 == 0) continue;
                edge.record(d3, n6);
                vector4.add(new CurveLink(edge.getCurve(), d2, d3, n6));
            }
            if (this.getState() != -1) {
                System.out.println("Still inside at end of active edge list!");
                System.out.println("num curves = " + (n3 - n2));
                System.out.println("num links = " + vector4.size());
                System.out.println("y top = " + arrd[0]);
                if (n3 < n) {
                    System.out.println("y top of next curve = " + arredge[n3].getCurve().getYTop());
                } else {
                    System.out.println("no more curves");
                }
                for (n4 = n2; n4 < n3; ++n4) {
                    edge = arredge[n4];
                    System.out.println(edge);
                    n6 = edge.getEquivalence();
                    if (n6 == 0) continue;
                    System.out.println("  was equal to " + n6 + "...");
                }
            }
            AreaOp.resolveLinks(vector2, vector3, vector4);
            vector4.clear();
            arrd[0] = d3;
        }
        AreaOp.finalizeSubCurves(vector2, vector3);
        Vector<Curve> vector5 = new Vector<Curve>();
        Enumeration enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            CurveLink curveLink = (CurveLink)enumeration.nextElement();
            vector5.add(curveLink.getMoveto());
            CurveLink curveLink2 = curveLink;
            while ((curveLink2 = curveLink2.getNext()) != null) {
                if (curveLink.absorb(curveLink2)) continue;
                vector5.add(curveLink.getSubCurve());
                curveLink = curveLink2;
            }
            vector5.add(curveLink.getSubCurve());
        }
        return vector5;
    }

    public static void finalizeSubCurves(Vector vector, Vector vector2) {
        int n = vector2.size();
        if (n == 0) {
            return;
        }
        if ((n & 1) != 0) {
            throw new InternalError("Odd number of chains!");
        }
        ChainEnd[] arrchainEnd = new ChainEnd[n];
        vector2.toArray(arrchainEnd);
        for (int i = 1; i < n; i += 2) {
            ChainEnd chainEnd = arrchainEnd[i - 1];
            ChainEnd chainEnd2 = arrchainEnd[i];
            CurveLink curveLink = chainEnd.linkTo(chainEnd2);
            if (curveLink == null) continue;
            vector.add(curveLink);
        }
        vector2.clear();
    }

    public static void resolveLinks(Vector vector, Vector vector2, Vector vector3) {
        ChainEnd[] arrchainEnd;
        CurveLink[] arrcurveLink;
        int n = vector3.size();
        if (n == 0) {
            arrcurveLink = EmptyLinkList;
        } else {
            if ((n & 1) != 0) {
                throw new InternalError("Odd number of new curves!");
            }
            arrcurveLink = new CurveLink[n + 2];
            vector3.toArray(arrcurveLink);
        }
        int n2 = vector2.size();
        if (n2 == 0) {
            arrchainEnd = EmptyChainList;
        } else {
            if ((n2 & 1) != 0) {
                throw new InternalError("Odd number of chains!");
            }
            arrchainEnd = new ChainEnd[n2 + 2];
            vector2.toArray(arrchainEnd);
        }
        int n3 = 0;
        int n4 = 0;
        vector2.clear();
        ChainEnd chainEnd = arrchainEnd[0];
        ChainEnd chainEnd2 = arrchainEnd[1];
        CurveLink curveLink = arrcurveLink[0];
        CurveLink curveLink2 = arrcurveLink[1];
        while (chainEnd != null || curveLink != null) {
            boolean bl;
            boolean bl2 = curveLink == null;
            boolean bl3 = bl = chainEnd == null;
            if (!bl2 && !bl) {
                bl2 = !(n3 & true) && chainEnd.getX() == chainEnd2.getX();
                boolean bl4 = bl = !(n4 & true) && curveLink.getX() == curveLink2.getX();
                if (!bl2 && !bl) {
                    double d = chainEnd.getX();
                    double d2 = curveLink.getX();
                    bl2 = chainEnd2 != null && d < d2 && AreaOp.obstructs(chainEnd2.getX(), d2, n3);
                    boolean bl5 = bl = curveLink2 != null && d2 < d && AreaOp.obstructs(curveLink2.getX(), d, n4);
                }
            }
            if (bl2) {
                CurveLink curveLink3 = chainEnd.linkTo(chainEnd2);
                if (curveLink3 != null) {
                    vector.add(curveLink3);
                }
                chainEnd = arrchainEnd[n3 += 2];
                chainEnd2 = arrchainEnd[n3 + 1];
            }
            if (bl) {
                ChainEnd chainEnd3 = new ChainEnd(curveLink, null);
                ChainEnd chainEnd4 = new ChainEnd(curveLink2, chainEnd3);
                chainEnd3.setOtherEnd(chainEnd4);
                vector2.add(chainEnd3);
                vector2.add(chainEnd4);
                curveLink = arrcurveLink[n4 += 2];
                curveLink2 = arrcurveLink[n4 + 1];
            }
            if (bl2 || bl) continue;
            chainEnd.addLink(curveLink);
            vector2.add(chainEnd);
            chainEnd = chainEnd2;
            chainEnd2 = arrchainEnd[++n3 + 1];
            curveLink = curveLink2;
            curveLink2 = arrcurveLink[++n4 + 1];
        }
        if ((vector2.size() & 1) != 0) {
            System.out.println("Odd number of chains!");
        }
    }

    public static boolean obstructs(double d, double d2, int n) {
        return (n & 1) == 0 ? d <= d2 : d < d2;
    }

    public static class EOWindOp
    extends AreaOp {
        private boolean inside;

        @Override
        public void newRow() {
            this.inside = false;
        }

        @Override
        public int classify(Edge edge) {
            boolean bl;
            this.inside = bl = !this.inside;
            return bl ? 1 : -1;
        }

        @Override
        public int getState() {
            return this.inside ? 1 : -1;
        }
    }

    public static class NZWindOp
    extends AreaOp {
        private int count;

        @Override
        public void newRow() {
            this.count = 0;
        }

        @Override
        public int classify(Edge edge) {
            int n = this.count;
            int n2 = n == 0 ? 1 : 0;
            this.count = n += edge.getCurve().getDirection();
            return n == 0 ? -1 : n2;
        }

        @Override
        public int getState() {
            return this.count == 0 ? -1 : 1;
        }
    }

    public static class XorOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl != bl2;
        }
    }

    public static class IntOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl && bl2;
        }
    }

    public static class SubOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl && !bl2;
        }
    }

    public static class AddOp
    extends CAGOp {
        @Override
        public boolean newClassification(boolean bl, boolean bl2) {
            return bl || bl2;
        }
    }

    public static abstract class CAGOp
    extends AreaOp {
        boolean inLeft;
        boolean inRight;
        boolean inResult;

        @Override
        public void newRow() {
            this.inLeft = false;
            this.inRight = false;
            this.inResult = false;
        }

        @Override
        public int classify(Edge edge) {
            if (edge.getCurveTag() == 0) {
                this.inLeft = !this.inLeft;
            } else {
                this.inRight = !this.inRight;
            }
            boolean bl = this.newClassification(this.inLeft, this.inRight);
            if (this.inResult == bl) {
                return 0;
            }
            this.inResult = bl;
            return bl ? 1 : -1;
        }

        @Override
        public int getState() {
            return this.inResult ? 1 : -1;
        }

        public abstract boolean newClassification(boolean var1, boolean var2);
    }
}

