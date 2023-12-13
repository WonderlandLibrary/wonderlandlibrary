// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.log4j.spi.LoggingEvent;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Vector;
import org.apache.log4j.Appender;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.spi.AppenderAttachable;

public class AppenderAttachableImpl implements AppenderAttachable
{
    private final ConcurrentMap<String, Appender> appenders;
    protected Vector appenderList;
    
    public AppenderAttachableImpl() {
        this.appenders = new ConcurrentHashMap<String, Appender>();
    }
    
    @Override
    public void addAppender(final Appender appender) {
        if (appender != null) {
            this.appenders.put(Objects.toString(appender.getName()), appender);
        }
    }
    
    public int appendLoopOnAppenders(final LoggingEvent event) {
        for (final Appender appender : this.appenders.values()) {
            appender.doAppend(event);
        }
        return this.appenders.size();
    }
    
    public void close() {
        for (final Appender appender : this.appenders.values()) {
            appender.close();
        }
    }
    
    @Override
    public Enumeration<Appender> getAllAppenders() {
        return Collections.enumeration(this.appenders.values());
    }
    
    @Override
    public Appender getAppender(final String name) {
        return (name == null) ? null : this.appenders.get(name);
    }
    
    @Override
    public boolean isAttached(final Appender appender) {
        return appender != null && this.appenders.containsValue(appender);
    }
    
    @Override
    public void removeAllAppenders() {
        this.appenders.clear();
    }
    
    @Override
    public void removeAppender(final Appender appender) {
        if (appender != null) {
            final String name = appender.getName();
            if (name != null) {
                this.appenders.remove(name, appender);
            }
        }
    }
    
    @Override
    public void removeAppender(final String name) {
        if (name != null) {
            this.appenders.remove(name);
        }
    }
}
