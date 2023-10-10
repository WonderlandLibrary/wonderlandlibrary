// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.LogEvent;

public interface PatternSelector
{
    public static final String ELEMENT_TYPE = "patternSelector";
    
    PatternFormatter[] getFormatters(final LogEvent event);
}
