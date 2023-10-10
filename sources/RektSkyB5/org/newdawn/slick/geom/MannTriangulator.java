/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.geom.Triangulator;
import org.newdawn.slick.geom.Vector2f;

public class MannTriangulator
implements Triangulator {
    private static final double EPSILON = 1.0E-5;
    protected PointBag contour;
    protected PointBag holes;
    private PointBag nextFreePointBag;
    private Point nextFreePoint;
    private List triangles = new ArrayList();

    public MannTriangulator() {
        this.contour = this.getPointBag();
    }

    public void addPolyPoint(float x2, float y2) {
        this.addPoint(new Vector2f(x2, y2));
    }

    public void reset() {
        while (this.holes != null) {
            this.holes = this.freePointBag(this.holes);
        }
        this.contour.clear();
        this.holes = null;
    }

    public void startHole() {
        PointBag newHole = this.getPointBag();
        newHole.next = this.holes;
        this.holes = newHole;
    }

    private void addPoint(Vector2f pt) {
        if (this.holes == null) {
            Point p2 = this.getPoint(pt);
            this.contour.add(p2);
        } else {
            Point p3 = this.getPoint(pt);
            this.holes.add(p3);
        }
    }

    private Vector2f[] triangulate(Vector2f[] result) {
        Point pContour;
        this.contour.computeAngles();
        PointBag hole = this.holes;
        while (hole != null) {
            hole.computeAngles();
            hole = hole.next;
        }
        while (this.holes != null) {
            Point pHole = this.holes.first;
            block2: do {
                if (!(pHole.angle <= 0.0)) continue;
                Point pContour2 = this.contour.first;
                do {
                    if (!pHole.isInfront(pContour2) || !pContour2.isInfront(pHole) || this.contour.doesIntersectSegment(pHole.pt, pContour2.pt)) continue;
                    PointBag hole2 = this.holes;
                    while (!hole2.doesIntersectSegment(pHole.pt, pContour2.pt)) {
                        hole2 = hole2.next;
                        if (hole2 != null) continue;
                        Point newPtContour = this.getPoint(pContour2.pt);
                        pContour2.insertAfter(newPtContour);
                        Point newPtHole = this.getPoint(pHole.pt);
                        pHole.insertBefore(newPtHole);
                        pContour2.next = pHole;
                        pHole.prev = pContour2;
                        newPtHole.next = newPtContour;
                        newPtContour.prev = newPtHole;
                        pContour2.computeAngle();
                        pHole.computeAngle();
                        newPtContour.computeAngle();
                        newPtHole.computeAngle();
                        this.holes.first = null;
                        break block2;
                    }
                } while ((pContour2 = pContour2.next) != this.contour.first);
            } while ((pHole = pHole.next) != this.holes.first);
            this.holes = this.freePointBag(this.holes);
        }
        int numTriangles = this.contour.countPoints() - 2;
        int neededSpace = numTriangles * 3 + 1;
        if (result.length < neededSpace) {
            result = (Vector2f[])Array.newInstance(result.getClass().getComponentType(), neededSpace);
        }
        int idx = 0;
        while ((pContour = this.contour.first) != null && pContour.next != pContour.prev) {
            Point next;
            Point prev;
            do {
                if (!(pContour.angle > 0.0)) continue;
                prev = pContour.prev;
                next = pContour.next;
                if (next.next != prev && (!prev.isInfront(next) || !next.isInfront(prev)) || this.contour.doesIntersectSegment(prev.pt, next.pt)) continue;
                result[idx++] = pContour.pt;
                result[idx++] = next.pt;
                result[idx++] = prev.pt;
                break;
            } while ((pContour = pContour.next) != this.contour.first);
            prev = pContour.prev;
            next = pContour.next;
            this.contour.first = prev;
            pContour.unlink();
            this.freePoint(pContour);
            next.computeAngle();
            prev.computeAngle();
        }
        result[idx] = null;
        this.contour.clear();
        return result;
    }

    private PointBag getPointBag() {
        PointBag pb = this.nextFreePointBag;
        if (pb != null) {
            this.nextFreePointBag = pb.next;
            pb.next = null;
            return pb;
        }
        return new PointBag();
    }

    private PointBag freePointBag(PointBag pb) {
        PointBag next = pb.next;
        pb.clear();
        pb.next = this.nextFreePointBag;
        this.nextFreePointBag = pb;
        return next;
    }

    private Point getPoint(Vector2f pt) {
        Point p2 = this.nextFreePoint;
        if (p2 != null) {
            this.nextFreePoint = p2.next;
            p2.next = null;
            p2.prev = null;
            p2.pt = pt;
            return p2;
        }
        return new Point(pt);
    }

    private void freePoint(Point p2) {
        p2.next = this.nextFreePoint;
        this.nextFreePoint = p2;
    }

    private void freePoints(Point head) {
        head.prev.next = this.nextFreePoint;
        head.prev = null;
        this.nextFreePoint = head;
    }

    public boolean triangulate() {
        Vector2f[] temp = this.triangulate(new Vector2f[0]);
        for (int i2 = 0; i2 < temp.length && temp[i2] != null; ++i2) {
            this.triangles.add(temp[i2]);
        }
        return true;
    }

    public int getTriangleCount() {
        return this.triangles.size() / 3;
    }

    public float[] getTrianglePoint(int tri, int i2) {
        Vector2f pt = (Vector2f)this.triangles.get(tri * 3 + i2);
        return new float[]{pt.x, pt.y};
    }

    protected class PointBag
    implements Serializable {
        protected Point first;
        protected PointBag next;

        protected PointBag() {
        }

        public void clear() {
            if (this.first != null) {
                MannTriangulator.this.freePoints(this.first);
                this.first = null;
            }
        }

        public void add(Point p2) {
            if (this.first != null) {
                this.first.insertBefore(p2);
            } else {
                this.first = p2;
                p2.next = p2;
                p2.prev = p2;
            }
        }

        public void computeAngles() {
            if (this.first == null) {
                return;
            }
            Point p2 = this.first;
            do {
                p2.computeAngle();
            } while ((p2 = p2.next) != this.first);
        }

        public boolean doesIntersectSegment(Vector2f v1, Vector2f v2) {
            double dxA = v2.x - v1.x;
            double dyA = v2.y - v1.y;
            Point p2 = this.first;
            while (true) {
                double dxB;
                double dyB;
                double d2;
                Point n2 = p2.next;
                if (p2.pt != v1 && n2.pt != v1 && p2.pt != v2 && n2.pt != v2 && Math.abs(d2 = dxA * (dyB = (double)(n2.pt.y - p2.pt.y)) - dyA * (dxB = (double)(n2.pt.x - p2.pt.x))) > 1.0E-5) {
                    double tmp1 = p2.pt.x - v1.x;
                    double tmp2 = p2.pt.y - v1.y;
                    double tA = (dyB * tmp1 - dxB * tmp2) / d2;
                    double tB = (dyA * tmp1 - dxA * tmp2) / d2;
                    if (tA >= 0.0 && tA <= 1.0 && tB >= 0.0 && tB <= 1.0) {
                        return true;
                    }
                }
                if (n2 == this.first) {
                    return false;
                }
                p2 = n2;
            }
        }

        public int countPoints() {
            if (this.first == null) {
                return 0;
            }
            int count = 0;
            Point p2 = this.first;
            do {
                ++count;
            } while ((p2 = p2.next) != this.first);
            return count;
        }

        public boolean contains(Vector2f point) {
            if (this.first == null) {
                return false;
            }
            if (this.first.prev.pt.equals(point)) {
                return true;
            }
            return this.first.pt.equals(point);
        }
    }

    private static class Point
    implements Serializable {
        protected Vector2f pt;
        protected Point prev;
        protected Point next;
        protected double nx;
        protected double ny;
        protected double angle;
        protected double dist;

        public Point(Vector2f pt) {
            this.pt = pt;
        }

        public void unlink() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = null;
            this.prev = null;
        }

        public void insertBefore(Point p2) {
            this.prev.next = p2;
            p2.prev = this.prev;
            p2.next = this;
            this.prev = p2;
        }

        public void insertAfter(Point p2) {
            this.next.prev = p2;
            p2.prev = this;
            p2.next = this.next;
            this.next = p2;
        }

        private double hypot(double x2, double y2) {
            return Math.sqrt(x2 * x2 + y2 * y2);
        }

        public void computeAngle() {
            if (this.prev.pt.equals(this.pt)) {
                this.pt.x += 0.01f;
            }
            double dx1 = this.pt.x - this.prev.pt.x;
            double dy1 = this.pt.y - this.prev.pt.y;
            double len1 = this.hypot(dx1, dy1);
            dx1 /= len1;
            dy1 /= len1;
            if (this.next.pt.equals(this.pt)) {
                this.pt.y += 0.01f;
            }
            double dx2 = this.next.pt.x - this.pt.x;
            double dy2 = this.next.pt.y - this.pt.y;
            double len2 = this.hypot(dx2, dy2);
            double nx1 = -dy1;
            double ny1 = dx1;
            this.nx = (nx1 - (dy2 /= len2)) * 0.5;
            this.ny = (ny1 + (dx2 /= len2)) * 0.5;
            if (this.nx * this.nx + this.ny * this.ny < 1.0E-5) {
                this.nx = dx1;
                this.ny = dy2;
                this.angle = 1.0;
                if (dx1 * dx2 + dy1 * dy2 > 0.0) {
                    this.nx = -dx1;
                    this.ny = -dy1;
                }
            } else {
                this.angle = this.nx * dx2 + this.ny * dy2;
            }
        }

        public double getAngle(Point p2) {
            double dx = p2.pt.x - this.pt.x;
            double dy = p2.pt.y - this.pt.y;
            double dlen = this.hypot(dx, dy);
            return (this.nx * dx + this.ny * dy) / dlen;
        }

        public boolean isConcave() {
            return this.angle < 0.0;
        }

        public boolean isInfront(double dx, double dy) {
            boolean sidePrev = (double)(this.prev.pt.y - this.pt.y) * dx + (double)(this.pt.x - this.prev.pt.x) * dy >= 0.0;
            boolean sideNext = (double)(this.pt.y - this.next.pt.y) * dx + (double)(this.next.pt.x - this.pt.x) * dy >= 0.0;
            return this.angle < 0.0 ? sidePrev | sideNext : sidePrev & sideNext;
        }

        public boolean isInfront(Point p2) {
            return this.isInfront(p2.pt.x - this.pt.x, p2.pt.y - this.pt.y);
        }
    }
}

