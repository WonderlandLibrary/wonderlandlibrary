// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.logging.log4j.util.StackLocatorUtil;

public class BasicConfigurator
{
    public static void configure() {
        LogManager.reconfigure(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configure(final Appender appender) {
        LogManager.getRootLogger(StackLocatorUtil.getCallerClassLoader(2)).addAppender(appender);
    }
    
    public static void resetConfiguration() {
        LogManager.resetConfiguration(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    protected BasicConfigurator() {
    }
}
