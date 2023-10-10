// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

public interface LoggerConfigAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
    
    String getName();
    
    String getLevel();
    
    void setLevel(final String level);
    
    boolean isAdditive();
    
    void setAdditive(final boolean additive);
    
    boolean isIncludeLocation();
    
    String getFilter();
    
    String[] getAppenderRefs();
}
