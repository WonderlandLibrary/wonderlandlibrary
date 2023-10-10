// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder
{
    private static final StaticLoggerBinder SINGLETON;
    public static String REQUESTED_API_VERSION;
    private static final String loggerFactoryClassStr;
    private final ILoggerFactory loggerFactory;
    
    public static final StaticLoggerBinder getSingleton() {
        return StaticLoggerBinder.SINGLETON;
    }
    
    private StaticLoggerBinder() {
        this.loggerFactory = new SimpleLoggerFactory();
    }
    
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }
    
    public String getLoggerFactoryClassStr() {
        return StaticLoggerBinder.loggerFactoryClassStr;
    }
    
    static {
        SINGLETON = new StaticLoggerBinder();
        StaticLoggerBinder.REQUESTED_API_VERSION = "1.6.99";
        loggerFactoryClassStr = SimpleLoggerFactory.class.getName();
    }
}
