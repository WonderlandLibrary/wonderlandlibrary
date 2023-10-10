/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import org.newdawn.slick.geom.Triangulator;

public class NeatTriangulator
implements Triangulator {
    static final float EPSILON = 1.0E-6f;
    private float[] pointsX = new float[100];
    private float[] pointsY = new float[100];
    private int numPoints = 0;
    private Edge[] edges = new Edge[100];
    private int[] V;
    private int numEdges = 0;
    private Triangle[] triangles = new Triangle[100];
    private int numTriangles = 0;
    private float offset = 1.0E-6f;

    public void clear() {
        this.numPoints = 0;
        this.numEdges = 0;
        this.numTriangles = 0;
    }

    private int findEdge(int i2, int j2) {
        int l2;
        int k2;
        if (i2 < j2) {
            k2 = i2;
            l2 = j2;
        } else {
            k2 = j2;
            l2 = i2;
        }
        for (int i1 = 0; i1 < this.numEdges; ++i1) {
            if (this.edges[i1].v0 != k2 || this.edges[i1].v1 != l2) continue;
            return i1;
        }
        return -1;
    }

    private void addEdge(int i2, int j2, int k2) {
        int i1;
        int l2;
        Edge edge;
        int k1;
        int j1;
        int l1 = this.findEdge(i2, j2);
        if (l1 < 0) {
            if (this.numEdges == this.edges.length) {
                Edge[] aedge = new Edge[this.edges.length * 2];
                System.arraycopy(this.edges, 0, aedge, 0, this.numEdges);
                this.edges = aedge;
            }
            j1 = -1;
            k1 = -1;
            l1 = this.numEdges++;
            edge = this.edges[l1] = new Edge();
        } else {
            edge = this.edges[l1];
            j1 = edge.t0;
            k1 = edge.t1;
        }
        if (i2 < j2) {
            l2 = i2;
            i1 = j2;
            j1 = k2;
        } else {
            l2 = j2;
            i1 = i2;
            k1 = k2;
        }
        edge.v0 = l2;
        edge.v1 = i1;
        edge.t0 = j1;
        edge.t1 = k1;
        edge.suspect = true;
    }

    private void deleteEdge(int i2, int j2) throws InternalException {
        int k2 = this.findEdge(i2, j2);
        if (0 > k2) {
            throw new InternalException("Attempt to delete unknown edge");
        }
        this.edges[k2] = this.edges[--this.numEdges];
    }

    void markSuspect(int i2, int j2, boolean flag) throws InternalException {
        int k2 = this.findEdge(i2, j2);
        if (0 > k2) {
            throw new InternalException("Attempt to mark unknown edge");
        }
        this.edges[k2].suspect = flag;
    }

    private Edge chooseSuspect() {
        for (int i2 = 0; i2 < this.numEdges; ++i2) {
            Edge edge = this.edges[i2];
            if (!edge.suspect) continue;
            edge.suspect = false;
            if (edge.t0 < 0 || edge.t1 < 0) continue;
            return edge;
        }
        return null;
    }

    private static float rho(float f2, float f1, float f22, float f3, float f4, float f5) {
        float f6 = f4 - f22;
        float f9 = f1 - f5;
        float f7 = f5 - f3;
        float f8 = f2 - f4;
        float f18 = f6 * f9 - f7 * f8;
        if (f18 > 0.0f) {
            if (f18 < 1.0E-6f) {
                f18 = 1.0E-6f;
            }
            float f12 = f6 * f6;
            float f13 = f7 * f7;
            float f14 = f8 * f8;
            float f15 = f9 * f9;
            float f10 = f22 - f2;
            float f11 = f3 - f1;
            float f16 = f10 * f10;
            float f17 = f11 * f11;
            return (f12 + f13) * (f14 + f15) * (f16 + f17) / (f18 * f18);
        }
        return -1.0f;
    }

    private static boolean insideTriangle(float f2, float f1, float f22, float f3, float f4, float f5, float f6, float f7) {
        float f8 = f4 - f22;
        float f9 = f5 - f3;
        float f10 = f2 - f4;
        float f11 = f1 - f5;
        float f12 = f22 - f2;
        float f13 = f3 - f1;
        float f14 = f6 - f2;
        float f15 = f7 - f1;
        float f16 = f6 - f22;
        float f17 = f7 - f3;
        float f18 = f6 - f4;
        float f19 = f7 - f5;
        float f222 = f8 * f17 - f9 * f16;
        float f20 = f12 * f15 - f13 * f14;
        float f21 = f10 * f19 - f11 * f18;
        return (double)f222 >= 0.0 && (double)f21 >= 0.0 && (double)f20 >= 0.0;
    }

    private boolean snip(int i2, int j2, int k2, int l2) {
        float f2 = this.pointsX[this.V[j2]];
        float f3 = this.pointsX[this.V[i2]];
        float f5 = this.pointsY[this.V[k2]];
        float f1 = this.pointsY[this.V[i2]];
        float f32 = this.pointsY[this.V[j2]];
        float f4 = this.pointsX[this.V[k2]];
        if (1.0E-6f > (f2 - f3) * (f5 - f1) - (f32 - f1) * (f4 - f3)) {
            return false;
        }
        for (int i1 = 0; i1 < l2; ++i1) {
            float f7;
            float f6;
            if (i1 == i2 || i1 == j2 || i1 == k2 || !NeatTriangulator.insideTriangle(f3, f1, f2, f32, f4, f5, f6 = this.pointsX[this.V[i1]], f7 = this.pointsY[this.V[i1]])) continue;
            return false;
        }
        return true;
    }

    private float area() {
        float f2 = 0.0f;
        int i2 = this.numPoints - 1;
        int j2 = 0;
        while (j2 < this.numPoints) {
            f2 += this.pointsX[i2] * this.pointsY[j2] - this.pointsY[i2] * this.pointsX[j2];
            i2 = j2++;
        }
        return f2 * 0.5f;
    }

    public void basicTriangulation() throws InternalException {
        int i2 = this.numPoints;
        if (i2 < 3) {
            return;
        }
        this.numEdges = 0;
        this.numTriangles = 0;
        this.V = new int[i2];
        if (0.0 < (double)this.area()) {
            for (int k2 = 0; k2 < i2; ++k2) {
                this.V[k2] = k2;
            }
        } else {
            for (int l2 = 0; l2 < i2; ++l2) {
                this.V[l2] = this.numPoints - 1 - l2;
            }
        }
        int k1 = 2 * i2;
        int i1 = i2 - 1;
        while (i2 > 2) {
            int j1;
            if (0 >= k1--) {
                throw new InternalException("Bad polygon");
            }
            int j2 = i1;
            if (i2 <= j2) {
                j2 = 0;
            }
            if (i2 <= (i1 = j2 + 1)) {
                i1 = 0;
            }
            if (i2 <= (j1 = i1 + 1)) {
                j1 = 0;
            }
            if (!this.snip(j2, i1, j1, i2)) continue;
            int l1 = this.V[j2];
            int i22 = this.V[i1];
            int j22 = this.V[j1];
            if (this.numTriangles == this.triangles.length) {
                Triangle[] atriangle = new Triangle[this.triangles.length * 2];
                System.arraycopy(this.triangles, 0, atriangle, 0, this.numTriangles);
                this.triangles = atriangle;
            }
            this.triangles[this.numTriangles] = new Triangle(l1, i22, j22);
            this.addEdge(l1, i22, this.numTriangles);
            this.addEdge(i22, j22, this.numTriangles);
            this.addEdge(j22, l1, this.numTriangles);
            ++this.numTriangles;
            int k2 = i1;
            for (int l2 = i1 + 1; l2 < i2; ++l2) {
                this.V[k2] = this.V[l2];
                ++k2;
            }
            k1 = 2 * --i2;
        }
        this.V = null;
    }

    private void optimize() throws InternalException {
        Edge edge;
        while ((edge = this.chooseSuspect()) != null) {
            int i1 = edge.v0;
            int k1 = edge.v1;
            int i2 = edge.t0;
            int j2 = edge.t1;
            int j1 = -1;
            int l1 = -1;
            for (int k2 = 0; k2 < 3; ++k2) {
                int i22 = this.triangles[i2].v[k2];
                if (i1 == i22 || k1 == i22) continue;
                l1 = i22;
                break;
            }
            for (int l2 = 0; l2 < 3; ++l2) {
                int j22 = this.triangles[j2].v[l2];
                if (i1 == j22 || k1 == j22) continue;
                j1 = j22;
                break;
            }
            if (-1 == j1 || -1 == l1) {
                throw new InternalException("can't find quad");
            }
            float f2 = this.pointsX[i1];
            float f1 = this.pointsY[i1];
            float f22 = this.pointsX[j1];
            float f3 = this.pointsY[j1];
            float f4 = this.pointsX[k1];
            float f5 = this.pointsY[k1];
            float f6 = this.pointsX[l1];
            float f7 = this.pointsY[l1];
            float f8 = NeatTriangulator.rho(f2, f1, f22, f3, f4, f5);
            float f9 = NeatTriangulator.rho(f2, f1, f4, f5, f6, f7);
            float f10 = NeatTriangulator.rho(f22, f3, f4, f5, f6, f7);
            float f11 = NeatTriangulator.rho(f22, f3, f6, f7, f2, f1);
            if (0.0f > f8 || 0.0f > f9) {
                throw new InternalException("original triangles backwards");
            }
            if (!(0.0f <= f10) || !(0.0f <= f11)) continue;
            if (f8 > f9) {
                f8 = f9;
            }
            if (f10 > f11) {
                f10 = f11;
            }
            if (!(f8 > f10)) continue;
            this.deleteEdge(i1, k1);
            this.triangles[i2].v[0] = j1;
            this.triangles[i2].v[1] = k1;
            this.triangles[i2].v[2] = l1;
            this.triangles[j2].v[0] = j1;
            this.triangles[j2].v[1] = l1;
            this.triangles[j2].v[2] = i1;
            this.addEdge(j1, k1, i2);
            this.addEdge(k1, l1, i2);
            this.addEdge(l1, j1, i2);
            this.addEdge(l1, i1, j2);
            this.addEdge(i1, j1, j2);
            this.addEdge(j1, l1, j2);
            this.markSuspect(j1, l1, false);
        }
    }

    public boolean triangulate() {
        try {
            this.basicTriangulation();
            return true;
        }
        catch (InternalException e2) {
            this.numEdges = 0;
            return false;
        }
    }

    public void addPolyPoint(float x2, float y2) {
        for (int i2 = 0; i2 < this.numPoints; ++i2) {
            if (this.pointsX[i2] != x2 || this.pointsY[i2] != y2) continue;
            y2 += this.offset;
            this.offset += 1.0E-6f;
        }
        if (this.numPoints == this.pointsX.length) {
            float[] af = new float[this.numPoints * 2];
            System.arraycopy(this.pointsX, 0, af, 0, this.numPoints);
            this.pointsX = af;
            af = new float[this.numPoints * 2];
            System.arraycopy(this.pointsY, 0, af, 0, this.numPoints);
            this.pointsY = af;
        }
        this.pointsX[this.numPoints] = x2;
        this.pointsY[this.numPoints] = y2;
        ++this.numPoints;
    }

    public int getTriangleCount() {
        return this.numTriangles;
    }

    public float[] getTrianglePoint(int tri, int i2) {
        float xp = this.pointsX[this.triangles[tri].v[i2]];
        float yp = this.pointsY[this.triangles[tri].v[i2]];
        return new float[]{xp, yp};
    }

    public void startHole() {
    }

    class InternalException
    extends Exception {
        public InternalException(String msg) {
            super(msg);
        }
    }

    class Edge {
        int v0 = -1;
        int v1 = -1;
        int t0 = -1;
        int t1 = -1;
        boolean suspect;

        Edge() {
        }
    }

    class Triangle {
        int[] v = new int[3];

        Triangle(int i2, int j2, int k2) {
            this.v[0] = i2;
            this.v[1] = j2;
            this.v[2] = k2;
        }
    }
}

