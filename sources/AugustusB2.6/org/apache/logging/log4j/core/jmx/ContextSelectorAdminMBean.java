// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

public interface ContextSelectorAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=ContextSelector";
    
    String getImplementationClassName();
}
