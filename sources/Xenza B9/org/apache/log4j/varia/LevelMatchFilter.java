// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.varia;

import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;

public class LevelMatchFilter extends Filter
{
    boolean acceptOnMatch;
    Level levelToMatch;
    
    public LevelMatchFilter() {
        this.acceptOnMatch = true;
    }
    
    @Override
    public int decide(final LoggingEvent event) {
        if (this.levelToMatch == null) {
            return 0;
        }
        boolean matchOccured = false;
        if (this.levelToMatch.equals(event.getLevel())) {
            matchOccured = true;
        }
        if (!matchOccured) {
            return 0;
        }
        if (this.acceptOnMatch) {
            return 1;
        }
        return -1;
    }
    
    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }
    
    public String getLevelToMatch() {
        return (this.levelToMatch == null) ? null : this.levelToMatch.toString();
    }
    
    public void setAcceptOnMatch(final boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }
    
    public void setLevelToMatch(final String level) {
        this.levelToMatch = OptionConverter.toLevel(level, null);
    }
}
