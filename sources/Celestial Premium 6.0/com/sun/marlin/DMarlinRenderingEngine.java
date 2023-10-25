/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinProperties;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.Renderer;
import com.sun.marlin.RendererContext;
import com.sun.marlin.Version;
import com.sun.util.reentrant.ReentrantContextProvider;
import com.sun.util.reentrant.ReentrantContextProviderCLQ;
import com.sun.util.reentrant.ReentrantContextProviderTL;
import java.security.AccessController;

public final class DMarlinRenderingEngine
implements MarlinConst {
    private static final boolean USE_THREAD_LOCAL;
    static final int REF_TYPE;
    private static final ReentrantContextProvider<RendererContext> RDR_CTX_PROVIDER;
    private static boolean SETTINGS_LOGGED;

    private DMarlinRenderingEngine() {
    }

    public static void logSettings(String string) {
        String string2;
        if (SETTINGS_LOGGED) {
            return;
        }
        SETTINGS_LOGGED = true;
        switch (REF_TYPE) {
            default: {
                string2 = "hard";
                break;
            }
            case 1: {
                string2 = "soft";
                break;
            }
            case 2: {
                string2 = "weak";
            }
        }
        MarlinUtils.logInfo("===============================================================================");
        MarlinUtils.logInfo("Marlin software rasterizer    = ENABLED");
        MarlinUtils.logInfo("Version                       = [" + Version.getVersion() + "]");
        MarlinUtils.logInfo("prism.marlin                  = " + string);
        MarlinUtils.logInfo("prism.marlin.useThreadLocal   = " + USE_THREAD_LOCAL);
        MarlinUtils.logInfo("prism.marlin.useRef           = " + string2);
        MarlinUtils.logInfo("prism.marlin.edges            = " + MarlinConst.INITIAL_EDGES_COUNT);
        MarlinUtils.logInfo("prism.marlin.pixelWidth       = " + MarlinConst.INITIAL_PIXEL_WIDTH);
        MarlinUtils.logInfo("prism.marlin.pixelHeight      = " + MarlinConst.INITIAL_PIXEL_HEIGHT);
        MarlinUtils.logInfo("prism.marlin.profile          = " + (MarlinProperties.isProfileQuality() ? "quality" : "speed"));
        MarlinUtils.logInfo("prism.marlin.subPixel_log2_X  = " + MarlinConst.SUBPIXEL_LG_POSITIONS_X);
        MarlinUtils.logInfo("prism.marlin.subPixel_log2_Y  = " + MarlinConst.SUBPIXEL_LG_POSITIONS_Y);
        MarlinUtils.logInfo("prism.marlin.blockSize_log2   = " + MarlinConst.BLOCK_SIZE_LG);
        MarlinUtils.logInfo("prism.marlin.forceRLE         = " + MarlinProperties.isForceRLE());
        MarlinUtils.logInfo("prism.marlin.forceNoRLE       = " + MarlinProperties.isForceNoRLE());
        MarlinUtils.logInfo("prism.marlin.useTileFlags     = " + MarlinProperties.isUseTileFlags());
        MarlinUtils.logInfo("prism.marlin.useTileFlags.useHeuristics = " + MarlinProperties.isUseTileFlagsWithHeuristics());
        MarlinUtils.logInfo("prism.marlin.rleMinWidth      = " + MarlinConst.RLE_MIN_WIDTH);
        MarlinUtils.logInfo("prism.marlin.useSimplifier    = " + MarlinConst.USE_SIMPLIFIER);
        MarlinUtils.logInfo("prism.marlin.usePathSimplifier= " + MarlinConst.USE_PATH_SIMPLIFIER);
        MarlinUtils.logInfo("prism.marlin.pathSimplifier.pixTol = " + MarlinProperties.getPathSimplifierPixelTolerance());
        MarlinUtils.logInfo("prism.marlin.clip             = " + MarlinProperties.isDoClip());
        MarlinUtils.logInfo("prism.marlin.clip.runtime.enable = " + MarlinProperties.isDoClipRuntimeFlag());
        MarlinUtils.logInfo("prism.marlin.clip.subdivider  = " + MarlinProperties.isDoClipSubdivider());
        MarlinUtils.logInfo("prism.marlin.clip.subdivider.minLength = " + MarlinProperties.getSubdividerMinLength());
        MarlinUtils.logInfo("prism.marlin.doStats          = " + MarlinConst.DO_STATS);
        MarlinUtils.logInfo("prism.marlin.doMonitors       = false");
        MarlinUtils.logInfo("prism.marlin.doChecks         = " + MarlinConst.DO_CHECKS);
        MarlinUtils.logInfo("prism.marlin.log              = " + MarlinConst.ENABLE_LOGS);
        MarlinUtils.logInfo("prism.marlin.useLogger        = " + MarlinConst.USE_LOGGER);
        MarlinUtils.logInfo("prism.marlin.logCreateContext = " + MarlinConst.LOG_CREATE_CONTEXT);
        MarlinUtils.logInfo("prism.marlin.logUnsafeMalloc  = " + MarlinConst.LOG_UNSAFE_MALLOC);
        MarlinUtils.logInfo("prism.marlin.curve_len_err    = " + MarlinProperties.getCurveLengthError());
        MarlinUtils.logInfo("prism.marlin.cubic_dec_d2     = " + MarlinProperties.getCubicDecD2());
        MarlinUtils.logInfo("prism.marlin.cubic_inc_d1     = " + MarlinProperties.getCubicIncD1());
        MarlinUtils.logInfo("prism.marlin.quad_dec_d2      = " + MarlinProperties.getQuadDecD2());
        MarlinUtils.logInfo("Renderer settings:");
        MarlinUtils.logInfo("CUB_DEC_BND  = " + Renderer.CUB_DEC_BND);
        MarlinUtils.logInfo("CUB_INC_BND  = " + Renderer.CUB_INC_BND);
        MarlinUtils.logInfo("QUAD_DEC_BND = " + Renderer.QUAD_DEC_BND);
        MarlinUtils.logInfo("INITIAL_EDGES_CAPACITY        = " + MarlinConst.INITIAL_EDGES_CAPACITY);
        MarlinUtils.logInfo("INITIAL_CROSSING_COUNT        = " + Renderer.INITIAL_CROSSING_COUNT);
        MarlinUtils.logInfo("===============================================================================");
    }

    public static RendererContext getRendererContext() {
        RendererContext rendererContext = RDR_CTX_PROVIDER.acquire();
        return rendererContext;
    }

    public static void returnRendererContext(RendererContext rendererContext) {
        rendererContext.dispose();
        RDR_CTX_PROVIDER.release(rendererContext);
    }

    static {
        String string;
        USE_THREAD_LOCAL = MarlinProperties.isUseThreadLocal();
        switch (string = AccessController.doPrivileged(() -> {
            String string = System.getProperty("prism.marlin.useRef");
            return string == null ? "soft" : string;
        })) {
            default: {
                REF_TYPE = 1;
                break;
            }
            case "weak": {
                REF_TYPE = 2;
                break;
            }
            case "hard": {
                REF_TYPE = 0;
            }
        }
        RDR_CTX_PROVIDER = USE_THREAD_LOCAL ? new ReentrantContextProviderTL<RendererContext>(REF_TYPE){

            @Override
            protected RendererContext newContext() {
                return RendererContext.createContext();
            }
        } : new ReentrantContextProviderCLQ<RendererContext>(REF_TYPE){

            @Override
            protected RendererContext newContext() {
                return RendererContext.createContext();
            }
        };
        DMarlinRenderingEngine.logSettings(Renderer.class.getName());
        SETTINGS_LOGGED = !ENABLE_LOGS;
    }
}

