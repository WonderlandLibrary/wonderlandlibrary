// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public interface HierarchyEventListener
{
    void addAppenderEvent(final Category cat, final Appender appender);
    
    void removeAppenderEvent(final Category cat, final Appender appender);
}
