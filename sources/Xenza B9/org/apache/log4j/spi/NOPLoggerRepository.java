// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.Level;
import java.util.Vector;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.Category;

public final class NOPLoggerRepository implements LoggerRepository
{
    @Override
    public void addHierarchyEventListener(final HierarchyEventListener listener) {
    }
    
    @Override
    public void emitNoAppenderWarning(final Category cat) {
    }
    
    @Override
    public Logger exists(final String name) {
        return null;
    }
    
    @Override
    public void fireAddAppenderEvent(final Category logger, final Appender appender) {
    }
    
    @Override
    public Enumeration getCurrentCategories() {
        return this.getCurrentLoggers();
    }
    
    @Override
    public Enumeration getCurrentLoggers() {
        return new Vector().elements();
    }
    
    @Override
    public Logger getLogger(final String name) {
        return new NOPLogger(this, name);
    }
    
    @Override
    public Logger getLogger(final String name, final LoggerFactory factory) {
        return new NOPLogger(this, name);
    }
    
    @Override
    public Logger getRootLogger() {
        return new NOPLogger(this, "root");
    }
    
    @Override
    public Level getThreshold() {
        return Level.OFF;
    }
    
    @Override
    public boolean isDisabled(final int level) {
        return true;
    }
    
    @Override
    public void resetConfiguration() {
    }
    
    @Override
    public void setThreshold(final Level level) {
    }
    
    @Override
    public void setThreshold(final String val) {
    }
    
    @Override
    public void shutdown() {
    }
}
