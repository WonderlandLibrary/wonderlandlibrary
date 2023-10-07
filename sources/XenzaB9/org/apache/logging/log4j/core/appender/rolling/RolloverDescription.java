// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.action.Action;

public interface RolloverDescription
{
    String getActiveFileName();
    
    boolean getAppend();
    
    Action getSynchronous();
    
    Action getAsynchronous();
}
