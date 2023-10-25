/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Vector;

class ShapeEvaluator {
    private Shape savedv0;
    private Shape savedv1;
    private Geometry geom0;
    private Geometry geom1;

    ShapeEvaluator() {
    }

    public Shape evaluate(Shape shape, Shape shape2, float f) {
        if (this.savedv0 != shape || this.savedv1 != shape2) {
            if (this.savedv0 == shape2 && this.savedv1 == shape) {
                Geometry geometry = this.geom0;
                this.geom0 = this.geom1;
                this.geom1 = geometry;
            } else {
                this.recalculate(shape, shape2);
            }
            this.savedv0 = shape;
            this.savedv1 = shape2;
        }
        return this.getShape(f);
    }

    private void recalculate(Shape shape, Shape shape2) {
        this.geom0 = new Geometry(shape);
        this.geom1 = new Geometry(shape2);
        float[] arrf = this.geom0.getTvals();
        float[] arrf2 = this.geom1.getTvals();
        float[] arrf3 = ShapeEvaluator.mergeTvals(arrf, arrf2);
        this.geom0.setTvals(arrf3);
        this.geom1.setTvals(arrf3);
    }

    private Shape getShape(float f) {
        return new MorphedShape(this.geom0, this.geom1, f);
    }

    private static float[] mergeTvals(float[] arrf, float[] arrf2) {
        int n = ShapeEvaluator.sortTvals(arrf, arrf2, null);
        float[] arrf3 = new float[n];
        ShapeEvaluator.sortTvals(arrf, arrf2, arrf3);
        return arrf3;
    }

