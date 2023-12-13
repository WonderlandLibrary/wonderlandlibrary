// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

public interface LoggerRepository
{
    void addHierarchyEventListener(final HierarchyEventListener listener);
    
    boolean isDisabled(final int level);
    
    void setThreshold(final Level level);
    
    void setThreshold(final String val);
    
    void emitNoAppenderWarning(final Category cat);
    
    Level getThreshold();
    
    Logger getLogger(final String name);
    
    Logger getLogger(final String name, final LoggerFactory factory);
    
    Logger getRootLogger();
    
    Logger exists(final String name);
    
    void shutdown();
    
    Enumeration getCurrentLoggers();
    
    Enumeration getCurrentCategories();
    
    void fireAddAppenderEvent(final Category logger, final Appender appender);
    
    void resetConfiguration();
}
