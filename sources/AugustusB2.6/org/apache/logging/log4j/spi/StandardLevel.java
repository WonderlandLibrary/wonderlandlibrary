// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Iterator;
import java.util.EnumSet;

public enum StandardLevel
{
    OFF(0), 
    FATAL(100), 
    ERROR(200), 
    WARN(300), 
    INFO(400), 
    DEBUG(500), 
    TRACE(600), 
    ALL(Integer.MAX_VALUE);
    
    private static final EnumSet<StandardLevel> LEVELSET;
    private final int intLevel;
    
    private StandardLevel(final int val) {
        this.intLevel = val;
    }
    
    public int intLevel() {
        return this.intLevel;
    }
    
    public static StandardLevel getStandardLevel(final int intLevel) {
        StandardLevel level = StandardLevel.OFF;
        for (final StandardLevel lvl : StandardLevel.LEVELSET) {
            if (lvl.intLevel() > intLevel) {
                break;
            }
            level = lvl;
        }
        return level;
    }
    
    static {
        LEVELSET = EnumSet.allOf(StandardLevel.class);
    }
}
