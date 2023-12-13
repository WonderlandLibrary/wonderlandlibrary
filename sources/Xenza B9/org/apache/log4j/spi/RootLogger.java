// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public final class RootLogger extends Logger
{
    public RootLogger(final Level level) {
        super("root");
        this.setLevel(level);
    }
    
    public final Level getChainedLevel() {
        return this.getLevel();
    }
    
    @Override
    public final void setLevel(final Level level) {
        if (level == null) {
            LogLog.error("You have tried to set a null level to root.", new Throwable());
        }
        else {
            super.setLevel(level);
        }
    }
}
