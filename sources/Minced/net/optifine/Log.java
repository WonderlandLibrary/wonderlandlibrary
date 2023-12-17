// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{
    private static final Logger LOGGER;
    public static final boolean logDetail;
    
    public static void detail(final String s) {
        if (Log.logDetail) {
            Log.LOGGER.info("[OptiFine] " + s);
        }
    }
    
    public static void dbg(final String s) {
        Log.LOGGER.info("[OptiFine] " + s);
    }
    
    public static void warn(final String s) {
        Log.LOGGER.warn("[OptiFine] " + s);
    }
    
    public static void warn(final String s, final Throwable t) {
        Log.LOGGER.warn("[OptiFine] " + s, t);
    }
    
    public static void error(final String s) {
        Log.LOGGER.error("[OptiFine] " + s);
    }
    
    public static void error(final String s, final Throwable t) {
        Log.LOGGER.error("[OptiFine] " + s, t);
    }
    
    public static void log(final String s) {
        dbg(s);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        logDetail = System.getProperty("log.detail", "false").equals("true");
    }
}
