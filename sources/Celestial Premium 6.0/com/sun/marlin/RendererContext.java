/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.ByteArrayCache;
import com.sun.marlin.CollinearSimplifier;
import com.sun.marlin.Curve;
import com.sun.marlin.Dasher;
import com.sun.marlin.DoubleArrayCache;
import com.sun.marlin.IntArrayCache;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.MaskMarlinAlphaConsumer;
import com.sun.marlin.OffHeapArray;
import com.sun.marlin.PathSimplifier;
import com.sun.marlin.Renderer;
import com.sun.marlin.RendererNoAA;
import com.sun.marlin.RendererStats;
import com.sun.marlin.Stroker;
import com.sun.marlin.TransformingPathConsumer2D;
import com.sun.util.reentrant.ReentrantContext;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

public final class RendererContext
extends ReentrantContext
implements MarlinConst {
    private static final AtomicInteger CTX_COUNT = new AtomicInteger(1);
    private final Object cleanerObj;
    public boolean dirty = false;
    public final float[] float6 = new float[6];
    final Curve curve = new Curve();
    public final TransformingPathConsumer2D transformerPC2D;
    private WeakReference<Path2D> refPath2D = null;
    public final Renderer renderer;
    public final Stroker stroker;
    public final CollinearSimplifier simplifier = new CollinearSimplifier();
    public final PathSimplifier pathSimplifier = new PathSimplifier();
    public final Dasher dasher;
    int stroking = 0;
    public boolean doClip = false;
    boolean closedPath = false;
    public final double[] clipRect = new double[4];
    public double clipInvScale = 0.0;
    public final TransformingPathConsumer2D.CurveBasicMonotonizer monotonizer;
    final TransformingPathConsumer2D.CurveClipSplitter curveClipSplitter;
    final RendererSharedMemory rdrMem;
    private RendererNoAA rendererNoAA = null;
    public final Rectangle clip = new Rectangle();
    public MaskMarlinAlphaConsumer consumer = null;
    private final IntArrayCache cleanIntCache = new IntArrayCache(true, 5);
    private final IntArrayCache dirtyIntCache = new IntArrayCache(false, 5);
    private final DoubleArrayCache dirtyDoubleCache = new DoubleArrayCache(false, 4);
    private final ByteArrayCache dirtyByteCache = new ByteArrayCache(false, 2);
    final RendererStats stats;

    public static RendererContext createContext() {
        return new RendererContext("ctx" + Integer.toString(CTX_COUNT.getAndIncrement()));
    }

    RendererContext(String string) {
        if (LOG_CREATE_CONTEXT) {
            MarlinUtils.logInfo("new RendererContext = " + string);
        }
        this.cleanerObj = new Object();
        if (DO_STATS) {
            this.stats = RendererStats.createInstance(this.cleanerObj, string);
            this.stats.cacheStats = new ArrayCacheConst.CacheStats[]{this.cleanIntCache.stats, this.dirtyIntCache.stats, this.dirtyDoubleCache.stats, this.dirtyByteCache.stats};
        } else {
            this.stats = null;
        }
        this.monotonizer = new TransformingPathConsumer2D.CurveBasicMonotonizer(this);
        this.curveClipSplitter = new TransformingPathConsumer2D.CurveClipSplitter(this);
        this.transformerPC2D = new TransformingPathConsumer2D(this);
        this.rdrMem = new RendererSharedMemory(this);
        this.renderer = new Renderer(this);
        this.stroker = new Stroker(this);
        this.dasher = new Dasher(this);
    }

    public void dispose() {
        if (DO_STATS) {
            if (this.stats.totalOffHeap > this.stats.totalOffHeapMax) {
                this.stats.totalOffHeapMax = this.stats.totalOffHeap;
            }
            this.stats.totalOffHeap = 0L;
        }
        this.stroking = 0;
        this.doClip = false;
        this.closedPath = false;
        this.clipInvScale = 0.0;
        if (this.dirty) {
            this.dasher.dispose();
            this.stroker.dispose();
            this.dirty = false;
        }
    }

    public Path2D getPath2D() {
        Path2D path2D;
        Path2D path2D2 = path2D = this.refPath2D != null ? (Path2D)this.refPath2D.get() : null;
        if (path2D == null) {
            path2D = new Path2D(1, INITIAL_EDGES_COUNT);
            this.refPath2D = new WeakReference<Path2D>(path2D);
        }
        path2D.reset();
        return path2D;
    }

    public RendererNoAA getRendererNoAA() {
        if (this.rendererNoAA == null) {
            this.rendererNoAA = new RendererNoAA(this);
        }
        return this.rendererNoAA;
    }

    OffHeapArray newOffHeapArray(long l) {
        if (DO_STATS) {
            this.stats.totalOffHeapInitial += l;
        }
        return new OffHeapArray(this.cleanerObj, l);
    }

    IntArrayCache.Reference newCleanIntArrayRef(int n) {
        return this.cleanIntCache.createRef(n);
    }

    IntArrayCache.Reference newDirtyIntArrayRef(int n) {
        return this.dirtyIntCache.createRef(n);
    }

    DoubleArrayCache.Reference newDirtyDoubleArrayRef(int n) {
        return this.dirtyDoubleCache.createRef(n);
    }

    ByteArrayCache.Reference newDirtyByteArrayRef(int n) {
        return this.dirtyByteCache.createRef(n);
    }

    static final class RendererSharedMemory {
        final OffHeapArray edges;
        final IntArrayCache.Reference edgeBuckets_ref;
        final IntArrayCache.Reference edgeBucketCounts_ref;
        final IntArrayCache.Reference alphaLine_ref;
        final IntArrayCache.Reference crossings_ref;
        final IntArrayCache.Reference edgePtrs_ref;
        final IntArrayCache.Reference aux_crossings_ref;
        final IntArrayCache.Reference aux_edgePtrs_ref;
        final IntArrayCache.Reference blkFlags_ref;

        RendererSharedMemory(RendererContext rendererContext) {
            this.edges = rendererContext.newOffHeapArray(MarlinConst.INITIAL_EDGES_CAPACITY);
            this.edgeBuckets_ref = rendererContext.newCleanIntArrayRef(MarlinConst.INITIAL_BUCKET_ARRAY);
            this.edgeBucketCounts_ref = rendererContext.newCleanIntArrayRef(MarlinConst.INITIAL_BUCKET_ARRAY);
            this.alphaLine_ref = rendererContext.newCleanIntArrayRef(MarlinConst.INITIAL_AA_ARRAY);
            this.crossings_ref = rendererContext.newDirtyIntArrayRef(MarlinConst.INITIAL_CROSSING_COUNT);
            this.aux_crossings_ref = rendererContext.newDirtyIntArrayRef(MarlinConst.INITIAL_CROSSING_COUNT);
            this.edgePtrs_ref = rendererContext.newDirtyIntArrayRef(MarlinConst.INITIAL_CROSSING_COUNT);
            this.aux_edgePtrs_ref = rendererContext.newDirtyIntArrayRef(MarlinConst.INITIAL_CROSSING_COUNT);
            this.blkFlags_ref = rendererContext.newCleanIntArrayRef(256);
        }
    }
}

