// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.logging.log4j.Level;

public class Priority
{
    public static final int OFF_INT = Integer.MAX_VALUE;
    public static final int FATAL_INT = 50000;
    public static final int ERROR_INT = 40000;
    public static final int WARN_INT = 30000;
    public static final int INFO_INT = 20000;
    public static final int DEBUG_INT = 10000;
    public static final int ALL_INT = Integer.MIN_VALUE;
    @Deprecated
    public static final Priority FATAL;
    @Deprecated
    public static final Priority ERROR;
    @Deprecated
    public static final Priority WARN;
    @Deprecated
    public static final Priority INFO;
    @Deprecated
    public static final Priority DEBUG;
    transient int level;
    transient String levelStr;
    transient int syslogEquivalent;
    transient Level version2Level;
    
    protected Priority() {
        this.level = 10000;
        this.levelStr = "DEBUG";
        this.syslogEquivalent = 7;
    }
    
    protected Priority(final int level, final String levelStr, final int syslogEquivalent) {
        this.level = level;
        this.levelStr = levelStr;
        this.syslogEquivalent = syslogEquivalent;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Priority) {
            final Priority r = (Priority)o;
            return this.level == r.level;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.level;
    }
    
    public final int getSyslogEquivalent() {
        return this.syslogEquivalent;
    }
    
    public Level getVersion2Level() {
        return this.version2Level;
    }
    
    public boolean isGreaterOrEqual(final Priority r) {
        return this.level >= r.level;
    }
    
    @Deprecated
    public static Priority[] getAllPossiblePriorities() {
        return new Priority[] { Priority.FATAL, Priority.ERROR, org.apache.log4j.Level.WARN, Priority.INFO, Priority.DEBUG };
    }
    
    @Override
    public final String toString() {
        return this.levelStr;
    }
    
    public final int toInt() {
        return this.level;
    }
    
    @Deprecated
    public static Priority toPriority(final String sArg) {
        return org.apache.log4j.Level.toLevel(sArg);
    }
    
    @Deprecated
    public static Priority toPriority(final int val) {
        return toPriority(val, Priority.DEBUG);
    }
    
    @Deprecated
    public static Priority toPriority(final int val, final Priority defaultPriority) {
        return org.apache.log4j.Level.toLevel(val, (org.apache.log4j.Level)defaultPriority);
    }
    
    @Deprecated
    public static Priority toPriority(final String sArg, final Priority defaultPriority) {
        return org.apache.log4j.Level.toLevel(sArg, (org.apache.log4j.Level)defaultPriority);
    }
    
    static {
        FATAL = new org.apache.log4j.Level(50000, "FATAL", 0);
        ERROR = new org.apache.log4j.Level(40000, "ERROR", 3);
        WARN = new org.apache.log4j.Level(30000, "WARN", 4);
        INFO = new org.apache.log4j.Level(20000, "INFO", 6);
        DEBUG = new org.apache.log4j.Level(10000, "DEBUG", 7);
    }
}
