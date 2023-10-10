// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

public interface AsyncAppenderAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
    
    String getName();
    
    String getLayout();
    
    boolean isIgnoreExceptions();
    
    String getErrorHandler();
    
    String getFilter();
    
    String[] getAppenderRefs();
    
    boolean isIncludeLocation();
    
    boolean isBlocking();
    
    String getErrorRef();
    
    int getQueueCapacity();
    
    int getQueueRemainingCapacity();
}
