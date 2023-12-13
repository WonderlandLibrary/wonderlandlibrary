/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.geom.Triangulator;

public class BasicTriangulator
implements Triangulator {
    private static final float EPSILON = 1.0E-10f;
    private PointList poly = new PointList();
    private PointList tris = new PointList();
    private boolean tried;

    public void addPolyPoint(float x2, float y2) {
        Point p2 = new Point(x2, y2);
        if (!this.poly.contains(p2)) {
            this.poly.add(p2);
        }
    }

    public int getPolyPointCount() {
        return this.poly.size();
    }

    public float[] getPolyPoint(int index) {
        return new float[]{this.poly.get(index).x, this.poly.get(index).y};
    }

    public boolean triangulate() {
        this.tried = true;
        boolean worked = this.process(this.poly, this.tris);
        return worked;
    }

    public int getTriangleCount() {
        if (!this.tried) {
            throw new RuntimeException("Call triangulate() before accessing triangles");
        }
        return this.tris.size() / 3;
    }

    public float[] getTrianglePoint(int tri, int i2) {
        if (!this.tried) {
            throw new RuntimeException("Call triangulate() before accessing triangles");
        }
        return this.tris.get(tri * 3 + i2).toArray();
    }

    private float area(PointList contour) {
        int n2 = contour.size();
        float A = 0.0f;
        int p2 = n2 - 1;
        int q2 = 0;
        while (q2 < n2) {
            Point contourP = contour.get(p2);
            Point contourQ = contour.get(q2);
            A += contourP.getX() * contourQ.getY() - contourQ.getX() * contourP.getY();
            p2 = q2++;
        }
        return A * 0.5f;
    }

    private boolean insideTriangle(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Px, float Py) {
        float ax = Cx - Bx;
        float ay = Cy - By;
        float bx = Ax - Cx;
        float by = Ay - Cy;
        float cx = Bx - Ax;
        float cy = By - Ay;
        float apx = Px - Ax;
        float apy = Py - Ay;
        float bpx = Px - Bx;
        float bpy = Py - By;
        float cpx = Px - Cx;
        float cpy = Py - Cy;
        float aCROSSbp = ax * bpy - ay * bpx;
        float cCROSSap = cx * apy - cy * apx;
        float bCROSScp = bx * cpy - by * cpx;
        return aCROSSbp >= 0.0f && bCROSScp >= 0.0f && cCROSSap >= 0.0f;
    }

    private boolean snip(PointList contour, int u2, int v2, int w2, int n2, int[] V) {
        float Ax = contour.get(V[u2]).getX();
        float Ay = contour.get(V[u2]).getY();
        float Bx = contour.get(V[v2]).getX();
        float By = contour.get(V[v2]).getY();
        float Cx = contour.get(V[w2]).getX();
        float Cy = contour.get(V[w2]).getY();
        if (1.0E-10f > (Bx - Ax) * (Cy - Ay) - (By - Ay) * (Cx - Ax)) {
            return false;
        }
        for (int p2 = 0; p2 < n2; ++p2) {
            float Py;
            float Px;
            if (p2 == u2 || p2 == v2 || p2 == w2 || !this.insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px = contour.get(V[p2]).getX(), Py = contour.get(V[p2]).getY())) continue;
            return false;
        }
        return true;
    }

    private boolean process(PointList contour, PointList result) {
        int v2;
        result.clear();
        int n2 = contour.size();
        if (n2 < 3) {
            return false;
        }
        int[] V = new int[n2];
        if (0.0f < this.area(contour)) {
            for (v2 = 0; v2 < n2; ++v2) {
                V[v2] = v2;
            }
        } else {
            for (v2 = 0; v2 < n2; ++v2) {
                V[v2] = n2 - 1 - v2;
            }
        }
        int nv = n2;
        int count = 2 * nv;
        int m2 = 0;
        int v3 = nv - 1;
        while (nv > 2) {
            int w2;
            if (0 >= count--) {
                return false;
            }
            int u2 = v3;
            if (nv <= u2) {
                u2 = 0;
            }
            if (nv <= (v3 = u2 + 1)) {
                v3 = 0;
            }
            if (nv <= (w2 = v3 + 1)) {
                w2 = 0;
            }
            if (!this.snip(contour, u2, v3, w2, nv, V)) continue;
            int a2 = V[u2];
            int b2 = V[v3];
            int c2 = V[w2];
            result.add(contour.get(a2));
            result.add(contour.get(b2));
            result.add(contour.get(c2));
            ++m2;
            int s2 = v3;
            for (int t2 = v3 + 1; t2 < nv; ++t2) {
                V[s2] = V[t2];
                ++s2;
            }
            count = 2 * --nv;
        }
        return true;
    }

    public void startHole() {
    }

    private class PointList {
        private ArrayList points = new ArrayList();

        public boolean contains(Point p2) {
            return this.points.contains(p2);
        }

        public void add(Point point) {
            this.points.add(point);
        }

        public void remove(Point point) {
            this.points.remove(point);
        }

        public int size() {
            return this.points.size();
        }

        public Point get(int i2) {
            return (Point)this.points.get(i2);
        }

        public void clear() {
            this.points.clear();
        }
    }

    private class Point {
        private float x;
        private float y;
        private float[] array;

        public Point(float x2, float y2) {
            this.x = x2;
            this.y = y2;
            this.array = new float[]{x2, y2};
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public float[] toArray() {
            return this.array;
        }

        public int hashCode() {
            return (int)(this.x * this.y * 31.0f);
        }

        public boolean equals(Object other) {
            if (other instanceof Point) {
                Point p2 = (Point)other;
                return p2.x == this.x && p2.y == this.y;
            }
            return false;
        }
    }
}

