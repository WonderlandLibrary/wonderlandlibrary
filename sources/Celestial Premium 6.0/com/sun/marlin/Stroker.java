/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.Curve;
import com.sun.marlin.DPathConsumer2D;
import com.sun.marlin.Helpers;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.RendererContext;
import com.sun.marlin.TransformingPathConsumer2D;
import java.util.Arrays;

public final class Stroker
implements DPathConsumer2D,
MarlinConst {
    private static final int MOVE_TO = 0;
    private static final int DRAWING_OP_TO = 1;
    private static final int CLOSE = 2;
    private static final double ERR_JOIN = 1.0f / MIN_SUBPIXELS;
    private static final double ROUND_JOIN_THRESHOLD = ERR_JOIN * ERR_JOIN;
    private static final double C = 4.0 * (Math.sqrt(2.0) - 1.0) / 3.0;
    private static final double SQRT_2 = Math.sqrt(2.0);
    private DPathConsumer2D out;
    private int capStyle;
    private int joinStyle;
    private double lineWidth2;
    private double invHalfLineWidth2Sq;
    private final double[] offset0 = new double[2];
    private final double[] offset1 = new double[2];
    private final double[] offset2 = new double[2];
    private final double[] miter = new double[2];
    private double miterLimitSq;
    private int prev;
    private double sx0;
    private double sy0;
    private double sdx;
    private double sdy;
    private double cx0;
    private double cy0;
    private double cdx;
    private double cdy;
    private double smx;
    private double smy;
    private double cmx;
    private double cmy;
    private final Helpers.PolyStack reverse;
    private final double[] lp = new double[8];
    private final double[] rp = new double[8];
    final RendererContext rdrCtx;
    final Curve curve;
    private double[] clipRect;
    private int cOutCode = 0;
    private int sOutCode = 0;
    private boolean opened = false;
    private boolean capStart = false;
    private boolean monotonize;
    private boolean subdivide = false;
    private final TransformingPathConsumer2D.CurveClipSplitter curveSplitter;

    Stroker(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.reverse = rendererContext.stats != null ? new Helpers.PolyStack(rendererContext, rendererContext.stats.stat_str_polystack_types, rendererContext.stats.stat_str_polystack_curves, rendererContext.stats.hist_str_polystack_curves, rendererContext.stats.stat_array_str_polystack_curves, rendererContext.stats.stat_array_str_polystack_types) : new Helpers.PolyStack(rendererContext);
        this.curve = rendererContext.curve;
        this.curveSplitter = rendererContext.curveClipSplitter;
    }

    public Stroker init(DPathConsumer2D dPathConsumer2D, double d, int n, int n2, double d2, boolean bl) {
        this.out = dPathConsumer2D;
        this.lineWidth2 = d / 2.0;
        this.invHalfLineWidth2Sq = 1.0 / (2.0 * this.lineWidth2 * this.lineWidth2);
        this.monotonize = bl;
        this.capStyle = n;
        this.joinStyle = n2;
        double d3 = d2 * this.lineWidth2;
        this.miterLimitSq = d3 * d3;
        this.prev = 2;
        this.rdrCtx.stroking = 1;
        if (this.rdrCtx.doClip) {
            double d4 = this.lineWidth2;
            if (n == 2) {
                d4 *= SQRT_2;
            }
            if (n2 == 0 && d4 < d3) {
                d4 = d3;
            }
            double[] arrd = this.rdrCtx.clipRect;
            arrd[0] = arrd[0] - d4;
            arrd[1] = arrd[1] + d4;
            arrd[2] = arrd[2] - d4;
            arrd[3] = arrd[3] + d4;
            this.clipRect = arrd;
            if (MarlinConst.DO_LOG_CLIP) {
                MarlinUtils.logInfo("clipRect (stroker): " + Arrays.toString(this.rdrCtx.clipRect));
            }
            if (DO_CLIP_SUBDIVIDER) {
                this.subdivide = bl;
                this.curveSplitter.init();
            } else {
                this.subdivide = false;
            }
        } else {
            this.clipRect = null;
            this.cOutCode = 0;
            this.sOutCode = 0;
        }
        return this;
    }

    public void disableClipping() {
        this.clipRect = null;
        this.cOutCode = 0;
        this.sOutCode = 0;
    }

    void dispose() {
        this.reverse.dispose();
        this.opened = false;
        this.capStart = false;
    }

    private static void computeOffset(double d, double d2, double d3, double[] arrd) {
        double d4 = d * d + d2 * d2;
        if (d4 == 0.0) {
            arrd[0] = 0.0;
            arrd[1] = 0.0;
        } else {
            d4 = Math.sqrt(d4);
            arrd[0] = d2 * d3 / d4;
            arrd[1] = -(d * d3) / d4;
        }
    }

    private static boolean isCW(double d, double d2, double d3, double d4) {
        return d * d4 <= d2 * d3;
    }

    private void mayDrawRoundJoin(double d, double d2, double d3, double d4, double d5, double d6, boolean bl) {
        if (d3 == 0.0 && d4 == 0.0 || d5 == 0.0 && d6 == 0.0) {
            return;
        }
        double d7 = d3 - d5;
        double d8 = d4 - d6;
        double d9 = d7 * d7 + d8 * d8;
        if (d9 < ROUND_JOIN_THRESHOLD) {
            return;
        }
        if (bl) {
            d3 = -d3;
            d4 = -d4;
            d5 = -d5;
            d6 = -d6;
        }
        this.drawRoundJoin(d, d2, d3, d4, d5, d6, bl);
    }

    private void drawRoundJoin(double d, double d2, double d3, double d4, double d5, double d6, boolean bl) {
        double d7 = d3 * d5 + d4 * d6;
        if (d7 >= 0.0) {
            this.drawBezApproxForArc(d, d2, d3, d4, d5, d6, bl);
        } else {
            double d8 = d6 - d4;
            double d9 = d3 - d5;
            double d10 = Math.sqrt(d8 * d8 + d9 * d9);
            double d11 = this.lineWidth2 / d10;
            double d12 = d8 * d11;
            double d13 = d9 * d11;
            if (bl) {
                d12 = -d12;
                d13 = -d13;
            }
            this.drawBezApproxForArc(d, d2, d3, d4, d12, d13, bl);
            this.drawBezApproxForArc(d, d2, d12, d13, d5, d6, bl);
        }
    }

    private void drawBezApproxForArc(double d, double d2, double d3, double d4, double d5, double d6, boolean bl) {
        double d7 = (d3 * d5 + d4 * d6) * this.invHalfLineWidth2Sq;
        if (d7 >= 0.5) {
            return;
        }
        double d8 = 1.3333333333333333 * Math.sqrt(0.5 - d7) / (1.0 + Math.sqrt(d7 + 0.5));
        if (bl) {
            d8 = -d8;
        }
        double d9 = d + d3;
        double d10 = d2 + d4;
        double d11 = d9 - d8 * d4;
        double d12 = d10 + d8 * d3;
        double d13 = d + d5;
        double d14 = d2 + d6;
        double d15 = d13 + d8 * d6;
        double d16 = d14 - d8 * d5;
        this.emitCurveTo(d9, d10, d11, d12, d15, d16, d13, d14, bl);
    }

    private void drawRoundCap(double d, double d2, double d3, double d4) {
        double d5 = C * d3;
        double d6 = C * d4;
        this.emitCurveTo(d + d3 - d6, d2 + d4 + d5, d - d4 + d5, d2 + d3 + d6, d - d4, d2 + d3);
        this.emitCurveTo(d - d4 - d5, d2 + d3 - d6, d - d3 - d6, d2 - d4 + d5, d - d3, d2 - d4);
    }

    private static void computeMiter(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double[] arrd) {
        double d9 = d3 - d;
        double d10 = d4 - d2;
        double d11 = d7 - d5;
        double d12 = d8 - d6;
        double d13 = d9 * d12 - d11 * d10;
        double d14 = d11 * (d2 - d6) - d12 * (d - d5);
        arrd[0] = d + (d14 /= d13) * d9;
        arrd[1] = d2 + d14 * d10;
    }

    private static void safeComputeMiter(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double[] arrd) {
        double d9 = d3 - d;
        double d10 = d8 - d6;
        double d11 = d7 - d5;
        double d12 = d4 - d2;
        double d13 = d9 * d10 - d11 * d12;
        if (d13 == 0.0) {
            arrd[2] = (d + d5) / 2.0;
            arrd[3] = (d2 + d6) / 2.0;
        } else {
            double d14 = d11 * (d2 - d6) - d10 * (d - d5);
            arrd[2] = d + (d14 /= d13) * d9;
            arrd[3] = d2 + d14 * d12;
        }
    }

    private void drawMiter(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, boolean bl) {
        if (d9 == d7 && d10 == d8 || d == 0.0 && d2 == 0.0 || d5 == 0.0 && d6 == 0.0) {
            return;
        }
        if (bl) {
            d7 = -d7;
            d8 = -d8;
            d9 = -d9;
            d10 = -d10;
        }
        Stroker.computeMiter(d3 - d + d7, d4 - d2 + d8, d3 + d7, d4 + d8, d5 + d3 + d9, d6 + d4 + d10, d3 + d9, d4 + d10, this.miter);
        double d11 = this.miter[0];
        double d12 = this.miter[1];
        double d13 = (d11 - d3) * (d11 - d3) + (d12 - d4) * (d12 - d4);
        if (d13 < this.miterLimitSq) {
            this.emitLineTo(d11, d12, bl);
        }
    }

    @Override
    public void moveTo(double d, double d2) {
        this._moveTo(d, d2, this.cOutCode);
        this.sx0 = d;
        this.sy0 = d2;
        this.sdx = 1.0;
        this.sdy = 0.0;
        this.opened = false;
        this.capStart = false;
        if (this.clipRect != null) {
            int n;
            this.cOutCode = n = Helpers.outcode(d, d2, this.clipRect);
            this.sOutCode = n;
        }
    }

    private void _moveTo(double d, double d2, int n) {
        if (this.prev == 0) {
            this.cx0 = d;
            this.cy0 = d2;
        } else {
            if (this.prev == 1) {
                this.finish(n);
            }
            this.prev = 0;
            this.cx0 = d;
            this.cy0 = d2;
            this.cdx = 1.0;
            this.cdy = 0.0;
        }
    }

    @Override
    public void lineTo(double d, double d2) {
        this.lineTo(d, d2, false);
    }

    private void lineTo(double d, double d2, boolean bl) {
        int n = this.cOutCode;
        if (!bl && this.clipRect != null) {
            int n2 = Helpers.outcode(d, d2, this.clipRect);
            int n3 = n | n2;
            if (n3 != 0) {
                int n4 = n & n2;
                if (n4 == 0) {
                    if (this.subdivide) {
                        this.subdivide = false;
                        boolean bl2 = this.curveSplitter.splitLine(this.cx0, this.cy0, d, d2, n3, this);
                        this.subdivide = true;
                        if (bl2) {
                            return;
                        }
                    }
                } else {
                    this.cOutCode = n2;
                    this._moveTo(d, d2, n);
                    this.opened = true;
                    return;
                }
            }
            this.cOutCode = n2;
        }
        double d3 = d - this.cx0;
        double d4 = d2 - this.cy0;
        if (d3 == 0.0 && d4 == 0.0) {
            d3 = 1.0;
        }
        Stroker.computeOffset(d3, d4, this.lineWidth2, this.offset0);
        double d5 = this.offset0[0];
        double d6 = this.offset0[1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, d3, d4, this.cmx, this.cmy, d5, d6, n);
        this.emitLineTo(this.cx0 + d5, this.cy0 + d6);
        this.emitLineTo(d + d5, d2 + d6);
        this.emitLineToRev(this.cx0 - d5, this.cy0 - d6);
        this.emitLineToRev(d - d5, d2 - d6);
        this.prev = 1;
        this.cx0 = d;
        this.cy0 = d2;
        this.cdx = d3;
        this.cdy = d4;
        this.cmx = d5;
        this.cmy = d6;
    }

    @Override
    public void closePath() {
        if (this.prev != 1 && !this.opened) {
            if (this.prev == 2) {
                return;
            }
            this.emitMoveTo(this.cx0, this.cy0 - this.lineWidth2);
            this.sdx = 1.0;
            this.sdy = 0.0;
            this.cdx = 1.0;
            this.cdy = 0.0;
            this.smx = 0.0;
            this.smy = -this.lineWidth2;
            this.cmx = 0.0;
            this.cmy = -this.lineWidth2;
            this.finish(this.cOutCode);
            return;
        }
        if ((this.sOutCode & this.cOutCode) == 0) {
            if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
                this.lineTo(this.sx0, this.sy0, true);
            }
            this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, this.sdx, this.sdy, this.cmx, this.cmy, this.smx, this.smy, this.sOutCode);
            this.emitLineTo(this.sx0 + this.smx, this.sy0 + this.smy);
            if (this.opened) {
                this.emitLineTo(this.sx0 - this.smx, this.sy0 - this.smy);
            } else {
                this.emitMoveTo(this.sx0 - this.smx, this.sy0 - this.smy);
            }
        }
        this.emitReverse();
        this.prev = 2;
        this.cx0 = this.sx0;
        this.cy0 = this.sy0;
        this.cOutCode = this.sOutCode;
        if (this.opened) {
            this.opened = false;
        } else {
            this.emitClose();
        }
    }

    private void emitReverse() {
        this.reverse.popAll(this.out);
    }

    @Override
    public void pathDone() {
        if (this.prev == 1) {
            this.finish(this.cOutCode);
        }
        this.out.pathDone();
        this.prev = 2;
        this.dispose();
    }

    private void finish(int n) {
        if (this.rdrCtx.closedPath) {
            this.emitReverse();
        } else {
            if (n == 0) {
                if (this.capStyle == 1) {
                    this.drawRoundCap(this.cx0, this.cy0, this.cmx, this.cmy);
                } else if (this.capStyle == 2) {
                    this.emitLineTo(this.cx0 - this.cmy + this.cmx, this.cy0 + this.cmx + this.cmy);
                    this.emitLineTo(this.cx0 - this.cmy - this.cmx, this.cy0 + this.cmx - this.cmy);
                }
            }
            this.emitReverse();
            if (!this.capStart) {
                this.capStart = true;
                if (this.sOutCode == 0) {
                    if (this.capStyle == 1) {
                        this.drawRoundCap(this.sx0, this.sy0, -this.smx, -this.smy);
                    } else if (this.capStyle == 2) {
                        this.emitLineTo(this.sx0 + this.smy - this.smx, this.sy0 - this.smx - this.smy);
                        this.emitLineTo(this.sx0 + this.smy + this.smx, this.sy0 - this.smx + this.smy);
                    }
                }
            }
        }
        this.emitClose();
    }

    private void emitMoveTo(double d, double d2) {
        this.out.moveTo(d, d2);
    }

    private void emitLineTo(double d, double d2) {
        this.out.lineTo(d, d2);
    }

    private void emitLineToRev(double d, double d2) {
        this.reverse.pushLine(d, d2);
    }

    private void emitLineTo(double d, double d2, boolean bl) {
        if (bl) {
            this.emitLineToRev(d, d2);
        } else {
            this.emitLineTo(d, d2);
        }
    }

    private void emitQuadTo(double d, double d2, double d3, double d4) {
        this.out.quadTo(d, d2, d3, d4);
    }

    private void emitQuadToRev(double d, double d2, double d3, double d4) {
        this.reverse.pushQuad(d, d2, d3, d4);
    }

    private void emitCurveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        this.out.curveTo(d, d2, d3, d4, d5, d6);
    }

    private void emitCurveToRev(double d, double d2, double d3, double d4, double d5, double d6) {
        this.reverse.pushCubic(d, d2, d3, d4, d5, d6);
    }

    private void emitCurveTo(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, boolean bl) {
        if (bl) {
            this.reverse.pushCubic(d, d2, d3, d4, d5, d6);
        } else {
            this.out.curveTo(d3, d4, d5, d6, d7, d8);
        }
    }

    private void emitClose() {
        this.out.closePath();
    }

    private void drawJoin(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, int n) {
        if (this.prev != 1) {
            this.emitMoveTo(d3 + d9, d4 + d10);
            if (!this.opened) {
                this.sdx = d5;
                this.sdy = d6;
                this.smx = d9;
                this.smy = d10;
            }
        } else {
            boolean bl = Stroker.isCW(d, d2, d5, d6);
            if (n == 0) {
                if (this.joinStyle == 0) {
                    this.drawMiter(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, bl);
                } else if (this.joinStyle == 1) {
                    this.mayDrawRoundJoin(d3, d4, d7, d8, d9, d10, bl);
                }
            }
            this.emitLineTo(d3, d4, !bl);
        }
        this.prev = 1;
    }

    private static boolean within(double d, double d2, double d3, double d4, double d5) {
        assert (d5 > 0.0) : "";
        return Helpers.within(d, d3, d5) && Helpers.within(d2, d4, d5);
    }

    private void getLineOffsets(double d, double d2, double d3, double d4, double[] arrd, double[] arrd2) {
        Stroker.computeOffset(d3 - d, d4 - d2, this.lineWidth2, this.offset0);
        double d5 = this.offset0[0];
        double d6 = this.offset0[1];
        arrd[0] = d + d5;
        arrd[1] = d2 + d6;
        arrd[2] = d3 + d5;
        arrd[3] = d4 + d6;
        arrd2[0] = d - d5;
        arrd2[1] = d2 - d6;
        arrd2[2] = d3 - d5;
        arrd2[3] = d4 - d6;
    }

    private int computeOffsetCubic(double[] arrd, int n, double[] arrd2, double[] arrd3) {
        double d = arrd[n];
        double d2 = arrd[n + 1];
        double d3 = arrd[n + 2];
        double d4 = arrd[n + 3];
        double d5 = arrd[n + 4];
        double d6 = arrd[n + 5];
        double d7 = arrd[n + 6];
        double d8 = arrd[n + 7];
        double d9 = d7 - d5;
        double d10 = d8 - d6;
        double d11 = d3 - d;
        double d12 = d4 - d2;
        boolean bl = Stroker.within(d, d2, d3, d4, 6.0 * Math.ulp(d4));
        boolean bl2 = Stroker.within(d5, d6, d7, d8, 6.0 * Math.ulp(d8));
        if (bl && bl2) {
            this.getLineOffsets(d, d2, d7, d8, arrd2, arrd3);
            return 4;
        }
        if (bl) {
            d11 = d5 - d;
            d12 = d6 - d2;
        } else if (bl2) {
            d9 = d7 - d3;
            d10 = d8 - d4;
        }
        double d13 = d11 * d9 + d12 * d10;
        d13 *= d13;
        double d14 = d11 * d11 + d12 * d12;
        double d15 = d9 * d9 + d10 * d10;
        if (Helpers.within(d13, d14 * d15, 4.0 * Math.ulp(d13))) {
            this.getLineOffsets(d, d2, d7, d8, arrd2, arrd3);
            return 4;
        }
        double d16 = (d + 3.0 * (d3 + d5) + d7) / 8.0;
        double d17 = (d2 + 3.0 * (d4 + d6) + d8) / 8.0;
        double d18 = d5 + d7 - d - d3;
        double d19 = d6 + d8 - d2 - d4;
        Stroker.computeOffset(d11, d12, this.lineWidth2, this.offset0);
        Stroker.computeOffset(d18, d19, this.lineWidth2, this.offset1);
        Stroker.computeOffset(d9, d10, this.lineWidth2, this.offset2);
        double d20 = d + this.offset0[0];
        double d21 = d2 + this.offset0[1];
        double d22 = d16 + this.offset1[0];
        double d23 = d17 + this.offset1[1];
        double d24 = d7 + this.offset2[0];
        double d25 = d8 + this.offset2[1];
        double d26 = 4.0 / (3.0 * (d11 * d10 - d12 * d9));
        double d27 = 2.0 * d22 - d20 - d24;
        double d28 = 2.0 * d23 - d21 - d25;
        double d29 = d26 * (d10 * d27 - d9 * d28);
        double d30 = d26 * (d11 * d28 - d12 * d27);
        double d31 = d20 + d29 * d11;
        double d32 = d21 + d29 * d12;
        double d33 = d24 + d30 * d9;
        double d34 = d25 + d30 * d10;
        arrd2[0] = d20;
        arrd2[1] = d21;
        arrd2[2] = d31;
        arrd2[3] = d32;
        arrd2[4] = d33;
        arrd2[5] = d34;
        arrd2[6] = d24;
        arrd2[7] = d25;
        d20 = d - this.offset0[0];
        d21 = d2 - this.offset0[1];
        d24 = d7 - this.offset2[0];
        d25 = d8 - this.offset2[1];
        d27 = 2.0 * (d22 -= 2.0 * this.offset1[0]) - d20 - d24;
        d28 = 2.0 * (d23 -= 2.0 * this.offset1[1]) - d21 - d25;
        d29 = d26 * (d10 * d27 - d9 * d28);
        d30 = d26 * (d11 * d28 - d12 * d27);
        d31 = d20 + d29 * d11;
        d32 = d21 + d29 * d12;
        d33 = d24 + d30 * d9;
        d34 = d25 + d30 * d10;
        arrd3[0] = d20;
        arrd3[1] = d21;
        arrd3[2] = d31;
        arrd3[3] = d32;
        arrd3[4] = d33;
        arrd3[5] = d34;
        arrd3[6] = d24;
        arrd3[7] = d25;
        return 8;
    }

    private int computeOffsetQuad(double[] arrd, int n, double[] arrd2, double[] arrd3) {
        double d = arrd[n];
        double d2 = arrd[n + 1];
        double d3 = arrd[n + 2];
        double d4 = arrd[n + 3];
        double d5 = arrd[n + 4];
        double d6 = arrd[n + 5];
        double d7 = d5 - d3;
        double d8 = d6 - d4;
        double d9 = d3 - d;
        double d10 = d4 - d2;
        boolean bl = Stroker.within(d, d2, d3, d4, 6.0 * Math.ulp(d4));
        boolean bl2 = Stroker.within(d3, d4, d5, d6, 6.0 * Math.ulp(d6));
        if (bl || bl2) {
            this.getLineOffsets(d, d2, d5, d6, arrd2, arrd3);
            return 4;
        }
        double d11 = d9 * d7 + d10 * d8;
        double d12 = d9 * d9 + d10 * d10;
        double d13 = d7 * d7 + d8 * d8;
        if (Helpers.within(d11 *= d11, d12 * d13, 4.0 * Math.ulp(d11))) {
            this.getLineOffsets(d, d2, d5, d6, arrd2, arrd3);
            return 4;
        }
        Stroker.computeOffset(d9, d10, this.lineWidth2, this.offset0);
        Stroker.computeOffset(d7, d8, this.lineWidth2, this.offset1);
        double d14 = d + this.offset0[0];
        double d15 = d2 + this.offset0[1];
        double d16 = d5 + this.offset1[0];
        double d17 = d6 + this.offset1[1];
        Stroker.safeComputeMiter(d14, d15, d14 + d9, d15 + d10, d16, d17, d16 - d7, d17 - d8, arrd2);
        arrd2[0] = d14;
        arrd2[1] = d15;
        arrd2[4] = d16;
        arrd2[5] = d17;
        d14 = d - this.offset0[0];
        d15 = d2 - this.offset0[1];
        d16 = d5 - this.offset1[0];
        d17 = d6 - this.offset1[1];
        Stroker.safeComputeMiter(d14, d15, d14 + d9, d15 + d10, d16, d17, d16 - d7, d17 - d8, arrd3);
        arrd3[0] = d14;
        arrd3[1] = d15;
        arrd3[4] = d16;
        arrd3[5] = d17;
        return 6;
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
                    this._moveTo(d5, d6, n);
                    this.opened = true;
                    return;
                }
            }
            this.cOutCode = n2;
        }
        this._curveTo(d, d2, d3, d4, d5, d6, n);
    }

    private void _curveTo(double d, double d2, double d3, double d4, double d5, double d6, int n) {
        double[] arrd;
        Object object;
        double d7;
        double d8 = d - this.cx0;
        double d9 = d2 - this.cy0;
        double d10 = d5 - d3;
        double d11 = d6 - d4;
        if (d8 == 0.0 && d9 == 0.0) {
            d8 = d3 - this.cx0;
            d9 = d4 - this.cy0;
            if (d8 == 0.0 && d9 == 0.0) {
                d8 = d5 - this.cx0;
                d9 = d6 - this.cy0;
            }
        }
        if (d10 == 0.0 && d11 == 0.0) {
            d10 = d5 - d;
            d11 = d6 - d2;
            if (d10 == 0.0 && d11 == 0.0) {
                d10 = d5 - this.cx0;
                d11 = d6 - this.cy0;
            }
        }
        if (d8 == 0.0 && d9 == 0.0) {
            if (this.clipRect != null) {
                this.cOutCode = n;
            }
            this.lineTo(this.cx0, this.cy0);
            return;
        }
        if (Math.abs(d8) < 0.1 && Math.abs(d9) < 0.1) {
            d7 = Math.sqrt(d8 * d8 + d9 * d9);
            d8 /= d7;
            d9 /= d7;
        }
        if (Math.abs(d10) < 0.1 && Math.abs(d11) < 0.1) {
            d7 = Math.sqrt(d10 * d10 + d11 * d11);
            d10 /= d7;
            d11 /= d7;
        }
        Stroker.computeOffset(d8, d9, this.lineWidth2, this.offset0);
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, d8, d9, this.cmx, this.cmy, this.offset0[0], this.offset0[1], n);
        int n2 = 0;
        double[] arrd2 = this.lp;
        if (this.monotonize) {
            object = this.rdrCtx.monotonizer.curve(this.cx0, this.cy0, d, d2, d3, d4, d5, d6);
            n2 = ((TransformingPathConsumer2D.CurveBasicMonotonizer)object).nbSplits;
            arrd = ((TransformingPathConsumer2D.CurveBasicMonotonizer)object).middle;
        } else {
            arrd = arrd2;
            arrd[0] = this.cx0;
            arrd[1] = this.cy0;
            arrd[2] = d;
            arrd[3] = d2;
            arrd[4] = d3;
            arrd[5] = d4;
            arrd[6] = d5;
            arrd[7] = d6;
        }
        object = this.rp;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n4 <= n2) {
            n3 = this.computeOffsetCubic(arrd, n5, arrd2, (double[])object);
            this.emitLineTo(arrd2[0], arrd2[1]);
            switch (n3) {
                case 8: {
                    this.emitCurveTo(arrd2[2], arrd2[3], arrd2[4], arrd2[5], arrd2[6], arrd2[7]);
                    this.emitCurveToRev((double)object[0], (double)object[1], (double)object[2], (double)object[3], (double)object[4], (double)object[5]);
                    break;
                }
                case 4: {
                    this.emitLineTo(arrd2[2], arrd2[3]);
                    this.emitLineToRev((double)object[0], (double)object[1]);
                    break;
                }
            }
            this.emitLineToRev((double)object[n3 - 2], (double)object[n3 - 1]);
            ++n4;
            n5 += 6;
        }
        this.prev = 1;
        this.cx0 = d5;
        this.cy0 = d6;
        this.cdx = d10;
        this.cdy = d11;
        this.cmx = (arrd2[n3 - 2] - object[n3 - 2]) / 2.0;
        this.cmy = (arrd2[n3 - 1] - object[n3 - 1]) / 2.0;
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
                    this._moveTo(d3, d4, n);
                    this.opened = true;
                    return;
                }
            }
            this.cOutCode = n2;
        }
        this._quadTo(d, d2, d3, d4, n);
    }

    private void _quadTo(double d, double d2, double d3, double d4, int n) {
        double[] arrd;
        Object object;
        double d5;
        double d6 = d - this.cx0;
        double d7 = d2 - this.cy0;
        double d8 = d3 - d;
        double d9 = d4 - d2;
        if (d6 == 0.0 && d7 == 0.0 || d8 == 0.0 && d9 == 0.0) {
            d6 = d8 = d3 - this.cx0;
            d7 = d9 = d4 - this.cy0;
        }
        if (d6 == 0.0 && d7 == 0.0) {
            if (this.clipRect != null) {
                this.cOutCode = n;
            }
            this.lineTo(this.cx0, this.cy0);
            return;
        }
        if (Math.abs(d6) < 0.1 && Math.abs(d7) < 0.1) {
            d5 = Math.sqrt(d6 * d6 + d7 * d7);
            d6 /= d5;
            d7 /= d5;
        }
        if (Math.abs(d8) < 0.1 && Math.abs(d9) < 0.1) {
            d5 = Math.sqrt(d8 * d8 + d9 * d9);
            d8 /= d5;
            d9 /= d5;
        }
        Stroker.computeOffset(d6, d7, this.lineWidth2, this.offset0);
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, d6, d7, this.cmx, this.cmy, this.offset0[0], this.offset0[1], n);
        int n2 = 0;
        double[] arrd2 = this.lp;
        if (this.monotonize) {
            object = this.rdrCtx.monotonizer.quad(this.cx0, this.cy0, d, d2, d3, d4);
            n2 = ((TransformingPathConsumer2D.CurveBasicMonotonizer)object).nbSplits;
            arrd = ((TransformingPathConsumer2D.CurveBasicMonotonizer)object).middle;
        } else {
            arrd = arrd2;
            arrd[0] = this.cx0;
            arrd[1] = this.cy0;
            arrd[2] = d;
            arrd[3] = d2;
            arrd[4] = d3;
            arrd[5] = d4;
        }
        object = this.rp;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n4 <= n2) {
            n3 = this.computeOffsetQuad(arrd, n5, arrd2, (double[])object);
            this.emitLineTo(arrd2[0], arrd2[1]);
            switch (n3) {
                case 6: {
                    this.emitQuadTo(arrd2[2], arrd2[3], arrd2[4], arrd2[5]);
                    this.emitQuadToRev((double)object[0], (double)object[1], (double)object[2], (double)object[3]);
                    break;
                }
                case 4: {
                    this.emitLineTo(arrd2[2], arrd2[3]);
                    this.emitLineToRev((double)object[0], (double)object[1]);
                    break;
                }
            }
            this.emitLineToRev((double)object[n3 - 2], (double)object[n3 - 1]);
            ++n4;
            n5 += 4;
        }
        this.prev = 1;
        this.cx0 = d3;
        this.cy0 = d4;
        this.cdx = d8;
        this.cdy = d9;
        this.cmx = (arrd2[n3 - 2] - object[n3 - 2]) / 2.0;
        this.cmy = (arrd2[n3 - 1] - object[n3 - 1]) / 2.0;
    }
}

