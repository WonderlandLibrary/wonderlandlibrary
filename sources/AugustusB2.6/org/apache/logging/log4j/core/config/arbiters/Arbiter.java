// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.arbiters;

public interface Arbiter
{
    public static final String ELEMENT_TYPE = "Arbiter";
    
    boolean isCondition();
}
