/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.DPathConsumer2D;
import com.sun.marlin.DoubleArrayCache;
import com.sun.marlin.FloatMath;
import com.sun.marlin.Helpers;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinProperties;
import com.sun.marlin.RendererContext;
import com.sun.marlin.TransformingPathConsumer2D;

public final class Dasher
implements DPathConsumer2D,
MarlinConst {
    static final int REC_LIMIT = 16;
    static final double CURVE_LEN_ERR = MarlinProperties.getCurveLengthError();
    static final double MIN_T_INC = 1.52587890625E-5;
    static final double EPS = 1.0E-6;
    static final double MAX_CYCLES = 1.6E7;
    private DPathConsumer2D out;
    private double[] dash;
    private int dashLen;
    private double startPhase;
    private boolean startDashOn;
    private int startIdx;
    private boolean starting;
    private boolean needsMoveTo;
    private int idx;
    private boolean dashOn;
    private double phase;
    private double sx0;
    private double sy0;
    private double cx0;
    private double cy0;
    private final double[] curCurvepts;
    final RendererContext rdrCtx;
    boolean recycleDashes;
    private double[] firstSegmentsBuffer;
    private int firstSegidx;
    final DoubleArrayCache.Reference dashes_ref;
    final DoubleArrayCache.Reference firstSegmentsBuffer_ref;
    private double[] clipRect;
    private int cOutCode = 0;
    private boolean subdivide = DO_CLIP_SUBDIVIDER;
    private final LengthIterator li = new LengthIterator();
    private final TransformingPathConsumer2D.CurveClipSplitter curveSplitter;
    private double cycleLen;
    private boolean outside;
    private double totalSkipLen;

    Dasher(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.dashes_ref = rendererContext.newDirtyDoubleArrayRef(256);
        this.firstSegmentsBuffer_ref = rendererContext.newDirtyDoubleArrayRef(256);
        this.firstSegmentsBuffer = this.firstSegmentsBuffer_ref.initial;
        this.curCurvepts = new double[16];
        this.curveSplitter = rendererContext.curveClipSplitter;
    }

    public Dasher init(DPathConsumer2D dPathConsumer2D, double[] arrd, int n, double d, boolean bl) {
        this.out = dPathConsumer2D;
        int n2 = 0;
        this.dashOn = true;
        double d2 = 0.0;
        for (int i = 0; i < n; ++i) {
            d2 += arrd[i];
        }
        this.cycleLen = d2;
        double d3 = d / d2;
        if (d < 0.0) {
            if (-d3 >= 1.6E7) {
                d = 0.0;
            } else {
                int n3 = FloatMath.floor_int(-d3);
                if ((n3 & n & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                d += (double)n3 * d2;
                while (d < 0.0) {
                    if (--n2 < 0) {
                        n2 = n - 1;
                    }
                    d += arrd[n2];
                    this.dashOn = !this.dashOn;
                }
            }
        } else if (d > 0.0) {
            if (d3 >= 1.6E7) {
                d = 0.0;
            } else {
                int n4 = FloatMath.floor_int(d3);
                if ((n4 & n & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                d -= (double)n4 * d2;
                while (true) {
                    double d4;
                    double d5 = arrd[n2];
                    if (!(d >= d4)) break;
                    d -= d5;
                    n2 = (n2 + 1) % n;
                    this.dashOn = !this.dashOn;
                }
            }
        }
        this.dash = arrd;
        this.dashLen = n;
        this.phase = d;
        this.startPhase = d;
        this.startDashOn = this.dashOn;
        this.startIdx = n2;
        this.starting = true;
        this.needsMoveTo = false;
        this.firstSegidx = 0;
        this.recycleDashes = bl;
        if (this.rdrCtx.doClip) {
            this.clipRect = this.rdrCtx.clipRect;
        } else {
            this.clipRect = null;
            this.cOutCode = 0;
        }
        return this;
    }

    void dispose() {
        if (this.recycleDashes) {
            this.dash = this.dashes_ref.putArray(this.dash);
        }
        this.firstSegmentsBuffer = this.firstSegmentsBuffer_ref.putArray(this.firstSegmentsBuffer);
    }

    public double[] copyDashArray(float[] arrf) {
        double[] arrd;
        int n = arrf.length;
        if (n <= 256) {
            arrd = this.dashes_ref.initial;
        } else {
            if (DO_STATS) {
                this.rdrCtx.stats.stat_array_dasher_dasher.add(n);
            }
            arrd = this.dashes_ref.getArray(n);
        }
        for (int i = 0; i < n; ++i) {
            arrd[i] = arrf[i];
        }
        return arrd;
    }

    @Override
    public void moveTo(double d, double d2) {
        if (this.firstSegidx != 0) {
            this.out.moveTo(this.sx0, this.sy0);
            this.emitFirstSegments();
        }
        this.needsMoveTo = true;
        this.idx = this.startIdx;
        this.dashOn = this.startDashOn;
        this.phase = this.startPhase;
        this.cx0 = d;
        this.cy0 = d2;
        this.sx0 = d;
        this.sy0 = d2;
        this.starting = true;
        if (this.clipRect != null) {
            int n;
            this.cOutCode = n = Helpers.outcode(d, d2, this.clipRect);
            this.outside = false;
            this.totalSkipLen = 0.0;
        }
    }

    private void emitSeg(double[] arrd, int n, int n2) {
        switch (n2) {
            case 4: {
                this.out.lineTo(arrd[n], arrd[n + 1]);
                return;
            }
            case 8: {
                this.out.curveTo(arrd[n], arrd[n + 1], arrd[n + 2], arrd[n + 3], arrd[n + 4], arrd[n + 5]);
                return;
            }
            case 6: {
                this.out.quadTo(arrd[n], arrd[n + 1], arrd[n + 2], arrd[n + 3]);
                return;
            }
        }
    }

    private void emitFirstSegments() {
        int n;
        double[] arrd = this.firstSegmentsBuffer;
        int n2 = this.firstSegidx;
        for (int i = 0; i < n2; i += n - 1) {
            n = (int)arrd[i];
            this.emitSeg(arrd, i + 1, n);
        }
        this.firstSegidx = 0;
    }

    private void goTo(double[] arrd, int n, int n2, boolean bl) {
        int n3 = n + n2;
        double d = arrd[n3 - 4];
        double d2 = arrd[n3 - 3];
        if (bl) {
            if (this.starting) {
                this.goTo_starting(arrd, n, n2);
            } else {
                if (this.needsMoveTo) {
                    this.needsMoveTo = false;
                    this.out.moveTo(this.cx0, this.cy0);
                }
                this.emitSeg(arrd, n, n2);
            }
        } else {
            if (this.starting) {
                this.starting = false;
            }
            this.needsMoveTo = true;
        }
        this.cx0 = d;
        this.cy0 = d2;
    }

    private void goTo_starting(double[] arrd, int n, int n2) {
        int n3 = this.firstSegidx;
        int n4 = n2 - 1;
        double[] arrd2 = this.firstSegmentsBuffer;
        if (n3 + n4 > arrd2.length) {
            if (DO_STATS) {
                this.rdrCtx.stats.stat_array_dasher_firstSegmentsBuffer.add(n3 + n4);
            }
            this.firstSegmentsBuffer = arrd2 = this.firstSegmentsBuffer_ref.widenArray(arrd2, n3, n3 + n4);
        }
        arrd2[n3++] = n2;
        System.arraycopy(arrd, n, arrd2, n3, --n4);
        this.firstSegidx = n3 + n4;
    }

    @Override
    public void lineTo(double d, double d2) {
        int n = this.cOutCode;
        if (this.clipRect != null) {
            int n2 = Helpers.outcode(d, d2, this.clipRect);
            int n3 = n | n2;
            if (n3 != 0) {
                int n4 = n & n2;
                if (n4 == 0) {
                    if (this.subdivide) {
                        this.subdivide = false;
                        boolean bl = this.curveSplitter.splitLine(this.cx0, this.cy0, d, d2, n3, this);
                        this.subdivide = true;
                        if (bl) {
                            return;
                        }
                    }
                } else {
                    this.cOutCode = n2;
                    this.skipLineTo(d, d2);
                    return;
                }
            }
            this.cOutCode = n2;
            if (this.outside) {
                this.outside = false;
                this.skipLen();
            }
        }
        this._lineTo(d, d2);
    }

    private void _lineTo(double d, double d2) {
        double d3 = d - this.cx0;
        double d4 = d2 - this.cy0;
        double d5 = d3 * d3 + d4 * d4;
        if (d5 == 0.0) {
            return;
        }
        d5 = Math.sqrt(d5);
        double d6 = d3 / d5;
        double d7 = d4 / d5;
        double[] arrd = this.curCurvepts;
        double[] arrd2 = this.dash;
        int n = this.dashLen;
        int n2 = this.idx;
        boolean bl = this.dashOn;
        double d8 = this.phase;
        while (true) {
            double d9;
            double d10;
            if ((d10 = d5 - (d9 = arrd2[n2] - d8)) <= 1.0E-6) {
                arrd[0] = d;
                arrd[1] = d2;
                this.goTo(arrd, 0, 4, bl);
                d8 += d5;
                if (!(Math.abs(d10) <= 1.0E-6)) break;
                d8 = 0.0;
                n2 = (n2 + 1) % n;
                bl = !bl;
                break;
            }
            arrd[0] = this.cx0 + d9 * d6;
            arrd[1] = this.cy0 + d9 * d7;
            this.goTo(arrd, 0, 4, bl);
            d5 = d10;
            n2 = (n2 + 1) % n;
            bl = !bl;
            d8 = 0.0;
        }
        this.idx = n2;
        this.dashOn = bl;
        this.phase = d8;
    }

    private void skipLineTo(double d, double d2) {
        double d3 = d - this.cx0;
        double d4 = d2 - this.cy0;
        double d5 = d3 * d3 + d4 * d4;
        if (d5 != 0.0) {
            d5 = Math.sqrt(d5);
        }
        this.outside = true;
        this.totalSkipLen += d5;
        this.needsMoveTo = true;
        this.starting = false;
        this.cx0 = d;
        this.cy0 = d2;
    }

    public void skipLen() {
        double d = this.totalSkipLen;
        this.totalSkipLen = 0.0;
        double[] arrd = this.dash;
        int n = this.dashLen;
        int n2 = this.idx;
        boolean bl = this.dashOn;
        double d2 = this.phase;
        long l = (long)Math.floor(d / this.cycleLen) - 2L;
        if (l > 0L) {
            d -= this.cycleLen * (double)l;
            long l2 = l * (long)n;
            n2 = (int)(l2 + (long)n2) % n;
            boolean bl2 = bl = (l2 + (bl ? 1L : 0L) & 1L) == 1L;
        }
        while (true) {
            double d3;
            double d4;
            if ((d4 = d - (d3 = arrd[n2] - d2)) <= 1.0E-6) {
                d2 += d;
                if (!(Math.abs(d4) <= 1.0E-6)) break;
                d2 = 0.0;
                n2 = (n2 + 1) % n;
                bl = !bl;
                break;
            }
            d = d4;
            n2 = (n2 + 1) % n;
            bl = !bl;
            d2 = 0.0;
        }
        this.idx = n2;
        this.dashOn = bl;
        this.phase = d2;
    }

    private void somethingTo(int n) {
        double[] arrd = this.curCurvepts;
        if (Dasher.pointCurve(arrd, n)) {
            return;
        }
        LengthIterator lengthIterator = this.li;
        double[] arrd2 = this.dash;
        int n2 = this.dashLen;
        lengthIterator.initializeIterationOnCurve(arrd, n);
        int n3 = this.idx;
        boolean bl = this.dashOn;
        double d = this.phase;
        int n4 = 0;
        double d2 = 0.0;
        double d3 = arrd2[n3] - d;
        while (true) {
            double d4;
            double d5 = lengthIterator.next(d3);
            if (!(d4 < 1.0)) break;
            if (d5 != 0.0) {
                Helpers.subdivideAt((d5 - d2) / (1.0 - d2), arrd, n4, arrd, 0, n);
                d2 = d5;
                this.goTo(arrd, 2, n, bl);
                n4 = n;
            }
            n3 = (n3 + 1) % n2;
            bl = !bl;
            d = 0.0;
            d3 = arrd2[n3];
        }
        this.goTo(arrd, n4 + 2, n, bl);
        d += lengthIterator.lastSegLen();
        if (d + 1.0E-6 >= arrd2[n3]) {
            d = 0.0;
            n3 = (n3 + 1) % n2;
            bl = !bl;
        }
        this.idx = n3;
        this.dashOn = bl;
        this.phase = d;
        lengthIterator.reset();
    }

    private void skipSomethingTo(int n) {
        double[] arrd = this.curCurvepts;
        if (Dasher.pointCurve(arrd, n)) {
            return;
        }
        LengthIterator lengthIterator = this.li;
        lengthIterator.initializeIterationOnCurve(arrd, n);
        double d = lengthIterator.totalLength();
        this.outside = true;
        this.totalSkipLen += d;
        this.needsMoveTo = true;
        this.starting = false;
    }

    private static boolean pointCurve(double[] arrd, int n) {
        for (int i = 2; i < n; ++i) {
            if (arrd[i] == arrd[i - 2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public void curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        int n = this.cOutCode;
        if (this.clipRect != null) {
            int n2;
            int n3;
            int n4 = Helpers.outcode(d, d2, this.clipRect);
            int n5 = n | n4 | (n3 = Helpers.outcode(d3, d4, this.clipRect)) | (n2 = Helpers.outcode(d5, d6, this.clipRect));
            if (n5 != 0) {
                int n6 = n & n4 & n3 & n2;
                if (n6 == 0) {
                    if (this.subdivide) {
                        this.subdivide = false;
                        boolean bl = this.curveSplitter.splitCurve(this.cx0, this.cy0, d, d2, d3, d4, d5, d6, n5, this);
                        this.subdivide = true;
                        if (bl) {
                            return;
                        }
                    }
                } else {
                    this.cOutCode = n2;
                    this.skipCurveTo(d, d2, d3, d4, d5, d6);
                    return;
                }
            }
            this.cOutCode = n2;
            if (this.outside) {
                this.outside = false;
                this.skipLen();
            }
        }
        this._curveTo(d, d2, d3, d4, d5, d6);
    }

    private void _curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        double[] arrd = this.curCurvepts;
        TransformingPathConsumer2D.CurveBasicMonotonizer curveBasicMonotonizer = this.rdrCtx.monotonizer.curve(this.cx0, this.cy0, d, d2, d3, d4, d5, d6);
        int n = curveBasicMonotonizer.nbSplits;
        double[] arrd2 = curveBasicMonotonizer.middle;
        int n2 = 0;
        int n3 = 0;
        while (n2 <= n) {
            System.arraycopy(arrd2, n3, arrd, 0, 8);
            this.somethingTo(8);
            ++n2;
            n3 += 6;
        }
    }

    private void skipCurveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        double[] arrd = this.curCurvepts;
        arrd[0] = this.cx0;
        arrd[1] = this.cy0;
        arrd[2] = d;
        arrd[3] = d2;
        arrd[4] = d3;
        arrd[5] = d4;
        arrd[6] = d5;
        arrd[7] = d6;
        this.skipSomethingTo(8);
        this.cx0 = d5;
        this.cy0 = d6;
    }

    @Override
    public void quadTo(double d, double d2, double d3, double d4) {
        int n = this.cOutCode;
        if (this.clipRect != null) {
            int n2;
            int n3 = Helpers.outcode(d, d2, this.clipRect);
            int n4 = n | n3 | (n2 = Helpers.outcode(d3, d4, this.clipRect));
            if (n4 != 0) {
                int n5 = n & n3 & n2;
                if (n5 == 0) {
                    if (this.subdivide) {
                        this.subdivide = false;
                        boolean bl = this.curveSplitter.splitQuad(this.cx0, this.cy0, d, d2, d3, d4, n4, this);
                        this.subdivide = true;
                        if (bl) {
                            return;
                        }
                    }
                } else {
                    this.cOutCode = n2;
                    this.skipQuadTo(d, d2, d3, d4);
                    return;
                }
            }
            this.cOutCode = n2;
            if (this.outside) {
                this.outside = false;
                this.skipLen();
            }
        }
        this._quadTo(d, d2, d3, d4);
    }

    private void _quadTo(double d, double d2, double d3, double d4) {
        double[] arrd = this.curCurvepts;
        TransformingPathConsumer2D.CurveBasicMonotonizer curveBasicMonotonizer = this.rdrCtx.monotonizer.quad(this.cx0, this.cy0, d, d2, d3, d4);
        int n = curveBasicMonotonizer.nbSplits;
        double[] arrd2 = curveBasicMonotonizer.middle;
        int n2 = 0;
        int n3 = 0;
        while (n2 <= n) {
            System.arraycopy(arrd2, n3, arrd, 0, 8);
            this.somethingTo(6);
            ++n2;
            n3 += 4;
        }
    }

    private void skipQuadTo(double d, double d2, double d3, double d4) {
        double[] arrd = this.curCurvepts;
        arrd[0] = this.cx0;
        arrd[1] = this.cy0;
        arrd[2] = d;
        arrd[3] = d2;
        arrd[4] = d3;
        arrd[5] = d4;
        this.skipSomethingTo(6);
        this.cx0 = d3;
        this.cy0 = d4;
    }

    @Override
    public void closePath() {
        if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
            this.lineTo(this.sx0, this.sy0);
        }
        if (this.firstSegidx != 0) {
            if (!this.dashOn || this.needsMoveTo) {
                this.out.moveTo(this.sx0, this.sy0);
            }
            this.emitFirstSegments();
        }
        this.moveTo(this.sx0, this.sy0);
    }

    @Override
    public void pathDone() {
        if (this.firstSegidx != 0) {
            this.out.moveTo(this.sx0, this.sy0);
            this.emitFirstSegments();
        }
        this.out.pathDone();
        this.dispose();
    }

    static final class LengthIterator {
        private final double[][] recCurveStack;
        private final boolean[] sidesRight;
        private int curveType;
        private double nextT;
        private double lenAtNextT;
        private double lastT;
        private double lenAtLastT;
        private double lenAtLastSplit;
        private double lastSegLen;
        private int recLevel;
        private boolean done = true;
        private final double[] curLeafCtrlPolyLengths = new double[3];
        private int cachedHaveLowAcceleration = -1;
        private final double[] nextRoots = new double[4];
        private final double[] flatLeafCoefCache = new double[]{0.0, 0.0, -1.0, 0.0};

        LengthIterator() {
            this.recCurveStack = new double[17][8];
            this.sidesRight = new boolean[16];
            this.nextT = Double.MAX_VALUE;
            this.lenAtNextT = Double.MAX_VALUE;
            this.lenAtLastSplit = Double.MIN_VALUE;
            this.recLevel = Integer.MIN_VALUE;
            this.lastSegLen = Double.MAX_VALUE;
        }

        void reset() {
        }

        void initializeIterationOnCurve(double[] arrd, int n) {
            System.arraycopy(arrd, 0, this.recCurveStack[0], 0, 8);
            this.curveType = n;
            this.recLevel = 0;
            this.lastT = 0.0;
            this.lenAtLastT = 0.0;
            this.nextT = 0.0;
            this.lenAtNextT = 0.0;
            this.goLeft();
            this.lenAtLastSplit = 0.0;
            if (this.recLevel > 0) {
                this.sidesRight[0] = false;
                this.done = false;
            } else {
                this.sidesRight[0] = true;
                this.done = true;
            }
            this.lastSegLen = 0.0;
        }

        private boolean haveLowAcceleration(double d) {
            if (this.cachedHaveLowAcceleration == -1) {
                double d2;
                double d3;
                double d4 = this.curLeafCtrlPolyLengths[0];
                double d5 = this.curLeafCtrlPolyLengths[1];
                if (!Helpers.within(d4, d5, d * d5)) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                if (!(this.curveType != 8 || Helpers.within(d5, d3 = this.curLeafCtrlPolyLengths[2], d2 = d * d3) && Helpers.within(d4, d3, d2))) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                this.cachedHaveLowAcceleration = 1;
                return true;
            }
            return this.cachedHaveLowAcceleration == 1;
        }

        double next(double d) {
            double d2 = this.lenAtLastSplit + d;
            while (this.lenAtNextT < d2) {
                if (this.done) {
                    this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
                    return 1.0;
                }
                this.goToNextLeaf();
            }
            this.lenAtLastSplit = d2;
            double d3 = this.lenAtNextT - this.lenAtLastT;
            double d4 = (d2 - this.lenAtLastT) / d3;
            if (!this.haveLowAcceleration(0.05)) {
                double d5;
                int n;
                double d6;
                double d7;
                double d8;
                double[] arrd = this.flatLeafCoefCache;
                if (arrd[2] < 0.0) {
                    d8 = this.curLeafCtrlPolyLengths[0];
                    d7 = d8 + this.curLeafCtrlPolyLengths[1];
                    if (this.curveType == 8) {
                        d6 = d7 + this.curLeafCtrlPolyLengths[2];
                        arrd[0] = 3.0 * (d8 - d7) + d6;
                        arrd[1] = 3.0 * (d7 - 2.0 * d8);
                        arrd[2] = 3.0 * d8;
                        arrd[3] = -d6;
                    } else if (this.curveType == 6) {
                        arrd[0] = 0.0;
                        arrd[1] = d7 - 2.0 * d8;
                        arrd[2] = 2.0 * d8;
                        arrd[3] = -d7;
                    }
                }
                if ((n = Helpers.cubicRootsInAB(d8 = arrd[0], d7 = arrd[1], d6 = arrd[2], d5 = d4 * arrd[3], this.nextRoots, 0, 0.0, 1.0)) == 1 && !Double.isNaN(this.nextRoots[0])) {
                    d4 = this.nextRoots[0];
                }
            }
            if ((d4 = d4 * (this.nextT - this.lastT) + this.lastT) >= 1.0) {
                d4 = 1.0;
                this.done = true;
            }
            this.lastSegLen = d;
            return d4;
        }

        double totalLength() {
            while (!this.done) {
                this.goToNextLeaf();
            }
            this.reset();
            return this.lenAtNextT;
        }

        double lastSegLen() {
            return this.lastSegLen;
        }

        private void goToNextLeaf() {
            boolean[] arrbl = this.sidesRight;
            int n = this.recLevel;
            --n;
            while (arrbl[n]) {
                if (n == 0) {
                    this.recLevel = 0;
                    this.done = true;
                    return;
                }
                --n;
            }
            arrbl[n] = true;
            System.arraycopy(this.recCurveStack[n++], 0, this.recCurveStack[n], 0, 8);
            this.recLevel = n;
            this.goLeft();
        }

        private void goLeft() {
            double d = this.onLeaf();
            if (d >= 0.0) {
                this.lastT = this.nextT;
                this.lenAtLastT = this.lenAtNextT;
                this.nextT += (double)(1 << 16 - this.recLevel) * 1.52587890625E-5;
                this.lenAtNextT += d;
                this.flatLeafCoefCache[2] = -1.0;
                this.cachedHaveLowAcceleration = -1;
            } else {
                Helpers.subdivide(this.recCurveStack[this.recLevel], this.recCurveStack[this.recLevel + 1], this.recCurveStack[this.recLevel], this.curveType);
                this.sidesRight[this.recLevel] = false;
                ++this.recLevel;
                this.goLeft();
            }
        }

        private double onLeaf() {
            double[] arrd = this.recCurveStack[this.recLevel];
            int n = this.curveType;
            double d = 0.0;
            double d2 = arrd[0];
            double d3 = arrd[1];
            for (int i = 2; i < n; i += 2) {
                double d4 = arrd[i];
                double d5 = arrd[i + 1];
                double d6 = Helpers.linelen(d2, d3, d4, d5);
                d += d6;
                this.curLeafCtrlPolyLengths[(i >> 1) - 1] = d6;
                d2 = d4;
                d3 = d5;
            }
            double d7 = Helpers.linelen(arrd[0], arrd[1], d2, d3);
            if (d - d7 < CURVE_LEN_ERR || this.recLevel == 16) {
                return (d + d7) / 2.0;
            }
            return -1.0;
        }
    }
}

