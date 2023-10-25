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

public final class Renderer
implements MarlinRenderer,
MarlinConst {
    static final boolean DISABLE_RENDER = false;
    private static final int ALL_BUT_LSB = -2;
    private static final int ERR_STEP_MAX = Integer.MAX_VALUE;
    private static final double POWER_2_TO_32 = 4.294967296E9;
    static final double SUBPIXEL_SCALE_X = SUBPIXEL_POSITIONS_X;
    static final double SUBPIXEL_SCALE_Y = SUBPIXEL_POSITIONS_Y;
    static final int SUBPIXEL_MASK_X = SUBPIXEL_POSITIONS_X - 1;
    static final int SUBPIXEL_MASK_Y = SUBPIXEL_POSITIONS_Y - 1;
    private static final double RDR_OFFSET_X = 0.5 / SUBPIXEL_SCALE_X;
    private static final double RDR_OFFSET_Y = 0.5 / SUBPIXEL_SCALE_Y;
    public static final long OFF_CURX_OR = 0L;
    public static final long OFF_ERROR = 0L + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_X = OFF_ERROR + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_ERR = OFF_BUMP_X + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_NEXT = OFF_BUMP_ERR + (long)OffHeapArray.SIZE_INT;
    public static final long OFF_YMAX = OFF_NEXT + (long)OffHeapArray.SIZE_INT;
    public static final int SIZEOF_EDGE_BYTES = (int)(OFF_YMAX + (long)OffHeapArray.SIZE_INT);
    private static final double CUB_DEC_ERR_SUBPIX = (double)MarlinProperties.getCubicDecD2() * ((double)SUBPIXEL_POSITIONS_X / 8.0);
    private static final double CUB_INC_ERR_SUBPIX = (double)MarlinProperties.getCubicIncD1() * ((double)SUBPIXEL_POSITIONS_X / 8.0);
    public static final double SCALE_DY = (double)SUBPIXEL_POSITIONS_X / (double)SUBPIXEL_POSITIONS_Y;
    public static final double CUB_DEC_BND = 8.0 * CUB_DEC_ERR_SUBPIX;
    public static final double CUB_INC_BND = 8.0 * CUB_INC_ERR_SUBPIX;
    public static final int CUB_COUNT_LG = 2;
    private static final int CUB_COUNT = 4;
    private static final int CUB_COUNT_2 = 16;
    private static final int CUB_COUNT_3 = 64;
    private static final double CUB_INV_COUNT = 0.25;
    private static final double CUB_INV_COUNT_2 = 0.0625;
    private static final double CUB_INV_COUNT_3 = 0.015625;
    private static final double QUAD_DEC_ERR_SUBPIX = (double)MarlinProperties.getQuadDecD2() * ((double)SUBPIXEL_POSITIONS_X / 8.0);
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
        double d5 = Math.abs(curve.dbx) + Math.abs(curve.dby) * SCALE_DY;
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
        double d13 = SCALE_DY;
        double d14 = d;
        double d15 = d2;
        while (n > 0) {
            while (n % 2 == 0 && Math.abs(d7) + Math.abs(d8) * d13 <= d12) {
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
            while (Math.abs(d7) + Math.abs(d8) * d13 >= d11) {
                d7 = d7 / 4.0 - (d5 /= 8.0);
                d8 = d8 / 4.0 - (d6 /= 8.0);
                d9 = (d9 - d7) / 2.0;
                d10 = (d10 - d8) / 2.0;
                n <<= 1;
                if (!DO_STATS) continue;
                this.rdrCtx.stats.stat_rdr_curveBreak_dec.add(n);
            }
            if (--n == 0) break;
            this.addLine(d, d2, d14 += (d9 += (d7 += d5)), d15 += (d10 += (d8 += d6)));
            d = d14;
            d2 = d15;
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

    Renderer(RendererContext rendererContext) {
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
    public Renderer init(int n, int n2, int n3, int n4, int n5) {
        int n6;
        this.windingRule = n5;
        this.boundsMinX = n << SUBPIXEL_LG_POSITIONS_X;
        this.boundsMaxX = n + n3 << SUBPIXEL_LG_POSITIONS_X;
        this.boundsMinY = n2 << SUBPIXEL_LG_POSITIONS_Y;
        this.boundsMaxY = n2 + n4 << SUBPIXEL_LG_POSITIONS_Y;
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
        return SUBPIXEL_SCALE_X * d;
    }

    private static double tosubpixy(double d) {
        return SUBPIXEL_SCALE_Y * d - 0.5;
    }

    @Override
    public void moveTo(double d, double d2) {
        this.closePath();
        double d3 = Renderer.tosubpixx(d);
        double d4 = Renderer.tosubpixy(d2);
        this.sx0 = d3;
        this.sy0 = d4;
        this.x0 = d3;
        this.y0 = d4;
    }

    @Override
    public void lineTo(double d, double d2) {
        double d3 = Renderer.tosubpixx(d);
        double d4 = Renderer.tosubpixy(d2);
        this.addLine(this.x0, this.y0, d3, d4);
        this.x0 = d3;
        this.y0 = d4;
    }

    @Override
    public void curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = Renderer.tosubpixx(d5);
        double d8 = Renderer.tosubpixy(d6);
        this.curve.set(this.x0, this.y0, Renderer.tosubpixx(d), Renderer.tosubpixy(d2), Renderer.tosubpixx(d3), Renderer.tosubpixy(d4), d7, d8);
        this.curveBreakIntoLinesAndAdd(this.x0, this.y0, this.curve, d7, d8);
        this.x0 = d7;
        this.y0 = d8;
    }

    @Override
    public void quadTo(double d, double d2, double d3, double d4) {
        double d5 = Renderer.tosubpixx(d3);
        double d6 = Renderer.tosubpixy(d4);
        this.curve.set(this.x0, this.y0, Renderer.tosubpixx(d), Renderer.tosubpixy(d2), d5, d6);
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
        int n5 = SUBPIXEL_LG_POSITIONS_X;
        int n6 = SUBPIXEL_LG_POSITIONS_Y;
        int n7 = SUBPIXEL_MASK_X;
        int n8 = SUBPIXEL_MASK_Y;
        int n9 = SUBPIXEL_POSITIONS_X;
        int n10 = Integer.MAX_VALUE;
        int n11 = Integer.MIN_VALUE;
        int n12 = n;
        int n13 = n12 - this.boundsMinY;
        int n14 = this.edgeCount;
        int n15 = arrn5.length;
        int n16 = arrn4.length;
        int n17 = this.activeEdgeMaxUsed;
        int n18 = 0;
        int[] arrn8 = this.blkFlags;
        int n19 = BLOCK_SIZE_LG;
        int n20 = BLOCK_SIZE;
        boolean bl2 = ENABLE_BLOCK_FLAGS_HEURISTICS && this.enableBlkFlags;
        boolean bl3 = this.prevUseBlkFlags;
        int n21 = this.rdrCtx.stroking;
        int n22 = -1;
        while (n12 < n2) {
            int n23;
            int n24;
            int n25;
            long l7;
            int n26 = arrn3[n13];
            int n27 = n14;
            if (n26 != 0) {
                if (DO_STATS) {
                    this.rdrCtx.stats.stat_rdr_activeEdges_updates.add(n14);
                }
                if ((n26 & 1) != 0) {
                    l7 = l6 + l5;
                    int n28 = 0;
                    for (n25 = 0; n25 < n14; ++n25) {
                        n24 = arrn5[n25];
                        if (unsafe.getInt(l7 + (long)n24) <= n12) continue;
                        arrn5[n28++] = n24;
                    }
                    n27 = n14 = n28;
                }
                if ((n18 = n26 >> 1) != 0) {
                    int n29;
                    if (DO_STATS) {
                        this.rdrCtx.stats.stat_rdr_activeEdges_adds.add(n18);
                        if (n18 > 10) {
                            this.rdrCtx.stats.stat_rdr_activeEdges_adds_high.add(n18);
                        }
                    }
                    if (n15 < (n29 = n14 + n18)) {
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_edgePtrs.add(n29);
                        }
                        this.edgePtrs = arrn5 = this.edgePtrs_ref.widenArray(arrn5, n14, n29);
                        n15 = arrn5.length;
                        this.aux_edgePtrs_ref.putArray(arrn7);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_aux_edgePtrs.add(n29);
                        }
                        this.aux_edgePtrs = arrn7 = this.aux_edgePtrs_ref.getArray(ArrayCacheConst.getNewSize(n14, n29));
                    }
                    l7 = l6 + l4;
                    n24 = arrn2[n13];
                    while (n14 < n29) {
                        arrn5[n14] = n24;
                        n24 = unsafe.getInt(l7 + (long)n24);
                        ++n14;
                    }
                    if (n16 < n14) {
                        this.crossings_ref.putArray(arrn4);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_crossings.add(n14);
                        }
                        this.crossings = arrn4 = this.crossings_ref.getArray(n14);
                        this.aux_crossings_ref.putArray(arrn6);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_array_renderer_aux_crossings.add(n14);
                        }
                        this.aux_crossings = arrn6 = this.aux_crossings_ref.getArray(n14);
                        n16 = arrn4.length;
                    }
                    if (DO_STATS && n14 > n17) {
                        n17 = n14;
                    }
                }
            }
            if (n14 != 0) {
                int n30;
                int n31;
                int n32;
                int n33;
                int n34;
                int n35;
                int n36;
                int n37;
                int n38;
                int n39;
                if (n18 < 10 || n14 < 40) {
                    if (DO_STATS) {
                        this.rdrCtx.stats.hist_rdr_crossings.add(n14);
                        this.rdrCtx.stats.hist_rdr_crossings_adds.add(n18);
                    }
                    boolean bl4 = n14 >= 20;
                    n39 = Integer.MIN_VALUE;
                    for (n25 = 0; n25 < n14; ++n25) {
                        n24 = arrn5[n25];
                        l7 = l6 + (long)n24;
                        n37 = n38 = unsafe.getInt(l7);
                        n36 = unsafe.getInt(l7 + l) + unsafe.getInt(l7 + l3);
                        unsafe.putInt(l7, (n38 += unsafe.getInt(l7 + l2)) - (n36 >> 30 & 0xFFFFFFFE));
                        unsafe.putInt(l7 + l, n36 & Integer.MAX_VALUE);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_rdr_crossings_updates.add(n14);
                        }
                        if (n37 < n39) {
                            if (DO_STATS) {
                                this.rdrCtx.stats.stat_rdr_crossings_sorts.add(n25);
                            }
                            if (bl4 && n25 >= n27) {
                                if (DO_STATS) {
                                    this.rdrCtx.stats.stat_rdr_crossings_bsearch.add(n25);
                                }
                                int n40 = 0;
                                int n41 = n25 - 1;
                                do {
                                    int n42;
                                    if (arrn4[n42 = n40 + n41 >> 1] < n37) {
                                        n40 = n42 + 1;
                                        continue;
                                    }
                                    n41 = n42 - 1;
                                } while (n40 <= n41);
                                for (n35 = n25 - 1; n35 >= n40; --n35) {
                                    arrn4[n35 + 1] = arrn4[n35];
                                    arrn5[n35 + 1] = arrn5[n35];
                                }
                                arrn4[n40] = n37;
                                arrn5[n40] = n24;
                                continue;
                            }
                            n35 = n25 - 1;
                            arrn4[n25] = arrn4[n35];
                            arrn5[n25] = arrn5[n35];
                            while (--n35 >= 0 && arrn4[n35] > n37) {
                                arrn4[n35 + 1] = arrn4[n35];
                                arrn5[n35 + 1] = arrn5[n35];
                            }
                            arrn4[n35 + 1] = n37;
                            arrn5[n35 + 1] = n24;
                            continue;
                        }
                        arrn4[n25] = n39 = n37;
                    }
                } else {
                    if (DO_STATS) {
                        this.rdrCtx.stats.stat_rdr_crossings_msorts.add(n14);
                        this.rdrCtx.stats.hist_rdr_crossings_ratio.add(1000 * n18 / n14);
                        this.rdrCtx.stats.hist_rdr_crossings_msorts.add(n14);
                        this.rdrCtx.stats.hist_rdr_crossings_msorts_adds.add(n18);
                    }
                    n39 = Integer.MIN_VALUE;
                    for (n25 = 0; n25 < n14; ++n25) {
                        n24 = arrn5[n25];
                        l7 = l6 + (long)n24;
                        n37 = n38 = unsafe.getInt(l7);
                        n36 = unsafe.getInt(l7 + l) + unsafe.getInt(l7 + l3);
                        unsafe.putInt(l7, (n38 += unsafe.getInt(l7 + l2)) - (n36 >> 30 & 0xFFFFFFFE));
                        unsafe.putInt(l7 + l, n36 & Integer.MAX_VALUE);
                        if (DO_STATS) {
                            this.rdrCtx.stats.stat_rdr_crossings_updates.add(n14);
                        }
                        if (n25 >= n27) {
                            arrn4[n25] = n37;
                            continue;
                        }
                        if (n37 < n39) {
                            if (DO_STATS) {
                                this.rdrCtx.stats.stat_rdr_crossings_sorts.add(n25);
                            }
                            n35 = n25 - 1;
                            arrn6[n25] = arrn6[n35];
                            arrn7[n25] = arrn7[n35];
                            while (--n35 >= 0 && arrn6[n35] > n37) {
                                arrn6[n35 + 1] = arrn6[n35];
                                arrn7[n35 + 1] = arrn7[n35];
                            }
                            arrn6[n35 + 1] = n37;
                            arrn7[n35 + 1] = n24;
                            continue;
                        }
                        arrn6[n25] = n39 = n37;
                        arrn7[n25] = n24;
                    }
                    MergeSort.mergeSortNoCopy(arrn4, arrn5, arrn6, arrn7, n14, n27);
                }
                n18 = 0;
                int n43 = arrn4[0];
                int n44 = n43 >> 1;
                if (n44 < n10) {
                    n10 = n44;
                }
                if ((n34 = arrn4[n14 - 1] >> 1) > n11) {
                    n11 = n34;
                }
                int n45 = n38 = n44;
                int n46 = ((n43 & 1) << 1) - 1;
                if (bl) {
                    n33 = n46;
                    for (n25 = 1; n25 < n14; ++n25) {
                        n43 = arrn4[n25];
                        n38 = n43 >> 1;
                        n46 = ((n43 & 1) << 1) - 1;
                        if ((n33 & 1) != 0) {
                            int n47 = n44 = n45 > n3 ? n45 : n3;
                            if (n38 < n4) {
                                n34 = n38;
                            } else {
                                n34 = n4;
                                n25 = n14;
                            }
                            if (n44 < n34) {
                                n32 = (n44 -= n3) >> n5;
                                n31 = (n34 -= n3) - 1 >> n5;
                                if (n32 == n31) {
                                    n23 = n34 - n44;
                                    int n48 = n32;
                                    arrn[n48] = arrn[n48] + n23;
                                    int n49 = n32 + 1;
                                    arrn[n49] = arrn[n49] - n23;
                                    if (bl3) {
                                        arrn8[n32 >> n19] = 1;
                                    }
                                } else {
                                    n23 = n44 & n7;
                                    int n50 = n32;
                                    arrn[n50] = arrn[n50] + (n9 - n23);
                                    int n51 = n32 + 1;
                                    arrn[n51] = arrn[n51] + n23;
                                    n30 = n34 >> n5;
                                    n23 = n34 & n7;
                                    int n52 = n30;
                                    arrn[n52] = arrn[n52] - (n9 - n23);
                                    int n53 = n30 + 1;
                                    arrn[n53] = arrn[n53] - n23;
                                    if (bl3) {
                                        arrn8[n32 >> n19] = 1;
                                        arrn8[n30 >> n19] = 1;
                                    }
                                }
                            }
                        }
                        n33 += n46;
                        n45 = n38;
                    }
                } else {
                    n25 = 1;
                    n33 = 0;
                    while (true) {
                        if ((n33 += n46) != 0) {
                            if (n45 > n38) {
                                n45 = n38;
                            }
                        } else {
                            int n54 = n44 = n45 > n3 ? n45 : n3;
                            if (n38 < n4) {
                                n34 = n38;
                            } else {
                                n34 = n4;
                                n25 = n14;
                            }
                            if (n44 < n34) {
                                n32 = (n44 -= n3) >> n5;
                                n31 = (n34 -= n3) - 1 >> n5;
                                if (n32 == n31) {
                                    n23 = n34 - n44;
                                    int n55 = n32;
                                    arrn[n55] = arrn[n55] + n23;
                                    int n56 = n32 + 1;
                                    arrn[n56] = arrn[n56] - n23;
                                    if (bl3) {
                                        arrn8[n32 >> n19] = 1;
                                    }
                                } else {
                                    n23 = n44 & n7;
                                    int n57 = n32;
                                    arrn[n57] = arrn[n57] + (n9 - n23);
                                    int n58 = n32 + 1;
                                    arrn[n58] = arrn[n58] + n23;
                                    n30 = n34 >> n5;
                                    n23 = n34 & n7;
                                    int n59 = n30;
                                    arrn[n59] = arrn[n59] - (n9 - n23);
                                    int n60 = n30 + 1;
                                    arrn[n60] = arrn[n60] - n23;
                                    if (bl3) {
                                        arrn8[n32 >> n19] = 1;
                                        arrn8[n30 >> n19] = 1;
                                    }
                                }
                            }
                            n45 = Integer.MAX_VALUE;
                        }
                        if (n25 == n14) break;
                        n43 = arrn4[n25];
                        n38 = n43 >> 1;
                        n46 = ((n43 & 1) << 1) - 1;
                        ++n25;
                    }
                }
            }
            if ((n12 & n8) == n8) {
                n22 = n12 >> n6;
                n10 = FloatMath.max(n10, n3) >> n5;
                if ((n11 = FloatMath.min(n11, n4) >> n5) >= n10) {
                    this.copyAARow(arrn, n22, n10, n11 + 1, bl3, marlinAlphaConsumer);
                    if (bl2) {
                        boolean bl5 = bl3 = (n11 -= n10) > n20 && n11 > (n14 >> n21) - 1 << n19;
                        if (DO_STATS) {
                            n23 = FloatMath.max(1, (n14 >> n21) - 1);
                            this.rdrCtx.stats.hist_tile_generator_encoding_dist.add(n11 / n23);
                        }
                    }
                } else {
                    marlinAlphaConsumer.clearAlphas(n22);
                }
                n10 = Integer.MAX_VALUE;
                n11 = Integer.MIN_VALUE;
            }
            ++n12;
            ++n13;
        }
        --n12;
        n12 >>= n6;
        n10 = FloatMath.max(n10, n3) >> n5;
        if ((n11 = FloatMath.min(n11, n4) >> n5) >= n10) {
            this.copyAARow(arrn, n12, n10, n11 + 1, bl3, marlinAlphaConsumer);
        } else if (n12 != n22) {
            marlinAlphaConsumer.clearAlphas(n12);
        }
        this.edgeCount = n14;
        this.prevUseBlkFlags = bl3;
        if (DO_STATS) {
            this.activeEdgeMaxUsed = n17;
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
        int n6 = n2 >> SUBPIXEL_LG_POSITIONS_X;
        int n7 = n3 + SUBPIXEL_MASK_X >> SUBPIXEL_LG_POSITIONS_X;
        int n8 = n4 >> SUBPIXEL_LG_POSITIONS_Y;
        int n9 = n5 + SUBPIXEL_MASK_Y >> SUBPIXEL_LG_POSITIONS_Y;
        this.initConsumer(n6, n8, n7, n9);
        if (ENABLE_BLOCK_FLAGS) {
            this.enableBlkFlags = this.useRLE;
            boolean bl = this.prevUseBlkFlags = this.enableBlkFlags && !ENABLE_BLOCK_FLAGS_HEURISTICS;
            if (this.enableBlkFlags && (n = (n7 - n6 >> BLOCK_SIZE_LG) + 2) > 256) {
                this.blkFlags = this.blkFlags_ref.getArray(n);
            }
        }
        this.bbox_spminX = n6 << SUBPIXEL_LG_POSITIONS_X;
        this.bbox_spmaxX = n7 << SUBPIXEL_LG_POSITIONS_X;
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
        marlinAlphaConsumer.setMaxAlpha(MAX_AA_ALPHA);
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
        return RDR_OFFSET_X;
    }

    @Override
    public double getOffsetY() {
        return RDR_OFFSET_Y;
    }
}