    private static int sortTvals(float[] arrf, float[] arrf2, float[] arrf3) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n < arrf.length && n2 < arrf2.length) {
            float f = arrf[n];
            float f2 = arrf2[n2];
            if (f <= f2) {
                if (arrf3 != null) {
                    arrf3[n3] = f;
                }
                ++n;
            }
            if (f2 <= f) {
                if (arrf3 != null) {
                    arrf3[n3] = f2;
                }
                ++n2;
            }
            ++n3;
        }
        return n3;
    }

    private static float interp(float f, float f2, float f3) {
        return f + (f2 - f) * f3;
    }

    private static class Geometry {
        static final float THIRD = 0.33333334f;
        static final float MIN_LEN = 0.001f;
        float[] bezierCoords = new float[20];
        int numCoords;
        int windingrule;
        float[] myTvals;

        public Geometry(Shape shape) {
            int n;
            int n2;
            float f;
            float f2;
            float f3;
            float f4;
            PathIterator pathIterator = shape.getPathIterator(null);
            this.windingrule = pathIterator.getWindingRule();
            if (pathIterator.isDone()) {
                this.numCoords = 8;
            }
            float[] arrf = new float[6];
            int n3 = pathIterator.currentSegment(arrf);
            pathIterator.next();
            if (n3 != 0) {
                throw new IllegalPathStateException("missing initial moveto");
            }
            float f5 = f4 = arrf[0];
            this.bezierCoords[0] = f4;
            float f6 = f3 = arrf[1];
            this.bezierCoords[1] = f3;
            Vector<Point2D> vector = new Vector<Point2D>();
            this.numCoords = 2;
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(arrf)) {
                    case 0: {
                        if (f5 != f4 || f6 != f3) {
                            this.appendLineTo(f5, f6, f4, f3);
                            f5 = f4;
                            f6 = f3;
                        }
                        f2 = arrf[0];
                        f = arrf[1];
                        if (f5 == f2 && f6 == f) break;
                        vector.add(new Point2D(f4, f3));
                        this.appendLineTo(f5, f6, f2, f);
                        f5 = f4 = f2;
                        f6 = f3 = f;
                        break;
                    }
                    case 4: {
                        if (f5 == f4 && f6 == f3) break;
                        this.appendLineTo(f5, f6, f4, f3);
                        f5 = f4;
                        f6 = f3;
                        break;
                    }
                    case 1: {
                        f2 = arrf[0];
                        f = arrf[1];
                        this.appendLineTo(f5, f6, f2, f);
                        f5 = f2;
                        f6 = f;
                        break;
                    }
                    case 2: {
                        float f7 = arrf[0];
                        float f8 = arrf[1];
                        f2 = arrf[2];
                        f = arrf[3];
                        this.appendQuadTo(f5, f6, f7, f8, f2, f);
                        f5 = f2;
                        f6 = f;
                        break;
                    }
                    case 3: {
                        f5 = arrf[4];
                        f6 = arrf[5];
                        this.appendCubicTo(arrf[0], arrf[1], arrf[2], arrf[3], f5, f6);
                    }
                }
                pathIterator.next();
            }
            if (this.numCoords < 8 || f5 != f4 || f6 != f3) {
                this.appendLineTo(f5, f6, f4, f3);
                f5 = f4;
                f6 = f3;
            }
            for (n2 = vector.size() - 1; n2 >= 0; --n2) {
                Point2D point2D = (Point2D)vector.get(n2);
                f2 = point2D.x;
                f = point2D.y;
                if (f5 == f2 && f6 == f) continue;
                this.appendLineTo(f5, f6, f2, f);
                f5 = f2;
                f6 = f;
            }
            n2 = 0;
            float f9 = this.bezierCoords[0];
            float f10 = this.bezierCoords[1];
            for (int i = 6; i < this.numCoords; i += 6) {
                float f11 = this.bezierCoords[i];
                float f12 = this.bezierCoords[i + 1];
                if (!(f12 < f10) && (f12 != f10 || !(f11 < f9))) continue;
                n2 = i;
                f9 = f11;
                f10 = f12;
            }
            if (n2 > 0) {
                float[] arrf2 = new float[this.numCoords];
                System.arraycopy(this.bezierCoords, n2, arrf2, 0, this.numCoords - n2);
                System.arraycopy(this.bezierCoords, 2, arrf2, this.numCoords - n2, n2);
                this.bezierCoords = arrf2;
            }
            float f13 = 0.0f;
            f5 = this.bezierCoords[0];
            f6 = this.bezierCoords[1];
            for (n = 2; n < this.numCoords; n += 2) {
                f2 = this.bezierCoords[n];
                f = this.bezierCoords[n + 1];
                f13 += f5 * f - f2 * f6;
                f5 = f2;
                f6 = f;
            }
            if (f13 < 0.0f) {
                n = 2;
                for (int i = this.numCoords - 4; n < i; n += 2, i -= 2) {
                    f5 = this.bezierCoords[n];
                    f6 = this.bezierCoords[n + 1];
                    this.bezierCoords[n] = this.bezierCoords[i];
                    this.bezierCoords[n + 1] = this.bezierCoords[i + 1];
                    this.bezierCoords[i] = f5;
                    this.bezierCoords[i + 1] = f6;
                }
            }
        }

        private void appendLineTo(float f, float f2, float f3, float f4) {
            this.appendCubicTo(ShapeEvaluator.interp(f, f3, 0.33333334f), ShapeEvaluator.interp(f2, f4, 0.33333334f), ShapeEvaluator.interp(f3, f, 0.33333334f), ShapeEvaluator.interp(f4, f2, 0.33333334f), f3, f4);
        }

        private void appendQuadTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.appendCubicTo(ShapeEvaluator.interp(f3, f, 0.33333334f), ShapeEvaluator.interp(f4, f2, 0.33333334f), ShapeEvaluator.interp(f3, f5, 0.33333334f), ShapeEvaluator.interp(f4, f6, 0.33333334f), f5, f6);
        }

        private void appendCubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
            if (this.numCoords + 6 > this.bezierCoords.length) {
                int n = (this.numCoords - 2) * 2 + 2;
                float[] arrf = new float[n];
                System.arraycopy(this.bezierCoords, 0, arrf, 0, this.numCoords);
                this.bezierCoords = arrf;
            }
            this.bezierCoords[this.numCoords++] = f;
            this.bezierCoords[this.numCoords++] = f2;
            this.bezierCoords[this.numCoords++] = f3;
            this.bezierCoords[this.numCoords++] = f4;
            this.bezierCoords[this.numCoords++] = f5;
            this.bezierCoords[this.numCoords++] = f6;
        }

        public int getWindingRule() {
            return this.windingrule;
        }

        public int getNumCoords() {
            return this.numCoords;
        }

        public float getCoord(int n) {
            return this.bezierCoords[n];
        }

        public float[] getTvals() {
            float f;
            float f2;
            if (this.myTvals != null) {
                return this.myTvals;
            }
            float[] arrf = new float[(this.numCoords - 2) / 6 + 1];
            float f3 = this.bezierCoords[0];
            float f4 = this.bezierCoords[1];
            float f5 = 0.0f;
            int n = 2;
            int n2 = 0;
            while (n < this.numCoords) {
                f2 = f3;
                f = f4;
                float f6 = this.bezierCoords[n++];
                float f7 = this.bezierCoords[n++];
                float f8 = (float)Math.sqrt((f2 -= f6) * f2 + (f -= f7) * f);
                f2 = f6;
                f = f7;
                f6 = this.bezierCoords[n++];
                f7 = this.bezierCoords[n++];
                f8 += (float)Math.sqrt((f2 -= f6) * f2 + (f -= f7) * f);
                f2 = f6;
                f = f7;
                f6 = this.bezierCoords[n++];
                f7 = this.bezierCoords[n++];
                f8 += (float)Math.sqrt((f2 -= f6) * f2 + (f -= f7) * f);
                f8 += (float)Math.sqrt((f3 -= f6) * f3 + (f4 -= f7) * f4);
                if ((f8 /= 2.0f) < 0.001f) {
                    f8 = 0.001f;
                }
                arrf[n2++] = f5 += f8;
                f3 = f6;
                f4 = f7;
            }
            f2 = arrf[0];
            arrf[0] = 0.0f;
            for (n2 = 1; n2 < arrf.length - 1; ++n2) {
                f = arrf[n2];
                arrf[n2] = f2 / f5;
                f2 = f;
            }
            arrf[n2] = 1.0f;
            this.myTvals = arrf;
            return arrf;
        }

        public void setTvals(float[] arrf) {
            float f;
            float f2;
            float[] arrf2 = this.bezierCoords;
            float[] arrf3 = new float[2 + (arrf.length - 1) * 6];
            float[] arrf4 = this.getTvals();
            int n = 0;
            float f3 = f2 = arrf2[n++];
            float f4 = f2;
            float f5 = f2;
            float f6 = f = arrf2[n++];
            float f7 = f;
            float f8 = f;
            int n2 = 0;
            arrf3[n2++] = f5;
            arrf3[n2++] = f8;
            float f9 = 0.0f;
            float f10 = 0.0f;
            int n3 = 1;
            int n4 = 1;
            while (n4 < arrf.length) {
                if (f9 >= f10) {
                    f5 = f2;
                    f8 = f;
                    f4 = arrf2[n++];
                    f7 = arrf2[n++];
                    f3 = arrf2[n++];
                    f6 = arrf2[n++];
                    f2 = arrf2[n++];
                    f = arrf2[n++];
                    f10 = arrf4[n3++];
                }
                int n5 = n4++;
                float f11 = arrf[n5];
                if (f11 < f10) {
                    float f12 = (f11 - f9) / (f10 - f9);
                    arrf3[n2++] = f5 = ShapeEvaluator.interp(f5, f4, f12);
                    arrf3[n2++] = f8 = ShapeEvaluator.interp(f8, f7, f12);
                    f4 = ShapeEvaluator.interp(f4, f3, f12);
                    f7 = ShapeEvaluator.interp(f7, f6, f12);
                    f3 = ShapeEvaluator.interp(f3, f2, f12);
                    f6 = ShapeEvaluator.interp(f6, f, f12);
                    arrf3[n2++] = f5 = ShapeEvaluator.interp(f5, f4, f12);
                    arrf3[n2++] = f8 = ShapeEvaluator.interp(f8, f7, f12);
                    f4 = ShapeEvaluator.interp(f4, f3, f12);
                    f7 = ShapeEvaluator.interp(f7, f6, f12);
                    arrf3[n2++] = f5 = ShapeEvaluator.interp(f5, f4, f12);
                    arrf3[n2++] = f8 = ShapeEvaluator.interp(f8, f7, f12);
                } else {
                    arrf3[n2++] = f4;
                    arrf3[n2++] = f7;
                    arrf3[n2++] = f3;
                    arrf3[n2++] = f6;
                    arrf3[n2++] = f2;
                    arrf3[n2++] = f;
                }
                f9 = f11;
            }
            this.bezierCoords = arrf3;
            this.numCoords = arrf3.length;
            this.myTvals = arrf;
        }
    }

    private static class MorphedShape
    extends Shape {
        Geometry geom0;
        Geometry geom1;
        float t;

        MorphedShape(Geometry geometry, Geometry geometry2, float f) {
            this.geom0 = geometry;
            this.geom1 = geometry2;
            this.t = f;
        }

        public Rectangle getRectangle() {
            return new Rectangle(this.getBounds());
        }

        @Override
        public RectBounds getBounds() {
            float f;
            float f2;
            int n = this.geom0.getNumCoords();
            float f3 = f2 = ShapeEvaluator.interp(this.geom0.getCoord(0), this.geom1.getCoord(0), this.t);
            float f4 = f = ShapeEvaluator.interp(this.geom0.getCoord(1), this.geom1.getCoord(1), this.t);
            for (int i = 2; i < n; i += 2) {
                float f5 = ShapeEvaluator.interp(this.geom0.getCoord(i), this.geom1.getCoord(i), this.t);
                float f6 = ShapeEvaluator.interp(this.geom0.getCoord(i + 1), this.geom1.getCoord(i + 1), this.t);
                if (f3 > f5) {
                    f3 = f5;
                }
                if (f4 > f6) {
                    f4 = f6;
                }
                if (f2 < f5) {
                    f2 = f5;
                }
                if (!(f < f6)) continue;
                f = f6;
            }
            return new RectBounds(f3, f4, f2, f);
        }

        @Override
        public boolean contains(float f, float f2) {
            return Path2D.contains(this.getPathIterator(null), f, f2);
        }

        @Override
        public boolean intersects(float f, float f2, float f3, float f4) {
            return Path2D.intersects(this.getPathIterator(null), f, f2, f3, f4);
        }

        @Override
        public boolean contains(float f, float f2, float f3, float f4) {
            return Path2D.contains(this.getPathIterator(null), f, f2, f3, f4);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform) {
            return new Iterator(baseTransform, this.geom0, this.geom1, this.t);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
            return new FlatteningPathIterator(this.getPathIterator(baseTransform), f);
        }

        @Override
        public Shape copy() {
            return new Path2D(this);
        }
    }

    private static class Iterator
    implements PathIterator {
        BaseTransform at;
        Geometry g0;
        Geometry g1;
        float t;
        int cindex;

        public Iterator(BaseTransform baseTransform, Geometry geometry, Geometry geometry2, float f) {
            this.at = baseTransform;
            this.g0 = geometry;
            this.g1 = geometry2;
            this.t = f;
        }

        @Override
        public int getWindingRule() {
            return (double)this.t < 0.5 ? this.g0.getWindingRule() : this.g1.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return this.cindex > this.g0.getNumCoords();
        }

        @Override
        public void next() {
            this.cindex = this.cindex == 0 ? 2 : (this.cindex += 6);
        }

        @Override
        public int currentSegment(float[] arrf) {
            int n;
            int n2;
            if (this.cindex == 0) {
                n2 = 0;
                n = 2;
            } else if (this.cindex >= this.g0.getNumCoords()) {
                n2 = 4;
                n = 0;
            } else {
                n2 = 3;
                n = 6;
            }
            if (n > 0) {
                for (int i = 0; i < n; ++i) {
                    arrf[i] = ShapeEvaluator.interp(this.g0.getCoord(this.cindex + i), this.g1.getCoord(this.cindex + i), this.t);
                }
                if (this.at != null) {
                    this.at.transform(arrf, 0, arrf, 0, n / 2);
                }
            }
            return n2;
        }
    }
}

