/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ByteArrayCache;
import com.sun.marlin.Curve;
import com.sun.marlin.DPathConsumer2D;
import com.sun.marlin.DoubleArrayCache;
import com.sun.marlin.IntArrayCache;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.RendererContext;
import com.sun.marlin.stats.Histogram;
import com.sun.marlin.stats.StatLong;
import java.util.Arrays;

final class Helpers
implements MarlinConst {
    private Helpers() {
        throw new Error("This is a non instantiable class");
    }

    static boolean within(double d, double d2, double d3) {
        double d4 = d2 - d;
        return d4 <= d3 && d4 >= -d3;
    }

    static double evalCubic(double d, double d2, double d3, double d4, double d5) {
        return d5 * (d5 * (d5 * d + d2) + d3) + d4;
    }

    static double evalQuad(double d, double d2, double d3, double d4) {
        return d4 * (d4 * d + d2) + d3;
    }

    static int quadraticRoots(double d, double d2, double d3, double[] arrd, int n) {
        int n2 = n;
        if (d != 0.0) {
            double d4 = d2 * d2 - 4.0 * d * d3;
            if (d4 > 0.0) {
                double d5 = Math.sqrt(d4);
                if (d2 >= 0.0) {
                    arrd[n2++] = 2.0 * d3 / (-d2 - d5);
                    arrd[n2++] = (-d2 - d5) / (2.0 * d);
                } else {
                    arrd[n2++] = (-d2 + d5) / (2.0 * d);
                    arrd[n2++] = 2.0 * d3 / (-d2 + d5);
                }
            } else if (d4 == 0.0) {
                arrd[n2++] = -d2 / (2.0 * d);
            }
        } else if (d2 != 0.0) {
            arrd[n2++] = -d3 / d2;
        }
        return n2 - n;
    }

    static int cubicRootsInAB(double d, double d2, double d3, double d4, double[] arrd, int n, double d5, double d6) {
        int n2;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        if (d == 0.0) {
            int n3 = Helpers.quadraticRoots(d2, d3, d4, arrd, n);
            return Helpers.filterOutNotInAB(arrd, n, n3, d5, d6) - n;
        }
        if ((d12 = (d11 = 0.5 * (0.07407407407407407 * (d2 /= d) * (d10 = d2 * d2) - (d9 = 0.3333333333333333 * d2) * (d3 /= d) + (d4 /= d))) * d11 + (d8 = (d7 = 0.3333333333333333 * (-0.3333333333333333 * d10 + d3)) * d7 * d7)) < 0.0) {
            double d13 = 0.3333333333333333 * Math.acos(-d11 / Math.sqrt(-d8));
            double d14 = 2.0 * Math.sqrt(-d7);
            arrd[n] = d14 * Math.cos(d13) - d9;
            arrd[n + 1] = -d14 * Math.cos(d13 + 1.0471975511965976) - d9;
            arrd[n + 2] = -d14 * Math.cos(d13 - 1.0471975511965976) - d9;
            n2 = 3;
        } else {
            double d15 = Math.sqrt(d12);
            double d16 = Math.cbrt(d15 - d11);
            double d17 = -Math.cbrt(d15 + d11);
            arrd[n] = d16 + d17 - d9;
            n2 = 1;
            if (Helpers.within(d12, 0.0, 1.0E-8)) {
                arrd[n + 1] = -0.5 * (d16 + d17) - d9;
                n2 = 2;
            }
        }
        return Helpers.filterOutNotInAB(arrd, n, n2, d5, d6) - n;
    }

    static int filterOutNotInAB(double[] arrd, int n, int n2, double d, double d2) {
        int n3 = n;
        int n4 = n + n2;
        for (int i = n; i < n4; ++i) {
            if (!(arrd[i] >= d) || !(arrd[i] < d2)) continue;
            arrd[n3++] = arrd[i];
        }
        return n3;
    }

    static double fastLineLen(double d, double d2, double d3, double d4) {
        double d5 = d3 - d;
        double d6 = d4 - d2;
        return Math.abs(d5) + Math.abs(d6);
    }

    static double linelen(double d, double d2, double d3, double d4) {
        double d5 = d3 - d;
        double d6 = d4 - d2;
        return Math.sqrt(d5 * d5 + d6 * d6);
    }

    static double fastQuadLen(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d3 - d;
        double d8 = d5 - d3;
        double d9 = d4 - d2;
        double d10 = d6 - d4;
        return Math.abs(d7) + Math.abs(d8) + Math.abs(d9) + Math.abs(d10);
    }

    static double quadlen(double d, double d2, double d3, double d4, double d5, double d6) {
        return (Helpers.linelen(d, d2, d3, d4) + Helpers.linelen(d3, d4, d5, d6) + Helpers.linelen(d, d2, d5, d6)) / 2.0;
    }

    static double fastCurvelen(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        double d9 = d3 - d;
        double d10 = d5 - d3;
        double d11 = d7 - d5;
        double d12 = d4 - d2;
        double d13 = d6 - d4;
        double d14 = d8 - d6;
        return Math.abs(d9) + Math.abs(d10) + Math.abs(d11) + Math.abs(d12) + Math.abs(d13) + Math.abs(d14);
    }

    static double curvelen(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        return (Helpers.linelen(d, d2, d3, d4) + Helpers.linelen(d3, d4, d5, d6) + Helpers.linelen(d5, d6, d7, d8) + Helpers.linelen(d, d2, d7, d8)) / 2.0;
    }

    static int findSubdivPoints(Curve curve, double[] arrd, double[] arrd2, int n, double d) {
        double d2 = arrd[2] - arrd[0];
        double d3 = arrd[3] - arrd[1];
        if (d3 != 0.0 && d2 != 0.0) {
            double d4 = Math.sqrt(d2 * d2 + d3 * d3);
            double d5 = d2 / d4;
            double d6 = d3 / d4;
            double d7 = d5 * arrd[0] + d6 * arrd[1];
            double d8 = d5 * arrd[1] - d6 * arrd[0];
            double d9 = d5 * arrd[2] + d6 * arrd[3];
            double d10 = d5 * arrd[3] - d6 * arrd[2];
            double d11 = d5 * arrd[4] + d6 * arrd[5];
            double d12 = d5 * arrd[5] - d6 * arrd[4];
            switch (n) {
                case 8: {
                    double d13 = d5 * arrd[6] + d6 * arrd[7];
                    double d14 = d5 * arrd[7] - d6 * arrd[6];
                    curve.set(d7, d8, d9, d10, d11, d12, d13, d14);
                    break;
                }
                case 6: {
                    curve.set(d7, d8, d9, d10, d11, d12);
                    break;
                }
            }
        } else {
            curve.set(arrd, n);
        }
        int n2 = 0;
        n2 += curve.dxRoots(arrd2, n2);
        n2 += curve.dyRoots(arrd2, n2);
        if (n == 8) {
            n2 += curve.infPoints(arrd2, n2);
        }
        n2 += curve.rootsOfROCMinusW(arrd2, n2, d, 1.0E-4);
        n2 = Helpers.filterOutNotInAB(arrd2, 0, n2, 1.0E-4, 0.9999);
        Helpers.isort(arrd2, n2);
        return n2;
    }

    static int findClipPoints(Curve curve, double[] arrd, double[] arrd2, int n, int n2, double[] arrd3) {
        curve.set(arrd, n);
        int n3 = 0;
        if ((n2 & 4) != 0) {
            n3 += curve.xPoints(arrd2, n3, arrd3[2]);
        }
        if ((n2 & 8) != 0) {
            n3 += curve.xPoints(arrd2, n3, arrd3[3]);
        }
        if ((n2 & 1) != 0) {
            n3 += curve.yPoints(arrd2, n3, arrd3[0]);
        }
        if ((n2 & 2) != 0) {
            n3 += curve.yPoints(arrd2, n3, arrd3[1]);
        }
        Helpers.isort(arrd2, n3);
        return n3;
    }

    static void subdivide(double[] arrd, double[] arrd2, double[] arrd3, int n) {
        switch (n) {
            case 8: {
                Helpers.subdivideCubic(arrd, arrd2, arrd3);
                return;
            }
            case 6: {
                Helpers.subdivideQuad(arrd, arrd2, arrd3);
                return;
            }
        }
        throw new InternalError("Unsupported curve type");
    }

    static void isort(double[] arrd, int n) {
        for (int i = 1; i < n; ++i) {
            double d = arrd[i];
            for (int j = i - 1; j >= 0 && arrd[j] > d; --j) {
                arrd[j + 1] = arrd[j];
            }
            arrd[j + 1] = d;
        }
    }

    static void subdivideCubic(double[] arrd, double[] arrd2, double[] arrd3) {
        double d = arrd[0];
        double d2 = arrd[1];
        double d3 = arrd[2];
        double d4 = arrd[3];
        double d5 = arrd[4];
        double d6 = arrd[5];
        double d7 = arrd[6];
        double d8 = arrd[7];
        arrd2[0] = d;
        arrd2[1] = d2;
        arrd3[6] = d7;
        arrd3[7] = d8;
        d = (d + d3) / 2.0;
        d2 = (d2 + d4) / 2.0;
        d7 = (d7 + d5) / 2.0;
        d8 = (d8 + d6) / 2.0;
        double d9 = (d3 + d5) / 2.0;
        double d10 = (d4 + d6) / 2.0;
        d3 = (d + d9) / 2.0;
        d4 = (d2 + d10) / 2.0;
        d5 = (d7 + d9) / 2.0;
        d6 = (d8 + d10) / 2.0;
        d9 = (d3 + d5) / 2.0;
        d10 = (d4 + d6) / 2.0;
        arrd2[2] = d;
        arrd2[3] = d2;
        arrd2[4] = d3;
        arrd2[5] = d4;
        arrd2[6] = d9;
        arrd2[7] = d10;
        arrd3[0] = d9;
        arrd3[1] = d10;
        arrd3[2] = d5;
        arrd3[3] = d6;
        arrd3[4] = d7;
        arrd3[5] = d8;
    }

    static void subdivideCubicAt(double d, double[] arrd, int n, double[] arrd2, int n2, int n3) {
        double d2 = arrd[n];
        double d3 = arrd[n + 1];
        double d4 = arrd[n + 2];
        double d5 = arrd[n + 3];
        double d6 = arrd[n + 4];
        double d7 = arrd[n + 5];
        double d8 = arrd[n + 6];
        double d9 = arrd[n + 7];
        arrd2[n2] = d2;
        arrd2[n2 + 1] = d3;
        arrd2[n3 + 6] = d8;
        arrd2[n3 + 7] = d9;
        d2 += d * (d4 - d2);
        d3 += d * (d5 - d3);
        d8 = d6 + d * (d8 - d6);
        d9 = d7 + d * (d9 - d7);
        double d10 = d4 + d * (d6 - d4);
        double d11 = d5 + d * (d7 - d5);
        d4 = d2 + d * (d10 - d2);
        d5 = d3 + d * (d11 - d3);
        d6 = d10 + d * (d8 - d10);
        d7 = d11 + d * (d9 - d11);
        d10 = d4 + d * (d6 - d4);
        d11 = d5 + d * (d7 - d5);
        arrd2[n2 + 2] = d2;
        arrd2[n2 + 3] = d3;
        arrd2[n2 + 4] = d4;
        arrd2[n2 + 5] = d5;
        arrd2[n2 + 6] = d10;
        arrd2[n2 + 7] = d11;
        arrd2[n3] = d10;
        arrd2[n3 + 1] = d11;
        arrd2[n3 + 2] = d6;
        arrd2[n3 + 3] = d7;
        arrd2[n3 + 4] = d8;
        arrd2[n3 + 5] = d9;
    }

    static void subdivideQuad(double[] arrd, double[] arrd2, double[] arrd3) {
        double d = arrd[0];
        double d2 = arrd[1];
        double d3 = arrd[2];
        double d4 = arrd[3];
        double d5 = arrd[4];
        double d6 = arrd[5];
        arrd2[0] = d;
        arrd2[1] = d2;
        arrd3[4] = d5;
        arrd3[5] = d6;
        d = (d + d3) / 2.0;
        d2 = (d2 + d4) / 2.0;
        d5 = (d5 + d3) / 2.0;
        d6 = (d6 + d4) / 2.0;
        d3 = (d + d5) / 2.0;
        d4 = (d2 + d6) / 2.0;
        arrd2[2] = d;
        arrd2[3] = d2;
        arrd2[4] = d3;
        arrd2[5] = d4;
        arrd3[0] = d3;
        arrd3[1] = d4;
        arrd3[2] = d5;
        arrd3[3] = d6;
    }

    static void subdivideQuadAt(double d, double[] arrd, int n, double[] arrd2, int n2, int n3) {
        double d2 = arrd[n];
        double d3 = arrd[n + 1];
        double d4 = arrd[n + 2];
        double d5 = arrd[n + 3];
        double d6 = arrd[n + 4];
        double d7 = arrd[n + 5];
        arrd2[n2] = d2;
        arrd2[n2 + 1] = d3;
        arrd2[n3 + 4] = d6;
        arrd2[n3 + 5] = d7;
        d2 += d * (d4 - d2);
        d3 += d * (d5 - d3);
        d6 = d4 + d * (d6 - d4);
        d7 = d5 + d * (d7 - d5);
        d4 = d2 + d * (d6 - d2);
        d5 = d3 + d * (d7 - d3);
        arrd2[n2 + 2] = d2;
        arrd2[n2 + 3] = d3;
        arrd2[n2 + 4] = d4;
        arrd2[n2 + 5] = d5;
        arrd2[n3] = d4;
        arrd2[n3 + 1] = d5;
        arrd2[n3 + 2] = d6;
        arrd2[n3 + 3] = d7;
    }

    static void subdivideLineAt(double d, double[] arrd, int n, double[] arrd2, int n2, int n3) {
        double d2 = arrd[n];
        double d3 = arrd[n + 1];
        double d4 = arrd[n + 2];
        double d5 = arrd[n + 3];
        arrd2[n2] = d2;
        arrd2[n2 + 1] = d3;
        arrd2[n3 + 2] = d4;
        arrd2[n3 + 3] = d5;
        d2 += d * (d4 - d2);
        d3 += d * (d5 - d3);
        arrd2[n2 + 2] = d2;
        arrd2[n2 + 3] = d3;
        arrd2[n3] = d2;
        arrd2[n3 + 1] = d3;
    }

    static void subdivideAt(double d, double[] arrd, int n, double[] arrd2, int n2, int n3) {
        if (n3 == 8) {
            Helpers.subdivideCubicAt(d, arrd, n, arrd2, n2, n2 + n3);
        } else if (n3 == 4) {
            Helpers.subdivideLineAt(d, arrd, n, arrd2, n2, n2 + n3);
        } else {
            Helpers.subdivideQuadAt(d, arrd, n, arrd2, n2, n2 + n3);
        }
    }

    static int outcode(double d, double d2, double[] arrd) {
        int n = d2 < arrd[0] ? 1 : (d2 >= arrd[1] ? 2 : 0);
        if (d < arrd[2]) {
            n |= 4;
        } else if (d >= arrd[3]) {
            n |= 8;
        }
        return n;
    }

    static final class IndexStack {
        private static final int INITIAL_COUNT = MarlinConst.INITIAL_EDGES_COUNT >> 2;
        private int end;
        private int[] indices;
        private final IntArrayCache.Reference indices_ref;
        private int indicesUseMark;
        private final StatLong stat_idxstack_indices;
        private final Histogram hist_idxstack_indices;
        private final StatLong stat_array_idxstack_indices;

        IndexStack(RendererContext rendererContext) {
            this(rendererContext, null, null, null);
        }

        IndexStack(RendererContext rendererContext, StatLong statLong, Histogram histogram, StatLong statLong2) {
            this.indices_ref = rendererContext.newDirtyIntArrayRef(INITIAL_COUNT);
            this.indices = this.indices_ref.initial;
            this.end = 0;
            if (MarlinConst.DO_STATS) {
                this.indicesUseMark = 0;
            }
            this.stat_idxstack_indices = statLong;
            this.hist_idxstack_indices = histogram;
            this.stat_array_idxstack_indices = statLong2;
        }

        void dispose() {
            this.end = 0;
            if (MarlinConst.DO_STATS) {
                this.stat_idxstack_indices.add(this.indicesUseMark);
                this.hist_idxstack_indices.add(this.indicesUseMark);
                this.indicesUseMark = 0;
            }
            this.indices = this.indices_ref.putArray(this.indices);
        }

        boolean isEmpty() {
            return this.end == 0;
        }

        void reset() {
            this.end = 0;
        }

        void push(int n) {
            int n2;
            int[] arrn = this.indices;
            if ((n2 = this.end--) != 0 && arrn[n2 - 1] == n) {
                return;
            }
            if (arrn.length <= n2) {
                if (MarlinConst.DO_STATS) {
                    this.stat_array_idxstack_indices.add(n2 + 1);
                }
                this.indices = arrn = this.indices_ref.widenArray(arrn, n2, n2 + 1);
            }
            arrn[this.end++] = n;
            if (MarlinConst.DO_STATS && this.end > this.indicesUseMark) {
                this.indicesUseMark = this.end;
            }
        }

        void pullAll(double[] arrd, DPathConsumer2D dPathConsumer2D) {
            int n = this.end;
            if (n == 0) {
                return;
            }
            int[] arrn = this.indices;
            for (int i = 0; i < n; ++i) {
                int n2 = arrn[i] << 1;
                dPathConsumer2D.lineTo(arrd[n2], arrd[n2 + 1]);
            }
            this.end = 0;
        }
    }

    static final class PolyStack {
        private static final byte TYPE_LINETO = 0;
        private static final byte TYPE_QUADTO = 1;
        private static final byte TYPE_CUBICTO = 2;
        private static final int INITIAL_CURVES_COUNT = MarlinConst.INITIAL_EDGES_COUNT << 1;
        private static final int INITIAL_TYPES_COUNT = MarlinConst.INITIAL_EDGES_COUNT;
        double[] curves;
        int end;
        byte[] curveTypes;
        int numCurves;
        final DoubleArrayCache.Reference curves_ref;
        final ByteArrayCache.Reference curveTypes_ref;
        int curveTypesUseMark;
        int curvesUseMark;
        private final StatLong stat_polystack_types;
        private final StatLong stat_polystack_curves;
        private final Histogram hist_polystack_curves;
        private final StatLong stat_array_polystack_curves;
        private final StatLong stat_array_polystack_curveTypes;

        PolyStack(RendererContext rendererContext) {
            this(rendererContext, null, null, null, null, null);
        }

        PolyStack(RendererContext rendererContext, StatLong statLong, StatLong statLong2, Histogram histogram, StatLong statLong3, StatLong statLong4) {
            this.curves_ref = rendererContext.newDirtyDoubleArrayRef(INITIAL_CURVES_COUNT);
            this.curves = this.curves_ref.initial;
            this.curveTypes_ref = rendererContext.newDirtyByteArrayRef(INITIAL_TYPES_COUNT);
            this.curveTypes = this.curveTypes_ref.initial;
            this.numCurves = 0;
            this.end = 0;
            if (MarlinConst.DO_STATS) {
                this.curveTypesUseMark = 0;
                this.curvesUseMark = 0;
            }
            this.stat_polystack_types = statLong;
            this.stat_polystack_curves = statLong2;
            this.hist_polystack_curves = histogram;
            this.stat_array_polystack_curves = statLong3;
            this.stat_array_polystack_curveTypes = statLong4;
        }

        void dispose() {
            this.end = 0;
            this.numCurves = 0;
            if (MarlinConst.DO_STATS) {
                this.stat_polystack_types.add(this.curveTypesUseMark);
                this.stat_polystack_curves.add(this.curvesUseMark);
                this.hist_polystack_curves.add(this.curvesUseMark);
                this.curveTypesUseMark = 0;
                this.curvesUseMark = 0;
            }
            this.curves = this.curves_ref.putArray(this.curves);
            this.curveTypes = this.curveTypes_ref.putArray(this.curveTypes);
        }

        private void ensureSpace(int n) {
            if (this.curves.length - this.end < n) {
                if (MarlinConst.DO_STATS) {
                    this.stat_array_polystack_curves.add(this.end + n);
                }
                this.curves = this.curves_ref.widenArray(this.curves, this.end, this.end + n);
            }
            if (this.curveTypes.length <= this.numCurves) {
                if (MarlinConst.DO_STATS) {
                    this.stat_array_polystack_curveTypes.add(this.numCurves + 1);
                }
                this.curveTypes = this.curveTypes_ref.widenArray(this.curveTypes, this.numCurves, this.numCurves + 1);
            }
        }

        void pushCubic(double d, double d2, double d3, double d4, double d5, double d6) {
            this.ensureSpace(6);
            this.curveTypes[this.numCurves++] = 2;
            double[] arrd = this.curves;
            int n = this.end;
            arrd[n++] = d5;
            arrd[n++] = d6;
            arrd[n++] = d3;
            arrd[n++] = d4;
            arrd[n++] = d;
            arrd[n++] = d2;
            this.end = n;
        }

        void pushQuad(double d, double d2, double d3, double d4) {
            this.ensureSpace(4);
            this.curveTypes[this.numCurves++] = 1;
            double[] arrd = this.curves;
            int n = this.end;
            arrd[n++] = d3;
            arrd[n++] = d4;
            arrd[n++] = d;
            arrd[n++] = d2;
            this.end = n;
        }

        void pushLine(double d, double d2) {
            this.ensureSpace(2);
            this.curveTypes[this.numCurves++] = 0;
            this.curves[this.end++] = d;
            this.curves[this.end++] = d2;
        }

        void pullAll(DPathConsumer2D dPathConsumer2D) {
            int n = this.numCurves;
            if (n == 0) {
                return;
            }
            if (MarlinConst.DO_STATS) {
                if (this.numCurves > this.curveTypesUseMark) {
                    this.curveTypesUseMark = this.numCurves;
                }
                if (this.end > this.curvesUseMark) {
                    this.curvesUseMark = this.end;
                }
            }
            byte[] arrby = this.curveTypes;
            double[] arrd = this.curves;
            int n2 = 0;
            block5: for (int i = 0; i < n; ++i) {
                switch (arrby[i]) {
                    case 0: {
                        dPathConsumer2D.lineTo(arrd[n2], arrd[n2 + 1]);
                        n2 += 2;
                        continue block5;
                    }
                    case 2: {
                        dPathConsumer2D.curveTo(arrd[n2], arrd[n2 + 1], arrd[n2 + 2], arrd[n2 + 3], arrd[n2 + 4], arrd[n2 + 5]);
                        n2 += 6;
                        continue block5;
                    }
                    case 1: {
                        dPathConsumer2D.quadTo(arrd[n2], arrd[n2 + 1], arrd[n2 + 2], arrd[n2 + 3]);
                        n2 += 4;
                        continue block5;
                    }
                }
            }
            this.numCurves = 0;
            this.end = 0;
        }

        void popAll(DPathConsumer2D dPathConsumer2D) {
            int n = this.numCurves;
            if (n == 0) {
                return;
            }
            if (MarlinConst.DO_STATS) {
                if (this.numCurves > this.curveTypesUseMark) {
                    this.curveTypesUseMark = this.numCurves;
                }
                if (this.end > this.curvesUseMark) {
                    this.curvesUseMark = this.end;
                }
            }
            byte[] arrby = this.curveTypes;
            double[] arrd = this.curves;
            int n2 = this.end;
            block5: while (n != 0) {
                switch (arrby[--n]) {
                    case 0: {
                        dPathConsumer2D.lineTo(arrd[n2 -= 2], arrd[n2 + 1]);
                        continue block5;
                    }
                    case 2: {
                        dPathConsumer2D.curveTo(arrd[n2 -= 6], arrd[n2 + 1], arrd[n2 + 2], arrd[n2 + 3], arrd[n2 + 4], arrd[n2 + 5]);
                        continue block5;
                    }
                    case 1: {
                        dPathConsumer2D.quadTo(arrd[n2 -= 4], arrd[n2 + 1], arrd[n2 + 2], arrd[n2 + 3]);
                        continue block5;
                    }
                }
            }
            this.numCurves = 0;
            this.end = 0;
        }

        public String toString() {
            Object object = "";
            int n = this.numCurves;
            int n2 = this.end;
            while (n != 0) {
                int n3;
                switch (this.curveTypes[--n]) {
                    case 0: {
                        n3 = 2;
                        object = (String)object + "line: ";
                        break;
                    }
                    case 1: {
                        n3 = 4;
                        object = (String)object + "quad: ";
                        break;
                    }
                    case 2: {
                        n3 = 6;
                        object = (String)object + "cubic: ";
                        break;
                    }
                    default: {
                        n3 = 0;
                    }
                }
                object = (String)object + Arrays.toString(Arrays.copyOfRange(this.curves, n2 -= n3, n2 + n3)) + "\n";
            }
            return object;
        }
    }
}

