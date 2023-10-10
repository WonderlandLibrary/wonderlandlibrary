// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

public interface LifeCycle
{
    State getState();
    
    void initialize();
    
    void start();
    
    void stop();
    
    boolean isStarted();
    
    boolean isStopped();
    
    public enum State
    {
        INITIALIZING, 
        INITIALIZED, 
        STARTING, 
        STARTED, 
        STOPPING, 
        STOPPED;
    }
}
