// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.varia;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;

public class LevelRangeFilter extends Filter
{
    boolean acceptOnMatch;
    Level levelMin;
    Level levelMax;
    
    @Override
    public int decide(final LoggingEvent event) {
        if (this.levelMin != null && !event.getLevel().isGreaterOrEqual(this.levelMin)) {
            return -1;
        }
        if (this.levelMax != null && event.getLevel().toInt() > this.levelMax.toInt()) {
            return -1;
        }
        if (this.acceptOnMatch) {
            return 1;
        }
        return 0;
    }
    
    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }
    
    public Level getLevelMax() {
        return this.levelMax;
    }
    
    public Level getLevelMin() {
        return this.levelMin;
    }
    
    public void setAcceptOnMatch(final boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }
    
    public void setLevelMax(final Level levelMax) {
        this.levelMax = levelMax;
    }
    
    public void setLevelMin(final Level levelMin) {
        this.levelMin = levelMin;
    }
}
