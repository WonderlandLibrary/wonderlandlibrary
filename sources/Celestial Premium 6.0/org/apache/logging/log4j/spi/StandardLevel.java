/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.EnumSet;
import java.util.Iterator;

public enum StandardLevel {
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

    private StandardLevel(int val) {
        this.intLevel = val;
    }

    public int intLevel() {
        return this.intLevel;
    }

    public static StandardLevel getStandardLevel(int intLevel) {
        StandardLevel lvl;
        StandardLevel level = OFF;
        Iterator iterator = LEVELSET.iterator();
        while (iterator.hasNext() && (lvl = (StandardLevel)((Object)iterator.next())).intLevel() <= intLevel) {
            level = lvl;
        }
        return level;
    }

    static {
        LEVELSET = EnumSet.allOf(StandardLevel.class);
    }
}

