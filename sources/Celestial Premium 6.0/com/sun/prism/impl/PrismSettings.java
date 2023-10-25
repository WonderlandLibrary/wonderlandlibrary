/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 */
package com.sun.prism.impl;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.util.Utils;
import java.security.AccessController;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public final class PrismSettings {
    public static final boolean verbose;
    public static final boolean debug;
    public static final boolean trace;
    public static final boolean printAllocs;
    public static final boolean isVsyncEnabled;
    public static final boolean dirtyOptsEnabled;
    public static final boolean occlusionCullingEnabled;
    public static final boolean scrollCacheOpt;
    public static final boolean threadCheck;
    public static final boolean cacheSimpleShapes;
    public static final boolean cacheComplexShapes;
    public static final boolean useNewImageLoader;
    public static final List<String> tryOrder;
    public static final int prismStatFrequency;
    public static final RasterizerType rasterizerSpec;
    public static final String refType;
    public static final boolean forceRepaint;
    public static final boolean noFallback;
    public static final boolean showDirtyRegions;
    public static final boolean showOverdraw;
    public static final boolean printRenderGraph;
    public static final int minRTTSize;
    public static final int dirtyRegionCount;
    public static final boolean disableBadDriverWarning;
    public static final boolean forceGPU;
    public static final int maxTextureSize;
    public static final int primTextureSize;
    public static final boolean disableRegionCaching;
    public static final boolean forcePow2;
    public static final boolean noClampToZero;
    public static final boolean disableD3D9Ex;
    public static final boolean allowHiDPIScaling;
    public static final long maxVram;
    public static final long targetVram;
    public static final boolean poolStats;
    public static final boolean poolDebug;
    public static final boolean disableEffects;
    public static final int glyphCacheWidth;
    public static final int glyphCacheHeight;
    public static final String perfLog;
    public static final boolean perfLogExitFlush;
    public static final boolean perfLogFirstPaintFlush;
    public static final boolean perfLogFirstPaintExit;
    public static final boolean superShader;
    public static final boolean forceUploadingPainter;
    public static final boolean forceAlphaTestShader;
    public static final boolean forceNonAntialiasedShape;

    private PrismSettings() {
    }

    private static void printBooleanOption(boolean bl, String string) {
        if (bl) {
            System.out.println(string);
        } else {
            System.out.print("Not ");
            System.out.print(Character.toLowerCase(string.charAt(0)));
            System.out.println(string.substring(1));
        }
    }

    private static int parseInt(String string, int n, int n2, String string2) {
        return "true".equalsIgnoreCase(string) ? n2 : PrismSettings.parseInt(string, n, string2);
    }

    private static int parseInt(String string, int n, String string2) {
        block3: {
            if (string != null) {
                try {
                    return Integer.parseInt(string);
                }
                catch (Exception exception) {
                    if (string2 == null) break block3;
                    System.err.println(string2);
                }
            }
        }
        return n;
    }

    private static long parseLong(String string, long l, long l2, String string2) {
        block15: {
            if (string != null && string.length() > 0) {
                long l3 = 1L;
                if (string.endsWith("%")) {
                    if (l2 > 0L) {
                        try {
                            string = string.substring(0, string.length() - 1);
                            double d = Double.parseDouble(string);
                            if (d >= 0.0 && d <= 100.0) {
                                return Math.round((double)l2 * d / 100.0);
                            }
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    if (string2 != null) {
                        System.err.println(string2);
                    }
                    return l;
                }
                if (string.endsWith("k") || string.endsWith("K")) {
                    l3 = 1024L;
                } else if (string.endsWith("m") || string.endsWith("M")) {
                    l3 = 0x100000L;
                } else if (string.endsWith("g") || string.endsWith("G")) {
                    l3 = 0x40000000L;
                }
                if (l3 > 1L) {
                    string = string.substring(0, string.length() - 1);
                }
                try {
                    return Long.parseLong(string) * l3;
                }
                catch (Exception exception) {
                    if (string2 == null) break block15;
                    System.err.println(string2);
                }
            }
        }
        return l;
    }

    private static String[] split(String string, String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, string2);
        String[] arrstring = new String[stringTokenizer.countTokens()];
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            arrstring[n++] = stringTokenizer.nextToken();
        }
        return arrstring;
    }

    private static boolean getBoolean(Properties properties, String string, boolean bl) {
        String string2 = properties.getProperty(string);
        return string2 != null ? Boolean.parseBoolean(string2) : bl;
    }

    private static boolean getBoolean(Properties properties, String string, boolean bl, boolean bl2) {
        String string2 = properties.getProperty(string);
        if (string2 != null && string2.length() == 0) {
            return bl2;
        }
        return string2 != null ? Boolean.parseBoolean(string2) : bl;
    }

    private static int getInt(Properties properties, String string, int n, int n2, String string2) {
        return PrismSettings.parseInt(properties.getProperty(string), n, n2, string2);
    }

    private static int getInt(Properties properties, String string, int n, String string2) {
        return PrismSettings.parseInt(properties.getProperty(string), n, string2);
    }

    private static long getLong(Properties properties, String string, long l, String string2) {
        return PrismSettings.parseLong(properties.getProperty(string), l, 0L, string2);
    }

    private static long getLong(Properties properties, String string, long l, long l2, String string2) {
        return PrismSettings.parseLong(properties.getProperty(string), l, l2, string2);
    }

    static {
        int n;
        Object object;
        Properties properties = (Properties)AccessController.doPrivileged(() -> System.getProperties());
        isVsyncEnabled = PrismSettings.getBoolean(properties, "prism.vsync", true) && !PrismSettings.getBoolean(properties, "javafx.animation.fullspeed", false);
        dirtyOptsEnabled = PrismSettings.getBoolean(properties, "prism.dirtyopts", true);
        occlusionCullingEnabled = dirtyOptsEnabled && PrismSettings.getBoolean(properties, "prism.occlusion.culling", true);
        dirtyRegionCount = Utils.clamp(0, PrismSettings.getInt(properties, "prism.dirtyregioncount", 6, null), 15);
        scrollCacheOpt = PrismSettings.getBoolean(properties, "prism.scrollcacheopt", false);
        threadCheck = PrismSettings.getBoolean(properties, "prism.threadcheck", false);
        showDirtyRegions = PrismSettings.getBoolean(properties, "prism.showdirty", false);
        showOverdraw = PrismSettings.getBoolean(properties, "prism.showoverdraw", false);
        printRenderGraph = PrismSettings.getBoolean(properties, "prism.printrendergraph", false);
        forceRepaint = PrismSettings.getBoolean(properties, "prism.forcerepaint", false);
        noFallback = PrismSettings.getBoolean(properties, "prism.noFallback", false);
        String string = properties.getProperty("prism.cacheshapes", "complex");
        if ("all".equals(string) || "true".equals(string)) {
            cacheSimpleShapes = true;
            cacheComplexShapes = true;
        } else if ("complex".equals(string)) {
            cacheSimpleShapes = false;
            cacheComplexShapes = true;
        } else {
            cacheSimpleShapes = false;
            cacheComplexShapes = false;
        }
        useNewImageLoader = PrismSettings.getBoolean(properties, "prism.newiio", true);
        verbose = PrismSettings.getBoolean(properties, "prism.verbose", false);
        prismStatFrequency = PrismSettings.getInt(properties, "prism.printStats", 0, 1, "Try -Dprism.printStats=<true or number>");
        debug = PrismSettings.getBoolean(properties, "prism.debug", false);
        trace = PrismSettings.getBoolean(properties, "prism.trace", false);
        printAllocs = PrismSettings.getBoolean(properties, "prism.printallocs", false);
        disableBadDriverWarning = PrismSettings.getBoolean(properties, "prism.disableBadDriverWarning", false);
        forceGPU = PrismSettings.getBoolean(properties, "prism.forceGPU", false);
        String string2 = properties.getProperty("prism.order");
        String[] arrstring = string2 != null ? PrismSettings.split(string2, ",") : (PlatformUtil.isWindows() ? new String[]{"d3d", "sw"} : (PlatformUtil.isMac() ? new String[]{"es2", "sw"} : (PlatformUtil.isIOS() ? new String[]{"es2"} : (PlatformUtil.isAndroid() ? new String[]{"es2"} : (PlatformUtil.isLinux() ? new String[]{"es2", "sw"} : new String[]{"sw"})))));
        tryOrder = List.of(arrstring);
        RasterizerType rasterizerType = null;
        String string3 = properties.getProperty("prism.rasterizerorder");
        if (string3 != null) {
            object = PrismSettings.split(string3.toLowerCase(), ",");
            n = ((String[])object).length;
            block7: for (int i = 0; i < n; ++i) {
                Object object2;
                switch (object2 = object[i]) {
                    case "marlin": 
                    case "doublemarlin": {
                        rasterizerType = RasterizerType.DoubleMarlin;
                        break block7;
                    }
                }
            }
        }
        if (rasterizerType == null) {
            rasterizerType = RasterizerType.DoubleMarlin;
        }
        rasterizerSpec = rasterizerType;
        object = properties.getProperty("prism.primtextures");
        primTextureSize = object == null ? (PlatformUtil.isEmbedded() ? -1 : 0) : (((String)object).equals("true") ? -1 : (((String)object).equals("false") ? 0 : PrismSettings.parseInt((String)object, 0, "Try -Dprism.primtextures=[true|false|<number>]")));
        refType = properties.getProperty("prism.reftype");
        forcePow2 = PrismSettings.getBoolean(properties, "prism.forcepowerof2", false);
        noClampToZero = PrismSettings.getBoolean(properties, "prism.noclamptozero", false);
        allowHiDPIScaling = PrismSettings.getBoolean(properties, "prism.allowhidpi", true);
        maxVram = PrismSettings.getLong(properties, "prism.maxvram", 0x20000000L, "Try -Dprism.maxvram=<long>[kKmMgG]");
        targetVram = PrismSettings.getLong(properties, "prism.targetvram", maxVram / 8L, maxVram, "Try -Dprism.targetvram=<long>[kKmMgG]|<double(0,100)>%");
        poolStats = PrismSettings.getBoolean(properties, "prism.poolstats", false);
        poolDebug = PrismSettings.getBoolean(properties, "prism.pooldebug", false);
        if (verbose) {
            System.out.print("Prism pipeline init order: ");
            for (String string4 : tryOrder) {
                System.out.print(string4 + " ");
            }
            System.out.println("");
            if (string3 != null) {
                System.out.println("Requested rasterizer preference order: " + string3);
            }
            System.out.println("Using " + rasterizerType);
            PrismSettings.printBooleanOption(dirtyOptsEnabled, "Using dirty region optimizations");
            if (primTextureSize == 0) {
                System.out.println("Not using texture mask for primitives");
            } else if (primTextureSize < 0) {
                System.out.println("Using system sized mask for primitives");
            } else {
                System.out.println("Using " + primTextureSize + " sized mask for primitives");
            }
            PrismSettings.printBooleanOption(forcePow2, "Forcing power of 2 sizes for textures");
            PrismSettings.printBooleanOption(!noClampToZero, "Using hardware CLAMP_TO_ZERO mode");
            PrismSettings.printBooleanOption(allowHiDPIScaling, "Opting in for HiDPI pixel scaling");
        }
        if ((n = PrismSettings.getInt(properties, "prism.maxTextureSize", 4096, "Try -Dprism.maxTextureSize=<number>")) <= 0) {
            n = Integer.MAX_VALUE;
        }
        maxTextureSize = n;
        minRTTSize = PrismSettings.getInt(properties, "prism.minrttsize", PlatformUtil.isEmbedded() ? 16 : 0, "Try -Dprism.minrttsize=<number>");
        disableRegionCaching = PrismSettings.getBoolean(properties, "prism.disableRegionCaching", false);
        disableD3D9Ex = PrismSettings.getBoolean(properties, "prism.disableD3D9Ex", false);
        disableEffects = PrismSettings.getBoolean(properties, "prism.disableEffects", false);
        glyphCacheWidth = PrismSettings.getInt(properties, "prism.glyphCacheWidth", 1024, "Try -Dprism.glyphCacheWidth=<number>");
        glyphCacheHeight = PrismSettings.getInt(properties, "prism.glyphCacheHeight", 1024, "Try -Dprism.glyphCacheHeight=<number>");
        perfLog = properties.getProperty("sun.perflog");
        perfLogExitFlush = PrismSettings.getBoolean(properties, "sun.perflog.fx.exitflush", false, true);
        perfLogFirstPaintFlush = PrismSettings.getBoolean(properties, "sun.perflog.fx.firstpaintflush", false, true);
        perfLogFirstPaintExit = PrismSettings.getBoolean(properties, "sun.perflog.fx.firstpaintexit", false, true);
        superShader = PrismSettings.getBoolean(properties, "prism.supershader", true);
        forceUploadingPainter = PrismSettings.getBoolean(properties, "prism.forceUploadingPainter", false);
        forceAlphaTestShader = PrismSettings.getBoolean(properties, "prism.forceAlphaTestShader", false);
        forceNonAntialiasedShape = PrismSettings.getBoolean(properties, "prism.forceNonAntialiasedShape", false);
    }

    public static final class RasterizerType
    extends Enum<RasterizerType> {
        public static final /* enum */ RasterizerType DoubleMarlin = new RasterizerType("Double Precision Marlin Rasterizer");
        private String publicName;
        private static final /* synthetic */ RasterizerType[] $VALUES;

        public static RasterizerType[] values() {
            return (RasterizerType[])$VALUES.clone();
        }

        public static RasterizerType valueOf(String string) {
            return Enum.valueOf(RasterizerType.class, string);
        }

        private RasterizerType(String string2) {
            this.publicName = string2;
        }

        public String toString() {
            return this.publicName;
        }

        private static /* synthetic */ RasterizerType[] $values() {
            return new RasterizerType[]{DoubleMarlin};
        }

        static {
            $VALUES = RasterizerType.$values();
        }
    }
}

