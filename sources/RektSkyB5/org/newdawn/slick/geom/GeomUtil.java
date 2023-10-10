/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtil {
    public float EPSILON = 1.0E-4f;
    public float EDGE_SCALE = 1.0f;
    public int MAX_POINTS = 10000;
    public GeomUtilListener listener;

    public Shape[] subtract(Shape target, Shape missing) {
        int i2;
        target = target.transform(new Transform());
        missing = missing.transform(new Transform());
        int count = 0;
        for (int i3 = 0; i3 < target.getPointCount(); ++i3) {
            if (!missing.contains(target.getPoint(i3)[0], target.getPoint(i3)[1])) continue;
            ++count;
        }
        if (count == target.getPointCount()) {
            return new Shape[0];
        }
        if (!target.intersects(missing)) {
            return new Shape[]{target};
        }
        int found = 0;
        for (i2 = 0; i2 < missing.getPointCount(); ++i2) {
            if (!target.contains(missing.getPoint(i2)[0], missing.getPoint(i2)[1]) || this.onPath(target, missing.getPoint(i2)[0], missing.getPoint(i2)[1])) continue;
            ++found;
        }
        for (i2 = 0; i2 < target.getPointCount(); ++i2) {
            if (!missing.contains(target.getPoint(i2)[0], target.getPoint(i2)[1]) || this.onPath(missing, target.getPoint(i2)[0], target.getPoint(i2)[1])) continue;
            ++found;
        }
        if (found < 1) {
            return new Shape[]{target};
        }
        return this.combine(target, missing, true);
    }

    private boolean onPath(Shape path, float x2, float y2) {
        for (int i2 = 0; i2 < path.getPointCount() + 1; ++i2) {
            int n2 = GeomUtil.rationalPoint(path, i2 + 1);
            Line line = this.getLine(path, GeomUtil.rationalPoint(path, i2), n2);
            Vector2f vector2f = new Vector2f(x2, y2);
            if (!(line.distance(vector2f) < this.EPSILON * 100.0f)) continue;
            return true;
        }
        return false;
    }

    public void setListener(GeomUtilListener listener) {
        this.listener = listener;
    }

    public Shape[] union(Shape target, Shape other) {
        int i2;
        if (!(target = target.transform(new Transform())).intersects(other = other.transform(new Transform()))) {
            return new Shape[]{target, other};
        }
        boolean touches = false;
        int buttCount = 0;
        for (i2 = 0; i2 < target.getPointCount(); ++i2) {
            if (other.contains(target.getPoint(i2)[0], target.getPoint(i2)[1]) && !other.hasVertex(target.getPoint(i2)[0], target.getPoint(i2)[1])) {
                touches = true;
                break;
            }
            if (!other.hasVertex(target.getPoint(i2)[0], target.getPoint(i2)[1])) continue;
            ++buttCount;
        }
        for (i2 = 0; i2 < other.getPointCount(); ++i2) {
            if (!target.contains(other.getPoint(i2)[0], other.getPoint(i2)[1]) || target.hasVertex(other.getPoint(i2)[0], other.getPoint(i2)[1])) continue;
            touches = true;
            break;
        }
        if (!touches && buttCount < 2) {
            return new Shape[]{target, other};
        }
        return this.combine(target, other, false);
    }

    private Shape[] combine(Shape target, Shape other, boolean subtract) {
        if (subtract) {
            float[] point;
            int i2;
            ArrayList<Shape> shapes = new ArrayList<Shape>();
            ArrayList<Vector2f> used = new ArrayList<Vector2f>();
            for (i2 = 0; i2 < target.getPointCount(); ++i2) {
                point = target.getPoint(i2);
                if (!other.contains(point[0], point[1])) continue;
                used.add(new Vector2f(point[0], point[1]));
                if (this.listener == null) continue;
                this.listener.pointExcluded(point[0], point[1]);
            }
            for (i2 = 0; i2 < target.getPointCount(); ++i2) {
                point = target.getPoint(i2);
                Vector2f pt = new Vector2f(point[0], point[1]);
                if (used.contains(pt)) continue;
                Shape result = this.combineSingle(target, other, true, i2);
                shapes.add(result);
                for (int j2 = 0; j2 < result.getPointCount(); ++j2) {
                    float[] kpoint = result.getPoint(j2);
                    Vector2f kpt = new Vector2f(kpoint[0], kpoint[1]);
                    used.add(kpt);
                }
            }
            return shapes.toArray(new Shape[0]);
        }
        for (int i3 = 0; i3 < target.getPointCount(); ++i3) {
            if (other.contains(target.getPoint(i3)[0], target.getPoint(i3)[1]) || other.hasVertex(target.getPoint(i3)[0], target.getPoint(i3)[1])) continue;
            Shape shape = this.combineSingle(target, other, false, i3);
            return new Shape[]{shape};
        }
        return new Shape[]{other};
    }

    private Shape combineSingle(Shape target, Shape missing, boolean subtract, int start) {
        Shape current = target;
        Shape other = missing;
        int point = start;
        int dir = 1;
        Polygon poly = new Polygon();
        boolean first = true;
        int loop = 0;
        float px = current.getPoint(point)[0];
        float py = current.getPoint(point)[1];
        while (!poly.hasVertex(px, py) || first || current != target) {
            Line line;
            HitResult hit;
            first = false;
            if (++loop > this.MAX_POINTS) break;
            poly.addPoint(px, py);
            if (this.listener != null) {
                this.listener.pointUsed(px, py);
            }
            if ((hit = this.intersect(other, line = this.getLine(current, px, py, GeomUtil.rationalPoint(current, point + dir)))) != null) {
                Shape temp;
                Line hitLine = hit.line;
                Vector2f pt = hit.pt;
                px = pt.x;
                py = pt.y;
                if (this.listener != null) {
                    this.listener.pointIntersected(px, py);
                }
                if (other.hasVertex(px, py)) {
                    point = other.indexOf(pt.x, pt.y);
                    dir = 1;
                    px = pt.x;
                    py = pt.y;
                    Shape temp2 = current;
                    current = other;
                    other = temp2;
                    continue;
                }
                float dx = hitLine.getDX() / hitLine.length();
                float dy = hitLine.getDY() / hitLine.length();
                if (current.contains(pt.x + (dx *= this.EDGE_SCALE), pt.y + (dy *= this.EDGE_SCALE))) {
                    if (subtract) {
                        if (current == missing) {
                            point = hit.p2;
                            dir = -1;
                        } else {
                            point = hit.p1;
                            dir = 1;
                        }
                    } else if (current == target) {
                        point = hit.p2;
                        dir = -1;
                    } else {
                        point = hit.p2;
                        dir = -1;
                    }
                    temp = current;
                    current = other;
                    other = temp;
                    continue;
                }
                if (current.contains(pt.x - dx, pt.y - dy)) {
                    if (subtract) {
                        if (current == target) {
                            point = hit.p2;
                            dir = -1;
                        } else {
                            point = hit.p1;
                            dir = 1;
                        }
                    } else if (current == missing) {
                        point = hit.p1;
                        dir = 1;
                    } else {
                        point = hit.p1;
                        dir = 1;
                    }
                    temp = current;
                    current = other;
                    other = temp;
                    continue;
                }
                if (subtract) break;
                point = hit.p1;
                dir = 1;
                temp = current;
                current = other;
                other = temp;
                point = GeomUtil.rationalPoint(current, point + dir);
                px = current.getPoint(point)[0];
                py = current.getPoint(point)[1];
                continue;
            }
            point = GeomUtil.rationalPoint(current, point + dir);
            px = current.getPoint(point)[0];
            py = current.getPoint(point)[1];
        }
        poly.addPoint(px, py);
        if (this.listener != null) {
            this.listener.pointUsed(px, py);
        }
        return poly;
    }

    public HitResult intersect(Shape shape, Line line) {
        float distance = Float.MAX_VALUE;
        HitResult hit = null;
        for (int i2 = 0; i2 < shape.getPointCount(); ++i2) {
            float newDis;
            int next = GeomUtil.rationalPoint(shape, i2 + 1);
            Line local = this.getLine(shape, i2, next);
            Vector2f pt = line.intersect(local, true);
            if (pt == null || !((newDis = pt.distance(line.getStart())) < distance) || !(newDis > this.EPSILON)) continue;
            hit = new HitResult();
            hit.pt = pt;
            hit.line = local;
            hit.p1 = i2;
            hit.p2 = next;
            distance = newDis;
        }
        return hit;
    }

    public static int rationalPoint(Shape shape, int p2) {
        while (p2 < 0) {
            p2 += shape.getPointCount();
        }
        while (p2 >= shape.getPointCount()) {
            p2 -= shape.getPointCount();
        }
        return p2;
    }

    public Line getLine(Shape shape, int s2, int e2) {
        float[] start = shape.getPoint(s2);
        float[] end = shape.getPoint(e2);
        Line line = new Line(start[0], start[1], end[0], end[1]);
        return line;
    }

    public Line getLine(Shape shape, float sx, float sy, int e2) {
        float[] end = shape.getPoint(e2);
        Line line = new Line(sx, sy, end[0], end[1]);
        return line;
    }

    public class HitResult {
        public Line line;
        public int p1;
        public int p2;
        public Vector2f pt;
    }
}

