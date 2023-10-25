/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.Curve;
import com.sun.marlin.FloatMath;
import com.sun.marlin.IntArrayCache;
import com.sun.marlin.MarlinAlphaConsumer;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinProperties;
import com.sun.marlin.MarlinRenderer;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.MergeSort;
import com.sun.marlin.OffHeapArray;
import com.sun.marlin.RendererContext;
import sun.misc.Unsafe;

public final class RendererNoAA
implements MarlinRenderer,
MarlinConst {
    static final boolean DISABLE_RENDER = false;
    private static final int ALL_BUT_LSB = -2;
    private static final int ERR_STEP_MAX = Integer.MAX_VALUE;
    private static final double POWER_2_TO_32 = 4.294967296E9;
    private static final double RDR_OFFSET_X = 0.5;
    private static final double RDR_OFFSET_Y = 0.5;
    public static final long OFF_CURX_OR = 0L;
    public static final long OFF_ERROR = 0L + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_X = OFF_ERROR + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_ERR = OFF_BUMP_X + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_NEXT = OFF_BUMP_ERR + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_YMAX = OFF_NEXT + (long)OffHeapArray.SIZE_INT;
    public static final int SIZEOF_EDGE_BYTES = (int)(OFF_YMAX + (long)OffHeapArray.SIZE_INT);
    private static final double CUB_DEC_ERR_SUBPIX = (double)MarlinProperties.getCubicDecD2() * 0.125;
    private static final double CUB_INC_ERR_SUBPIX = (double)MarlinProperties.getCubicIncD1() * 0.125;
    public static final double CUB_DEC_BND = 8.0 * CUB_DEC_ERR_SUBPIX;
    public static final double CUB_INC_BND = 8.0 * CUB_INC_ERR_SUBPIX;
    public static final int CUB_COUNT_LG = 2;
    private static final int CUB_COUNT = 4;
    private static final int CUB_COUNT_2 = 16;
    private static final int CUB_COUNT_3 = 64;
    private static final double CUB_INV_COUNT = 0.25;
    private static final double CUB_INV_COUNT_2 = 0.0625;
    private static final double CUB_INV_COUNT_3 = 0.015625;
    private static final double QUAD_DEC_ERR_SUBPIX = (double)MarlinProperties.getQuadDecD2() * 0.125;
    public static final double QUAD_DEC_BND = 8.0 * QUAD_DEC_ERR_SUBPIX;
    private int[] crossings;
    private int[] aux_crossings;
    private int edgeCount;
    private int[] edgePtrs;
    private int[] aux_edgePtrs;
    private int activeEdgeMaxUsed;
    private final IntArrayCache.Reference crossings_ref;
    private final IntArrayCache.Reference edgePtrs_ref;
    private final IntArrayCache.Reference aux_crossings_ref;
    private final IntArrayCache.Reference aux_edgePtrs_ref;
    private int edgeMinY = Integer.MAX_VALUE;
    private int edgeMaxY = Integer.MIN_VALUE;
    private double edgeMinX = Double.POSITIVE_INFINITY;
    private double edgeMaxX = Double.NEGATIVE_INFINITY;
    private final OffHeapArray edges;
    private int[] edgeBuckets;
    private int[] edgeBucketCounts;
    private int buckets_minY;
    private int buckets_maxY;
    private final IntArrayCache.Reference edgeBuckets_ref;
    private final IntArrayCache.Reference edgeBucketCounts_ref;
    boolean useRLE = false;
    private int boundsMinX;
    private int boundsMinY;
    private int boundsMaxX;
    private int boundsMaxY;
    private int windingRule;
    private double x0;
    private double y0;
    private double sx0;
    private double sy0;
    final RendererContext rdrCtx;
    private final Curve curve;
    private int[] alphaLine;
    private final IntArrayCache.Reference alphaLine_ref;
    private boolean enableBlkFlags = false;
    private boolean prevUseBlkFlags = false;
    private int[] blkFlags;
    private final IntArrayCache.Reference blkFlags_ref;
    private int bbox_spminX;
    private int bbox_spmaxX;
    private int bbox_spminY;
    private int bbox_spmaxY;
    int bboxX0;
    int bboxX1;
    int bboxY0;
    int bboxY1;

    private void quadBreakIntoLinesAndAdd(double d, double d2, Curve curve, double d3, double d4) {
        int n = 1;
        double d5 = Math.abs(curve.dbx) + Math.abs(curve.dby);
        double d6 = QUAD_DEC_BND;
        while (d5 >= d6) {
            d5 /= 4.0;
            n <<= 1;
            if (!DO_STATS) continue;
            this.rdrCtx.stats.stat_rdr_quadBreak_dec.add(n);
        }
        int n2 = n;
        if (n > 1) {
            double d7 = 1.0 / (double)n;
            double d8 = d7 * d7;
            double d9 = curve.dbx * d8;
            double d10 = curve.dby * d8;
            double d11 = curve.bx * d8 + curve.cx * d7;
            double d12 = curve.by * d8 + curve.cy * d7;
            double d13 = d;
            double d14 = d2;
            while (--n > 0) {
                this.addLine(d, d2, d13 += d11, d14 += d12);
                d = d13;
                d2 = d14;
                d11 += d9;
                d12 += d10;
            }
        }
        this.addLine(d, d2, d3, d4);
        if (DO_STATS) {
            this.rdrCtx.stats.stat_rdr_quadBreak.add(n2);
        }
    }

    private void curveBreakIntoLinesAndAdd(double d, double d2, Curve curve, double d3, double d4) {
        int n = 4;
        double d5 = 2.0 * curve.dax * 0.015625;
        double d6 = 2.0 * curve.day * 0.015625;
        double d7 = d5 + curve.dbx * 0.0625;
        double d8 = d6 + curve.dby * 0.0625;
        double d9 = curve.ax * 0.015625 + curve.bx * 0.0625 + curve.cx * 0.25;
        double d10 = curve.ay * 0.015625 + curve.by * 0.0625 + curve.cy * 0.25;
        int n2 = 0;
        double d11 = CUB_DEC_BND;
        double d12 = CUB_INC_BND;
        double d13 = d;
        double d14 = d2;
        while (n > 0) {
            while (n % 2 == 0 && Math.abs(d7) + Math.abs(d8) <= d12) {
                d9 = 2.0 * d9 + d7;
                d10 = 2.0 * d10 + d8;
                d7 = 4.0 * (d7 + d5);
                d8 = 4.0 * (d8 + d6);
                d5 *= 8.0;
                d6 *= 8.0;
                n >>= 1;
                if (!DO_STATS) continue;
                this.rdrCtx.stats.stat_rdr_curveBreak_inc.add(n);
            }
            while (Math.abs(d7) + Math.abs(d8) >= d11) {
                d7 = d7 / 4.0 - (d5 /= 8.0);
                d8 = d8 / 4.0 - (d6 /= 8.0);
                d9 = (d9 - d7) / 2.0;
                d10 = (d10 - d8) / 2.0;
                n <<= 1;
                if (!DO_STATS) continue;
                this.rdrCtx.stats.stat_rdr_curveBreak_dec.add(n);
            }
            if (--n == 0) break;
            this.addLine(d, d2, d13 += (d9 += (d7 += d5)), d14 += (d10 += (d8 += d6)));
            d = d13;
            d2 = d14;
        }
        this.addLine(d, d2, d3, d4);
        if (DO_STATS) {
            this.rdrCtx.stats.stat_rdr_curveBreak.add(n2 + 1);
        }
    }

    private void addLine(double d, double d2, double d3, double d4) {
        double d5;
        int n;
        int n2;
        if (DO_STATS) {
            this.rdrCtx.stats.stat_rdr_addLine.add(1);
        }
        int n3 = 1;
        if (d4 < d2) {
            n3 = 0;
            double d6 = d4;
            d4 = d2;
            d2 = d6;
            d6 = d3;
            d3 = d;
            d = d6;
        }
        if ((n2 = FloatMath.max(FloatMath.ceil_int(d2), this.boundsMinY)) >= (n = FloatMath.min(FloatMath.ceil_int(d4), this.boundsMaxY))) {
            if (DO_STATS) {
                this.rdrCtx.stats.stat_rdr_addLine_skip.add(1);
            }
            return;
        }
        if (n2 < this.edgeMinY) {
            this.edgeMinY = n2;
        }
        if (n > this.edgeMaxY) {
            this.edgeMaxY = n;
        }
        if ((d5 = (d - d3) / (d2 - d4)) >= 0.0) {
            if (d < this.edgeMinX) {
                this.edgeMinX = d;
            }
            if (d3 > this.edgeMaxX) {
                this.edgeMaxX = d3;
            }
        } else {
            if (d3 < this.edgeMinX) {
                this.edgeMinX = d3;
            }
            if (d > this.edgeMaxX) {
                this.edgeMaxX = d;
            }
        }
        int n4 = SIZEOF_EDGE_BYTES;
        OffHeapArray offHeapArray = this.edges;
        int n5 = offHeapArray.used;
        if (offHeapArray.length - (long)n5 < (long)n4) {
            long l = ArrayCacheConst.getNewLargeSize(offHeapArray.length, n5 + n4);
            if (DO_STATS) {
                this.rdrCtx.stats.stat_rdr_edges_resizes.add(l);
            }
            offHeapArray.resize(l);
        }
        Unsafe unsafe = OffHeapArray.UNSAFE;
        long l = offHeapArray.address + (long)n5;
        double d7 = d + ((double)n2 - d2) * d5;
        long l2 = (long)(4.294967296E9 * d7) + Integer.MAX_VALUE;
        unsafe.putInt(l, (int)(l2 >> 31) & 0xFFFFFFFE | n3);
        unsafe.putInt(l += 4L, (int)l2 >>> 1);
        long l3 = (long)(4.294967296E9 * d5);
        unsafe.putInt(l += 4L, (int)(l3 >> 31) & 0xFFFFFFFE);
        unsafe.putInt(l += 4L, (int)l3 >>> 1);
        int[] arrn = this.edgeBuckets;
        int[] arrn2 = this.edgeBucketCounts;
        int n6 = this.boundsMinY;
        int n7 = n2 - n6;
        unsafe.putInt(l += 4L, arrn[n7]);
        unsafe.putInt(l += 4L, n);
        arrn[n7] = n5;
        int n8 = n7;
        arrn2[n8] = arrn2[n8] + 2;
        int n9 = n - n6;
        arrn2[n9] = arrn2[n9] | 1;
        offHeapArray.used += n4;
    }

    RendererNoAA(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.curve = rendererContext.curve;
        this.edges = rendererContext.rdrMem.edges;
        this.edgeBuckets_ref = rendererContext.rdrMem.edgeBuckets_ref;
        this.edgeBucketCounts_ref = rendererContext.rdrMem.edgeBucketCounts_ref;
        this.edgeBuckets = this.edgeBuckets_ref.initial;
        this.edgeBucketCounts = this.edgeBucketCounts_ref.initial;
        this.alphaLine_ref = rendererContext.rdrMem.alphaLine_ref;
        this.alphaLine = this.alphaLine_ref.initial;
        this.crossings_ref = rendererContext.rdrMem.crossings_ref;
        this.aux_crossings_ref = rendererContext.rdrMem.aux_crossings_ref;
        this.edgePtrs_ref = rendererContext.rdrMem.edgePtrs_ref;
        this.aux_edgePtrs_ref = rendererContext.rdrMem.aux_edgePtrs_ref;
        this.crossings = this.crossings_ref.initial;
        this.aux_crossings = this.aux_crossings_ref.initial;
        this.edgePtrs = this.edgePtrs_ref.initial;
        this.aux_edgePtrs = this.aux_edgePtrs_ref.initial;
        this.blkFlags_ref = rendererContext.rdrMem.blkFlags_ref;
        this.blkFlags = this.blkFlags_ref.initial;
    }

    @Override
    public RendererNoAA init(int n, int n2, int n3, int n4, int n5) {
        int n6;
        this.windingRule = n5;
        this.boundsMinX = n;
        this.boundsMaxX = n + n3;
        this.boundsMinY = n2;
        this.boundsMaxY = n2 + n4;
        if (DO_LOG_BOUNDS) {
            MarlinUtils.logInfo("boundsXY = [" + this.boundsMinX + " ... " + this.boundsMaxX + "[ [" + this.boundsMinY + " ... " + this.boundsMaxY + "[");
        }
        if ((n6 = this.boundsMaxY - this.boundsMinY + 1) > INITIAL_BUCKET_ARRAY) {
            if (DO_STATS) {
                this.rdrCtx.stats.stat_array_renderer_edgeBuckets.add(n6);
                this.rdrCtx.stats.stat_array_renderer_edgeBucketCounts.add(n6);
            }
            this.edgeBuckets = this.edgeBuckets_ref.getArray(n6);
            this.edgeBucketCounts = this.edgeBucketCounts_ref.getArray(n6);
        }
        this.edgeMinY = Integer.MAX_VALUE;
        this.edgeMaxY = Integer.MIN_VALUE;
        this.edgeMinX = Double.POSITIVE_INFINITY;
        this.edgeMaxX = Double.NEGATIVE_INFINITY;
        this.edgeCount = 0;
        this.activeEdgeMaxUsed = 0;
        this.edges.used = 0;
        this.bboxX0 = 0;
        this.bboxX1 = 0;
        return this;
    }

    @Override
    public void dispose() {
        if (DO_STATS) {
            this.rdrCtx.stats.stat_rdr_activeEdges.add(this.activeEdgeMaxUsed);
            this.rdrCtx.stats.stat_rdr_edges.add(this.edges.used);
            this.rdrCtx.stats.stat_rdr_edges_count.add(this.edges.used / SIZEOF_EDGE_BYTES);
            this.rdrCtx.stats.hist_rdr_edges_count.add(this.edges.used / SIZEOF_EDGE_BYTES);
            this.rdrCtx.stats.totalOffHeap += this.edges.length;
        }
        this.crossings = this.crossings_ref.putArray(this.crossings);
        this.aux_crossings = this.aux_crossings_ref.putArray(this.aux_crossings);
        this.edgePtrs = this.edgePtrs_ref.putArray(this.edgePtrs);
        this.aux_edgePtrs = this.aux_edgePtrs_ref.putArray(this.aux_edgePtrs);
        this.alphaLine = this.alphaLine_ref.putArray(this.alphaLine, 0, 0);
        this.blkFlags = this.blkFlags_ref.putArray(this.blkFlags, 0, 0);
        if (this.edgeMinY != Integer.MAX_VALUE) {
            if (this.rdrCtx.dirty) {
                this.buckets_minY = 0;
                this.buckets_maxY = this.boundsMaxY - this.boundsMinY;
            }
            this.edgeBuckets = this.edgeBuckets_ref.putArray(this.edgeBuckets, this.buckets_minY, this.buckets_maxY);
            this.edgeBucketCounts = this.edgeBucketCounts_ref.putArray(this.edgeBucketCounts, this.buckets_minY, this.buckets_maxY + 1);
        } else {
            this.edgeBuckets = this.edgeBuckets_ref.putArray(this.edgeBuckets, 0, 0);
            this.edgeBucketCounts = this.edgeBucketCounts_ref.putArray(this.edgeBucketCounts, 0, 0);
        }
        if (this.edges.length != (long)INITIAL_EDGES_CAPACITY) {
            this.edges.resize(INITIAL_EDGES_CAPACITY);
        }
    }

    private static double tosubpixx(double d) {
        return d;
    }

    private static double tosubpixy(double d) {
        return d - 0.5;
    }

    @Override
    public void moveTo(double d, double d2) {
        this.closePath();
        double d3 = RendererNoAA.tosubpixx(d);
        double d4 = RendererNoAA.tosubpixy(d2);
        this.sx0 = d3;
        this.sy0 = d4;
        this.x0 = d3;
        this.y0 = d4;
    }

    @Override
    public void lineTo(double d, double d2) {
        double d3 = RendererNoAA.tosubpixx(d);
        double d4 = RendererNoAA.tosubpixy(d2);
        this.addLine(this.x0, this.y0, d3, d4);
        this.x0 = d3;
        this.y0 = d4;
    }

    @Override
    public void curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = RendererNoAA.tosubpixx(d5);
        double d8 = RendererNoAA.tosubpixy(d6);
        this.curve.set(this.x0, this.y0, RendererNoAA.tosubpixx(d), RendererNoAA.tosubpixy(d2), RendererNoAA.tosubpixx(d3), RendererNoAA.tosubpixy(d4), d7, d8);
        this.curveBreakIntoLinesAndAdd(this.x0, this.y0, this.curve, d7, d8);
        this.x0 = d7;
        this.y0 = d8;
    }

    @Override
    public void quadTo(double d, double d2, double d3, double d4) {
        double d5 = RendererNoAA.tosubpixx(d3);
        double d6 = RendererNoAA.tosubpixy(d4);
        this.curve.set(this.x0, this.y0, RendererNoAA.tosubpixx(d), RendererNoAA.tosubpixy(d2), d5, d6);
        this.quadBreakIntoLinesAndAdd(this.x0, this.y0, this.curve, d5, d6);
        this.x0 = d5;
        this.y0 = d6;
    }

    @Override
    public void closePath() {
        if (this.x0 != this.sx0 || this.y0 != this.sy0) {
            this.addLine(this.x0, this.y0, this.sx0, this.sy0);
            this.x0 = this.sx0;
            this.y0 = this.sy0;
        }
    }

    @Override
    public void pathDone() {
        this.closePath();
        this.endRendering();
    }

    private void _endRendering(int n, int n2, MarlinAlphaConsumer marlinAlphaConsumer) {
        int n3 = this.bbox_spminX;
        int n4 = this.bbox_spmaxX;
        boolean bl = this.windingRule == 0;
        int[] arrn = this.alphaLine;
        OffHeapArray offHeapArray = this.edges;
        int[] arrn2 = this.edgeBuckets;
        int[] arrn3 = this.edgeBucketCounts;
        int[] arrn4 = this.crossings;
        int[] arrn5 = this.edgePtrs;
        int[] arrn6 = this.aux_crossings;
        int[] arrn7 = this.aux_edgePtrs;
        long l = OFF_ERROR;
        long l2 = OFF_BUMP_X;
        long l3 = OFF_BUMP_ERR;
        long l4 = OFF_NEXT;
        long l5 = OFF_YMAX;
        Unsafe unsafe = OffHeapArray.UNSAFE;
        long l6 = offHeapArray.address;
        int n5 = Integer.MAX_VALUE;
        int n6 = Integer.MIN_VALUE;
        int n7 = n;
        int n8 = n7 - this.boundsMinY;
        int n9 = this.edgeCount;
        int n10 = arrn5.length;
        int n11 = arrn4.length;
        int n12 = this.activeEdgeMaxUsed;
        int n13 = 0;
        int[] arrn8 = this.blkFlags;
        int n14 = BLOCK_SIZE_LG;
        int n15 = BLOCK_SIZE;
        boolean bl2 = ENABLE_BLOCK_FLAGS_HEURISTICS && this.enableBlkFlags;
        boolean bl3 = this.prevUseBlkFlags;
        int n16 = this.rdrCtx.stroking;
        int n17 = -1;
        while (n7 < n2) {
            int n18;
            int n19;
            long l7;
            int n20 = arrn3[n8];
            int n21 = n9;
            if (n20 != 0) {
                if (DO_STATS) {
                    this.rdrCtx.stats.stat_rdr_activeEdges_updates.add(n9);
                }
                if ((n20 & 1) != 0) {
                    l7 = l6 + l5;
                    int n22 = 0;
                    for (n19 = 0; n19 < n9; ++n19) {
                        n18 = arrn5[n19];
                        if (unsafe.getInt(l7 + (long)n18) <= n7) continue;
                        arrn5[n22++] = n18;
                    }
                    n21 = n9 = n22;
                }
                if ((n13 = n20 >> 1) != 0) {
                    int n23;
                    if (DO_STATS) {
                        this.rdrCtx.stats.stat_rdr_activeEdges_adds.add(n13);
                        if (n13 > 10) {
                            this.rdrCtx.stats.stat_rdr_activeEdges_adds_high.add(n13);
                        }
                    }
                    if (n10 < (n23 = n9 + n13)) {
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_edgePtrs.add(n23);
                        }
                        this.edgePtrs = arrn5 = this.edgePtrs_ref.widenArray(arrn5, n9, n23);
                        n10 = arrn5.length;
                        this.aux_edgePtrs_ref.putArray(arrn7);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_aux_edgePtrs.add(n23);
                        }
                        this.aux_edgePtrs = arrn7 = this.aux_edgePtrs_ref.getArray(ArrayCacheConst.getNewSize(n9, n23));
                    }
                    l7 = l6 + l4;
                    n18 = arrn2[n8];
                    while (n9 < n23) {
                        arrn5[n9] = n18;
                        n18 = unsafe.getInt(l7 + (long)n18);
                        ++n9;
                    }
                    if (n11 < n9) {
                        this.crossings_ref.putArray(arrn4);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_crossings.add(n9);
                        }
                        this.crossings = arrn4 = this.crossings_ref.getArray(n9);
                        this.aux_crossings_ref.putArray(arrn6);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_aux_crossings.add(n9);
                        }
                        this.aux_crossings = arrn6 = this.aux_crossings_ref.getArray(n9);
                        n11 = arrn4.length;
                    }
                    if (DO_STATS && n9 > n12) {
                        n12 = n9;
                    }
                }
            }
            if (n9 != 0) {
                int n24;
                int n25;
                int n26;
                int n27;
                int n28;
                int n29;
                int n30;
                if (n13 < 10 || n9 < 40) {
                    if (DO_STATS) {
                        this.rdrCtx.stats.hist_rdr_crossings.add(n9);
                        this.rdrCtx.stats.hist_rdr_crossings_adds.add(n13);
                    }
                    boolean bl4 = n9 >= 20;
                    n30 = Integer.MIN_VALUE;
                    for (n19 = 0; n19 < n9; ++n19) {
                        n18 = arrn5[n19];
                        l7 = l6 + (long)n18;
                        n28 = n29 = unsafe.getInt(l7);
                        n27 = unsafe.getInt(l7 + l) + unsafe.getInt(l7 + l3);
                        unsafe.putInt(l7, (n29 += unsafe.getInt(l7 + l2)) - (n27 >> 30 & 0xFFFFFFFE));
                        unsafe.putInt(l7 + l, n27 & Integer.MAX_VALUE);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_rdr_crossings_updates.add(n9);
                        }
                        if (n28 < n30) {
                            if (DO_STATS) {
                                this.rdrCtx.stats.stat_rdr_crossings_sorts.add(n19);
                            }
                            if (bl4 && n19 >= n21) {
                                if (DO_STATS) {
                                    this.rdrCtx.stats.stat_rdr_crossings_bsearch.add(n19);
                                }
                                int n31 = 0;
                                int n32 = n19 - 1;
                                do {
                                    int n33;
                                    if (arrn4[n33 = n31 + n32 >> 1] < n28) {
                                        n31 = n33 + 1;
                                        continue;
                                    }
                                    n32 = n33 - 1;
                                } while (n31 <= n32);
                                for (n26 = n19 - 1; n26 >= n31; --n26) {
                                    arrn4[n26 + 1] = arrn4[n26];
                                    arrn5[n26 + 1] = arrn5[n26];
                                }
                                arrn4[n31] = n28;
                                arrn5[n31] = n18;
                                continue;
                            }
                            n26 = n19 - 1;
                            arrn4[n19] = arrn4[n26];
                            arrn5[n19] = arrn5[n26];
                            while (--n26 >= 0 && arrn4[n26] > n28) {
                                arrn4[n26 + 1] = arrn4[n26];
                                arrn5[n26 + 1] = arrn5[n26];
                            }
                            arrn4[n26 + 1] = n28;
                            arrn5[n26 + 1] = n18;
                            continue;
                        }
                        arrn4[n19] = n30 = n28;
                    }
                } else {
                    if (DO_STATS) {
                        this.rdrCtx.stats.stat_rdr_crossings_msorts.add(n9);
                        this.rdrCtx.stats.hist_rdr_crossings_ratio.add(1000 * n13 / n9);
                        this.rdrCtx.stats.hist_rdr_crossings_msorts.add(n9);
                        this.rdrCtx.stats.hist_rdr_crossings_msorts_adds.add(n13);
                    }
                    n30 = Integer.MIN_VALUE;
                    for (n19 = 0; n19 < n9; ++n19) {
                        n18 = arrn5[n19];
                        l7 = l6 + (long)n18;
                        n28 = n29 = unsafe.getInt(l7);
                        n27 = unsafe.getInt(l7 + l) + unsafe.getInt(l7 + l3);
                        unsafe.putInt(l7, (n29 += unsafe.getInt(l7 + l2)) - (n27 >> 30 & 0xFFFFFFFE));
                        unsafe.putInt(l7 + l, n27 & Integer.MAX_VALUE);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_rdr_crossings_updates.add(n9);
                        }
                        if (n19 >= n21) {
                            arrn4[n19] = n28;
                            continue;
                        }
                        if (n28 < n30) {
                            if (DO_STATS) {
                                this.rdrCtx.stats.stat_rdr_crossings_sorts.add(n19);
                            }
                            n26 = n19 - 1;
                            arrn6[n19] = arrn6[n26];
                            arrn7[n19] = arrn7[n26];
                            while (--n26 >= 0 && arrn6[n26] > n28) {
                                arrn6[n26 + 1] = arrn6[n26];
                                arrn7[n26 + 1] = arrn7[n26];
                            }
                            arrn6[n26 + 1] = n28;
                            arrn7[n26 + 1] = n18;
                            continue;
                        }
                        arrn6[n19] = n30 = n28;
                        arrn7[n19] = n18;
                    }
                    MergeSort.mergeSortNoCopy(arrn4, arrn5, arrn6, arrn7, n9, n21);
                }
                n13 = 0;
                int n34 = arrn4[0];
                int n35 = n34 >> 1;
                if (n35 < n5) {
                    n5 = n35;
                }
                if ((n25 = arrn4[n9 - 1] >> 1) > n6) {
                    n6 = n25;
                }
                int n36 = n29 = n35;
                int n37 = ((n34 & 1) << 1) - 1;
                if (bl) {
                    n24 = n37;
                    for (n19 = 1; n19 < n9; ++n19) {
                        n34 = arrn4[n19];
                        n29 = n34 >> 1;
                        n37 = ((n34 & 1) << 1) - 1;
                        if ((n24 & 1) != 0) {
                            int n38 = n35 = n36 > n3 ? n36 : n3;
                            if (n29 < n4) {
                                n25 = n29;
                            } else {
                                n25 = n4;
                                n19 = n9;
                            }
                            if (n35 < n25) {
                                int n39 = n35 -= n3;
                                arrn[n39] = arrn[n39] + 1;
                                int n40 = n25 -= n3;
                                arrn[n40] = arrn[n40] - 1;
                                if (bl3) {
                                    arrn8[n35 >> n14] = 1;
                                    arrn8[n25 >> n14] = 1;
                                }
                            }
                        }
                        n24 += n37;
                        n36 = n29;
                    }
                } else {
                    n19 = 1;
                    n24 = 0;
                    while (true) {
                        if ((n24 += n37) != 0) {
                            if (n36 > n29) {
                                n36 = n29;
                            }
                        } else {
                            int n41 = n35 = n36 > n3 ? n36 : n3;
                            if (n29 < n4) {
                                n25 = n29;
                            } else {
                                n25 = n4;
                                n19 = n9;
                            }
                            if (n35 < n25) {
                                int n42 = n35 -= n3;
                                arrn[n42] = arrn[n42] + 1;
                                int n43 = n25 -= n3;
                                arrn[n43] = arrn[n43] - 1;
                                if (bl3) {
                                    arrn8[n35 >> n14] = 1;
                                    arrn8[n25 >> n14] = 1;
                                }
                            }
                            n36 = Integer.MAX_VALUE;
                        }
                        if (n19 == n9) break;
                        n34 = arrn4[n19];
                        n29 = n34 >> 1;
                        n37 = ((n34 & 1) << 1) - 1;
                        ++n19;
                    }
                }
            }
            n17 = n7;
            n5 = FloatMath.max(n5, n3);
            if ((n6 = FloatMath.min(n6, n4)) >= n5) {
                this.copyAARow(arrn, n17, n5, n6 + 1, bl3, marlinAlphaConsumer);
                if (bl2) {
                    boolean bl5 = bl3 = (n6 -= n5) > n15 && n6 > (n9 >> n16) - 1 << n14;
                    if (DO_STATS) {
                        int n44 = FloatMath.max(1, (n9 >> n16) - 1);
                        this.rdrCtx.stats.hist_tile_generator_encoding_dist.add(n6 / n44);
                    }
                }
            } else {
                marlinAlphaConsumer.clearAlphas(n17);
            }
            n5 = Integer.MAX_VALUE;
            n6 = Integer.MIN_VALUE;
            ++n7;
            ++n8;
        }
        --n7;
        n5 = FloatMath.max(n5, n3);
        if ((n6 = FloatMath.min(n6, n4)) >= n5) {
            this.copyAARow(arrn, n7, n5, n6 + 1, bl3, marlinAlphaConsumer);
        } else if (n7 != n17) {
            marlinAlphaConsumer.clearAlphas(n7);
        }
        this.edgeCount = n9;
        this.prevUseBlkFlags = bl3;
        if (DO_STATS) {
            this.activeEdgeMaxUsed = n12;
        }
    }

    void endRendering() {
        int n;
        if (this.edgeMinY == Integer.MAX_VALUE) {
            return;
        }
        int n2 = FloatMath.max(FloatMath.ceil_int(this.edgeMinX - 0.5), this.boundsMinX);
        int n3 = FloatMath.min(FloatMath.ceil_int(this.edgeMaxX - 0.5), this.boundsMaxX);
        int n4 = this.edgeMinY;
        int n5 = this.edgeMaxY;
        this.buckets_minY = n4 - this.boundsMinY;
        this.buckets_maxY = n5 - this.boundsMinY;
        if (DO_LOG_BOUNDS) {
            MarlinUtils.logInfo("edgesXY = [" + this.edgeMinX + " ... " + this.edgeMaxX + "[ [" + this.edgeMinY + " ... " + this.edgeMaxY + "[");
            MarlinUtils.logInfo("spXY    = [" + n2 + " ... " + n3 + "[ [" + n4 + " ... " + n5 + "[");
        }
        if (n2 >= n3 || n4 >= n5) {
            return;
        }
        int n6 = n2;
        int n7 = n3;
        int n8 = n4;
        int n9 = n5;
        this.initConsumer(n6, n8, n7, n9);
        if (ENABLE_BLOCK_FLAGS) {
            this.enableBlkFlags = this.useRLE;
            boolean bl = this.prevUseBlkFlags = this.enableBlkFlags && !ENABLE_BLOCK_FLAGS_HEURISTICS;
            if (this.enableBlkFlags && (n = (n7 - n6 >> BLOCK_SIZE_LG) + 2) > 256) {
                this.blkFlags = this.blkFlags_ref.getArray(n);
            }
        }
        this.bbox_spminX = n6;
        this.bbox_spmaxX = n7;
        this.bbox_spminY = n4;
        this.bbox_spmaxY = n5;
        if (DO_LOG_BOUNDS) {
            MarlinUtils.logInfo("pXY       = [" + n6 + " ... " + n7 + "[ [" + n8 + " ... " + n9 + "[");
            MarlinUtils.logInfo("bbox_spXY = [" + this.bbox_spminX + " ... " + this.bbox_spmaxX + "[ [" + this.bbox_spminY + " ... " + this.bbox_spmaxY + "[");
        }
        if ((n = n7 - n6 + 2) > INITIAL_AA_ARRAY) {
            if (DO_STATS) {
                this.rdrCtx.stats.stat_array_renderer_alphaline.add(n);
            }
            this.alphaLine = this.alphaLine_ref.getArray(n);
        }
    }

    void initConsumer(int n, int n2, int n3, int n4) {
        this.bboxX0 = n;
        this.bboxX1 = n3;
        this.bboxY0 = n2;
        this.bboxY1 = n4;
        int n5 = n3 - n;
        this.useRLE = FORCE_NO_RLE ? false : (FORCE_RLE ? true : n5 > RLE_MIN_WIDTH);
    }

    @Override
    public void produceAlphas(MarlinAlphaConsumer marlinAlphaConsumer) {
        marlinAlphaConsumer.setMaxAlpha(1);
        if (this.enableBlkFlags && !marlinAlphaConsumer.supportBlockFlags()) {
            this.enableBlkFlags = false;
            this.prevUseBlkFlags = false;
        }
        this._endRendering(this.bbox_spminY, this.bbox_spmaxY, marlinAlphaConsumer);
    }

    void copyAARow(int[] arrn, int n, int n2, int n3, boolean bl, MarlinAlphaConsumer marlinAlphaConsumer) {
        if (DO_STATS) {
            this.rdrCtx.stats.stat_cache_rowAA.add(n3 - n2);
        }
        if (bl) {
            if (DO_STATS) {
                this.rdrCtx.stats.hist_tile_generator_encoding.add(1);
            }
            marlinAlphaConsumer.setAndClearRelativeAlphas(this.blkFlags, arrn, n, n2, n3);
        } else {
            if (DO_STATS) {
                this.rdrCtx.stats.hist_tile_generator_encoding.add(0);
            }
            marlinAlphaConsumer.setAndClearRelativeAlphas(arrn, n, n2, n3);
        }
    }

    @Override
    public int getOutpixMinX() {
        return this.bboxX0;
    }

    @Override
    public int getOutpixMaxX() {
        return this.bboxX1;
    }

    @Override
    public int getOutpixMinY() {
        return this.bboxY0;
    }

    @Override
    public int getOutpixMaxY() {
        return this.bboxY1;
    }

    @Override
    public double getOffsetX() {
        return 0.5;
    }

    @Override
    public double getOffsetY() {
        return 0.5;
    }
}

