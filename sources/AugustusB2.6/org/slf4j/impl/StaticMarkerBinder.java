// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder implements MarkerFactoryBinder
{
    public static final StaticMarkerBinder SINGLETON;
    final IMarkerFactory markerFactory;
    
    private StaticMarkerBinder() {
        this.markerFactory = new BasicMarkerFactory();
    }
    
    public static StaticMarkerBinder getSingleton() {
        return StaticMarkerBinder.SINGLETON;
    }
    
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }
    
    public String getMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }
    
    static {
        SINGLETON = new StaticMarkerBinder();
    }
}
