// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;

public class StaticMDCBinder
{
    public static final StaticMDCBinder SINGLETON;
    
    private StaticMDCBinder() {
    }
    
    public static final StaticMDCBinder getSingleton() {
        return StaticMDCBinder.SINGLETON;
    }
    
    public MDCAdapter getMDCA() {
        return new NOPMDCAdapter();
    }
    
    public String getMDCAdapterClassStr() {
        return NOPMDCAdapter.class.getName();
    }
    
    static {
        SINGLETON = new StaticMDCBinder();
    }
}
