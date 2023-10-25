/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.stats.Histogram;
import com.sun.marlin.stats.Monitor;
import com.sun.marlin.stats.StatLong;
import java.security.AccessController;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class RendererStats
implements MarlinConst {
    final String name;
    final StatLong stat_cache_rowAA = new StatLong("cache.rowAA");
    final StatLong stat_cache_rowAAChunk = new StatLong("cache.rowAAChunk");
    final StatLong stat_cache_tiles = new StatLong("cache.tiles");
    final StatLong stat_rdr_addLine = new StatLong("renderer.addLine");
    final StatLong stat_rdr_addLine_skip = new StatLong("renderer.addLine.skip");
    final StatLong stat_rdr_curveBreak = new StatLong("renderer.curveBreakIntoLinesAndAdd");
    final StatLong stat_rdr_curveBreak_dec = new StatLong("renderer.curveBreakIntoLinesAndAdd.dec");
    final StatLong stat_rdr_curveBreak_inc = new StatLong("renderer.curveBreakIntoLinesAndAdd.inc");
    final StatLong stat_rdr_quadBreak = new StatLong("renderer.quadBreakIntoLinesAndAdd");
    final StatLong stat_rdr_quadBreak_dec = new StatLong("renderer.quadBreakIntoLinesAndAdd.dec");
    final StatLong stat_rdr_edges = new StatLong("renderer.edges");
    final StatLong stat_rdr_edges_count = new StatLong("renderer.edges.count");
    final StatLong stat_rdr_edges_resizes = new StatLong("renderer.edges.resize");
    final StatLong stat_rdr_activeEdges = new StatLong("renderer.activeEdges");
    final StatLong stat_rdr_activeEdges_updates = new StatLong("renderer.activeEdges.updates");
    final StatLong stat_rdr_activeEdges_adds = new StatLong("renderer.activeEdges.adds");
    final StatLong stat_rdr_activeEdges_adds_high = new StatLong("renderer.activeEdges.adds_high");
    final StatLong stat_rdr_crossings_updates = new StatLong("renderer.crossings.updates");
    final StatLong stat_rdr_crossings_sorts = new StatLong("renderer.crossings.sorts");
    final StatLong stat_rdr_crossings_bsearch = new StatLong("renderer.crossings.bsearch");
    final StatLong stat_rdr_crossings_msorts = new StatLong("renderer.crossings.msorts");
    final StatLong stat_str_polystack_curves = new StatLong("stroker.polystack.curves");
    final StatLong stat_str_polystack_types = new StatLong("stroker.polystack.types");
    final StatLong stat_cpd_polystack_curves = new StatLong("closedPathDetector.polystack.curves");
    final StatLong stat_cpd_polystack_types = new StatLong("closedPathDetector.polystack.types");
    final StatLong stat_pcf_idxstack_indices = new StatLong("pathClipFilter.stack.indices");
    final StatLong stat_array_dasher_dasher = new StatLong("array.dasher.dasher.d_float");
    final StatLong stat_array_dasher_firstSegmentsBuffer = new StatLong("array.dasher.firstSegmentsBuffer.d_float");
    final StatLong stat_array_marlincache_rowAAChunk = new StatLong("array.marlincache.rowAAChunk.resize");
    final StatLong stat_array_marlincache_touchedTile = new StatLong("array.marlincache.touchedTile.int");
    final StatLong stat_array_renderer_alphaline = new StatLong("array.renderer.alphaline.int");
    final StatLong stat_array_renderer_crossings = new StatLong("array.renderer.crossings.int");
    final StatLong stat_array_renderer_aux_crossings = new StatLong("array.renderer.aux_crossings.int");
    final StatLong stat_array_renderer_edgeBuckets = new StatLong("array.renderer.edgeBuckets.int");
    final StatLong stat_array_renderer_edgeBucketCounts = new StatLong("array.renderer.edgeBucketCounts.int");
    final StatLong stat_array_renderer_edgePtrs = new StatLong("array.renderer.edgePtrs.int");
    final StatLong stat_array_renderer_aux_edgePtrs = new StatLong("array.renderer.aux_edgePtrs.int");
    final StatLong stat_array_str_polystack_curves = new StatLong("array.stroker.polystack.curves.d_float");
    final StatLong stat_array_str_polystack_types = new StatLong("array.stroker.polystack.curveTypes.d_byte");
    final StatLong stat_array_cpd_polystack_curves = new StatLong("array.closedPathDetector.polystack.curves.d_float");
    final StatLong stat_array_cpd_polystack_types = new StatLong("array.closedPathDetector.polystack.curveTypes.d_byte");
    final StatLong stat_array_pcf_idxstack_indices = new StatLong("array.pathClipFilter.stack.indices.d_int");
    final Histogram hist_rdr_edges_count = new Histogram("renderer.edges.count");
    final Histogram hist_rdr_crossings = new Histogram("renderer.crossings");
    final Histogram hist_rdr_crossings_ratio = new Histogram("renderer.crossings.ratio");
    final Histogram hist_rdr_crossings_adds = new Histogram("renderer.crossings.adds");
    final Histogram hist_rdr_crossings_msorts = new Histogram("renderer.crossings.msorts");
    final Histogram hist_rdr_crossings_msorts_adds = new Histogram("renderer.crossings.msorts.adds");
    final Histogram hist_str_polystack_curves = new Histogram("stroker.polystack.curves");
    final Histogram hist_tile_generator_alpha = new Histogram("tile_generator.alpha");
    final Histogram hist_tile_generator_encoding = new Histogram("tile_generator.encoding");
    final Histogram hist_tile_generator_encoding_dist = new Histogram("tile_generator.encoding.dist");
    final Histogram hist_tile_generator_encoding_ratio = new Histogram("tile_generator.encoding.ratio");
    final Histogram hist_tile_generator_encoding_runLen = new Histogram("tile_generator.encoding.runLen");
    final Histogram hist_cpd_polystack_curves = new Histogram("closedPathDetector.polystack.curves");
    final Histogram hist_pcf_idxstack_indices = new Histogram("pathClipFilter.stack.indices");
    final StatLong[] statistics = new StatLong[]{this.stat_cache_rowAA, this.stat_cache_rowAAChunk, this.stat_cache_tiles, this.stat_rdr_addLine, this.stat_rdr_addLine_skip, this.stat_rdr_curveBreak, this.stat_rdr_curveBreak_dec, this.stat_rdr_curveBreak_inc, this.stat_rdr_quadBreak, this.stat_rdr_quadBreak_dec, this.stat_rdr_edges, this.stat_rdr_edges_count, this.stat_rdr_edges_resizes, this.stat_rdr_activeEdges, this.stat_rdr_activeEdges_updates, this.stat_rdr_activeEdges_adds, this.stat_rdr_activeEdges_adds_high, this.stat_rdr_crossings_updates, this.stat_rdr_crossings_sorts, this.stat_rdr_crossings_bsearch, this.stat_rdr_crossings_msorts, this.stat_str_polystack_types, this.stat_str_polystack_curves, this.stat_cpd_polystack_curves, this.stat_cpd_polystack_types, this.stat_pcf_idxstack_indices, this.hist_rdr_edges_count, this.hist_rdr_crossings, this.hist_rdr_crossings_ratio, this.hist_rdr_crossings_adds, this.hist_rdr_crossings_msorts, this.hist_rdr_crossings_msorts_adds, this.hist_tile_generator_alpha, this.hist_tile_generator_encoding, this.hist_tile_generator_encoding_dist, this.hist_tile_generator_encoding_ratio, this.hist_tile_generator_encoding_runLen, this.hist_str_polystack_curves, this.hist_cpd_polystack_curves, this.hist_pcf_idxstack_indices, this.stat_array_dasher_dasher, this.stat_array_dasher_firstSegmentsBuffer, this.stat_array_marlincache_rowAAChunk, this.stat_array_marlincache_touchedTile, this.stat_array_renderer_alphaline, this.stat_array_renderer_crossings, this.stat_array_renderer_aux_crossings, this.stat_array_renderer_edgeBuckets, this.stat_array_renderer_edgeBucketCounts, this.stat_array_renderer_edgePtrs, this.stat_array_renderer_aux_edgePtrs, this.stat_array_str_polystack_curves, this.stat_array_str_polystack_types, this.stat_array_cpd_polystack_curves, this.stat_array_cpd_polystack_types, this.stat_array_pcf_idxstack_indices};
    final Monitor mon_pre_getAATileGenerator = new Monitor("MarlinRenderingEngine.getAATileGenerator()");
    final Monitor mon_rdr_addLine = new Monitor("Renderer.addLine()");
    final Monitor mon_rdr_endRendering = new Monitor("Renderer.endRendering()");
    final Monitor mon_rdr_endRendering_Y = new Monitor("Renderer._endRendering(Y)");
    final Monitor mon_rdr_copyAARow = new Monitor("Renderer.copyAARow()");
    final Monitor mon_pipe_renderTiles = new Monitor("AAShapePipe.renderTiles()");
    final Monitor mon_ptg_getAlpha = new Monitor("MarlinTileGenerator.getAlpha()");
    final Monitor mon_debug = new Monitor("DEBUG()");
    final Monitor[] monitors = new Monitor[]{this.mon_pre_getAATileGenerator, this.mon_rdr_addLine, this.mon_rdr_endRendering, this.mon_rdr_endRendering_Y, this.mon_rdr_copyAARow, this.mon_pipe_renderTiles, this.mon_ptg_getAlpha, this.mon_debug};
    long totalOffHeapInitial = 0L;
    long totalOffHeap = 0L;
    long totalOffHeapMax = 0L;
    ArrayCacheConst.CacheStats[] cacheStats = null;

    static RendererStats createInstance(Object object, String string) {
        RendererStats rendererStats = new RendererStats(string);
        RendererStatsHolder.getInstance().add(object, rendererStats);
        return rendererStats;
    }

    public static void dumpStats() {
        RendererStatsHolder.dumpStats();
    }

    private RendererStats(String string) {
        this.name = string;
    }

    void dump() {
        MarlinUtils.logInfo("RendererContext: " + this.name);
        if (DO_STATS) {
            for (StatLong statLong : this.statistics) {
                if (statLong.count == 0L) continue;
                MarlinUtils.logInfo(statLong.toString());
                statLong.reset();
            }
            MarlinUtils.logInfo("OffHeap footprint: initial: " + this.totalOffHeapInitial + " bytes - max: " + this.totalOffHeapMax + " bytes");
            this.totalOffHeapMax = 0L;
            MarlinUtils.logInfo("Array caches for RendererContext: " + this.name);
            long l = this.totalOffHeapInitial;
            long l2 = 0L;
            if (this.cacheStats != null) {
                for (ArrayCacheConst.CacheStats cacheStats : this.cacheStats) {
                    l2 += cacheStats.dumpStats();
                    l += cacheStats.getTotalInitialBytes();
                    cacheStats.reset();
                }
            }
            MarlinUtils.logInfo("Heap footprint: initial: " + l + " bytes - cache: " + l2 + " bytes");
        }
    }

    static final class RendererStatsHolder {
        private static volatile RendererStatsHolder SINGLETON = null;
        private final ConcurrentLinkedQueue<RendererStats> allStats = new ConcurrentLinkedQueue();

        static synchronized RendererStatsHolder getInstance() {
            if (SINGLETON == null) {
                SINGLETON = new RendererStatsHolder();
            }
            return SINGLETON;
        }

        static void dumpStats() {
            if (SINGLETON != null) {
                SINGLETON.dump();
            }
        }

        private RendererStatsHolder() {
            AccessController.doPrivileged(() -> {
                Thread thread = new Thread(MarlinUtils.getRootThreadGroup(), new Runnable(){

                    @Override
                    public void run() {
                        this.dump();
                    }
                }, "MarlinStatsHook");
                thread.setContextClassLoader(null);
                Runtime.getRuntime().addShutdownHook(thread);
                return null;
            });
        }

        void add(Object object, RendererStats rendererStats) {
            this.allStats.add(rendererStats);
            MarlinUtils.getCleaner().register(object, () -> this.remove(rendererStats));
        }

        void remove(RendererStats rendererStats) {
            rendererStats.dump();
            this.allStats.remove(rendererStats);
        }

        void dump() {
            for (RendererStats rendererStats : this.allStats) {
                rendererStats.dump();
            }
        }
    }
}

