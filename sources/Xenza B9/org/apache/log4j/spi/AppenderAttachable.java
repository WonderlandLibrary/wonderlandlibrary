// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;

public interface AppenderAttachable
{
    void addAppender(final Appender newAppender);
    
    Enumeration<Appender> getAllAppenders();
    
    Appender getAppender(final String name);
    
    boolean isAttached(final Appender appender);
    
    void removeAllAppenders();
    
    void removeAppender(final Appender appender);
    
    void removeAppender(final String name);
}
