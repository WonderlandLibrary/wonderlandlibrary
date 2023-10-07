// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

public interface DirectFileRolloverStrategy
{
    String getCurrentFileName(final RollingFileManager manager);
    
    void clearCurrentFileName();
}
