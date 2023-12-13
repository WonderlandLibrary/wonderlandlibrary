// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;

public class UtilLoggingLevel extends Level
{
    private static final long serialVersionUID = 909301162611820211L;
    public static final int SEVERE_INT = 22000;
    public static final int WARNING_INT = 21000;
    public static final int CONFIG_INT = 14000;
    public static final int FINE_INT = 13000;
    public static final int FINER_INT = 12000;
    public static final int FINEST_INT = 11000;
    public static final int UNKNOWN_INT = 10000;
    public static final UtilLoggingLevel SEVERE;
    public static final UtilLoggingLevel WARNING;
    public static final UtilLoggingLevel INFO;
    public static final UtilLoggingLevel CONFIG;
    public static final UtilLoggingLevel FINE;
    public static final UtilLoggingLevel FINER;
    public static final UtilLoggingLevel FINEST;
    
    protected UtilLoggingLevel(final int level, final String levelStr, final int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
    
    public static UtilLoggingLevel toLevel(final int val, final UtilLoggingLevel defaultLevel) {
        switch (val) {
            case 22000: {
                return UtilLoggingLevel.SEVERE;
            }
            case 21000: {
                return UtilLoggingLevel.WARNING;
            }
            case 20000: {
                return UtilLoggingLevel.INFO;
            }
            case 14000: {
                return UtilLoggingLevel.CONFIG;
            }
            case 13000: {
                return UtilLoggingLevel.FINE;
            }
            case 12000: {
                return UtilLoggingLevel.FINER;
            }
            case 11000: {
                return UtilLoggingLevel.FINEST;
            }
            default: {
                return defaultLevel;
            }
        }
    }
    
    public static Level toLevel(final int val) {
        return toLevel(val, UtilLoggingLevel.FINEST);
    }
    
    public static List getAllPossibleLevels() {
        final ArrayList<UtilLoggingLevel> list = new ArrayList<UtilLoggingLevel>();
        list.add(UtilLoggingLevel.FINE);
        list.add(UtilLoggingLevel.FINER);
        list.add(UtilLoggingLevel.FINEST);
        list.add(UtilLoggingLevel.INFO);
        list.add(UtilLoggingLevel.CONFIG);
        list.add(UtilLoggingLevel.WARNING);
        list.add(UtilLoggingLevel.SEVERE);
        return list;
    }
    
    public static Level toLevel(final String s) {
        return toLevel(s, Level.DEBUG);
    }
    
    public static Level toLevel(final String sArg, final Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }
        final String s = sArg.toUpperCase();
        if (s.equals("SEVERE")) {
            return UtilLoggingLevel.SEVERE;
        }
        if (s.equals("WARNING")) {
            return UtilLoggingLevel.WARNING;
        }
        if (s.equals("INFO")) {
            return UtilLoggingLevel.INFO;
        }
        if (s.equals("CONFIG")) {
            return UtilLoggingLevel.CONFIG;
        }
        if (s.equals("FINE")) {
            return UtilLoggingLevel.FINE;
        }
        if (s.equals("FINER")) {
            return UtilLoggingLevel.FINER;
        }
        if (s.equals("FINEST")) {
            return UtilLoggingLevel.FINEST;
        }
        return defaultLevel;
    }
    
    static {
        SEVERE = new UtilLoggingLevel(22000, "SEVERE", 0);
        WARNING = new UtilLoggingLevel(21000, "WARNING", 4);
        INFO = new UtilLoggingLevel(20000, "INFO", 5);
        CONFIG = new UtilLoggingLevel(14000, "CONFIG", 6);
        FINE = new UtilLoggingLevel(13000, "FINE", 7);
        FINER = new UtilLoggingLevel(12000, "FINER", 8);
        FINEST = new UtilLoggingLevel(11000, "FINEST", 9);
    }
}
