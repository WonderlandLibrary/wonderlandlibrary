// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.util.StringBuilderFormattable;

public interface Instant extends StringBuilderFormattable
{
    long getEpochSecond();
    
    int getNanoOfSecond();
    
    long getEpochMillisecond();
    
    int getNanoOfMillisecond();
}
