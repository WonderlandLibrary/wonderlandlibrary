// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import java.io.ObjectStreamException;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Locale;
import org.apache.log4j.helpers.OptionConverter;
import java.io.Serializable;

public class Level extends Priority implements Serializable
{
    public static final int TRACE_INT = 5000;
    public static final Level OFF;
    public static final Level FATAL;
    public static final Level ERROR;
    public static final Level WARN;
    public static final Level INFO;
    public static final Level DEBUG;
    public static final Level TRACE;
    public static final Level ALL;
    private static final long serialVersionUID = 3491141966387921974L;
    
    protected Level(final int level, final String levelStr, final int syslogEquivalent) {
        this(level, levelStr, syslogEquivalent, null);
    }
    
    protected Level(final int level, final String levelStr, final int syslogEquivalent, final org.apache.logging.log4j.Level version2Equivalent) {
        super(level, levelStr, syslogEquivalent);
        this.version2Level = ((version2Equivalent != null) ? version2Equivalent : OptionConverter.createLevel(this));
    }
    
    public static Level toLevel(final String sArg) {
        return toLevel(sArg, Level.DEBUG);
    }
    
    public static Level toLevel(final int val) {
        return toLevel(val, Level.DEBUG);
    }
    
    public static Level toLevel(final int val, final Level defaultLevel) {
        switch (val) {
            case Integer.MIN_VALUE: {
                return Level.ALL;
            }
            case 10000: {
                return Level.DEBUG;
            }
            case 20000: {
                return Level.INFO;
            }
            case 30000: {
                return Level.WARN;
            }
            case 40000: {
                return Level.ERROR;
            }
            case 50000: {
                return Level.FATAL;
            }
            case Integer.MAX_VALUE: {
                return Level.OFF;
            }
            case 5000: {
                return Level.TRACE;
            }
            default: {
                return defaultLevel;
            }
        }
    }
    
    public static Level toLevel(final String sArg, final Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }
        final String upperCase;
        final String s = upperCase = sArg.toUpperCase(Locale.ROOT);
        switch (upperCase) {
            case "ALL": {
                return Level.ALL;
            }
            case "DEBUG": {
                return Level.DEBUG;
            }
            case "INFO": {
                return Level.INFO;
            }
            case "WARN": {
                return Level.WARN;
            }
            case "ERROR": {
                return Level.ERROR;
            }
            case "FATAL": {
                return Level.FATAL;
            }
            case "OFF": {
                return Level.OFF;
            }
            case "TRACE": {
                return Level.TRACE;
            }
            default: {
                return defaultLevel;
            }
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.level = s.readInt();
        this.syslogEquivalent = s.readInt();
        this.levelStr = s.readUTF();
        if (this.levelStr == null) {
            this.levelStr = "";
        }
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.level);
        s.writeInt(this.syslogEquivalent);
        s.writeUTF(this.levelStr);
    }
    
    protected Object readResolve() throws ObjectStreamException {
        if (this.getClass() == Level.class) {
            return toLevel(this.level);
        }
        return this;
    }
    
    static {
        OFF = new Level(Integer.MAX_VALUE, "OFF", 0, org.apache.logging.log4j.Level.OFF);
        FATAL = new Level(50000, "FATAL", 0, org.apache.logging.log4j.Level.FATAL);
        ERROR = new Level(40000, "ERROR", 3, org.apache.logging.log4j.Level.ERROR);
        WARN = new Level(30000, "WARN", 4, org.apache.logging.log4j.Level.WARN);
        INFO = new Level(20000, "INFO", 6, org.apache.logging.log4j.Level.INFO);
        DEBUG = new Level(10000, "DEBUG", 7, org.apache.logging.log4j.Level.DEBUG);
        TRACE = new Level(5000, "TRACE", 7, org.apache.logging.log4j.Level.TRACE);
        ALL = new Level(Integer.MIN_VALUE, "ALL", 7, org.apache.logging.log4j.Level.ALL);
    }
}
