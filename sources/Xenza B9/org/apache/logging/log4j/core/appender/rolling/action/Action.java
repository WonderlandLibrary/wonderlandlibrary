// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;

public interface Action extends Runnable
{
    boolean execute() throws IOException;
    
    void close();
    
    boolean isComplete();
}
