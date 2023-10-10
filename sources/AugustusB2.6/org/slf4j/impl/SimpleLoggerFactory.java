// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;

public class SimpleLoggerFactory implements ILoggerFactory
{
    ConcurrentMap<String, Logger> loggerMap;
    
    public SimpleLoggerFactory() {
        this.loggerMap = new ConcurrentHashMap<String, Logger>();
        SimpleLogger.lazyInit();
    }
    
    public Logger getLogger(final String name) {
        final Logger simpleLogger = this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        final Logger newInstance = new SimpleLogger(name);
        final Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return (oldInstance == null) ? newInstance : oldInstance;
    }
    
    void reset() {
        this.loggerMap.clear();
    }
}
