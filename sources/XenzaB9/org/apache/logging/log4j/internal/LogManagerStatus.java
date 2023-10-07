// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.internal;

public class LogManagerStatus
{
    private static boolean initialized;
    
    public static void setInitialized(final boolean managerStatus) {
        LogManagerStatus.initialized = managerStatus;
    }
    
    public static boolean isInitialized() {
        return LogManagerStatus.initialized;
    }
    
    static {
        LogManagerStatus.initialized = false;
    }
}
