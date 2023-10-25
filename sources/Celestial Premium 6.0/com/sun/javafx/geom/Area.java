/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.AreaIterator;
import com.sun.javafx.geom.AreaOp;
import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Enumeration;
import java.util.Vector;

public class Area
extends Shape {
    private static final Vector EmptyCurves = new Vector();
    private Vector curves;
    private RectBounds cachedBounds;

    public Area() {
        this.curves = EmptyCurves;
    }

    public Area(Shape shape) {
        this.curves = shape instanceof Area ? ((Area)shape).curves : Area.pathToCurves(shape.getPathIterator(null));
    }

    public Area(PathIterator pathIterator) {
        this.curves = Area.pathToCurves(pathIterator);
    }

    private static Vector pathToCurves(PathIterator pathIterator) {
        Vector vector = new Vector();
        int n = pathIterator.getWindingRule();
        float[] arrf = new float[6];
        double[] arrd = new double[23];
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    Curve.insertLine(vector, d3, d4, d, d2);
                    d3 = d = (double)arrf[0];
                    d4 = d2 = (double)arrf[1];
                    Curve.insertMove(vector, d, d2);
                    break;
                }
                case 1: {
                    double d5 = arrf[0];
                    double d6 = arrf[1];
                    Curve.insertLine(vector, d3, d4, d5, d6);
                    d3 = d5;
                    d4 = d6;
                    break;
                }
                case 2: {
                    double d5 = arrf[2];
                    double d6 = arrf[3];
                    Curve.insertQuad(vector, arrd, d3, d4, arrf[0], arrf[1], arrf[2], arrf[3]);
                    d3 = d5;
                    d4 = d6;
                    break;
                }
                case 3: {
                    double d5 = arrf[4];
                    double d6 = arrf[5];
                    Curve.insertCubic(vector, arrd, d3, d4, arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                    d3 = d5;
                    d4 = d6;
                    break;
                }
                case 4: {
                    Curve.insertLine(vector, d3, d4, d, d2);
                    d3 = d;
                    d4 = d2;
                }
            }
            pathIterator.next();
        }
        Curve.insertLine(vector, d3, d4, d, d2);
        AreaOp areaOp = n == 0 ? new AreaOp.EOWindOp() : new AreaOp.NZWindOp();
        return areaOp.calculate(vector, EmptyCurves);
    }

    public void add(Area area) {
        this.curves = new AreaOp.AddOp().calculate(this.curves, area.curves);
        this.invalidateBounds();
    }

    public void subtract(Area area) {
        this.curves = new AreaOp.SubOp().calculate(this.curves, area.curves);
        this.invalidateBounds();
    }

    public void intersect(Area area) {
        this.curves = new AreaOp.IntOp().calculate(this.curves, area.curves);
        this.invalidateBounds();
    }

    public void exclusiveOr(Area area) {
        this.curves = new AreaOp.XorOp().calculate(this.curves, area.curves);
        this.invalidateBounds();
    }

    public void reset() {
        this.curves = new Vector();
        this.invalidateBounds();
    }

    public boolean isEmpty() {
        return this.curves.size() == 0;
    }

    public boolean isPolygonal() {
        Enumeration enumeration = this.curves.elements();
        while (enumeration.hasMoreElements()) {
            if (((Curve)enumeration.nextElement()).getOrder() <= 1) continue;
            return false;
        }
        return true;
    }

    public boolean isRectangular() {
        int n = this.curves.size();
        if (n == 0) {
            return true;
        }
        if (n > 3) {
            return false;
        }
        Curve curve = (Curve)this.curves.get(1);
        Curve curve2 = (Curve)this.curves.get(2);
        if (curve.getOrder() != 1 || curve2.getOrder() != 1) {
            return false;
        }
        if (curve.getXTop() != curve.getXBot() || curve2.getXTop() != curve2.getXBot()) {
            return false;
        }
        return curve.getYTop() == curve2.getYTop() && curve.getYBot() == curve2.getYBot();
    }

    public boolean isSingular() {
        if (this.curves.size() < 3) {
            return true;
        }
        Enumeration enumeration = this.curves.elements();
        enumeration.nextElement();
        while (enumeration.hasMoreElements()) {
            if (((Curve)enumeration.nextElement()).getOrder() != 0) continue;
            return false;
        }
        return true;
    }

    private void invalidateBounds() {
        this.cachedBounds = null;
    }

    private RectBounds getCachedBounds() {
        if (this.cachedBounds != null) {
            return this.cachedBounds;
        }
        RectBounds rectBounds = new RectBounds();
        if (this.curves.size() > 0) {
            Curve curve = (Curve)this.curves.get(0);
            rectBounds.setBounds((float)curve.getX0(), (float)curve.getY0(), 0.0f, 0.0f);
            for (int i = 1; i < this.curves.size(); ++i) {
                ((Curve)this.curves.get(i)).enlarge(rectBounds);
            }
        }
        this.cachedBounds = rectBounds;
        return this.cachedBounds;
    }

    @Override
    public RectBounds getBounds() {
        return new RectBounds(this.getCachedBounds());
    }

    public boolean isEquivalent(Area area) {
        if (area == this) {
            return true;
        }
        if (area == null) {
            return false;
        }
        Vector vector = new AreaOp.XorOp().calculate(this.curves, area.curves);
        return vector.isEmpty();
    }

    public void transform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            throw new NullPointerException("transform must not be null");
        }
        this.curves = Area.pathToCurves(this.getPathIterator(baseTransform));
        this.invalidateBounds();
    }

    public Area createTransformedArea(BaseTransform baseTransform) {
        Area area = new Area(this);
        area.transform(baseTransform);
        return area;
    }

    @Override
    public boolean contains(float f, float f2) {
        if (!this.getCachedBounds().contains(f, f2)) {
            return false;
        }
        Enumeration enumeration = this.curves.elements();
        int n = 0;
        while (enumeration.hasMoreElements()) {
            Curve curve = (Curve)enumeration.nextElement();
            n += curve.crossingsFor(f, f2);
        }
        return n & true;
    }

    @Override
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    @Override
    public boolean contains(float f, float f2, float f3, float f4) {
        if (f3 < 0.0f || f4 < 0.0f) {
            return false;
        }
        if (!this.getCachedBounds().contains(f, f2) || !this.getCachedBounds().contains(f + f3, f2 + f4)) {
            return false;
        }
        Crossings crossings = Crossings.findCrossings(this.curves, f, f2, f + f3, f2 + f4);
        return crossings != null && crossings.covers(f2, f2 + f4);
    }

    @Override
    public boolean intersects(float f, float f2, float f3, float f4) {
        if (f3 < 0.0f || f4 < 0.0f) {
            return false;
        }
        if (!this.getCachedBounds().intersects(f, f2, f3, f4)) {
            return false;
        }
        Crossings crossings = Crossings.findCrossings(this.curves, f, f2, f + f3, f2 + f4);
        return crossings == null || !crossings.isEmpty();
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return new AreaIterator(this.curves, baseTransform);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
    }

    @Override
    public Area copy() {
        return new Area(this);
    }
}

